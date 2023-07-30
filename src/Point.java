import java.util.Comparator;
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
        xStr = Double.toString(x);
        yStr = Double.toString(y);
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
        Point other = (Point) o;
        return Double.compare(this.getX(), other.getX()) == 0 && Double.compare(this.getY(), other.getY()) == 0;
    }

    public float distance(PointEntry other) {
        return (float) Math.sqrt(pseudoDistance(other.getPoint()));
    }

    public float distance(Point other) {
        return (float) Math.sqrt(pseudoDistance(other));
    }

    public float pseudoDistance(Point other) {
        float d1 = (float) (Math.pow(this.getX() - other.getX(), 2));
        float d2 = (float) (Math.pow(this.getY() - other.getY(), 2));
        return d1 + d2; // to get the actual distance, you must find the sqrt of this number
        // But, it's faster not to calculate the sqrt, so if it's not needed, it shall not be calculated
    }

    /**
     * Calculate the distance of a point from a given rectangle
     *
     * @param rect The rectangle from which the distance is needed
     * @return The distance between this point and the given rectangle
     */
    public double distance(RectangleEntry rect) {
        return this.distance(rect.getRectangle());
    }
    /**
     * Calculate the distance of a point from a given rectangle
     *
     * @param rect The rectangle from which the distance is needed
     * @return The distance between this point and the given rectangle
     */
    public double distance(Rectangle rect) {
        // first determine if this point is inside this rectangle
        // if point is inside rect => return 0
        // if not
        // find the minimum distance between two possible distances
        // one is perpendicular to the closest edge of the rectangle
        // the other is perpendicular to the rectangle's nearest right-angle
        // return the min_dist
        double dist = -1; // set initial value to something impossible (distance cannot be negative)
        if (rect.contains(this))
            return 0;

        // find the closest edge - X axis
        double xDist = Math.min(Math.abs(this.getX() - rect.xStart), Math.abs(this.getX() - rect.xEnd));
        double yDist = Math.min(Math.abs(this.getY() - rect.yStart), Math.abs(this.getY() - rect.yEnd));

        // if this point is inside the rectangle, x-axis-wise
        if (this.getX() >= rect.xStart && this.getX() <= rect.xEnd)
            dist = yDist; // we're interested in the y distance
        else if (this.getY() > rect.yStart && this.getY() <= rect.yEnd)
            dist = xDist; // else we're insterested in the x distance
        else
            // if however this point is outside both the x and y axes of the rectangle
            // we should find the distance between this point and the right-angle
            // which right-angle? the one that was determined when xDist and yDist was calculated
            dist = (float) Math.sqrt(Math.pow(xDist, 2) + Math.pow(yDist, 2));
        return dist;
    }


    @Override
    public int hashCode() {
        return Objects.hash(x, y, id);
    }

}
