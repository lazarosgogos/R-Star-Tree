import java.util.*;

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
        double min = -1d;
        int selectedAxis = 0; // AXIS = DIMENSION
        for (int i = 0; i < dimensions; i++) { // for each AXIS !
            ArrayList<RectangleEntryDoublePair> startCoordsOfAxisI = new ArrayList<>(M);
            ArrayList<RectangleEntryDoublePair> endCoordsOfAxisI = new ArrayList<>(M);
            // now for each POINT
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
                int k = m - 1 + j; // taken straight from the R*-tree paper
                List<RectangleEntryDoublePair> groupOneStartPairs = startCoordsOfAxisI.subList(0, k);
                List<RectangleEntryDoublePair> groupTwoStartPairs = startCoordsOfAxisI.subList(k, startCoordsOfAxisI.size());

                List<RectangleEntryDoublePair> groupOneEndPairs = endCoordsOfAxisI.subList(0, k);
                List<RectangleEntryDoublePair> groupTwoEndPairs = endCoordsOfAxisI.subList(k, endCoordsOfAxisI.size());

                double goodnessGroupOneStartPairs = determineGoodnessValueMarginWise(groupOneStartPairs);
                double goodnessGroupTwoStartPairs = determineGoodnessValueMarginWise(groupTwoStartPairs);
                double goodnessGroupOneEndPairs = determineGoodnessValueMarginWise(groupOneEndPairs);
                double goodnessGroupTwoEndPairs = determineGoodnessValueMarginWise(groupTwoEndPairs);
                double sum = goodnessGroupOneStartPairs + goodnessGroupTwoStartPairs +
                        goodnessGroupOneEndPairs + goodnessGroupTwoEndPairs;
                if (min == -1d || min > sum) {
                    min = sum;
                    selectedAxis = i;
                }
                // now do something with choose split index
                // with different goodness values and such

            }
        }
        return selectedAxis;
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

    private static class RectangleEntryDoublePairComparator implements Comparator<RectangleEntryDoublePair> {
        @Override
        public int compare(RectangleEntryDoublePair o, RectangleEntryDoublePair t1) {
            return Double.compare(o.getValue(), t1.getValue());
        }
    }

    /**
     * Determine the entry/index at which the split will happen
     */
    public static int chooseSplitIndex(RectangleEntry parentRect, int M, int m, int axis) {
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
        double min = -1d;
        int selectedIndex = 0;
        // for the given axis/dimension
        ArrayList<RectangleEntryDoublePair> startCoordsOfGivenAxis = new ArrayList<>(M);
        ArrayList<RectangleEntryDoublePair> endCoordsOfGivenAxis = new ArrayList<>(M);
        // now for each POINT
        for (int j = 0; j < M; j++) {
            RectangleEntryDoublePair startPair = new RectangleEntryDoublePair(startingPoints.get(j).getRectangleEntry(), startingPoints.get(j).getPoint().getCoords().get(axis));
            RectangleEntryDoublePair endPair = new RectangleEntryDoublePair(endingPoints.get(j).getRectangleEntry(), endingPoints.get(j).getPoint().getCoords().get(axis));
            startCoordsOfGivenAxis.add(startPair);
            endCoordsOfGivenAxis.add(endPair);
        }
        Collections.sort(startCoordsOfGivenAxis, new RectangleEntryDoublePairComparator());
        Collections.sort(endCoordsOfGivenAxis, new RectangleEntryDoublePairComparator());
        // now determine all distributions. how?
        // for each sort, M - 2m + 2 distributions of the M+1 entries into two groups are determined
        // for the k-th distribution, where k is in [1, M-2m+2], the first group contains
        // the first m-1+k entries, the second group the remaining ones
        // for these two groups, goodness values are determined
        // now, in the current axis
        for (int j = 0; j < M - 2 * m + 2; j++) {
            int k = m - 1 + j; // taken straight from the R*-tree paper
            List<RectangleEntryDoublePair> groupOneStartPairs = startCoordsOfGivenAxis.subList(0, k);
            List<RectangleEntryDoublePair> groupTwoStartPairs = startCoordsOfGivenAxis.subList(k, startCoordsOfGivenAxis.size());

            List<RectangleEntryDoublePair> groupOneEndPairs = endCoordsOfGivenAxis.subList(0, k);
            List<RectangleEntryDoublePair> groupTwoEndPairs = endCoordsOfGivenAxis.subList(k, endCoordsOfGivenAxis.size());

            double goodnessGroupOneStartPairs = determineGoodnessValueOverlapWise(groupOneStartPairs);
            double goodnessGroupTwoStartPairs = determineGoodnessValueOverlapWise(groupTwoStartPairs);
            double goodnessGroupOneEndPairs = determineGoodnessValueOverlapWise(groupOneEndPairs);
            double goodnessGroupTwoEndPairs = determineGoodnessValueOverlapWise(groupTwoEndPairs);
            double sum = goodnessGroupOneStartPairs + goodnessGroupTwoStartPairs +
                    goodnessGroupOneEndPairs + goodnessGroupTwoEndPairs;
            if (min == -1d || min > sum) {
                min = sum;
                selectedIndex = j;
            } else if (min == sum) { // would this be a tie? How are ties defined?
                goodnessGroupOneStartPairs = determineGoodnessValueAreaWise(groupOneStartPairs);
                goodnessGroupTwoStartPairs = determineGoodnessValueAreaWise(groupTwoStartPairs);
                goodnessGroupOneEndPairs = determineGoodnessValueAreaWise(groupOneEndPairs);
                goodnessGroupTwoEndPairs = determineGoodnessValueAreaWise(groupTwoEndPairs);
                sum = goodnessGroupOneStartPairs + goodnessGroupTwoStartPairs +
                        goodnessGroupOneEndPairs + goodnessGroupTwoEndPairs;
                min = sum;
                selectedIndex = j;
            }
        }
        return selectedIndex;
    }

    // we will go with the margin value
    private static double determineGoodnessValueMarginWise(List<RectangleEntryDoublePair> entries) {
        double sum = 0;
        for (RectangleEntryDoublePair entry : entries) {
            Rectangle r = entry.getRectangleEntry().getRectangle();
            double margin = r.getMargin();
//            double area = r.getArea();
            sum += margin;
        }
        return sum;
    }

    private static double determineGoodnessValueOverlapWise(List<RectangleEntryDoublePair> entries) {
        double sum = 0;
        for (int i = 0; i < entries.size(); i++) {
            RectangleEntryDoublePair entry = entries.get(i);
            for (int j = i + 1; j < entries.size(); j++) {
                Rectangle r = entry.getRectangleEntry().getRectangle();
                RectangleEntryDoublePair otherEntry = entries.get(j);
                Rectangle otherRectangle = otherEntry.getRectangleEntry().getRectangle();
                double overlap = 0;
                if (r.overlaps(otherRectangle)) {
                    overlap = r.getOverlap(otherRectangle);
                }
//            double margin = r.getMargin();
//                double area = r.getArea();
                sum += overlap;
            }
        }
        return sum;
    }

    private static double determineGoodnessValueAreaWise(List<RectangleEntryDoublePair> entries) {
        double sum = 0;
        for (RectangleEntryDoublePair entry : entries) {
            Rectangle r = entry.getRectangleEntry().getRectangle();
//            double margin = r.getMargin();
            double area = r.getArea();
            sum += area;
        }
        return sum;
    }
}
