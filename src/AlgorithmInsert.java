import java.util.*;

public class AlgorithmInsert {

    private static HashSet<Integer> levelOverflowStatus = new HashSet<>(/* number of levels of tree */);

    public static void insert(PointEntry entry) {
        boolean action = false;
        LeafNode N = (LeafNode) ChooseSubtree.chooseSubtree(Main.root, entry);
        if (N.getPointEntries().size() < Main.M) {
            N.addEntry(entry);
            return;
        }
        if (N.getPointEntries().size() == Main.M) {
            action = overflowTreatment(N.getLevel());
        }
        if (action) { // if boolean var "action" is true, invoke reinsert
            reInsert(N, entry);
        } else { // else invoke split
            NoLeafNode returnable = AlgorithmSplit.split(N, entry);
            if (returnable != null) {
                Main.root = returnable;
                Main.root.setLevel(((NoLeafNode) Main.root).getRectangleEntries().get(0).getChild().getLevel() + 1);
            }
        }
    }

    /**
     * This method decides whether a reinsertion or a split will occur. If true is returned, a reinsertion must happen.
     * If false is returned, a split must be put into place.
     *
     * @param level The level of the node that is being inserted
     * @return True if reinsertion must be done, false if a split must be done.
     */
    public static boolean overflowTreatment(int level) {
        // if this level has not been examined yet
        // hence a reinsertion must occur
        if (!levelOverflowStatus.contains(level)) { // OT1
            levelOverflowStatus.add(level);
            return true;
        }
        return false;
    }

    public static void reInsert(Node N, PointEntry pointEntry) {
        int p = Math.round(0.3f * Main.M);

        if (N instanceof NoLeafNode currentNode) {
            LinkedList<RectangleEntryDoublePair> pairs = new LinkedList<>();

            for (RectangleEntry entry : currentNode.getRectangleEntries()) { //RI1
                double distance = entry.getRectangle().getCenter().distance(currentNode.getParent().getRectangle().getCenter());
                RectangleEntryDoublePair pair = new RectangleEntryDoublePair(entry, distance);
                pairs.add(pair);
            }

            pairs.sort(new RectangleEntryDoublePairComparator()); // RI2

            List<RectangleEntryDoublePair> trash;
            trash = pairs.subList(p, pairs.size());
            HashSet<PointEntry> temp = new HashSet<>();
            for (RectangleEntryDoublePair pair : trash) {// RI4
                dfs(pair.getRectangleEntry().getChild(), temp);
            }
            for (PointEntry pe : temp) {
                insert(pe);
            }

        } else { // N instance of LeafNode
            LeafNode currentNode = (LeafNode) N;

            LinkedList<PointEntryDoublePair> pairs = new LinkedList<>();

            for (PointEntry entry : currentNode.getPointEntries()) { //RI1
                double distance = entry.getPoint().distance(currentNode.getParent().getRectangle().getCenter());
                pairs.add(new PointEntryDoublePair(entry, distance));
            }
            pairs.add(new PointEntryDoublePair(pointEntry, pointEntry.getPoint().distance(currentNode.getParent().getRectangle().getCenter())));

            pairs.sort(new PointEntryDoublePairComparator()); //RI2

            LinkedList<PointEntryDoublePair> trash = new LinkedList<>();
            for (int i = 0; i < p; i++) {
                trash.add(pairs.pop());
            }

            // Τα στοιχεία που θα μείνουν μέσα στον υπάρχοντα κόμβο
            LinkedList<PointEntry> pointEntriesTemp = new LinkedList<>();
            for (PointEntryDoublePair pair : pairs) {
                pointEntriesTemp.add(pair.getPointEntry());
            }

            currentNode.update(pointEntriesTemp);

            RectangleEntry tempRE;
            // Αν δεν είναι ρίζα, τότε πρέπει να προσαρμόσουμε και τα τετράγωνα των παραπάνων επιπέδων
            if (!currentNode.isRoot()) {
                NoLeafNode parentContainer = (NoLeafNode) currentNode.getParent().getContainer();
                while (true) {

                    // Φτιάχνουμε έναν εικονικό κόμβο για να υπολογίσουμε το νέο τετράγωνο
                    NoLeafNode tempNode = new NoLeafNode(parentContainer.getRectangleEntries());
                    tempRE = new RectangleEntry(tempNode);

                    // Προσαρμόζουμε το τετράγωνό του υπάρχοντα κόμβου
                    parentContainer.getParent().getRectangle().setStartPoint(tempRE.getRectangle().getStartPoint());
                    parentContainer.getParent().getRectangle().setEndPoint(tempRE.getRectangle().getEndPoint());

                    if (parentContainer.isRoot()) { // Σταματάμε μόλις προσαρμόσουμε και την ρίζα
                        break;
                    }
                    parentContainer = (NoLeafNode) parentContainer.getParent().getContainer();
                }
            }

            // Call insert() to reinsert the p points into the tree
            for (PointEntryDoublePair pair : trash) { //RI4
                insert(pair.getPointEntry());
            }
        }
    }

    public static void dfs(Node root, HashSet<PointEntry> list) {
        if (!root.leaf) {
            LinkedList<RectangleEntry> rectangles = ((NoLeafNode) root).getRectangleEntries();
            for (RectangleEntry rectangleEntry : rectangles) {
                dfs(rectangleEntry.getChild(), list);
            }
        } else {
            list.addAll(((LeafNode) root).getPointEntries());
        }
    }


    private static class RectangleEntryDoublePairComparator implements Comparator<RectangleEntryDoublePair> {
        @Override
        public int compare(RectangleEntryDoublePair o, RectangleEntryDoublePair t1) {
            return Double.compare(o.getValue(), t1.getValue());
        }
    }

    private static class RectangleEntryDoublePair {
        private RectangleEntry rectangleEntry;
        private Double value; // the value of some point

        public RectangleEntryDoublePair(RectangleEntry rectangleEntry, Double value) {
            this.rectangleEntry = rectangleEntry;
            this.value = value;
        }

        public RectangleEntry getRectangleEntry() {
            return rectangleEntry;
        }

        public Double getValue() {
            return value;
        }
    }

    private static class PointEntryDoublePairComparator implements Comparator<PointEntryDoublePair> {
        @Override
        public int compare(PointEntryDoublePair o, PointEntryDoublePair t1) {
            return Double.compare(o.getValue(), t1.getValue());
        }
    }

    private static class PointEntryDoublePair {
        private PointEntry pointEntry;
        private Double value; // the value of some point

        public PointEntryDoublePair(PointEntry pointEntry, Double value) {
            this.pointEntry = pointEntry;
            this.value = value;
        }

        public PointEntry getPointEntry() {
            return pointEntry;
        }

        public Double getValue() {
            return value;
        }
    }
}
