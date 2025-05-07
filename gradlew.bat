@echo off
rem -----------------------------------------------------------------------------
rem Gradle startup script for Windows
rem -----------------------------------------------------------------------------

setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%

set DEFAULT_JVM_OPTS=

rem Find java.exe
set JAVA_EXE=
if defined JAVA_HOME set JAVA_EXE=%JAVA_HOME%\bin\java.exe
if not defined JAVA_EXE set JAVA_EXE=java

rem Execute Gradle
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% -Dorg.gradle.appname=%APP_BASE_NAME% -classpath "%APP_HOME%\gradle\wrapper\gradle-wrapper.jar" org.gradle.wrapper.GradleWrapperMain %*

endlocal