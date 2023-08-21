import java.util.*;

public class ChooseSubtree {

//    /**
//     * @deprecated This method is inside the {@link Rectangle} class.
//     * @param r1 The first rectangle
//     * @param r2 The second rectangle
//     * @return The overlap between the two
//     */
//    @Deprecated
//    public static double overlapCalculation(Rectangle r1, Rectangle r2) {
//        int dimensions = r1.getStartPoint().getCoords().size();
//
//        double product = 1;
//        for (int i = 0; i < dimensions; i++) {
//            double[] array = {r2.getStartPoint().getCoords().get(i),
//                    r2.getEndPoint().getCoords().get(i),
//                    r1.getStartPoint().getCoords().get(i),
//                    r1.getEndPoint().getCoords().get(i)
//            };
//            Arrays.sort(array);
//            product *= array[2] - array[1];
//        }
//
//        return product;
//    }

    public static double totalOverlap(Rectangle r1, HashSet<RectangleEntry> rectangles) {
        double sum = 0;
        for (RectangleEntry rectangle : rectangles) {
            sum += r1.getOverlap(rectangle.getRectangle());
        }
        if (sum <= 0) {
            return 50000000;
        }
        return sum;
    }

    public static boolean contains(RectangleEntry r, PointEntry p) {
        return r.getRectangle().contains(p);
    }

    public static Rectangle enlargeRectangle(RectangleEntry r, PointEntry p) {
        // list of lists with coordinates
        ArrayList<ArrayList<Double>> elements = new ArrayList<>();

        int dimensions = p.getPoint().getCoords().size();
        ArrayList<Double> min = new ArrayList<>();
        ArrayList<Double> max = new ArrayList<>();

        // initialize the list of lists with empty lists
        for (int i = 0; i < dimensions; i++) {
            elements.add(new ArrayList<>());
            elements.get(i).add(r.getRectangle().getStartPoint().getCoords().get(i));
            elements.get(i).add(r.getRectangle().getEndPoint().getCoords().get(i));
            elements.get(i).add(p.getPoint().getCoords().get(i));
            min.add(RectangleEntry.min(elements.get(i)));
            max.add(RectangleEntry.max(elements.get(i)));
        }

        return new Rectangle(new Point(min), new Point(max));
    }

//    /**
//     * @deprecated
//     * This method is inside the {@link Rectangle} class
//     * @param r The rectangle whose area needs to be calculated
//     * @return The area of the given rectangle
//     */
//    @Deprecated
//    public static double getArea(Rectangle r) {
//        double product = 1;
//        int destinations = r.getStartPoint().getCoords().size();
//        for (int i = 0; i < destinations; i++) {
//            product *= r.getEndPoint().getCoords().get(i) - r.getStartPoint().getCoords().get(i);
//        }
//        return product;
//    }

    public static double areaEnlargementCost(RectangleEntry r, PointEntry p) {
        Rectangle re = enlargeRectangle(r, p);
        return re.getArea() - r.getRectangle().getArea();
    }

