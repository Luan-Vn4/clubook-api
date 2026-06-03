@echo off
rem Use JDK 17 for Maven (QueryDSL apt plugin requires a full JDK).
if not defined JAVA_HOME (
  for /d %%i in ("C:\Program Files\Eclipse Adoptium\jdk-17*") do set "JAVA_HOME=%%i"
)
if not defined JAVA_HOME (
  echo ERROR: JDK 17 not found. Install Eclipse Temurin 17 or set JAVA_HOME.
  exit /b 1
)
set "PATH=%JAVA_HOME%\bin;%PATH%"
call mvnw.cmd %*
