package zeta.client.input;

import org.lwjgl.input.Keyboard;
import zeta.client.event.EventBus;
import zeta.client.event.TickEvent;
import zeta.client.gui.GuiHandler;

public class KeybindManager {

    private static final int GUI_KEY = Keyboard.KEY_RSHIFT;
    private boolean wasDown;

    @EventBus.Subscribe
    public void onTick(TickEvent event) {
        boolean current = Keyboard.isKeyDown(GUI_KEY);
        if (current && !wasDown) {
            GuiHandler.toggleGui();
        }
        wasDown = current;
    }
}
