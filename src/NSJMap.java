import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

import java.util.HashMap;

public class NSJMap {

    //Maps layer to objects on that layer
    public HashMap<Integer, List<NSJEntity>> layerMap = new HashMap<Integer, List<NSJEntity>>();

    public List<NSJProjectile> projectiles = new ArrayList<NSJProjectile>();
    private NSJEntity player;



    private void layerFromTileMap(int layerNum, String tileMap, NSJEntity[] entityEncoding, int tileSize) {
        String[] rows = tileMap.split("\n");

        int y = 0;
        int x = 0;

        for (String row : rows) {
            String[] cols = row.split(",");
            for (String col : cols) {
                if (!col.equals(" ")) {
                    int id = Integer.parseInt(col);
                    addEntity(layerNum, entityEncoding[id].copy(x,y));
                }
                x += tileSize;
            }
            x = 0;
            y += tileSize;
        }
    }


    public NSJMap(NSJEntity player) {
        Texture floor = new Texture("assets/floor.png");
        Texture wall = new Texture("assets/wall.png");
        //Texture me = new Texture("assets/ship.png");
       // Texture marcus = new Texture("assets/bullet.png");
        //Texture churly = new Texture("assets/enemybullet.png");
        Texture water = new Texture("assets/water.png");

        NSJEntity floorE = new NSJEntity(floor, 0,0, 32, 32);
        NSJEntity wallE = new NSJEntity(wall,0,0,32,32);
        wallE.setCanPlayerWalkThrough(false);

        this.player = player;


        /*for (int x = 0; x < 32 * 10; x += 32)
            for (int y = 0; y < 32 * 10; y += 32)
                addEntity(0, new NSJEntity(floor, x, y, 32, 32));

        for (int x = 0; x < 32 * 10; x += 32)
            for (int y = 0; y < 32 * 10; y += 32)
                if (x == 0 || y == 0 || x == 32 * 9 || y == 32 * 9) {
                    NSJEntity e = new NSJEntity(wall, x, y,32,32);
                    e.setCanPlayerWalkThrough(false);
                    addEntity(1, e);
                }*/

        layerFromTileMap(0,
                " , ,1,1,1,1,1,1,1, , \n" +
                " ,1,0,0,0,0,0,0,0,1, \n" +
                " ,1,0,0,0,0,0,0,0,1, \n" +
                "1,0,0,0,0,0,0,0,0,0,1\n" +
                "1,0,0,0,0,0,0,0,0,0,1\n" +
                "1,0,0,0,0,0,0,0,0,0,1\n" +
                "1,0,0,0,0,0,0,0,0,0,1\n" +
                "1,0,0,0,0,0,0,0,0,0,1\n" +
                "1,0,0,0,0,0,0,0,0,0,1\n" +
                "1,0,0,0,0,0,0,0,0,0,1\n" +
                "1,0,0,0,0,0,0,0,0,0,1\n" +
                "1,0,0,0,0,0,0,0,0,0,1\n" +
                " ,1,0,0,0,0,0,0,0,1, \n" +
                " ,1,0,0,0,0,0,0,0,1, \n" +
                " , ,1,1,1,0,1,1,1,1,1,1,1, \n" +
                " , ,1,0,1,0,0,0,0,0,0,0,0,1\n" +
                " ,1,0,0,0,1,1,1,1,1,1,1,0,1\n" +
                " ,1,0,0,0,0,0,0,0,0,0,1,0,1\n" +
                " , ,1,0,0,0,1,0,0,0,1,0,0,1\n" +
                " , , ,1,0,0,0,1,0,0,1,0,0,0,1\n" +
                " , , , ,1,0,0,0,0,1,0,0,0,0,0,1\n"+
                " , , , ,1,0,0,0,0,1,0,0,0,0,0,1\n"+
                " , , , , ,1,0,0,0,1,0,0,0,0,0,1\n"+
                " , , , , , ,1,0,0,1,0,0,0,0,0,1\n" +
                " , , , , , , ,1,0,0,0,0,0,0,0,1\n" +
                " , , , , , , , ,1,1,1,1,1,1,1, \n",
                new NSJEntity[] { floorE, wallE },32);


        List<NSJVert> verts = new ArrayList<NSJVert>();
        verts.add(new NSJVert(30,10));
        verts.add(new NSJVert(110,10));
        verts.add(new NSJVert(130,30));
        verts.add(new NSJVert(130,110));
        verts.add(new NSJVert(110,130));
        verts.add(new NSJVert(30,130));
        verts.add(new NSJVert(10,110));
        verts.add(new NSJVert(10,30));
        NSJPolygon poly = new NSJPolygon(water, 150,150, verts);
        addEntity(1, poly);

    }

