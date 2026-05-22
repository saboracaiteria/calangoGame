Add-Type -AssemblyName System.Drawing
function Resize-Image($path, $width, $height, $out, $align = "Center") {
    $img = [System.Drawing.Image]::FromFile($path)
    $newImg = New-Object System.Drawing.Bitmap($width, $height)
    $g = [System.Drawing.Graphics]::FromImage($newImg)
    
    $ratioX = $width / $img.Width
    $ratioY = $height / $img.Height
    $ratio = [Math]::Max($ratioX, $ratioY)
    
    $newWidth = $img.Width * $ratio
    $newHeight = $img.Height * $ratio
    
    $posX = ($width - $newWidth) / 2
    
    # Lógica de Alinhamento Vertical
    $posY = 0
    if ($align -eq "Center") {
        $posY = ($height - $newHeight) / 2
    } elseif ($align -eq "Bottom") {
        $posY = $height - $newHeight
    }
    # Se for "Top", $posY continua 0
    
    $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
    $g.DrawImage($img, $posX, $posY, $newWidth, $newHeight)
    
    $newImg.Save($out, [System.Drawing.Imaging.ImageFormat]::Png)
    $img.Dispose()
    $newImg.Dispose()
    $g.Dispose()
}

$cwd = Get-Location
# Landscape agora usa alinhamento ao TOPO para proteger o Logo
Resize-Image "$cwd\capa_landscape_v2.png" 1920 1080 "$cwd\capa_landscape_final.png" "Top"
Resize-Image "$cwd\capa_portrait_v2.png" 800 1200 "$cwd\capa_portrait_final.png" "Center"
Resize-Image "$cwd\capa_square_v2.png" 800 800 "$cwd\capa_square_final.png" "Center"
