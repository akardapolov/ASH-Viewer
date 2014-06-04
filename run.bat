@REM ----------------------------------------------------------------------------
@REM Licensed to the GNU GENERAL PUBLIC LICENSE Version 3
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM ASH Viewer start up batch script
@REM
@REM Required ENV vars:
@REM JAVA_HOME - location of a JDK home dir
@REM  -jar ASHV.jar

SET JAVA_HOME=C:\Program Files\Java\jdk1.5.0_16
                
SET JAVA_EXE="%JAVA_HOME%\bin\java.exe"

%JAVA_EXE% -Xmx128m -Doracle.net.tns_admin=<<path to tnsnames.ora>> -jar ASHV.jar