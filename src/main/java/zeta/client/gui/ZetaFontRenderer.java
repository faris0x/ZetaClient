package zeta.client.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class ZetaFontRenderer {

    private static final int CHAR_START = 32;
    private static final int CHAR_END = 127;
    private static final int ATLAS_SIZE = 512;

    private int textureId = -1;
    private final float[] charWidth = new float[CHAR_END];
    private final float[] charHeight = new float[CHAR_END];
    private final float[] charAdvance = new float[CHAR_END];
    private final float[] uMin = new float[CHAR_END];
    private final float[] vMin = new float[CHAR_END];
    private final float[] uMax = new float[CHAR_END];
    private final float[] vMax = new float[CHAR_END];
    private boolean initialized = false;

    public ZetaFontRenderer(String resourcePath, int fontSize) {
        try {
            InputStream is = getClass().getClassLoader()
                .getResourceAsStream(resourcePath);
            if (is == null) {
                System.out.println("[Zeta Font] Font not found: " + resourcePath);
                return;
            }
            Font awtFont = Font.createFont(Font.TRUETYPE_FONT, is);
            is.close();
            awtFont = awtFont.deriveFont(Font.PLAIN, fontSize);
            buildAtlas(awtFont);
            initialized = true;
        } catch (Exception e) {
            System.out.println("[Zeta Font] Failed: " + e.getMessage());
        }
    }

    private void buildAtlas(Font font) {
        BufferedImage atlas = new BufferedImage(ATLAS_SIZE, ATLAS_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = atlas.createGraphics();
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, ATLAS_SIZE, ATLAS_SIZE);
        g.setColor(Color.WHITE);
        g.setFont(font);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        FontMetrics fm = g.getFontMetrics();
        int x = 1, y = 1, rowH = 0;

        for (int ch = CHAR_START; ch < CHAR_END; ch++) {
            int cw = Math.max(fm.charWidth(ch), 1);
            int chH = fm.getHeight();

            if (x + cw + 1 >= ATLAS_SIZE) { x = 1; y += rowH + 1; rowH = 0; }
            if (y + chH + 1 >= ATLAS_SIZE) break;

            g.drawString(String.valueOf((char) ch), x, y + fm.getAscent());

            charWidth[ch] = cw;
            charHeight[ch] = chH;
            charAdvance[ch] = cw;
            uMin[ch] = (float) x / ATLAS_SIZE;
            vMin[ch] = (float) y / ATLAS_SIZE;
            uMax[ch] = (float) (x + cw) / ATLAS_SIZE;
            vMax[ch] = (float) (y + chH) / ATLAS_SIZE;

            x += cw + 1;
            if (chH > rowH) rowH = chH;
        }
        g.dispose();

        textureId = GL11.glGenTextures();
        GlStateManager.bindTexture(textureId);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

        int[] pixels = new int[ATLAS_SIZE * ATLAS_SIZE];
        atlas.getRGB(0, 0, ATLAS_SIZE, ATLAS_SIZE, pixels, 0, ATLAS_SIZE);
        IntBuffer buf = BufferUtils.createIntBuffer(pixels.length);
        buf.put(pixels);
        buf.flip();
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, ATLAS_SIZE, ATLAS_SIZE,
            0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buf);
    }

    public void drawString(String text, float x, float y, int color, boolean shadow) {
        if (!initialized || textureId < 0 || text == null || text.isEmpty()) return;

        float r = (color >> 16 & 255) / 255f;
        float g = (color >> 8 & 255) / 255f;
        float b = (color & 255) / 255f;
        float a = (color >> 24 & 255) / 255f;
        if (a == 0) a = 1f;

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.enableTexture2D();
        GlStateManager.bindTexture(textureId);

        Tessellator tes = Tessellator.getInstance();
        WorldRenderer wr = tes.getWorldRenderer();

        float curX = x;
        for (int i = 0; i < text.length(); i++) {
            int ch = text.charAt(i);
            if (ch < CHAR_START || ch >= CHAR_END) { curX += charAdvance[' '] > 0 ? charAdvance[' '] : 8; continue; }

            float w = charWidth[ch], h = charHeight[ch];
            if (w < 1 || h < 1) { curX += charAdvance[ch]; continue; }

            if (shadow) {
                wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
                GlStateManager.color(0, 0, 0, a * 0.7f);
                wr.pos(curX + 1, y + h + 1, 0).tex(uMin[ch], vMax[ch]).endVertex();
                wr.pos(curX + w + 1, y + h + 1, 0).tex(uMax[ch], vMax[ch]).endVertex();
                wr.pos(curX + w + 1, y + 1, 0).tex(uMax[ch], vMin[ch]).endVertex();
                wr.pos(curX + 1, y + 1, 0).tex(uMin[ch], vMin[ch]).endVertex();
                tes.draw();
            }

            wr.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
            GlStateManager.color(r, g, b, a);
            wr.pos(curX, y + h, 0).tex(uMin[ch], vMax[ch]).endVertex();
            wr.pos(curX + w, y + h, 0).tex(uMax[ch], vMax[ch]).endVertex();
            wr.pos(curX + w, y, 0).tex(uMax[ch], vMin[ch]).endVertex();
            wr.pos(curX, y, 0).tex(uMin[ch], vMin[ch]).endVertex();
            tes.draw();

            curX += charAdvance[ch];
        }

        GlStateManager.popMatrix();
    }

    public float getStringWidth(String text) {
        if (!initialized || text == null) return 0;
        float w = 0;
        for (int i = 0; i < text.length(); i++) {
            int ch = text.charAt(i);
            if (ch < CHAR_START || ch >= CHAR_END) {
                w += charAdvance[' '] > 0 ? charAdvance[' '] : 8;
            } else {
                w += charAdvance[ch];
            }
        }
        return w;
    }

    public boolean isInitialized() { return initialized; }
}
