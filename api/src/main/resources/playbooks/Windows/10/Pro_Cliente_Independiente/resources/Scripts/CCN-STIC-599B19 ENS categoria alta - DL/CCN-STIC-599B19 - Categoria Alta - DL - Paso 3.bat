@echo off
cls
@echo ------------------------------------------------------------------------
@echo     CCN-STIC-599B19 ENS Cliente Windows 10 Independiente - Paso 3
@echo                         Categoria alta - DL
@echo ------------------------------------------------------------------------
@echo.
@echo  Este script modifica la configuracion de inicio de los servicios
@echo  requeridos para la seguridad del sistema.
@echo.
@echo  Antes de ejecutar este script asegurese que los ficheros
@echo  y scripts se encuentran en el directorio "C:\Scripts".
@echo.
@echo ------------------------------------------------------------------------

pause

set Plantilla="c:\scripts\CCN-STIC-599B19 ENS categoria alta - DL\CCN-STIC-599B19 ENS Servicios categoria alta - DL.inf"

@echo.
@echo Configurando servicios de Windows...
@echo on
secedit /configure /quiet /db "c:\scripts\servicios_windows.sdb" /cfg %Plantilla% /overwrite /log "c:\scripts\CCN-STIC-599B19 ENS categoria alta - DL\servicios_windows.log"
@echo off


@echo.
@echo Servicios de Windows configurados.
@echo.
@echo.
@echo ------------------------------------------------------------------
@echo       CCN-STIC-599B19  -  Paso 3  :     EJECUCION FINALIZADA 
@echo ------------------------------------------------------------------
pause
cls
