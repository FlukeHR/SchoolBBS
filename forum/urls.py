from django.urls import path

from . import views


urlpatterns = [
    path("", views.home, name="home"),
    path("accounts/signup/", views.signup, name="signup"),
    path("accounts/login/", views.SchoolLoginView.as_view(), name="login"),
    path("accounts/logout/", views.SchoolLogoutView.as_view(), name="logout"),
    path("boards/", views.board_list, name="board_list"),
    path("boards/<int:pk>/", views.board_detail, name="board_detail"),
    path("boards/<int:pk>/follow/", views.board_follow, name="board_follow"),
    path("posts/new/", views.post_create, name="post_create"),
    path("posts/<int:pk>/", views.post_detail, name="post_detail"),
    path("posts/<int:pk>/edit/", views.post_edit, name="post_edit"),
    path("posts/<int:pk>/delete/", views.post_delete, name="post_delete"),
    path("posts/<int:pk>/like/", views.post_like, name="post_like"),
    path("posts/<int:post_pk>/comments/", views.comment_add, name="comment_add"),
    path("comments/<int:pk>/edit/", views.comment_edit, name="comment_edit"),
    path("comments/<int:pk>/delete/", views.comment_delete, name="comment_delete"),
    path("comments/<int:pk>/like/", views.comment_like, name="comment_like"),
    path("attachments/<int:pk>/", views.attachment_download, name="attachment_download"),
    path("attachments/<int:pk>/delete/", views.attachment_delete, name="attachment_delete"),
    path("search/", views.search, name="search"),
    path("me/", views.profile, name="profile"),
]

