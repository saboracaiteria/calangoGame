package com.calangorun.entities

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

class Coin {
    var x: Float
    var y: Float
    var width = 32f
    var height = 32f
    
    private var collected = false
    private var sprite: Bitmap? = null
    private var rotation = 0f
    var speed: Float = 0f
    
    // Magnet effect
    var isAttracted = false
    var attractionSpeed = 800f
    
    constructor(x: Float, y: Float, speed: Float) {
        this.x = x
        this.y = y
        this.speed = speed
    }
    
    fun update(deltaTime: Float, playerX: Float, playerY: Float) {
        if (collected) return
        
        if (isAttracted) {
            // Move towards player
            val dx = playerX - x
            val dy = playerY - y
            val distance = kotlin.math.sqrt(dx * dx + dy * dy)
            
            if (distance > 0) {
                x += (dx / distance) * attractionSpeed * deltaTime
                y += (dy / distance) * attractionSpeed * deltaTime
            }
        } else {
            x -= speed * deltaTime
        }
        
        // Rotate animation
        rotation += 360 * deltaTime
    }
    
    fun collect() {
        collected = true
    }
    
    fun isCollected(): Boolean = collected
    
    fun draw(canvas: Canvas, paint: Paint) {
        if (collected) return
        
        if (sprite != null) {
            val srcRect = Rect(0, 0, sprite!!.width, sprite!!.height)
            val dstRect = Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt())
            canvas.drawBitmap(sprite!!, srcRect, dstRect, paint)
        } else {
            // Placeholder - draw gold circle
            paint.color = 0xFFFFD700.toInt()
            canvas.drawCircle(x + width / 2, y + height / 2, width / 2, paint)
            
            paint.color = 0xFF000000.toInt()
            paint.textSize = 16f
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText("$", x + width / 2, y + height / 2 + 5, paint)
        }
    }
    
    fun getBounds(): Rect {
        return Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt())
    }
    
    fun isOffScreen(): Boolean {
        return x + width < 0
    }
}
