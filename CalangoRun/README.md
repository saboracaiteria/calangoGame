# Calango Run Deluxe - Android Native

Jogo endless runner 2D reescrito em Kotlin nativo para Android usando SurfaceView e Canvas API.

## Pré-requisitos

- Android Studio (versão 2023.1.1 ou superior)
- JDK 17 ou superior
- Android SDK 34
- Kotlin 1.9.20

## Estrutura do Projeto

```
CalangoRun/
├── app/
│   ├── src/main/
│   │   ├── java/com/calangorun/
│   │   │   ├── MainActivity.kt          # Activity principal
│   │   │   ├── game/
│   │   │   │   ├── GameView.kt          # SurfaceView principal do jogo
│   │   │   │   ├── GameLoop.kt          # Thread do game loop (60 FPS)
│   │   │   │   └── InputHandler.kt      # Handler de touch events
│   │   │   ├── entities/
│   │   │   │   ├── Player.kt            # Jogador (Calango)
│   │   │   │   ├── Obstacle.kt          # Obstáculos (cactos, pedras, animais)
│   │   │   │   ├── Coin.kt              # Moedas coletáveis
│   │   │   │   ├── PowerUp.kt           # Power-ups (Pimenta, Ímã, Folha)
│   │   │   │   └── Boss.kt              # Boss battles
│   │   │   ├── managers/
│   │   │   │   ├── GameManager.kt       # Estado global do jogo
│   │   │   │   ├── ScoreManager.kt      # Persistência de pontuação
│   │   │   │   ├── ShopManager.kt       # Sistema de loja
│   │   │   │   └── AudioManager.kt      # Efeitos sonoros
│   │   │   ├── rendering/
│   │   │   │   ├── Animation.kt         # Sistema de animação
│   │   │   │   ├── Background.kt        # Parallax + dia/noite
│   │   │   │   └── ParticleSystem.kt    # Efeitos de partículas
│   │   │   └── ui/                      # Telas de UI
│   │   ├── res/                         # Resources Android
│   │   │   ├── drawable/                # Assets visuais
│   │   │   ├── values/                  # Strings PT, cores, temas
│   │   │   ├── values-en/               # Strings EN
│   │   │   └── raw/                     # Sons/música
│   │   └── AndroidManifest.xml
│   └── build.gradle
└── build.gradle (project level)
```

## Como Buildar o APK

### Opção 1: Usando Android Studio

1. Abra o Android Studio
2. Clique em **File > Open**
3. Navegue até a pasta `CalangoRun/` e selecione
4. Aguarde o Gradle sync completar
5. Clique em **Build > Build Bundle(s) / APK(s) > Build APK(s)**
6. O APK será gerado em `app/build/outputs/apk/debug/`

### Opção 2: Usando Command Line

```bash
# Navegue até a pasta do projeto
cd CalangoRun

# Build APK de debug
./gradlew assembleDebug

# Build APK de release (requer signing)
./gradlew assembleRelease

# Instalar em dispositivo conectado
./gradlew installDebug
```

O APK será gerado em:
- Debug: `app/build/outputs/apk/debug/app-debug.apk`
- Release: `app/build/outputs/apk/release/app-release.apk`

## Instalação no Dispositivo

### Método 1: USB Direct

1. Ative **Developer Options** no dispositivo Android
2. Ative **USB Debugging**
3. Conecte via USB
4. Execute: `adb install app/build/outputs/apk/debug/app-debug.apk`

### Método 2: Transferência Manual

1. Copie o APK para o dispositivo
2. Abra o arquivo APK no dispositivo
3. Permita instalação de fontes desconhecidas se necessário
4. Instale normalmente

## Características Implementadas

✅ Game loop com 60 FPS estável  
✅ SurfaceView para rendering nativo  
✅ Sistema de física (gravidade, pulo, slide)  
✅ Touch controls (tap, swipe)  
✅ Parallax scrolling com ciclo dia/noite  
✅ Sistema de partículas (poeira, faíscas, explosão, chuva, areia)  
✅ Entidades: Player, Obstacles, Coins, Power-ups, Boss  
✅ GameManager com estados (Menu, Playing, Paused, Game Over, Shop)  
✅ ScoreManager com persistência (SharedPreferences)  
✅ ShopManager para skins, chapéus e trilhas  
✅ AudioManager para efeitos sonoros  
✅ Internacionalização (Português/Inglês)  
✅ Fullscreen immersive mode  
✅ Orientação landscape  

## Próximos Passos

1. **Adicionar Sprites/Assets**: Substituir placeholders por bitmaps reais
2. **Implementar Colisões**: Detecção de colisão entre entidades
3. **Spawn System**: Sistema de spawn progressivo de obstáculos
4. **Boss Battles**: Implementar padrões de ataque do boss
5. **Power-up Effects**: Efeitos visuais e lógicos dos power-ups
6. **Sound Files**: Adicionar arquivos de áudio em `res/raw/`
7. **Icon/Launcher**: Criar ícone do app em `res/mipmap-*/`
8. **Testing**: Testar em múltiplos dispositivos e tamanhos de tela
9. **Optimization**: Object pooling, sprite batching, memory management

## Performance Targets

- 60 FPS estável
- Uso de memória < 150MB
- APK size < 50MB (sem assets)
- Min SDK: Android 7.0 (API 24)
- Target SDK: Android 14 (API 34)

## Tecnologias

- **Linguagem**: Kotlin 1.9.20
- **Min SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **Rendering**: SurfaceView + Canvas API
- **UI**: Material Components
- **Persistência**: SharedPreferences

## Licença

Projeto privado - Calango Run Deluxe

## Desenvolvedor

Criado por nildoxz  
🇧🇷 Brazilian Indie Game
