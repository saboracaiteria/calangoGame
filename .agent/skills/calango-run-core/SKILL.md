# Calango Run Core - Project Skill

> Expert instruction set for the "Calango Run Deluxe" project. Covers architecture, entity physics, SDK compliance, and responsive design patterns.

---

## 🏗️ Architecture Overview

Calango Run is a high-fidelity 2D endless runner built on **Vanilla JavaScript**, **HTML5 Canvas**, and **TailwindCSS**. It adheres to the **Entity-Component-ish** pattern within a single-file environment (`index.html`) to maximize loading speed and platform compatibility.

### Tech Stack
-   **Engine:** Custom 2D Canvas Engine (60 FPS target).
-   **Styling:** TailwindCSS (UI Overlay) + Custom CSS (CRT/Vignette).
-   **SDK:** CrazyGames SDK v3 (Asynchronous).
-   **Audio:** Custom 8-bit Oscillator-based Audio Engine (No external .mp3 files).

---

## 🏃 Core Logic & Physics

### Dino (Calango) State Machine
-   **X-Position:** Fixed (50px mobile / 150px desktop).
-   **Gravity:** `1.0` | **Jump Force:** `-20`.
-   **Collision Box:**
    -   *Normal:* `56x60` px.
    -   *Duck (Slide):* `56x34` px.
-   **Power-Ups:**
    -   `pepper`: Multiplies `gameSpeed` by 1.5x, makes invulnerable, and draws flame particles.
    -   `magnet`: Pulls coins/items from a `w * 2` radius.
    -   `leaf`: Slows down gravity during fall (`dy > 0`).

### Procedural Spawning
Obstacles and items are spawned based on `frames`.
-   **Difficulty Scaling:** `gameSpeed` starts at 7 and increases over time.
-   **Obstacle Types:** Cacti (Low/High), Birds (Flying), Ground Spikes.

---

## ⚡ CrazyGames SDK Integration (v3)

All implementations MUST be asynchronous to prevent platform-level race conditions.

### 1. Mandatory Sequence
Always use `CrazyGamesEngine.init()` which wraps `await sdk.init()`. 
**Critical:** `loadingStart()` must be called *only* after `init()` success.

### 2. Automatic Checklist Approval
Use the `triggerChecklist()` method to force-call required modules during initialization. This ensures the "Requirements" dots in the portal turn green immediately.

```javascript
// Modules to trigger:
this.sdk.game.happytime();
this.sdk.game.inviteLink({ roomName: "check" });
this.sdk.user.getUser();
this.sdk.data.setItem('cg_test', '1');
```

### 3. Save System (Hybrid)
-   **Primary:** `sdk.data.setItem` (CrazyGames Cloud Save).
-   **Secondary:** `localStorage` (Offline Fallback).
-   Data Structure: `coins`, `highScore`, `level`, `unlocked` (array), `eqSkin`, `eqHat`.

---

## 🎨 Asset & UI Guidelines

### Resizing Requirements
Portal covers must follow the "Safe Zone" rule (avoid top-left corner).
-   **Landscape (16:9):** 1920x1080 (Min) | Use 2560x1440 for flexible cropping.
-   **Portrait (4:6):** 800x1200.
-   **Square (1:1):** 800x800.

### Aesthetic Rules
-   **Pixel Art:** Always use `image-rendering: pixelated` on the canvas.
-   **CRTEffect:** Maintain the `crt-overlay` and `vignette` divs for the premium/retro look.
-   **Bilingual:** Every UI string must use the `t()` helper function (PT/EN support).

---

## 📱 Mobile Optimization
-   **Touch Handling:** `touch-action: none` on body.
-   **Controls:** Floating buttons `btnJump`, `btnDuck` positioned with `env(safe-area-inset-bottom)`.
-   **Canvas Resize:** `resizeCanvas()` must recalculate `GROUND_Y` and adjust entity Y positions proportionally to prevent stuck characters.

---

## 🛠️ Common Tasks

### Adding a new Skin/Hat
1.  Add entry to `shopItems` object.
2.  Update `draw()` method in the `dino` object to handle the specific rendering logic for that ID.
3.  Add the localized name to the `translations` object.

### Triggering a Milestone (HappyTime)
Call `CrazyGamesEngine.happyTime()` only on high-impact moments:
-   Defeating a Boss.
-   Reaching a new Level.
-   Unlocking a legendary skin.
