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

    public void setPointEntries(LinkedList<PointEntry> pointEntries) {
        this.pointEntries = pointEntries;
    }

    public void update(LinkedList<PointEntry> pointEntriesTemp){
        // Φτιάχνουμε έναν εικονικό κόμβο για να υπολογίσουμε το νέο τετράγωνο
        LeafNode tempLeafNode = new LeafNode(pointEntriesTemp);
        RectangleEntry tempRE = new RectangleEntry(tempLeafNode);

        // Ανανεώνουμε τα στοιχεία του υπάρχοντα κόμβου και προσαρμόζουμε το τετράγωνό του
        LeafNode cNode = this;
        cNode.getParent().getRectangle().setStartPoint(tempRE.getRectangle().getStartPoint());
        cNode.getParent().getRectangle().setEndPoint(tempRE.getRectangle().getEndPoint());
        cNode.setPointEntries(pointEntriesTemp);
        for (PointEntry entry : cNode.getPointEntries()){
            entry.setContainer(cNode);
        }
    }

    public void addEntry(PointEntry entry) {
        Node currentNode = this;

        LinkedList<PointEntry> pointEntriesTemp = new LinkedList<>(((LeafNode) currentNode).getPointEntries());
        pointEntriesTemp.add(entry);

        // IF ROOT IS LEAF NODE
        if (currentNode.isRoot()) {
            /*Main.root = new LeafNode(pointEntriesTemp);
            Main.root.setRoot(true);
            Main.rootEntry = new RectangleEntry((LeafNode) Main.root);
            Main.rootEntry.setContainer(Main.imaginaryRoot);
            Main.root.setParent(Main.rootEntry);*/

            update(pointEntriesTemp);
            return;
        }

        // IF LEAF NODE IS NOT ROOT
        RectangleEntry pointerToCurrentNode; // RE OF LEAF NODE

        LeafNode adjustedLeaf = new LeafNode(pointEntriesTemp);
        for (PointEntry pe : adjustedLeaf.getPointEntries()){
            pe.setContainer(adjustedLeaf);
        }
        RectangleEntry pointerToAdjustedLeaf = new RectangleEntry(adjustedLeaf); // CREATE RE FOR LEAF NODE WITH NEW POINT
        adjustedLeaf.setParent(pointerToAdjustedLeaf);

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
    }
}
