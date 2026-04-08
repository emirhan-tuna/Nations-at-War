package UI;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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

public class GameScreenUI implements Screen {
    private Label healthLabel;
    private Label resourceLabel;
    private SpriteBatch batch;
    private Table popupMenu;
    private String lastTroop = "";

    private Texture skyTexture;
    private Texture groundTexture;

    private Main game;
    private Stage stage;
    private Table mainTable;
    private Game.Player player;
    private NetworkManager manager;
    private ClientGameManager clientManager;

    public GameScreenUI(Main game, NetworkManager manager) {
        this.player = new Player(1);
        this.game = game;
        this.stage = new Stage();
        batch = new SpriteBatch();
        this.manager = manager;
        clientManager = new ClientGameManager();

        this.stage = new Stage(new FitViewport(1920, 1080));
        this.batch = new SpriteBatch();

        skyTexture = new Texture(Gdx.files.internal("Sprites/background_clouds.png"));
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
        troopTable.add(mageButton).size(buttonWidth, buttonHeight).padRight(10f);
        troopTable.row().padTop(5f);

        troopTable.add(new Label("500", game.skin)).center().padRight(10f);
        troopTable.add(new Label("100", game.skin)).center().padRight(10f);
        troopTable.add(new Label("75", game.skin)).center().padRight(10f);
        troopTable.add(new Label("150", game.skin)).center().padRight(10f);

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

        mainTable.add(troopTable).expand().top().right();
    }

    public int troopID(String troop) {
        if (troop.equals("archer")) {
            return 0;
        } else if (troop.equals("dragon")) {
            return 1;
        } else if (troop.equals("knight")) {
            return 2;
        } else if (troop.equals("mage")) {
            return 3;
        }

        return -1;
    }

    public void draw(GameObject object) {
        if (object.getTeam() == 0) {
            if (object.getType() == 0) {
                batch.draw(new Texture(Gdx.files.internal("sprites/archer_sprite_left.png")), object.getX(), object.getY());
            } else if (object.getType() == 1) {
                batch.draw(new Texture(Gdx.files.internal("sprites/dragon_sprite_left.png")), object.getX(), object.getY());
            } else if (object.getType() == 2) {
                batch.draw(new Texture(Gdx.files.internal("sprites/knight_sprite_left.png")), object.getX(), object.getY());
            } else if (object.getType() == 3) {
                batch.draw(new Texture(Gdx.files.internal("sprites/mage_sprite_left.png")), object.getX(), object.getY());
            } else if (object.getType() == 4) {
                batch.draw(new Texture(Gdx.files.internal("sprites/tower_sprite.png")), object.getX(), object.getY());
            }
        } else {
            if (object.getType() == 0) {
                batch.draw(new Texture(Gdx.files.internal("sprites/archer_sprite_right.png")), object.getX(), object.getY());
            } else if (object.getType() == 1) {
                batch.draw(new Texture(Gdx.files.internal("sprites/dragon_sprite_right.png")), object.getX(), object.getY());
            } else if (object.getType() == 2) {
                batch.draw(new Texture(Gdx.files.internal("sprites/knight_sprite_right.png")), object.getX(), object.getY());
            } else if (object.getType() == 3) {
                batch.draw(new Texture(Gdx.files.internal("sprites/mage_sprite_right.png")), object.getX(), object.getY());
            } else if (object.getType() == 4) {
                batch.draw(new Texture(Gdx.files.internal("sprites/tower_sprite.png")), object.getX(), object.getY());
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
            clientManager.update(delta);
        }

        List<GameObject> objects= clientManager.getObjects();

        for(GameObject object : objects) {
            draw(object);
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
