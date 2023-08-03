/**
 * This class represents a Node of the tree.
 * All Nodes except root have a parent Node.
 */
public class Node {
    RectangleEntry parent;
    Boolean leaf;
    public void setParent(RectangleEntry parent) {
        this.parent = parent;
    }
}
