package zeta.client.gui.hud;

import zeta.client.nanovg.NanoVGManager;

public abstract class HudElement {

    protected float x, y;
    protected final String name;
    protected boolean dragging;
    protected float hoverAlpha;

    private static final int BG_COLOR = 0xCC111111;
    private static final int EDIT_OUTLINE = 0x66FF5555;
    protected static final int ACCENT_WIDTH = 3;

    public HudElement(String name, float defaultX, float defaultY) {
        this.name = name;
        this.x = defaultX;
        this.y = defaultY;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public String getName() { return name; }

    public abstract float getWidth();
    public abstract float getHeight();

    public abstract void drawNvg(NanoVGManager nvg);

    public void drawNvgEdit(NanoVGManager nvg) {
        drawNvg(nvg);
        nvg.drawOutline(x - 1, y - 1, getWidth() + 2, getHeight() + 2, 1, EDIT_OUTLINE);
    }

    public void drawNvg(NanoVGManager nvg, boolean editMode, int mouseX, int mouseY) {
        boolean hovered = !editMode && contains(mouseX, mouseY);
        hoverAlpha += ((hovered || editMode) ? 1f : 0f) - hoverAlpha * 0.1f;
        hoverAlpha = Math.max(0, Math.min(1, hoverAlpha));
        float alpha = 0.4f + hoverAlpha * 0.6f;

        nvg.nvgSave();
        nvg.nvgGlobalAlpha(alpha);
        if (editMode) {
            drawNvgEdit(nvg);
        } else {
            drawNvg(nvg);
        }
        nvg.nvgGlobalAlpha(1f);
        nvg.nvgRestore();
    }

    public boolean contains(float mx, float my) {
        return mx >= x && mx <= x + getWidth() && my >= y && my <= y + getHeight();
    }

    public void startDrag() { dragging = true; }
    public void stopDrag() { dragging = false; }
    public boolean isDragging() { return dragging; }

    protected void drawBackground(NanoVGManager nvg, int accentColor) {
        float w = getWidth();
        float h = getHeight();
        nvg.drawRoundedRect(x, y, w, h, 4, BG_COLOR);
        nvg.drawRoundedRect(x, y, ACCENT_WIDTH, h, 4, accentColor);
    }

    protected void drawLabel(NanoVGManager nvg, String label, int color, float size) {
        nvg.drawText(label, x + ACCENT_WIDTH + 6, y + getHeight() / 2 - 1, color, size, "dm-sans");
    }
}
