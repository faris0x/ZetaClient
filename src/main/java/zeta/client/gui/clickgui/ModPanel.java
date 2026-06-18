package zeta.client.gui.clickgui;

import org.lwjgl.input.Mouse;
import zeta.client.nanovg.NanoVGManager;

import java.util.ArrayList;
import java.util.List;

public class ModPanel {

    private final List<ModButton> buttons = new ArrayList<>();
    private float scrollOffset;
    private float targetScroll;
    private float displayScroll;

    private float x, y, width, height;
    private static final int BG_COLOR = 0xBB151515;
    private static final float SCROLL_SPEED = 20;
    private static final int PADDING = 4;

    public void setBounds(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void setButtons(List<ModButton> buttons) {
        this.buttons.clear();
        this.buttons.addAll(buttons);
    }

    public void handleMouseScroll() {
        int wheel = Mouse.getDWheel();
        if (wheel != 0) {
            targetScroll -= Math.signum(wheel) * SCROLL_SPEED;
            float totalHeight = 0;
            for (ModButton b : buttons) totalHeight += b.getHeight();
            float maxScroll = Math.max(0, totalHeight - height + PADDING);
            targetScroll = Math.max(0, Math.min(maxScroll, targetScroll));
        }
    }

    public void renderNvg(NanoVGManager nvg, int mouseX, int mouseY) {
        displayScroll += (targetScroll - displayScroll) * 0.2f;
        if (Math.abs(targetScroll - displayScroll) < 0.1f) displayScroll = targetScroll;

        nvg.scissor(x, y, width, height);
        nvg.drawRoundedRect(x, y, width, height, 0, BG_COLOR);

        float drawY = y + PADDING - displayScroll;

        for (ModButton button : buttons) {
            float buttonH = button.getHeight();
            if (drawY + buttonH > y && drawY < y + height) {
                button.setPosition(x + 2, drawY);
                button.renderNvg(nvg, mouseX, mouseY);
            }
            drawY += buttonH;
        }

        nvg.resetScissor();
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (mouseX < x || mouseX > x + width || mouseY < y || mouseY > y + height) return;
        for (ModButton modButton : buttons) {
            modButton.mouseClicked(mouseX, mouseY, button);
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int button) {
        for (ModButton modButton : buttons) {
            modButton.mouseReleased(mouseX, mouseY, button);
        }
    }
}
