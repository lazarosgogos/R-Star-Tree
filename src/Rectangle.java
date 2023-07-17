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
}
