@echo off
cls
@echo ------------------------------------------------------------------
@echo           Eliminar Aplicaciones (UWP) en Windows 10
@echo ------------------------------------------------------------------
@echo.
@echo  Este script elimina las aplicaciones provisionadas y el resto de
@echo  appx instaladas en el sistema.
@echo.
@echo  Debera editar el script en caso de necesitar alguna appx que ya
@echo  estuviese instalada en el equipo. Si no edita dicho script se 
@echo  eliminaran las aplicaciones (UWP) innecesarias del sistema 
@echo  operativo Windows 10.
@echo.
@echo ------------------------------------------------------------------
@echo.
pause
@echo off
c:
cd c:\scripts
@echo on
powershell.exe -executionpolicy RemoteSigned -File Eliminar_apps_appx_provisionadas.ps1
@echo off
@echo.
@echo ------------------------------------------------------------------
@echo         Eliminar Aplicaciones : Ejecucion finalizada
@echo ------------------------------------------------------------------
@echo.
pause
