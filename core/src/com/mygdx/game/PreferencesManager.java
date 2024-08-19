package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class PreferencesManager {
    private static final String PREFERENCES_NAME = "GamePreferences";
    private static final String KEY_SCENARIOS_INDEX = "scenariosIndex";
    private static final String KEY_TEXT_INDEX = "textIndex";

    private final Preferences preferences;

    public PreferencesManager() {
        preferences = Gdx.app.getPreferences(PREFERENCES_NAME);
    }

    public void saveGame(int scenariosIndex, int textIndex) {
        preferences.putInteger(KEY_SCENARIOS_INDEX, scenariosIndex);
        preferences.putInteger(KEY_TEXT_INDEX, textIndex);
        preferences.flush();
    }

    public int[] loadGame() {
        int scenariosIndex = preferences.getInteger(KEY_SCENARIOS_INDEX, 1);
        int textIndex = preferences.getInteger(KEY_TEXT_INDEX, 0);
        return new int[]{scenariosIndex, textIndex};
    }

    public void clear() {
        preferences.clear();
        preferences.flush();
    }
}
