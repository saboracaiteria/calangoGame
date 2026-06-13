package com.calangorun.rendering

import android.graphics.Canvas
import android.graphics.Paint

class Particle {
    var x: Float
    var y: Float
    var velocityX: Float
    var velocityY: Float
    var lifetime: Float
    var maxLifetime: Float
    var size: Float
    var color: Int
    var alpha: Float = 1f
    
    constructor(
        x: Float,
        y: Float,
        velocityX: Float,
        velocityY: Float,
        lifetime: Float,
        size: Float,
        color: Int
    ) {
        this.x = x
        this.y = y
        this.velocityX = velocityX
        this.velocityY = velocityY
        this.lifetime = lifetime
        this.maxLifetime = lifetime
        this.size = size
        this.color = color
    }
    
    fun update(deltaTime: Float) {
        x += velocityX * deltaTime
        y += velocityY * deltaTime
        lifetime -= deltaTime
        
        // Fade out
        alpha = kotlin.math.max(0f, lifetime / maxLifetime)
        
        // Gravity effect
        velocityY += 500 * deltaTime
    }
    
    fun isAlive(): Boolean = lifetime > 0
    
    fun draw(canvas: Canvas, paint: Paint) {
        val alphaColor = (alpha * 255).toInt() shl 24 or (color and 0xFFFFFF)
        paint.color = alphaColor
        canvas.drawCircle(x, y, size, paint)
    }
}

class ParticleSystem {
    private val particles = mutableListOf<Particle>()
    
    enum class ParticleType {
        DUST,
        SPARKLE,
        EXPLOSION,
        RAIN,
        SAND
    }
    
    fun emit(
        x: Float,
        y: Float,
        type: ParticleType,
        count: Int = 10
    ) {
        repeat(count) {
            val particle = when (type) {
                ParticleType.DUST -> createDustParticle(x, y)
                ParticleType.SPARKLE -> createSparkleParticle(x, y)
                ParticleType.EXPLOSION -> createExplosionParticle(x, y)
                ParticleType.RAIN -> createRainParticle(x, y)
                ParticleType.SAND -> createSandParticle(x, y)
            }
            particles.add(particle)
        }
    }
    
    private fun createDustParticle(x: Float, y: Float): Particle {
        return Particle(
            x = x + (Math.random() * 20 - 10).toFloat(),
            y = y + (Math.random() * 10 - 5).toFloat(),
            velocityX = (Math.random() * 100 - 50).toFloat(),
            velocityY = (Math.random() * -100 - 50).toFloat(),
            lifetime = 0.5f + Math.random().toFloat() * 0.5f,
            size = 2f + Math.random().toFloat() * 3f,
            color = 0xFFD2B48C.toInt() // Tan color
        )
    }
    
    private fun createSparkleParticle(x: Float, y: Float): Particle {
        return Particle(
            x = x + (Math.random() * 30 - 15).toFloat(),
            y = y + (Math.random() * 30 - 15).toFloat(),
            velocityX = (Math.random() * 200 - 100).toFloat(),
            velocityY = (Math.random() * 200 - 100).toFloat(),
            lifetime = 0.3f + Math.random().toFloat() * 0.4f,
            size = 1f + Math.random().toFloat() * 2f,
            color = 0xFFFFD700.toInt() // Gold color
        )
    }
    
    private fun createExplosionParticle(x: Float, y: Float): Particle {
        return Particle(
            x = x,
            y = y,
            velocityX = (Math.random() * 400 - 200).toFloat(),
            velocityY = (Math.random() * 400 - 200).toFloat(),
            lifetime = 0.5f + Math.random().toFloat() * 0.5f,
            size = 3f + Math.random().toFloat() * 5f,
            color = 0xFFFF4500.toInt() // Orange-red
        )
    }
    
    private fun createRainParticle(x: Float, y: Float): Particle {
        return Particle(
            x = x + (Math.random() * 800).toFloat(),
            y = -10f,
            velocityX = 0f,
            velocityY = 800f + Math.random().toFloat() * 200f,
            lifetime = 1.5f + Math.random().toFloat() * 0.5f,
            size = 1f + Math.random().toFloat() * 1f,
            color = 0xFF87CEEB.toInt() // Sky blue
        )
    }
    
    private fun createSandParticle(x: Float, y: Float): Particle {
        return Particle(
            x = x + (Math.random() * 800).toFloat(),
            y = y + (Math.random() * 600).toFloat(),
            velocityX = 200f + Math.random().toFloat() * 300f,
            velocityY = (Math.random() * 100 - 50).toFloat(),
            lifetime = 1f + Math.random().toFloat() * 1f,
            size = 1f + Math.random().toFloat() * 2f,
            color = 0xFFD2691E.toInt() // Chocolate
        )
    }
    
    fun update(deltaTime: Float) {
        particles.removeAll { !it.isAlive() }
        particles.forEach { it.update(deltaTime) }
    }
    
    fun draw(canvas: Canvas, paint: Paint) {
        particles.forEach { it.draw(canvas, paint) }
    }
    
    fun clear() {
        particles.clear()
    }
}
