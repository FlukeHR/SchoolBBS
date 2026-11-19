from django import forms
from django.contrib.auth.forms import UserCreationForm
from django.contrib.auth.models import User

from .models import Comment, Post
from .validators import MAX_ATTACHMENTS_PER_POST, validate_attachment


class MultipleFileInput(forms.ClearableFileInput):
    allow_multiple_selected = True


class MultipleFileField(forms.FileField):
    widget = MultipleFileInput

    def clean(self, data, initial=None):
        if not data:
            return []
        return [super(MultipleFileField, self).clean(item, initial) for item in data]


class SignUpForm(UserCreationForm):
    first_name = forms.CharField(label="昵称", max_length=150)

    class Meta(UserCreationForm.Meta):
        fields = ("username", "first_name")
        labels = {"username": "用户名"}


class ProfileForm(forms.ModelForm):
    class Meta:
        model = User
        fields = ("first_name",)
        labels = {"first_name": "昵称"}


class PostForm(forms.ModelForm):
    attachments = MultipleFileField(label="添加图片或资料", required=False)

    class Meta:
        model = Post
        fields = ("board", "title", "body")
        labels = {"board": "发布到版块", "title": "标题", "body": "正文"}
        widgets = {
            "title": forms.TextInput(attrs={"placeholder": "一句话说清你想聊什么"}),
            "body": forms.Textarea(
                attrs={"rows": 10, "placeholder": "补充背景、细节或你的想法……"}
            ),
        }

    def clean_attachments(self):
        uploads = self.cleaned_data.get("attachments", [])
        existing = self.instance.attachments.count() if self.instance.pk else 0
        if existing + len(uploads) > MAX_ATTACHMENTS_PER_POST:
            raise forms.ValidationError("每个帖子最多保留 5 个附件。")
        return [validate_attachment(upload) for upload in uploads]


class CommentForm(forms.ModelForm):
    class Meta:
        model = Comment
        fields = ("body",)
        labels = {"body": "回复"}
        widgets = {
            "body": forms.Textarea(
                attrs={"rows": 3, "placeholder": "写下你的回复，友善地表达不同看法……"}
            )
        }

