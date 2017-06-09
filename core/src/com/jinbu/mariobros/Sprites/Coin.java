package com.jinbu.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;

import static com.jinbu.mariobros.MarioBros.COIN_BIT;

/**
 * Created by 15049051 on 14/05/2017.
 */
public class Coin extends InteractiveRectangleObject {
    public final static String LAYER_NAME   = "coins";
    public final static Class<RectangleMapObject> TYPE = RectangleMapObject.class; // TODO: move to super?, make it more dynamic?

    public Coin (World world, TiledMap map, MapObject object){
        super(world, map, object, 0);
        super.fixture.setUserData(this);
        setCategoryFIlter(COIN_BIT);
    }

    @Override
    public void collisionOccured(Object object, int filterBit) {
        Gdx.app.log(LAYER_NAME, "Collision");
    }
}
