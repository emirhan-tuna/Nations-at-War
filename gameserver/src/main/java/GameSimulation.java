import simulation.Simulation;;

public class GameSimulation implements Runnable {
    private Simulation simulation;
    private boolean running = true;
    public GameRoom room;

    public GameSimulation(GameRoom room) {
        this.simulation = new Simulation(true);
        this.room = room;
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
                    room.endGame(winnerId);
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

    public void stop() {
        this.running = false;
    }
}
