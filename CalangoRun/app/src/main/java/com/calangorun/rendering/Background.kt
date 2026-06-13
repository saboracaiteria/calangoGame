package com.calangorun.rendering

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import com.calangorun.managers.GameManager

class Background {
    private var layer1X = 0f  // Far background (slowest)
    private var layer2X = 0f  // Mid background
    private var layer3X = 0f  // Near background (fastest)
    private var groundY = 0f
    
    private var layer1Bitmap: Bitmap? = null
    private var layer2Bitmap: Bitmap? = null
    private var layer3Bitmap: Bitmap? = null
    private var groundBitmap: Bitmap? = null
    
    // Day/night cycle
    private var dayNightPhase = 0f // 0 = day, 0.5 = night
    
    fun update(deltaTime: Float, gameSpeed: Float) {
        // Parallax scrolling
        layer1X -= gameSpeed * 0.2f * deltaTime
        layer2X -= gameSpeed * 0.5f * deltaTime
        layer3X -= gameSpeed * 0.8f * deltaTime
        
        // Reset position when off-screen (create infinite scroll)
        // Width will be set when bitmaps are loaded
        if (layer1X <= -1920) layer1X = 0f
        if (layer2X <= -1920) layer2X = 0f
        if (layer3X <= -1920) layer3X = 0f
        
        // Update day/night cycle based on score
        dayNightPhase = (GameManager.score % 5000) / 5000f
    }
    
    fun draw(canvas: Canvas, paint: Paint, canvasWidth: Int, canvasHeight: Int) {
        groundY = canvasHeight * 0.75f
        
        // Draw sky gradient based on day/night phase
        drawSky(canvas, paint, canvasWidth, canvasHeight)
        
        // Draw parallax layers
        if (layer1Bitmap != null) {
            canvas.drawBitmap(layer1Bitmap!!, layer1X, 0f, paint)
            canvas.drawBitmap(layer1Bitmap!!, layer1X + 1920, 0f, paint)
        }
        
        if (layer2Bitmap != null) {
            canvas.drawBitmap(layer2Bitmap!!, layer2X, canvasHeight * 0.3f, paint)
            canvas.drawBitmap(layer2Bitmap!!, layer2X + 1920, canvasHeight * 0.3f, paint)
        }
        
        if (layer3Bitmap != null) {
            canvas.drawBitmap(layer3Bitmap!!, layer3X, canvasHeight * 0.5f, paint)
            canvas.drawBitmap(layer3Bitmap!!, layer3X + 1920, canvasHeight * 0.5f, paint)
        }
        
        // Draw ground
        drawGround(canvas, paint, canvasWidth, canvasHeight)
    }
    
    private fun drawSky(canvas: Canvas, paint: Paint, width: Int, height: Int) {
        // Interpolate between day and night colors
        val skyColor = interpolateColor(
            0xFF87CEEB.toInt(), // Day - sky blue
            0xFF1a1a2e.toInt(), // Night - dark blue
            dayNightPhase
        )
        paint.color = skyColor
        canvas.drawRect(0f, 0f, width.toFloat(), groundY, paint)
    }
    
    private fun drawGround(canvas: Canvas, paint: Paint, width: Int, height: Int) {
        // Desert/caatinga ground
        val groundColor = interpolateColor(
            0xFFD2691E.toInt(), // Day - chocolate
            0xFF8B4513.toInt(), // Night - saddle brown
            dayNightPhase
        )
        paint.color = groundColor
        canvas.drawRect(0f, groundY, width.toFloat(), height.toFloat(), paint)
    }
    
    private fun interpolateColor(color1: Int, color2: Int, factor: Float): Int {
        val a1 = (color1 shr 24) and 0xFF
        val r1 = (color1 shr 16) and 0xFF
        val g1 = (color1 shr 8) and 0xFF
        val b1 = color1 and 0xFF
        
        val a2 = (color2 shr 24) and 0xFF
        val r2 = (color2 shr 16) and 0xFF
        val g2 = (color2 shr 8) and 0xFF
        val b2 = color2 and 0xFF
        
        val a = (a1 + (a2 - a1) * factor).toInt()
        val r = (r1 + (r2 - r1) * factor).toInt()
        val g = (g1 + (g2 - g1) * factor).toInt()
        val b = (b1 + (b2 - b1) * factor).toInt()
        
        return (a shl 24) or (r shl 16) or (g shl 8) or b
    }
    
    fun setLayer1Bitmap(bitmap: Bitmap) {
        layer1Bitmap = bitmap
    }
    
    fun setLayer2Bitmap(bitmap: Bitmap) {
        layer2Bitmap = bitmap
    }
    
    fun setLayer3Bitmap(bitmap: Bitmap) {
        layer3Bitmap = bitmap
    }
}
