package UI;

import org.omg.CORBA.Request;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import Game.Main;
import Network.NetworkManager;
import network.Routes;

public class FindGameUI implements Screen {
    private Texture backTexture;
    private SpriteBatch batch;
    private Main game;
    private Stage stage;
    private Table mainTable;
    private NetworkManager networkManage;

    // UI elements we need to update when the server responds
    private Label progressLabel;
    private Image loadingImage;
    private TextButton cancelButton;

    public FindGameUI(Main game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(1920, 1080));
        this.batch = new SpriteBatch();
        this.stage = new Stage();
        networkManage = new NetworkManager();

        backTexture = new Texture(Gdx.files.internal("menu_items/background.jpg"));

        mainTable = new Table();
        mainTable.setFillParent(true);

        this.networkManage = new NetworkManager();
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        String url = "https://nationsapi.fly.dev/hello";

        HttpRequest request = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(Routes.API_hello).build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse response) {

                String responseString = response.getResultAsString();
                JsonReader reader = new JsonReader();
                JsonValue file = reader.parse(responseString);

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        String serverIP = file.getString("host");
                        int port = file.getInt("port");
                        long id = file.getLong("id");

                        GameScreenUI newUI = new GameScreenUI(game, networkManage);

                        networkManage.connect(serverIP, port, id, newUI);

                        GameScreenUI newUI = new GameScreenUI(game);
                    }
                });
            }
            @Override
            public void failed(Throwable t) {}
            @Override
            public void cancelled() {}
        });
    }

    public void createUI() {
        mainTable.clear();
        mainTable.center();

        progressLabel = new Label("Contacting Master Server...", game.skin, "very_big_title");
        mainTable.add(progressLabel).padBottom(50f).row();

        Texture loadingTexture = new Texture(Gdx.files.internal("menu_items/loading_symbol.png"));
        loadingImage = new Image(loadingTexture);
        float newSize = 100f;

        loadingImage.setSize(newSize, newSize);
        loadingImage.setOrigin(newSize / 2f, newSize / 2f);
        loadingImage.addAction(Actions.forever(Actions.rotateBy(360f, 1.5f)));

        mainTable.add(loadingImage).padBottom(50f).row();

        cancelButton = new TextButton("Cancel", game.skin);
        mainTable.add(cancelButton).width(200f).height(60f);

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                System.out.println("LOG: Matchmaking cancelled.");
                game.setScreen(new MainMenuUi(game, stage, game.skin));
            }
        });
    }

    private void findGameViaAPI() {
        Net.HttpRequest request = new Net.HttpRequest(Net.HttpMethods.GET);
        request.setUrl("https://nationsapi.fly.dev/hello");

        System.out.println("LOG: Requesting game server allocation from API...");

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse httpResponse) {
                final String jsonResponse = httpResponse.getResultAsString();
                System.out.println("LOG: API Responded with: " + jsonResponse);

                Gdx.app.postRunnable(() -> {
                    try {
                        if (jsonResponse == null || jsonResponse.trim().isEmpty()) {
                            throw new Exception("Server returned an empty response!");
                        }

                        JsonReader jsonReader = new JsonReader();
                        JsonValue base = jsonReader.parse(jsonResponse);

                        if (base == null) {
                            throw new Exception("JSON parsing failed.");
                        }

                        // ==========================================
                        // NEW SAFETY CHECK: Did the API send an error?
                        // ==========================================
                        if (base.has("error")) {
                            String errorMessage = base.getString("error");
                            // Throwing this exception jumps straight to the catch block below
                            throw new Exception(errorMessage);
                        }

                        // If we made it here, we have a real server!
                        String serverIp = base.getString("host");
                        int serverPort = base.getInt("port");
                        int code = 0;

                        progressLabel.setText("Server found! Connecting...");
                        loadingImage.clearActions();

                        GameScreenUI gameScreen = new GameScreenUI(game);

                        new Thread(() -> {
                            networkManage.connect(serverIp, serverPort, code, gameScreen);

                            Gdx.app.postRunnable(() -> {
                                game.setScreen(gameScreen);
                            });
                        }).start();

                    } catch (Exception e) {
                        System.err.println("LOG: Matchmaking Error - " + e.getMessage());

                        // Display the exact API error on the screen for the player
                        progressLabel.setText("Failed: " + e.getMessage());
                        loadingImage.clearActions();
                    }
                });
            }

            @Override
            public void failed(Throwable t) {
                Gdx.app.postRunnable(() -> {
                    System.out.println("LOG: API Request Failed: " + t.getMessage());
                    progressLabel.setText("Failed to reach matchmaking server.");
                    loadingImage.clearActions();
                });
            }

            @Override
            public void cancelled() {
                Gdx.app.postRunnable(() -> {
                    progressLabel.setText("Matchmaking cancelled.");
                    loadingImage.clearActions();
                });
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the background perfectly to the window size
        batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.begin();
        batch.setColor(1f, 1f, 1f, 0.8f); // 80% opacity to darken the background
        batch.draw(backTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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
        batch.dispose();
        backTexture.dispose();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        mainTable.setFillParent(true);
        createUI();
        stage.addActor(mainTable);

        // Instantly start looking for a game when this screen opens
        findGameViaAPI();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