    public static Node chooseSubtree(Node root, PointEntry entry) {
        //CS1
        Node tempN = root;
        Node choosenEntry = new Node();

        //CS2
        while (tempN instanceof NoLeafNode) {
            NoLeafNode N = (NoLeafNode) tempN;
             /* First, sort the rectangles in N in increasing order
             of their area enlargement needed to include the new data rectangle
             Let A be the group of the first P entries
             From the entries in A, considering all entries in N, choose the entry
             whose rectangle needs the least overlap enlargement.
             Resolve ties as described above
             */
            if (N.getChildren().get(0) instanceof LeafNode) { // if the children of node N point to leaves
                HashSet<RectangleEntry> rectangleEntries = new HashSet<>(N.getRectangleEntries());
                HashMap<RectangleEntry, Double> overlapEnlargementScores = new HashMap<>();
                for (RectangleEntry rectangleEntry : rectangleEntries) {
                    // An apla anikei se yparxon tetragono kai den xreiazetai megethinsi
                    if (contains(rectangleEntry, entry)) {
                        choosenEntry = rectangleEntry.getChild();
                    } else { //calculate overlap enlargement for all possible enlargements
                        HashSet<RectangleEntry> temp = new HashSet<>(N.getRectangleEntries());
                        temp.remove(rectangleEntry);
                        overlapEnlargementScores.put(rectangleEntry, totalOverlap(enlargeRectangle(rectangleEntry, entry), temp));
                    }
                }
                double minOverlap = Collections.min(overlapEnlargementScores.values()); //find min
                ArrayList<RectangleEntry> minOverlapRectangles = new ArrayList<>();
                for (RectangleEntry key : overlapEnlargementScores.keySet()) { //find rectangle entries that are min
                    if (Double.compare(overlapEnlargementScores.get(key), minOverlap) == 0) {
                        minOverlapRectangles.add(key);
                    }
                }
                // TODO will this ever happen? Due to floating point number comparisons
                // https://stackoverflow.com/questions/8081827/how-to-compare-two-double-values-in-java
                if (minOverlapRectangles.size() > 1) { //if there are ties
                    HashMap<RectangleEntry, Double> areaEnlargementScores = new HashMap<>();
                    for (RectangleEntry rectangleEntry : minOverlapRectangles) { // calculate all possible area enlargements
                        areaEnlargementScores.put(rectangleEntry, areaEnlargementCost(rectangleEntry, entry));
                    }
                    double minAreaEnlargement = Collections.min(areaEnlargementScores.values()); //find min
                    ArrayList<RectangleEntry> minAreaEnlargementRectangles = new ArrayList<>();
                    for (RectangleEntry key : areaEnlargementScores.keySet()) {
                        if (Double.compare(areaEnlargementScores.get(key), minAreaEnlargement) == 0) { // find rectangle entries that are min
                            minAreaEnlargementRectangles.add(key);
                        }
                    }
                    if (minAreaEnlargementRectangles.size() > 1) { //if there are ties in area enlargement
                        HashMap<RectangleEntry, Double> areaScores = new HashMap<>();
                        for (RectangleEntry rectangleEntry : minAreaEnlargementRectangles) { // calculate area of all
                            areaScores.put(rectangleEntry, rectangleEntry.getRectangle().getArea());
                        }
                        double minArea = Collections.min(areaScores.values());
                        for (RectangleEntry key : areaScores.keySet()) {
                            if (Double.compare(areaScores.get(key), minArea) == 0) {
                                choosenEntry = key.getChild(); // choose rectangle with smallest area
                            }
                        }
                    } else { // if there is only one min area enlargement
                        choosenEntry = minAreaEnlargementRectangles.get(0).getChild();
                    }
                } else { //if there is only one min overlap enlargement
                    choosenEntry = minOverlapRectangles.get(0).getChild();
                }
            } else { // if the children of node N do NOT point to leaves
                HashSet<RectangleEntry> rectangleEntries = new HashSet<>(N.getRectangleEntries());
                HashMap<RectangleEntry, Double> areaEnlargementScores = new HashMap<>();
                for (RectangleEntry rectangleEntry : rectangleEntries) { // calculate all possible area enlargements
                    areaEnlargementScores.put(rectangleEntry, areaEnlargementCost(rectangleEntry, entry));
                }
                double minAreaEnlargement = Collections.min(areaEnlargementScores.values()); //find min
                ArrayList<RectangleEntry> minAreaEnlargementRectangles = new ArrayList<>();
                for (RectangleEntry key : areaEnlargementScores.keySet()) {
                    if (Double.compare(areaEnlargementScores.get(key), minAreaEnlargement) == 0) { // find rectangle entries that are min
                        minAreaEnlargementRectangles.add(key);
                    }
                }
                if (minAreaEnlargementRectangles.size() > 1) { //if there are ties in area enlargement
                    HashMap<RectangleEntry, Double> areaScores = new HashMap<>();
                    for (RectangleEntry rectangleEntry : minAreaEnlargementRectangles) { // calculate area of all
                        areaScores.put(rectangleEntry, rectangleEntry.getRectangle().getArea());
                    }
                    double minArea = Collections.min(areaScores.values());
                    for (RectangleEntry key : areaScores.keySet()) {
                        if (Double.compare(areaScores.get(key), minArea) == 0) {
                            choosenEntry = key.getChild(); // choose rectangle with smallest area
                        }
                    }
                } else { // if there is only one min area enlargement
                    choosenEntry = minAreaEnlargementRectangles.get(0).getChild();
                }
            }
            //CS3
            tempN = choosenEntry;
        }
        return tempN;
    }
}
