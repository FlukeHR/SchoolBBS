from pathlib import Path
from uuid import uuid4

from django.contrib.auth.models import User
from django.db import models, transaction
from django.db.models.signals import post_delete
from django.dispatch import receiver
from django.urls import reverse


class Board(models.Model):
    name = models.CharField("名称", max_length=64, unique=True)
    description = models.TextField("简介", blank=True, max_length=500)
    created_at = models.DateTimeField("创建时间", auto_now_add=True)
    followers = models.ManyToManyField(User, blank=True, related_name="followed_boards", verbose_name="关注用户")

    class Meta:
        ordering = ["name"]
        verbose_name = "版块"
        verbose_name_plural = "版块"

    def __str__(self):
        return self.name


class Post(models.Model):
    board = models.ForeignKey(Board, on_delete=models.CASCADE, related_name="posts", verbose_name="版块")
    author = models.ForeignKey(User, on_delete=models.CASCADE, related_name="posts", verbose_name="作者")
    title = models.CharField("标题", max_length=200)
    body = models.TextField("正文", max_length=20_000)
    created_at = models.DateTimeField("创建时间", auto_now_add=True)
    updated_at = models.DateTimeField("更新时间", auto_now=True)
    liked_by = models.ManyToManyField(User, blank=True, related_name="liked_posts", verbose_name="点赞用户")

    class Meta:
        ordering = ["-created_at"]
        verbose_name = "帖子"
        verbose_name_plural = "帖子"

    def __str__(self):
        return self.title

    def get_absolute_url(self):
        return reverse("post_detail", args=[self.pk])


class Comment(models.Model):
    post = models.ForeignKey(Post, on_delete=models.CASCADE, related_name="comments", verbose_name="帖子")
    author = models.ForeignKey(User, on_delete=models.CASCADE, related_name="comments", verbose_name="作者")
    body = models.TextField("评论", max_length=3_000)
    created_at = models.DateTimeField("创建时间", auto_now_add=True)
    updated_at = models.DateTimeField("更新时间", auto_now=True)
    liked_by = models.ManyToManyField(User, blank=True, related_name="liked_comments", verbose_name="点赞用户")

    class Meta:
        ordering = ["created_at"]
        verbose_name = "评论"
        verbose_name_plural = "评论"

    def __str__(self):
        return f"{self.author}: {self.body[:30]}"


def attachment_path(instance, filename):
    suffix = Path(filename.replace("\\", "/")).suffix.lower()
    return f"attachments/{uuid4().hex}{suffix}"


class Attachment(models.Model):
    post = models.ForeignKey(Post, on_delete=models.CASCADE, related_name="attachments", verbose_name="帖子")
    file = models.FileField("文件", upload_to=attachment_path)
    original_name = models.CharField("原文件名", max_length=255)
    content_type = models.CharField("文件类型", max_length=100)
    size = models.PositiveBigIntegerField("字节数")
    uploaded_at = models.DateTimeField("上传时间", auto_now_add=True)

    class Meta:
        ordering = ["uploaded_at"]
        verbose_name = "附件"
        verbose_name_plural = "附件"

    def __str__(self):
        return self.original_name

    @property
    def is_image(self):
        return self.content_type.startswith("image/")


@receiver(post_delete, sender=Attachment)
def delete_attachment_file(sender, instance, **kwargs):
    if instance.file:
        storage = instance.file.storage
        name = instance.file.name
        transaction.on_commit(lambda: storage.delete(name))
