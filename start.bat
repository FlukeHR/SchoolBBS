@echo off

echo  启动校园贴吧系统...

REM 检查并启动 MySQL
echo  检查 MySQL 服务...
sc query mysql | findstr "STATE" | findstr "RUNNING" > nul
if %errorlevel% neq 0 (
    echo    启动 MySQL...
    net start mysql
    timeout /t 3 > nul
) else (
    echo    MySQL 已在运行
)

REM 检查并启动 Tomcat
echo  检查 Tomcat 服务...
sc query Tomcat9 | findstr "STATE" | findstr "RUNNING" > nul
if %errorlevel% neq 0 (
    echo    启动 Tomcat...
    if exist "%CATALINA_HOME%\bin\startup.bat" (
        call "%CATALINA_HOME%\bin\startup.bat"
    ) else (
        echo    未找到 Tomcat 启动脚本，请确保 CATALINA_HOME 环境变量已设置
        echo    或手动启动 Tomcat
        pause
        exit /b 1
    )
    echo    等待应用部署...
    timeout /t 5 > nul
) else (
    echo    Tomcat 已在运行
)

REM 打开浏览器
echo 🌍 打开浏览器...
start http://localhost:8080/index.html

echo ✅ 启动完成！
echo.
echo 访问地址：
echo   - 登录页面: http://localhost:8080/index.html
echo   - 管理员登录: http://localhost:8080/adminLogin.jsp
echo.
echo 停止服务请运行: stop.bat
pause