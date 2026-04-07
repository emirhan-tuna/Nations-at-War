import simulation.Simulation;;

public class GameSimulation implements Runnable {
    private Simulation simulation;
    private boolean running = true;
    public StartServer server;

    public GameSimulation(StartServer server) {
        this.simulation = new Simulation(true);
        this.server = server;
    }

    public Simulation getSimulation() {
        return simulation;
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

                int winnerId = simulation.getWinner();
                if (winnerId != -1) {
                    running = false;
                    server.endGame(winnerId);
                }
                
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
