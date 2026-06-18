package zeta.client.mixin;

import net.minecraft.client.multiplayer.WorldClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import zeta.client.config.PerfSettings;

@Mixin(WorldClient.class)
public class MixinChunkThrottle {

    @ModifyConstant(method = "doVoidFogParticles", constant = @Constant(intValue = 16), require = 0)
    private int throttleChunks(int original) {
        return PerfSettings.chunkUpdates;
    }
}
