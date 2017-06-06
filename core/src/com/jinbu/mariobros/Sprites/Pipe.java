package com.jinbu.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by 15049051 on 09/05/2017.
 */
public class Pipe extends InteractiveRectangleObject {
    public final static String LAYER_NAME   = "pipes";
    public final static Class<RectangleMapObject> TYPE = RectangleMapObject.class; // TODO: move to super?, make it more dynamic?

    public Pipe (World world, TiledMap map, MapObject object){
        super(world, map, object, 0);
        super.fixture.setUserData(this);
    }

    @Override
    public void sensorCollision() {
        Gdx.app.log(LAYER_NAME, "Collision");
    }
}
