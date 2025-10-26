# 校园贴吧系统

## 项目简介
校园贴吧系统是一个专为大学生设计的在线交流平台，提供帖子发布、评论互动、用户管理和商品交易等功能，旨在为校园社区创造便捷的信息交流和资源共享环境。

## 技术栈

### 后端
- **编程语言**: Java
- **Web框架**: Servlet
- **ORM框架**: MyBatis 3.5.6
- **数据库**: MySQL 8.0
- **日志框架**: Log4j 2.20.0

### 前端
- **页面技术**: JSP、HTML5、CSS3
- **JavaScript框架**: jQuery 3.5.1
- **样式库**: 自定义CSS

### 项目管理
- **构建工具**: Maven
- **服务器**: Apache Tomcat

## 系统架构
本项目采用经典的MVC三层架构设计：

- **模型层(Model)**: 实体类(POJO)和数据访问层(Mapper)，负责数据处理
- **控制层(Controller)**: Servlet控制器，处理用户请求并调用业务逻辑
- **视图层(View)**: JSP页面，展示数据并与用户交互

## 核心功能模块

### 1. 用户管理
- 用户注册与登录
- 个人信息查看与修改
- 管理员登录与权限管理

### 2. 帖子系统
- 发布、编辑、删除帖子
- 按分类浏览帖子
- 帖子搜索功能
- 支持多媒体文件上传

### 3. 评论系统
- 对帖子进行评论
- 评论管理(管理员功能)

### 4. 搜索功能
- 按标题模糊搜索帖子
- 分页显示搜索结果

### 5. 商品交易
- 发布交易信息
- 查看个人发布的交易信息

## 项目结构

```
├── java/
│   └── com/bbs/
│       ├── controller/
│       ├── mapper/
│       ├── pojo/
│       └── service/
├── resources/
│   ├── db.properties.sample
│   ├── log4j.properties
│   ├── mybatis-config.xml
│   └── schema.sql
├── webapp/
│   ├── WEB-INF/
│   ├── css/
│   ├── images/
│   ├── js/
│   └── *.jsp
├── pom.xml
├── start.sh
└── stop.sh
```

## 数据库配置

### 数据库连接信息
⚠️ **重要安全提示**: 数据库凭据不应明文存储在代码库中

配置方式：
1. 复制 `resources/db.properties.sample` 为 `resources/db.properties`
2. 修改其中的占位符为实际值：
   - **数据库名**: forumsys
   - **用户名**: 替换 ${DB_USERNAME:your_username}
   - **密码**: 替换 ${DB_PASSWORD:your_password}
   - **驱动**: com.mysql.cj.jdbc.Driver
   - **URL**: jdbc:mysql://localhost:3306/forumsys?serverTimezone=UTC

### 主要数据表
- **user**: 用户信息表
- **post**: 帖子表
- **comment**: 评论表
- **admin**: 管理员表
- **trade**: 交易信息表

## 安装与部署

### 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Apache Tomcat 9.0+

### 部署步骤

1. **配置数据库**
   - 创建MySQL数据库：`CREATE DATABASE forumsys CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`
   - 导入数据库脚本：`resources/schema.sql`
   - 创建数据库配置文件：
     ```bash
     cp resources/db.properties.sample resources/db.properties
     # 编辑 db.properties 文件，替换占位符为实际数据库凭据
     ```

2. **构建项目**
   ```bash
   mvn clean package
   ```

3. **部署到Tomcat**
   - 将生成的WAR包复制到Tomcat的webapps目录
   - 或使用Maven插件直接部署

4. **启动服务**
   - 使用提供的启动脚本：
     ```bash
     ./start.sh
     ```
   - 或手动启动Tomcat服务

## 运行与访问

### 启动服务
```bash
./start.sh
```

### 访问地址
- **登录页面**: http://localhost:8080/index.html
- **首页**: http://localhost:8080/home.jsp
- **管理员登录**: http://localhost:8080/adminLogin.jsp

### 停止服务
```bash
./stop.sh
```

## 系统功能使用说明

### 用户功能
1. **注册与登录**
   - 访问登录页面，选择注册或直接登录
   - 注册时需填写用户名、密码等基本信息

2. **帖子操作**
   - 登录后可在顶部导航栏点击"发帖"发布新内容
   - 在帖子详情页可进行评论互动
   - 在"我的"页面可查看和管理自己发布的帖子

3. **个人资料**
   - 点击"个人资料"查看和修改个人信息

### 管理员功能
1. **管理员登录**
   - 通过管理员登录页面进入管理后台

2. **后台管理**
   - 管理用户账号
   - 审核和管理帖子
   - 管理评论内容

## 开发注意事项

### 安全配置
1. **数据库凭据管理**
   - 绝不将数据库密码提交到版本控制系统
   - 使用 `db.properties.sample` 作为配置模板
   - 在生产环境中通过环境变量或外部配置管理数据库凭据

2. **敏感信息保护**
   - User 和 Admin 实体类已配置密码字段掩码，避免在日志中泄露
   - 确保应用日志不包含敏感用户信息

### 依赖安全
1. **日志框架升级**
   - 已升级到 Log4j 2.20.0 以避免已知安全漏洞
   - 测试依赖已正确配置 scope 为 test

### 技术配置
1. **数据库连接**
   - 确保MySQL服务正常运行
   - 验证数据库配置文件中的连接信息

2. **日志配置**
   - 系统日志默认输出到控制台和log.log文件
   - 可通过log4j.properties调整日志级别和输出位置

3. **文件上传**
   - 帖子支持多媒体文件上传
   - 确保上传目录权限正确设置

## 故障排除

1. **服务无法启动**
   - 检查MySQL和Tomcat服务是否正常运行
   - 查看日志文件查找错误信息

2. **数据库连接失败**
   - 确认数据库配置正确
   - 检查MySQL服务状态

3. **页面访问异常**
   - 清除浏览器缓存
   - 检查Tomcat日志中的错误信息

## 许可证
本项目仅供学习和参考使用。

## 致谢
感谢所有为项目做出贡献的开发者。