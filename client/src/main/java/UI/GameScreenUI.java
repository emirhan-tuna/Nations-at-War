package UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import Game.Main;
import Game.Player;
import UI.TroopManagement.TroopManager;


public class GameScreenUI implements Screen{
    private SpriteBatch batch;
    private Table popupMenu;
    private String lastTroop = ""; 
    private Texture backTexture;
    private Image backImage;
    private Main game;
    private Stage stage;
    private Table mainTable;
    private UI.TroopManagement.TroopManager troopManage;
    private Game.Player player;

    public GameScreenUI(Main game) {
        this.player = new Player(2);
        this.game = game;
        this.stage = new Stage();
        this.troopManage = new TroopManager();
        batch = new SpriteBatch();

        backTexture = new Texture("menu_items/background.jpg");

        mainTable = new Table(); 
        popupMenu = new Table();
        popupMenu.setVisible(false);
    }

    public void popupMenu() {
        popupMenu.clear();
        popupMenu.setVisible(true);

        popupMenu.setFillParent(true);
        popupMenu.top().left().padTop(50f).padLeft(15f);

        TextButton one = new TextButton("Top Lane", game.skin);
        TextButton two =new TextButton("Mid Lane", game.skin);
        TextButton third = new TextButton("Bot Lane", game.skin);

        popupMenu.add(one).size(100f,100f).padRight(10f);
        popupMenu.add(two).size(100f,100f).padRight(10f);
        popupMenu.add(third).size(100f,100f).padRight(10f);

        one.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                if (player.getId() == 1) {
                    troopManage.spawn(lastTroop, player.getId(), 20f, 680f);
                    troopManage.createTroop(lastTroop, 20f, 600f, player.getId());
                } else {
                    troopManage.spawn(lastTroop, player.getId(), 1600f, 680f);
                    troopManage.createTroop(lastTroop, 1600f, 680f, player.getId());
                }
                popupMenu.setVisible(false);
            }
        });

        two.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                if (player.getId() == 1) {
                    troopManage.spawn(lastTroop, player.getId(), 1600f, 400f);
                    troopManage.createTroop(lastTroop, 1600f, 4000f, player.getId());
                } else {
                    troopManage.spawn(lastTroop, player.getId(), 1600f, 400f);
                    troopManage.createTroop(lastTroop, 1600f, 400f, player.getId());
                }
                popupMenu.setVisible(false);
            }
        });

        third.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                if (player.getId() == 1) {
                    troopManage.spawn(lastTroop, player.getId(), 1600f, 120f);
                    troopManage.createTroop(lastTroop, 1600f, 120f, player.getId());
                } else {
                    troopManage.spawn(lastTroop, player.getId(), 1600f, 120f);
                    troopManage.createTroop(lastTroop, 1600f, 120f, player.getId());
                }
               popupMenu.setVisible(false);
            }
        });
    }

    public void showHealthandResourc() {
        Table infoTable = new Table();

        infoTable.bottom().right();

        Label health = new Label(Integer.toString(player.getHealth()), game.skin);
        Label resources = new Label(Integer.toString(player.getResources()), game.skin);

        infoTable.add(health).padRight(20f).padBottom(20f).right().row();
        infoTable.add(resources).padRight(20f).right();

        mainTable.add(infoTable).expand().bottom().right();
    }

    public void showTroops() {
        Table troopTable = new Table();

        float buttonWidth = 150f;
        float buttonHeight = 150f;

        ImageButton archerButton = new ImageButton(game.skin, "archer");
        archerButton.getImageCell().expand().fill();
        ImageButton swordsmanButton = new ImageButton(game.skin, "swordsman");
        swordsmanButton.getImageCell().expand().fill();
        ImageButton mageButton = new ImageButton(game.skin, "mage");
        mageButton.getImageCell().expand().fill();
        ImageButton knighButton = new ImageButton(game.skin, "knight");
        knighButton.getImageCell().expand().fill();
        ImageButton dragonButton = new ImageButton(game.skin, "dragon");
        dragonButton.getImageCell().expand().fill();

        troopTable.add(dragonButton).size(buttonWidth, buttonHeight).padRight(10f);
        troopTable.add(archerButton).size(buttonWidth, buttonHeight).padRight(10f);
        troopTable.add(swordsmanButton).size(buttonWidth, buttonHeight).padRight(10f);
        troopTable.add(knighButton).size(buttonWidth, buttonHeight).padRight(10f);
        troopTable.add(mageButton).size(buttonWidth, buttonHeight).padRight(10f);
        troopTable.row().padTop(5f);

        troopTable.add(new Label("500", game.skin)).center().padRight(10f);
        troopTable.add(new Label("100", game.skin)).center().padRight(10f);
        troopTable.add(new Label("50", game.skin)).center().padRight(10f);
        troopTable.add(new Label("75", game.skin)).center().padRight(10f);
        troopTable.add(new Label("150", game.skin)).center().padRight(10f);

        archerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                lastTroop = "archer";
                popupMenu();
            }
        });

        swordsmanButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                lastTroop = "swordsman";
                popupMenu();
            }
        });

        knighButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                lastTroop = "knight";
                popupMenu();
            }
        });

        mageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                lastTroop = "mage";
                popupMenu();
            }
        });

        dragonButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                lastTroop = "dragon";
                popupMenu();
            }
        });

        mainTable.add(troopTable).expand().top().right();
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        troopManage.updateAll(delta);
        batch.begin();
        batch.draw(backTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        troopManage.drawAll(batch);
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
        Gdx.input.setInputProcessor(stage);

        mainTable.setFillParent(true);

        showTroops();
        showHealthandResourc();
        stage.addActor(mainTable);
        stage.addActor(popupMenu);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
