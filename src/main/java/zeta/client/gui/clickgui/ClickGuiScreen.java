package zeta.client.gui.clickgui;

import net.minecraft.client.gui.GuiScreen;
import zeta.client.Zeta;
import zeta.client.config.BlockOutlineSettings;
import zeta.client.mod.Mod;
import zeta.client.nanovg.NanoVGManager;
import zeta.client.gui.notify.Notifications;

import java.util.ArrayList;
import java.util.List;

public class ClickGuiScreen extends GuiScreen {

    private final CategoryTab tab = new CategoryTab();
    private final ModPanel panel = new ModPanel();
    private final List<ModButton> modButtons = new ArrayList<>();

    private float animProgress;
    private static final int ACCENT = 0xFFFF5555;

    @Override
    public void initGui() {
        buildModButtons();
        float guiX = width * 0.15f;
        float guiY = height * 0.12f;
        float guiW = width * 0.7f;
        float guiH = height * 0.76f;
        tab.setMods(modButtons);
        tab.setBounds(guiX, guiY, guiW);
        panel.setBounds(guiX, guiY + tab.getHeight(), guiW, guiH - tab.getHeight());
        panel.setButtons(tab.getSelectedButtons());
    }

    private void buildModButtons() {
        modButtons.clear();
        for (Mod mod : Zeta.getInstance().getModManager().getMods()) {
            ModButton btn = new ModButton(mod, width * 0.7f - 4);
            addSettings(btn, mod);
            modButtons.add(btn);
        }
    }

    private void addSettings(ModButton btn, Mod mod) {
        if (mod.getName().equals("Block Outlines")) {
            float settingW = btn.getWidth() - 6;
            btn.addSetting(new SliderSetting("Line Width", BlockOutlineSettings.lineWidth, 1, 10, settingW));
            btn.addSetting(new SliderSetting("Red", BlockOutlineSettings.red, 0, 1, settingW));
            btn.addSetting(new SliderSetting("Green", BlockOutlineSettings.green, 0, 1, settingW));
            btn.addSetting(new SliderSetting("Blue", BlockOutlineSettings.blue, 0, 1, settingW));
            btn.addSetting(new SliderSetting("Alpha", BlockOutlineSettings.alpha, 0, 1, settingW));
        }
    }

    private void syncSettingsToConfig() {
        for (ModButton btn : modButtons) {
            if (btn.getSettings().isEmpty()) continue;
            List<SettingElement> settings = btn.getSettings();
            if (btn.getMod().getName().equals("Block Outlines") && settings.size() >= 5) {
                BlockOutlineSettings.lineWidth = ((SliderSetting) settings.get(0)).getValue();
                BlockOutlineSettings.red = ((SliderSetting) settings.get(1)).getValue();
                BlockOutlineSettings.green = ((SliderSetting) settings.get(2)).getValue();
                BlockOutlineSettings.blue = ((SliderSetting) settings.get(3)).getValue();
                BlockOutlineSettings.alpha = ((SliderSetting) settings.get(4)).getValue();
            }
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        animProgress += 0.1f;
        animProgress = Math.min(1, animProgress);

        float alpha = 1 - (1 - animProgress) * (1 - animProgress);
        float scale = 0.9f + alpha * 0.1f;

        NanoVGManager nvg = Zeta.getInstance().getNvgManager();
        if (nvg.getCtx() != 0) {
            nvg.setupAndDraw(() -> {
                nvg.drawRoundedRect(0, 0, width, height, 0, (int)(alpha * 0.8f * 255) << 24 | 0x000A0A0A);

                float guiX = width * 0.15f;
                float guiY = height * 0.12f;
                float guiW = width * 0.7f;
                float panelH = height * 0.76f - tab.getHeight();

                float cx = width / 2f;
                float cy = height / 2f;

                nvg.nvgSave();
                nvg.nvgTranslate(cx, cy);
                nvg.nvgScale(scale, scale);
                nvg.nvgTranslate(-cx, -cy);
                nvg.nvgGlobalAlpha(alpha);

                nvg.drawDropShadow(guiX - 4, guiY - 4, guiW + 8, panelH + tab.getHeight() + 8, 16, 0x44000000);

                nvg.drawRoundedRect(guiX, guiY, guiW, 24, 6, 0xDD1A1A1A);
                nvg.drawText("Zeta Client", guiX + 8, guiY + 16, ACCENT, 14, "dm-sans");

                tab.renderNvg(nvg, mouseX, mouseY);
                panel.setButtons(tab.getSelectedButtons());
                panel.renderNvg(nvg, mouseX, mouseY);

                nvg.nvgResetTransform();
                nvg.nvgGlobalAlpha(1f);

                Notifications.render(nvg, width);
            });
        }

        handleDragging(mouseX);
    }

    private void handleDragging(int mouseX) {
        for (ModButton btn : modButtons) {
            for (SettingElement s : btn.getSettings()) {
                if (s instanceof SliderSetting && ((SliderSetting) s).isDragging()) {
                    ((SliderSetting) s).updateValueFromMouse(mouseX);
                    syncSettingsToConfig();
                }
            }
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        tab.mouseClicked(mouseX, mouseY, mouseButton);
        panel.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        panel.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void handleMouseInput() {
        try { super.handleMouseInput(); } catch (Exception e) {}
        panel.handleMouseScroll();
    }

    @Override
    public boolean doesGuiPauseGame() { return false; }
}
