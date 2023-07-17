import java.util.LinkedList;

/**
 * This class represents the LeafNode of the tree. A LeafNode is a Node which contains only PointEntry objects.
 */
public class LeafNode extends Node {
    LinkedList<PointEntry> pointEntries;

    public LeafNode(LinkedList<PointEntry> pointEntries) {
        this.pointEntries = pointEntries;
        leaf = true;
    }

    public LinkedList<PointEntry> getPointEntries() {
        return pointEntries;
    }
}
