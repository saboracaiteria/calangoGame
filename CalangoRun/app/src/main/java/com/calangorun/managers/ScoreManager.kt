package com.calangorun.managers

import android.content.Context
import android.content.SharedPreferences

object ScoreManager {
    private const val PREFS_NAME = "calango_run_prefs"
    private const val KEY_HIGH_SCORE = "high_score"
    private const val KEY_TOTAL_COINS = "total_coins"
    private const val KEY_GAMES_PLAYED = "games_played"
    
    private lateinit var prefs: SharedPreferences
    
    fun initialize(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    var highScore: Int
        get() = prefs.getInt(KEY_HIGH_SCORE, 0)
        set(value) = prefs.edit().putInt(KEY_HIGH_SCORE, value).apply()
    
    var totalCoins: Int
        get() = prefs.getInt(KEY_TOTAL_COINS, 0)
        set(value) = prefs.edit().putInt(KEY_TOTAL_COINS, value).apply()
    
    var gamesPlayed: Int
        get() = prefs.getInt(KEY_GAMES_PLAYED, 0)
        set(value) = prefs.edit().putInt(KEY_GAMES_PLAYED, value).apply()
    
    fun addCoins(amount: Int) {
        totalCoins += amount
    }
    
    fun spendCoins(amount: Int): Boolean {
        if (totalCoins >= amount) {
            totalCoins -= amount
            return true
        }
        return false
    }
    
    fun updateHighScore(newScore: Int) {
        if (newScore > highScore) {
            highScore = newScore
        }
    }
    
    fun incrementGamesPlayed() {
        gamesPlayed++
    }
}
