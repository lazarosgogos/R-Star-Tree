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
     *
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
     *
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

        // list of lists with coordinates
        ArrayList<ArrayList<Double>> es = new ArrayList<>();

        int number_of_coords = child.getPointEntries().get(0).getPoint().getCoords().size();

        // initialize the list of lists with empty lists
        for (int i = 0; i < number_of_coords; i++) {
            es.add(new ArrayList<>());
        }

        for (PointEntry pointEntry : child.getPointEntries()) {
            Point temp = pointEntry.getPoint();
            int counter = 0;
            for (double coord : temp.getCoords()) {
                es.get(counter).add(coord);
                counter++;
            }

        }

        ArrayList<Double> min = new ArrayList<>();
        ArrayList<Double> max = new ArrayList<>();

        for (int i = 0; i < number_of_coords; i++) {
            min.add(RectangleEntry.min(es.get(i)));
            max.add(RectangleEntry.max(es.get(i)));
        }

        rectangle = new Rectangle(new Point(min), new Point(max));
    }

    public RectangleEntry(NoLeafNode child) {
        this.child = child;
        this.leaf = false;

        // list of lists
        // each list represents a coordinate
        // and has a list with all values for this coordinate
        ArrayList<ArrayList<Double>> starts = new ArrayList<>();

        // list of lists
        ArrayList<ArrayList<Double>> ends = new ArrayList<>();

        int n = child.getRectangleEntries().get(0).getRectangle().getStartPoint().getCoords().size();

        for (int i = 0; i < n; i++) {
            starts.add(new ArrayList<>());
            ends.add(new ArrayList<>());
        }

        for (RectangleEntry rectangleEntry : child.getRectangleEntries()) {
            Rectangle temp = rectangleEntry.getRectangle();

            int counter = 0;
            for (double coord : temp.getStartPoint().getCoords()) {
                starts.get(counter).add(coord);
                counter++;
            }

            counter = 0;
            for (double coord : temp.getEndPoint().getCoords()) {
                ends.get(counter).add(coord);
                counter++;
            }
        }

        ArrayList<Double> min = new ArrayList<>();
        ArrayList<Double> max = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            min.add(RectangleEntry.min(starts.get(i)));
            max.add(RectangleEntry.max(ends.get(i)));
        }

        rectangle = new Rectangle(new Point(min), new Point(max));
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Node getChild() {
        return child;
    }
}
