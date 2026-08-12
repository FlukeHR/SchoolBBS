# SchoolBBS

SchoolBBS 是一个面向校园交流的轻量论坛。项目使用 Django 服务端渲染，提供账号、版块关注、发帖、附件、评论、点赞、搜索、个人页面和管理后台。

交易、余额、充值和旧 MySQL 数据不在当前版本范围内；旧 Servlet/JSP 地址也不再兼容。

## 环境

- Python 3.12
- Django 5.2.16
- Pillow 12.3.0
- SQLite

项目没有 Node、Docker、启动脚本或外部数据库依赖。

## 本地部署

在项目根目录创建虚拟环境：

```powershell
python -m venv .venv
.\.venv\Scripts\Activate.ps1
python -m pip install -r requirements.txt
```

Linux 或 macOS 使用：

```bash
python3 -m venv .venv
source .venv/bin/activate
python -m pip install -r requirements.txt
```

初始化数据库和管理员：

```bash
python manage.py migrate
python manage.py createsuperuser
```

启动本地服务：

```bash
python manage.py runserver
```

访问地址：

- 论坛：<http://127.0.0.1:8000/>
- 管理后台：<http://127.0.0.1:8000/admin/>

首次使用时，在管理后台创建至少一个版块，普通用户才能发帖。

`runserver` 仅用于本地开发和课程演示，不是生产级常驻服务。本项目没有提供 VPS、反向代理或容器部署配置。

## 配置

设置直接读取系统环境变量，不依赖 `.env` 扩展：

- `DJANGO_SECRET_KEY`：站点密钥；本地未设置时使用仅限开发的默认值。
- `DJANGO_DEBUG`：默认为 `true`，可设为 `false`。
- `DJANGO_ALLOWED_HOSTS`：逗号分隔的主机名，默认 `localhost,127.0.0.1,testserver`。

SQLite 数据库保存在 `db.sqlite3`，上传文件保存在 `media/`；两者都不会提交到 Git。附件只能由已登录用户通过受保护的下载地址访问。

## 验证

```bash
python manage.py check
python manage.py makemigrations --check
python manage.py test
```
