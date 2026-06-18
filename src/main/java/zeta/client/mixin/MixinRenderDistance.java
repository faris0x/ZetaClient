package zeta.client.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.settings.GameSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import zeta.client.config.PerfSettings;

@Mixin(RenderGlobal.class)
public class MixinRenderDistance {

    @Redirect(method = "loadRenderers", at = @At(value = "FIELD", target = "Lnet/minecraft/client/settings/GameSettings;renderDistanceChunks:I", ordinal = 0), require = 0)
    private int overrideRenderDistance(GameSettings settings) {
        return PerfSettings.renderDistance;
    }
}
