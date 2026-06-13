package com.calangorun.managers

import android.content.Context
import android.content.SharedPreferences

object ShopManager {
    private const val PREFS_NAME = "calango_shop_prefs"
    private const val PREFIX_OWNED = "owned_"
    private const val PREFIX_EQUIPPED = "equipped_"
    
    private lateinit var prefs: SharedPreferences
    
    fun initialize(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    // Skins
    enum class SkinType {
        DEFAULT, GOLDEN, SHADOW, NEON
    }
    
    // Hats
    enum class HatType {
        NONE, COWBOY, CROWN, MAGIC
    }
    
    // Trails
    enum class TrailType {
        NONE, FIRE, RAINBOW, STARDUST
    }
    
    fun isSkinOwned(skin: SkinType): Boolean {
        return prefs.getBoolean("${PREFIX_OWNED}skin_${skin.name}", skin == SkinType.DEFAULT)
    }
    
    fun buySkin(skin: SkinType, cost: Int): Boolean {
        if (ScoreManager.spendCoins(cost)) {
            prefs.edit().putBoolean("${PREFIX_OWNED}skin_${skin.name}", true).apply()
            return true
        }
        return false
    }
    
    fun equipSkin(skin: SkinType) {
        if (isSkinOwned(skin)) {
            prefs.edit().putString(PREFIX_EQUIPPED + "skin", skin.name).apply()
        }
    }
    
    fun getEquippedSkin(): SkinType {
        val skinName = prefs.getString(PREFIX_EQUIPPED + "skin", SkinType.DEFAULT.name)
        return try {
            SkinType.valueOf(skinName ?: SkinType.DEFAULT.name)
        } catch (e: IllegalArgumentException) {
            SkinType.DEFAULT
        }
    }
    
    fun isHatOwned(hat: HatType): Boolean {
        return prefs.getBoolean("${PREFIX_OWNED}hat_${hat.name}", hat == HatType.NONE)
    }
    
    fun buyHat(hat: HatType, cost: Int): Boolean {
        if (ScoreManager.spendCoins(cost)) {
            prefs.edit().putBoolean("${PREFIX_OWNED}hat_${hat.name}", true).apply()
            return true
        }
        return false
    }
    
    fun equipHat(hat: HatType) {
        if (isHatOwned(hat)) {
            prefs.edit().putString(PREFIX_EQUIPPED + "hat", hat.name).apply()
        }
    }
    
    fun getEquippedHat(): HatType {
        val hatName = prefs.getString(PREFIX_EQUIPPED + "hat", HatType.NONE.name)
        return try {
            HatType.valueOf(hatName ?: HatType.NONE.name)
        } catch (e: IllegalArgumentException) {
            HatType.NONE
        }
    }
    
    fun isTrailOwned(trail: TrailType): Boolean {
        return prefs.getBoolean("${PREFIX_OWNED}trail_${trail.name}", trail == TrailType.NONE)
    }
    
    fun buyTrail(trail: TrailType, cost: Int): Boolean {
        if (ScoreManager.spendCoins(cost)) {
            prefs.edit().putBoolean("${PREFIX_OWNED}trail_${trail.name}", true).apply()
            return true
        }
        return false
    }
    
    fun equipTrail(trail: TrailType) {
        if (isTrailOwned(trail)) {
            prefs.edit().putString(PREFIX_EQUIPPED + "trail", trail.name).apply()
        }
    }
    
    fun getEquippedTrail(): TrailType {
        val trailName = prefs.getString(PREFIX_EQUIPPED + "trail", TrailType.NONE.name)
        return try {
            TrailType.valueOf(trailName ?: TrailType.NONE.name)
        } catch (e: IllegalArgumentException) {
            TrailType.NONE
        }
    }
}
