package UI;

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

public class FindGameUI implements Screen{
    private Texture backTexture;
    private Image backImage;
    private Main game;
    private Stage stage;
    private Table mainTable;
    private NetworkManager networkManage;

    public FindGameUI(Main game) {
        this.game = game;
        this.stage = new Stage();
        networkManage = new NetworkManager();

        backTexture = new Texture("menu_items/background.jpg");
        backImage = new Image(backTexture);
        backImage.getColor().a = 0.8f;

        backImage.setFillParent(true);
        stage.addActor(backImage);

        mainTable = new Table(); 

        this.networkManage = new NetworkManager();
        HttpRequestBuilder requestBuilder = new HttpRequestBuilder();

        String url = "https://nationsapi.fly.dev/hello";

        HttpRequest request = requestBuilder.newRequest().method(Net.HttpMethods.GET).url(url).build();

        Gdx.net.sendHttpRequest(request, new Net.HttpResponseListener() {
            @Override
            public void handleHttpResponse(Net.HttpResponse response) {

                System.out.print("Got response");
                String responseString = response.getResultAsString();
                JsonReader reader = new JsonReader();
                JsonValue file = reader.parse(responseString);

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        String serverIP = file.getString("host");
                        int port = file.getInt("port");
                        int id = file.getInt("id");

                        GameScreenUI newUI = new GameScreenUI(game, networkManage);
                        ClientGameManager manager = newUI.getClientManager();

                        networkManage.connect(serverIP, port, id, manager);
                        game.setScreen(newUI);

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

