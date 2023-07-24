import java.util.ArrayList;
import java.util.Iterator;

/**
 * This class represents a Rectangle that has been inserted in a NoLeafNode of the tree.
 */
public class RectangleEntry extends Entry {
    private Rectangle rectangle;
    private Node child;
    private Boolean leaf;

    /**
     * Find min element in a list
     * @param list of elements
     * @return min
     */
    public static double min(ArrayList<Double> list) {
        double min = list.get(0);
        // Iterate over the list starting from the second element
        for (int i = 1; i < list.size(); i++) {
            double current = list.get(i);
            if (current < min) {
                min = current; // Update the minimum value
            }
        }
        return min;
    }

    /**
     * Find max element in a list
     * @param list of elements
     * @return max
     */
    public static double max(ArrayList<Double> list) {
        double max = list.get(0);
        // Iterate over the list starting from the second element
        for (int i = 1; i < list.size(); i++) {
            double current = list.get(i);
            if (current > max) {
                max = current; // Update the maximum value
            }
        }
        return max;
    }

    public RectangleEntry(LeafNode child) {
        this.child = child;
        this.leaf = true;

        ArrayList<Double> xes = new ArrayList<>();
        ArrayList<Double> yes = new ArrayList<>();

        for (PointEntry pointEntry : child.getPointEntries()) {
            Point temp = pointEntry.getPoint();
            xes.add(temp.getX());
            yes.add(temp.getY());
        }

        double minX = RectangleEntry.min(xes);
        double maxX = RectangleEntry.max(xes);

        double minY = RectangleEntry.min(yes);
        double maxY = RectangleEntry.max(yes);

        rectangle = new Rectangle(minX, minY, maxX, maxY);
    }

    public RectangleEntry(NoLeafNode child) {
        this.child = child;
        this.leaf = false;

        ArrayList<Double> xStarts = new ArrayList<>();
        ArrayList<Double> yStarts = new ArrayList<>();
        ArrayList<Double> xEnds = new ArrayList<>();
        ArrayList<Double> yEnds = new ArrayList<>();

        for (RectangleEntry rectangleEntry : child.getRectangleEntries()) {
            Rectangle temp = rectangleEntry.getRectangle();
            xStarts.add(temp.xStart);
            yStarts.add(temp.yStart);
            xEnds.add(temp.xEnd);
            yEnds.add(temp.yEnd);
        }

        double minX = RectangleEntry.min(xStarts);
        double minY = RectangleEntry.min(yStarts);
        double maxX = RectangleEntry.max(xEnds);
        double maxY = RectangleEntry.max(yEnds);

        rectangle = new Rectangle(minX, minY, maxX, maxY);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Node getChild() {
        return child;
    }
}
