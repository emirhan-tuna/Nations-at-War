package io.github.some_example_name.UI;

import com.badlogic.gdx.Game;

public class MyGame extends Game {
    private NetworkManager networkManager;

    @Override
    public void create() {
        this.networkManager = new NetworkManager();
        setScreen(new PlayScreen());
    }
}