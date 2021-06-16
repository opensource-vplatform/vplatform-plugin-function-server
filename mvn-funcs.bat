@echo off
rem chcp 65001 == utf-8字符集
set setting=E:\works\toone-maven-settings.xml
echo ==================maven 仓库设置(%setting%)====================
rd /s /Q target
mkdir target 

echo =================批量打包=================
for %%i in (
    Serverfunc_ABS
    Serverfunc_GetEntityRowCountFunc
	Serverfunc_ConcatStr
) do (
	echo "处理:===%%i ===" 
	call mvn clean package -f %%i\pom.xml --settings %setting%
    call copy %%i\target\*.jar target\
)
   
del target\*-javadoc.jar
del target\*-sources.jar
pause
