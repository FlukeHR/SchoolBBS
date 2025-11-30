# 校园贴吧系统

## 技术栈
- **后端**: Java 8+, Servlet, MyBatis, MySQL 8.0+
- **前端**: JSP, HTML5, CSS3, JavaScript, jQuery
- **部署**: Maven 3.6+, Tomcat 9.0+

## 环境要求
- Java 8+ 
- Maven 3.6+
- MySQL 8.0+
- Tomcat 9.0+

## 部署步骤

### 1. 数据库配置
```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE forumsys CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# 导入数据库脚本
mysql -u root -p forumsys < src/main/resources/schema.sql

# 配置数据库连接（编辑db.properties文件）
```

### 2. 一键部署

**Linux/macOS:**
```bash
chmod +x deploy.sh
./deploy.sh
```

**Windows:**
```cmd
deploy.bat
```

### 3. 启动服务

**Linux/macOS:**
```bash
./start.sh
```

**Windows:**
```cmd
start.bat
```

## 系统访问
- **用户入口**: http://localhost:8080
- **管理员入口**: http://localhost:8080/adminLogin.jsp

## 主要功能
- 用户注册与登录
- 帖子发布、编辑、删除
- 评论与点赞
- 二手商品交易
- 用户和内容管理

## 停止服务

**Linux/macOS:**
```bash
./stop.sh
```

**Windows:**
```cmd
stop.bat
```