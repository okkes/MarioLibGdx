package com.jinbu.mariobros.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * Created by 15049051 on 09/05/2017.
 */
public class Pipe extends ObjectDefiner{
    private final static String LAYER_NAME = "pipe";

    public Pipe(TiledMap map){
        super(map, LAYER_NAME);
    }

    /**
     * The constructor of the object you can use if you called the layer other than the pre set name.
     * @param map the loaded tiled map object.
     * @param newLayerName the new name of the layer.
     */
    public Pipe(TiledMap map, String newLayerName){
        super(map, newLayerName);
    }
}
