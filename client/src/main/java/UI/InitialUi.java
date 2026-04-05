package UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import Network.AccountCheck;
import Network.FirebaseTest;
import Game.Main;
import Network.Stats;

public class InitialUi implements Screen {
    private Main game;
    private Stage stage;
    private Table mainTable;
    private FirebaseTest test;

    public InitialUi(Main game) {
        this.game = game;
        this.stage = new Stage();
        this.test = new FirebaseTest();

        mainTable = new Table();

        showEmailScreen();
    }

    public void showEmailScreen() {
        mainTable.clear();

        Label emailLabel = new Label("Enter your email:", game.skin);
        TextField emailField = new TextField("", game.skin);
        emailField.setMessageText("Email address:");
        TextButton button = new TextButton("Next", game.skin);

        mainTable.add(emailLabel).padBottom(20f).row();
        mainTable.add(emailField).width(300f).padBottom(10f).row();
        mainTable.add(button).width(150f);

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
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    if (exists) {
                                        showLoginScreen(email);
                                    } else {
                                        showSignupScreen(email);
                                    }
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

        Label emailLabel = new Label(email, game.skin);
        TextField passwordField = new TextField("", game.skin);
        passwordField.setMessageText("Password:");

        TextButton button = new TextButton("Login", game.skin);
        mainTable.add(emailLabel).padBottom(20f).row();
        mainTable.add(passwordField).width(300f).padBottom(10f).row();
        mainTable.add(button).width(150f);

        button.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                button.setText("logging in");
                button.setDisabled(true);
                String password = passwordField.getText();
                test.login(email, password, new Stats() {
                    @Override
                    public void statsLoaded(String username, int games, int wins) {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                game.games = games;
                                game.username = username;
                                game.wins = wins;

                                stage.clear();

                                game.setScreen(new MainMenuUi(game, stage, game.skin));
                            }
                        });
                    }
                });
            }
        });
    }

    public void showSignupScreen(String email) {
        mainTable.clear();

        Label emailLabel = new Label(email, game.skin);
        TextField passwordField = new TextField("", game.skin);
        passwordField.setMessageText("Password:");

        TextButton button = new TextButton("Signup", game.skin);
        mainTable.add(emailLabel).padBottom(20f).row();
        mainTable.add(passwordField).width(300f).padBottom(10f).row();
        mainTable.add(button).width(150f);

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
        TextField usernameTextField = new TextField("", game.skin);
        usernameTextField.setMessageText("Username:");

        TextButton button = new TextButton("Create", game.skin);
        mainTable.add(usernamLabel).padBottom(20f).row();
        mainTable.add(usernameTextField).width(300f).padBottom(10f).row();
        mainTable.add(button).width(150f);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                String username = usernameTextField.getText();
                if (!username.isEmpty()) {
                    test.signUp(email, password, username, new Stats() {
                        @Override
                        public void statsLoaded(String username, int games, int wins) {
                            Gdx.app.postRunnable(new Runnable() {
                                @Override
                                public void run() {
                                    game.games = games;
                                    game.username = username;
                                    game.wins = wins;

                                    stage.clear();

                                    game.setScreen(new MainMenuUi(game, stage, game.skin));
                                }
                            });
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
        showEmailScreen();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
