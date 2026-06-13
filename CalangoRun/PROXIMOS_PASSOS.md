# Guia de Próximos Passos - Calango Run Android

## ✅ O que já foi implementado

### Estrutura Core
- ✅ Projeto Android configurado com Kotlin
- ✅ Game loop com 60 FPS (Thread dedicada)
- ✅ SurfaceView para rendering nativo
- ✅ Sistema de input (touch events)
- ✅ GameManager com estados (Menu, Playing, Paused, Game Over, Shop)

### Entidades do Jogo
- ✅ Player (Calango) com física de pulo e slide
- ✅ Obstacles (Cactus, Rock, Animal)
- ✅ Coins com efeito de ímã
- ✅ PowerUps (Pepper, Magnet, Leaf)
- ✅ Boss com sistema de vida e ataques

### Sistemas de Rendering
- ✅ Background com parallax scrolling
- ✅ Ciclo dia/noite baseado no score
- ✅ ParticleSystem (Dust, Sparkle, Explosion, Rain, Sand)
- ✅ Animation framework para spritesheets

### Managers
- ✅ ScoreManager com persistência (SharedPreferences)
- ✅ ShopManager (skins, hats, trails)
- ✅ AudioManager (SoundPool para efeitos)

### UI/UX
- ✅ MainActivity com fullscreen immersive mode
- ✅ Telas: Menu, Game, Game Over, Shop, Paused
- ✅ HUD com score e moedas
- ✅ Internacionalização PT/EN

### Configuração
- ✅ AndroidManifest.xml configurado
- ✅ Build Gradle com dependências
- ✅ Resources (strings, colors, themes)
- ✅ ProGuard rules
- ✅ Backup rules

---

## 🎯 Próximos Passos para Gerar o APK

### Passo 1: Instalar Android Studio

1. Baixe em: https://developer.android.com/studio
2. Instale normalmente
3. Na primeira execução, ele instalará o SDK automaticamente

### Passo 2: Abrir o Projeto

1. Abra o Android Studio
2. Clique em **File > Open**
3. Navegue até: `C:\Users\Terminal\Documents\gemini\CALANGO2\CalangoRun`
4. Selecione a pasta e clique em **OK**
5. Aguarde o **Gradle Sync** completar (pode levar alguns minutos na primeira vez)

### Passo 3: Corrigir Imports (se necessário)

O Android Studio pode mostrar alguns imports em vermelho. Isso é normal porque:
- O projeto ainda não foi sincronizado com o Gradle
- Após o sync, todos os imports serão resolvidos automaticamente

### Passo 4: Build do APK

#### Opção A: Via Android Studio (Recomendado)

1. No menu superior: **Build > Build Bundle(s) / APK(s) > Build APK(s)**
2. Aguarde o build completar
3. Clique em **locate** quando aparecer a notificação
4. O APK estará em: `app\build\outputs\apk\debug\app-debug.apk`

#### Opção B: Via Script PowerShell

```powershell
cd C:\Users\Terminal\Documents\gemini\CALANGO2\CalangoRun
.\build-apk.ps1
```

Escolha a opção **1** para Debug APK.

#### Opção C: Via Command Line

```bash
cd C:\Users\Terminal\Documents\gemini\CALANGO2\CalangoRun
gradlew.bat assembleDebug
```

### Passo 5: Instalar no Dispositivo

#### Método A: USB Debugging

1. No dispositivo Android:
   - Vá em **Configurações > Sobre o telefone**
   - Toque 7 vezes em **Número da build** para ativar Developer Options
   - Volte e vá em **Configurações > Opções de desenvolvedor**
   - Ative **Depuração USB**

2. Conecte o dispositivo via USB
3. No Android Studio, clique em **Run** (▶️) ou execute:
   ```bash
   gradlew.bat installDebug
   ```

#### Método B: Transferir APK

