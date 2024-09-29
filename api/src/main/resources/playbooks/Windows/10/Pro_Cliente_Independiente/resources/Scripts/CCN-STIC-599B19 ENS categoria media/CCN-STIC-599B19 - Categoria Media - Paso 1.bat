@echo off
cls
@echo ------------------------------------------------------------------------
@echo     CCN-STIC-599B19 ENS Cliente Windows 10 Independiente - Paso 1
@echo                         Categoria media
@echo ------------------------------------------------------------------------
@echo.
@echo  Este script aplica la plantilla de seguridad.
@echo.
@echo  Antes de ejecutar este script asegurese que los ficheros
@echo  y scripts se encuentran en el directorio "C:\Scripts".
@echo.
@echo ------------------------------------------------------------------------

pause

set Plantilla="c:\scripts\CCN-STIC-599B19 ENS categoria media\CCN-STIC-599B19 ENS clientes independientes categoria media.inf"

@echo.
@echo Configurando plantilla de seguridad...
@echo on
secedit /configure /quiet /db "c:\scripts\plantilla_windows.sdb" /cfg %Plantilla% /overwrite /log "c:\scripts\CCN-STIC-599B19 ENS categoria media\plantilla_windows.log"
@echo off


@echo.
@echo Plantilla de seguridad configurada.
@echo.
@echo.
@echo ------------------------------------------------------------------
@echo       CCN-STIC-599B19  -  Paso 1  :     EJECUCION FINALIZADA 
@echo ------------------------------------------------------------------
pause
cls
