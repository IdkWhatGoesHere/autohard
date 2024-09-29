@echo off
@echo ---------------------------------------------------------------------
@echo       CCN-STIC-599B19  -  Deshabilitar Recuperacion Del Sistema      
@echo ---------------------------------------------------------------------
@echo.                                                                     
@echo Este script dehabilita la recuperacion del sistema para evitar      
@echo fugas de informacion y evasiones de autenticacion, tenga en cuenta  
@echo que esto provocara que no se pueda recuperar el sistema en caso de  
@echo error.
@echo.                                                              
@echo ---------------------------------------------------------------------
pause

bcdedit /set {default} bootstatuspolicy ignoreallfailures

@echo.
@echo ------------------------------------------------------------------
@echo   Deshabilitar Recuperacion Del Sistema  :  Ejecucion Finalizada 
@echo ------------------------------------------------------------------
pause
cls