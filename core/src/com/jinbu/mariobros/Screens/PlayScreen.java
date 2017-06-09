package com.jinbu.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jinbu.mariobros.Levels.Level1;
import com.jinbu.mariobros.MarioBros;
import com.jinbu.mariobros.Scenes.Hud;
import com.jinbu.mariobros.Sprites.InvisibleWall;
import com.jinbu.mariobros.Sprites.Mario;
import com.jinbu.mariobros.Tools.InputHandler;
import com.jinbu.mariobros.Tools.WorldContactListener;

import static com.jinbu.mariobros.MarioBros.PPM;

/**
 * Created by 15049051 on 29/04/2017.
 */
public class PlayScreen implements Screen {
    private final InputHandler  controller;
    private InvisibleWall wall;

    private MarioBros           game;
    private TextureAtlas        atlas;
    private OrthographicCamera  gameCam;
    private Viewport            gamePort;
    private Hud                 hud;

    // load our map into the game
    private TmxMapLoader maploader;

    // reference to the map itself
    private TiledMap map;

    // This is what renders our map to the screen
    private OrthogonalTiledMapRenderer mapRenderer;

    private World world;

    // graphical representation of the bodies in box2d world to see the boxes.
    private Box2DDebugRenderer b2dr;

    private Mario player;

    public PlayScreen(MarioBros game, InputHandler controller){
        //todo: you may want to look at asset manager in order to manage your textures much better.
        atlas = new TextureAtlas("Mario_and_Enemies.pack");

        // Keep the reference in order to set another screen later on.
        this.game   = game;

        this.controller = controller;

        // Create cam used to follow mario through cam world
        gameCam     = new OrthographicCamera();

        // Create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort    = new FitViewport(MarioBros.V_WIDTH / PPM, MarioBros.V_HEIGHT / PPM, gameCam); // add bars to the screen to keep the ratio.

        // Create our game HUD for scores/timers/level info
        hud         = new Hud(game.batch);

        maploader   = new TmxMapLoader();
        map         = maploader.load("level1.tmx");

        // The renderer needs to be scaled. The second parameter can be used to scale. 1 over pixel per meter.
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1 / PPM);

        // The gamecam's default focus position is the coordinate 0,0 (which is on the left bottom corner.
        // The gameCam will use the 0,0 as centerpoint. If we keep it as the default, we will miss 50% of the view.
        // So in order to show the left bottom corner of the worldmap in the left bottom corner of the screen.
        // We have to get the world with and devide that by 2 + height / 2, the last param is the z axes, which we don't
        // use for the 2d game world.
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // Vector2 is used for gravity and the second parameter (true) makes the object go to sleep
        // Box2d doesnt calculate inside his physics simulation the bodies that are sleeping. this saves some time
        world = new World(new Vector2(0, -11.5f), true);

        b2dr = new Box2DDebugRenderer();

        Level1 level = new Level1(map, world);

        player = new Mario(this.world, this);
        wall = new InvisibleWall(this.world);

        world.setContactListener(new WorldContactListener());
    }

    public TextureAtlas getAtlas(){
        return atlas;
    }

    @Override
    public void show() {
        System.out.println("show");
    }

    @Override
    public void render(float delta) {
        update(delta);

        clearScreen();

        renderMap();
        renderBatch();
        renderHud();
    }

    private void clearScreen(){
        // Set screen color to black
        Gdx.gl.glClearColor(0, 0, 0, 1);

        // Clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void renderMap(){
        // render the maprenderer after clearing the screen.
        mapRenderer.render();

        b2dr.render(world, gameCam.combined);
    }

    private void renderBatch(){
        // main cam when we run around
        game.batch.setProjectionMatrix(gameCam.combined);

        game.batch.begin();
        player.draw(game.batch);
        game.batch.end();
    }

    private void renderHud(){
        // render only what the camera can see rather than everything
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);

        hud.stage.draw();
    }

    private void updateCamera(){
        // Don't follow the player when he is close to the edges of the map.
        // This prevents the camera from showing outside the map.
        float leftEdgeScreenScaled = MarioBros.V_WIDTH / 2 / PPM;
        if(player.getPositionX() <= leftEdgeScreenScaled){
            return;
        }

        //todo: add the right edge as well.

        // Check if the player walks to the left. If so, don't follow
        if(player.getPositionX() <= gameCam.position.x){
            return;
        }

        // update camera.
        gameCam.position.x = player.getPositionX();

        // Always update the camera anytime it moves.
        gameCam.update();
    }

    public void update(float delta){
        handleInput(delta);

        // In order for box2d to execute the simulation, we need to tell him, how many times to calculate per second.
        // velocity and position iterations effects how two bodies react during the collision, the higher the number,
        // the preciser but longer the calculation takes.
        // takes 1 step in the physics simulation (60 times per second)
        world.step(1/60f, 6, 10);

        player.update(delta);

        updateCamera();

        // let the maprenderer know what it needs to render
        mapRenderer.setView(gameCam);
    }

    private void handleInput(float delta){
        // There are two ways to move an object in box2d. you can use force which is gradual increase or decrease in speed
        // Or you could use inpulse, which is an imediate change in speed
        player.jump(controller.jumpIsPressed());

        if(controller.rightIsPressed()){
            player.moveToRight(controller.sprintIsPressed());
        }

        if(controller.leftIsPressed()){
            // Update the wall first to prevent the player from walking out of the screen.
            wall.update(getLeftEdgeOfScreen());

            player.moveToLeft(controller.sprintIsPressed());
        }
    }

    private float getLeftEdgeOfScreen(){
        return gameCam.position.x - (MarioBros.V_WIDTH / 2 / PPM);
    }

    //todo: check if this fixes the android bugs.
    private void inputExample(){
//        if (Gdx.input.isTouched())
//        {
//            int dX = Gdx.input.getDeltaX();
//            int dY = Gdx.input.getDeltaY();
//
//            if (Math.abs(dX)+5 > Math.abs(dY))
//            {
//                if (dX>0 && player.b2body.getLinearVelocity().x <= 2)
//                    controller.rightPressed = true;
//                if (dX<0 && player.b2body.getLinearVelocity().x >= -2)
//                    controller.leftPressed = true;
//            }
//            else
//            {
//                if (player.b2body.getLinearVelocity().y == 0)
//                    controller.upPressed = true;
//            }
//        }
//
//        if(controller.isUpPressed()  && player.b2body.getLinearVelocity().y == 0)
//            player.b2body.applyLinearImpulse(new Vector2(0,4f), player.b2body.getWorldCenter(),true);
//        if (controller.isRightPressed()&& player.b2body.getLinearVelocity().x <= 2)
//            player.b2body.applyLinearImpulse(new Vector2(0.1f,0), player.b2body.getWorldCenter(), true);
//        if (controller.isLeftPressed()&& player.b2body.getLinearVelocity().x>=-2)
//            player.b2body.applyLinearImpulse(new Vector2(-0.1f,0), player.b2body.getWorldCenter(), true);
//
//        controller.leftPressed=false;
//        controller.rightPressed=false;
//        controller.upPressed = false;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {
        System.out.println("pause");
        System.out.println("Making big changes");
    }

    @Override
    public void resume() {
        System.out.println("resume");
    }

    @Override
    public void hide() {
        System.out.println("hide");
    }

    @Override
    public void dispose() {
        System.out.println("dispose");
        map.dispose();
        mapRenderer.dispose();
        world.dispose();
        b2dr.dispose();
    }
}
