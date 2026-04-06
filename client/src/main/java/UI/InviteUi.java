package UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import Game.Main;

public class InviteUi implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private Table mainTable;
    private Texture backTexture;
    private Image backImage;

    public InviteUi(Main game, Stage stage, Skin skin) {
        this.game = game;
        this.stage = stage;
        this.skin = skin;
        this.batch = new SpriteBatch();

        backTexture = new Texture(Gdx.files.internal("menu_items/background.jpg"));
        backImage = new Image(backTexture);
        backImage.setFillParent(true);
        backImage.getColor().a = 0.8f;
        stage.addActor(backImage);

        mainTable = new Table();
        mainTable.setFillParent(true);
    }

    private void buildUi() {
        mainTable.clear();
        mainTable.center();

        Table leftColumn = new Table();
        Table rightColumn = new Table();

        leftColumn.top().left();
        rightColumn.top().left();

        Label emailTitle = new Label("Email", skin);
        leftColumn.add(emailTitle).left().padBottom(5f).row();

        final TextField inviteEmailField = new TextField("", skin);
        inviteEmailField.setMessageText("Enter opponent's email...");
        leftColumn.add(inviteEmailField).width(350f).height(40f).padBottom(20f).row();

        TextButton sendInviteBtn = new TextButton("Send invite", skin);
        leftColumn.add(sendInviteBtn).width(350f).height(50f).padBottom(20f).row();

        TextButton backBtn = new TextButton("Back to main menu", skin);
        leftColumn.add(backBtn).width(350f).height(50f).padBottom(20f).row();

        TextButton testButton = new TextButton("Test Game UI", skin);
        leftColumn.add(testButton).width(350f).height(50f).row();

        Label invitesTitle = new Label("Your invites", skin);
        rightColumn.add(invitesTitle).left().padBottom(20f).row();

        Table inviteListContainer = new Table(skin);
        inviteListContainer.top().left();
        inviteListContainer.add(createMockInvite("emirhan@bilkent.edu.tr")).expandX().fillX().padBottom(10f).row();
        inviteListContainer.add(createMockInvite("alper@bilkent.edu.tr")).expandX().fillX().padBottom(10f).row();

        rightColumn.add(inviteListContainer).expandX().fillX().top().left();

        mainTable.add(leftColumn).expand().fill().pad(50f);
        mainTable.add(rightColumn).expand().fill().pad(50f);

        sendInviteBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String targetEmail = inviteEmailField.getText();
                if (!targetEmail.isEmpty()) {
                    inviteEmailField.setText("");
                }
            }
        });

        backBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuUi(game, stage, skin));
            }
        });

        testButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y) {
                game.setScreen(new GameScreenUI(game));
            }
        });

        stage.addActor(mainTable);
    }

    private Table createMockInvite(final String senderEmail) {
        final Table row = new Table(skin);
        Label inviteText = new Label("invite from <" + senderEmail + ">", skin);
        TextButton acceptBtn = new TextButton("accept", skin);
        TextButton denyBtn = new TextButton("deny", skin);

        row.add(inviteText).expandX().left().padRight(10f);
        row.add(acceptBtn).padRight(10f);
        row.add(denyBtn);

        denyBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                row.remove();
            }
        });

        return row;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        mainTable.setFillParent(true);
        stage.addActor(mainTable);
        buildUi();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render background to absolute window size
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
        backTexture.dispose();
        batch.dispose();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
