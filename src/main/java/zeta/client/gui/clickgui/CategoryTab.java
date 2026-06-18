package zeta.client.gui.clickgui;

import zeta.client.mod.Mod;
import zeta.client.nanovg.NanoVGManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CategoryTab {

    private final Map<Mod.Category, List<ModButton>> categories = new LinkedHashMap<>();
    private Mod.Category selected = Mod.Category.VISUAL;

    private float x, y;
    private float width;
    private static final float TAB_HEIGHT = 26;
    private static final int TAB_BG = 0xCC1A1A1A;
    private static final int TAB_ACTIVE = 0xCC2A2A2A;
    private static final int TAB_HOVER = 0xCC333333;

    public void setBounds(float x, float y, float width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    public void setMods(List<ModButton> allButtons) {
        categories.clear();
        for (ModButton btn : allButtons) {
            categories.computeIfAbsent(btn.getMod().getCategory(), k -> new ArrayList<>()).add(btn);
        }
        if (!categories.containsKey(selected) && !categories.isEmpty()) {
            selected = categories.keySet().iterator().next();
        }
    }

    public Mod.Category getSelected() { return selected; }
    public List<ModButton> getSelectedButtons() {
        return categories.getOrDefault(selected, new ArrayList<>());
    }

    public float getHeight() { return TAB_HEIGHT; }

    public void renderNvg(NanoVGManager nvg, int mouseX, int mouseY) {
        nvg.drawRoundedRect(x, y, width, TAB_HEIGHT, 4, TAB_BG);

        Mod.Category[] cats = categories.keySet().toArray(new Mod.Category[0]);
        float tabW = width / Math.max(1, cats.length);

        for (int i = 0; i < cats.length; i++) {
            float tx = x + i * tabW;
            boolean hovered = mouseX >= tx && mouseX <= tx + tabW && mouseY >= y && mouseY <= y + TAB_HEIGHT;
            boolean active = cats[i] == selected;

            int color = active ? TAB_ACTIVE : (hovered ? TAB_HOVER : TAB_BG);
            nvg.drawRoundedRect(tx, y, tabW, TAB_HEIGHT, active ? 4 : 0, color);

            String label = cats[i].displayName;
            int textColor = active ? 0xFFFF5555 : (hovered ? 0xFFCCCCCC : 0xFF777777);
            nvg.drawCenteredText(label, tx + tabW / 2, y + TAB_HEIGHT / 2, textColor, 12, "dm-sans");
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int button) {
        if (button != 0) return;
        if (mouseY < y || mouseY > y + TAB_HEIGHT) return;

        Mod.Category[] cats = categories.keySet().toArray(new Mod.Category[0]);
        float tabW = width / Math.max(1, cats.length);
        int idx = (int) ((mouseX - x) / tabW);
        if (idx >= 0 && idx < cats.length) {
            selected = cats[idx];
        }
    }
}
