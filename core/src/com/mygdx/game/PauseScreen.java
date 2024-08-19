package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class PauseScreen extends ScreenAdapter {
    private final MyGdxGame game;
    private final SoundManager soundManager;
    private final GameScreen gameScreen;
    private final Stage stage;
    private final Texture resumeButtonUpTexture;
    private final Texture resumeButtonHoverTexture;
    private final Texture resumeButtonDownTexture;
    private final Texture saveButtonUpTexture;
    private final Texture saveButtonHoverTexture;
    private final Texture saveButtonDownTexture;
    private final Texture exitButtonUpTexture;
    private final Texture exitButtonHoverTexture;
    private final Texture exitButtonDownTexture;
    private final Texture backgroundTexture;

    public PauseScreen(MyGdxGame game, SoundManager soundManager, GameScreen gameScreen) {
        this.game = game;
        this.soundManager = soundManager;
        this.gameScreen = gameScreen;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        resumeButtonUpTexture = new Texture(Gdx.files.internal("continue.png"));
        resumeButtonHoverTexture = new Texture(Gdx.files.internal("continue1.png"));
        resumeButtonDownTexture = new Texture(Gdx.files.internal("continue1.png"));

        saveButtonUpTexture = new Texture(Gdx.files.internal("save.png"));
        saveButtonHoverTexture = new Texture(Gdx.files.internal("save1.png"));
        saveButtonDownTexture = new Texture(Gdx.files.internal("save1.png"));

        exitButtonUpTexture = new Texture(Gdx.files.internal("Exit1.png"));
        exitButtonHoverTexture = new Texture(Gdx.files.internal("Exit2.png"));
        exitButtonDownTexture = new Texture(Gdx.files.internal("Exit2.png"));

        backgroundTexture = new Texture(Gdx.files.internal("pause-Screen.png"));
    }

    @Override
    public void show() {
        TextureRegionDrawable resumeButtonUp = new TextureRegionDrawable(resumeButtonUpTexture);
        TextureRegionDrawable resumeButtonHover = new TextureRegionDrawable(resumeButtonHoverTexture);
        TextureRegionDrawable resumeButtonDown = new TextureRegionDrawable(resumeButtonDownTexture);

        ImageButton.ImageButtonStyle resumeButtonStyle = new ImageButton.ImageButtonStyle();
        resumeButtonStyle.up = resumeButtonUp;
        resumeButtonStyle.over = resumeButtonHover;
        resumeButtonStyle.down = resumeButtonDown;

        ImageButton resumeButton = new ImageButton(resumeButtonStyle);
        resumeButton.setSize(500, 250);
        resumeButton.setPosition((float) Gdx.graphics.getWidth() / 2 - 250, (float) Gdx.graphics.getHeight() / 2);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundManager.stopMusic("pause_music");
                game.setScreen(gameScreen);
            }
        });
        stage.addActor(resumeButton);

        TextureRegionDrawable saveButtonUp = new TextureRegionDrawable(saveButtonUpTexture);
        TextureRegionDrawable saveButtonHover = new TextureRegionDrawable(saveButtonHoverTexture);
        TextureRegionDrawable saveButtonDown = new TextureRegionDrawable(saveButtonDownTexture);

        ImageButton.ImageButtonStyle saveButtonStyle = new ImageButton.ImageButtonStyle();
        saveButtonStyle.up = saveButtonUp;
        saveButtonStyle.over = saveButtonHover;
        saveButtonStyle.down = saveButtonDown;

        ImageButton saveButton = new ImageButton(saveButtonStyle);
        saveButton.setSize(500, 250);
        saveButton.setPosition((float) Gdx.graphics.getWidth() / 2 - 250, (float) Gdx.graphics.getHeight() / 2 - 150);
        saveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.saveGameState();
                // Добавьте логику для отображения подтверждения сохранения, если необходимо
            }
        });
        stage.addActor(saveButton);

        TextureRegionDrawable exitButtonUp = new TextureRegionDrawable(exitButtonUpTexture);
        TextureRegionDrawable exitButtonHover = new TextureRegionDrawable(exitButtonHoverTexture);
        TextureRegionDrawable exitButtonDown = new TextureRegionDrawable(exitButtonDownTexture);

        ImageButton.ImageButtonStyle exitButtonStyle = new ImageButton.ImageButtonStyle();
        exitButtonStyle.up = exitButtonUp;
        exitButtonStyle.over = exitButtonHover;
        exitButtonStyle.down = exitButtonDown;

        ImageButton exitButton = new ImageButton(exitButtonStyle);
        exitButton.setSize(500, 250);
        exitButton.setPosition((float) Gdx.graphics.getWidth() / 2 - 250, (float) Gdx.graphics.getHeight() / 2 - 300);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundManager.stopMusic("pause_music");
                game.setScreen(new MainMenu(game, soundManager));
            }
        });
        stage.addActor(exitButton);

        soundManager.setVolume(0.1f);
        soundManager.playMusic("pause_music");

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getBatch().begin();
        stage.getBatch().draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage.getBatch().end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        resumeButtonUpTexture.dispose();
        resumeButtonHoverTexture.dispose();
        resumeButtonDownTexture.dispose();
        saveButtonUpTexture.dispose();
        saveButtonHoverTexture.dispose();
        saveButtonDownTexture.dispose();
        exitButtonUpTexture.dispose();
        exitButtonHoverTexture.dispose();
        exitButtonDownTexture.dispose();
        backgroundTexture.dispose();
    }
}
