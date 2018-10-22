package com.tycrowe.games.citybus.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.tycrowe.games.citybus.CityBusGame;
import com.tycrowe.games.citybus.actors.CityBus;
import com.tycrowe.games.citybus.actors.PlayerCar;
import com.tycrowe.games.citybus.screens.HighwayLevel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;

public class GameDirector extends LinkedList<GameDirector.Road> {
    private AssetManager am;
    private Stage gameStage;
    private Stage uiStage;
    private PlayerCar playerCar;
    private ArrayList<CityBus> buses;
    private HighwayLevel level;

    private Texture cityBusTexture;

    private boolean attemptedDanger = false;
    private boolean dangerQueued = false;
    private boolean busActive = false;

    public GameDirector(AssetManager am, HighwayLevel level, Stage gameStage, Stage uiStage, PlayerCar playerCar) {
        this.am = am;
        this.level = level;
        this.gameStage = gameStage;
        this.uiStage = uiStage;
        this.playerCar = playerCar;
        this.buses = new ArrayList<>();
        this.cityBusTexture = am.get("bus_01.png");
        this.buildUI();
    }

    private void buildUI() {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = new BitmapFont();
        Label label = new Label("This is a test", labelStyle);
        label.setPosition(0, 0);
        uiStage.addActor(label);
    }

    class Road extends Texture {
        private Vector2 pos;
        private Rectangle box;
        private boolean isIntersection;

        Road(FileHandle roadType, float x, float y, boolean isIntersection) {
            super(roadType);
            pos = new Vector2(x, y);
            this.isIntersection = isIntersection;
            this.box = new Rectangle(x, y, getWidth(), getHeight());
        }

        float getX() {
            return pos.x;
        }

        float getY() {
            return pos.y;
        }

        Vector2 getPosition() {
            return pos;
        }

    }

    public void addFirstHelper(float x, float y) {
        boolean isIntersection = MathUtils.randomBoolean(.45f);
        addFirst(new Road(isIntersection ?
                am.getFileHandleResolver().resolve("street_02.png") : am.getFileHandleResolver().resolve("street_01.png"),
                x, y,
                isIntersection)
        );
        if(isIntersection && !busActive)
            attemptedDanger = false;
    }

    private void busSpawnEvent(Vector2 target, CityBus.BUS_ACTION bus_action) {
        CityBus bus;
        Gdx.app.log("EVENT", "Bus spawned! Action: " + bus_action.name());
        switch (bus_action) {
            case INTERCEPT:
                bus = new CityBus(
                        "bus_" + UUID.randomUUID(),
                        cityBusTexture,
                        target.x + (cityBusTexture.getWidth() * 8),
                        target.y - cityBusTexture.getWidth(),
                        bus_action
                );
                bus.rotateBy(90);
                gameStage.addActor(bus);
                buses.add(bus);
                break;
            case PASSIVE_DRIVEBY:
                bus = new CityBus(
                        "bus_" + UUID.randomUUID(),
                        cityBusTexture,
                        target.x - cityBusTexture.getWidth(),
                        playerCar.getY() + 500,
                        bus_action
                );
                bus.rotateBy(180);
                gameStage.addActor(bus);
                buses.add(bus);
                break;
        }
    }

    private void updateBuses(Vector2 point, float distanceCheck) {
        Iterator<CityBus> cityBusIterable = buses.iterator();
        while(cityBusIterable.hasNext()) {
            CityBus bus = cityBusIterable.next();
            if(Intersector.overlapConvexPolygons(playerCar.getBounds(), bus.getBounds())) {
                Gdx.app.log(CityBusGame.LOG_TAGS.NORMAL.name(), "Collision detected!");
            }
            if(bus.getAction() == CityBus.BUS_ACTION.INTERCEPT) {
                if(point.dst(bus.getCenter()) > distanceCheck) {
                    Gdx.app.log(CityBusGame.LOG_TAGS.NORMAL.name(), "Distance greater than distanceCheck parameter, de-spawning bus.");
                    bus.remove();
                    cityBusIterable.remove();
                }
            }
            if (bus.getAction().equals(CityBus.BUS_ACTION.PASSIVE_DRIVEBY)) {
                System.out.println(bus.getX() + " " + bus.getY());
            }
        }
        if (buses.isEmpty()) {
            busActive = false;
            dangerQueued = false;
        }
    }

    public void update(Batch batch) {
        if(!isEmpty() || playerCar == null) {
            Road lastRoad = getLast();
            Road firstRoad = getFirst();
            // Draw the road(s)
            batch.draw(lastRoad, lastRoad.getX(), lastRoad.getY());
            batch.draw(firstRoad, firstRoad.getX(), firstRoad.getY());
            // If distance of car is more than the height of the road's texture plus half the road's texture, pop and add.
            float distance = playerCar.getPosition(new Vector2()).dst(lastRoad.getPosition());
            if (distance > (lastRoad.getHeight() / 2f) && size() != 2) {
                addFirstHelper(lastRoad.pos.x, lastRoad.pos.y + lastRoad.getHeight());
            }
            if (distance > (lastRoad.getHeight() + (lastRoad.getHeight() / 2f))) {
                removeLast();
            }
            // Is the player in an intersection image?
            if(!busActive) {
                if(firstRoad.isIntersection && !attemptedDanger) {
                    attemptedDanger = true;
                    dangerQueued = MathUtils.randomBoolean(1f);
                } else if(dangerQueued) {
                    // Check if the player's car is close to the center of the road (intersection)
                    if(firstRoad.box.getCenter(new Vector2()).sub(playerCar.getCenter()).y < 0) {
                        busSpawnEvent(firstRoad.box.getCenter(new Vector2()), CityBus.BUS_ACTION.INTERCEPT); // BUS SUMMON
                        busActive = true;
                    }
                }
            } else {
                // BUS ACTIVE LOGIC HERE
                updateBuses(firstRoad.box.getCenter(new Vector2()), 1200);
            }
            if(MathUtils.randomBoolean(.005f)) {
                busSpawnEvent(level.leftLane, CityBus.BUS_ACTION.PASSIVE_DRIVEBY);
            }
        }
    }

}
