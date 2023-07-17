import java.util.ArrayList;
import java.util.LinkedList;

/**
 * This class represents the NoLeafNode of the tree.
 * NoLeafNodes are the root of the tree and
 * all the Nodes between root level and leaves level.
 * NoLeafNode contains rectangleEntries and a pointer pointing to its child Node.
 * Root Node doesn't have a parent Node.
 */
public class NoLeafNode extends Node {
    private Boolean root;
    private LinkedList<RectangleEntry> rectangleEntries;
    private LinkedList<Node> children;

    public NoLeafNode(LinkedList<RectangleEntry> rectangleEntries) {
        this.rectangleEntries = rectangleEntries;
        leaf = false;
    }

    public void setChildren(LinkedList<Node> children) {
        this.children = children;
    }

    public LinkedList<RectangleEntry> getRectangleEntries() {
        return rectangleEntries;
    }

    public void setRoot(Boolean root) {
        this.root = root;
    }
}
