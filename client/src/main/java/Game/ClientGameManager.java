package Game;

import simulation.Simulation;

public class ClientGameManager {
    private Simulation simulation;
    
    private static final float TIME_STEP = 1f / 20f; 
    private float accumulator = 0f;

    public ClientGameManager() {
        this.simulation = new Simulation(false); 
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public void update(float delta) {
        float frameTime = Math.min(delta, 0.25f); 
        
        accumulator += frameTime;

        while (accumulator >= TIME_STEP) {
            simulation.update();
            accumulator -= TIME_STEP;
        }
    }

    public float getInterpolationAlpha() {
        return accumulator / TIME_STEP;
    }
}