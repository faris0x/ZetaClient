package zeta.client.gui.clickgui;

import zeta.client.mod.Mod;
import zeta.client.nanovg.NanoVGManager;
import zeta.client.gui.notify.Notifications;

import java.util.ArrayList;
import java.util.List;

public class ModButton {

    private final Mod mod;
    private float x, y;
    private final float width;
    private static final float HEIGHT = 26;
    private static final float GEAR_SIZE = 12;

    private boolean expanded;
    private final List<SettingElement> settings = new ArrayList<>();

    private float hoverProgress;
    private static final int BG_COLOR = 0xAA222222;
    private static final int HOVER_COLOR = 0xAA333333;

    public ModButton(Mod mod, float width) {
        this.mod = mod;
        this.width = width;
    }

    public void addSetting(SettingElement setting) {
        settings.add(setting);
    }

    public List<SettingElement> getSettings() { return settings; }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Mod getMod() { return mod; }
    public float getWidth() { return width; }

    public float getHeight() {
        float h = HEIGHT;
        if (expanded) {
            for (SettingElement s : settings) {
                h += s.getHeight() + 2;
            }
            h += 2;
        }
        return h;
    }

    public float getY() { return y; }

    public void renderNvg(NanoVGManager nvg, int mouseX, int mouseY) {
        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + HEIGHT;

        hoverProgress += (hovered ? 1.0f : -1.0f) * 0.15f;
        hoverProgress = Math.max(0, Math.min(1, hoverProgress));

        int bg = RenderUtils.lerpColor(BG_COLOR, HOVER_COLOR, hoverProgress);
        nvg.drawRoundedRect(x, y, width, HEIGHT, 4, bg);

        String status = mod.isEnabled() ? "[ON]" : "[OFF]";
        int statusColor = mod.isEnabled() ? 0xFF55FF55 : 0xFFFF5555;
        nvg.drawText(status, x + 6, y + HEIGHT / 2 - 1, statusColor, 12, "dm-sans");

        nvg.drawText(mod.getName(), x + 52, y + HEIGHT / 2 - 1, 0xCCCCCC, 12, "dm-sans");

        if (!settings.isEmpty()) {
            String gear = expanded ? "v" : ">";
            nvg.drawText(gear, x + width - 18, y + HEIGHT / 2 - 1, 0x888888, 14, "dm-sans");
        }

        if (expanded) {
            float settingY = y + HEIGHT + 2;
            for (SettingElement setting : settings) {
                setting.setPosition(x + 2, settingY);
                setting.renderNvg(nvg, mouseX, mouseY);
                settingY += setting.getHeight() + 2;
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (button != 0) return;

        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + HEIGHT) {
            float gearX = x + width - GEAR_SIZE - 10;
            if (!settings.isEmpty() && mouseX >= gearX && mouseX <= gearX + GEAR_SIZE + 10) {
                expanded = !expanded;
                return;
            }
            mod.toggle();
            Notifications.show(mod.getName() + " " + (mod.isEnabled() ? "\u00a7aON" : "\u00a7cOFF"));
            return;
        }

        if (expanded) {
            for (SettingElement setting : settings) {
                setting.mouseClicked(mouseX, mouseY, button);
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int button) {
        for (SettingElement setting : settings) {
            setting.mouseReleased(mouseX, mouseY, button);
        }
    }
}
