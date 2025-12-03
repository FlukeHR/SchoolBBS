#!/bin/bash

echo " 开始部署校园贴吧系统..."

# 1. 停止Tomcat
echo " 停止Tomcat服务..."
bash ./stop.sh

# 2. 编译项目
echo "🔨 编译项目..."
mvn clean package

if [ $? -ne 0 ]; then
    echo " 编译失败！"
    exit 1
fi


# 4. 复制WAR为固定上下文名 ROOT.war
echo "📂 复制WAR文件到Tomcat并命名为 ROOT.war..."
cp target/untitled2-1.0-SNAPSHOT.war /usr/local/tomcat9/webapps/ROOT.war

# 5. 启动Tomcat
echo " 启动Tomcat服务..."
/usr/local/tomcat9/bin/catalina.sh start

# 6. 等待部署完成
echo "⏳ 等待应用部署..."
sleep 8

# 7. 验证部署
echo "🔍 验证部署..."
if [ -d "/usr/local/tomcat9/webapps/ROOT" ]; then
    echo "    ROOT目录已创建"
else
    echo "    ROOT目录创建失败"
    exit 1
fi


# 9. 打开浏览器
echo "打开浏览器..."
open http://localhost:8080/index.html

echo " 部署完成！"
echo ""
echo " 访问地址："
echo "  - 登录页面: http://localhost:8080/index.html"
echo "  - 管理员登录: http://localhost:8080/adminLogin.jsp"
