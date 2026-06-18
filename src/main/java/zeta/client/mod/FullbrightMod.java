package zeta.client.mod;

import zeta.client.config.PerfSettings;

public class FullbrightMod extends Mod {
    public FullbrightMod() {
        super("Fullbright", "Maximum brightness everywhere", Category.VISUAL);
    }
    @Override public void onEnable() { PerfSettings.fullbright = true; }
    @Override public void onDisable() { PerfSettings.fullbright = false; }
}
