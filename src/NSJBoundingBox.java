import com.badlogic.gdx.math.Intersector;

import com.badlogic.gdx.math.Rectangle;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 24/06/14
 * Time: 21:12
 * To change this template use File | Settings | File Templates.
 */
public class NSJBoundingBox {

    private NSJVert[] vertices = new NSJVert[4];

    public NSJBoundingBox(NSJVert upperLeft, NSJVert upperRight, NSJVert lowerLeft, NSJVert lowerRight) {
        vertices[0] = upperLeft;
        vertices[1] = upperRight;
        vertices[2] = lowerLeft;
        vertices[3] = lowerRight;
    }

    public Rectangle toRectangle(float offsetX, float offsetY) {
        return new Rectangle(vertices[0].getX() + offsetX, vertices[0].getY() + offsetY, vertices[1].getX() - vertices[0].getX(), vertices[2].getY() - vertices[0].getY());
    }

    public static boolean within(float x, float y, NSJEntity entity) {



        /*float tx = x - entity.getX();
        float ty = y - entity.getY();

        if (tx >= entity.getBoundingBox().vertices[0].getX() &&
            tx <= entity.getBoundingBox().vertices[1].getX() &&
            ty >= entity.getBoundingBox().vertices[0].getY() &&
            ty <= entity.getBoundingBox().vertices[2].getY())
            return true;*/
        return false;
    }

    public static boolean within(NSJEntity entity1, NSJEntity entity2) {
        return false;//return Intersector.overlaps(entity1.getBoundingBox().toRectangle(entity1.getX() ,entity1.getY()), entity2.getBoundingBox().toRectangle(entity2.getX(), entity2.getY() ));
    }
}
