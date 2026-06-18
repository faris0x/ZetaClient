package zeta.client.gui.clickgui;

import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

public class RenderUtils {

    public static void drawRect(float x, float y, float w, float h, int color) {
        setColor(color);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + w, y);
        GL11.glVertex2f(x + w, y + h);
        GL11.glVertex2f(x, y + h);
        GL11.glEnd();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawOutline(float x, float y, float w, float h, float lineWidth, int color) {
        setColor(color);
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glLineWidth(lineWidth);

        GL11.glBegin(GL11.GL_LINE_LOOP);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + w, y);
        GL11.glVertex2f(x + w, y + h);
        GL11.glVertex2f(x, y + h);
        GL11.glEnd();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawGradientRect(float x, float y, float w, float h, int topColor, int bottomColor) {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.shadeModel(GL11.GL_SMOOTH);

        GL11.glBegin(GL11.GL_QUADS);

        setColor(topColor);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + w, y);

        setColor(bottomColor);
        GL11.glVertex2f(x + w, y + h);
        GL11.glVertex2f(x, y + h);

        GL11.glEnd();

        GlStateManager.shadeModel(GL11.GL_FLAT);
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void scissor(float x, float y, float w, float h, int scaledWidth, int displayWidth, int displayHeight) {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        float scale = (float) displayWidth / scaledWidth;
        int sx = (int) (x * scale);
        int sy = (int) (displayHeight - (y + h) * scale);
        int sw = (int) (w * scale);
        int sh = (int) (h * scale);
        GL11.glScissor(sx, sy, Math.max(1, sw), Math.max(1, sh));
    }

    public static void endScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static int lerpColor(int from, int to, float progress) {
        int fa = from >> 24 & 0xFF, fr = from >> 16 & 0xFF, fg = from >> 8 & 0xFF, fb = from & 0xFF;
        int ta = to >> 24 & 0xFF, tr = to >> 16 & 0xFF, tg = to >> 8 & 0xFF, tb = to & 0xFF;
        int ra = (int) (fa + (ta - fa) * progress);
        int rr = (int) (fr + (tr - fr) * progress);
        int rg = (int) (fg + (tg - fg) * progress);
        int rb = (int) (fb + (tb - fb) * progress);
        return (ra << 24) | (rr << 16) | (rg << 8) | rb;
    }

    private static void setColor(int color) {
        float a = (color >> 24 & 0xFF) / 255f;
        float r = (color >> 16 & 0xFF) / 255f;
        float g = (color >> 8 & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;
        GL11.glColor4f(r, g, b, a);
    }
}
