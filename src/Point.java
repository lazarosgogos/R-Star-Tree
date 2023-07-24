import java.util.Objects;

/**
 * This class represents the Point, not necessarily a Point that has been in the tree.
 */
public class Point {
    double x;
    double y;
    String xStr;
    String yStr;
    int xs;
    int ys;
    String name;
    long id;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
        this.xs = (int) x;
        this.ys = (int) y;
    }

    public Point(long id, String name, String xStr, String yStr) {
        this.xStr = xStr;
        this.yStr = yStr;
        this.y = Double.parseDouble(yStr);
        this.x = Double.parseDouble(xStr);
        this.xs = Integer.parseInt(xStr.replace(".", ""));
        this.ys = Integer.parseInt(yStr.replace(".", ""));
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return "(" + xStr + ", " + yStr + ") " + name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return id == point.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, id);
    }
}
