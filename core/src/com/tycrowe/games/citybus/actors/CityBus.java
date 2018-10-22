package com.tycrowe.games.citybus.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CityBus extends Actor {

    public enum BUS_ACTION {
        INTERCEPT,
        PASSIVE_DRIVEBY
    }

    private Texture cityBusTexture;
    private Polygon collisionBox;
    private float busSpeed = 10f;
    private BUS_ACTION action;
    private Vector2 busCenter;

    public CityBus(String name, Texture cityBusTexture, float x, float y, BUS_ACTION action) {
        this.cityBusTexture = cityBusTexture;
        setName(name);
        setSize(cityBusTexture.getWidth(), cityBusTexture.getHeight());
        setPosition(x, y);
        this.collisionBox = new Polygon(new float[] {
                getX(), getY(),
                getX() + getWidth(), getY(),
                getX() + getWidth(), getY() + getHeight(),
                getX(), getY() + getHeight()
        });
        this.busCenter = new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
        this.action = action;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(
                cityBusTexture,
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
    public void drawDebug(ShapeRenderer shapes) {
        shapes.setColor(Color.RED);
        shapes.polygon(collisionBox.getTransformedVertices());

    }

    @Override
    public void act(float delta) {
        switch (action) {
            case INTERCEPT:
                // SPEED UP FOOL
                setX(getX() - busSpeed);
                break;
            case PASSIVE_DRIVEBY:
                break;
        }
        collisionBox.setPosition(getX(), getY());
        setOrigin(getWidth() / 2, getHeight() / 2);
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
        return busCenter;
    }

    public Polygon getBounds() {
        return collisionBox;
    }

    public BUS_ACTION getAction() {
        return action;
    }
}