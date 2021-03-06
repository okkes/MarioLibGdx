package com.jinbu.mariobros.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.Contact;
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
        setcategoryfilter(COIN_BIT);
    }

    @Override
    public void beginContactCollision(Object object, int filterBit) {
        Gdx.app.log(LAYER_NAME, "Collision");
    }

    @Override
    public void endContactCollision(Contact contact, int filterBit) {

    }

    @Override
    public void preSolveCollision(Contact contact, int filterBit) {

    }

    @Override
    public void postSolveCollision(Contact contact, int filterBit) {

    }
}
