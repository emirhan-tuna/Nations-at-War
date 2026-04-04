package io.github.some_example_name.UI;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.some_example_name.FirebaseTest;

public class MainMenuUi {
    private Stage stage;
    private Skin skin;
    private Table mainTable;
    private FirebaseTest test;

    public MainMenuUi(Stage aStage, Skin aSkin) {
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

    }

    public void showSettings() {

    }

    public void quit() {

    }
}
