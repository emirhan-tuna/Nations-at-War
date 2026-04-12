package UI;

import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.net.HttpRequestBuilder;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import Game.ClientGameManager;
import Game.Main;
import Network.NetworkManager;
import network.Routes;

public class FindGameUI implements Screen{
    private boolean gameFound = false;
    private Texture backTexture;
    private Image backImage;
    private Main game;
    private Stage stage;
    private Table mainTable;
    private NetworkManager networkManage;

    public FindGameUI(Main game) {
        this.game = game;
        this.stage = new Stage();
        networkManage = game.networkManager;

        backTexture = new Texture("menu_items/background.jpg");
        backImage = new Image(backTexture);
        backImage.getColor().a = 0.8f;

        backImage.setFillParent(true);
        stage.addActor(backImage);

        mainTable = new Table(); 
        matchmake();

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                getAnswer(this);
            }
        }, 0f, 1f);
    }

    public void createUI() {
        Label progress = new Label("Finding game", game.skin, "very_big_title");

        mainTable.add(progress).padBottom(50f).row();

        Texture loadingTexture = new Texture("menu_items/loading_symbol.png");
        Image loadingImage = new Image(loadingTexture);
        float newSize = 100f;

        loadingImage.setSize(newSize, newSize);

        loadingImage.setOrigin(newSize / 2f, newSize / 2f);
        loadingImage.addAction(Actions.forever(Actions.rotateBy(360f, 1.5f)));

        mainTable.add(loadingImage).padBottom(20f);
    }

    public void getAnswer(Timer.Task task) {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        String matchmakeURL = Routes.API_HOST + ":" + Routes.API_PORT + Routes.API_MATCHMAKER_STATUS;

        HttpRequest request = requestBuilder.newRequest()
        .method(Net.HttpMethods.GET)
        .header("Content-type", "application/json")
        .header("Authorization", "Bearer " + game.userToken) 
        .url(matchmakeURL)
        .build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse response) {

                System.out.print("Got response");
                int status = response.getStatus().getStatusCode();
                System.out.println(status);
                String responseString = response.getResultAsString();
                JsonReader reader = new JsonReader();
                JsonValue file = reader.parse(responseString);

                int serverStatus = file.getInt("status"); 
                System.out.println("Server status");

                if (serverStatus == 0) {
                    if (gameFound) {
                        task.cancel();

                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                JsonValue serverInfo = file.get("server");
                                int gameId = serverInfo.getInt("gameId");
                                String host = serverInfo.getString("host");
                                int port = serverInfo.getInt("port");

                                GameScreenUI newUI = new GameScreenUI(game, networkManage);
                                ClientGameManager manager = newUI.getClientManager();

                                networkManage.connect(host, port, gameId, game.userToken, manager);
                                game.setScreen(newUI);

                            }
                        });
                    } else if (serverStatus == 2) {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                game.setScreen(new MainMenuUi(game, stage, game.skin));
                            }
                        });
                    }  
                }  
            }
            @Override
            public void failed(Throwable t) {}
            @Override
            public void cancelled() {}
        });
    }

    public void matchmake() {
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        String matchmakeURL = Routes.API_HOST + ":" + Routes.API_PORT + Routes.API_MATCHMAKER_JOIN;

        Net.HttpRequest request = requestBuilder.newRequest()
            .method(Net.HttpMethods.POST)
            .url(matchmakeURL)
            .header("Content-type", "application/json")
            .header("Authorization", "Bearer " + game.userToken) 
            .build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse response) {

                System.out.print("Got response");
                int status = response.getStatus().getStatusCode();
                String responseString = response.getResultAsString();
                JsonReader reader = new JsonReader();
                JsonValue file = reader.parse(responseString);

                if (status == 200) {
                    System.out.println("Success.");
                }
                
            }
            @Override
            public void failed(Throwable t) {}
            @Override
            public void cancelled() {}
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

        createUI();
        stage.addActor(mainTable);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}

