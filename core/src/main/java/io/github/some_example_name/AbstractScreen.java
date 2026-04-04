package io.github.some_example_name.UI;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class AbstractScreen {
    protected SpriteBatch batch;
    protected OrthographicCamera camera;

    public AbstractScreen() {
        this.batch = new SpriteBatch();
        this.camera = new OrthographicCamera();
    }

    public abstract void render(float delta);
    public abstract void dispose();
}