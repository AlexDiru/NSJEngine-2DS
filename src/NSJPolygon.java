import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;

import java.util.List;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 24/06/14
 * Time: 00:53
 * To change this template use File | Settings | File Templates.
 */
public class NSJPolygon extends NSJEntity {

    private float[] vertices;
    private float polygonWidth;
    private float polygonHeight;

    public NSJPolygon(Texture texture, int x, int y, List<NSJVert> vertices) {
        this(texture, x, y, texture.getWidth(), texture.getHeight(), vertices);
    }

    public NSJPolygon(Texture texture, int x, int y, int width, int height, List<NSJVert> vertices) {
        super(texture, x, y, width, height);

        this.vertices = new float[vertices.size() * 2];
        for (int i = 0; i < vertices.size(); i++) {
            this.vertices[i * 2] = vertices.get(i).getX();
            this.vertices[(i * 2) + 1] = vertices.get(i).getY();
        }

        //Calculate height and width of polygon
        float maxX = 0;
        float maxY = 0;
        for (NSJVert vertex : vertices) {
            if (vertex.getX() > maxX)
                maxX = vertex.getX();
            if (vertex.getY() > maxY)
                maxY = vertex.getY();
        }
        polygonWidth = maxX;
        polygonHeight = maxY;

        //TODO find min coords to speed up rendering
    }

    public void render(SpriteBatch spriteBatch, int offsetX, int offsetY) {
        for (int y = 0; y < polygonHeight; y++)
            for (int x = 0; x < polygonWidth; x++) {
                if (Intersector.isPointInPolygon(vertices,0,vertices.length, x,y))
                    //TODO change modulo image size to account for scale.. urgh
                    //TODO Have to store a scaled texture because scaling each loop will be VERY TIME CONSUMING
                    spriteBatch.draw(texture, this.x + x - offsetX,this.y +  y - offsetY, 0,0,1, 1,1,1,0,x%texture.getWidth(),y%texture.getHeight(),1,1,false,false);

//                public void draw(com.badlogic.gdx.graphics.Texture texture, float x, float y, float originX, float originY, float width, float height, float scaleX, float scaleY, float rotation, int srcX, int srcY, int srcWidth, int srcHeight, boolean flipX, boolean flipY) { /* compiled code */ }


            }
    }
}
