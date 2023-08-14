import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    public static int chooseSplitAxis(RectangleEntry rect, int M, int m) {
        // for each axis/dimension! --> we need an arraylist (A) of arraylists (B)
        // size of A -> # of dimensions
        // A holds D arraylists
        // each array list has the starting points' values of the axis at index i
        // and for each such array we need to sort it
        int dimensions = rect.getRectangle().getStartPoint().getCoords().size();
        // let's make this as clear as day

        if (rect.getChild().leaf) return 0; // TODO  we'll see!
        // else:
        NoLeafNode node;
        node = (NoLeafNode) rect.getChild();

        ArrayList<Point> startingPoints = new ArrayList<>(dimensions);
        ArrayList<Point> endingPoints = new ArrayList<>(dimensions);
        // first get all start and end points
        for (RectangleEntry rectangleEntry : node.getRectangleEntries()) {
            startingPoints.add(rectangleEntry.getRectangle().getStartPoint());
            endingPoints.add(rectangleEntry.getRectangle().getEndPoint());
        }

        // int M = startingPoints.size(); // M is the number of POINTS we have, or the number of axes
        for (int i = 0; i < dimensions; i++) {
            ArrayList<Double> startCoordsOfAxisI = new ArrayList<>(M);
            ArrayList<Double> endCoordsOfAxisI = new ArrayList<>(M);
            // now for each axis
            for (int j = 0; j < M; j++) {
                startCoordsOfAxisI.add(startingPoints.get(j).getCoords().get(i));
                endCoordsOfAxisI.add(endingPoints.get(j).getCoords().get(i));
            }
            Collections.sort(startCoordsOfAxisI);
            Collections.sort(endCoordsOfAxisI);
            // now determine all distributions. how?
            // for each sort, M - 2m + 2 distributions of the M+1 entries into two groups are determined
            // for the k-th distribution, where k is in [1, M-2m+2], the first group contains
            // the first m-1+k entries, the second group the remaining ones
            // for these two groups, goodness values are determined

        }
        return 0;
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
