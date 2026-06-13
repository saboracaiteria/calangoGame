package com.calangorun.game

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.calangorun.managers.GameManager
import com.calangorun.managers.GameManager.GameState

class GameView(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs),
    SurfaceHolder.Callback {

    private val gameLoop = GameLoop(this)
    private val inputHandler = InputHandler(context)
    private val paint = Paint()

    init {
        holder.addCallback(this)
        setWillNotDraw(false)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        gameLoop.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Handle surface changes if needed
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        gameLoop.running = false
        while (retry) {
            try {
                gameLoop.join()
                retry = false
            } catch (e: InterruptedException) {
                // Retry
            }
        }
    }

    fun draw(canvas: Canvas) {
        // Clear canvas
        canvas.drawColor(Color.BLACK)

        when (GameManager.gameState) {
            GameState.MENU -> drawMenu(canvas)
            GameState.PLAYING -> drawGame(canvas)
            GameState.PAUSED -> drawPaused(canvas)
            GameState.GAME_OVER -> drawGameOver(canvas)
            GameState.SHOP -> drawShop(canvas)
        }
    }

    private fun drawMenu(canvas: Canvas) {
        paint.color = Color.WHITE
        paint.textSize = 60f
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("CALANGO RUN DELUXE", width / 2f, height / 3f, paint)

        paint.textSize = 30f
        canvas.drawText("Toque para Jogar", width / 2f, height / 2f, paint)
        
        paint.textSize = 20f
        canvas.drawText("Loja", width / 2f, height / 2f + 80f, paint)
    }

    private fun drawGame(canvas: Canvas) {
        // Draw background
        drawBackground(canvas)
        
        // Draw game entities (player, obstacles, coins, etc.)
        // This will be expanded as we implement entities
        GameManager.draw(canvas, paint)
        
        // Draw HUD
        drawHUD(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        // Parallax background will be implemented here
        paint.color = Color.parseColor("#1a0f0a")
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
    }

    private fun drawHUD(canvas: Canvas) {
        paint.color = Color.WHITE
        paint.textSize = 24f
        paint.textAlign = Paint.Align.LEFT
        canvas.drawText("Score: ${GameManager.score}", 20f, 40f, paint)
        canvas.drawText("Moedas: ${GameManager.coins}", 20f, 70f, paint)
    }

    private fun drawPaused(canvas: Canvas) {
        drawGame(canvas)
        paint.color = Color.argb(180, 0, 0, 0)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        
        paint.color = Color.WHITE
        paint.textSize = 50f
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("PAUSADO", width / 2f, height / 2f, paint)
    }

    private fun drawGameOver(canvas: Canvas) {
        drawGame(canvas)
        paint.color = Color.argb(200, 0, 0, 0)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        
        paint.color = Color.WHITE
        paint.textSize = 50f
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("GAME OVER", width / 2f, height / 3f, paint)
        
        paint.textSize = 30f
        canvas.drawText("Score: ${GameManager.score}", width / 2f, height / 2f, paint)
        canvas.drawText("Toque para Jogar Novamente", width / 2f, height / 2f + 80f, paint)
    }

    private fun drawShop(canvas: Canvas) {
        paint.color = Color.parseColor("#1a0f0a")
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
        
        paint.color = Color.WHITE
        paint.textSize = 50f
        paint.textAlign = Paint.Align.CENTER
        canvas.drawText("LOJA", width / 2f, height / 4f, paint)
        
        paint.textSize = 25f
        canvas.drawText("Em breve...", width / 2f, height / 2f, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return inputHandler.handleTouchEvent(event, this)
    }

    fun update() {
        // Update game logic
        GameManager.update()
    }
}
