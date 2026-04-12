package simulation.ScheduledActions;

import io.netty.buffer.ByteBuf;
import simulation.SimPlayer;
import simulation.Simulation;
import simulation.GameObjects.Troops.Troop;

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
        this.lane = buf.readByte();
    }

    public void execute(Simulation sim) {
        SimPlayer player = sim.getSimPlayer(this.team);
        int cost = Troop.getRequiredGold(this.type);

        if (player.getGold() >= cost) {
            
            player.setGold(player.getGold() - cost); 
            sim.spawnObject(this.type, this.team, this.lane);
            
            System.out.println("spawned " + type + " for team " + team + " at tick " + getTick());

        } else {
            System.out.println("spawn failed at tick " + getTick() + " (not enough gold).");
        }
    }

    public void write(ByteBuf buf) {
        buf.writeByte(this.type);
        buf.writeByte(this.team);
        buf.writeByte(this.lane);
    }
    
}
