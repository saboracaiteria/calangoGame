# Plano: Converter Calango Run Deluxe para Android Nativo (Kotlin/Java)

## Visão Geral
Reescrever o jogo endless runner 2D de HTML5 Canvas para Android nativo usando Kotlin com SurfaceView + Canvas API, criando um APK completo e funcional.

---

## Task 1: Configuração do Projeto Android

### 1.1 Criar Estrutura do Projeto Android
Criar projeto Android com a seguinte estrutura:
```
CalangoRun/
├── app/
│   ├── src/main/
│   │   ├── java/com/calangorun/
│   │   │   ├── MainActivity.kt
│   │   │   ├── game/
│   │   │   │   ├── GameView.kt (SurfaceView principal)
│   │   │   │   ├── GameLoop.kt (thread do jogo)
│   │   │   │   └── InputHandler.kt (touch/teclado)
│   │   │   ├── entities/
│   │   │   │   ├── Player.kt (Calango)
│   │   │   │   ├── Obstacle.kt
│   │   │   │   ├── Coin.kt
│   │   │   │   ├── PowerUp.kt
│   │   │   │   └── Boss.kt
│   │   │   ├── managers/
│   │   │   │   ├── GameManager.kt
│   │   │   │   ├── ScoreManager.kt
│   │   │   │   ├── ShopManager.kt
│   │   │   │   └── AudioManager.kt
│   │   │   ├── rendering/
│   │   │   │   ├── Sprite.kt
│   │   │   │   ├── Animation.kt
│   │   │   │   ├── Background.kt
│   │   │   │   └── ParticleSystem.kt
│   │   │   └── ui/
│   │   │       ├── MainMenu.kt
│   │   │       ├── ShopScreen.kt
│   │   │       └── GameOverScreen.kt
│   │   ├── res/
│   │   │   ├── drawable/ (sprites do jogo)
│   │   │   ├── values/ (strings, cores)
│   │   │   └── raw/ (sons/música)
│   │   └── AndroidManifest.xml
│   └── build.gradle
└── build.gradle (project level)
```

### 1.2 Configurar build.gradle
```gradle
plugins {
    id 'com.android.application'
    id 'org.jetbrains.kotlin.android'
}

android {
    namespace 'com.calangorun'
    compileSdk 34
    
    defaultConfig {
        applicationId "com.calangorun.deluxe"
        minSdk 24
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }
    
    buildFeatures {
        viewBinding true
    }
}
```

---

## Task 2: Migrar Assets Visuais

### 2.1 Extrair Sprites do HTML/Canvas
- Analisar `index.html` (linhas 489+) para identificar como os sprites são desenhados
- Converter desenhos procedurais (canvas draw calls) para bitmaps PNG
- Criar spritesheets para animações do Calango (correr, pular, deslizar)
- Extrair/criar assets para: obstáculos, moedas, power-ups, chefes, backgrounds

### 2.2 Otimizar para Android
- Colocar assets em `res/drawable-nodpi/`
- Usar formato WebP ou PNG otimizado
- Criar diferentes densidades se necessário (mdpi, hdpi, xhdpi, xxhdpi)

---

## Task 3: Implementar Game Engine Core

### 3.1 GameView.kt (SurfaceView Principal)
```kotlin
class GameView(context: Context, attrs: AttributeSet) : SurfaceView(context, attrs), 
    SurfaceHolder.Callback {
    
    private val gameLoop = GameLoop(this)
    private val inputHandler = InputHandler(context)
    
    override fun surfaceCreated(holder: SurfaceHolder) {
        gameLoop.start()
    }
    
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        gameLoop.running = false
    }
    
    fun draw(canvas: Canvas) {
        // Lógica de renderização principal
    }
}
```

