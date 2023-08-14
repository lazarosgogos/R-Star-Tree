import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * What we want to do is split a Rectangle into two rectangles
 * And we must find the best way to do so
 */
public class AlgorithmSplit {


    public static Node split(RectangleEntry rect) {
//        int splitAxis = chooseSplitAxis();
        return null;
    }

    /**
     * Determine which axis/dimension should be selected by calculating goodness values
     */
    public static int chooseSplitAxis(RectangleEntry parentRect, int M, int m) {
        // for each axis/dimension! --> we need an arraylist (A) of arraylists (B)
        // size of A -> # of dimensions
        // A holds D arraylists
        // each array list has the starting points' values of the axis at index i
        // and for each such array we need to sort it
        int dimensions = parentRect.getRectangle().getStartPoint().getCoords().size();
        // let's make this as clear as day

        if (parentRect.getChild().leaf) return 0; // TODO  we'll see!
        // else:
        NoLeafNode node;
        node = (NoLeafNode) parentRect.getChild();

        ArrayList<RectanglePointPair> startingPoints = new ArrayList<>(dimensions);
        ArrayList<RectanglePointPair> endingPoints = new ArrayList<>(dimensions);
        // first get all start and end points
        for (RectangleEntry rectangleEntry : node.getRectangleEntries()) {
            Point startPoint = rectangleEntry.getRectangle().getStartPoint();
            Point endPoint = rectangleEntry.getRectangle().getEndPoint();

            startingPoints.add(new RectanglePointPair(rectangleEntry, startPoint));
            endingPoints.add(new RectanglePointPair(rectangleEntry, endPoint));
        }

        // int M = startingPoints.size(); // M is the number of POINTS we have, or the number of axes
        for (int i = 0; i < dimensions; i++) {
            ArrayList<RectangleEntryDoublePair> startCoordsOfAxisI = new ArrayList<>(M);
            ArrayList<RectangleEntryDoublePair> endCoordsOfAxisI = new ArrayList<>(M);
            // now for each axis
            for (int j = 0; j < M; j++) {
                RectangleEntryDoublePair startPair = new RectangleEntryDoublePair(startingPoints.get(j).getRectangleEntry(), startingPoints.get(j).getPoint().getCoords().get(i));
                RectangleEntryDoublePair endPair = new RectangleEntryDoublePair(endingPoints.get(j).getRectangleEntry(), endingPoints.get(j).getPoint().getCoords().get(i));
                startCoordsOfAxisI.add(startPair);
                endCoordsOfAxisI.add(endPair);
            }
            Collections.sort(startCoordsOfAxisI, new RectangleEntryDoublePairComparator());
            Collections.sort(endCoordsOfAxisI, new RectangleEntryDoublePairComparator());
            // now determine all distributions. how?
            // for each sort, M - 2m + 2 distributions of the M+1 entries into two groups are determined
            // for the k-th distribution, where k is in [1, M-2m+2], the first group contains
            // the first m-1+k entries, the second group the remaining ones
            // for these two groups, goodness values are determined
            // now, in the current axis
            for (int j = 0; j < M - 2 * m + 2; j++) {

            }
        }
        return 0;
    }

    private static class RectanglePointPair {
        private RectangleEntry rectangleEntry;
        private Point point;

        public RectanglePointPair(RectangleEntry rectangleEntry, Point point) {
            this.rectangleEntry = rectangleEntry;
            this.point = point;
        }

        public RectangleEntry getRectangleEntry() {
            return rectangleEntry;
        }

        public Point getPoint() {
            return point;
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

    private static class RectangleEntryDoublePairComparator implements Comparator<RectangleEntryDoublePair>{
        @Override
        public int compare(RectangleEntryDoublePair o, RectangleEntryDoublePair t1) {
            return Double.compare(o.getValue(), t1.getValue());
        }
    }

    /**
     * Determine the entry/index at which the split will happen
     */
    public static int chooseSplitIndex(RectangleEntry rect) {
        return 0;
    }

    // we will go with the minimum area value
    private static double determineGoodnessValue(ArrayList<RectangleEntry> entries){
        return 0;
    }

}
