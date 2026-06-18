package zeta.client.nanovg;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public class NanoVGManager {

    private long ctx;
    private final List<ByteBuffer> fontBuffers = new ArrayList<>();
    private static final int DEFAULT_FLAGS = NanoVG.NVG_ANTIALIAS | NanoVG.NVG_STENCIL_STROKES;

    public boolean init() {
        if (!NanoVG.isAvailable()) return false;
        try {
            ctx = NanoVG.nvgCreate(DEFAULT_FLAGS);
            if (ctx == 0) {
                System.out.println("[NanoVG] nvgCreate returned null");
                return false;
            }
            System.out.println("[NanoVG] Context created: " + Long.toHexString(ctx));
            return true;
        } catch (Throwable t) {
            System.out.println("[NanoVG] Context creation failed: " + t.getMessage());
            return false;
        }
    }

    public void destroy() {
        if (ctx != 0) {
            NanoVG.nvgDelete(ctx);
            ctx = 0;
        }
    }

    public long getCtx() { return ctx; }

    public void setupAndDraw(Runnable task) {
        if (ctx == 0) return;

        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution sr = new ScaledResolution(mc);

        NanoVG.nvgBeginFrame(ctx, mc.displayWidth, mc.displayHeight, 1);

        float scale = (float) mc.displayWidth / (float) sr.getScaledWidth();
        NanoVG.nvgScale(ctx, scale, scale);

        task.run();

        NanoVG.nvgEndFrame(ctx);
    }

    public void drawRoundedRect(float x, float y, float w, float h, float radius, int color) {
        NanoVG.nvgBeginPath(ctx);
        NanoVG.nvgRoundedRect(ctx, x, y, w, h, radius);
        NanoVG.nvgFillColor(ctx, f(color >> 16), f(color >> 8), f(color), f(color >> 24));
        NanoVG.nvgFill(ctx);
    }

    public void drawRect(float x, float y, float w, float h, int color) {
        NanoVG.nvgBeginPath(ctx);
        NanoVG.nvgRect(ctx, x, y, w, h);
        NanoVG.nvgFillColor(ctx, f(color >> 16), f(color >> 8), f(color), f(color >> 24));
        NanoVG.nvgFill(ctx);
    }

    public void drawOutline(float x, float y, float w, float h, float strokeWidth, int color) {
        NanoVG.nvgBeginPath(ctx);
        NanoVG.nvgRect(ctx, x, y, w, h);
        NanoVG.nvgStrokeColor(ctx, f(color >> 16), f(color >> 8), f(color), f(color >> 24));
        NanoVG.nvgStrokeWidth(ctx, strokeWidth);
        NanoVG.nvgStroke(ctx);
    }

    public void drawText(String text, float x, float y, int color, float size, String font) {
        NanoVG.nvgFontSize(ctx, size);
        NanoVG.nvgFontFace(ctx, font);
        NanoVG.nvgFillColor(ctx, f(color >> 16), f(color >> 8), f(color), f(color >> 24));
        NanoVG.nvgText(ctx, x, y, text);
    }

    public void drawCenteredText(String text, float x, float y, int color, float size, String font) {
        NanoVG.nvgFontSize(ctx, size);
        NanoVG.nvgFontFace(ctx, font);
        NanoVG.nvgTextAlign(ctx, NanoVG.NVG_ALIGN_CENTER | NanoVG.NVG_ALIGN_MIDDLE);
        NanoVG.nvgFillColor(ctx, f(color >> 16), f(color >> 8), f(color), f(color >> 24));
        NanoVG.nvgText(ctx, x, y, text);
    }

    public float getTextWidth(String text, float size, String font) {
        NanoVG.nvgFontSize(ctx, size);
        NanoVG.nvgFontFace(ctx, font);
        return NanoVG.nvgGetTextWidth(ctx, text);
    }

    public void scissor(float x, float y, float w, float h) {
        NanoVG.nvgScissor(ctx, x, y, w, h);
    }

    public void resetScissor() {
        NanoVG.nvgResetScissor(ctx);
    }

    public void loadFont(String name, java.nio.ByteBuffer ttfData) {
        NanoVG.nvgCreateFontMem(ctx, name, ttfData, 0);
    }

    public void nvgSave() { NanoVG.nvgSave(ctx); }
    public void nvgRestore() { NanoVG.nvgRestore(ctx); }
    public void nvgTranslate(float x, float y) { NanoVG.nvgTranslate(ctx, x, y); }
    public void nvgScale(float x, float y) { NanoVG.nvgScale(ctx, x, y); }
    public void nvgResetTransform() { NanoVG.nvgResetTransform(ctx); }
    public void nvgGlobalAlpha(float a) { NanoVG.nvgGlobalAlpha(ctx, a); }
    public void nvgRotate(float a) { NanoVG.nvgRotate(ctx, a); }

    public void drawDropShadow(float x, float y, float w, float h, float blur, int color) {
        NanoVG.nvgBeginPath(ctx);
        NanoVG.nvgRect(ctx, x, y, w, h);
        NanoVG.nvgFillColor(ctx, f(color >> 16), f(color >> 8), f(color), f(color >> 24));
        NanoVG.nvgFill(ctx);
        NanoVG.nvgFontBlur(ctx, blur);
        NanoVG.nvgFill(ctx);
        NanoVG.nvgFontBlur(ctx, 0);
    }

    public void loadFontFromResource(String fontName, String resourcePath) {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath);
            if (is == null) {
                System.out.println("[NanoVG] Font not found: " + resourcePath);
                return;
            }
            byte[] bytes = new byte[is.available()];
            int total = 0;
            while (total < bytes.length) {
                int read = is.read(bytes, total, bytes.length - total);
                if (read < 0) break;
                total += read;
            }
            is.close();

            ByteBuffer buf = ByteBuffer.allocateDirect(bytes.length);
            buf.put(bytes);
            buf.flip();
            fontBuffers.add(buf);
            loadFont(fontName, buf);
            System.out.println("[NanoVG] Font loaded: " + fontName + " (" + bytes.length + " bytes)");
        } catch (Exception e) {
            System.out.println("[NanoVG] Font load failed: " + resourcePath + " - " + e.getMessage());
        }
    }

    private static float f(int c) {
        return (c & 0xFF) / 255f;
    }
}
