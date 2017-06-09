package com.jinbu.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by 15049051 on 14/05/2017.
 */
public class Ground extends InteractiveRectangleObject {
    public final static String LAYER_NAME = "ground";
    public final static Class<RectangleMapObject> TYPE = RectangleMapObject.class; // TODO: move to super?, make it more dynamic?

    public Ground(World world, TiledMap map, MapObject object) {
        super(world, map, object, 0);
        super.fixture.setUserData(this);
    }

    @Override
    public void collisionOccured(Object object, int filterBit) {
//        Gdx.app.log(LAYER_NAME, "Collision");
    }
}
