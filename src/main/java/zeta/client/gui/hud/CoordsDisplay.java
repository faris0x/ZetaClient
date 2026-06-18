package zeta.client.gui.hud;

import net.minecraft.client.Minecraft;
import zeta.client.nanovg.NanoVGManager;

public class CoordsDisplay extends HudElement {

    private static final float WIDTH = 160;
    private static final float HEIGHT = 22;
    private static final int ACCENT_COLOR = 0xFF55AAFF;
    private static final int VALUE_COLOR = 0xFFFFFFFF;
    private static final int LABEL_COLOR = 0xFF888888;

    public CoordsDisplay() {
        super("Coords", 4, 28);
    }

    @Override
    public float getWidth() { return WIDTH; }

    @Override
    public float getHeight() { return HEIGHT; }

    @Override
    public void drawNvg(NanoVGManager nvg) {
        drawBackground(nvg, ACCENT_COLOR);

        Minecraft mc = Minecraft.getMinecraft();
        String xyz = String.format("%.0f %.0f %.0f",
            mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);

        nvg.drawText("XYZ", x + ACCENT_WIDTH + 6, y + HEIGHT / 2 - 1, LABEL_COLOR, 11, "dm-sans");
        float tw = nvg.getTextWidth(xyz, 11, "dm-sans");
        nvg.drawText(xyz, x + getWidth() - tw - 6, y + HEIGHT / 2 - 1, VALUE_COLOR, 11, "dm-sans");
    }
}
