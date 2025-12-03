@echo off

echo  停止校园贴吧系统...

REM 停止 Tomcat
echo  停止 Tomcat...
sc query Tomcat9 | findstr "STATE" | findstr "RUNNING" > nul
if %errorlevel% equ 0 (
    if exist "%CATALINA_HOME%\bin\shutdown.bat" (
        call "%CATALINA_HOME%\bin\shutdown.bat"
        timeout /t 2 > nul
        echo    Tomcat 已停止
    ) else (
        echo    未找到 Tomcat 停止脚本，请确保 CATALINA_HOME 环境变量已设置
        echo    或手动停止 Tomcat
    )
) else (
    echo    Tomcat 未在运行
)

REM 可选：停止 MySQL（默认不停止，保持注释状态）
REM echo  停止 MySQL...
REM net stop mysql

echo  停止完成！
echo.
echo 注意：MySQL 服务仍在运行（如需停止请运行: net stop mysql）
pause