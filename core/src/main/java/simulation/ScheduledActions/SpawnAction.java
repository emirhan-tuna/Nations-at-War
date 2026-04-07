package simulation.ScheduledActions;

import io.netty.buffer.ByteBuf;
import simulation.Simulation;

public class SpawnAction extends ScheduledAction {
    private int type;
    private int team;
    private int lane;

    public SpawnAction(int type, int team, int tick, int lane) {
        super(0, tick);
        this.type = type;
        this.team = team;
        this.lane = lane;
    }

    public SpawnAction(ByteBuf buf) {
        super(0, 0);
        this.type = buf.readByte();
        this.team = buf.readByte();
    }

    public void write(ByteBuf buf) {
        buf.writeByte(this.team);
    }

    public void execute(Simulation sim) {
        sim.spawnObject(this.type, team, this.lane);
    }
    
}
