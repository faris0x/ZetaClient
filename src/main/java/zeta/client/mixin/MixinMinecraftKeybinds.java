package zeta.client.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zeta.client.Zeta;
import zeta.client.event.TickEvent;

@Mixin(Minecraft.class)
public class MixinMinecraftKeybinds {

    private int tickCount;

    @Inject(method = "runTick", at = @At("RETURN"))
    private void zetaOnTick(CallbackInfo ci) {
        Zeta zeta = Zeta.getInstance();
        if (zeta != null) {
            tickCount++;
            zeta.getEventBus().post(new TickEvent(tickCount));
        }
    }
}
