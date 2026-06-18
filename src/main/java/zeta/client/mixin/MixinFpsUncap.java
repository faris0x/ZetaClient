package zeta.client.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import zeta.client.config.PerfSettings;

@Mixin(Minecraft.class)
public class MixinFpsUncap {

    @Overwrite
    public int getLimitFramerate() {
        return PerfSettings.fpsUncap ? 260 : 120;
    }
}
