
import network.ActionPacket;
import simulation.Simulation;
import simulation.ScheduledActions.ScheduledAction;
import simulation.ScheduledActions.SpawnAction;
import simulation.Simulation.Snapshot;

public class GameSimulation implements Runnable {
    private StartServer server;
    private Simulation simulation;
    private boolean running = true;

    public GameSimulation(StartServer server) {
        this.simulation = new Simulation(true);
        this.server = server;
    }

    public ScheduledAction scheduleAction(ScheduledAction action) {
        return simulation.scheduleAction(action);
    }

    public ScheduledAction scheduleSpawn(int type, int team) {
        SpawnAction spawn = new SpawnAction(type, team, 0);
        ScheduledAction actionToSend = scheduleAction(spawn);

        return actionToSend;
    }

    public long getChecksum(int tick) {
        return simulation.getChecksum(tick);
    }

    public Snapshot getSnapshot() {
        return simulation.getSnapshot();
    }

    public int getTick() {
        return simulation.getTick();
    }

    @Override
    public void run() {
        long time = System.nanoTime();
        long NANOS_PER_TICK = 1000000000 / 20; //20tps

        while(running) {
            long now = System.nanoTime();

            if(now >= time) {
                simulation.update();
                time += NANOS_PER_TICK;
            } else {
                long sleepTime = (time - now) / 1000000;
                if(sleepTime > 0) {
                    try {
                        Thread.sleep(sleepTime);
                    } catch(InterruptedException err) {}
                }
            }
        }
    }
}
