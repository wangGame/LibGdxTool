@echo off
setlocal
pushd "%~dp0"
call gradlew.bat html:lazySingleHtml --console=plain %*
set "packResult=%errorlevel%"
popd
exit /b %packResult%
