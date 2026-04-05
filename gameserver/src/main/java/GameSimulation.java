import simulation.Simulation;

public class GameSimulation implements Runnable {
    private Simulation simulation;
    private boolean running = true;

    public GameSimulation() {
        this.simulation = new Simulation();
    }

    public long getChecksum(int tick) {
        return simulation.getChecksum(tick);
    }

    public int getX() {
        return simulation.getX();
    }

    public int getY() {
        return simulation.getY();
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
