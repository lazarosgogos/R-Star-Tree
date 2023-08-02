import java.text.DecimalFormat;

/**
 * This class represents the Rectangle that wraps some Points.
 * This is the class of the Minimum Bounding Rectangle (MBR).
 * The only info we need about its spacing are the left-down and the top-right corners.
 */
public class Rectangle {
    double xStart; //down left corner
    double yStart; //down left corner
    double xEnd; //up right corner
    double yEnd; //up right corner

    private Point start; // down left corner
    private Point end; // upper right corner

    public Rectangle(double xStart, double yStart, double xEnd, double yEnd) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
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
        if (this.xStart < other.xEnd && this.xEnd > other.xStart)
            if (this.yStart < other.yEnd && this.yEnd > other.yStart)
                return true;
        return false;
    }

    public boolean contains(Rectangle other) {
        if (other.xStart >= this.xStart && other.xEnd <= this.xEnd){
            if (other.yStart >= this.yStart && other.yEnd <= this.yEnd)
                return true;
        }
        return false;
    }

    public boolean contains(PointEntry pe){
        Point p = pe.getPoint();
        if (p.getX() >= this.xStart && p.getX() <= this.xEnd)
            if (p.getY() >= this.yStart && p.getY() <= this.yEnd)
                return true;
        return false;
    }

    public boolean contains(Point p){
        if (p.getX() >= this.xStart && p.getX() <= this.xEnd)
            if (p.getY() >= this.yStart && p.getY() <= this.yEnd)
                return true;
        return false;
    }

}
