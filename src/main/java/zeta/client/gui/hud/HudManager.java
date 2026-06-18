package zeta.client.gui.hud;

import com.google.gson.JsonObject;
import org.lwjgl.input.Mouse;
import net.minecraft.client.Minecraft;
import zeta.client.config.ConfigManager;
import zeta.client.mod.ModManager;
import zeta.client.nanovg.NanoVGManager;

import java.util.ArrayList;
import java.util.List;

public class HudManager {

    private final List<HudElement> elements = new ArrayList<>();
    private HudElement draggingElement;
    private boolean editMode;
    private float dragOffsetX, dragOffsetY;
    private ConfigManager configManager;
    private ModManager modManager;

    public HudManager() {
        elements.add(new FpsDisplay());
        elements.add(new CoordsDisplay());
    }

    public List<HudElement> getElements() { return elements; }
    public boolean isEditMode() { return editMode; }
    public void setEditMode(boolean editMode) { this.editMode = editMode; }
    public void setConfigManager(ConfigManager configManager) { this.configManager = configManager; }
    public void setModManager(ModManager modManager) { this.modManager = modManager; }

    public void render(NanoVGManager nvg) {
        if (nvg.getCtx() == 0) return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.thePlayer == null) return;

        handleDrag(mc);

        int mx = Mouse.getX();
        int my = mc.displayHeight - Mouse.getY();

        nvg.setupAndDraw(() -> {
            for (HudElement el : elements) {
                el.drawNvg(nvg, editMode, mx, my);
            }
        });
    }

    private void handleDrag(Minecraft mc) {
        int mx = Mouse.getX();
        int my = mc.displayHeight - Mouse.getY();

        if (!editMode) return;

        if (Mouse.isButtonDown(0)) {
            if (draggingElement != null) {
                draggingElement.setPosition(mx - dragOffsetX, my - dragOffsetY);
                return;
            }
            for (HudElement el : elements) {
                if (el.contains(mx, my)) {
                    draggingElement = el;
                    dragOffsetX = mx - el.getX();
                    dragOffsetY = my - el.getY();
                    el.startDrag();
                    return;
                }
            }
        } else {
            if (draggingElement != null) {
                draggingElement.stopDrag();
                if (configManager != null && modManager != null) {
                    configManager.save(modManager, this);
                }
                draggingElement = null;
            }
        }
    }

    public JsonObject getPositions() {
        JsonObject obj = new JsonObject();
        for (HudElement el : elements) {
            JsonObject pos = new JsonObject();
            pos.addProperty("x", el.getX());
            pos.addProperty("y", el.getY());
            obj.add(el.getName(), pos);
        }
        return obj;
    }

    public void setPositions(JsonObject obj) {
        if (obj == null) return;
        for (HudElement el : elements) {
            if (obj.has(el.getName())) {
                JsonObject pos = obj.getAsJsonObject(el.getName());
                el.setPosition(pos.get("x").getAsFloat(), pos.get("y").getAsFloat());
            }
        }
    }
}
