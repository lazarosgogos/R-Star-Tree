/**
 * This class represents a Node of the tree.
 * All Nodes except root have a parent Node.
 */
public class Node implements java.io.Serializable {
    private RectangleEntry parent;
    private boolean root;
    Boolean leaf;

    private int level;

    public void setParent(RectangleEntry parent) {
        this.parent = parent;
    }

    public RectangleEntry getParent() {
        return parent;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setRoot(Boolean root) {
        this.root = root;
    }

    public Boolean isRoot(){return root;}
}
