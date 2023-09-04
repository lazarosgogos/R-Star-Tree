import java.io.Serial;
import java.util.LinkedList;

/**
 * This class represents the LeafNode of the tree. A LeafNode is a Node which contains only PointEntry objects.
 */
public class LeafNode extends Node {

    /**
     * Static UID for serialization of the class.
     */
    @Serial
    private static final long serialVersionUID = 8867397353887205397L;

    private LinkedList<PointEntry> pointEntries;

    public LeafNode(LinkedList<PointEntry> pointEntries) {
        this.pointEntries = pointEntries;
        leaf = true;
    }

    public LinkedList<PointEntry> getPointEntries() {
        return pointEntries;
    }


    public void addEntry(PointEntry entry) {
        Node currentNode = this;

        LinkedList<PointEntry> pointEntriesTemp = new LinkedList<>(((LeafNode) currentNode).getPointEntries());
        pointEntriesTemp.add(entry);

        // IF ROOT IS LEAF NODE
        if (currentNode.isRoot()) {
            Main.root = new LeafNode(pointEntriesTemp);
            Main.root.setRoot(true);
            Main.rootEntry = new RectangleEntry((LeafNode) Main.root);
            Main.rootEntry.setContainer(Main.imaginaryRoot);
            Main.root.setParent(Main.rootEntry);
            return;
        }

        // IF LEAF NODE IS NOT ROOT
        RectangleEntry pointerToCurrentNode; // RE OF LEAF NODE

        LeafNode adjustedLeaf = new LeafNode(pointEntriesTemp);
        RectangleEntry pointerToAdjustedLeaf = new RectangleEntry(adjustedLeaf); // CREATE RE FOR LEAF NODE WITH NEW POINT
        adjustedLeaf.setParent(pointerToAdjustedLeaf);
        for (PointEntry re : adjustedLeaf.getPointEntries()){
            re.setContainer(adjustedLeaf);
        }

        while (true) {
            pointerToCurrentNode = currentNode.getParent();

            NoLeafNode containerOfPointerToCurrentNode = (NoLeafNode) pointerToCurrentNode.getContainer();
            LinkedList<RectangleEntry> rectangleEntriesOfContainer = new LinkedList<>(containerOfPointerToCurrentNode.getRectangleEntries());
            rectangleEntriesOfContainer.remove(pointerToCurrentNode);
            rectangleEntriesOfContainer.add(pointerToAdjustedLeaf);

            NoLeafNode newParallelNode = new NoLeafNode(rectangleEntriesOfContainer);
            for (RectangleEntry re : newParallelNode.getRectangleEntries()){
                re.setContainer(newParallelNode);
            }
            pointerToAdjustedLeaf = new RectangleEntry(newParallelNode);
            newParallelNode.setParent(pointerToAdjustedLeaf);

            currentNode = containerOfPointerToCurrentNode;

            if (currentNode.isRoot()){
                newParallelNode.setRoot(true);
                Main.root = newParallelNode;
                break;
            }
        }
        //TODO ERRORS prepei na broume tropo na stamatame sto upwards stin riza xoris na akoumpane se nulls
        //I THINK I FIXED THAT
    }
}
