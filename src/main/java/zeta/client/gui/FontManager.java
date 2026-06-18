package zeta.client.gui;

public class FontManager {

    private static ZetaFontRenderer fontRenderer;

    public static void init() {
        try {
            fontRenderer = new ZetaFontRenderer("fonts/DMSans-Thin.ttf", 18);
            System.out.println("[Zeta] Custom font loaded");
        } catch (Exception e) {
            System.out.println("[Zeta] Custom font failed: " + e.getMessage());
            fontRenderer = null;
        }
    }

    public static ZetaFontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public static boolean hasCustomFont() {
        return fontRenderer != null && fontRenderer.isInitialized();
    }
}
