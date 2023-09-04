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



    public void addEntry(PointEntry entry){
        LinkedList<PointEntry> temp = new LinkedList<>(getPointEntries());
        temp.add(entry);

        RectangleEntry oldParent = this.getParent();
        if (this.isRoot()){
            Main.root = new LeafNode(temp);
            Main.root.setRoot(true);
            Main.rootEntry = new RectangleEntry((LeafNode) Main.root);
            Main.rootEntry.setContainer(Main.imaginaryRoot);
            Main.root.setParent(Main.rootEntry);
            return;
        }
        RectangleEntry newParent = new RectangleEntry(new LeafNode(temp));
        do {
            this.setParent(newParent);

            NoLeafNode oldParentContainer = (NoLeafNode) oldParent.getContainer();
            oldParentContainer.getRectangleEntries().remove(oldParent);
            oldParentContainer.getRectangleEntries().add(newParent);

            newParent = new RectangleEntry(new NoLeafNode(oldParentContainer.getRectangleEntries()));
            oldParent = oldParentContainer.getParent();
        } while (oldParent != null);
        //TODO ERRORS prepei na broume tropo na stamatame sto upwards stin riza xoris na akoumpane se nulls
    }
}
