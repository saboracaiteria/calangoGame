package com.calangorun.entities

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

class Obstacle {
    enum class ObstacleType {
        CACTUS, ROCK, ANIMAL
    }
    
    var x: Float
    var y: Float
    var width: Float
    var height: Float
    var type: ObstacleType
    
    private var sprite: Bitmap? = null
    var speed: Float = 0f
    
    constructor(x: Float, y: Float, width: Float, height: Float, type: ObstacleType, speed: Float) {
        this.x = x
        this.y = y
        this.width = width
        this.height = height
        this.type = type
        this.speed = speed
    }
    
    fun update(deltaTime: Float) {
        x -= speed * deltaTime
    }
    
    fun draw(canvas: Canvas, paint: Paint) {
        if (sprite != null) {
            val srcRect = Rect(0, 0, sprite!!.width, sprite!!.height)
            val dstRect = Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt())
            canvas.drawBitmap(sprite!!, srcRect, dstRect, paint)
        } else {
            // Placeholder - draw obstacle
            when (type) {
                ObstacleType.CACTUS -> paint.color = 0xFF2D5016.toInt()
                ObstacleType.ROCK -> paint.color = 0xFF808080.toInt()
                ObstacleType.ANIMAL -> paint.color = 0xFF8B4513.toInt()
            }
            canvas.drawRect(x, y, x + width, y + height, paint)
        }
    }
    
    fun getBounds(): Rect {
        return Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt())
    }
    
    fun isOffScreen(): Boolean {
        return x + width < 0
    }
}
