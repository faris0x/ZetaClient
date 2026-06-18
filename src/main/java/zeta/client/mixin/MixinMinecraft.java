package zeta.client.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zeta.client.Zeta;
import zeta.client.gui.GuiHandler;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Inject(method = "startGame", at = @At("RETURN"))
    private void onStartGame(CallbackInfo ci) {
        System.out.println("[Zeta Client] Starting v0.1.0");
        GuiHandler.setMc((Minecraft)(Object)this);
        new Zeta().initialize();
    }
}
