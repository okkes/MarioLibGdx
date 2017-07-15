package com.jinbu.mariobros.Tools;

import com.badlogic.gdx.physics.box2d.*;
import com.jinbu.mariobros.Sprites.*;

/**
 * Created by 15049051 on 05/06/2017.
 */
public class WorldContactListener implements ContactListener{
    @Override
    public void beginContact(Contact contact) {
        InteractiveTileObject objectA = (InteractiveTileObject)contact.getFixtureA().getUserData();
        InteractiveTileObject objectB = (InteractiveTileObject)contact.getFixtureB().getUserData();

        objectA.beginContactCollision(objectB, filterBit(contact));
        objectB.beginContactCollision(objectA, filterBit(contact));
    }

    @Override
    public void endContact(Contact contact) {
        InteractiveTileObject objectA = (InteractiveTileObject)contact.getFixtureA().getUserData();
        InteractiveTileObject objectB = (InteractiveTileObject)contact.getFixtureB().getUserData();

        objectA.endContactCollision(contact, filterBit(contact));
        objectB.endContactCollision(contact, filterBit(contact));
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        InteractiveTileObject objectA = (InteractiveTileObject)contact.getFixtureA().getUserData();
        InteractiveTileObject objectB = (InteractiveTileObject)contact.getFixtureB().getUserData();

        objectA.preSolveCollision(contact, filterBit(contact));
        objectB.preSolveCollision(contact, filterBit(contact));
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        InteractiveTileObject objectA = (InteractiveTileObject)contact.getFixtureA().getUserData();
        InteractiveTileObject objectB = (InteractiveTileObject)contact.getFixtureB().getUserData();

        objectA.postSolveCollision(contact, filterBit(contact));
        objectB.postSolveCollision(contact, filterBit(contact));
    }

    private int filterBit(Contact contact){
        return contact.getFixtureA().getFilterData().categoryBits |
                contact.getFixtureB().getFilterData().categoryBits;
    }
}
