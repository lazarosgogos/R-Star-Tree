import java.util.*;
import java.util.random.RandomGenerator;

public class AlgorithmInsert {

    private static HashSet<Integer> levelOverflowStatus = new HashSet<>(/* number of levels of tree */);

    public static void insert(PointEntry entry) {
        /*TODO How do we keep track of each node's level ?
         *  Either we keep that piece of information inside the node in some short integer
         *  or we keep a list/hashmap in a local field with each node's ID and its corresponding level.
         *  I find the first solution to be the optimal one, as it's both easier to implement, and more
         *  straight-forward */
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
            System.out.println("Going to split...");
            NoLeafNode returnable = AlgorithmSplit.split(N, entry);
            if (returnable != null) {
                Main.root = returnable;
                Main.root.setLevel(((NoLeafNode) Main.root).getRectangleEntries().get(0).getChild().getLevel() + 1);
                System.out.println("Split Done!");
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
//            reInsert();
            levelOverflowStatus.add(level);
            return true;
        } /*else {*/
//            AlgorithmSplit.split();
        return false;
//        return true;
        /**/
    }

    public static void reInsert(Node N, PointEntry pointEntry) {
        // TODO How are we supposed to reinsert the M+1 nodes
        //  when M is the maximum number of entries that can fit in the Node
        //  and it's possible that a reInsertion won't leave enough room for the final +1 entry
        //  that we want to insert? Or is it granted that it'll fit?

        int p = Math.round(0.3f * Main.M);

        if (N instanceof NoLeafNode) {
            LinkedList<RectangleEntryDoublePair> pairs = new LinkedList<>();

            for (RectangleEntry entry : ((NoLeafNode) N).getRectangleEntries()) { //RI1
                double distance = entry.getRectangle().getCenter().distance(N.getParent().getRectangle().getCenter());
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
            LinkedList<PointEntryDoublePair> pairs = new LinkedList<>();

            for (PointEntry entry : ((LeafNode) N).getPointEntries()) { //RI1
                double distance = entry.getPoint().distance(N.getParent().getRectangle().getCenter());
                pairs.add(new PointEntryDoublePair(entry, distance));
            }
            pairs.add(new PointEntryDoublePair(pointEntry, pointEntry.getPoint().distance(N.getParent().getRectangle().getCenter())));

            pairs.sort(new PointEntryDoublePairComparator()); //RI2

            LinkedList<PointEntryDoublePair> trash = new LinkedList<>();
            for (int i = 0; i < p; i++) {
                trash.add(pairs.pop());
            }

            //TODO ANANEOSE TO KOMVO ME TA STOIXEIA POY DEN PANE TRASH
            // Ton pairs, ton kanoume lista, ftiaxnoume neo komvo, neo rectangle kai antikathistoume isos kai upwards

            LinkedList<PointEntry> pointEntriesTemp = new LinkedList<>();
            for (PointEntryDoublePair pair : pairs) {
                pointEntriesTemp.add(pair.getPointEntry());
            }

            LeafNode tempLeafNode = new LeafNode(pointEntriesTemp);
            RectangleEntry tempRE = new RectangleEntry(tempLeafNode);

            LeafNode currentNode = (LeafNode) N;
            currentNode.getParent().getRectangle().setStartPoint(tempRE.getRectangle().getStartPoint());
            currentNode.getParent().getRectangle().setEndPoint(tempRE.getRectangle().getEndPoint());
            currentNode.setPointEntries(pointEntriesTemp);

            if (!currentNode.isRoot()) {
                NoLeafNode parentContainer = (NoLeafNode) currentNode.getParent().getContainer();
                while (true) {
                    NoLeafNode tempNode = new NoLeafNode(parentContainer.getRectangleEntries());
                    tempRE = new RectangleEntry(tempNode);

                    parentContainer.getParent().getRectangle().setStartPoint(tempRE.getRectangle().getStartPoint());
                    parentContainer.getParent().getRectangle().setEndPoint(tempRE.getRectangle().getEndPoint());

                    if (parentContainer.isRoot()) {
                        break;
                    }
                    parentContainer = (NoLeafNode) parentContainer.getParent().getContainer();
                }
            }

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
