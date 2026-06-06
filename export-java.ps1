$output = Join-Path (Split-Path $PSScriptRoot -Parent) "export.txt"

Get-ChildItem -Recurse -Include *.java |
ForEach-Object {
    "`n===== $($_.FullName) =====`n" | Out-File $output -Append
    Get-Content $_.FullName | Out-File $output -Append
}

Write-Host "Done"