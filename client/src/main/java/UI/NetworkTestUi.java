package UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;

import Game.Main;
import Network.NetworkManager;
import simulation.Simulation;
import simulation.Simulation.Snapshot;

public class NetworkTestUi implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private Table mainTable;

    // Log variables so your friend can easily update them
    private Label logLabel;
    private String logText = "--- Network Logs Initialized ---\n";
    private ScrollPane logScroll;

    // The mysterious blue box
    private Texture blueTexture;
    private Image blueBox;
    private NetworkManager network;

    private Simulation sim;
    private final float TICK_RATE = 1f / 20f;
    private float tickTime = 0f;

    public NetworkTestUi(Main game, Stage stage, Skin skin) {
        this.game = game;
        this.stage = stage;
        this.skin = skin;
        this.sim = new Simulation(false);
        this.network = new NetworkManager("169.155.61.249", 9000);

        mainTable = new Table();
        mainTable.setFillParent(true);

        // Generate a solid blue texture using code
        Pixmap pixmap = new Pixmap(100, 100, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.ROYAL);
        pixmap.fill();
        blueTexture = new Texture(pixmap);
        pixmap.dispose();
    }

    private void buildUi() {
        mainTable.clear();
        mainTable.top().pad(30f);

        // ==========================================
        // TOP SECTION: Code & Connect
        // ==========================================
        Table connectTable = new Table();

        final TextField codeField = new TextField("", skin);
        codeField.setMessageText("code");

        TextButton connectBtn = new TextButton("connect", skin);

        connectTable.add(codeField).width(200f).height(50f).padRight(20f);
        connectTable.add(connectBtn).width(150f).height(50f);

        mainTable.add(connectTable).expandX().left().padBottom(30f).row();

        // ==========================================
        // MIDDLE SECTION: The Sync Area
        // ==========================================
        Table syncAreaTable = new Table();
        // FIXED: Removed the dangerous setBackground call that caused the crash

        blueBox = new Image(blueTexture);

        syncAreaTable.add(blueBox).expand().center();

        mainTable.add(syncAreaTable).expandX().fillX().height(300f).padBottom(10f).row();

        TextButton checksumBtn = new TextButton("send checksum", skin);
        mainTable.add(checksumBtn).left().width(200f).height(50f).padBottom(30f).row();

        // ==========================================
        // BOTTOM SECTION: Logs
        // ==========================================
        logLabel = new Label(logText, skin);
        logLabel.setAlignment(Align.topLeft);
        logLabel.setWrap(true);

        logScroll = new ScrollPane(logLabel, skin);
        logScroll.setFadeScrollBars(false);

        Table logContainer = new Table();
        // FIXED: Removed the dangerous setBackground call that caused the crash
        logContainer.add(logScroll).expand().fill().pad(10f);

        mainTable.add(logContainer).expand().fill().row();

        TextButton backBtn = new TextButton("Back to Menu", skin);
        mainTable.add(backBtn).left().padTop(20f).width(150f).height(40f);

        // ==========================================
        // BUTTON LOGIC
        // ==========================================

        connectBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int code = Integer.parseInt(codeField.getText());
                addLog("Attempting to connect to room: " + code + "...");
                network.connect(code, NetworkTestUi.this);
            }
        });

        checksumBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addLog("Generating and sending checksum data...");
                long checksum = sim.getCurrentChecksum();
                network.sendChecksum(checksum, sim.getTick());
            }
        });

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuUi(game, stage, skin));
            }
        });

        stage.addActor(mainTable);
    }

    public void addLog(String message) {
        logText += "> " + message + "\n";
        if (logLabel != null) {
            logLabel.setText(logText);
            logScroll.layout();
            logScroll.scrollTo(0, 0, 0, 0);
        }
    }
    
    public void correctPosition(Snapshot snapshot) {
        sim.correct(snapshot);
    }

    @Override
    public void show() {
        stage.setViewport(new FitViewport(1280, 720));
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        Gdx.input.setInputProcessor(stage);
        stage.clear();
        buildUi();
    }

    @Override
    public void render(float delta) {
        tickTime += delta;

        while(tickTime >= TICK_RATE) {
            sim.update();
            tickTime -= TICK_RATE;
        }

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
        if (blueTexture != null) {
            blueTexture.dispose();
        }
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
