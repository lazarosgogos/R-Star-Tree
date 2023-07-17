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
    public static float min(ArrayList<Float> list) {
        float min = list.get(0);
        // Iterate over the list starting from the second element
        for (int i = 1; i < list.size(); i++) {
            float current = list.get(i);
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
    public static float max(ArrayList<Float> list) {
        float max = list.get(0);
        // Iterate over the list starting from the second element
        for (int i = 1; i < list.size(); i++) {
            float current = list.get(i);
            if (current > max) {
                max = current; // Update the maximum value
            }
        }
        return max;
    }

    public RectangleEntry(LeafNode child) {
        this.child = child;
        this.leaf = true;

        ArrayList<Float> xes = new ArrayList<>();
        ArrayList<Float> yes = new ArrayList<>();

        for (PointEntry pointEntry : child.getPointEntries()) {
            Point temp = pointEntry.getPoint();
            xes.add(temp.getX());
            yes.add(temp.getY());
        }

        float minX = RectangleEntry.min(xes);
        float maxX = RectangleEntry.max(xes);

        float minY = RectangleEntry.min(yes);
        float maxY = RectangleEntry.max(yes);

        rectangle = new Rectangle(minX, minY, maxX, maxY);
    }

    public RectangleEntry(NoLeafNode child) {
        this.child = child;
        this.leaf = false;

        ArrayList<Float> xStarts = new ArrayList<>();
        ArrayList<Float> yStarts = new ArrayList<>();
        ArrayList<Float> xEnds = new ArrayList<>();
        ArrayList<Float> yEnds = new ArrayList<>();

        for (RectangleEntry rectangleEntry : child.getRectangleEntries()) {
            Rectangle temp = rectangleEntry.getRectangle();
            xStarts.add(temp.xStart);
            yStarts.add(temp.yStart);
            xEnds.add(temp.xEnd);
            yEnds.add(temp.yEnd);
        }

        float minX = RectangleEntry.min(xStarts);
        float minY = RectangleEntry.min(yStarts);
        float maxX = RectangleEntry.max(xEnds);
        float maxY = RectangleEntry.max(yEnds);

        rectangle = new Rectangle(minX, minY, maxX, maxY);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Node getChild() {
        return child;
    }
}
