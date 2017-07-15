package com.jinbu.mariobros.Sprites;

import com.badlogic.gdx.physics.box2d.Contact;

/**
 * Created by 15049051 on 05/06/2017.
 */
public interface InteractiveTileObject {
    void beginContactCollision(Object object, int filterBit);
    void endContactCollision(Contact contact, int filterBit);
    void preSolveCollision(Contact contact, int filterBit);
    void postSolveCollision(Contact contact, int filterBit);
}
