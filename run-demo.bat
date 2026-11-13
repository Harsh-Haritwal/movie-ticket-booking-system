@echo off
REM The previous console demo referenced classes no longer present in this web application.
REM Run the supported embedded-Tomcat application instead.
setlocal
cd /d "%~dp0"
call "%~dp0run-web.bat"
