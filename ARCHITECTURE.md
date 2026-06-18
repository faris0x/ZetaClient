# Architecture

## Boot Chain

```
Tweaker → MixinBootstrap → Mixin config → Minecraft boots
  → MixinMinecraft @Inject startGame() RETURN
    → GuiHandler.setMc(mc)
    → Zeta.initialize()
      → FontManager, ModManager, ConfigManager, NanoVGManager, HudManager
      → EventBus registers KeybindManager, ToggleSprint
```

## Classloader Fix

The `tweaker-client` plugin routes project classes through `AppClassLoader` while Minecraft classes use `LaunchClassLoader`. This causes `LinkageError`. The fix uses reflection to remove `zeta` entries from `LaunchClassLoader.classLoaderExceptions`, forcing our classes into `LaunchClassLoader`.

## Rendering Layers

Three rendering tiers, all from mixin callbacks:

| Mixin | Context | Method |
|-------|---------|--------|
| `MixinGuiMainMenu` | Title screen | `mc.fontRendererObj.drawString()` |
| `MixinGuiIngame` | In-game HUD | NanoVG via `HudManager` |
| `ClickGuiScreen` | Mod menu (RSHIFT) | NanoVG via `nvg.setupAndDraw()` |

## NanoVG Stack

```
NanoVGManager (Java) → JNI (NanoVG.java)
  → zeta_nanovg_bridge.c
    → nanovg.c + nanovg_gl.h (GL2 backend)
      → OpenGL 2.0 (Minecraft's LWJGL 2 context)
```

### Font Rendering
- DM Sans Thin TTF loaded via `nvgCreateFontMem`
- `ByteBuffer` kept alive to prevent GC (avoids `stbtt` crash)
- Text via `nvgFontFace("dm-sans")` + `nvgText()`

## Event System

```
MixinMinecraftKeybinds @Inject runTick() RETURN
  → EventBus.post(TickEvent)
    → KeybindManager.onTick() → RSHIFT → GuiHandler.toggleGui()
    → ToggleSprint.onTick() → auto-sprint
```

## Mod System

`Mod` (abstract) → `onEnable()` / `onDisable()` toggle static settings fields.

Settings classes (`BlockOutlineSettings`, `PerfSettings`) hold static field values. Mixins read these fields at runtime to conditionally apply transformations.

## Adding a Mod

1. Subclass `Mod` with name, description, category
2. Override `onEnable()` / `onDisable()` to change a settings field
3. Mixin reads the settings field via its mixin callback
4. Register in `Zeta.initialize()` with `modManager.add()`
5. For tick-based mods, use `@EventBus.Subscribe` + `eventBus.register()`
