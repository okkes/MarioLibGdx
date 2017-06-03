package com.jinbu.mariobros.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by 15049051 on 14/05/2017.
 */
public class Stair extends InteractivePolygonObject{
    public final static String LAYER_NAME   = "stairs";
    public final static Class<PolygonMapObject> TYPE = PolygonMapObject.class; // TODO: move to super?, make it more dynamic?

    public Stair (World world, TiledMap map, MapObject object){
        super(world, map, object, 0);
    }
}
