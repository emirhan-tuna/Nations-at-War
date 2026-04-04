package io.github.some_example_name; // Make sure this matches your package

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.some_example_name.UI.InitialUi;

public class Main extends Game {
    public Skin skin;

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));

        this.setScreen(new InitialUi(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        if (skin != null) skin.dispose();
        if (screen != null) screen.dispose();
    }
}
