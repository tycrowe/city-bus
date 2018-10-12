package com.tycrowe.games.citybus.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class PlayerCar extends Actor {

    private Texture playerCarTexture;
    private Rectangle box;
    private float carSpeed = 2f;
    private Vector2 position;
    private Vector2 carCenter;

    public PlayerCar(String name, Texture playerCarTexture) {
        this.playerCarTexture = playerCarTexture;
        this.setName(name);
        setSize(playerCarTexture.getWidth(), playerCarTexture.getHeight());
        this.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(InputEvent event, char character) {
                return false;
            }
        });
        this.position = new Vector2(getX(), getY());
        this.box = new Rectangle(getX(), getY(), getWidth(), getHeight());
        this.carCenter = new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(playerCarTexture, getX(), getY());
    }

    @Override
    public void act(float delta) {
        position.set(getX(), getY());
        setY(getY() + carSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            setY((getY() + (carSpeed + 1.5f)));
        }
        box.set(getX(), getY(), getWidth(), getHeight());
        carCenter.x = getX() + getWidth() / 2;
        carCenter.y = getY() + getHeight() / 2;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getCarSpeed() {
        return carSpeed;
    }

    public Rectangle getBox() {
        return box;
    }

    public Vector2 getCarCenter() {
        return carCenter;
    }

}
