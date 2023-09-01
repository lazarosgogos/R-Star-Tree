/**
 * This class represents a Node of the tree.
 * All Nodes except root have a parent Node.
 */
public class Node implements java.io.Serializable {
    RectangleEntry parent;
    Boolean leaf;

    private short level;

    public void setParent(RectangleEntry parent) {
        this.parent = parent;
    }

    public RectangleEntry getParent() {
        return parent;
    }

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }
}
