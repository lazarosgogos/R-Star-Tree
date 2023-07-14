/**
 * This class represents a Point that has been inserted in a LeafNode of the tree.
 */
public class PointEntry extends Entry {
    private Point point;
    private String record_ID;

    public PointEntry(Point point, String record_ID) {
        this.point = point;
        this.record_ID = record_ID;
    }

    public Point getPoint() {
        return point;
    }
}
