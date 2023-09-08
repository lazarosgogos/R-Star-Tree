import java.util.LinkedList;

/**
 * This class represents the NoLeafNode of the tree.
 * NoLeafNodes are the root of the tree and
 * all the Nodes between root level and leaves level.
 * NoLeafNode contains rectangleEntries and a pointer pointing to its child Node.
 * Root Node doesn't have a parent Node.
 */
public class NoLeafNode extends Node {
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

    public LinkedList<Node> getChildren() {
        return children;
    }

    public void update(){
        NoLeafNode tempNode = new NoLeafNode(getRectangleEntries());
        RectangleEntry tempRE = new RectangleEntry(tempNode);

        getParent().getRectangle().setStartPoint(tempRE.getRectangle().getStartPoint());
        getParent().getRectangle().setEndPoint(tempRE.getRectangle().getEndPoint());
    }

    public void update2(RectangleEntry re1, RectangleEntry re2){
        getRectangleEntries().add(re1);
        getRectangleEntries().add(re2);
        re1.setContainer(this);
        re2.setContainer(this);
    }
}
