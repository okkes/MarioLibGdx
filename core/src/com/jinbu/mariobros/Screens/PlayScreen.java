package com.jinbu.mariobros.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.jinbu.mariobros.MarioBros;
import com.jinbu.mariobros.Scenes.Hud;

/**
 * Created by 15049051 on 29/04/2017.
 */
public class PlayScreen implements Screen {
    private MarioBros           game;
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

    public PlayScreen(MarioBros game){
        // Keep the reference in order to set another screen later on.
        this.game   = game;

        // Create cam used to follow mario through cam world
        gameCam     = new OrthographicCamera();

        // Create a FitViewport to maintain virtual aspect ratio despite screen size
        gamePort    = new FitViewport(MarioBros.V_WIDTH, MarioBros.V_HEIGHT, gameCam); // add bars to the screen to keep the ratio.

        // Create our game HUD for scores/timers/levle info
        hud         = new Hud(game.batch);

        maploader   = new TmxMapLoader();
        map         = maploader.load("level1.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(map);

        // The gamecam's default focus position is the coordinate 0,0 (which is on the left bottom corner.
        // The gameCam will use the 0,0 as centerpoint. If we keep it as the default, we will miss 50% of the view.
        // So in order to show the left bottom corner of the worldmap in the left bottom corner of the screen.
        // We have to get the world with and devide that by 2 + height / 2, the last param is the z axes, which we don't
        // use for the 2d game world.
        gameCam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        // Vector2 is used for gravity and the second parameter (true) makes the object go to sleep
        // Box2d doesnt calculate inside his physics simulation the bodies that are sleeping. this saves some time
        world = new World(new Vector2(0, 0), true);

        b2dr = new Box2DDebugRenderer();

        // before we can actually create the body, we need to define what the body consist of.
        BodyDef bdef = new BodyDef();

        // polygon shape for the fixture
        PolygonShape shape = new PolygonShape();

        // before you can create the fixture, you have to define the fixture first and then add it to the boyd and then you add the body the world.
        FixtureDef fdef = new FixtureDef();

        // the body itself
        Body body;

        // To create a body and fixture for each body in the loaded tiled map layers, we use the following code.
        for(MapObject object: map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);

            // add body the world
            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fdef.shape = shape;
            //add fixture to the body
            body.createFixture(fdef);
        }

        // To create a body and fixture for each body in the loaded tiled map layers, we use the following code.
        for(MapObject object: map.getLayers().get(2).getObjects().getByType(PolygonMapObject.class)){
            float vertices[] = ((PolygonMapObject) object).getPolygon().getTransformedVertices();
            for(int x = 0; x < vertices.length; x++){
//                vertices[x] /= 10;
            }
            ChainShape shape2 = new ChainShape();
            shape2.createChain(vertices);
            bdef.position.set(0, 0);

            // add body the world
            body = world.createBody(bdef);
            fdef.shape = shape2;
            //add fixture to the body
            body.createFixture(fdef);
        }

        // To create a body and fixture for each body in the loaded tiled map layers, we use the following code.
        for(MapObject object: map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);

            // add body the world
            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fdef.shape = shape;
            //add fixture to the body
            body.createFixture(fdef);
        }

        // To create a body and fixture for each body in the loaded tiled map layers, we use the following code.
        for(MapObject object: map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set(rect.getX() + rect.getWidth() / 2, rect.getY() + rect.getHeight() / 2);

            // add body the world
            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2, rect.getHeight() / 2);
            fdef.shape = shape;
            //add fixture to the body
            body.createFixture(fdef);
        }
    }

    @Override
    public void show() {
        System.out.println("show");
    }

    @Override
    public void render(float delta) {
        update(delta);

        // Set screen color to black
        Gdx.gl.glClearColor(0, 0, 0, 1);

        // Clear screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // render the maprenderer after clearing the screen.
        mapRenderer.render();

        b2dr.render(world, gameCam.combined);

        // render only what the camera can see rather than everything
        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);

        hud.stage.draw();
    }

    public void update(float delta){
        handleInput(delta);

        // Always update the camera anytime it moves.
        gameCam.update();

        // let the maprenderer know what it needs to render
        mapRenderer.setView(gameCam);
    }

    private void handleInput(float delta){
        if(Gdx.input.isTouched()){
            gameCam.position.x += 100 * delta; // temp
        }
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
    }
}
