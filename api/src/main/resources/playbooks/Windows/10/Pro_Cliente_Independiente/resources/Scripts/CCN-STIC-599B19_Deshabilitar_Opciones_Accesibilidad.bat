@echo off
cls
@echo ------------------------------------------------------------------
@echo     CCN-STIC-599B19  -  Deshabilitar Opciones de Accesibilidad     
@echo ------------------------------------------------------------------
@echo.                                                                 
@echo  Este script deshabilita las opciones de accesibilidad.           
@echo.                                                                 
@echo  Antes de ejecutar este script asegurese que los ficheros     
@echo  y scripts se encuentran en el directorio "C:\Scripts".             
@echo.                                                                 
@echo ------------------------------------------------------------------

pause

c:
cd c:\scripts

@echo Deshabilitando Opciones de Accesibilidad ...
regedit.exe /s Deshabilitar_Opciones_Accesibilidad.reg

@echo.
@echo ------------------------------------------------------------------
@echo   Deshabilitar Opciones de Accesibilidad  :  Ejecucion Finaliazda 
@echo ------------------------------------------------------------------
pause
cls
