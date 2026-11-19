import shutil
import tempfile
from io import BytesIO
from pathlib import Path

from django.contrib.auth.models import User
from django.core.files.uploadedfile import SimpleUploadedFile
from django.test import Client, TestCase, override_settings
from django.urls import reverse
from PIL import Image

from .forms import PostForm, ProfileForm
from .models import Attachment, Board, Comment, Post
from .validators import MAX_ATTACHMENT_SIZE


def png_upload(name="campus.png"):
    data = BytesIO()
    Image.new("RGB", (4, 4), "#167d78").save(data, "PNG")
    return SimpleUploadedFile(name, data.getvalue(), content_type="image/png")


class AuthTests(TestCase):
    def test_anonymous_visitors_are_sent_to_login(self):
        response = self.client.get(reverse("home"))
        self.assertRedirects(response, f"{reverse('login')}?next=/")
        self.assertEqual(self.client.get(reverse("signup")).status_code, 200)
        self.assertEqual(self.client.get(reverse("admin:login")).status_code, 200)

    def test_signup_hashes_password_and_logs_user_in(self):
        response = self.client.post(
            reverse("signup"),
            {"username": "student", "first_name": "小林", "password1": "LongPass!2026", "password2": "LongPass!2026"},
        )
        self.assertRedirects(response, reverse("home"))
        user = User.objects.get(username="student")
        self.assertTrue(user.check_password("LongPass!2026"))
        self.assertEqual(self.client.session["_auth_user_id"], str(user.pk))

    def test_profile_never_exposes_username(self):
        user = User.objects.create_user("fixed", password="LongPass!2026")
        self.client.force_login(user)
        self.assertNotIn("username", ProfileForm(instance=user).fields)
        self.client.post(reverse("profile"), {"first_name": "新昵称", "username": "changed"})
        user.refresh_from_db()
        self.assertEqual(user.username, "fixed")
        self.assertEqual(user.first_name, "新昵称")


class ForumTests(TestCase):
    @classmethod
    def setUpTestData(cls):
        cls.author = User.objects.create_user("author", password="LongPass!2026", first_name="作者")
        cls.other = User.objects.create_user("other", password="LongPass!2026")
        cls.admin = User.objects.create_superuser("admin", password="LongPass!2026", email="admin@example.com")
        cls.board = Board.objects.create(name="校园生活", description="日常交流")
        cls.post = Post.objects.create(board=cls.board, author=cls.author, title="开学清单", body="记得带校园卡")

    def setUp(self):
        self.client.force_login(self.author)

    def test_admin_can_manage_boards(self):
        self.client.force_login(self.admin)
        response = self.client.get(reverse("admin:forum_board_add"))
        self.assertEqual(response.status_code, 200)
        response = self.client.post(reverse("admin:forum_board_add"), {"name": "学习交流", "description": "课程讨论", "followers": []})
        self.assertEqual(response.status_code, 302)
        self.assertTrue(Board.objects.filter(name="学习交流").exists())

    def test_author_can_edit_post_but_other_user_cannot(self):
        response = self.client.post(
            reverse("post_edit", args=[self.post.pk]),
            {"board": self.board.pk, "title": "更新标题", "body": "更新正文"},
        )
        self.assertRedirects(response, self.post.get_absolute_url())
        self.post.refresh_from_db()
        self.assertEqual(self.post.title, "更新标题")

        self.client.force_login(self.other)
        response = self.client.post(reverse("post_delete", args=[self.post.pk]))
        self.assertEqual(response.status_code, 404)
        self.assertTrue(Post.objects.filter(pk=self.post.pk).exists())

    def test_comment_crud_and_permissions(self):
        response = self.client.post(reverse("comment_add", args=[self.post.pk]), {"body": "有帮助"})
        self.assertRedirects(response, self.post.get_absolute_url())
        comment = Comment.objects.get(post=self.post)

        response = self.client.post(reverse("comment_edit", args=[comment.pk]), {"body": "非常有帮助"})
        self.assertRedirects(response, self.post.get_absolute_url())
        comment.refresh_from_db()
        self.assertEqual(comment.body, "非常有帮助")

        self.client.force_login(self.other)
        self.assertEqual(self.client.post(reverse("comment_delete", args=[comment.pk])).status_code, 404)

    def test_like_and_follow_toggles_are_idempotent(self):
        self.client.post(reverse("post_like", args=[self.post.pk]))
        self.client.post(reverse("post_like", args=[self.post.pk]))
        self.assertEqual(self.post.liked_by.count(), 0)

        self.client.post(reverse("board_follow", args=[self.board.pk]))
        self.client.post(reverse("board_follow", args=[self.board.pk]))
        self.assertEqual(self.board.followers.count(), 0)

    def test_search_and_pagination(self):
        response = self.client.get(reverse("search"), {"q": "校园卡"})
        self.assertContains(response, "开学清单")
        Post.objects.bulk_create(
            [Post(board=self.board, author=self.author, title=f"帖子 {number}", body="正文") for number in range(21)]
        )
        response = self.client.get(reverse("board_detail", args=[self.board.pk]), {"page": 2})
        self.assertEqual(response.context["page_obj"].number, 2)
        self.assertEqual(len(response.context["page_obj"]), 2)

    def test_home_filters_followed_boards_and_sorts_by_activity(self):
        other_board = Board.objects.create(name="学习交流")
        other_post = Post.objects.create(
            board=other_board, author=self.other, title="课程攻略", body="如何选课"
        )
        self.board.followers.add(self.author)
        self.post.liked_by.add(self.other)

        response = self.client.get(reverse("home"), {"feed": "following"})
        self.assertContains(response, self.post.title)
        self.assertNotContains(response, other_post.title)

        response = self.client.get(reverse("home"), {"sort": "popular"})
        self.assertEqual(response.context["current_sort"], "popular")
        self.assertEqual(response.context["page_obj"][0], self.post)
        self.assertEqual(response.context["page_obj"][0].activity_count, 1)

        response = self.client.get(reverse("home"), {"feed": "unknown", "sort": "unknown"})
        self.assertEqual(response.context["current_feed"], "all")
        self.assertEqual(response.context["current_sort"], "latest")

    def test_board_quick_follow_and_preselected_post_form(self):
        response = self.client.get(reverse("board_list"))
        board = response.context["boards"].get(pk=self.board.pk)
        self.assertEqual(board.post_count, 1)
        self.assertFalse(board.is_following)

        response = self.client.post(
            reverse("board_follow", args=[self.board.pk]), {"next": reverse("board_list")}
        )
        self.assertRedirects(response, reverse("board_list"))
        response = self.client.get(reverse("board_list"))
        self.assertTrue(response.context["boards"].get(pk=self.board.pk).is_following)

        response = self.client.get(reverse("post_create"), {"board": self.board.pk})
        self.assertContains(response, f'<option value="{self.board.pk}" selected>')

    def test_pagination_preserves_feed_and_sort(self):
        Post.objects.bulk_create(
            [Post(board=self.board, author=self.author, title=f"帖子 {number}", body="正文") for number in range(21)]
        )
        response = self.client.get(
            reverse("home"), {"feed": "all", "sort": "popular", "page": 2}
        )
        self.assertContains(response, "?feed=all&amp;sort=popular&amp;page=1")

    def test_mutations_require_csrf(self):
        client = Client(enforce_csrf_checks=True)
        client.force_login(self.author)
        response = client.post(reverse("post_like", args=[self.post.pk]))
        self.assertEqual(response.status_code, 403)


