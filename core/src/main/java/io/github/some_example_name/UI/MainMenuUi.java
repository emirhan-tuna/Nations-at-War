package io.github.some_example_name.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.some_example_name.FirebaseTest;
import io.github.some_example_name.Main;

public class MainMenuUi implements Screen{
    private Stage stage;
    private Main game;
    private Table mainTable;
    private FirebaseTest test;
    private Table statsTable;

    public MainMenuUi(Main game, Stage stage, Skin skin) {
        this.stage = new Stage();
        this.test = new FirebaseTest();
        this.game = game;

        mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        showMainMenu();
        showStats();
    }

    public void showMainMenu() {
        mainTable.clear();

        TextButton playGameButton = new TextButton("Play Game", game.skin);
        TextButton settingButton = new TextButton("Settings", game.skin);
        TextButton quitButton = new TextButton("Quit", game.skin);

        mainTable.add(playGameButton).width(150f).padBottom(50f).row();
        mainTable.add(settingButton).width(150f).padBottom(50f).row();
        mainTable.add(quitButton).width(150f);

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
    }

    public void showStats() {
        statsTable = new Table();
        statsTable.setFillParent(true);

        statsTable.top().right();

        Label userLabel = new Label("username: " + game.username, game.skin);
        Label gameLabel = new Label("Played games: " + game.games, game.skin);
        Label winLabel = new Label("wins: " + game.wins, game.skin);

        statsTable.add(userLabel).padRight(20f).padTop(20f).right().row();
        statsTable.add(gameLabel).padRight(20f).padTop(5f).right().row();
        statsTable.add(winLabel).padRight(20f).padTop(5f).right();

        stage.addActor(statsTable);
    }

    public void playGame() {

    }

    public void showSettings() {
        game.setScreen(new SettingsUi(game, stage, game.skin));
    }

    public void quit() {

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
        stage.dispose();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        mainTable.setFillParent(true);
        stage.addActor(mainTable);
    }

    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
}
