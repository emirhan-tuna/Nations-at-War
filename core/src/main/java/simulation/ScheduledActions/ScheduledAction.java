package simulation.ScheduledActions;

import io.netty.buffer.ByteBuf;
import simulation.Simulation;

public abstract class ScheduledAction {
    protected int id;
    protected int tick;

    public ScheduledAction(int id, int tick) {
        this.id = id;
        this.tick = tick;
    }

    public abstract void execute(Simulation sim);

    public abstract void write(ByteBuf buf);

    public int getTick() {return tick;}

    public void setTick(int tick) {this.tick = tick;}

    public int getId() {return id;}
}
