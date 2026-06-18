package zeta.client;

import zeta.client.config.ConfigManager;
import zeta.client.event.EventBus;
import zeta.client.gui.FontManager;
import zeta.client.gui.hud.HudManager;
import zeta.client.input.KeybindManager;
import zeta.client.mod.ModManager;
import zeta.client.nanovg.NanoVGManager;

import java.io.File;

public class Zeta {

    private static Zeta instance;

    private final EventBus eventBus;
    private final ModManager modManager;
    private final ConfigManager configManager;
    private final KeybindManager keybindManager;
    private final NanoVGManager nvgManager;
    private final HudManager hudManager;

    public Zeta() {
        instance = this;
        eventBus = new EventBus();
        modManager = new ModManager();
        configManager = new ConfigManager(new File("."));
        keybindManager = new KeybindManager();
        eventBus.register(keybindManager);
        nvgManager = new NanoVGManager();
        hudManager = new HudManager();
    }

    public void initialize() {
        FontManager.init();
        modManager.add(new zeta.client.mod.BlockOutlineMod());
        modManager.add(new zeta.client.mod.ToggleSprintMod());
        modManager.add(new zeta.client.mod.FpsUncapMod());
        eventBus.register(modManager.get("Toggle Sprint"));
        configManager.load(modManager);
        modManager.enableAll();

        if (nvgManager.init()) {
            System.out.println("[Zeta Client] NanoVG initialized");
            nvgManager.loadFontFromResource("dm-sans", "fonts/DMSans-Thin.ttf");
        }

        configManager.loadHud(hudManager);
        hudManager.setConfigManager(configManager);
        hudManager.setModManager(modManager);

        System.out.println("[Zeta Client] Loaded " + modManager.getMods().size() + " mods");
    }

    public static Zeta getInstance() {
        return instance;
    }

    public EventBus getEventBus() { return eventBus; }
    public ModManager getModManager() { return modManager; }
    public ConfigManager getConfigManager() { return configManager; }
    public KeybindManager getKeybindManager() { return keybindManager; }
    public NanoVGManager getNvgManager() { return nvgManager; }
    public HudManager getHudManager() { return hudManager; }
}
