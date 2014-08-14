import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.List;
import java.awt.Point;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 26/06/14
 * Time: 17:47
 * To change this template use File | Settings | File Templates.
 */
public class NSJProjectile extends NSJDynamicEntity {

    public static final int Fireball = 0;
    private static final Texture FireballTexture = new Texture("assets/projectile.png");
    private static final float FireballSpeed = 200f;
    private static final boolean FireballRicochet = false;
    private static final int FireballDamage = 10;
    private static final int FireballWidth = 16;
    private static final int FireballHeight = 16;

    private Random random = new Random();
    private boolean ricochet;
    private int damage;
    private boolean ricochetFlag = false; //So projectile can't richochet twice off the same object (immediately)

    private NSJEntity creator;
    private boolean avoidCreatorInitiallyFlag = true;


    public NSJProjectile(int id, float x, float y, float targetX, float targetY, NSJEntity creator) {
        if (id == Fireball) {
            NSJPair<Float, Float> speeds = NSJProjectile.computeSpeeds(x,y,targetX, targetY, FireballSpeed);
            construct(FireballTexture, x, y, FireballWidth, FireballHeight, speeds.getLeft(),speeds.getRight(), 0,0, FireballRicochet, FireballDamage, creator);
        }
    }

    public void construct(Texture texture, float x, float y, int width, int height, float velocityX, float velocityY, float accelerationX, float accelerationY, boolean ricochet, int damage, NSJEntity creator) {
        super.construct(texture, x, y, width, height, velocityX, velocityY, accelerationX, accelerationY);
        this.ricochet = ricochet;
        this.damage = damage;
        this.creator = creator;
    }



    public void onSolidCollision(NSJEntity entity) {
        if (ricochet && !ricochetFlag) {
            ricochetFlag = true;
            //Where is entity
            //Up, down, left, right


            //Left & Right
            //calculate lowest error


            float errorLeft = entity.getBoundingBox().getX() + entity.getBoundingBox().getWidth() - getBoundingBox().getX();
            float errorRight = entity.getBoundingBox().getX() - (getBoundingBox().getX() + getBoundingBox().getWidth());
            float errorX = Math.min(Math.abs(errorLeft), Math.abs(errorRight));

            float errorUp = entity.getBoundingBox().getY() + entity.getBoundingBox().getHeight() - getBoundingBox().getY();
            float errorDown = entity.getBoundingBox().getY() - (getBoundingBox().getY() + getBoundingBox().getHeight());
            float errorY = Math.min(Math.abs(errorUp), Math.abs(errorDown));




           // if (Math.abs(errorX - errorY) < 6f)
            //{
                velocityX *= -1;
                velocityY *= -1;
        //    } else
         //   if (errorX < errorY)
         //       velocityX *= -1;
        //    else

        //        velocityY *= -1;


        } else {
            destroy();
        }
    }

    public void updateProjectile(NSJMap map) {



        velocityX += accelerationX * Gdx.graphics.getDeltaTime();
        velocityY += accelerationY* Gdx.graphics.getDeltaTime();

        float destX = x + (velocityX * Gdx.graphics.getDeltaTime());
        float destY = y + (velocityY * Gdx.graphics.getDeltaTime());

        List<Point> inBetween = NSJBresenham.getPoints((int)x,(int)y,(int)destX,(int)destY);

        boolean collidedWithCreator = false;

        for (Point pt : inBetween) {
            List<NSJEntity> entities = map.getEntitiesAtPosition(getBoundingBox(), pt.x,pt.y);
            for (NSJEntity entity : entities) {
                if (entity instanceof NSJCharacter || entity instanceof NSJPlayer)
                {
                    if (entity == creator && avoidCreatorInitiallyFlag) {
                        collidedWithCreator = true;
                        continue;
                    }

                    if (getBoundingBox().overlaps(entity.getBoundingBox())) {
                        ((NSJCharacter)entity).onProjectileCollision(this);

                        //Destroy projectile
                        destroy();


                        return;
                    }
                }

                if (!entity.canPlayerWalkThrough)
                {
                    if (getBoundingBox().overlaps(entity.getBoundingBox())) {
                        onSolidCollision(entity);
                        //Potentially recalculate update with changed velocities
                        //But i think this will be complex


                        x += velocityX* Gdx.graphics.getDeltaTime();
                        y += velocityY* Gdx.graphics.getDeltaTime();
                        return;
                    }
                }
            }
        }

        ricochetFlag = false;

        if (!collidedWithCreator)
            avoidCreatorInitiallyFlag = false;

        x += velocityX* Gdx.graphics.getDeltaTime();
        y += velocityY* Gdx.graphics.getDeltaTime();
    }

    public int getDamage() {
        return damage;
    }

    /**
     * Pair(HSpeed, VSpeed)
     * @param originX
     * @param originY
     * @param destX
     * @param destY
     * @return
     */
    public static NSJPair<Float,Float> computeSpeeds(float originX, float originY, float destX, float destY, float desiredProjectileSpeed) {

        float deltaX = originX - destX;
        float deltaY = originY - destY;

        //Compute Horizontal and Vertical components such that
        //Satisfy:
        //HorizontalComponent + VerticalComponent = Desired Projectile Speed
        //HorizontalComponent/VerticalComponent = DeltaX/DeltaY
        //Simplifies to:
        //HorizontalComponent = (DeltaX * Desired Projectile Speed) / (DeltaX + DeltaY)
        //VerticalComponent = (DeltaY * Desired Projectile Speed) / (DeltaX + DeltaY)

        float k = -(desiredProjectileSpeed / (Math.abs(deltaX) + Math.abs(deltaY)));
        float hSpeed = deltaX * k;
        float vSpeed = deltaY * k;

        return new NSJPair<Float, Float>(hSpeed, vSpeed);
    }
}
