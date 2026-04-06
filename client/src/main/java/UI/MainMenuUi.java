package UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import Game.Main;
import Network.FirebaseTest;

public class MainMenuUi implements Screen {
    private Texture backTexture;
    private SpriteBatch batch;
    private Stage stage;
    private Main game;
    private Table mainTable;
    private FirebaseTest test;
    private Table statsTable;
    private Texture statsBgTexture;

    public MainMenuUi(Main game, Stage stage, Skin skin) {
        this.stage = stage;
        this.test = new FirebaseTest();
        this.game = game;
        this.batch = new SpriteBatch();

        stage.clear();

        backTexture = new Texture(Gdx.files.internal("menu_items/background.jpg"));
        backImage = new Image(backTexture);
        backImage.getColor().a = 0.8f;
        backImage.setFillParent(true);
        stage.addActor(backImage);

        mainTable = new Table();
        mainTable.setFillParent(true);
        this.stage.addActor(mainTable);

        showMainMenu();
        showStats();
    }

    public void showMainMenu() {
        mainTable.clear();
        mainTable.center();

        TextButton playGameButton = new TextButton("Play Game", game.skin);
        TextButton settingButton = new TextButton("Settings", game.skin);
        TextButton quitButton = new TextButton("Quit", game.skin);

        mainTable.add(playGameButton).width(150f).padTop(80f).padBottom(200f).row();
        mainTable.add(settingButton).width(150f).padBottom(200f).row();
        mainTable.add(quitButton).width(150f);
        mainTable.add(logOutButton).padLeft(200f).width(150f).right().bottom();

        playGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new InviteUi(game, stage, game.skin));
            }
        });

        settingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new SettingsUi(game, stage, game.skin));
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new InitialUi(game));
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                Gdx.app.exit();
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

        innerStatsBox.add(userLabel).right().row();
        innerStatsBox.add(gameLabel).padTop(10f).right().row();
        innerStatsBox.add(winLabel).padTop(10f).right();

        statsTable.add(innerStatsBox).padTop(30f).padRight(30f);
        stage.addActor(statsTable);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render background to absolute window size
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
        backTexture.dispose();
        batch.dispose();
        if (statsBgTexture != null) statsBgTexture.dispose();
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
