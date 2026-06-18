package zeta.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zeta.client.Zeta;
import zeta.client.gui.hud.HudManager;

@Mixin(GuiIngame.class)
public class MixinGuiIngame {

    @Inject(method = "renderGameOverlay", at = @At("RETURN"))
    private void onRenderGameOverlay(float partialTicks, CallbackInfo ci) {
        Zeta zeta = Zeta.getInstance();
        if (zeta == null) return;

        HudManager hud = zeta.getHudManager();
        if (hud == null) return;

        hud.setEditMode(Keyboard.isKeyDown(Keyboard.KEY_RCONTROL));
        hud.render(zeta.getNvgManager());
    }
}
