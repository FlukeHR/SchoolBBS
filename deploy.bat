@echo off

echo  开始部署校园贴吧系统...

REM 1. 停止Tomcat
echo  停止Tomcat服务...
call stop.bat

REM 2. 编译项目
echo  编译项目...
mvn clean package

if %errorlevel% neq 0 (
    echo  编译失败！
    pause
    exit /b 1
)

REM 3. 创建Tomcat webapps目录（如果不存在）
if not defined CATALINA_HOME (
    echo  错误：未设置CATALINA_HOME环境变量
    echo 请设置CATALINA_HOME为Tomcat安装目录
    pause
    exit /b 1
)

REM 4. 复制WAR文件到Tomcat并命名为 ROOT.war
echo  复制WAR文件到Tomcat并命名为 ROOT.war...
if exist "target\untitled2-1.0-SNAPSHOT.war" (
    copy /Y "target\untitled2-1.0-SNAPSHOT.war" "%CATALINA_HOME%\webapps\ROOT.war"
    if %errorlevel% neq 0 (
        echo  复制WAR文件失败！
        pause
        exit /b 1
    )
) else (
    echo  未找到编译后的WAR文件：target\untitled2-1.0-SNAPSHOT.war
    pause
    exit /b 1
)

REM 5. 启动Tomcat
echo  启动Tomcat服务...
call "%CATALINA_HOME%\bin\startup.bat"

REM 6. 等待部署完成
echo  等待应用部署...
timeout /t 8 > nul

REM 7. 验证部署
echo  验证部署...
if exist "%CATALINA_HOME%\webapps\ROOT" (
    echo     ROOT目录已创建
) else (
    echo     ROOT目录创建失败
    pause
    exit /b 1
)

REM 8. 打开浏览器
echo  打开浏览器...
start http://localhost:8080/index.html

echo  部署完成！
echo.
echo  访问地址：
echo   - 登录页面: http://localhost:8080/index.html
echo   - 管理员登录: http://localhost:8080/adminLogin.jsp
pause