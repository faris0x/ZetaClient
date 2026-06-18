package zeta.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiMainMenu.class)
public class MixinGuiMainMenu {

    @Inject(method = "drawScreen", at = @At("RETURN"))
    private void onDrawScreen(int mouseX, int mouseY, float partialTicks, CallbackInfo ci) {
        Minecraft mc = Minecraft.getMinecraft();
        String text = "Zeta Client v0.1.0";
        int x = mc.currentScreen.width - mc.fontRendererObj.getStringWidth(text) - 2;
        mc.fontRendererObj.drawString(text, x, 2, 0xFF5555, false);
    }
}
