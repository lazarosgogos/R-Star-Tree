import java.util.*;

public class AlgorithmInsert {

    private static HashSet<Integer> levelOverflowStatus = new HashSet<>(/* number of levels of tree */);

    public static void insertData(PointEntry entry, final int M) {
        int leafLevel = 0;
        insert(leafLevel, entry, M);
    }

    public static void insert(int leafLevel, PointEntry entry, final int M) {
        Node root = null;
//        M = 4;
        /*TODO How do we keep track of each node's level ?
        *  Either we keep that piece of information inside the node in some short integer
        *  or we keep a list/hashmap in a local field with each node's ID and its corresponding level.
        *  I find the first solution to be the optimal one, as it's both easier to implement, and more
        *  straight-forward */
        boolean action = false;
        LeafNode N = (LeafNode) ChooseSubtree.chooseSubtree(root, entry);
        if (N.getPointEntries().size() < M) {
            N.addEntry(entry);
        }
        if (N.getPointEntries().size() == M) {
            action = overflowTreatment(N.getLevel());
        }
        if (action) { // if boolean var "action" is true, invoke reinsert
            reInsert(N, M);
        } else { // else invoke split
            // do split
        }
    }


    /**
     * This method decides whether a reinsertion or a split will occur. If true is returned, a reinsertion must happen.
     * If false is returned, a split must be put into place.
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


    public static void reInsert(Node N, final int M) {
//        Node N = null;
//        M = 4; // ??
        // TODO How are we supposed to reinsert the M+1 nodes
        //  when M is the maximum number of entries that can fit in the Node
        //  and it's possible that a reInsertion won't leave enough room for the final +1 entry
        //  that we want to insert? Or is it granted that it'll fit?
        if (N instanceof NoLeafNode) {
            // deprecate this, it's a bad tactic
            HashMap<RectangleEntry, Double> distances = new HashMap<RectangleEntry, Double>();
            // adopt this style
            ArrayList<RectangleEntryDoublePair> pairs = new ArrayList<>();

            for (RectangleEntry entry : ((NoLeafNode) N).getRectangleEntries()) { //RI1
                double distance = entry.getRectangle().getCenter().distance(N.getParent().getRectangle().getCenter());
                RectangleEntryDoublePair pair = new RectangleEntryDoublePair(entry, distance);
//                distances.put(entry, entry.getRectangle().getCenter().distance(N.parent.getRectangle().getCenter()));
                pairs.add(pair);
            }
            pairs.sort(new RectangleEntryDoublePairComparator()); // RI2
//            LinkedList<Double> list = new LinkedList<>(distances.values());
//            Collections.sort(list); //RI2
            int p = (int) (0.3 * M);
            List<RectangleEntryDoublePair> trash;
            /*for (int i = 0; i < p; i++) { //RI3
                trash.add(list.removeLast());
            }*/
            trash = pairs.subList(p, pairs.size()); //
            for (RectangleEntryDoublePair pair : trash) {
//                for (RectangleEntry key : distances.keySet()) {
//                    if (distances.get(key) == value) {

                // this is a recursive call, but I am not sure if it will work out that well!
                // maybe the reinsertion should not be recursive but iterative
                // RI4
                reInsert(N, pair.getRectangleEntry(), M); // this should invoke Insert and not reInsert!
//                    }
//                }
            }
        } else {
            //((LeafNode) N).getPointEntries()
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
}
