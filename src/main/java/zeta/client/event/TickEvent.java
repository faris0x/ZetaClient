package zeta.client.event;

public class TickEvent extends Event {
    public final int tick;
    public TickEvent(int tick) {
        this.tick = tick;
    }
}
