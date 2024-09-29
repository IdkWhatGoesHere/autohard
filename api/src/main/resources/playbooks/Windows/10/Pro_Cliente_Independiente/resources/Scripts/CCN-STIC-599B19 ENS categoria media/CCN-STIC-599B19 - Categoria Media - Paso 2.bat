@echo off
cls
@echo ------------------------------------------------------------------
@echo  CCN-STIC-599B19 ENS Cliente Windows 10 Independiente - Paso 2
@echo                         Categoria media
@echo ------------------------------------------------------------------
@echo.
@echo  Este script copia el objeto de politica local a su ubicacion
@echo  en la carpeta "C:\Windows\System32\grouppolicy".
@echo.
@echo  Esta politica combina la configuracion local de seguridad del
@echo  Cliente Windows 10 independiente y la establecida para ENS.
@echo.
@echo ------------------------------------------------------------------

pause

@echo off
%systemroot%\system32\xcopy /E /H /R /I /Y "c:\Scripts\CCN-STIC-599B19 ENS categoria media\grouppolicy" c:\windows\system32\grouppolicy

@echo off

@echo.
@echo ------------------------------------------------------------------
@echo       CCN-STIC-599B19  -  Paso 2  :     EJECUCION FINALIZADA 
@echo ------------------------------------------------------------------
pause
cls
