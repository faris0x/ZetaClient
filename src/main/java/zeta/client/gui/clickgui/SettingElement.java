package zeta.client.gui.clickgui;

import zeta.client.nanovg.NanoVGManager;

public abstract class SettingElement {

    protected final String name;
    protected float x, y;
    protected final float width;
    protected static final float HEIGHT = 24;

    public SettingElement(String name, float width) {
        this.name = name;
        this.width = width;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getHeight() { return HEIGHT; }

    public abstract void renderNvg(NanoVGManager nvg, int mouseX, int mouseY);

    public void mouseClicked(int mouseX, int mouseY, int button) {}

    public void mouseReleased(int mouseX, int mouseY, int button) {}
}
