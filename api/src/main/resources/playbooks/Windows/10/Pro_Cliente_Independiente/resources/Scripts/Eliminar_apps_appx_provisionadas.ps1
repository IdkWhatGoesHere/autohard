$appname = @(
"*Store*"
"*Zune*"
"*FeedbackHub*"
"*Xbox*"
"*Office*"
"*OneNote*"
"*Skype*"
"*Print3D*"
"*3DViewer*"
"*SolitaireCollection*"
"*CandyCrush*"
"*Sticky*"
"*Maps*"
"*Alarms*"
"*People*"
"*Comm*"
"*Mess*"
"*Camera*"
"*Bing*"
"*SoundRec*"
"*GetHelp*"
"*Phone*"
"*OneConnect*"
"*Advertising*"
"*MixedReality*"
"*Getstarted*"
"*Wallet*"
"*screensk*"
"*Game*"
"*DesktopApp*"
"*DolbyAccess*"
"*Holographic*"
"*PPIProjection*"
"*Spotify*"
)

"Eliminando Appx"

ForEach($app in $appname){
Get-AppxPackage -AllUsers $app | Remove-AppxPackage -ErrorAction SilentlyContinue
}

"Eliminando Appx Provisioned"

ForEach($app in $appname){
Get-AppxProvisionedPackage -online | Where-Object {$_.packagename -like $app} | Remove-AppxProvisionedPackage -Online
}