### 3.2 GameLoop.kt (Thread do Jogo)
```kotlin
class GameLoop(private val gameView: GameView) : Thread() {
    @Volatile var running = false
    
    override fun run() {
        while (running) {
            val canvas = gameView.holder.lockCanvas()
            try {
                gameView.draw(canvas)
                update()
            } finally {
                gameView.holder.unlockCanvasAndPost(canvas)
            }
            // Controlar FPS (60fps = 16ms por frame)
        }
    }
}
```

### 3.3 InputHandler.kt
- Mapear touch events para ações do jogo
- Detectar: tap (pular), swipe down (deslizar), swipe left/right (mover)
- Suporte a controles na tela (botões virtuais)

---

## Task 4: Reimplementar Entidades do Jogo

### 4.1 Player.kt (Calango)
```kotlin
class Player {
    var x = 0f
    var y = 0f
    var velocityY = 0f
    var isJumping = false
    var isSliding = false
    var currentAnimation: Animation
    
    fun update(deltaTime: Float) {
        // Física de pulo (gravidade)
        // Animação de corrida/pulo/deslize
        // Detecção de colisão
    }
    
    fun jump() { /* ... */ }
    fun slide() { /* ... */ }
}
```

### 4.2 Obstacle.kt
- Tipos: cactos, pedras, animais
- Sistema de spawn baseado em pontuação
- Padrões de spawn progressivos

### 4.3 Coin.kt
- Coletáveis normais e especiais
- Efeito de ímã quando power-up ativo

### 4.4 PowerUp.kt
- Pimenta (velocidade/invencibilidade)
- Ímã (atrai moedas)
- Folha (habilidade especial)
- Timer de duração

### 4.5 Boss.kt
- Spawn a cada 1000 pontos
- Padrões de ataque
- Sistema de vida/dano

---

## Task 5: Sistemas de Gerenciamento

### 5.1 GameManager.kt
```kotlin
object GameManager {
    var gameState: GameState = GameState.MENU
    var score: Int = 0
    var highScore: Int = 0
    var coins: Int = 0
    var level: Int = 1
    
    fun startGame()
    fun gameOver()
    fun pause()
    fun resume()
}
```

### 5.2 ScoreManager.kt
- Sistema de pontuação progressiva
- Multiplicadores
- Salvamento em SharedPreferences

### 5.3 ShopManager.kt
- Sistema de compras com moedas
- Skins desbloqueáveis
- Chapéus e trilhas
- Persistência em SharedPreferences/Room DB

### 5.4 AudioManager.kt
- Efeitos sonoros (pulo, moeda, colisão)
- Música de fundo
- Controles de volume

---

## Task 6: Sistemas Visuais Avançados

### 6.1 Background.kt
- Parallax scrolling com múltiplas camadas
- Ciclo dia/noite (baseado em score/tempo)
- Tempestades de areia (efeitos de partículas)
- Chuva tropical (sistema de partículas)

### 6.2 ParticleSystem.kt
```kotlin
class ParticleSystem {
    private val particles = mutableListOf<Particle>()
    
    fun emit(x: Float, y: Float, type: ParticleType)
    fun update(deltaTime: Float)
    fun draw(canvas: Canvas)
}
```

### 6.3 Animation.kt
```kotlin
class Animation(
    private val frames: List<Bitmap>,
    private val frameDuration: Long
) {
    private var currentFrame = 0
    fun update(deltaTime: Long): Bitmap
}
```

---

## Task 7: Telas de UI

### 7.1 MainActivity.kt
```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Setup fullscreen, imersivo
    }
}
```

### 7.2 Telas Principais
- **Menu Principal**: Logo, botões (Jogar, Loja, Configurações)
- **HUD em Jogo**: Score, moedas, botões de ação (jump, slide, power-ups)
- **Game Over**: Score final, high score, botão replay
- **Loja**: Grid de itens, preview, botões de compra
- **Configurações**: Idioma (PT/EN), volume, qualidade gráfica

### 7.3 Layout XML
Usar ViewBinding para telas de UI, Canvas para gameplay.

