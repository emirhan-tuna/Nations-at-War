package UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import Game.Main;

public class GameOverUI implements Screen{
    private SpriteBatch batch;
    private Table popupMenu;
    private String lastTroop = ""; 
    private Texture backTexture;
    private Image backImage;
    private Main game;
    private Stage stage;
    private Table mainTable;

    public GameOverUI(Main game) {
        this.game = game;
        this.stage = new Stage();

        mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);
    }

    public void createUI() {
        Label overLabel = new Label("Game Over.", game.skin, "title");

        TextButton button = new TextButton("Back to main menu: ", game.skin);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new MainMenuUi(game, stage, game.skin));
            }
        });

        mainTable.add(overLabel).padBottom(50f).row();
        mainTable.add(button).padBottom(50f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        mainTable.setFillParent(true);

        createUI();
        stage.addActor(mainTable);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}