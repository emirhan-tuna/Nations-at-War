package simulation.ScheduledActions;

import io.netty.buffer.ByteBuf;
import simulation.Simulation;

public class SpawnAction extends ScheduledAction {
    private int type;
    private int team;

    public SpawnAction(int type, int team, int tick) {
        super(0, tick);
        this.type = type;
        this.team = team;
    }

    public SpawnAction(ByteBuf buf) {
        super(0, 0);
        this.type = buf.readByte();
        this.team = buf.readByte();
    }

    public void execute(Simulation sim) {
        sim.spawnObject(this.type, team);
    }

    public void write(ByteBuf buf) {
        buf.writeByte(this.team);
    }


}
