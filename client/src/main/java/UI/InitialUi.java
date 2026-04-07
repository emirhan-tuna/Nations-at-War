package UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import Network.AccountCheck;
import Network.FirebaseTest;
import Game.Main;
import Network.Stats;

public class InitialUi implements Screen {
    private Texture backTexture;
    private Image backImage;
    private SpriteBatch batch;
    private Main game;
    private Stage stage;
    private Table mainTable;
    private FirebaseTest test;
    private Label title;

    public InitialUi(Main game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(1920, 1080));
        this.test = new FirebaseTest();
        this.batch = new SpriteBatch();

        backTexture = new Texture(Gdx.files.internal("menu_items/background.jpg"));

        title = new Label("NATIONS AT WAR", game.skin, "title");

        mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        showEmailScreen();
    }

    public void showEmailScreen() {
        mainTable.clear();
        mainTable.add(title).padBottom(300f).top().center().row();

        Label emailLabel = new Label("Enter your email:", game.skin, "title");
        emailLabel.setColor(Color.BLACK);
        //emailLabel.setFontScale(2f);
        TextField emailField = new TextField("", game.skin);
        emailField.setMessageText("Email address:");
        TextButton button = new TextButton("Next", game.skin);

        mainTable.add(emailLabel).padBottom(20f).center().row();
        mainTable.add(emailField).size(800f, 50f).padBottom(50f).center().row();
        mainTable.add(button).size(400f, 50f);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String email = emailField.getText();
                if (!email.isEmpty()) {
                    button.setText("Loading");
                    button.setDisabled(true);
                    test.doesAccountExist(email, new AccountCheck() {
                        @Override
                        public void onResult(boolean exists) {
                            Gdx.app.postRunnable(() -> {
                                if (exists) {
                                    showLoginScreen(email);
                                } else {
                                    showSignupScreen(email);
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    public void showLoginScreen(String email) {
        mainTable.clear();

        Label emailLabel = new Label(email, game.skin, "title");
        emailLabel.setColor(Color.BLACK);
        //emailLabel.setFontScale(2f);

        TextField passwordField = new TextField("", game.skin);
        passwordField.setMessageText("Password:");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        TextButton button = new TextButton("Login", game.skin);
        mainTable.add(emailLabel).padBottom(20f).row();
        mainTable.add(passwordField).size(800f, 50f).padBottom(50f).row();
        mainTable.add(button).size(400f, 50f);

        button.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                button.setText("logging in");
                button.setDisabled(true);
                String password = passwordField.getText();
                test.login(email, password, new Stats() {
                    @Override
                    public void statsLoaded(String username, int games, int wins) {
                        Gdx.app.postRunnable(() -> {
                            game.games = games;
                            game.username = username;
                            game.wins = wins;
                            game.setScreen(new MainMenuUi(game, stage, game.skin));
                        });
                    }
                    @Override
                    public void getUserID(String userID) {
                        Gdx.app.postRunnable(() -> game.userID = userID);
                    }
                });
            }
        });
    }

    public void showSignupScreen(String email) {
        mainTable.clear();

        Label emailLabel = new Label(email, game.skin, "title");
        emailLabel.setColor(Color.BLACK);
        //emailLabel.setFontScale(2f);
        TextField passwordField = new TextField("", game.skin);
        passwordField.setMessageText("Password:");
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');

        TextButton button = new TextButton("Signup", game.skin);
        mainTable.add(emailLabel).padBottom(20f).row();
        mainTable.add(passwordField).size(800f, 50f).padBottom(50f).row();
        mainTable.add(button).size(400f,50f);

        button.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                String password = passwordField.getText();
                createUsername(email, password);
            }
        });
    }

    public void createUsername(String email, String password) {
        mainTable.clear();

        Label usernamLabel = new Label("Username:", game.skin);
        usernamLabel.setColor(Color.BLACK);
        usernamLabel.setFontScale(2f);
        TextField usernameTextField = new TextField("", game.skin);
        usernameTextField.setMessageText("Username:");

        TextButton button = new TextButton("Create", game.skin);
        mainTable.add(usernamLabel).padBottom(20f).row();
        mainTable.add(usernameTextField).size(800f, 50f).padBottom(50f).row();
        mainTable.add(button).size(400f, 50f);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                String username = usernameTextField.getText();
                if (!username.isEmpty()) {
                    test.signUp(email, password, username, new Stats() {
                        @Override
                        public void statsLoaded(String username, int games, int wins) {
                            Gdx.app.postRunnable(() -> {
                                game.games = games;
                                game.username = username;
                                game.wins = wins;
                                game.setScreen(new MainMenuUi(game, stage, game.skin));
                            });
                        }
                        @Override
                        public void getUserID(String userID) {
                            Gdx.app.postRunnable(() -> game.userID = userID);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.getProjectionMatrix().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.begin();
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
        backTexture.dispose();
        batch.dispose();
    }

    @Override public void show() { 
        Gdx.input.setInputProcessor(stage);
        mainTable.setFillParent(true);
        stage.addActor(mainTable); 

        showEmailScreen();
    }
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
