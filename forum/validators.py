from io import BytesIO
from pathlib import Path
from zipfile import BadZipFile, ZipFile, is_zipfile

from django.core.exceptions import ValidationError
from PIL import Image, UnidentifiedImageError


MAX_ATTACHMENT_SIZE = 10 * 1024 * 1024
MAX_ATTACHMENTS_PER_POST = 5

IMAGE_FORMATS = {
    ".jpg": ("JPEG", "image/jpeg"),
    ".jpeg": ("JPEG", "image/jpeg"),
    ".png": ("PNG", "image/png"),
    ".gif": ("GIF", "image/gif"),
    ".webp": ("WEBP", "image/webp"),
}
MIME_TYPES = {
    ".pdf": "application/pdf",
    ".txt": "text/plain",
    ".doc": "application/msword",
    ".docx": "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
    ".xls": "application/vnd.ms-excel",
    ".xlsx": "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    ".ppt": "application/vnd.ms-powerpoint",
    ".pptx": "application/vnd.openxmlformats-officedocument.presentationml.presentation",
    ".zip": "application/zip",
}
OLE_MAGIC = b"\xd0\xcf\x11\xe0\xa1\xb1\x1a\xe1"
OFFICE_FOLDERS = {".docx": "word/", ".xlsx": "xl/", ".pptx": "ppt/"}


def validate_attachment(upload):
    if upload.size > MAX_ATTACHMENT_SIZE:
        raise ValidationError("单个附件不能超过 10MB。")

    name = Path(upload.name.replace("\\", "/")).name
    suffix = Path(name).suffix.lower()
    if suffix not in IMAGE_FORMATS and suffix not in MIME_TYPES:
        raise ValidationError("不支持这种附件格式。")

    upload.seek(0)
    data = upload.read()
    upload.seek(0)

    if suffix in IMAGE_FORMATS:
        try:
            image = Image.open(BytesIO(data))
            image.verify()
        except (UnidentifiedImageError, OSError, SyntaxError) as error:
            raise ValidationError("图片内容损坏或格式不正确。") from error
        expected_format, content_type = IMAGE_FORMATS[suffix]
        if image.format != expected_format:
            raise ValidationError("图片扩展名与实际格式不一致。")
    elif suffix == ".pdf":
        if not data.startswith(b"%PDF-"):
            raise ValidationError("PDF 扩展名与实际格式不一致。")
        content_type = MIME_TYPES[suffix]
    elif suffix == ".txt":
        try:
            data.decode("utf-8")
        except UnicodeDecodeError as error:
            raise ValidationError("TXT 附件必须使用 UTF-8 编码。") from error
        content_type = MIME_TYPES[suffix]
    elif suffix in {".doc", ".xls", ".ppt"}:
        if not data.startswith(OLE_MAGIC):
            raise ValidationError("Office 扩展名与实际格式不一致。")
        content_type = MIME_TYPES[suffix]
    else:
        if not is_zipfile(BytesIO(data)):
            raise ValidationError("压缩包或 Office 文件内容损坏。")
        if suffix in OFFICE_FOLDERS:
            try:
                with ZipFile(BytesIO(data)) as archive:
                    names = archive.namelist()
            except BadZipFile as error:
                raise ValidationError("Office 文件内容损坏。") from error
            if "[Content_Types].xml" not in names or not any(
                item.startswith(OFFICE_FOLDERS[suffix]) for item in names
            ):
                raise ValidationError("Office 扩展名与实际格式不一致。")
        content_type = MIME_TYPES[suffix]

    upload.validated_content_type = content_type
    upload.original_name = name
    return upload

