package zeta.client.mixin;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import zeta.client.config.BlockOutlineSettings;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {

    @Inject(method = "drawSelectionBox", at = @At("HEAD"), cancellable = true)
    private void onDrawSelectionBox(EntityPlayer player, MovingObjectPosition mop, int count, float partialTicks, CallbackInfo ci) {
        if (!BlockOutlineSettings.enabled) return;
        if (count != 0 || mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) return;

        ci.cancel();

        double px = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double py = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double pz = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        int bx = mop.getBlockPos().getX();
        int by = mop.getBlockPos().getY();
        int bz = mop.getBlockPos().getZ();

        double x = bx - px;
        double y = by - py;
        double z = bz - pz;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableTexture2D();
        GlStateManager.depthMask(false);
        GL11.glLineWidth(BlockOutlineSettings.lineWidth);

        if (BlockOutlineSettings.throughWalls) {
            GlStateManager.disableDepth();
        }

        GlStateManager.color(BlockOutlineSettings.red, BlockOutlineSettings.green,
            BlockOutlineSettings.blue, BlockOutlineSettings.alpha);

        Tessellator tes = Tessellator.getInstance();
        WorldRenderer wr = tes.getWorldRenderer();
        wr.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION);

        wr.pos(x, y, z).endVertex(); wr.pos(x + 1, y, z).endVertex();
        wr.pos(x + 1, y, z).endVertex(); wr.pos(x + 1, y, z + 1).endVertex();
        wr.pos(x + 1, y, z + 1).endVertex(); wr.pos(x, y, z + 1).endVertex();
        wr.pos(x, y, z + 1).endVertex(); wr.pos(x, y, z).endVertex();

        wr.pos(x, y + 1, z).endVertex(); wr.pos(x + 1, y + 1, z).endVertex();
        wr.pos(x + 1, y + 1, z).endVertex(); wr.pos(x + 1, y + 1, z + 1).endVertex();
        wr.pos(x + 1, y + 1, z + 1).endVertex(); wr.pos(x, y + 1, z + 1).endVertex();
        wr.pos(x, y + 1, z + 1).endVertex(); wr.pos(x, y + 1, z).endVertex();

        wr.pos(x, y, z).endVertex(); wr.pos(x, y + 1, z).endVertex();
        wr.pos(x + 1, y, z).endVertex(); wr.pos(x + 1, y + 1, z).endVertex();
        wr.pos(x + 1, y, z + 1).endVertex(); wr.pos(x + 1, y + 1, z + 1).endVertex();
        wr.pos(x, y, z + 1).endVertex(); wr.pos(x, y + 1, z + 1).endVertex();

        tes.draw();

        if (BlockOutlineSettings.throughWalls) {
            GlStateManager.enableDepth();
        }

        GlStateManager.depthMask(true);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }
}
