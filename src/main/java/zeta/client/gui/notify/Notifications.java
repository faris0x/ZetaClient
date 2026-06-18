package zeta.client.gui.notify;

import zeta.client.nanovg.NanoVGManager;

import java.util.concurrent.CopyOnWriteArrayList;

public class Notifications {

    private static final CopyOnWriteArrayList<Notification> list = new CopyOnWriteArrayList<>();

    public static void show(String text) {
        list.add(new Notification(text));
    }

    public static void render(NanoVGManager nvg, float screenWidth) {
        long now = System.currentTimeMillis();
        float y = 8;

        for (int i = list.size() - 1; i >= 0; i--) {
            Notification n = list.get(i);
            if (n.isExpired(now)) {
                list.remove(i);
                continue;
            }
            float alpha = n.getAlpha(now);
            float w = Math.max(120, nvg.getTextWidth(n.text, 12, "dm-sans") + 20);
            float x = screenWidth - w - 8;
            nvg.nvgSave();
            nvg.nvgGlobalAlpha(alpha);
            nvg.drawRoundedRect(x, y, w, 20, 4, 0xDD1A1A1A);
            nvg.drawText(n.text, x + 10, y + 13, 0xFFFF5555, 12, "dm-sans");
            nvg.nvgGlobalAlpha(1f);
            nvg.nvgRestore();
            y += 24;
        }
    }

    private static class Notification {
        final String text;
        final long createdAt;
        static final long DURATION = 3000;

        Notification(String text) {
            this.text = text;
            this.createdAt = System.currentTimeMillis();
        }

        boolean isExpired(long now) { return now - createdAt > DURATION; }

        float getAlpha(long now) {
            long age = now - createdAt;
            if (age < 300) return age / 300f;
            if (age > DURATION - 400) return Math.max(0, (DURATION - age) / 400f);
            return 1f;
        }
    }
}
