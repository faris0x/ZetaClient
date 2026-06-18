package zeta.client.mixin;

import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import zeta.client.config.PerfSettings;

@Mixin(MathHelper.class)
public class MixinFastMath {

    @Redirect(method = "sin", at = @At(value = "INVOKE", target = "Ljava/lang/Math;sin(D)D"), require = 0)
    private static double fastSin(double d) {
        if (!PerfSettings.fastMath) return Math.sin(d);
        return sinLookup(d);
    }

    @Redirect(method = "cos", at = @At(value = "INVOKE", target = "Ljava/lang/Math;cos(D)D"), require = 0)
    private static double fastCos(double d) {
        if (!PerfSettings.fastMath) return Math.cos(d);
        return cosLookup(d);
    }

    private static double sinLookup(double d) {
        return Math.sin(d);
    }

    private static double cosLookup(double d) {
        return Math.cos(d);
    }
}
