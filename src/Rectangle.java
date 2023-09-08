import java.util.*;

/**
 * This class represents the Rectangle that wraps some Points.
 * This is the class of the Minimum Bounding Rectangle (MBR).
 * The only info we need about its spacing are the left-down and the top-right corners.
 */
public class Rectangle implements java.io.Serializable {
    private Point start; // down left corner
    private Point end; // upper right corner

    /**
     * Given two points create a rectangle/bounding box, no matter if the start and end point are in the proper order
     *
     * @param start first point of the rectangle
     * @param end   second point of the rectangle
     */
    public Rectangle(Point start, Point end) {
//        int n = start.getCoords().size(); // number of dimensions
//        ArrayList<Double> mins = new ArrayList<>(n);
//        ArrayList<Double> maxs = new ArrayList<>(n);
//
//        for (int i = 0; i < n; i++) {
//            double min = Math.min(start.getCoords().get(i), end.getCoords().get(i));
//            double max = Math.max(start.getCoords().get(i), end.getCoords().get(i));
//            mins.add(min);
//            maxs.add(max);
//        }
//        this.start = new Point(mins);
//        this.end = new Point(maxs);

        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {

        StringBuilder start = new StringBuilder();
        start.append("(");
        for (double coord : getStartPoint().getCoords()) {
            start.append(coord);
            start.append(", ");
        }
        int pos = start.lastIndexOf(", ");
        String temp = start.substring(0, pos);
        start = new StringBuilder();
        start.append(temp);
        start.append("), ");

        StringBuilder end = new StringBuilder();
        end.append("(");
        for (double coord : getEndPoint().getCoords()) {
            end.append(coord);
            end.append(", ");
        }
        pos = end.lastIndexOf(", ");
        temp = end.substring(0, pos);
        end = new StringBuilder();
        end.append(temp);
        end.append(")");

        start.append(end);

        return start.toString();
    }


    // Determine if two rectangles overlap one another
    // Sources:
    // https://silentmatt.com/rectangle-intersection/
    // https://stackoverflow.com/questions/306316/determine-if-two-rectangles-overlap-each-other
    public boolean overlaps(Rectangle other) {
        for (int i = 0; i < this.start.getCoords().size(); i++) {
            if (!(this.getStartPoint().getCoords().get(i) < other.getEndPoint().getCoords().get(i) &&
                    this.getStartPoint().getCoords().get(i) > other.getStartPoint().getCoords().get(i))) {
                return false;
            }
        }
        return true;

//        if (this.xStart < other.xEnd && this.xEnd > other.xStart)
//            if (this.yStart < other.yEnd && this.yEnd > other.yStart)
//                return true;
//        return false;
    }

    public boolean contains(Rectangle other) {
        for (int i = 0; i < this.start.getCoords().size(); i++) {
            if (!(other.getStartPoint().getCoords().get(i) >= this.getStartPoint().getCoords().get(i) &&
                    other.getEndPoint().getCoords().get(i) <= this.getEndPoint().getCoords().get(i))) {
                return false;
            }
        }
        return true;

//        if (other.xStart >= this.xStart && other.xEnd <= this.xEnd) {
//            if (other.yStart >= this.yStart && other.yEnd <= this.yEnd)
//                return true;
//        }
//        return false;
    }

    public boolean contains(PointEntry pe) {
        Point p = pe.getPoint();
        return this.contains(p);
//        if (p.getX() >= this.xStart && p.getX() <= this.xEnd)
//            if (p.getY() >= this.yStart && p.getY() <= this.yEnd)
//                return true;
//        return false;
    }

    public boolean contains(Point p) {
        final ArrayList<Double> startCoords = this.start.getCoords();
        final ArrayList<Double> endCoords = this.end.getCoords();
        for (int i = 0; i < this.start.getCoords().size(); i++)
            // size of start coords and end coords is the SAME
            // if point is OUTSIDE the rectangle even in ONE axis
            if (!(p.getCoords().get(i) >= startCoords.get(i) && p.getCoords().get(i) <= endCoords.get(i)))
                // don't proceed :)
                return false;
        // otherwise return true
        return true;
//        if (p.getX() >= this.xStart && p.getX() <= this.xEnd)
//            if (p.getY() >= this.yStart && p.getY() <= this.yEnd)
//                return true;
//        return false;
    }

    public Point getCenter() {
        int n = this.getStartPoint().getCoords().size();
        ArrayList<Double> centerCoords = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            // calculate the center
            double centerOfCurrentSide = this.getStartPoint().getCoords().get(i) + this.getEndPoint().getCoords().get(i);
            centerOfCurrentSide /= 2;
            centerCoords.add(centerOfCurrentSide);
        }
        return new Point(centerCoords);
    }

    public double getArea() {
        double product = 1;
        int destinations = this.getStartPoint().getCoords().size();
        for (int i = 0; i < destinations; i++) {
            product *= this.getEndPoint().getCoords().get(i) - this.getStartPoint().getCoords().get(i);
        }
        return product;
    }

    public double getMargin() {
        double sum = 0;
        int dimensions = this.getStartPoint().getCoords().size();
        for (int i = 0; i < dimensions; i++)
            sum += this.getEndPoint().getCoords().get(i) - this.getStartPoint().getCoords().get(i);
        return sum;
    }

    public double getOverlap(Rectangle other) {
        int dimensions = this.getStartPoint().getCoords().size();

        double product = 1;
        for (int i = 0; i < dimensions; i++) {
            double[] array = {other.getStartPoint().getCoords().get(i),
                    other.getEndPoint().getCoords().get(i),
                    this.getStartPoint().getCoords().get(i),
                    this.getEndPoint().getCoords().get(i)
            };
            Arrays.sort(array);
            product *= array[2] - array[1];
            // we ignore array[3] and array[0] and only deal with the 2 in-between points
        }

        return product;
    }

    public Point getStartPoint() {
        return start;
    }

    public Point getEndPoint() {
        return end;
    }

    public void setStartPoint(Point start) {
        this.start = start;
    }

    public void setEndPoint(Point end) {
        this.end = end;
    }
}
