package io.github.some_example_name.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.some_example_name.FirebaseTest;
import io.github.some_example_name.Main;
import io.github.some_example_name.Player;
import io.github.some_example_name.UI.TroopManagement.TroopManager;

public class GameScreenUI implements Screen{
    private Table popupMenu;
    private Texture backTexture;
    private Image backImage;
    private Main game;
    private Stage stage;
    private Table mainTable;
    private FirebaseTest test;
    private TroopManager troopManage;
    private Player player;

    public GameScreenUI(Main game) {
        this.player = new Player();
        this.game = game;
        this.stage = new Stage();
        this.test = new FirebaseTest();
        this.troopManage = new TroopManager();

        backTexture = new Texture(Gdx.files.internal("menu_items/background.jpg"));
        backImage = new Image(backTexture);

        backImage.setFillParent(true);
        stage.addActor(backImage);

        mainTable = new Table(); 
        popupMenu = new Table();
    }

    public void askForVert(TakeVert vert) {
        popupMenu.clear();

        TextButton one = new TextButton("1", game.skin);
        TextButton two =new TextButton("2", game.skin);
        TextButton third = new TextButton("3", game.skin);

        popupMenu.addActor(one);
        popupMenu.addActor(two);
        popupMenu.addActor(third);

        one.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                vert.takeVertical(1);
            }
        });

        two.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                vert.takeVertical(2);
            }
        });

        third.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                vert.takeVertical(3);
            }
        });
    }

    public void showTroops() {
        ImageButton archerButton = new ImageButton(game.skin, "archer");
        ImageButton swordsmanButton = new ImageButton(game.skin, "swordsman");
        ImageButton mageButton = new ImageButton(game.skin, "mage");
        ImageButton knighButton = new ImageButton(game.skin, "knight");
        ImageButton dragonButton = new ImageButton(game.skin, "dragon");

        archerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                troopManage.spawn("archer", game.userID, x, y);
            }
        });

        swordsmanButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                troopManage.spawn("swordsman", game.userID, x, y);
            }
        });

        knighButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                troopManage.spawn("knight", game.userID, x, y);
            }
        });

        mageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                troopManage.spawn("mage", game.userID, x, y);
            }
        });

        dragonButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                troopManage.spawn("dragon", game.userID, x, y);
            }
        });
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