---

## Task 8: Internacionalização

### 8.1 strings.xml (Português)
```xml
<resources>
    <string name="app_name">Calango Run Deluxe</string>
    <string name="play">Jogar</string>
    <string name="shop">Loja</string>
    <!-- ... -->
</resources>
```

### 8.2 strings.xml (Inglês - values-en/)
```xml
<resources>
    <string name="play">Play</string>
    <string name="shop">Shop</string>
    <!-- ... -->
</resources>
```

---

## Task 9: Otimização e Performance

### 9.1 Object Pooling
- Reutilizar obstáculos, moedas, partículas
- Evitar garbage collection durante gameplay

### 9.2 Sprite Batching
- Minimizar chamadas de draw()
- Agrupar renders do mesmo tipo

### 9.3 Memory Management
- Reciclar bitmaps não usados
- Usar `Bitmap.Config.RGB_565` para menor consumo

### 9.4 Performance Targets
- 60 FPS estável
- Uso de memória < 150MB
- APK size < 50MB

---

## Task 10: Build e Teste

### 10.1 Configurar AndroidManifest.xml
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android">
    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:theme="@style/Theme.AppCompat.NoActionBar">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:screenOrientation="landscape"
            android:configChanges="orientation|screenSize">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
```

### 10.2 Gerar APK
```bash
./gradlew assembleDebug     # APK de debug
./gradlew assembleRelease   # APK para publicação (requer signing)
```

### 10.3 Testar em Dispositivo
- Testar em diferentes tamanhos de tela
- Testar performance (profiling)
- Testar controles touch

---

## Task 11: Migração da Lógica do Jogo (index.html → Kotlin)

### 11.1 Analisar Código Fonte HTML
- Ler `index.html` linhas 489-4707 (código JavaScript completo)
- Identificar:
  - Sistema de física (gravidade, velocidade, colisão)
  - Lógica de spawn de obstáculos
  - Sistema de power-ups
  - Lógica do boss
  - Progressão de níveis
  - Sistema de moedas

### 11.2 Traduzir Lógica para Kotlin
- Converter funções JS para métodos Kotlin
- Adaptar `requestAnimationFrame` para game loop
- Converter Canvas API web para Android Canvas API
- Traduzir event listeners para touch events Android

### 11.3 Mapeamento de APIs
| JavaScript Canvas | Android Canvas |
|-------------------|----------------|
| `ctx.drawImage()` | `canvas.drawBitmap()` |
| `ctx.fillRect()` | `canvas.drawRect()` |
| `ctx.fillStyle` | `Paint().color` |
| `requestAnimationFrame()` | Thread + `lockCanvas()` |
| `addEventListener('keydown')` | `onTouchEvent()` |

---

## Ordem de Execução Sugerida

1. **Setup** (Tasks 1-2): Configurar projeto e assets
2. **Core Engine** (Tasks 3-4): Game loop, player, entidades básicas
3. **Gameplay** (Tasks 5-6): Score, power-ups, backgrounds, partículas
4. **UI/UX** (Tasks 7-8): Telas, internacionalização
5. **Polish** (Task 9): Otimizações
6. **Build** (Tasks 10-11): Compilar, testar, gerar APK final

---

## Estimativa de Esforço

- **Setup inicial**: 1-2 horas
- **Engine core + Player**: 4-6 horas
- **Entidades + Sistemas**: 6-8 horas
- **UI + Telas**: 3-4 horas
- **Assets visuais**: 4-6 horas
- **Testes + Debug**: 2-3 horas
- **Total**: ~20-30 horas de desenvolvimento

---

## Próximos Passos Imediatos

1. Instalar Android Studio (se ainda não tiver)
2. Criar projeto Android com Kotlin
3. Configurar estrutura de pastas
4. Começar pela implementação do GameView + GameLoop
5. Implementar Player básico com sprite e pulo

**Quer que eu comece a executar este plano agora?**