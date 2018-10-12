package com.tycrowe.games.citybus.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CityBus extends Actor {

    private Texture cityBusTexture;
    private Vector2 position;
    private Polygon collisionBox;
    private float busSpeed = 10f;

    public CityBus(String name, Texture cityBusTexture, float x, float y) {
        setName(name);
        this.cityBusTexture = cityBusTexture;
        setSize(cityBusTexture.getWidth(), cityBusTexture.getHeight());
        setPosition(x, y);
        this.position = new Vector2(getX(), getY());
        collisionBox = new Polygon(new float[] {
                getX(), getY(),
                getX() + getWidth(), getY(),
                getX() + getWidth(), getY() + getHeight(),
                getX(), getY() + getHeight()
        });
        collisionBox.setOrigin(getX() + getWidth() / 2, getY() + getHeight() / 2);
        collisionBox.rotate(90);
        setRotation(90);
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
        // SPEED UP FOOL
        setX(getX() - busSpeed);
        setBounds(getX(), getY(), getWidth(), getHeight());
        setOrigin(getWidth() / 2, getHeight() / 2);
    }

    public Vector2 getBusCenter() {
        return new Vector2(getX() + getWidth() / 2, getY() + getHeight() / 2);
    }

}