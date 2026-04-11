package UI;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import Game.ClientGameManager;
import Game.Main;
import Game.Player;
import Network.NetworkManager;
import simulation.GameObjects.GameObject;
import simulation.GameObjects.Troops.Troop;

public class GameScreenUI implements Screen {
    private Label healthLabel;
    private Label resourceLabel;
    private SpriteBatch batch;
    private Table popupMenu;
    private String lastTroop = "";

    private Texture skyTexture;
    private Texture groundTexture;
    private Texture blankTexture; //DONT REMOVE!!!!!!!!!!!!!!!!!! used for objs with no asset (custom drawing like healthbar)

    private Main game;
    private Stage stage;
    private Table mainTable;
    private Game.Player player;
    private NetworkManager manager;
    private ClientGameManager clientManager;
    private Texture archer, knight, dragon, towerPlayer, towerEnemy, mage;

    public GameScreenUI(Main game, NetworkManager manager) {
        archer = new Texture(Gdx.files.internal("Sprites/archer_16x16.png"));
        archer.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);

        knight = new Texture(Gdx.files.internal("Sprites/knight_16x16.png"));
        knight.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        dragon = new Texture(Gdx.files.internal("Sprites/dragon_16x16.png"));
        dragon.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        mage = new Texture(Gdx.files.internal("Sprites/mage_16x16.png"));
        mage.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        towerPlayer = new Texture(Gdx.files.internal("Sprites/tower_player_16x16.png"));
        towerPlayer.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        towerEnemy = new Texture(Gdx.files.internal("Sprites/tower_enemy_16x16.png"));
        towerEnemy.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        blankTexture = new Texture(pixmap);
        pixmap.dispose();

        this.player = new Player(1);
        this.game = game;
        this.stage = new Stage();
        batch = new SpriteBatch();
        this.manager = manager;
        clientManager = new ClientGameManager(game);

        this.stage = new Stage(new FitViewport(1920, 1080));
        this.batch = new SpriteBatch();

        skyTexture = new Texture(Gdx.files.internal("Sprites/sky_background_144x81.png"));
        skyTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        groundTexture = new Texture(Gdx.files.internal("Sprites/ground_lanes.png"));

