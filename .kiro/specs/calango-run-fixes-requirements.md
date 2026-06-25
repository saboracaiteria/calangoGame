# Calango Run Android Fix - Requirements

## 1. InputHandler.kt Fix

### Current Problem
File has duplicate code - second version (lines 73-113) is placeholder with incomplete logic

### Required Implementation
- **Zone-based touch controls**:
  - Left 25% of screen → Move left
  - Right 25% of screen → Move right  
  - Center top (50%) → Jump
  - Center bottom (50%) → Hook/Activate special
- **Swipe detection**:
  - Swipe up (dy < -80px, duration < 300ms) → Jump
  - Swipe down (dy > 80px, duration < 300ms) → Slide
  - Tap (duration < 200ms, movement < 50px) → Jump
- **Touch release handling**:
  - Stop horizontal movement on release
  - Stop ducking on release
  - Handle ACTION_UP, ACTION_CANCEL

### File Location
`c:\Users\Terminal\Documents\gemini\CALANGO2\CalangoRun\app\src\main\java\com\calangorun\game\InputHandler.kt`

---

## 2. AudioManager.kt Enhancement

### Current Problem
Only generates synthetic beeps using AudioTrack, no real audio files

### Required Implementation
- **Audio loading from assets**:
  - res/raw/jump.wav, jump.ogg
  - res/raw/coin.wav, coin.ogg
  - res/raw/hit.wav, hit.ogg
  - res/raw/powerup.wav, powerup.ogg
  - res/raw/warning.wav, warning.ogg
  - res/raw/attack.wav, attack.ogg
- **AudioManager structure**:
  ```kotlin
  object AudioManager {
      private var soundPool: SoundPool? = null
      private var audioAttributes: AudioAttributes? = null
      
      fun initialize(context: Context) {
          audioAttributes = AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_GAME)
              .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
              .build()
          
          soundPool = SoundPool.Builder()
              .setMaxStreams(8)
              .setAudioAttributes(audioAttributes!!)
              .build()
      }
      
      fun playSound(soundId: Int) {
          soundPool?.play(soundId, 1f, 1f, 1, 0, 1f)
      }
  }
  ```
- **Volume control**: 0.0 to 1.0
- **Graceful degradation**: Fallback to beeps if audio files missing

### File Location
`c:\Users\Terminal\Documents\gemini\CALANGO2\CalangoRun\app\src\main\java\com\calangorun\managers\AudioManager.kt`

---

## 3. GameView.kt Controls Enhancement

### Current Problem
Missing proper swipe detection for duck/slide mechanics

### Required Implementation
- **Swipe calculation**:
  ```kotlin
  val dx = touchEndX - touchStartX
  val dy = touchEndY - touchStartY
  val duration = System.currentTimeMillis() - touchStartTime
  
  // Detect swipe direction
  when {
      dy < -80 && kotlin.math.abs(dy) > kotlin.math.abs(dx) && duration < 300 -> jump()
      dy > 80 && kotlin.math.abs(dy) > kotlin.math.abs(dx) && duration < 300 -> slide()
      duration < 200 && kotlin.math.abs(dx) < 50 && kotlin.math.abs(dy) < 50 -> jump()
  }
  ```
- **Continuous slide**: Keep ducking while touch is held
- **Release handling**: Stop ducking on ACTION_UP

### File Location
`c:\Users\Terminal\Documents\gemini\CALANGO2\CalangoRun\app\src\main\java\com\calangorun\game\GameView.kt`

---

## 4. GameManager.kt Collision Detection

### Current Problem
Needs AABB collision detection with proper bounds

### Required Implementation
- **AABB Collision**:
  ```kotlin
  fun rectsOverlap(a: RectF, b: RectF): Boolean {
      return a.left < b.right && a.right > b.left && a.top < b.bottom && a.bottom > b.top
  }
  ```
- **Stomp detection** (for birds):
  ```kotlin
  val isStomp = (player.velocityY > 0) && 
                (player.y + player.height) <= (obstacle.y + 10)
  ```
- **Collision flow**:
  1. Check coin collisions → Add score/coins
  2. Check power-up collisions → Apply effect
  3. Check obstacle collisions → Damage or destroy
  4. Check boss collisions → Boss fight logic

### File Location
`c:\Users\Terminal\Documents\gemini\CALANGO2\CalangoRun\app\src\main\java\com\calangorun\managers\GameManager.kt`

---

## 5. PowerUp Gameplay Effects

### Required Effects

#### Pepper Power-Up (Red)
- **Effect**: Increase game speed by 5f, destroy obstacles on collision
- **Visual**: Red glow around player
- **Timer**: 600 frames (10 seconds at 60fps)
- **Code**:
  ```kotlin
  when (item.type) {
      PowerUpType.PEPPER -> {
          gameSpeed += 5f
          player.currentPower = "pepper"
          player.powerTimer = 600f
      }
  }
  ```

