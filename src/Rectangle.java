import java.text.DecimalFormat;
import java.util.*;

/**
 * This class represents the Rectangle that wraps some Points.
 * This is the class of the Minimum Bounding Rectangle (MBR).
 * The only info we need about its spacing are the left-down and the top-right corners.
 */
public class Rectangle implements java.io.Serializable {
//    @Deprecated
//    double xStart; //down left corner
//    @Deprecated
//    double yStart; //down left corner
//    @Deprecated
//    double xEnd; //up right corner
//    @Deprecated
//    double yEnd; //up right corner
//    @Deprecated

    private Point start; // down left corner
    private Point end; // upper right corner

//    @Deprecated
//    public Rectangle(double xStart, double yStart, double xEnd, double yEnd) {
//        this.xStart = xStart;
//        this.yStart = yStart;
//        this.xEnd = xEnd;
//        this.yEnd = yEnd;
//        this.start = new Point(xStart, yStart);
//        this.end = new Point(xEnd, yEnd);
//    }


    /**
     * Given two points create a rectangle/bounding box, no matter if the start and end point are in the proper order
     *
     * @param start first point of the rectangle
     * @param end   second point of the rectangle
     */
    public Rectangle(Point start, Point end) {
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

        //return "(" + xStart + ", " + yStart + "), (" + xEnd + ", " + yEnd + ")";
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

    public Point getStartPoint() {
        return start;
    }

    public Point getEndPoint() {
        return end;
    }

    public static double overlapCalculation(Rectangle r1, Rectangle r2) {
        int dimensions = r1.getStartPoint().getCoords().size();

        double product = 1;
        for (int i = 0; i < dimensions; i++) {
            double[] array = {r2.getStartPoint().getCoords().get(i),
                    r2.getEndPoint().getCoords().get(i),
                    r1.getStartPoint().getCoords().get(i),
                    r1.getEndPoint().getCoords().get(i)
            };
            Arrays.sort(array);
            product *= array[2] - array[1];
        }

        return product;
    }

    public static double totalOverlap(Rectangle r1, HashSet<RectangleEntry> rectangles) {
        double sum = 0;
        for (RectangleEntry rectangle : rectangles) {
            sum += overlapCalculation(r1, rectangle.getRectangle());
        }
        return sum;
    }

    public static boolean contains(RectangleEntry r, PointEntry p) {
        return r.getRectangle().contains(p);
    }

    public static Rectangle expand(RectangleEntry r, PointEntry p) {
        // list of lists with coordinates
        ArrayList<ArrayList<Double>> elements = new ArrayList<>();

        int dimensions = p.getPoint().getCoords().size();
        ArrayList<Double> min = new ArrayList<>();
        ArrayList<Double> max = new ArrayList<>();

        // initialize the list of lists with empty lists
        for (int i = 0; i < dimensions; i++) {
            elements.add(new ArrayList<>());
            elements.get(i).add(r.getRectangle().getStartPoint().getCoords().get(i));
            elements.get(i).add(r.getRectangle().getEndPoint().getCoords().get(i));
            elements.get(i).add(p.getPoint().getCoords().get(i));
            min.add(RectangleEntry.min(elements.get(i)));
            max.add(RectangleEntry.max(elements.get(i)));
        }

        return new Rectangle(new Point(min), new Point(max));
    }

    public static double getArea(Rectangle r) {
        double product = 1;
        int destinations = r.getStartPoint().getCoords().size();
        for (int i = 0; i < destinations; i++) {
            product *= r.getEndPoint().getCoords().get(i) - r.getStartPoint().getCoords().get(i);
        }
        return product;

    }

    public static double areaEnlargement(RectangleEntry r, PointEntry p) {
        Rectangle re = expand(r, p);
        return getArea(re) - getArea(r.getRectangle());
    }

    public static Node chooseSubtree(Node root, PointEntry entry) {
        Node tempN = root;

        if (tempN instanceof LeafNode) {
            return tempN;
        } else {
            NoLeafNode N = (NoLeafNode) tempN;
            if (N.getChildren().get(0) instanceof LeafNode) {
                HashSet<RectangleEntry> rectangleEntries = new HashSet<>(N.getRectangleEntries());
                HashMap<RectangleEntry, Double> overlapEnlargementScores = new HashMap<>();
                for (RectangleEntry rectangeEntry : rectangleEntries) {
                    // An apla anikei se yparxon tetragono kai den xreiazetai megethinsi
                    if (contains(rectangeEntry, entry)) {
                        return rectangeEntry.getChild();
                    } else { //calculate overlap enlargement for all possible enlargments
                        HashSet<RectangleEntry> temp = new HashSet<>(N.getRectangleEntries());
                        temp.remove(rectangeEntry);
                        overlapEnlargementScores.put(rectangeEntry, totalOverlap(expand(rectangeEntry, entry), temp));
                    }
                }
                double minOverlap = Collections.min(overlapEnlargementScores.values()); //find min
                ArrayList<RectangleEntry> minOverlapRectangles = new ArrayList<>();
                for (RectangleEntry key : overlapEnlargementScores.keySet()) { //find rectangle entries that are min
                    if (overlapEnlargementScores.get(key) == minOverlap) {
                        minOverlapRectangles.add(key);
                    }
                }
                if (minOverlapRectangles.size() > 1) { //if there are ties
                    HashMap<RectangleEntry, Double> areaEnlargementScores = new HashMap<>();
                    for (RectangleEntry rectangleEntry : minOverlapRectangles) { // calculate all possible area enlargements
                        areaEnlargementScores.put(rectangleEntry, areaEnlargement(rectangleEntry, entry));
                    }
                    double minAreaEnlargement = Collections.min(areaEnlargementScores.values()); //find min
                    ArrayList<RectangleEntry> minAreaEnlargementRectangles = new ArrayList<>();
                    for (RectangleEntry key : areaEnlargementScores.keySet()) {
                        if (areaEnlargementScores.get(key) == minAreaEnlargement) { // find rectangle entries that are min
                            minAreaEnlargementRectangles.add(key);
                        }
                    }
                    if (minAreaEnlargementRectangles.size() > 1) { //if there are ties in area enlargement
                        HashMap<RectangleEntry, Double> areaScores = new HashMap<>();
                        for (RectangleEntry rectangleEntry : minAreaEnlargementRectangles) { // calculate area of all
                            areaScores.put(rectangleEntry, getArea(rectangleEntry.getRectangle()));
                        }
                        double minArea = Collections.min(areaScores.values());
                        for (RectangleEntry key : areaScores.keySet()) {
                            if (areaScores.get(key) == minArea) {
                                return key.getChild(); // choose rectangle with smallest area
                            }
                        }
                    } else { // if there is only one min area enlargement
                        return minAreaEnlargementRectangles.get(0).getChild();
                    }
                } else { //if there is only one min overlap enlargement
                    return minOverlapRectangles.get(0).getChild();
                }
            } else {
                HashSet<RectangleEntry> rectangleEntries = new HashSet<>(N.getRectangleEntries());
                HashMap<RectangleEntry, Double> areaEnlargementScores = new HashMap<>();
                for (RectangleEntry rectangleEntry : rectangleEntries) { // calculate all possible area enlargements
                    areaEnlargementScores.put(rectangleEntry, areaEnlargement(rectangleEntry, entry));
                }
                double minAreaEnlargement = Collections.min(areaEnlargementScores.values()); //find min
                ArrayList<RectangleEntry> minAreaEnlargementRectangles = new ArrayList<>();
                for (RectangleEntry key : areaEnlargementScores.keySet()) {
                    if (areaEnlargementScores.get(key) == minAreaEnlargement) { // find rectangle entries that are min
                        minAreaEnlargementRectangles.add(key);
                    }
                }
                if (minAreaEnlargementRectangles.size() > 1) { //if there are ties in area enlargement
                    HashMap<RectangleEntry, Double> areaScores = new HashMap<>();
                    for (RectangleEntry rectangleEntry : minAreaEnlargementRectangles) { // calculate area of all
                        areaScores.put(rectangleEntry, getArea(rectangleEntry.getRectangle()));
                    }
                    double minArea = Collections.min(areaScores.values());
                    for (RectangleEntry key : areaScores.keySet()) {
                        if (areaScores.get(key) == minArea) {
                            return key.getChild(); // choose rectangle with smallest area
                        }
                    }
                }
                else { // if there is only one min area enlargement
                    return minAreaEnlargementRectangles.get(0).getChild();
                }
            }
        }
        return null;
    }
}
