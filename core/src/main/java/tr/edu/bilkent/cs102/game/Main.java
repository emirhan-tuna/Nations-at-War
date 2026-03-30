package tr.edu.bilkent.cs102.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Main extends ApplicationAdapter {
    Stage stage;
    Texture bgTexture;
    Table table;

    ImageButton playButton;
    ImageButton settingsButton;

    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        bgTexture = new Texture(Gdx.files.internal("menu_items/background.jpg"));
        Texture playTex = new Texture(Gdx.files.internal("menu_items/play_button.png"));
        Texture settingsTex = new Texture(Gdx.files.internal("menu_items/settings_button.png"));

        TextureRegionDrawable playDrawable = new TextureRegionDrawable(new TextureRegion(playTex));
        TextureRegionDrawable settingsDrawable = new TextureRegionDrawable(new TextureRegion(settingsTex));

        playButton = new ImageButton(playDrawable);
        settingsButton = new ImageButton(settingsDrawable);

        table = new Table();
        table.setFillParent(true);
        table.center();

        table.add(playButton).size(300, 100).padBottom(20).row();
        table.add(settingsButton).size(300, 100);

        stage.addActor(table);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("LOG: Play Button Clicked!");
            }
        });
    }

    @Override
    public void render() {
        ScreenUtils.clear(0, 0, 0, 1);

        stage.getBatch().begin();
        stage.getBatch().draw(bgTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        bgTexture.dispose();
    }
}
