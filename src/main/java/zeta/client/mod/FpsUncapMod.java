package zeta.client.mod;

import zeta.client.config.PerfSettings;

public class FpsUncapMod extends Mod {
    public FpsUncapMod() {
        super("FPS Uncap", "Removes the 120 FPS limit", Category.PLAYER);
    }
    @Override public void onEnable() { PerfSettings.fpsUncap = true; }
    @Override public void onDisable() { PerfSettings.fpsUncap = false; }
}