#### Magnet Power-Up (Yellow)
- **Effect**: Attract coins when hook is active
- **Visual**: Yellow glow with rotating rings
- **Range**: 450 pixels
- **Pull speed**: 30 pixels per frame
- **Code**:
  ```kotlin
  if (isHookActive && dist < 450f) {
      x += (dx / dist) * 30f
      y += (dy / dist) * 30f
  }
  ```

#### Leaf Power-Up (Green)
- **Effect**: Invisibility/ghost mode
- **Visual**: Green trail particles, flashing player
- **Timer**: 600 frames (10 seconds)
- **Code**:
  ```kotlin
  if (player.currentPower == "leaf") {
      player.alpha = if (frames % 4 < 2) 128 else 255
  }
  ```

### File Location
`c:\Users\Terminal\Documents\gemini\CALANGO2\CalangoRun\app\src\main\java\com\calangorun\entities\PowerUp.kt`

---

## 6. Animation.kt Implementation

### Required Functions

#### Pulse Animation
```kotlin
fun drawPulse(canvas: Canvas, paint: Paint, cx: Float, cy: Float, radius: Float,
              color: Int, alpha: Float, progress: Float) {
    val scale = 1f + sin(progress * PI * 2f) * 0.15f
    paint.color = color
    paint.alpha = (alpha * 255).toInt()
    canvas.drawCircle(cx, cy, radius * scale, paint)
}
```

#### Shake Animation
```kotlin
fun shake(amplitude: Float = 5f): Pair<Float, Float> {
    val dx = (Math.random().toFloat() * 2f - 1f) * amplitude
    val dy = (Math.random().toFloat() * 2f - 1f) * amplitude
    return Pair(dx, dy)
}
```

#### Color Interpolation
```kotlin
fun lerpColor(color1: Int, color2: Int, t: Float): Int {
    val a1 = (color1 shr 24) and 0xFF
    val r1 = (color1 shr 16) and 0xFF
    val g1 = (color1 shr 8) and 0xFF
    val b1 = color1 and 0xFF
    val a2 = (color2 shr 24) and 0xFF
    val r2 = (color2 shr 16) and 0xFF
    val g2 = (color2 shr 8) and 0xFF
    val b2 = color2 and 0xFF
    return ((a1 + ((a2 - a1) * t)).toInt() shl 24) or
           ((r1 + ((r2 - r1) * t)).toInt() shl 16) or
           ((g1 + ((g2 - g1) * t)).toInt() shl 8) or
           (b1 + ((b2 - b1) * t)).toInt()
}
```

### File Location
`c:\Users\Terminal\Documents\gemini\CALANGO2\CalangoRun\app\src\main\java\com\calangorun\rendering\Animation.kt`

---

## 7. Shop UI Implementation

### Required UI Elements

#### Shop Header
- Title: "LOJA" with gradient text
- Coin balance display with icon

#### Character Preview
- Live drawing of equipped character
- Bobbing animation
- Effect trail rendering

#### Item Grids
- Skins (10 items)
- Hats (10 items)
- Effects (4 items)
- Upgrade items (3 items)

#### Item Card
- Image/Icon preview
- Item name
- Price in coins
- Unlock/Equip button
- Selected state indicator

#### Exit Button
- "VOLTAR" button to return to menu

### File Location
`c:\Users\Terminal\Documents\gemini\CALangoRun\app\src\main\java\com\calangorun\managers\ShopManager.kt`
`c:\Users\Terminal\Documents\gemini\CALANGO2\CalangoRun\app\src\main\java\com\calangorun\game\GameView.kt`

---

## 8. Day/Night Cycle Integration

### Cycle Phases (600 score points)

| Score Range | Phase | Sky Color | Celestial Body |
|-------------|-------|-----------|----------------|
| 0-275 | Night | Dark blue/black | Moon |
| 275-320 | Dawn | Blue to orange transition | Rising sun |
| 320-575 | Day | Light blue/orange | Sun |
| 575-600 | Dusk | Orange to dark transition | Setting sun |

### Sky Gradient Colors
```kotlin
// Day
intArrayOf(Color.parseColor("#38bdf8"), Color.parseColor("#0ea5e9"), Color.parseColor("#fdba74"))

// Night
intArrayOf(Color.parseColor("#0a0f1e"), Color.parseColor("#0f172a"), Color.parseColor("#1e293b"))
```

### Celestial Position
```kotlin
val cyclePos = score % 600
val celestialX = (cyclePos / 600f) * canvasWidth
```

### Star Rendering (Night Only)
- 60 stars with random positions
- Twinkle effect
- Different sizes

---

## 9. Particle System Integration

### Particle Types

| Type | Color | Use Case |
|------|-------|----------|
| DUST | Tan | Jump, landing |
| SPARKLE | Gold | Coin collection |
| EXPLOSION | Orange-red | Power-up pickup |
| RAIN | Sky blue | Weather effect |
| SAND | Chocolate | Ground particles |

