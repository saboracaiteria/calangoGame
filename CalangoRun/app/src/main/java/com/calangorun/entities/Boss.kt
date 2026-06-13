package com.calangorun.entities

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect

class Boss {
    var x: Float
    var y: Float
    var width = 120f
    var height = 120f
    
    var health = 100
    var maxHealth = 100
    var isActive = false
    
    private var sprite: Bitmap? = null
    private var attackPattern = 0
    private var attackTimer = 0L
    private val attackInterval = 2000L // 2 seconds between attacks
    
    constructor(screenWidth: Int, screenHeight: Int) {
        x = screenWidth * 0.7f
        y = screenHeight * 0.3f
    }
    
    fun update(deltaTime: Float, gameTime: Long) {
        if (!isActive) return
        
        attackTimer += (deltaTime * 1000).toLong()
        
        if (attackTimer >= attackInterval) {
            attackTimer = 0
            performAttack()
        }
    }
    
    private fun performAttack() {
        attackPattern = (attackPattern + 1) % 3
        // Attack patterns will be implemented
        // 0: Charge forward
        // 1: Spawn projectiles
        // 2: Area attack
    }
    
    fun takeDamage(amount: Int) {
        health = kotlin.math.max(0, health - amount)
        if (health <= 0) {
            isActive = false
            // Boss defeated - reward player
        }
    }
    
    fun draw(canvas: Canvas, paint: Paint) {
        if (!isActive) return
        
        if (sprite != null) {
            val srcRect = Rect(0, 0, sprite!!.width, sprite!!.height)
            val dstRect = Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt())
            canvas.drawBitmap(sprite!!, srcRect, dstRect, paint)
        } else {
            // Placeholder - draw boss
            paint.color = 0xFFFF0000.toInt()
            canvas.drawRect(x, y, x + width, y + height, paint)
            
            // Draw health bar
            val healthBarWidth = width
            val healthBarHeight = 10f
            val healthPercent = health.toFloat() / maxHealth
            
            paint.color = 0xFF333333.toInt()
            canvas.drawRect(x, y - 20f, x + healthBarWidth, y - 20f + healthBarHeight, paint)
            
            paint.color = 0xFF00FF00.toInt()
            canvas.drawRect(x, y - 20f, x + healthBarWidth * healthPercent, y - 20f + healthBarHeight, paint)
        }
    }
    
    fun getBounds(): Rect {
        return Rect(x.toInt(), y.toInt(), (x + width).toInt(), (y + height).toInt())
    }
    
    fun reset(screenWidth: Int, screenHeight: Int) {
        x = screenWidth * 0.7f
        y = screenHeight * 0.3f
        health = 100
        maxHealth = 100
        isActive = false
        attackTimer = 0
    }
    
    fun activate() {
        isActive = true
        health = maxHealth
    }
}
