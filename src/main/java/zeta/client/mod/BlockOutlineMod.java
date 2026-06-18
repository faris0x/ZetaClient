package zeta.client.mod;

import zeta.client.config.BlockOutlineSettings;

public class BlockOutlineMod extends Mod {

    public BlockOutlineMod() {
        super("Block Outlines", "Customizable block selection outline color and width", Category.VISUAL);
    }

    @Override
    public void onEnable() {
        BlockOutlineSettings.enabled = true;
    }

    @Override
    public void onDisable() {
        BlockOutlineSettings.enabled = false;
    }
}
