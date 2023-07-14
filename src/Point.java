/**
 * This class represents the Point, not necessarily a Point that has been in the tree.
 */
public class Point {
    double x;
    double y;
    int xs;
    int ys;
    String name;
    long id;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        this.xs = (int) x;
        this.ys = (int) y;
        //Enable this when using coordinates
//        this.xs = (int) x*10000000;
//        this.ys = (int) y*10000000;
    }

    public Point(long id, String name, double x, double y){
        this.x = x;
        this.y = y;
        this.xs = (int) x*10000000;
        this.ys = (int) y*10000000;
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
}
