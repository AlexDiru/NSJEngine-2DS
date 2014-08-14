import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 23/06/14
 * Time: 23:53
 * To change this template use File | Settings | File Templates.
 */
public class NSJDynamicEntity extends NSJEntity {
    protected float velocityX;
    protected float velocityY;
    protected float accelerationX;
    protected float accelerationY;


    public NSJDynamicEntity(Texture texture, float x, float y, int width, int height, float velocityX, float velocityY, float accelerationX, float accelerationY)  {
        construct(texture, x, y, width, height, velocityX, velocityY, accelerationX, accelerationY);
    }

    public void construct(Texture texture, float x, float y, int width, int height, float velocityX, float velocityY, float accelerationX, float accelerationY) {
        super.construct(texture, x, y, width, height);
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.accelerationX = accelerationX;
        this.accelerationY = accelerationY;
    }

    protected  NSJDynamicEntity() {

    }

    public void update(NSJMap map) {


        if (isDestroyed())
            return;

        if (this instanceof NSJProjectile) {
            ((NSJProjectile)this).updateProjectile(map);
            return;
        }
        velocityX += accelerationX * Gdx.graphics.getDeltaTime();
        velocityY += accelerationY* Gdx.graphics.getDeltaTime();
        x += velocityX* Gdx.graphics.getDeltaTime();
        y += velocityY* Gdx.graphics.getDeltaTime();

    }
}