class AttachmentTests(TestCase):
    def setUp(self):
        self.media_root = tempfile.mkdtemp()
        self.settings_override = override_settings(MEDIA_ROOT=self.media_root)
        self.settings_override.enable()
        self.user = User.objects.create_user("student", password="LongPass!2026")
        self.board = Board.objects.create(name="资源分享")
        self.client.force_login(self.user)

    def tearDown(self):
        self.settings_override.disable()
        shutil.rmtree(self.media_root, ignore_errors=True)

    def create_post_with_file(self, upload):
        return self.client.post(
            reverse("post_create"),
            {"board": self.board.pk, "title": "资料", "body": "请查收", "attachments": [upload]},
        )

    def test_valid_image_is_private_and_inline(self):
        response = self.create_post_with_file(png_upload())
        post = Post.objects.get(title="资料")
        self.assertRedirects(response, post.get_absolute_url())
        attachment = post.attachments.get()

        self.client.logout()
        response = self.client.get(reverse("attachment_download", args=[attachment.pk]))
        self.assertEqual(response.status_code, 302)

        self.client.force_login(self.user)
        response = self.client.get(reverse("attachment_download", args=[attachment.pk]))
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response["Content-Type"], "image/png")
        self.assertEqual(response["X-Content-Type-Options"], "nosniff")
        self.assertTrue(response["Content-Disposition"].startswith("inline"))

    def test_pdf_is_forced_to_download(self):
        pdf = SimpleUploadedFile("guide.pdf", b"%PDF-1.4\n%%EOF", content_type="application/pdf")
        self.create_post_with_file(pdf)
        attachment = Attachment.objects.get(original_name="guide.pdf")
        response = self.client.get(reverse("attachment_download", args=[attachment.pk]))
        self.assertEqual(response.status_code, 200)
        self.assertEqual(response["Content-Type"], "application/pdf")
        self.assertEqual(response["X-Content-Type-Options"], "nosniff")
        self.assertTrue(response["Content-Disposition"].startswith("attachment"))

    def test_fake_image_and_large_file_are_rejected(self):
        fake = SimpleUploadedFile("fake.jpg", b"not an image", content_type="image/jpeg")
        form = PostForm(
            {"board": self.board.pk, "title": "坏附件", "body": "正文"},
            {"attachments": fake},
        )
        self.assertFalse(form.is_valid())

        large = SimpleUploadedFile("large.txt", b"a" * (MAX_ATTACHMENT_SIZE + 1), content_type="text/plain")
        form = PostForm(
            {"board": self.board.pk, "title": "太大", "body": "正文"},
            {"attachments": large},
        )
        self.assertFalse(form.is_valid())

    def test_more_than_five_files_are_rejected(self):
        files = [SimpleUploadedFile(f"{number}.txt", b"ok", content_type="text/plain") for number in range(6)]
        form = PostForm(
            {"board": self.board.pk, "title": "太多", "body": "正文"},
            {"attachments": files},
        )
        self.assertFalse(form.is_valid())

    def test_deleting_post_removes_file_from_disk(self):
        self.create_post_with_file(png_upload())
        post = Post.objects.get(title="资料")
        attachment = post.attachments.get()
        path = Path(attachment.file.path)
        self.assertTrue(path.exists())
        with self.captureOnCommitCallbacks(execute=True):
            self.client.post(reverse("post_delete", args=[post.pk]))
        self.assertFalse(path.exists())
        self.assertFalse(Attachment.objects.filter(pk=attachment.pk).exists())

    def test_deleting_board_cascades_to_attachment_file(self):
        self.create_post_with_file(png_upload())
        attachment = Attachment.objects.get(original_name="campus.png")
        path = Path(attachment.file.path)
        with self.captureOnCommitCallbacks(execute=True):
            self.board.delete()
        self.assertFalse(path.exists())
        self.assertFalse(Attachment.objects.filter(pk=attachment.pk).exists())
