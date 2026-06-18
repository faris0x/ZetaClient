package zeta.client.mod;

import java.util.ArrayList;
import java.util.List;

public class ModManager {

    private final List<Mod> mods = new ArrayList<>();

    public void add(Mod mod) {
        mods.add(mod);
    }

    public void remove(Mod mod) {
        mods.remove(mod);
    }

    public Mod get(String name) {
        return mods.stream()
            .filter(m -> m.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    public List<Mod> getMods() {
        return new ArrayList<>(mods);
    }

    public void enableAll() {
        mods.forEach(Mod::enable);
    }

    public void disableAll() {
        mods.forEach(Mod::disable);
    }
}
