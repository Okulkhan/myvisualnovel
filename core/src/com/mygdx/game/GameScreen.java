package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class GameScreen extends ScreenAdapter {
    private final MyGdxGame game;
    private final SpriteBatch batch;
    private final Texture[] backgrounds;
    private final Texture textBackground;
    private final Texture texturePng; // Новый оверлей для пятого сценария
    private final BitmapFont font;
    private final GlyphLayout layout;
    private final String[][] textsScenario;
    private int scenariosIndex = 1;
    private int textIndex = 0;
    private boolean isSwitched = false;
    private boolean showingBackground = true;
    private boolean showOverlay = false; // Флаг для отображения оверлея

    private float time;
    private float charDisplayInterval = 0.05f;
    private final StringBuilder currentDisplayedText;

    private static final float TEXT_WIDTH = 1800;
    private static final float TEXT_X = 75;
    private static final float TEXT_Y = 820;
    private static final float TEXT_BACKGROUND_PADDING = 20;

    private boolean paused = false;
    private boolean isInstantTextDisplay = false;
    private final SoundManager soundManager;

    private final Preferences prefs;

    public GameScreen(MyGdxGame game, SoundManager soundManager) {
        this.game = game;
        this.soundManager = soundManager;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont(Gdx.files.internal("font.fnt"));
        this.layout = new GlyphLayout();
        this.backgrounds = new Texture[5];

        for (int i = 0; i < backgrounds.length; i++) {
            backgrounds[i] = new Texture("Scenarios" + (i + 1) + ".jpg");
        }

        this.textBackground = new Texture(Gdx.files.internal("textBackground.png"));
        this.texturePng = new Texture(Gdx.files.internal("groupmates.png")); // Добавьте путь к вашей PNG-картинке

        this.textsScenario = new String[5][];
        textsScenario[0] = loadTexts("scenario1.txt");
        textsScenario[1] = loadTexts("scenario2.txt");
        textsScenario[2] = loadTexts("scenario3.txt");
        textsScenario[3] = loadTexts("scenario4.txt");
        textsScenario[4] = loadTexts("scenario5.txt");
        this.currentDisplayedText = new StringBuilder();
        this.time = 0;

        soundManager.loadMusic("Scenarios1", "scenarios1_music.mp3");
        soundManager.loadMusic("Scenarios4", "scenarios4_music.mp3");
        soundManager.loadSound("OpenDoor", "doorOpen.mp3");

        this.prefs = Gdx.app.getPreferences("GamePreferences");
        this.scenariosIndex = prefs.getInteger("scenariosIndex", 1);
        this.textIndex = prefs.getInteger("textIndex", 0);
    }

    public GameScreen(MyGdxGame game, SoundManager soundManager, int scenarioIndex, int textIndex) {
        this(game, soundManager);
        this.scenariosIndex = scenarioIndex;
        this.textIndex = textIndex;
        this.currentDisplayedText.append(textsScenario[scenarioIndex - 1][textIndex]);
    }

    private String[] loadTexts(String fileName) {
        FileHandle file = Gdx.files.internal(fileName);
        String text = file.readString("UTF-8");
        return text.split("\n");
    }

    @Override
    public void show() {
        playScenarioMusic();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            soundManager.pauseMusic("Scenarios1");
            game.setScreen(new PauseScreen((MyGdxGame) game, soundManager, this));
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        time += delta;

        handleInput(delta);

        batch.begin();
        batch.draw(backgrounds[scenariosIndex - 1], 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (isSwitched || !showingBackground) {
            String displayedText = currentDisplayedText.toString();
            layout.setText(font, displayedText, font.getColor(), TEXT_WIDTH, -1, true);

            float backgroundWidth = 2050;
            float backgroundHeight = 900;
            float backgroundX = -50;
            float backgroundY = -370;
            batch.draw(textBackground, backgroundX, backgroundY, backgroundWidth, backgroundHeight);

            font.draw(batch, layout, TEXT_X, Gdx.graphics.getHeight() - TEXT_Y);

            if (scenariosIndex == 5) {
                batch.draw(texturePng, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            }
        }

        batch.end();
    }

    private void handleInput(float delta) {
        boolean inputDetected = (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT) ||
                Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ENTER));

        if (inputDetected) {
            if (showingBackground) {
                showingBackground = false;
                isSwitched = true;
                showOverlay = false;
                currentDisplayedText.setLength(0);
                time = 0;
                isInstantTextDisplay = false;
                toggleInstantTextDisplay();
            } else if (isSwitched) {
                String fullText = textsScenario[scenariosIndex - 1][textIndex];
                if (currentDisplayedText.length() >= fullText.length()) {
                    textIndex++;
                    if (textIndex >= getCurrentTexts().length) {
                        textIndex = 0;
                        scenariosIndex++;
                        if (scenariosIndex > backgrounds.length) {
                            scenariosIndex = 1;
                        }
                        showingBackground = true;
                        showOverlay = false;
                        playScenarioMusic();
                    }
                    isSwitched = !showingBackground;
                    currentDisplayedText.setLength(0);
                    time = 0;
                } else {
                    currentDisplayedText.setLength(0);
                    currentDisplayedText.append(fullText);
                }
            }
        }

        if (Gdx.input.justTouched() ||
                Gdx.input.isKeyJustPressed(Input.Keys.SPACE) ||
                Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            toggleInstantTextDisplay();
        }

        if (isSwitched && !isInstantTextDisplay) {
            String fullText = textsScenario[scenariosIndex - 1][textIndex];
            int charactersToShow = Math.min(fullText.length(), (int) (time / charDisplayInterval));
            currentDisplayedText.setLength(0);
            currentDisplayedText.append(fullText.substring(0, charactersToShow));

            if (charactersToShow == fullText.length()) {
                toggleInstantTextDisplay();
            }
        }
    }

    void saveGame() {
        prefs.putInteger("scenariosIndex", scenariosIndex);
        prefs.putInteger("textIndex", textIndex);
        prefs.putBoolean("isSwitched", isSwitched);
        prefs.putBoolean("showingBackground", showingBackground);
        prefs.putFloat("time", time);
        prefs.flush();
    }

    private void loadGame() {
        scenariosIndex = prefs.getInteger("scenariosIndex", 1);
        textIndex = prefs.getInteger("textIndex", 0);
        isSwitched = prefs.getBoolean("isSwitched", false);
        showingBackground = prefs.getBoolean("showingBackground", true);
        time = prefs.getFloat("time", 0);
        playScenarioMusic();
    }

    public void saveGameState() {
        Preferences prefs = Gdx.app.getPreferences("GamePreferences");
        prefs.putInteger("scenariosIndex", scenariosIndex);
        prefs.putInteger("textIndex", textIndex);
        prefs.flush();
    }

    private void toggleInstantTextDisplay() {
        isInstantTextDisplay = !isInstantTextDisplay;
    }

    private String[] getCurrentTexts() {
        return textsScenario[scenariosIndex - 1];
    }

    private void playScenarioMusic() {
        try {
            switch (scenariosIndex) {
                case 1:
                case 2:
                case 3:
                    if (!soundManager.isMusicPlaying("Scenarios1")) {
                        soundManager.playMusic("Scenarios1");
                    }
                    break;
                case 4:
                case 5:
                    if (!soundManager.isMusicPlaying("Scenarios4")) {
                        soundManager.playSound("OpenDoor");
                        soundManager.playMusic("Scenarios4");
                    }
                    break;
            }
        } catch (Exception e) {
            Gdx.app.log("GameScreen", "Error playing music: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
        paused = true;
        soundManager.pauseMusic("Scenarios1");
        saveGame();
    }

    @Override
    public void resume() {
        paused = false;
        loadGame();
        playScenarioMusic();
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        textBackground.dispose();
        texturePng.dispose();

        for (Texture texture : backgrounds) {
            texture.dispose();
        }
    }
}
