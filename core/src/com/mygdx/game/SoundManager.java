package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

public class SoundManager {
    private final HashMap<String, Music> musicMap;
    private final HashMap<String, Sound> soundMap;
    private Music currentMusic;
    private float volume;

    public SoundManager() {
        musicMap = new HashMap<>();
        soundMap = new HashMap<>();
        volume = 0.1f;
    }

    public void loadMusic(String key, String fileName) {
        Music music = Gdx.audio.newMusic(Gdx.files.internal(fileName));
        musicMap.put(key, music);
    }

    public void loadSound(String key, String fileName) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(fileName));
        soundMap.put(key, sound);
    }


    public void playMusic(String key) {
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.stop();
        }
        currentMusic = musicMap.get(key);
        if (currentMusic != null) {
            currentMusic.setLooping(true);
            currentMusic.setVolume(volume);
            currentMusic.play();
        }
    }

    public void pauseMusic(String musicName) {
        Music music = musicMap.get(musicName);
        if (music != null && music.isPlaying()) {
            music.pause();
        }
    }

    public boolean isMusicPlaying(String musicName) {
        Music music = musicMap.get(musicName);
        return music != null && music.isPlaying();
    }

    public void playSound(String key) {
        Sound sound = soundMap.get(key);
        if (sound != null) {
            sound.play(volume);
        }
    }

    public void stopMusic(String key) {
        Music music = musicMap.get(key);
        if (music != null && music.isPlaying()) {
            music.stop();
        }
    }

    public void stopAllMusic() {
        for (Music music : musicMap.values()) {
            if (music.isPlaying()) {
                music.stop();
            }
        }
    }

    public void setVolume(float volume) {
        this.volume = volume;
        if (currentMusic != null) {
            currentMusic.setVolume(volume);
        }
    }

    public float getVolume() {
        return volume;
    }

    public void dispose() {
        for (Music music : musicMap.values()) {
            music.dispose();
        }
        for (Sound sound : soundMap.values()) {
            sound.dispose();
        }
    }
}
