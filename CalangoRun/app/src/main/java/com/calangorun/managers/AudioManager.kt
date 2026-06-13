package com.calangorun.managers

import android.content.Context
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build

object AudioManager {
    private var soundPool: SoundPool? = null
    private var audioManager: AudioManager? = null
    
    // Sound IDs
    private var jumpSound = 0
    private var coinSound = 0
    private var collisionSound = 0
    private var powerupSound = 0
    private var bossSound = 0
    
    private var maxStreams = 10
    private var currentVolume = 1.0f
    
    fun initialize(context: Context) {
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        
        soundPool = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            SoundPool.Builder()
                .setMaxStreams(maxStreams)
                .build()
        } else {
            @Suppress("DEPRECATION")
            SoundPool(maxStreams, AudioManager.STREAM_MUSIC, 0)
        }
        
        // Load sounds from resources
        // jumpSound = soundPool?.load(context, R.raw.jump, 1) ?: 0
        // coinSound = soundPool?.load(context, R.raw.coin, 1) ?: 0
        // collisionSound = soundPool?.load(context, R.raw.collision, 1) ?: 0
        // powerupSound = soundPool?.load(context, R.raw.powerup, 1) ?: 0
        // bossSound = soundPool?.load(context, R.raw.boss, 1) ?: 0
    }
    
    fun playJump() {
        if (jumpSound != 0) {
            soundPool?.play(jumpSound, currentVolume, currentVolume, 1, 0, 1f)
        }
    }
    
    fun playCoin() {
        if (coinSound != 0) {
            soundPool?.play(coinSound, currentVolume, currentVolume, 1, 0, 1f)
        }
    }
    
    fun playCollision() {
        if (collisionSound != 0) {
            soundPool?.play(collisionSound, currentVolume, currentVolume, 1, 0, 1f)
        }
    }
    
    fun playPowerUp() {
        if (powerupSound != 0) {
            soundPool?.play(powerupSound, currentVolume, currentVolume, 1, 0, 1f)
        }
    }
    
    fun playBoss() {
        if (bossSound != 0) {
            soundPool?.play(bossSound, currentVolume, currentVolume, 1, 0, 1f)
        }
    }
    
    fun setVolume(volume: Float) {
        currentVolume = volume.coerceIn(0f, 1f)
        
        // Update system volume for music stream
        val maxVolume = audioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ?: 15
        val scaledVolume = (currentVolume * maxVolume).toInt()
        audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, scaledVolume, 0)
    }
    
    fun release() {
        soundPool?.release()
        soundPool = null
    }
}
