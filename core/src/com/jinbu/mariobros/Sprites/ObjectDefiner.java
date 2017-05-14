package com.jinbu.mariobros.Sprites;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.BodyDef;

/**
 * Created by 15049051 on 08/05/2017.
 */
public abstract class ObjectDefiner {
    private String layerName;
//    private Class<T> typeName;

    public ObjectDefiner(TiledMap map, String layerName){
        this.layerName = layerName;
    }

    public ObjectDefiner(TiledMap map, int layerNumber) {
//         To create a body and fixture for each body in the loaded tiled map layers, we use the following code.

//        for (MapObject object : map.getLayers().get(layerNumber).getObjects().getByType(RectangleMapObject.class)) {
//            Rectangle rect = ((RectangleMapObject) object).getRectangle();
//
//            bdef.type = BodyDef.BodyType.StaticBody;
//            bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);
//
//            // add body the world
//            body = world.createBody(bdef);
//
//            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
//            fdef.shape = shape;
//            //add fixture to the body
//            body.createFixture(fdef);
//        }
    }

    public MapObject getMapLayer(TiledMap map){
//        return map.getLayers().get(layerName).getObjects().get;
        return null;
    }


}
