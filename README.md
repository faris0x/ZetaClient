# Zeta Client

Minecraft 1.8.9 PvP client using LaunchWrapper + Mixin.

## Quick Start

```bash
JAVA_HOME=~/.jdks/jdk8u492-b09 PATH="$JAVA_HOME/bin:$PATH" ./gradlew runClient --rerun-tasks
```

**Requirements:** Java 8, OpenGL 2.0+.

## Controls

| Key | Action |
|-----|--------|
| **RSHIFT** | Open/close ClickGUI |
| **RCONTROL** (hold) | HUD edit mode — drag elements |

## Features

### Mods
- **Block Outlines** — custom color/width/alpha sliders
- **Toggle Sprint** — auto-sprint
- **FPS Uncap** — 260 FPS cap

### HUD
- FPS display with dark rounded background
- XYZ coordinates overlay
- Drag to reposition (hold RCONTROL)

### Performance
- FPS uncap
- Custom block outline rendering
- Texture animation toggle (GPU savings)

## Building

```bash
JAVA_HOME=~/.jdks/jdk8u492-b09 PATH="$JAVA_HOME/bin:$PATH" ./gradlew build
```

Output: `build/libs/zeta-client-0.1.0.jar`
