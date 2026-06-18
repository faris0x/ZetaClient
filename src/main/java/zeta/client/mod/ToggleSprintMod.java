package zeta.client.mod;

import net.minecraft.client.Minecraft;
import zeta.client.event.EventBus;
import zeta.client.event.TickEvent;

public class ToggleSprintMod extends Mod {

    public ToggleSprintMod() {
        super("Toggle Sprint", "Automatically holds sprint", Category.PLAYER);
    }

    @EventBus.Subscribe
    public void onTick(TickEvent event) {
        if (!isEnabled()) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null || mc.currentScreen != null) return;

        if (mc.thePlayer.moveForward > 0 && mc.thePlayer.getFoodStats().getFoodLevel() > 6) {
            mc.thePlayer.setSprinting(true);
        }
    }
}
