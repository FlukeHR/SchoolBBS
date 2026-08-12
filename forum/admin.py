from django.contrib import admin

from .models import Attachment, Board, Comment, Post


@admin.register(Board)
class BoardAdmin(admin.ModelAdmin):
    list_display = ("name", "created_at")
    search_fields = ("name", "description")
    filter_horizontal = ("followers",)


class AttachmentInline(admin.TabularInline):
    model = Attachment
    extra = 0
    fields = ("original_name", "content_type", "size", "uploaded_at")
    readonly_fields = fields

    def has_add_permission(self, request, obj=None):
        return False


@admin.register(Post)
class PostAdmin(admin.ModelAdmin):
    list_display = ("title", "board", "author", "created_at")
    list_filter = ("board", "created_at")
    search_fields = ("title", "body", "author__username")
    readonly_fields = ("created_at", "updated_at")
    inlines = (AttachmentInline,)


@admin.register(Comment)
class CommentAdmin(admin.ModelAdmin):
    list_display = ("post", "author", "created_at")
    search_fields = ("body", "author__username", "post__title")
    readonly_fields = ("created_at", "updated_at")


@admin.register(Attachment)
class AttachmentAdmin(admin.ModelAdmin):
    list_display = ("original_name", "post", "content_type", "size", "uploaded_at")
    search_fields = ("original_name", "post__title")
    readonly_fields = ("original_name", "content_type", "size", "uploaded_at")

    def has_add_permission(self, request):
        return False

    def has_change_permission(self, request, obj=None):
        return False


admin.site.site_header = "SchoolBBS 管理后台"
admin.site.site_title = "SchoolBBS"
admin.site.index_title = "论坛管理"
