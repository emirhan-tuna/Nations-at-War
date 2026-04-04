package io.github.some_example_name.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.some_example_name.FirebaseTest;
import io.github.some_example_name.Main;

public class MainMenuUi implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private Table mainTable;
    private FirebaseTest test;

    // FIXED: Now accepts 'Main game'
    public MainMenuUi(Main game, Stage aStage, Skin aSkin) {
        this.game = game;
        this.stage = aStage;
        this.skin = aSkin;
        this.test = new FirebaseTest();

        mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        showMainMenu();
    }

    public void showMainMenu() {
        mainTable.clear();

        TextButton playGameButton = new TextButton("Play Game", skin);
        TextButton settingButton = new TextButton("Settings", skin);
        TextButton quitButton = new TextButton("Quit", skin);

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

    public void playGame() {
        System.out.println("LOG: Transitioning to gameplay...");
    }

    public void showSettings() {
        // FIXED: Switches to your new Settings screen
        game.setScreen(new SettingsUi(game, stage, skin));
    }

    public void quit() {
        Gdx.app.exit();
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

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
