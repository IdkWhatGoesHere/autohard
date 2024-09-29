@echo off
cls
@echo ------------------------------------------------------------------
@echo CCN-STIC-599B19 ENS Cliente Windows 10 Configuracion de BitLocker                        
@echo ------------------------------------------------------------------
@echo.
@echo  Este script copia el objeto de politica local a su ubicacion
@echo  en la carpeta "C:\Windows\System32\grouppolicy".
@echo.
@echo  Esta politica combina la configuracion local de cliente Windows 
@echo  10 independiente, la establecida por el ENS y las opciones de 
@echo  configuracion de seguridad de BitLocker.
@echo.
@echo ------------------------------------------------------------------

pause

@echo off
%systemroot%\system32\xcopy /E /H /R /I /Y "c:\Scripts\CCN-STIC-599B19 ENS incremental portatiles categoria alta - DL - BitLocker\group policy" c:\windows\system32\grouppolicy

@echo off

@echo.
@echo ------------------------------------------------------------------
@echo      CCN-STIC-599B19  -  BitLocker  :     EJECUCION FINALIZADA 
@echo ------------------------------------------------------------------
pause
cls
