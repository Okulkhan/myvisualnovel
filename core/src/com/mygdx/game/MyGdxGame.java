package com.mygdx.game;

import com.badlogic.gdx.Game;

public class MyGdxGame extends Game {
	private SoundManager soundManager;

	@Override
	public void create() {
		soundManager = new SoundManager();
		soundManager.loadMusic("menu_music", "menu_music.mp3");
		soundManager.loadMusic("pause_music", "pause_music.mp3");
		setScreen(new MainMenu(this, soundManager));
	}


	@Override
	public void dispose() {
		soundManager.dispose();
		super.dispose();
	}
}
