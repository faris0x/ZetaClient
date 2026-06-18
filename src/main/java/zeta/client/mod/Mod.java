package zeta.client.mod;

public abstract class Mod {

    public enum Category {
        PLAYER("Player"),
        VISUAL("Visual"),
        UTILITY("Utility"),
        MISC("Misc");

        public final String displayName;
        Category(String displayName) { this.displayName = displayName; }
    }

    private final String name;
    private final String description;
    private final int keyCode;
    private final Category category;
    private boolean enabled;

    public Mod(String name, String description, int keyCode, Category category) {
        this.name = name;
        this.description = description;
        this.keyCode = keyCode;
        this.category = category;
    }

    public Mod(String name, String description, Category category) {
        this(name, description, 0, category);
    }

    public Mod(String name, String description) {
        this(name, description, 0, Category.MISC);
    }

    public void onEnable() {}
    public void onDisable() {}

    public void enable() {
        if (!enabled) {
            enabled = true;
            onEnable();
        }
    }

    public void disable() {
        if (enabled) {
            enabled = false;
            onDisable();
        }
    }

    public void toggle() {
        if (enabled) disable(); else enable();
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getKeyCode() { return keyCode; }
    public Category getCategory() { return category; }
    public boolean isEnabled() { return enabled; }
}
