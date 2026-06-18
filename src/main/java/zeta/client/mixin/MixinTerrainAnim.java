package zeta.client.mixin;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zeta.client.config.PerfSettings;

@Mixin(TextureAtlasSprite.class)
public class MixinTerrainAnim {

    @Inject(method = "updateAnimation", at = @At("HEAD"), cancellable = true)
    private void cancelAnimation(CallbackInfo ci) {
        if (PerfSettings.disableTerrainAnim) {
            ci.cancel();
        }
    }
}