        mainTable = new Table();
        popupMenu = new Table();
        popupMenu.setVisible(false);

    }

    public ClientGameManager getClientManager() {return clientManager;}

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
                manager.sendSpawn(troopID(lastTroop), 1);
                popupMenu.setVisible(false);
            }
        });

        two.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                manager.sendSpawn(troopID(lastTroop), 2);
                popupMenu.setVisible(false);
            }
        });

        third.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                manager.sendSpawn(troopID(lastTroop), 3);
                popupMenu.setVisible(false);
            }
        });
    }

    public void showHealthandResourc() {
        Table infoTable = new Table();

        if(player.getId() == 1) {
            infoTable.left();
        } else {
            infoTable.right();
        }

        healthLabel = new Label("Base Health: " + Integer.toString(player.getHealth()), game.skin, "very_big_title");
        resourceLabel = new Label("Gold: " + Integer.toString(player.getResources()), game.skin, "very_big_title");

        infoTable.add(healthLabel).padRight(20f).padBottom(20f).right().row();
        infoTable.add(resourceLabel).padRight(20f).right();

        mainTable.add(infoTable).expand().bottom().right().pad(30f);
    }

    public void showTroops() {
        Table troopTable = new Table();

        float buttonWidth = 150f;
        float buttonHeight = 150f;

        ImageButton archerButton = new ImageButton(game.skin, "archer");
        archerButton.getImageCell().expand().fill();
        ImageButton mageButton = new ImageButton(game.skin, "mage");
        mageButton.getImageCell().expand().fill();
        ImageButton knighButton = new ImageButton(game.skin, "knight");
        knighButton.getImageCell().expand().fill();
        ImageButton dragonButton = new ImageButton(game.skin, "dragon");
        dragonButton.getImageCell().expand().fill();

        troopTable.add(dragonButton).size(buttonWidth, buttonHeight).padRight(10f);
        troopTable.add(archerButton).size(buttonWidth, buttonHeight).padRight(10f);
        troopTable.add(knighButton).size(buttonWidth, buttonHeight).padRight(10f);
        troopTable.add(mageButton).size(buttonWidth, buttonHeight);
        troopTable.row().padTop(5f);

        troopTable.add(new Label("500", game.skin)).center().padRight(10f);
        troopTable.add(new Label("100", game.skin)).center().padRight(10f);
        troopTable.add(new Label("75", game.skin)).center().padRight(10f);
        troopTable.add(new Label("150", game.skin)).center();

        archerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                lastTroop = "archer";
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

        mainTable.add(troopTable).expand().top().right().row();
    }

    public Main getScreen() {
        return game;
    }

    public int troopID(String troop) {
        if (troop.equals("archer")) {
            return Troop.ARCHER;
        } else if (troop.equals("dragon")) {
            return Troop.DRAGON;
        } else if (troop.equals("knight")) {
            return Troop.KNIGHT;
        } else if (troop.equals("mage")) {
            return Troop.MAGE;
        }

        return -1;
    }

    public void draw(GameObject object, float alpha) {
        float width = 128f;
        float height = 128f;

        if (object.getTeam() == 0) {
            float objX = MathUtils.lerp(object.getLastX(), object.getX(), alpha);
            float objY = MathUtils.lerp(object.getLastY(), object.getY(), alpha);

            if(object instanceof Troop) {
                //draw healthbar
                Troop troop = (Troop) object;

                float healthPercent = MathUtils.lerp((float) troop.getLastHealth(), (float) troop.getHealth(), alpha) / troop.getMaxHealth();;

                float barWidth = width * 0.8f; 
                float barHeight = height * 0.2f;
                float barX = objX + (width - barWidth) / 2f;
                float barY = objY + height + barHeight;

                float originalColor = batch.getPackedColor();

                batch.setColor(0f, 0f, 0f, 0.3f);
                batch.draw(blankTexture, barX, barY, barWidth, barHeight);

                if(troop.getHealth() < troop.getLastHealth()) {
                    batch.setColor(0.5f, 1f - alpha, 0f, 1f);
                } else {
                    batch.setColor(Color.GREEN);
                }

                batch.draw(blankTexture, barX, barY, barWidth * healthPercent, barHeight);

                batch.setPackedColor(originalColor);
            }
            if (object.getType() == 0) {   
                batch.draw(archer, objX, objY, width, height);
            } else if (object.getType() == 1) {
                batch.draw(dragon, objX, objY, width, height);
            } else if (object.getType() == 2) {
                batch.draw(knight, objX, objY, width, height);
            } else if (object.getType() == 3) {
                batch.draw(mage, objX, objY, width, height);
            } else if (object.getType() == 4) {
                batch.draw(towerPlayer, objX, objY, 512f, 512f);
            } 
        } else {
            if (object.getType() == 0) {   
                batch.draw(archer, object.getX() + width, object.getY(), -width, height);
            } else if (object.getType() == 1) {
                batch.draw(dragon, object.getX() + width, object.getY(), -width, height);
            } else if (object.getType() == 2) {
                batch.draw(knight, object.getX() + width, object.getY(), -width, height);
            } else if (object.getType() == 3) {
                batch.draw(mage, object.getX() + width, object.getY(), -width, height);
            } else if (object.getType() == 4) {
                batch.draw(towerEnemy, object.getX(), object.getY(), 512f, 512f);
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(stage.getViewport().getCamera().combined);

        batch.begin();
        batch.draw(skyTexture, 0, 0, 1920, 1080);

        batch.draw(groundTexture, 0, 0, 1920, 1080);

        if(clientManager.getStarted()) {
            if (clientManager.isOver()) {
                game.setScreen(new GameOverUI(game));
            }

            List<GameObject> objects= clientManager.getObjects();

            clientManager.update(delta);
            for(GameObject object : objects) {
                draw(object, clientManager.getInterpolationAlpha());
            }
        }


        batch.end();

        healthLabel.setText("Health: " + Integer.toString(player.getHealth()));
        resourceLabel.setText("Gold: " + Integer.toString(player.getResources()));

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
        batch.dispose();
        skyTexture.dispose();
        groundTexture.dispose();
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