    public void addEntity(int layerNum, NSJEntity entity) {
        //Add to layer map
        if (layerMap.get(layerNum) == null)
            layerMap.put(layerNum, new ArrayList<NSJEntity>());
        layerMap.get(layerNum).add(entity);


        entity.setLayer(layerNum);
    }

    public void update() {
        List<NSJEntity> toRemove = new ArrayList<NSJEntity>();

        for (int layer : layerMap.keySet()) {
            for (int i = 0; i <  layerMap.get(layer).size(); i++) {
                NSJEntity entity = layerMap.get(layer).get(i);

                if (layerMap.get(layer).get(i) instanceof NSJDynamicEntity) {
                    NSJDynamicEntity dynamicEntity = (NSJDynamicEntity)entity;
                    dynamicEntity.update(this);
                }

                if (entity instanceof NSJCharacter || entity instanceof NSJAI) {
                    ((NSJCharacter)entity).update(this);
                }

                if (entity.isDestroyed())
                    toRemove.add(entity);
            }
        }

        for (NSJEntity rem : toRemove) {
            destroyEntity(rem);
        }
    }

    public void render(SpriteBatch spriteBatch, int offsetX, int offsetY) {
        for (int layer : layerMap.keySet()) {
            for (NSJEntity entity : layerMap.get(layer)) {
                if (entity instanceof NSJPlayer)
                    ((NSJPlayer)entity).render(spriteBatch);
                else
                    entity.render(spriteBatch, offsetX, offsetY);
            }
        }
    }

    public List<NSJEntity> getEntitiesAtPosition(Rectangle boundingBox, float curX, float curY) {
/*
        List<NSJEntity> entities = subtree.get(NSJHash.hashEntityPosition(curX,curY));
        List<NSJEntity> entitiess = new ArrayList<NSJEntity>();


        for (NSJEntity entity : entities) {
            if (NSJBoundingBox.within(curX, curY, entity)) {
                entitiess.add(entity);
            }
        }


        return entitiess;
        */


        //TODO speed up using octrees or something
        List<NSJEntity> entities = new ArrayList<NSJEntity>();


        for (int layer : layerMap.keySet()) {
            for (NSJEntity entity : layerMap.get(layer)) {
               if (boundingBox == null && entity.getBoundingBox().contains(curX,curY)) {
                    entities.add(entity);
                } else if (boundingBox != null && entity.getBoundingBox().overlaps(boundingBox))
                   entities.add(entity);
            }
        }

        return entities;


    }

    public List<NSJEntity> getEntitiesAtPosition(NSJEntity entityA) {
/*
        List<NSJEntity> entities = subtree.get(NSJHash.hashEntityPosition(curX,curY));
        List<NSJEntity> entitiess = new ArrayList<NSJEntity>();


        for (NSJEntity entity : entities) {
            if (NSJBoundingBox.within(curX, curY, entity)) {
                entitiess.add(entity);
            }
        }


        return entitiess;
        */


        //TODO speed up using octrees or something
        List<NSJEntity> entities = new ArrayList<NSJEntity>();


        for (int layer : layerMap.keySet()) {
            for (NSJEntity entity : layerMap.get(layer)) {
                if (entity.getBoundingBox().overlaps(entityA.getBoundingBox())) {
                    entities.add(entity);
                }
            }
        }

        return entities;


    }

    public void destroyEntity(NSJEntity entity) {
        //Remove from layer map
        layerMap.get(entity.getLayer()).remove(entity);

        if (entity instanceof NSJProjectile)
            projectiles.remove(entity);

    }

    public NSJEntity getPlayer() {
        return player;
    }

    public void increaseEntityZ(int v) {
        for (NSJEntity entity : layerMap.get(0))
            entity.increaseZ(v);
    }
}
