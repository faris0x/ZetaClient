package zeta.client.gui.hud;

import net.minecraft.client.Minecraft;
import zeta.client.nanovg.NanoVGManager;

public class FpsDisplay extends HudElement {

    private static final float WIDTH = 80;
    private static final float HEIGHT = 22;
    private static final int ACCENT_COLOR = 0xFFFF5555;
    private static final int VALUE_COLOR = 0xFFFFFFFF;
    private static final int LABEL_COLOR = 0xFF888888;

    public FpsDisplay() {
        super("FPS", 4, 4);
    }

    @Override
    public float getWidth() { return WIDTH; }

    @Override
    public float getHeight() { return HEIGHT; }

    @Override
    public void drawNvg(NanoVGManager nvg) {
        drawBackground(nvg, ACCENT_COLOR);

        int fps = Minecraft.getDebugFPS();
        String text = fps + " FPS";

        float tw = nvg.getTextWidth(text, 11, "dm-sans");
        nvg.drawText(text, x + getWidth() - tw - 6, y + HEIGHT / 2 - 1, VALUE_COLOR, 11, "dm-sans");
        nvg.drawText("FPS", x + ACCENT_WIDTH + 6, y + HEIGHT / 2 - 1, LABEL_COLOR, 11, "dm-sans");
    }
}
