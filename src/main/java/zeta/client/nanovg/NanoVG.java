package zeta.client.nanovg;

public class NanoVG {

    public static final int NVG_ANTIALIAS = 1;
    public static final int NVG_STENCIL_STROKES = 1 << 1;
    public static final int NVG_DEBUG = 1 << 2;

    public static final int NVG_ALIGN_LEFT = 1;
    public static final int NVG_ALIGN_CENTER = 2;
    public static final int NVG_ALIGN_RIGHT = 4;
    public static final int NVG_ALIGN_TOP = 8;
    public static final int NVG_ALIGN_MIDDLE = 16;
    public static final int NVG_ALIGN_BOTTOM = 32;
    public static final int NVG_ALIGN_BASELINE = 64;

    private static boolean loaded;

    static {
        try {
            System.loadLibrary("zeta-nanovg");
            loaded = true;
        } catch (Throwable t) {
            try {
                System.load("/home/faris/projects/zeta-client/native/build/libzeta-nanovg.so");
                loaded = true;
            } catch (Throwable t2) {
                System.err.println("[NanoVG] Failed to load native library: " + t2.getMessage());
            }
        }
    }

    public static boolean isAvailable() { return loaded; }

    public static native long nvgCreate(int flags);
    public static native void nvgDelete(long ctx);

    public static native void nvgBeginFrame(long ctx, int width, int height, float ratio);
    public static native void nvgEndFrame(long ctx);

    public static native void nvgBeginPath(long ctx);
    public static native void nvgRect(long ctx, float x, float y, float w, float h);
    public static native void nvgRoundedRect(long ctx, float x, float y, float w, float h, float r);
    public static native void nvgCircle(long ctx, float cx, float cy, float r);
    public static native void nvgArc(long ctx, float cx, float cy, float r, float a0, float a1, int dir);

    public static native void nvgFill(long ctx);
    public static native void nvgStroke(long ctx);

    public static native void nvgFillColor(long ctx, float r, float g, float b, float a);
    public static native void nvgStrokeColor(long ctx, float r, float g, float b, float a);
    public static native void nvgStrokeWidth(long ctx, float w);
    public static native void nvgLineCap(long ctx, int cap);
    public static native void nvgLineJoin(long ctx, int join);
    public static native void nvgMiterLimit(long ctx, float limit);

    public static native void nvgSave(long ctx);
    public static native void nvgRestore(long ctx);
    public static native void nvgReset(long ctx);
    public static native void nvgGlobalAlpha(long ctx, float alpha);

    public static native void nvgTranslate(long ctx, float x, float y);
    public static native void nvgScale(long ctx, float x, float y);
    public static native void nvgRotate(long ctx, float angle);
    public static native void nvgResetTransform(long ctx);

    public static native void nvgScissor(long ctx, float x, float y, float w, float h);
    public static native void nvgResetScissor(long ctx);

    public static native void nvgFontSize(long ctx, float size);
    public static native void nvgFontBlur(long ctx, float blur);
    public static native void nvgFontFace(long ctx, String name);
    public static native void nvgTextAlign(long ctx, int align);
    public static native void nvgTextLetterSpacing(long ctx, float spacing);
    public static native float nvgText(long ctx, float x, float y, String text);
    public static native void nvgTextBox(long ctx, float x, float y, float w, String text);
    public static native float nvgTextBounds(long ctx, float x, float y, String text, float[] bounds);
    public static native int nvgCreateFontMem(long ctx, String name, java.nio.ByteBuffer data, int freeOnClose);
    public static native float nvgGetTextWidth(long ctx, String text);
}
