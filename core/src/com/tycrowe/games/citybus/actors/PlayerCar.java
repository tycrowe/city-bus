package com.tycrowe.games.citybus.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerCar extends Actor {

    private Texture playerCarTexture;
    private Polygon collisionBox;
    private float carSpeed = 2f;
    private Vector2 position;
    private Vector2 carCenter;

    public PlayerCar(String name, Texture playerCarTexture) {
        this.playerCarTexture = playerCarTexture;
        this.setName(name);
        setSize(playerCarTexture.getWidth(), playerCarTexture.getHeight());
        this.position = new Vector2(getX(), getY());
        this.collisionBox = new Polygon(new float[] {
                getX(), getY(),
                getX() + getWidth(), getY(),
                getX() + getWidth(), getY() + getHeight(),
                getX(), getY() + getHeight()
        });
        this.carCenter = new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(
                playerCarTexture,
                getX(), getY(),
                getOriginX(), getOriginY(),
                getWidth(), getHeight(),
                getScaleX(), getScaleY(),
                getRotation(),
                0, 0, (int) getWidth(), (int) getHeight(),
                true, false
        );
    }

    @Override
    public void act(float delta) {
        position.set(getX(), getY());
        setY(getY() + carSpeed);
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            setY((getY() + (carSpeed + 1.5f)));
        }
        carCenter.x = getX() + getWidth() / 2;
        carCenter.y = getY() + getHeight() / 2;
    }

    @Override
    public void rotateBy(float amountInDegrees) {
        super.rotateBy(amountInDegrees);
        collisionBox.rotate(amountInDegrees);
    }

    public Vector2 getPosition(Vector2 vector2) {
        return vector2.set(getX(), getY());
    }

    public Vector2 getCenter() {
        return carCenter;
    }

    public Polygon getBounds() {
        return collisionBox;
    }

    public float getSpeed() {
        return carSpeed;
    }
}
