package UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import Game.Main;

public class MainMenuUi implements Screen{
    private Texture backTexture;
    private Stage stage;
    private Main game;
    private Table mainTable;
    private Table statsTable;
    private SpriteBatch batch;

    public MainMenuUi(Main game, Stage stage, Skin skin) {
        this.stage = stage;
        this.game = game;
        this.batch = new SpriteBatch();

        stage.clear();

        backTexture = new Texture(Gdx.files.internal("menu_items/background.jpg"));

        mainTable = new Table();
        mainTable.setFillParent(true);
        this.stage.addActor(mainTable);

        showMainMenu();
        showStats();
    }

    public void showMainMenu() {
        mainTable.clear();

        TextButton playGameButton = new TextButton("Play Game", game.skin);
        TextButton settingButton = new TextButton("Settings", game.skin);
        TextButton quitButton = new TextButton("Quit", game.skin);
        TextButton logOutButton = new TextButton("Log-out", game.skin);

        mainTable.add(playGameButton).width(150f).padTop(80f).padBottom(200f).row();
        mainTable.add(settingButton).width(150f).padBottom(200f).row();
        mainTable.add(quitButton).width(150f);
        mainTable.add(logOutButton).padLeft(200f).width(150f).right().bottom();

        playGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                playGame();
            }
        });

        settingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                showSettings();
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                quit();
            }
        });

        logOutButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new InitialUi(game));
            }
        });
    }

    public void showStats() {
        statsTable = new Table();
        statsTable.setFillParent(true);

        statsTable.top().right();

        Label userLabel = new Label("username: " + game.username, game.skin, "very_big_title");
        //userLabel.setFontScale(4f);
        Label gameLabel = new Label("Played games: " + game.games, game.skin, "very_big_title");
        //gameLabel.setFontScale(4f);
        Label winLabel = new Label("wins: " + game.wins, game.skin, "very_big_title");
        //winLabel.setFontScale(4f);

        statsTable.add(userLabel).padRight(20f).padTop(20f).right().row();
        statsTable.add(gameLabel).padRight(20f).padTop(5f).right().row();
        statsTable.add(winLabel).padRight(20f).padTop(5f).right();

        stage.addActor(statsTable);
    }

    public void playGame() {
        game.setScreen(new FindGameUI(game));
    }

    public void showSettings() {
        game.setScreen(new SettingsUi(game, stage, game.skin));
    }

    public void quit() {
        Gdx.app.exit();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.begin();
        batch.draw(backTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

    @Override
    public void show() {
        stage.setViewport(new FitViewport(1920, 1080));
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        Gdx.input.setInputProcessor(stage);

        mainTable.setFillParent(true);
        stage.addActor(mainTable);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
