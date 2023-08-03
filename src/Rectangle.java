import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * This class represents the Rectangle that wraps some Points.
 * This is the class of the Minimum Bounding Rectangle (MBR).
 * The only info we need about its spacing are the left-down and the top-right corners.
 */
public class Rectangle {
    @Deprecated
    double xStart; //down left corner
    @Deprecated
    double yStart; //down left corner
    @Deprecated
    double xEnd; //up right corner
    @Deprecated
    double yEnd; //up right corner
    @Deprecated

    private Point start; // down left corner
    private Point end; // upper right corner

    @Deprecated
    public Rectangle(double xStart, double yStart, double xEnd, double yEnd) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
    }


    /**
     * Given two points create a rectangle/bounding box, no matter if the start and end point are in the proper order
     * @param start first point of the rectangle
     * @param end second point of the rectangle
     */
    public Rectangle(Point start, Point end){
        // WRONG!
//        this.start = new Point();
//        this.end = new Point();
        int n = start.getCoords().size(); // number of dimensions
        ArrayList<Double> mins = new ArrayList<>(n);
        ArrayList<Double> maxs = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            double min = Math.min(start.getCoords().get(i), end.getCoords().get(i));
            double max = Math.max(start.getCoords().get(i), end.getCoords().get(i));
            mins.add(min);
            maxs.add(max);
        }
        this.start = new Point(mins);
        this.end = new Point(maxs);
    }

    @Override
    public String toString() {
//        int numberOfDecimalPlaces = 7;
//
//        DecimalFormat decimalFormat = new DecimalFormat("#." + "0".repeat(numberOfDecimalPlaces));
//        String formattedxStart = decimalFormat.format(xStart);
//        String formattedyStart = decimalFormat.format(yStart);
//        String formattedxEnd = decimalFormat.format(xEnd);
//        String formattedyEnd = decimalFormat.format(yEnd);
        return "(" + xStart + ", " + yStart + "), (" + xEnd + ", " + yEnd + ")";
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
    public Point getCenter(){
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

    public Point getStartPoint() {
        return start;
    }

    public Point getEndPoint() {
        return end;
    }
}
