@echo off
cls
@echo ------------------------------------------------------------------------
@echo      CCN-STIC-599B19 ENS Cliente Windows 10 Independiente - Paso 2
@echo                               BitLocker
@echo ------------------------------------------------------------------------
@echo.
@echo  Este script modifica la configuracion de inicio de los servicios
@echo  requeridos para la implementacion de BitLocker.
@echo.
@echo  Antes de ejecutar este script asegurese que los ficheros
@echo  y scripts se encuentran en el directorio "C:\Scripts".
@echo.
@echo ------------------------------------------------------------------------

pause

set Plantilla="c:\scripts\CCN-STIC-599B19 ENS incremental portatiles categoria alta - DL - BitLocker\CCN-STIC-599B19 ENS servicios bitlocker.inf"

@echo.
@echo Configurando servicios de Windows...
@echo on
secedit /configure /quiet /db "c:\scripts\servicios_windows.sdb" /cfg %Plantilla% /overwrite /log "c:\scripts\CCN-STIC-599B19 ENS incremental portatiles categoria alta - DL - BitLocker\servicios_windows.log"
@echo off


@echo.
@echo Servicios de Windows configurados.
@echo.
@echo.
@echo ------------------------------------------------------------------
@echo      CCN-STIC-599B19  -  Paso 2  :     EJECUCION FINALIZADA 
@echo ------------------------------------------------------------------
pause
cls
