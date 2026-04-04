package io.github.some_example_name.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import io.github.some_example_name.Main;

public class SettingsUi implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private Table mainTable;

    // Default volume
    private float currentVolume = 1.0f;

    public SettingsUi(Main game, Stage stage, Skin skin) {
        this.game = game;
        this.stage = stage;
        this.skin = skin;

        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.center();
    }

    private void buildUi() {
        mainTable.clear();

        // 0. Title
        Label titleLabel = new Label("Settings menu", skin);
        titleLabel.setFontScale(1.5f);
        mainTable.add(titleLabel).colspan(2).padBottom(50f).row();

        // 1. Volume Bar
        Label volumeLabel = new Label("Volume bar", skin);
        volumeLabel.setAlignment(Align.left);

        final Slider volumeSlider = new Slider(0f, 1f, 0.05f, false, skin);
        volumeSlider.setValue(currentVolume);

        mainTable.add(volumeLabel).width(150f).padBottom(30f);
        mainTable.add(volumeSlider).width(300f).padBottom(30f).row();

        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentVolume = volumeSlider.getValue();
                System.out.println("LOG: Volume set to " + currentVolume);
            }
        });

        // 2. FPS Limiter
        Label fpsLabel = new Label("FPS Limiter", skin);
        fpsLabel.setAlignment(Align.left);

        final SelectBox<String> fpsBox = new SelectBox<>(skin);
        fpsBox.setItems("240", "144", "120", "60", "unlimited");
        fpsBox.setSelected("60"); // Default

        mainTable.add(fpsLabel).width(150f).padBottom(30f);
        mainTable.add(fpsBox).width(300f).padBottom(30f).row();

        fpsBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selection = fpsBox.getSelected();
                switch (selection) {
                    case "240": Gdx.graphics.setForegroundFPS(240); break;
                    case "144": Gdx.graphics.setForegroundFPS(144); break;
                    case "120": Gdx.graphics.setForegroundFPS(120); break;
                    case "60": Gdx.graphics.setForegroundFPS(60); break;
                    case "unlimited": Gdx.graphics.setForegroundFPS(0); break;
                }
                System.out.println("LOG: FPS Locked to " + selection);
            }
        });

        // 3. Window Mode
        Label windowLabel = new Label("Window Mode", skin);
        windowLabel.setAlignment(Align.left);

        final SelectBox<String> windowBox = new SelectBox<>(skin);
        windowBox.setItems("windowed", "windowed borderless", "fullscreen");
        windowBox.setSelected("windowed"); // Default

        mainTable.add(windowLabel).width(150f).padBottom(50f);
        mainTable.add(windowBox).width(300f).padBottom(50f).row();

        windowBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selection = windowBox.getSelected();
                switch (selection) {
                    case "windowed":
                        Gdx.graphics.setWindowedMode(1280, 720);
                        break;
                    case "windowed borderless":
                        Gdx.graphics.setUndecorated(true);
                        Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
                        break;
                    case "fullscreen":
                        Gdx.graphics.setUndecorated(false);
                        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                        break;
                }
                System.out.println("LOG: Window set to " + selection);
            }
        });

        // 4. Apply and Exit Buttons Layout
        Table buttonTable = new Table();
        TextButton applyButton = new TextButton("Apply", skin);
        TextButton exitButton = new TextButton("Exit", skin);

        // Put them side by side with a 20-pixel gap between them
        buttonTable.add(applyButton).width(140f).padRight(20f);
        buttonTable.add(exitButton).width(140f);

        // Add the button layout to the main table
        mainTable.add(buttonTable).colspan(2).padTop(20f);

        // Listeners for the new buttons
        applyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("LOG: Settings Applied.");
                game.setScreen(new MainMenuUi(game, stage, skin));
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("LOG: Exited Settings without applying new ones.");
                game.setScreen(new MainMenuUi(game, stage, skin));
            }
        });

        stage.addActor(mainTable);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        buildUi(); // Build everything fresh when the screen opens
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

    @Override public void dispose() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
