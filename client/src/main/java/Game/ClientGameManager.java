package Game;

import java.util.List;

import simulation.Simulation;
import simulation.GameObjects.GameObject;

public class ClientGameManager {
    private Simulation simulation;
    private boolean isStarted = false;
    
    private static final float TIME_STEP = 1f / 20f; 
    private float accumulator = 0f;

    public ClientGameManager() {
        this.simulation = new Simulation(false); 
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public void start() {
        this.simulation = new Simulation(false);
        isStarted = true;
    }

    public void stop() {
        isStarted = false;
    }

    public boolean getStarted() {
        return isStarted;
    }

    public List<GameObject> getObjects() {
        return simulation.getObjects();
    }

    public void update(float delta) {
        if (!isStarted || simulation == null) {
            return; 
        }
        
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

    public void addSimulation(Simulation sim) {
        this.simulation = sim;
    }
}