
public class NSJHash {
    public static int hashEntityPosition(NSJEntity entity) {
        return hashEntityPosition(entity.getX(), entity.getY());
    }

    public static int hashEntityPosition(float x, float y) {
        return hashEntityPosition((int)x,(int)y);
    }

    public static int hashEntityPosition(int x, int y) {
       // if (x)
        return 0;
    }
}
