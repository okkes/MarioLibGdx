package com.jinbu.mariobros.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.*;
import com.jinbu.mariobros.Sprites.InteractiveRectangleObject;
import com.jinbu.mariobros.Sprites.InteractiveTileObject;

/**
 * Created by 15049051 on 05/06/2017.
 */
public class WorldContactListener implements ContactListener{
    private boolean headHitDetected = false;

    @Override
    public void beginContact(Contact contact) {
        // The contactListener event is triggered with two fixtures colliding with each other. It's unknown which fixture is which one
        // So we have to figure that out first.
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if(fixA.getUserData() == "feet" || fixB.getUserData() == "feet"){
            System.out.println("landed on my feet");
        }

        // Check if the fixture is mario's head
        if(fixA.getUserData() == "head" || fixB.getUserData() == "head"){
            if(headHitDetected){
                headHitDetected = false;
                return;
            }
            headHitDetected = true;

            // collision occured between mario's head and another object
            Fixture head = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = head == fixA? fixB : fixA;

            // let's see what the object is
            if(object.getUserData() instanceof InteractiveTileObject){
                ((InteractiveTileObject)object.getUserData()).sensorCollision();
            }
        }
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
