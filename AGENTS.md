# SchoolBBS

SchoolBBS 是一个使用 Django 服务端渲染的校园论坛，面向本地开发和课程演示。

## 常用命令

- 安装：`python -m pip install -r requirements.txt`
- 建库：`python manage.py migrate`
- 检查：`python manage.py check`
- 测试：`python manage.py test`
- 启动：`python manage.py runserver`

## 技术与目录

- Python 3.12、Django 5.2、SQLite、Django Templates、Pillow。
- `config/` 放项目配置，`forum/` 放全部论坛业务。
- `templates/` 和 `static/` 分别放共享模板与样式；`media/` 是运行时上传目录，不进 Git。

## 开发约定

- 保持单应用；不增加 service、repository、DTO 或 JSON API 层。
- 优先使用 Django 自带认证、ORM、表单、后台、分页和安全中间件。
- 所有写操作使用 POST、CSRF 和对象级权限检查；用户名不可编辑。
- 上传文件必须校验数量、大小和真实格式，并只能经登录保护的下载视图访问。
- 数据模型变更必须提交迁移，并通过 `makemigrations --check` 与测试。
- 不新增启动、停止、部署脚本；实际运行方式只维护在 README。

## 当前状态

- Java/JSP 旧实现已由 Django 全量替换；不迁移旧 MySQL 数据。
- 范围仅含论坛主线，不含交易、余额、充值或购买记录。
- Django 检查、迁移漂移检查、测试和响应式页面烟测均已通过。
- Git 历史清场必须等结果汇报后的明确确认。
