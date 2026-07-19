$ErrorActionPreference = "SilentlyContinue"
$b64 = Get-Content "C:\Users\CreeperCola123\.cursor\projects\d-MCDevelopment-ocelotsignmod-template-1-21-1\agent-tools\61347a09-bcfb-4e61-a969-05afdf26f4b9.txt" -Raw
$b64 = $b64.Trim()
$json = $b64 | ConvertFrom-Json
$content = $json.content -replace "`n","" -replace "`r",""
$decoded = [System.Text.Encoding]::UTF8.GetString([System.Convert]::FromBase64String($content))
$decoded | Out-File -FilePath "d:\MCDevelopment\ocelotsignmod-template-1.21.1\agent-tools\wallsign.txt" -Encoding UTF8
Write-Host "Done"
