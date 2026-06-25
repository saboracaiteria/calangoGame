# Calango Run Android Kotlin Fix Spec

## Overview
Fix all fake placeholders and incomplete logic in Calango Run Android Kotlin code to match the HTML functionality at `c:\Users\Terminal\Documents\gemini\CALANGO2\index.html`

## Requirements

### 1. InputHandler.kt Fix
- **Problem**: Has TWO versions - the second one (lines 73-113) is a placeholder
- **Solution**: Keep only the first complete version (lines 1-72) with proper swipe detection for jump/slide, zone-based controls for move/jump/hook
- **Requirements**: 
  - Touch zones: Left 25% = move left, Right 25% = move right, Center top = jump, Center bottom = hook
  - Swipe detection: Short swipe up = jump, short swipe down = slide
  - Proper release handling

### 2. AudioManager.kt Enhancement
- **Problem**: Only generates beeps, should load real audio files
- **Solution**: Implement real audio file loading from assets/res/raw
- **Requirements**:
  - Load actual sound files for: jump, coin, hit, powerup, warning, attack
  - Support multiple audio formats (WAV, OGG)
  - Implement volume control
  - Handle audio initialization errors gracefully

### 3. GameView.kt Controls Enhancement
- **Problem**: Missing swipe detection for slide mechanics
- **Solution**: Implement proper swipe detection with delta calculation
- **Requirements**:
  - Swipe up (dy < -80, duration < 300ms) = jump
  - Swipe down (dy > 80, duration < 300ms) = slide
  - Tap detection (duration < 200ms, minimal movement) = jump
  - Zone-based controls for horizontal movement

### 4. GameManager.kt Collision Detection
- **Problem**: Needs real AABB collision detection
- **Solution**: Implement proper AABB collision with bounds calculation
- **Requirements**:
  - AABB collision for Player vs Obstacle, Coin, PowerUp, Boss
  - Stomp detection for birds (landing on top)
  - Special collision for Mud (slows game speed)
  - Boss fight collision with stomp detection
  - Power-up collision triggers (Pepper=speed up, Magnet=coin attraction, Leaf=invisibility)

### 5. PowerUp Gameplay Effects
- **Problem**: Missing actual gameplay effects
- **Solution**: Implement real power-up effects
- **Requirements**:
  - **Pepper**: Increases game speed, allows destroying obstacles, visual red glow
  - **Magnet**: Attracts coins within range when hook is active, visual yellow glow
  - **Leaf**: Makes player invisible/ghost mode, visual green trail
  - All power-ups should have timer-based expiration

### 6. Animation.kt Implementation
- **Problem**: Missing or incomplete animation system
- **Solution**: Implement complete animation system with easing functions
- **Requirements**:
  - Pulse animation for power-up pickup
  - Shake animation for hit effects
  - Color interpolation for transitions
  - Sine wave for floating animations
  - Integration with particle system

### 7. Shop UI Implementation
- **Problem**: Missing shop UI implementation
- **Solution**: Implement complete shop UI with drawing
- **Requirements**:
  - Draw shop background with gradient
  - Show equipped items preview
  - Display skins, hats, effects grid
  - Show coin balance
  - Handle buy/equip logic
  - Exit button to return to menu

### 8. Background Day/Night Cycle
- **Problem**: Need real day/night cycle integration
- **Solution**: Implement sky color transitions based on score
- **Requirements**:
  - Score 0-275 = Night (dark blue/black)
  - Score 275-320 = Dawn transition
  - Score 320-575 = Day (light blue/orange)
  - Score 575-600 = Dusk transition
  - Celestial body (sun/moon) position based on score
  - Stars only visible at night
  - Ambient lighting changes

### 9. Particle System Integration
- **Problem**: Missing particle system effects
- **Solution**: Integrate ParticleSystem with gameplay events
- **Requirements**:
  - Emit particles on: jump, coin collection, power-up pickup, hit, death
  - Particle types: dust, sparkle, explosion, rain, sand
  - Gravity effect on particles
  - Fade out animation
  - Color coding by particle type

### 10. Revive System
- **Problem**: Missing revive mechanic
- **Solution**: Implement second chance revive
- **Requirements**:
  - Show revive screen on death
  - 5-second countdown timer
  - Watch ad to restore life
  - Skip option to restart
  - Save/restore game state during revive

## Design

### Entity Structure
```kotlin
class Player {
    var x, y, width, height
    var velocityY, isJumping, isDucking
    var currentPower, powerTimer
    fun getBounds(): RectF
    fun update(deltaTime)
    fun draw(canvas, paint, frames)
}

class Obstacle {
    var x, y, width, height
    enum class Type { CACTUS, TATU, MUD, BIRD }
    fun getBounds(): RectF
}

class Coin {
    var x, y, width, height
    fun getBounds(): RectF
}

class PowerUp {
    var x, y, width, height
    enum class Type { PEPPER, MAGNET, LEAF }
    fun getBounds(): RectF
}

class Boss {
    var x, y, width, height
    var hp, maxHp
    enum class State { ENTER, IDLE, WARN, ATTACK, RETURN, HURT }
    fun getBounds(): RectF
}
```

### Collision Detection
```kotlin
fun rectsOverlap(a: RectF, b: RectF): Boolean {
    return a.left < b.right && a.right > b.left && a.top < b.bottom && a.bottom > b.top
}
```

### Game Loop (60 FPS)
```kotlin
class GameLoop : Thread() {
    val targetFPS = 60
    val frameDuration = 1000 / targetFPS // 16ms
    
    override fun run() {
        while (running) {
            val startTime = System.currentTimeMillis()
            update()
            draw()
            val elapsed = System.currentTimeMillis() - startTime
            val sleepTime = frameDuration - elapsed
            if (sleepTime > 0) sleep(sleepTime)
        }
    }
}
```

## Tasks

1. **Fix InputHandler.kt**
   - Read current file
   - Delete duplicate placeholder version (lines 73-113)
   - Verify first version has complete swipe detection
   - Test touch zones work correctly

2. **Enhance AudioManager.kt**
   - Add audio file loading from res/raw
   - Implement AudioAttributes for modern Android
   - Add preloaded sound pool
   - Test audio playback

3. **Fix GameView.kt Controls**
   - Implement swipe detection with delta calculation
   - Add zone-based horizontal movement
   - Test jump/slide mechanics

4. **Fix GameManager.kt Collision**
   - Verify AABB collision implementation
   - Add stomp detection
   - Test power-up collision triggers

5. **Implement PowerUp Effects**
   - Add speed increase for Pepper
   - Add coin attraction for Magnet
   - Add invisibility for Leaf
   - Test all effects

6. **Complete Animation.kt**
   - Implement all easing functions
   - Add particle animation integration
   - Test visual effects

7. **Implement Shop UI**
   - Add shop drawing method
   - Add item rendering
   - Add buy/equip logic
   - Add preview drawing

8. **Day/Night Cycle Integration**
   - Implement sky gradient transitions
   - Add celestial body positioning
   - Add star rendering
   - Test cycle progression

9. **Particle System Integration**
   - Add particle emission on events
   - Test particle rendering
   - Verify fade out

10. **Revive System**
    - Add revive screen drawing
    - Add countdown timer
    - Add ad reward logic
    - Test revive flow

11. **Testing & Verification**
    - Run complete game loop
    - Verify 60 FPS
    - Test all game states
    - Verify save/load