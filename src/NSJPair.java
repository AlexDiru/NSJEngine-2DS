/**
 * Created with IntelliJ IDEA.
 * User: Alex
 * Date: 27/06/14
 * Time: 16:24
 * To change this template use File | Settings | File Templates.
 */
public class NSJPair<L,R> {

    private L left;
    private R right;

    public NSJPair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() {
        return left;
    }

    public void setLeft(L left) {
        this.left = left;
    }

    public R getRight() {
        return right;
    }

    public void setRight(R right) {
        this.right = right;
    }
}
