package io.github.some_example_name.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import io.github.some_example_name.AccountCheck;
import io.github.some_example_name.FirebaseTest;

public class InitialUi {
    private Stage stage;
    private Skin skin;
    private Table mainTable;
    private FirebaseTest test;

    public InitialUi(Stage aStage, Skin aSkin) {
        this.stage = aStage;
        this.skin = aSkin;
        this.test = new FirebaseTest();

        mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        showEmailScreen();
    }

    public void showEmailScreen() {
        mainTable.clear();

        Label emailLabel = new Label("Enter your email:", skin);
        TextField emailField = new TextField("", skin);
        emailField.setMessageText("Email address:");
        TextButton button = new TextButton("Next", skin);

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

        Label emailLabel = new Label(email, skin);
        TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("Password:");

        TextButton button = new TextButton("Login", skin);
        mainTable.add(emailLabel).padBottom(20f).row();
        mainTable.add(passwordField).width(300f).padBottom(10f).row();
        mainTable.add(button).width(150f);

        button.addListener(new ClickListener() {
            public void clicked(InputEvent e, float x, float y) {
                button.setText("logging in");
                button.setDisabled(true);
                String password = passwordField.getText();
                test.login(email, password);
            }
        });
    }

    public void showSignupScreen(String email) {
        mainTable.clear();

        Label emailLabel = new Label(email, skin);
        TextField passwordField = new TextField("", skin);
        passwordField.setMessageText("Password:");

        TextButton button = new TextButton("Signup", skin);
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

        Label usernamLabel = new Label("Username:", skin);
        TextField usernameTextField = new TextField("", skin);
        usernameTextField.setMessageText("Username:");

        TextButton button = new TextButton("Create", skin);
        mainTable.add(usernamLabel).padBottom(20f).row();
        mainTable.add(usernameTextField).width(300f).padBottom(10f).row();
        mainTable.add(button).width(150f);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                String username = usernameTextField.getText();
                if (!username.isEmpty()) {
                    test.signUp(email, password, username);
                }
            }
        });
    }
}
