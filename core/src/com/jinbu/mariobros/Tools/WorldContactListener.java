package com.jinbu.mariobros.Tools;

import com.badlogic.gdx.physics.box2d.*;
import com.jinbu.mariobros.Sprites.*;

import static com.jinbu.mariobros.MarioBros.MARIO_FEET_BIT;

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

        objectA.collisionOccured(objectB, categoryBit);
        objectB.collisionOccured(objectA, categoryBit);
    }

    @Override
    public void endContact(Contact contact) {
        Mario mario = null;
        Fixture marioFixture = null;
        if(contact.getFixtureA().getUserData() instanceof Mario){
            mario = (Mario) contact.getFixtureA().getUserData();
            marioFixture = contact.getFixtureA();
        }else if(contact.getFixtureB().getUserData() instanceof Mario){
            mario = (Mario) contact.getFixtureB().getUserData();
            marioFixture = contact.getFixtureB();
        }

        if(mario != null && marioFixture != null){
            if(mario.state == Mario.STATE.JUMPING){
                mario.noFriction = true;
            }
            if(mario.noFriction){
                System.out.print("OFF. friction: "+ marioFixture.getFriction());
                System.out.println(", state: " + mario.state);
                marioFixture.setFriction(0);
            }
        }
    }

    private int count = 0;

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Mario mario = null;
        Fixture marioFixture = null;
        if(contact.getFixtureA().getUserData() instanceof Mario){
            mario = (Mario) contact.getFixtureA().getUserData();
            marioFixture = contact.getFixtureA();
        }else if(contact.getFixtureB().getUserData() instanceof Mario){
            mario = (Mario) contact.getFixtureB().getUserData();
            marioFixture = contact.getFixtureB();
        }

        if(mario != null && marioFixture != null){
            if(count >= 15){
                mario.noFriction = false;
                count = 0;
            }
            if(!mario.noFriction){
                System.out.print("ONN. friction: "+ marioFixture.getFriction());
                System.out.println(", state: " + mario.state);
                marioFixture.setFriction(0.5f);
                contact.resetFriction();
            }
            if(mario.state == Mario.STATE.STANDING || mario.state == Mario.STATE.WALKING) count++;
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