### Particle Structure
```kotlin
class Particle {
    var x, y, velocityX, velocityY
    var lifetime, maxLifetime, size
    var color, alpha
    fun update(deltaTime)
    fun isAlive(): Boolean
    fun draw(canvas, paint)
}
```

### Emission Events
- Jump: 5-10 dust particles
- Coin: 3-5 sparkle particles
- Power-up: 10-15 explosion particles
- Hit: 5-10 particles
- Death: 20-30 particles

---

## 10. Revive System

### Revive Screen UI
- Title: "SECOND CHANCE!"
- Countdown timer with circular progress
- "RESTORE LIFE" button (shows ad)
- "SKIP" button (restarts game)
- 5-second countdown

### Timer Logic
```kotlin
var reviveTimer = 5f // seconds
reviveTimer -= deltaTime

if (reviveTimer <= 0) {
    gameOver() // Auto-fail after timer
}
```

### Ad Reward
```kotlin
CrazyGamesEngine.requestAd("rewarded", {
    onReward: {
        lives = MAX_LIVES
        gameState = GameState.PLAYING
    }
})
```

---

## 11. Complete Entity Structure

### Player Entity
```kotlin
class Player {
    var x: Float, y: Float
    var width: Float = 80f, height: Float = 64f
    var velocityY: Float = 0f
    var isJumping: Boolean = false
    var isDucking: Boolean = false
    var moveLeft: Boolean = false
    var moveRight: Boolean = false
    var currentPower: String? = null
    var powerTimer: Float = 0f
    var invulnerableTime: Int = 0
    
    fun jump()
    fun stopJump()
    fun duck(active: Boolean)
    fun getBounds(): RectF
    fun update(deltaTime: Float, canvasWidth: Int, canvasHeight: Int)
    fun draw(canvas: Canvas, paint: Paint, frames: Int)
    fun reset(screenWidth: Int, screenHeight: Int)
}
```

### Obstacle Entity
```kotlin
sealed class Obstacle {
    var x: Float, y: Float
    var width: Float, height: Float
    var markedForDeletion: Boolean = false
    
    fun getBounds(): RectF
    fun isOffScreen(): Boolean
    fun update(deltaTime: Float, gameSpeed: Float, timeScale: Float)
    fun draw(canvas: Canvas, paint: Paint, frames: Int, isDay: Boolean, celestialX: Float)
    
    class Cactus(x, y, w, h) : Obstacle()
    class Tatu(x, y, w, h) : Obstacle()
    class Mud(x, y, w, h) : Obstacle()
    class Bird(x, y, w, h) : Obstacle()
}
```

### Coin Entity
```kotlin
class Coin {
    var x: Float, y: Float
    var width: Float = 24f, height: Float = 24f
    var markedForDeletion: Boolean = false
    var speed: Float = 0f
    
    fun getBounds(): RectF
    fun update(deltaTime: Float, gameSpeed: Float, timeScale: Float)
    fun draw(canvas: Canvas, paint: Paint, frames: Int)
}
```

### PowerUp Entity
```kotlin
class PowerUp {
    enum class PowerUpType { PEPPER, MAGNET, LEAF }
    
    var x: Float, y: Float
    var width: Float = 20f, height: Float = 20f
    var type: PowerUpType
    var markedForDeletion: Boolean = false
    
    fun getBounds(): RectF
    fun update(deltaTime: Float, gameSpeed: Float, timeScale: Float, 
               playerX: Float, playerY: Float, isHookActive: Boolean)
    fun draw(canvas: Canvas, paint: Paint, frames: Int)
}
```

### Boss Entity
```kotlin
class Boss {
    enum class BossState { ENTER, IDLE, WARN, ATTACK, RETURN, HURT }
    
    var x: Float, y: Float
    var w: Float = 90f, h: Float = 60f
    var hp: Int, maxHp: Int
    var state: BossState
    var timer: Float
    var title: String
    var color: Int
    
    fun getBounds(): RectF
    fun update(deltaTime: Float, timeScale: Float, canvasWidth: Int)
    fun draw(canvas: Canvas, paint: Paint, frames: Int)
}
```

---

## 12. Save/Load System

### Data to Save
- High score
- Total coins
- Unlocked items
- Equipped skin/hat/effect
- Upgrade levels
- Current stage
- XP and level

### Shared Preferences
```kotlin
val prefs = context.getSharedPreferences("calango_prefs", Context.MODE_PRIVATE)

// Save
prefs.edit()
    .putFloat("high_score", highScore)
    .putInt("total_coins", totalCoins)
    .putString("equipped_skin", equippedSkin)
    .putString("equipped_hat", equippedHat)
    .putString("equipped_effect", equippedEffect)
    .putInt("level", level)
    .putInt("xp", xp)
    .apply()

// Load
highScore = prefs.getFloat("high_score", 0f)
totalCoins = prefs.getInt("total_coins", 0)
// ... etc
```