package com.jinbu.mariobros.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.jinbu.mariobros.Screens.PlayScreen;

import static com.jinbu.mariobros.MarioBros.PPM;

/**
 * Created by 15049051 on 07/05/2017.
 */
public class Mario extends Sprite{
    private final int NOT_IN_AIR = 0;
    private final float JUMP_VELOCITY = 4f;

    private final float MAX_RUN_VELOCITY_TO_RIGHT = 2f;
    private final float MAX_RUN_VELOCITY_TO_LEFT = -2f;

    private final float MAX_WALK_VELOCITY_TO_RIGHT = 0.8f;
    private final float MAX_WALK_VELOCITY_TO_LEFT = -0.8f;

    private final float MOVEMENT_SPEED_RIGHT = 0.1f;
    private final float MOVEMENT_SPEED_LEFT = -0.1f;

    // the world that mario is going to live in.
    private World world;

    private Body b2body;

    // get the individuel texture of mario standing still
    private TextureRegion marioStand;

    public Mario(World world, PlayScreen screen){
        // make a call to super, which is a sprite class and it can take a textureregion that we can manupulate later.
        super(screen.getAtlas().findRegion("little_mario"));

        this.world = world;
        defineMario();
    }

    private void defineMario(){
        createBody();
        createFeetForBody();
        addFixtureToBody();
    }

    public float getPositionX(){
        return b2body.getPosition().x;
    }

    public float getPositionY(){
        return b2body.getPosition().y;
    }

    public float getLinearVelocityX(){
        return b2body.getLinearVelocity().x;
    }

    public float getLinearVelocityY(){
        return b2body.getLinearVelocity().y;
    }

    private void addFixtureToBody(){
        // the first param is the texture we want to get.
        marioStand = new TextureRegion(getTexture(), 0, 10, 16, 16);

        setBounds(0, 0, 16 / PPM, 16 / PPM);
        setRegion(marioStand);
    }

    private void createFeetForBody(){
        FixtureDef fdef = new FixtureDef();
        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-6 / PPM, -7 / PPM), new Vector2(6 / PPM, -7 / PPM));
        fdef.shape = feet;
        fdef.friction = 0.5f;
        fdef.isSensor = false;
        b2body.createFixture(fdef);
    }

    private void createBody(){
        BodyDef bdef = new BodyDef();
        bdef.position.set(32 / PPM, 32 / PPM); // temp
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        // we need to create a circle shape
//        CircleShape shape = new CircleShape();
        PolygonShape shape2 = new PolygonShape();
        shape2.setAsBox(6 / PPM, 6 / PPM);
//        shape.setRadius(5 / PPM);
        fdef.shape = shape2;
        b2body.createFixture(fdef);
    }

    public void update(float dt){
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
    }

    public void jump() {
        if(b2body.getLinearVelocity().y == NOT_IN_AIR){
            // Do we want to impulse it to x or y direction. you can do that by sending the vector.
            // You can tell the box2d where to apply this inpulse/vorce to the body. By applying directly to the center of the body
            // it won't for example spin.
            // The final parameter is to wake the body up, otherwise it won't move.
            b2body.applyLinearImpulse(new Vector2(0, JUMP_VELOCITY), b2body.getWorldCenter(), true);
        }
    }

    public void moveToRight(boolean isRunning) {
        boolean move = false;
        if(isRunning && b2body.getLinearVelocity().x <= MAX_RUN_VELOCITY_TO_RIGHT){
            move = true;
        }
        else if (b2body.getLinearVelocity().x <= MAX_WALK_VELOCITY_TO_RIGHT){
            move = true;
        }
        System.out.println(b2body.getLinearVelocity().x);
        if(move){
            b2body.applyLinearImpulse(new Vector2(MOVEMENT_SPEED_RIGHT, 0), b2body.getWorldCenter(), true);
        }
    }

    public void moveToLeft(boolean isRunning) {
        boolean move = false;
        if(isRunning && b2body.getLinearVelocity().x >= MAX_RUN_VELOCITY_TO_LEFT){
            move = true;
        }
        else if (b2body.getLinearVelocity().x >= MAX_WALK_VELOCITY_TO_LEFT){
            move = true;
        }

        if(move){
            b2body.applyLinearImpulse(new Vector2(MOVEMENT_SPEED_LEFT, 0), b2body.getWorldCenter(), true);
        }
    }

    public void runToRight(){

    }

    public void runToLeft(){

    }
}
