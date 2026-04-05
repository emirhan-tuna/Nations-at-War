package UI;

import UI.TroopManagement.TroopManager;
import Game.Player;

public class PlayScreen extends AbstractScreen {
    private TroopManager troopManager;
    private Player p1;
    private Player p2;
    private InputHandler inputHandler;

    public PlayScreen() {
        super();
        this.troopManager = new TroopManager();
        this.p1 = new Player(p1.getId());
        this.p2 = new Player(p2.getId());
        this.inputHandler = new InputHandler(this);
    }

    public void update(float delta) {
        troopManager.updateAll(delta);

    }

    @Override
    public void render(float delta) {
        update(delta);
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}