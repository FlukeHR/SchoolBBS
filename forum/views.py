from django.contrib import messages
from django.contrib.auth import login
from django.contrib.auth.decorators import login_not_required
from django.contrib.auth.views import LoginView, LogoutView
from django.core.paginator import Paginator
from django.db.models import Q
from django.http import FileResponse, Http404
from django.shortcuts import get_object_or_404, redirect, render
from django.urls import reverse_lazy
from django.utils.http import url_has_allowed_host_and_scheme
from django.views.decorators.http import require_POST

from .forms import CommentForm, PostForm, ProfileForm, SignUpForm
from .models import Attachment, Board, Comment, Post


PAGE_SIZE = 20


class SchoolLoginView(LoginView):
    template_name = "registration/login.html"
    redirect_authenticated_user = True


class SchoolLogoutView(LogoutView):
    next_page = reverse_lazy("login")


def paginated(request, queryset):
    return Paginator(queryset, PAGE_SIZE).get_page(request.GET.get("page"))


def redirect_back(request, fallback, *args):
    target = request.POST.get("next")
    if target and url_has_allowed_host_and_scheme(target, allowed_hosts={request.get_host()}):
        return redirect(target)
    return redirect(fallback, *args)


def save_attachments(post, uploads):
    for upload in uploads:
        Attachment.objects.create(
            post=post,
            file=upload,
            original_name=upload.original_name,
            content_type=upload.validated_content_type,
            size=upload.size,
        )


@login_not_required
def signup(request):
    if request.user.is_authenticated:
        return redirect("home")
    form = SignUpForm(request.POST or None)
    if request.method == "POST" and form.is_valid():
        user = form.save()
        login(request, user)
        messages.success(request, "欢迎加入校园论坛。")
        return redirect("home")
    return render(request, "registration/signup.html", {"form": form})


def home(request):
    posts = Post.objects.select_related("author", "board").prefetch_related("liked_by")
    return render(
        request,
        "forum/home.html",
        {
            "boards": request.user.followed_boards.all(),
            "page_obj": paginated(request, posts),
        },
    )


def board_list(request):
    return render(request, "forum/board_list.html", {"boards": Board.objects.all()})


def board_detail(request, pk):
    board = get_object_or_404(Board, pk=pk)
    posts = board.posts.select_related("author", "board").prefetch_related("liked_by")
    return render(
        request,
        "forum/board_detail.html",
        {"board": board, "page_obj": paginated(request, posts)},
    )


def post_detail(request, pk):
    post = get_object_or_404(
        Post.objects.select_related("author", "board").prefetch_related(
            "liked_by", "attachments", "comments__author", "comments__liked_by"
        ),
        pk=pk,
    )
    return render(request, "forum/post_detail.html", {"post": post, "comment_form": CommentForm()})


def post_create(request):
    form = PostForm(request.POST or None, request.FILES or None)
    if request.method == "POST" and form.is_valid():
        post = form.save(commit=False)
        post.author = request.user
        post.save()
        save_attachments(post, form.cleaned_data["attachments"])
        messages.success(request, "帖子已发布。")
        return redirect(post)
    return render(request, "forum/post_form.html", {"form": form, "title": "发布帖子"})


def post_edit(request, pk):
    post = get_object_or_404(Post, pk=pk)
    if request.user != post.author and not request.user.is_staff:
        raise Http404
    form = PostForm(request.POST or None, request.FILES or None, instance=post)
    if request.method == "POST" and form.is_valid():
        post = form.save()
        save_attachments(post, form.cleaned_data["attachments"])
        messages.success(request, "帖子已更新。")
        return redirect(post)
    return render(request, "forum/post_form.html", {"form": form, "post": post, "title": "编辑帖子"})


@require_POST
def post_delete(request, pk):
    post = get_object_or_404(Post, pk=pk)
    if request.user != post.author and not request.user.is_staff:
        raise Http404
    post.delete()
    messages.success(request, "帖子已删除。")
    return redirect("home")


@require_POST
def attachment_delete(request, pk):
    attachment = get_object_or_404(Attachment.objects.select_related("post__author"), pk=pk)
    if request.user != attachment.post.author and not request.user.is_staff:
        raise Http404
    post_id = attachment.post_id
    attachment.delete()
    messages.success(request, "附件已删除。")
    return redirect("post_edit", pk=post_id)


@require_POST
def comment_add(request, post_pk):
    post = get_object_or_404(Post, pk=post_pk)
    form = CommentForm(request.POST)
    if form.is_valid():
        comment = form.save(commit=False)
        comment.post = post
        comment.author = request.user
        comment.save()
        messages.success(request, "评论已发布。")
    else:
        messages.error(request, "评论不能为空且不能超过 3000 字。")
    return redirect(post)


def comment_edit(request, pk):
    comment = get_object_or_404(Comment.objects.select_related("post", "author"), pk=pk)
    if request.user != comment.author and not request.user.is_staff:
        raise Http404
    form = CommentForm(request.POST or None, instance=comment)
    if request.method == "POST" and form.is_valid():
        form.save()
        messages.success(request, "评论已更新。")
        return redirect(comment.post)
    return render(request, "forum/simple_form.html", {"form": form, "title": "编辑评论"})


@require_POST
def comment_delete(request, pk):
    comment = get_object_or_404(Comment.objects.select_related("post", "author"), pk=pk)
    if request.user != comment.author and not request.user.is_staff:
        raise Http404
    post = comment.post
    comment.delete()
    messages.success(request, "评论已删除。")
    return redirect(post)


@require_POST
def post_like(request, pk):
    post = get_object_or_404(Post, pk=pk)
    if post.liked_by.filter(pk=request.user.pk).exists():
        post.liked_by.remove(request.user)
    else:
        post.liked_by.add(request.user)
    return redirect_back(request, "post_detail", post.pk)


@require_POST
def comment_like(request, pk):
    comment = get_object_or_404(Comment, pk=pk)
    if comment.liked_by.filter(pk=request.user.pk).exists():
        comment.liked_by.remove(request.user)
    else:
        comment.liked_by.add(request.user)
    return redirect_back(request, "post_detail", comment.post_id)


@require_POST
def board_follow(request, pk):
    board = get_object_or_404(Board, pk=pk)
    if board.followers.filter(pk=request.user.pk).exists():
        board.followers.remove(request.user)
    else:
        board.followers.add(request.user)
    return redirect_back(request, "board_detail", board.pk)


def search(request):
    query = request.GET.get("q", "").strip()
    posts = Post.objects.none()
    if query:
        posts = Post.objects.select_related("author", "board").filter(
            Q(title__icontains=query) | Q(body__icontains=query)
        )
    return render(
        request,
        "forum/search.html",
        {"query": query, "page_obj": paginated(request, posts)},
    )


def profile(request):
    form = ProfileForm(request.POST or None, instance=request.user)
    if request.method == "POST" and form.is_valid():
        form.save()
        messages.success(request, "昵称已更新。")
        return redirect("profile")
    posts = request.user.posts.select_related("board", "author")
    return render(
        request,
        "forum/profile.html",
        {
            "form": form,
            "boards": request.user.followed_boards.all(),
            "page_obj": paginated(request, posts),
        },
    )


def attachment_download(request, pk):
    attachment = get_object_or_404(Attachment, pk=pk)
    try:
        response = FileResponse(
            attachment.file.open("rb"),
            as_attachment=not attachment.is_image,
            filename=attachment.original_name,
            content_type=attachment.content_type,
        )
    except FileNotFoundError as error:
        raise Http404 from error
    response["X-Content-Type-Options"] = "nosniff"
    return response

