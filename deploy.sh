#!/bin/bash

echo "🚀 开始部署校园贴吧系统..."

# 1. 停止Tomcat
echo "📦 停止Tomcat服务..."
bash ./stop.sh

# 2. 编译项目
echo "🔨 编译项目..."
mvn clean package

if [ $? -ne 0 ]; then
    echo "❌ 编译失败！"
    exit 1
fi

# 3. 清理旧部署
echo "🗑️  清理旧的部署..."
rm -f /usr/local/tomcat9/webapps/untitled2.war
rm -rf /usr/local/tomcat9/webapps/untitled2

# 4. 复制WAR为固定上下文名 untitled2.war
echo "📂 复制WAR文件到Tomcat并命名为 untitled2.war..."
cp target/untitled2-1.0-SNAPSHOT.war /usr/local/tomcat9/webapps/untitled2.war

# 5. 启动Tomcat
echo "🌐 启动Tomcat服务..."
bash ./start.sh

echo "✅ 部署完成！"
echo ""
echo "访问地址："
echo "  - 登录页面: http://localhost:8080/untitled2/index.html"
echo "  - 管理员登录: http://localhost:8080/untitled2/adminLogin.jsp"
