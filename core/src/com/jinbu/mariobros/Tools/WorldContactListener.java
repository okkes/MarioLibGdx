package com.jinbu.mariobros.Tools;

import com.badlogic.gdx.physics.box2d.*;
import com.jinbu.mariobros.Sprites.*;

/**
 * Created by 15049051 on 05/06/2017.
 */
public class WorldContactListener implements ContactListener{

    @Override
    public void beginContact(Contact contact) {
        // The contactListener event is triggered with two fixtures colliding with each other. It's unknown which fixture is which one
        // So we have to figure that out first.

        int categoryBit = contact.getFixtureA().getFilterData().categoryBits |
                contact.getFixtureB().getFilterData().categoryBits;

        InteractiveTileObject objectA = (InteractiveTileObject)contact.getFixtureA().getUserData();
        InteractiveTileObject objectB = (InteractiveTileObject)contact.getFixtureB().getUserData();

        objectA.collisionOccured(objectB, categoryBit);
        objectB.collisionOccured(objectA, categoryBit);
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // once something is collided, you can change the characteristic of the collision.
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
