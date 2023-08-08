/**
 * This class represents an Entry in a Node of the tree.
 */
public class Entry implements java.io.Serializable {
    Node container;

    public void setContainer(Node container) {
        this.container = container;
    }

    public Node getContainer() {
        return container;
    }
}
