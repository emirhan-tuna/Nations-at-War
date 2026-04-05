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
import com.badlogic.gdx.utils.viewport.FitViewport;

import Game.Main;

public class InviteUi implements Screen {
    private Main game;
    private Stage stage;
    private Skin skin;
    private Table mainTable;

    public InviteUi(Main game, Stage stage, Skin skin) {
        this.game = game;
        this.stage = stage;
        this.skin = skin;

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

        // ==========================================
        // LEFT COLUMN: Info, Inviting, and Navigation
        // ==========================================

        Table infoBox = new Table(skin);
        Label userEmailLabel = new Label("player@email.com", skin);
        Label speedLabel = new Label("Ping: 45ms", skin);

        infoBox.add(userEmailLabel).expandX().left();
        infoBox.add(speedLabel).right();

        leftColumn.add(infoBox).expandX().fillX().padBottom(40f).row();

        Label emailTitle = new Label("Email", skin);
        leftColumn.add(emailTitle).left().padBottom(5f).row();

        final TextField inviteEmailField = new TextField("", skin);
        inviteEmailField.setMessageText("Enter opponent's email...");
        leftColumn.add(inviteEmailField).expandX().fillX().height(40f).padBottom(20f).row();

        // CLEANED UP TEXT
        TextButton sendInviteBtn = new TextButton("Send invite", skin);
        leftColumn.add(sendInviteBtn).expandX().fillX().height(50f).padBottom(20f).row();

        // CLEANED UP TEXT
        TextButton backBtn = new TextButton("Back to main menu", skin);
        leftColumn.add(backBtn).expandX().fillX().height(50f).row();


        // ==========================================
        // RIGHT COLUMN: Incoming Invites List
        // ==========================================

        Label invitesTitle = new Label("Your invites", skin);
        rightColumn.add(invitesTitle).left().padBottom(20f).row();

        Table inviteListContainer = new Table(skin);
        inviteListContainer.top().left();

        inviteListContainer.add(createMockInvite("emirhan@bilkent.edu.tr")).expandX().fillX().padBottom(10f).row();
        inviteListContainer.add(createMockInvite("alper@bilkent.edu.tr")).expandX().fillX().padBottom(10f).row();

        rightColumn.add(inviteListContainer).expandX().fillX().top().left();


        // ==========================================
        // ASSEMBLE THE MAIN SCREEN
        // ==========================================

        mainTable.add(leftColumn).expand().fill().pad(50f);
        mainTable.add(rightColumn).expand().fill().pad(50f);

        // ==========================================
        // BUTTON LOGIC
        // ==========================================

        sendInviteBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String targetEmail = inviteEmailField.getText();
                if (!targetEmail.isEmpty()) {
                    System.out.println("LOG: Sending game invite to " + targetEmail);
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

        acceptBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("LOG: Accepted invite from " + senderEmail);
                // game.setScreen(new PlayScreen());
            }
        });

        denyBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("LOG: Denied invite from " + senderEmail);
                row.remove();
            }
        });

        return row;
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
        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void dispose() {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
