$output = Join-Path (Split-Path $PSScriptRoot -Parent) "export.txt"

$utf8 = [System.Text.Encoding]::UTF8
[System.IO.File]::WriteAllText($output, "", $utf8)

Get-ChildItem -Recurse -Filter *.java | ForEach-Object {
    [System.IO.File]::AppendAllText(
        $output,
        "`r`n===== $($_.FullName) =====`r`n",
        $utf8
    )

    $content = [System.IO.File]::ReadAllText($_.FullName, $utf8)

    [System.IO.File]::AppendAllText(
        $output,
        $content + "`r`n",
        $utf8
    )
}

Write-Host "Done"