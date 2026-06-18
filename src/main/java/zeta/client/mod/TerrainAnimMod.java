package zeta.client.mod;

import zeta.client.config.PerfSettings;

public class TerrainAnimMod extends Mod {
    public TerrainAnimMod() {
        super("Terrain Animations", "Toggles lava/water texture animation (off = better FPS)", Category.VISUAL);
    }
    @Override public void onEnable() { PerfSettings.disableTerrainAnim = true; }
    @Override public void onDisable() { PerfSettings.disableTerrainAnim = false; }
}
