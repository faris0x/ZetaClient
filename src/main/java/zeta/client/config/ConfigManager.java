package zeta.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import zeta.client.gui.hud.HudManager;
import zeta.client.mod.Mod;
import zeta.client.mod.ModManager;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;

public class ConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final File file;

    public ConfigManager(File gameDir) {
        this.file = new File(gameDir, "zeta-client.json");
    }

    private JsonObject lastConfig;

    public void load(ModManager modManager) {
        if (!file.exists()) return;

        try (Reader reader = new FileReader(file)) {
            JsonObject obj = GSON.fromJson(reader, JsonObject.class);
            if (obj == null) return;
            lastConfig = obj;

            JsonArray arr = obj.getAsJsonArray("enabledMods");
            if (arr != null) {
                for (int i = 0; i < arr.size(); i++) {
                    JsonElement el = arr.get(i);
                    Mod mod = modManager.get(el.getAsString());
                    if (mod != null) mod.enable();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadHud(HudManager hud) {
        if (lastConfig != null && lastConfig.has("hudPositions")) {
            hud.setPositions(lastConfig.getAsJsonObject("hudPositions"));
        }
    }

    public void save(ModManager modManager, HudManager hud) {
        try {
            file.getParentFile().mkdirs();
            JsonObject obj = new JsonObject();
            JsonArray arr = new JsonArray();

            for (Mod mod : modManager.getMods()) {
                if (mod.isEnabled()) {
                    arr.add(new JsonPrimitive(mod.getName()));
                }
            }

            obj.add("enabledMods", arr);
            obj.add("hudPositions", hud.getPositions());

            lastConfig = obj;

            try (Writer writer = new FileWriter(file)) {
                GSON.toJson(obj, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
