package zeta.client.mixin;

import net.minecraft.client.renderer.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zeta.client.config.PerfSettings;

@Mixin(EntityRenderer.class)
public class MixinFullbright {

    @Shadow
    private int[] lightmapColors;

    @Inject(method = "updateLightmap", at = @At("HEAD"), cancellable = true)
    private void forceFullbright(float partialTicks, CallbackInfo ci) {
        if (!PerfSettings.fullbright) return;

        if (lightmapColors == null) {
            lightmapColors = new int[256];
        }
        for (int i = 0; i < lightmapColors.length; i++) {
            lightmapColors[i] = 0xFFFFFFFF;
        }
        ci.cancel();
    }
}
