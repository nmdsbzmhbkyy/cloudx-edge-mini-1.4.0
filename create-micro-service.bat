@echo off
set p1=%1
:begin
if "%p1%"=="" (
	set /p p1=input micro-service name: 
	goto begin
)
echo create micro-service: %p1%
mvn archetype:generate ^
       -DgroupId=com.pig4cloud ^
       -DartifactId=%p1% ^
       -Dversion=1.0.0-SNAPSHOT ^
       -Dpackage=com.pig4cloud.pigx.%p1% ^
       -DarchetypeGroupId=com.pig4cloud.archetype ^
       -DarchetypeArtifactId=pigx-gen ^
       -DarchetypeVersion=3.7.0 ^
       -DarchetypeCatalog=local
echo on
