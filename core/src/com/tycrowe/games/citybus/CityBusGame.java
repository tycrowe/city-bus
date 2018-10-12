package com.tycrowe.games.citybus;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.tycrowe.games.citybus.screens.HighwayLevel;

public class CityBusGame extends Game {
	public enum LOG_TAGS {
		NORMAL,
		LOADING
	}
    public static final float GAME_DELTA = 0.1f;
	public static final float CAMERA_LERP = 2.5f;
	public static final boolean DEBUG_MODE = true;
	// Asset Manager
	private AssetManager am;
	private boolean isLoading = true;

	private HighwayLevel hl;

	@Override
	public void create () {
		this.am = new AssetManager();
		am.load("bus_01.png", Texture.class);
		am.load("street_01.png", Texture.class);
		am.load("street_02.png", Texture.class);
		am.load("car_01.png", Texture.class);
		this.hl = new HighwayLevel(am);
	}

	@Override
	public void render () {
		if(isLoading) {
			if (am.update()) {
				Gdx.app.log(LOG_TAGS.LOADING.name(), "Done loading, now switching screens.");
				isLoading = false;
				setScreen(hl);
			} else
				Gdx.app.log(LOG_TAGS.LOADING.name(), "" + am.getProgress());
		} else
			super.render();
	}
	
	@Override
	public void dispose () {}

	@Override
	public void resize (int width, int height) {}
}
