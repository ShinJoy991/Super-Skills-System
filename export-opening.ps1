param(
    [string]$ProjectDir
)

# Sửa lỗi gán Encoding: Tạo UTF-8 không có BOM chuẩn .NET
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$utf8 = New-Object System.Text.UTF8Encoding($false)

$targetDir = "C:\Users\Admin\IdeaProjects\SSS"
$output = Join-Path $targetDir "export.txt"

# Tạo thư mục đích nếu chưa có
if (-not (Test-Path $targetDir)) {
    New-Item -ItemType Directory -Force -Path $targetDir | Out-Null
}

# Khởi tạo hoặc xóa trắng file export cũ (Ghi đè - Override)
[System.IO.File]::WriteAllText($output, "", $utf8)

$workspacePath = Join-Path $ProjectDir ".idea\workspace.xml"

if (Test-Path $workspacePath) {
    # Đọc nội dung file xml
    $xmlContent = [System.IO.File]::ReadAllText($workspacePath, $utf8)
    
    # Quét tất cả các đường dẫn file có trong cấu hình tab của IntelliJ
    $regex = 'file://\$PROJECT_DIR\$/([^"\s>]+)'
    $matches = [regex]::Matches($xmlContent, $regex)
    
    # Mảng để lọc trùng tab
    $processedFiles = @()

    foreach ($match in $matches) {
        $relativePath = $match.Groups[1].Value.Replace("/", "\")
        $fullPath = Join-Path $ProjectDir $relativePath
        
        # Chỉ lấy file thực tế tồn tại và chưa được xử lý ở vòng lặp trước
        if ((Test-Path $fullPath -PathType Leaf) -and ($processedFiles -notcontains $fullPath)) {
            
            # Kiểm tra xem file này có đang nằm trong danh sách Editor đang mở thực tế hay không
            if ($xmlContent -match """$($match.Groups[1].Value)""") {
                
                $processedFiles += $fullPath
                
                # 1. Ghi tiêu đề file
                [System.IO.File]::AppendAllText($output, "`r`n===== $relativePath =====`r`n", $utf8)
                
                # 2. Đọc nội dung file gốc và nối vào file tổng
                $content = [System.IO.File]::ReadAllText($fullPath, $utf8)
                [System.IO.File]::AppendAllText($output, $content + "`r`n", $utf8)
            }
        }
    }

    if ($processedFiles.Count -gt 0) {
        Write-Host "Done! Đã gộp thành công $($processedFiles.Count) file đang mở vào SSS\export.txt"
    } else {
        Write-Host "Không tìm thấy file nào đang mở trong Editor. Hãy chắc chắn bạn đang mở các tab code!"
    }
} else {
    Write-Host "Không tìm thấy file cấu hình .idea/workspace.xml tại thư mục dự án."
}