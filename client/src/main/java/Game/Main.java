package Game;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import UI.InitialUi;

public class Main extends Game {
    public Skin skin;
    public Player player;
    public String username;
    public int games;
    public int wins;
    public String userID;

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("skins/uiskinfontfix.json"));

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
