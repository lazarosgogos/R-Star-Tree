import java.util.*;

public class AlgorithmInsert {
    public static void insertData(PointEntry entry, final int M) {
        int leafLevel = 0;
        insert(leafLevel, entry, M);
    }

    public static void insert(int leafLevel, PointEntry entry, final int M) {
        Node root = null;
//        M = 4;
        boolean split = false;
        LeafNode N = (LeafNode) ChooseSubtree.chooseSubtree(root, entry);
        if (N.getPointEntries().size() < M) {
            N.addEntry(entry);
        }
        if (N.getPointEntries().size() == M) {
            split = overflowTreatment(leafLevel);
        }
        if (split) {
            //overflowTreatment() kai sta NoLeafNodes
        }
    }

    public static boolean overflowTreatment(int leafLevel) {
//        if () {
//            reInsert();
//        } else {
//            AlgorithmSplit.split();
        return true;
//        }
        //return false;
    }


    public static void reInsert(Node N, RectangleEntry e, final int M) {
//        Node N = null;
//        M = 4; // ??
        if (N instanceof NoLeafNode) {
            // deprecate this, it's a bad tactic
            HashMap<RectangleEntry, Double> distances = new HashMap<RectangleEntry, Double>();
            // adopt this style
            ArrayList<RectangleEntryDoublePair> pairs = new ArrayList<>();

            for (RectangleEntry entry : ((NoLeafNode) N).getRectangleEntries()) { //RI1
                double distance = entry.getRectangle().getCenter().distance(N.parent.getRectangle().getCenter());
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

                // this is a recursive call but I am not sure it will work out that well!
                // maybe the reinsertion should not be recursive but iterative
                reInsert(N, pair.getRectangleEntry(), M);
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
