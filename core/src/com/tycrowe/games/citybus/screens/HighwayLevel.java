package com.tycrowe.games.citybus.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.tycrowe.games.citybus.CityBusGame;
import com.tycrowe.games.citybus.actors.PlayerCar;
import com.tycrowe.games.citybus.ai.GameDirector;

public class HighwayLevel implements Screen {
    // Major accessors
    private AssetManager am;        // For asset manager
    private GameDirector director;  // For ai management
    private Stage gameStage;        // For updating actors
    private Camera gameCamera;      // For displaying stage

    // UI accessors
    private Stage uiStage;          // For ui actors
    private Camera uiCamera;        // For ui display

    // Major Actors
    private PlayerCar playerCar;

    // Helpers
    public Vector2 leftLane;
    public Vector2 rightLane;

    public HighwayLevel(AssetManager am) {
        this.am = am;
    }

    @Override
    public void show() {
        Gdx.app.log(CityBusGame.LOG_TAGS.NORMAL.name(), "Switched to HighwayLevel screen.");
        this.gameStage = new Stage(new ScreenViewport());
        this.gameCamera = gameStage.getCamera();

        // Set what processes input
        Gdx.input.setInputProcessor(gameStage);

        // Instantiate player car
        this.playerCar = new PlayerCar("Nick Brummitt", am.get("car_01.png", Texture.class));

        this.leftLane = new Vector2(-30, 0);
        this.rightLane = new Vector2(-135, 0);

        // Add the actor and adjust the camera.
        gameStage.addActor(playerCar);
        gameCamera.position.set(
                (playerCar.getWidth() / 2f) - playerCar.getX(),
                (playerCar.getHeight() / 2f) + playerCar.getY(),
                0
        );

        this.uiStage = new Stage(new ScreenViewport());
        this.director = new GameDirector(am, this, gameStage, uiStage, playerCar);
        director.addFirstHelper(rightLane.x, 0);
    }

    @Override
    public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		gameCamera.position.x += (playerCar.getCenter().x - gameCamera.position.x) * (playerCar.getSpeed() * .25) * CityBusGame.CAMERA_LERP * CityBusGame.GAME_DELTA;
        gameCamera.position.y += (playerCar.getCenter().y - gameCamera.position.y) * (playerCar.getSpeed()* .25) * CityBusGame.CAMERA_LERP * CityBusGame.GAME_DELTA;
        gameCamera.update();

        // UI
        uiStage.draw();
        uiStage.act();

		gameStage.getBatch().begin();
        director.update(gameStage.getBatch());
        gameStage.getBatch().end();
        gameStage.act(delta);
        gameStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        gameStage.getViewport().update(width, height);
        uiStage.getViewport().update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        gameStage.dispose();
        uiStage.dispose();
        am.dispose();
    }
}
