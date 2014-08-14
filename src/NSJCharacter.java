import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.List;

public class NSJCharacter extends NSJEntity {

    private int health =69696969;
    private float showPainTextureTimer = 0;
    protected Texture painTexture;


    private boolean jumping = false;
    private boolean beginJump =false;
    private float zVelocity;
    private float initialJumpVelocity = 2;
    private float jumpDeceleration = 3f;

    protected NSJCharacter() {

    }

    public NSJCharacter(Texture texture, Texture painTexture, float x, float y) {
        super(texture, x, y);
        this.painTexture = painTexture;
    }

    public int getHealth() {
        return health;
    }

    public void onProjectileCollision(NSJProjectile proj) {
        health -= proj.getDamage();
        showPainTextureTimer = 0.5f;
    }

    public void update(NSJMap map) {

        if (showPainTextureTimer > 0)
            showPainTextureTimer -= Gdx.graphics.getDeltaTime();

        if (health <= 0) {
            destroy();
        }

        if (this instanceof NSJAI)
            ((NSJAI)this).updateAI(map, map.getPlayer());

        //Jumping
        if (beginJump) {
            beginJump = false;
            jumping = true;
            zVelocity = initialJumpVelocity;
        }

        if (jumping) {
            z += zVelocity * Gdx.graphics.getDeltaTime();
            zVelocity -= jumpDeceleration * Gdx.graphics.getDeltaTime();

            if (zVelocity <= -initialJumpVelocity)
                jumping = false;
        }
    }

    public void renderCharacter(SpriteBatch spriteBatch, int offsetX, int offsetY) {
        if (showPainTextureTimer > 0)
            spriteBatch.draw(painTexture, x - offsetX, y - offsetY, 0,0,width, height,1,1,0,0,0,painTexture.getWidth(), painTexture.getHeight(),false,false);
        else
            spriteBatch.draw(texture, x - offsetX, y - offsetY, 0,0,width, height,1,1,0,0,0,texture.getWidth(), texture.getHeight(),false,false);
    }

    public boolean canMoveTo(NSJMap map, float destX, float destY) {
        //TODO only works for straight lines in either x or y
        //TODO change to use raycast/bresenham line or something similar


        if (Math.round(destX) == Math.round(x))
            if (Math.round(destY) > Math.round(y)) {
                for (int curY = Math.round(y); curY <= Math.round(destY); curY += 1){
                    List<NSJEntity> entities = map.getEntitiesAtPosition(null, destX, curY);

                    for (NSJEntity entity : entities)
                        if (!entity.canPlayerWalkThrough)
                            if (entity != this)
                                return false;
                }
            } else {
                for (int curY = Math.round(y); curY >= Math.round(destY); curY -= 1){
                    List<NSJEntity> entities = map.getEntitiesAtPosition(null, destX, curY);

                    for (NSJEntity entity : entities)
                        if (!entity.canPlayerWalkThrough)
                            if (entity != this)
                                return false;
                }
            }


        else if (Math.round(destY) == Math.round(y))
            if (Math.round(destX) > Math.round(x)) {
                for (int curX = Math.round(x); curX <= Math.round(destX); curX += 1){
                    List<NSJEntity> entities = map.getEntitiesAtPosition(null, curX, destY);

                    for (NSJEntity entity : entities)
                        if (!entity.canPlayerWalkThrough)
                            if (entity != this)
                                return false;
                }
            } else {
                for (float curX = Math.round(x); curX >= Math.round(destX); curX -= 1){
                    List<NSJEntity> entities = map.getEntitiesAtPosition(null, curX, destY);

                    for (NSJEntity entity : entities)
                        if (!entity.canPlayerWalkThrough)
                            if (entity != this)
                                return false;
                }
            }

        return true;
    }

    public void jump() {
        if (!jumping)
            beginJump = true;
    }
}
