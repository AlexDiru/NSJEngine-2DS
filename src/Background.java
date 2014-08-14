import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 14/01/14
 * Time: 18:26
 * To change this template use File | Settings | File Templates.
 */
public class Background {

    private Texture texture;
    private float scrollLevel;
    private float scrollSpeed;

    public Background(Texture texture, float scrollSpeed) {
        this.texture = texture;
        this.scrollSpeed = scrollSpeed/100f;

        if (scrollSpeed < 0)
            scrollLevel = 1;
        else
            scrollLevel = 0;
    }

    public void update() {
        scrollLevel += scrollSpeed * Gdx.graphics.getDeltaTime();

        if (scrollSpeed > 0)
            if (scrollLevel > 1)
                scrollLevel -= 1;

        if (scrollSpeed < 0)
            if (scrollLevel < 0)
                scrollLevel += 1;
    }

    public void render(SpriteBatch spriteBatch) {
        float y = (int)(scrollLevel * texture.getHeight());

        int width = 0;
        while (width < Gdx.graphics.getHeight()) {
            spriteBatch.draw(texture,0,width + y-texture.getHeight());
            spriteBatch.draw(texture,0,width + y);
            width += texture.getHeight();
        }
    }
}