1. Copie `app-debug.apk` para o dispositivo (USB, cloud, etc.)
2. No dispositivo, abra o arquivo APK
3. Permita instalação de fontes desconhecidas se solicitado
4. Instale normalmente

---

## 🔧 Melhorias para Implementar

### 1. Adicionar Assets Visuais (Prioridade Alta)

Atualmente o jogo usa placeholders (retângulos coloridos). Para adicionar sprites reais:

#### Criar Spritesheets:
- Player: sprites de corrida, pulo, slide
- Obstáculos: cactos, pedras, animais
- Moedas: animação de rotação
- Power-ups: Pepper, Magnet, Leaf
- Boss: sprites de ataque

#### Formato:
- PNG com fundo transparente
- Colocar em: `app/src/main/res/drawable-nodpi/`
- Nome: `player_run_01.png`, `player_run_02.png`, etc.

#### Carregar no código:
```kotlin
val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.player_run_01)
```

### 2. Sistema de Spawn de Obstáculos

Implementar spawn progressivo baseado em score:

```kotlin
class ObstacleSpawner {
    private var spawnTimer = 0f
    private var spawnInterval = 2.0f // seconds
    
    fun update(deltaTime: Float, score: Int) {
        spawnTimer += deltaTime
        
        // Decrease interval as score increases
        val adjustedInterval = spawnInterval - (score / 5000)
        
        if (spawnTimer >= adjustedInterval) {
            spawnTimer = 0f
            spawnObstacle()
        }
    }
    
    private fun spawnObstacle() {
        // Create new obstacle at right edge of screen
    }
}
```

### 3. Detecção de Colisões

Implementar AABB collision detection:

```kotlin
fun checkCollision(rect1: Rect, rect2: Rect): Boolean {
    return rect1.left < rect2.right &&
           rect1.right > rect2.left &&
           rect1.top < rect2.bottom &&
           rect1.bottom > rect2.top
}

// Usage in GameView:
if (checkCollision(player.getBounds(), obstacle.getBounds())) {
    GameManager.gameOver()
}
```

### 4. Power-up Effects

Implementar lógica dos power-ups:

```kotlin
// In Player.kt or GameManager
fun activatePowerUp(type: PowerUpType) {
    when (type) {
        PowerUpType.PEPPER -> {
            isInvincible = true
            gameSpeed *= 1.5f
            // Set timer to deactivate
        }
        PowerUpType.MAGNET -> {
            // Activate magnet range for coins
        }
        PowerUpType.LEAF -> {
            // Special ability (double jump?)
        }
    }
}
```

### 5. Boss Battle System

Implementar spawn do boss a cada 1000 pontos:

```kotlin
// In GameManager
if (score > 0 && score % 1000 == 0 && !boss.isActive) {
    boss.activate()
    AudioManager.playBoss()
}
```

### 6. Adicionar Sons

1. Criar/obter arquivos de áudio (WAV ou MP3)
2. Colocar em: `app/src/main/res/raw/`
   - `jump.wav`
   - `coin.wav`
   - `collision.wav`
   - `powerup.wav`
   - `boss.wav`
3. Descomentar linhas em AudioManager.kt:
```kotlin
jumpSound = soundPool?.load(context, R.raw.jump, 1) ?: 0
coinSound = soundPool?.load(context, R.raw.coin, 1) ?: 0
// etc...
```

### 7. Ícone do App

Criar ícone em diferentes densidades:
- `res/mipmap-hdpi/ic_launcher.png` (72x72)
- `res/mipmap-xhdpi/ic_launcher.png` (96x96)
- `res/mipmap-xxhdpi/ic_launcher.png` (144x144)

Ou usar **Image Asset Studio** no Android Studio:
- Right-click em `res` > **New** > **Image Asset**

### 8. Otimizações de Performance

