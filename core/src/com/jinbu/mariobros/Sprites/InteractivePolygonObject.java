package com.jinbu.mariobros.Sprites;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ShortArray;

import java.util.ArrayList;
import java.util.Arrays;

import static com.jinbu.mariobros.MarioBros.PPM;

/**
 * Created by 15049051 on 14/05/2017.
 */
public abstract class InteractivePolygonObject implements InteractiveTileObject {
    protected World         world;
    protected TiledMap      map;
    protected TiledMapTile  tile;
    protected float         vertices[];
    protected Body          bodies[];
    protected Fixture[]     fixture;

    public InteractivePolygonObject(World world, TiledMap map, MapObject object, float friction){
        // todo: this is where i found the code / solution to load convex polygon shapes.
        // https://stackoverflow.com/questions/28318167/triangulation-messing-up-triangles

        this.map        = map;
        this.world      = world;
        this.vertices   = ((PolygonMapObject) object).getPolygon().getTransformedVertices();

        EarClippingTriangulator ect         = new EarClippingTriangulator();
        ArrayList<PolygonShape> triangles   = new ArrayList();
        ShortArray indices;

        // scale the vertices
        for (int x = 0; x < vertices.length; x++) {
            vertices[x] /= PPM;
        }
        indices          = ect.computeTriangles(vertices);

        for(int i = 0; i < indices.size; i+= 3){
            int v1 = indices.get(i) * 2;
            int v2 = indices.get(i + 1) * 2;
            int v3 = indices.get(i + 2) * 2;

            PolygonShape triangleShape = new PolygonShape();
            triangleShape.set(new float[]{
                    vertices[v1 + 0],
                    vertices[v1 + 1],
                    vertices[v2 + 0],
                    vertices[v2 + 1],
                    vertices[v3 + 0],
                    vertices[v3 + 1]
            });
            triangles.add(triangleShape);
        }

        this.bodies  = new Body[triangles.size()];
        this.fixture = new Fixture[triangles.size()];

        for(int i = 0; i < triangles.size(); i++){
            BodyDef bdef    = new BodyDef();
            bdef.type       = BodyDef.BodyType.StaticBody;

            FixtureDef fdef = new FixtureDef();
            fdef.friction   = friction == 0? fdef.friction : friction;
            fdef.shape      = triangles.get(i);

            bodies[i]       =  world.createBody(bdef);
            fixture[i]      = bodies[i].createFixture(fdef);
        }
    }
}
