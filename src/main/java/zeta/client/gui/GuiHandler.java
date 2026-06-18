package zeta.client.gui;

import net.minecraft.client.Minecraft;
import zeta.client.gui.clickgui.ClickGuiScreen;

public class GuiHandler {

    private static Minecraft mc;

    public static void setMc(Minecraft instance) {
        mc = instance;
    }

    public static Minecraft getMc() {
        return mc;
    }

    public static void toggleGui() {
        if (mc == null) return;
        if (mc.currentScreen instanceof ClickGuiScreen) {
            mc.displayGuiScreen(null);
        } else {
            mc.displayGuiScreen(new ClickGuiScreen());
        }
    }
}
