/**
 * This class represents the Rectangle that wraps some Points.
 * This is the class of the Minimum Bounding Rectangle (MBR).
 * The only info we need about its spacing are the left-down and the top-right corners.
 */
public class Rectangle {
    float xStart; //down left corner
    float yStart; //down left corner
    float xEnd; //up right corner
    float yEnd; //up right corner

    public Rectangle(float xStart, float yStart, float xEnd, float yEnd) {
        this.xStart = xStart;
        this.yStart = yStart;
        this.xEnd = xEnd;
        this.yEnd = yEnd;
    }

    @Override
    public String toString() {
        return "(" + String.format("%.7f", xStart) + "," + String.format("%.7f", yStart) + "), (" + String.format("%.7f", xEnd) + "," + String.format("%.7f", yEnd) + ")";
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

}