#### Object Pooling:
```kotlin
object ObstaclePool {
    private val pool = mutableListOf<Obstacle>()
    
    fun obtain(): Obstacle {
        return if (pool.isNotEmpty()) pool.removeAt(0)
        else Obstacle(0f, 0f, 50f, 50f, ObstacleType.CACTUS, 0f)
    }
    
    fun release(obstacle: Obstacle) {
        pool.add(obstacle)
    }
}
```

#### Sprite Batching:
- Agrupar draws do mesmo tipo
- Minimizar chamadas a `canvas.drawBitmap()`

#### Memory Management:
- Usar `Bitmap.Config.RGB_565` para menor consumo
- Reciclar bitmaps não usados

### 9. Testes

Testar em diferentes:
- Tamanhos de tela (phones, tablets)
- Versões do Android (7.0, 8.0, 9.0, 10, 11, 12, 13, 14)
- Densidades de tela (mdpi, hdpi, xhdpi, xxhdpi, xxxhdpi)

---

## 📊 Status do Projeto

| Componente | Status | Progresso |
|------------|--------|-----------|
| Estrutura do Projeto | ✅ Completo | 100% |
| Game Engine Core | ✅ Completo | 100% |
| Entidades | ✅ Completo | 100% |
| Rendering Systems | ✅ Completo | 100% |
| Managers | ✅ Completo | 100% |
| UI/UX | ✅ Completo | 100% |
| Internacionalização | ✅ Completo | 100% |
| Configuração de Build | ✅ Completo | 100% |
| **Assets Visuais** | ⏳ Pendente | 0% |
| **Sistema de Spawn** | ⏳ Pendente | 0% |
| **Detecção de Colisões** | ⏳ Pendente | 0% |
| **Power-up Logic** | ⏳ Pendente | 0% |
| **Boss Battles** | ⏳ Pendente | 0% |
| **Audio Files** | ⏳ Pendente | 0% |
| **App Icon** | ⏳ Pendente | 0% |
| **Testing** | ⏳ Pendente | 0% |

**Progresso Total: ~60%** (estrutura completa, falta gameplay e assets)

---

## 🚀 Timeline Estimada

| Fase | Tempo | Descrição |
|------|-------|-----------|
| **Setup** | ✅ Feito | Projeto configurado |
| **Core Engine** | ✅ Feito | Game loop, entities, rendering |
| **Assets** | 4-6h | Criar/obter sprites e sons |
| **Gameplay** | 6-8h | Spawn, colisões, power-ups, boss |
| **Polish** | 3-4h | UI refinements, otimizações |
| **Testing** | 2-3h | Testar em dispositivos |
| **Build APK** | 0.5h | Gerar APK final |
| **Total** | **~16-22h** | Para APK jogável |

---

## 📚 Recursos Úteis

### Documentação Android
- SurfaceView: https://developer.android.com/reference/android/view/SurfaceView
- Canvas: https://developer.android.com/reference/android/graphics/Canvas
- Bitmap: https://developer.android.com/reference/android/graphics/Bitmap
- SoundPool: https://developer.android.com/reference/android/media/SoundPool

### Tutoriais de Jogos Android
- Game Loop Pattern: https://developer.android.com/games/guides
- 2D Game Development: https://www.raywenderlich.com/android-game-development

### Assets Gratuitos
- OpenGameArt: https://opengameart.org/
- Kenney Assets: https://kenney.nl/
- itch.io Game Assets: https://itch.io/game-assets

---

## 💡 Dicas

1. **Não tente fazer tudo perfeito na primeira vez** - Faça funcionar primeiro, otimize depois
2. **Teste frequentemente** - Build e teste no dispositivo a cada funcionalidade implementada
3. **Use placeholders** - Comece com retângulos coloridos, adicione sprites depois
4. **Performance primeiro** - Mantenha 60 FPS estável antes de adicionar features complexas
5. **Versionamento** - Use Git para versionar o código

---

## 🎮 Contato

Projeto: Calango Run Deluxe  
Desenvolvedor: nildoxz  
🇧🇷 Brazilian Indie Game
