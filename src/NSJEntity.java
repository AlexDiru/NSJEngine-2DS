import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 23/06/14
 * Time: 23:17
 * To change this template use File | Settings | File Templates.
 */
public class NSJEntity {

    protected float x;
    protected float y;
    protected float z;
    protected Texture texture;
    protected boolean canPlayerWalkThrough = true;
    private boolean destroyed = false; //Whether entity needs removing from map

    protected Rectangle boundingBox;
    protected int width;
    protected int height;
    private int layer;

    public NSJEntity(Texture texture, float x, float y) {
        construct(texture, x, y);
    }

    protected NSJEntity() {

    }

    protected void construct(Texture texture, float x, float y, int width, int height)  {
        this.x = x;
        this.y = y;
        this.texture = texture;
        this.width = width;
        this.height = height;


        boundingBox =  new Rectangle(0,0,width,height);
    }

    protected void construct(Texture texture, float x, float y) {
        construct(texture, x, y, texture.getWidth(), texture.getHeight());
    }

    public NSJEntity(Texture texture, float x, float y, int width, int height)  {
        construct(texture, x, y, width, height);
    }

    public void render(SpriteBatch spriteBatch, int offsetX, int offsetY) {


        float scaleRatio = (width*(z+1))/width;

        if (isDestroyed())
            return;

        if (this instanceof NSJPlayer)
            ((NSJPlayer)this).renderCharacter(spriteBatch, 0,0);
        else if (this instanceof NSJCharacter)
            ((NSJCharacter)this).renderCharacter(spriteBatch,offsetX,offsetY);
        else
            spriteBatch.draw(texture, x - offsetX - scaleRatio/2, y - offsetY - scaleRatio/2, 0,0,width, height,z+1,z+1,0,0,0,texture.getWidth(), texture.getHeight(),false,false);
    }

    public void setCanPlayerWalkThrough(boolean canPlayerWalkThrough) {
        this.canPlayerWalkThrough = canPlayerWalkThrough;
    }

    public Rectangle getBoundingBox() {
        boundingBox.setPosition(x,y);
        return boundingBox;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void increaseZ(float v) {
        this.z += v;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public void destroy() {
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public NSJEntity copy(int newXPosition, int newYPosition) {
        NSJEntity clone = new NSJEntity();
        clone.x = newXPosition;
        clone.y = newYPosition;
        clone.texture = texture;
        clone.width = width;
        clone.height = height;
        clone.boundingBox = new Rectangle(clone.x, clone.y, clone.width, clone.height);
        clone.canPlayerWalkThrough = canPlayerWalkThrough;
        clone.destroyed = false;
        return clone;
    }
}
