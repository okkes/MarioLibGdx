package com.jinbu.mariobros.Tools;

import com.badlogic.gdx.physics.box2d.*;
import com.jinbu.mariobros.Sprites.*;

/**
 * Created by 15049051 on 05/06/2017.
 */
public class WorldContactListener implements ContactListener{
    @Override
    public void beginContact(Contact contact) {
        int categoryBit = contact.getFixtureA().getFilterData().categoryBits |
                contact.getFixtureB().getFilterData().categoryBits;

        InteractiveTileObject objectA = (InteractiveTileObject)contact.getFixtureA().getUserData();
        InteractiveTileObject objectB = (InteractiveTileObject)contact.getFixtureB().getUserData();

        objectA.beginContactCollision(objectB, categoryBit);
        objectB.beginContactCollision(objectA, categoryBit);
    }

    @Override
    public void endContact(Contact contact) {
        InteractiveTileObject objectA = (InteractiveTileObject)contact.getFixtureA().getUserData();
        InteractiveTileObject objectB = (InteractiveTileObject)contact.getFixtureB().getUserData();

        objectA.endContactCollision(contact);
        objectB.endContactCollision(contact);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        InteractiveTileObject objectA = (InteractiveTileObject)contact.getFixtureA().getUserData();
        InteractiveTileObject objectB = (InteractiveTileObject)contact.getFixtureB().getUserData();

        objectA.preSolveCollision(contact);
        objectB.preSolveCollision(contact);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        InteractiveTileObject objectA = (InteractiveTileObject)contact.getFixtureA().getUserData();
        InteractiveTileObject objectB = (InteractiveTileObject)contact.getFixtureB().getUserData();

        objectA.postSolveCollision(contact);
        objectB.postSolveCollision(contact);
    }
}
