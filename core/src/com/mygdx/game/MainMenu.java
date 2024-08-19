package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenu extends ScreenAdapter {
    private final Game game;
    private final Stage stage;
    private final Texture startButtonUpTexture;
    private final Texture startButtonHoverTexture;
    private final Texture startButtonDownTexture;
    private final Texture continueButtonUpTexture;
    private final Texture continueButtonHoverTexture;
    private final Texture continueButtonDownTexture;
    private final Texture exitButtonUpTexture;
    private final Texture exitButtonHoverTexture;
    private final Texture exitButtonDownTexture;
    private final Texture backgroundTexture;
    private final Texture logoTexture;
    private final Texture volumeUpButtonTexture;
    private final Texture volumeDownButtonTexture;
    private final SoundManager soundManager;
    private final PreferencesManager preferencesManager;

    public MainMenu(Game game, SoundManager soundManager) {
        this.game = game;
        this.soundManager = soundManager;
        this.preferencesManager = new PreferencesManager();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        startButtonUpTexture = new Texture(Gdx.files.internal("play1.png"));
        startButtonHoverTexture = new Texture(Gdx.files.internal("play2.png"));
        startButtonDownTexture = new Texture(Gdx.files.internal("play2.png"));

        continueButtonUpTexture = new Texture(Gdx.files.internal("continue.png"));
        continueButtonHoverTexture = new Texture(Gdx.files.internal("continue1.png"));
        continueButtonDownTexture = new Texture(Gdx.files.internal("continue1.png"));

        exitButtonUpTexture = new Texture(Gdx.files.internal("exit.png"));
        exitButtonHoverTexture = new Texture(Gdx.files.internal("ex1.png"));
        exitButtonDownTexture = new Texture(Gdx.files.internal("ex1.png"));

        logoTexture = new Texture(Gdx.files.internal("logo.png"));

        backgroundTexture = new Texture(Gdx.files.internal("menu-background.jpg"));

        volumeUpButtonTexture = new Texture(Gdx.files.internal("volume_up.png"));
        volumeDownButtonTexture = new Texture(Gdx.files.internal("volume_down.png"));
    }

    @Override
    public void show() {
        TextureRegionDrawable startButtonUp = new TextureRegionDrawable(startButtonUpTexture);
        TextureRegionDrawable startButtonHover = new TextureRegionDrawable(startButtonHoverTexture);
        TextureRegionDrawable startButtonDown = new TextureRegionDrawable(startButtonDownTexture);

        ImageButton.ImageButtonStyle startButtonStyle = new ImageButton.ImageButtonStyle();
        startButtonStyle.up = startButtonUp;
        startButtonStyle.over = startButtonHover;
        startButtonStyle.down = startButtonDown;

        ImageButton startButton = new ImageButton(startButtonStyle);
        startButton.setSize(500, 250);
        startButton.setPosition((float) Gdx.graphics.getWidth() / 2 - 250, (float) Gdx.graphics.getHeight() / 2 -100);
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundManager.stopMusic("menu_music");
                preferencesManager.clear(); // Очистка сохраненных данных
                game.setScreen(new GameScreen((MyGdxGame) game, soundManager)); // Начало новой игры
            }
        });
        stage.addActor(startButton);

        TextureRegionDrawable continueButtonUp = new TextureRegionDrawable(continueButtonUpTexture);
        TextureRegionDrawable continueButtonHover = new TextureRegionDrawable(continueButtonHoverTexture);
        TextureRegionDrawable continueButtonDown = new TextureRegionDrawable(continueButtonDownTexture);

        ImageButton.ImageButtonStyle continueButtonStyle = new ImageButton.ImageButtonStyle();
        continueButtonStyle.up = continueButtonUp;
        continueButtonStyle.over = continueButtonHover;
        continueButtonStyle.down = continueButtonDown;

        ImageButton continueButton = new ImageButton(continueButtonStyle);
        continueButton.setSize(500, 250);
        continueButton.setPosition((float) Gdx.graphics.getWidth() / 2 - 250, (float) Gdx.graphics.getHeight() / 2 - 220);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                soundManager.stopMusic("menu_music");
                int[] gameState = preferencesManager.loadGame(); // Загрузка сохраненного состояния
                game.setScreen(new GameScreen((MyGdxGame) game, soundManager, gameState[0], gameState[1]));
            }
        });
        stage.addActor(continueButton);

        TextureRegionDrawable exitButtonUp = new TextureRegionDrawable(exitButtonUpTexture);
        TextureRegionDrawable exitButtonHover = new TextureRegionDrawable(exitButtonHoverTexture);
        TextureRegionDrawable exitButtonDown = new TextureRegionDrawable(exitButtonDownTexture);

        ImageButton.ImageButtonStyle exitButtonStyle = new ImageButton.ImageButtonStyle();
        exitButtonStyle.up = exitButtonUp;
        exitButtonStyle.over = exitButtonHover;
        exitButtonStyle.down = exitButtonDown;

        ImageButton exitButton = new ImageButton(exitButtonStyle);
        exitButton.setSize(500, 250);
        exitButton.setPosition((float) Gdx.graphics.getWidth() / 2 - 250, (float) Gdx.graphics.getHeight() / 2 - 350);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });
        stage.addActor(exitButton);

        TextureRegionDrawable volumeUpButtonDrawable = new TextureRegionDrawable(volumeUpButtonTexture);
        ImageButton volumeUpButton = new ImageButton(volumeUpButtonDrawable);
        volumeUpButton.setSize(100, 100);
        volumeUpButton.setPosition(Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 150);
        volumeUpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                float currentVolume = soundManager.getVolume();
                soundManager.setVolume(Math.min(currentVolume + 0.1f, 1.0f));
            }
        });
        stage.addActor(volumeUpButton);

        TextureRegionDrawable volumeDownButtonDrawable = new TextureRegionDrawable(volumeDownButtonTexture);
        ImageButton volumeDownButton = new ImageButton(volumeDownButtonDrawable);
        volumeDownButton.setSize(100, 100);
        volumeDownButton.setPosition(Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 270);
        volumeDownButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                float currentVolume = soundManager.getVolume();
                soundManager.setVolume(Math.max(currentVolume - 0.1f, 0.0f));
            }
        });
        stage.addActor(volumeDownButton);

        Image logo = new Image(logoTexture);
        logo.setSize(600, 300);
        logo.setPosition((float) Gdx.graphics.getWidth() / 2 - 300, (float) Gdx.graphics.getHeight() - 400);
        stage.addActor(logo);

        soundManager.playMusic("menu_music");
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
        startButtonUpTexture.dispose();
        startButtonHoverTexture.dispose();
        startButtonDownTexture.dispose();
        continueButtonUpTexture.dispose();
        continueButtonHoverTexture.dispose();
        continueButtonDownTexture.dispose();
        exitButtonUpTexture.dispose();
        exitButtonHoverTexture.dispose();
        exitButtonDownTexture.dispose();
        backgroundTexture.dispose();
        volumeUpButtonTexture.dispose();
        volumeDownButtonTexture.dispose();
        logoTexture.dispose();
    }
}
