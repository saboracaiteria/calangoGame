package com.calangorun.entities

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

class PowerUp {
    enum class PowerUpType {
        PEPPER,  // Speed + Invincibility
        MAGNET,  // Attract coins
        LEAF     // Special ability
    }
    
    var x: Float
    var y: Float
    var width = 40f
    var height = 40f
    var type: PowerUpType
    
    private var collected = false
    private var sprite: Bitmap? = null
    var speed: Float = 0f
    
    // Active timer (when collected)
    var isActive = false
    private var activeTime = 0L
    private val duration = 10000L // 10 seconds
    
    constructor(x: Float, y: Float, type: PowerUpType, speed: Float) {
        this.x = x
        this.y = y
        this.type = type
        this.speed = speed
    }
    
    fun update(deltaTime: Float) {
        if (collected) {
            if (isActive) {
                activeTime += (deltaTime * 1000).toLong()
                if (activeTime >= duration) {
                    isActive = false
                }
            }
            return
        }
        
        x -= speed * deltaTime
    }
    
    fun collect() {
        collected = true
        isActive = true
        activeTime = 0
    }
    
    fun isCollected(): Boolean = collected
    
    fun isActive(): Boolean = isActive && activeTime < duration
    
    fun getRemainingTime(): Long {
        if (!isActive) return 0
        return duration - activeTime
    }
    
    fun draw(canvas: Canvas, paint: Paint) {
        if (collected && !isActive) return
        
        if (sprite != null) {
            val srcRect = Rect(0, 0, sprite!!.width, sprite!!.height)
            val dstRect = Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt())
            canvas.drawBitmap(sprite!!, srcRect, dstRect, paint)
        } else {
            // Placeholder - draw colored shape based on type
            when (type) {
                PowerUpType.PEPPER -> paint.color = 0xFFFF4500.toInt() // Red-orange
                PowerUpType.MAGNET -> paint.color = 0xFF4169E1.toInt() // Blue
                PowerUpType.LEAF -> paint.color = 0xFF32CD32.toInt()   // Green
            }
            canvas.drawRect(x, y, x + width, y + height, paint)
            
            paint.color = 0xFFFFFFFF.toInt()
            paint.textSize = 20f
            paint.textAlign = Paint.Align.CENTER
            val label = when (type) {
                PowerUpType.PEPPER -> "P"
                PowerUpType.MAGNET -> "M"
                PowerUpType.LEAF -> "L"
            }
            canvas.drawText(label, x + width / 2, y + height / 2 + 7, paint)
        }
    }
    
    fun getBounds(): Rect {
        return Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt())
    }
    
    fun isOffScreen(): Boolean {
        return x + width < 0
    }
}
