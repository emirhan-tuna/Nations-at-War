package io.github.some_example_name.UI;

import io.github.some_example_name.UI.TroopManagement.TroopManager;

public class PlayScreen extends AbstractScreen {
    private TroopManager troopManager;
    private Player p1;
    private Player p2;
    private InputHandler inputHandler;

    public PlayScreen() {
        super();
        this.troopManager = new TroopManager();
        this.p1 = new Player(1, 100, 1000);
        this.p2 = new Player(2, 100, 1000);
        this.inputHandler = new InputHandler(this);
    }

    public void update(float delta) {
        troopManager.updateAll(delta);
        // Burada oyuncu canlarının kontrolü gibi mantıklar eklenebilir
    }

    @Override
    public void render(float delta) {
        update(delta);
        // Çizim işlemleri buraya gelecek
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}