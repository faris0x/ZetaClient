package zeta.client.gui.clickgui;

import zeta.client.nanovg.NanoVGManager;

public class SliderSetting extends SettingElement {

    private float value;
    private final float min;
    private final float max;
    private boolean dragging;

    private static final int TRACK_COLOR = 0xAA333333;
    private static final int FILL_COLOR = 0xCCFF5555;
    private static final int HANDLE_COLOR = 0xFFFFFFFF;
    private static final float HANDLE_RADIUS = 4.0f;

    public SliderSetting(String name, float value, float min, float max, float width) {
        super(name, width);
        this.value = value;
        this.min = min;
        this.max = max;
    }

    public float getValue() { return value; }
    public void setValue(float value) { this.value = Math.max(min, Math.min(max, value)); }

    @Override
    public void renderNvg(NanoVGManager nvg, int mouseX, int mouseY) {
        float trackX = x + 2;
        float trackW = width - 4;
        float trackY = y + HEIGHT / 2 - 1;
        float trackH = 4;

        nvg.drawRoundedRect(trackX, trackY, trackW, trackH, 2, TRACK_COLOR);

        float fill = (value - min) / (max - min) * trackW;
        nvg.drawRoundedRect(trackX, trackY, fill, trackH, 2, FILL_COLOR);

        float handleX = trackX + fill;
        float handleY = y + HEIGHT / 2;
        nvg.drawRoundedRect(handleX - HANDLE_RADIUS, handleY - HANDLE_RADIUS,
            HANDLE_RADIUS * 2, HANDLE_RADIUS * 2, HANDLE_RADIUS, HANDLE_COLOR);

        nvg.drawText(name + ": " + String.format("%.1f", value),
            x + 2, y + HEIGHT / 2 - 2, 0xCCAAAAAA, 11, "dm-sans");
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (button == 0 && mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + HEIGHT) {
            dragging = true;
            updateValueFromMouse(mouseX);
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int button) {
        dragging = false;
    }

    public void updateValueFromMouse(int mouseX) {
        float trackX = x + 2;
        float trackW = width - 4;
        float frac = (mouseX - trackX) / trackW;
        value = min + frac * (max - min);
        value = Math.max(min, Math.min(max, value));
    }

    public boolean isDragging() { return dragging; }
}
