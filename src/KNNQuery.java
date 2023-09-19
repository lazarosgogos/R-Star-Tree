import java.util.*;

public class KNNQuery {
    private static class PointComparator implements Comparator<PointPointEntriesPair> {
        @Override
        public int compare(PointPointEntriesPair pair1, PointPointEntriesPair pair2) {

            double d1 = pair1.distance(); // distance of two points in pair 1
            double d2 = pair2.distance(); // distance of two points in pair 2
            return Double.compare(d2, d1);
        }

        @Override
        public Comparator<PointPointEntriesPair> reversed() {
            return Comparator.super.reversed();
        }
    }

    private static class PointPointEntriesPair {
        private PointEntry point1, point2;

        public PointPointEntriesPair(PointEntry p1, PointEntry p2) {
            this.point1 = p1;
            this.point2 = p2;
        }

        public PointEntry getPoint1() {
            return point1;
        }

        public PointEntry getPoint2() {
            return point2;
        }

        public double distance() {
            /* Legacy code
            double x2 = point2.getX();
            double x1 = point1.getX();
            double y2 = point2.getY();
            double y1 = point1.getY();
            return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));*/
            return point1.getPoint().distance(point2.getPoint());
        }

        @Override
        public String toString() {
            return new StringBuilder().
//                    append(point2.getRecord_ID()).
                    append("PointPair{").
                    append("point1=").
                    append(point1).
                    append(", point2=").
                    append(point2).
                    append('}').
                    toString();
        }

        public String getDetails(){
            return point2.getPoint().toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PointPointEntriesPair that = (PointPointEntriesPair) o;
            return Objects.equals(point1, that.point1) && Objects.equals(point2, that.point2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(point1, point2);
        }
    }

    private static class PointRectanglePair {
        private PointEntry point;
        private RectangleEntry rect;

        public PointRectanglePair(PointEntry point, RectangleEntry rect) {
            this.point = point;
            this.rect = rect;
        }

        public PointEntry getPointEntry() {
            return point;
        }

        public RectangleEntry getRectangleEntry() {
            return rect;
        }
        public double distance() {
            /* Legacy code
            double x2 = point2.getX();
            double x1 = point1.getX();
            double y2 = point2.getY();
            double y1 = point1.getY();
            return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));*/
            return point.getPoint().distance(rect);
        }
    }
    private static class PointRectangleComparator implements Comparator<PointRectanglePair> {
        @Override
        public int compare(PointRectanglePair pair1, PointRectanglePair pair2) {

            double d1 = pair1.distance(); // distance of two points in pair 1
            double d2 = pair2.distance(); // distance of two points in pair 2
            return Double.compare(d2, d1);
        }

        @Override
        public Comparator<PointRectanglePair> reversed() {
            return Comparator.super.reversed();
        }
    }

    public static ArrayList<Point> knnSerialQuery(final Point center, LinkedList<Point> entries, final int k) {
        PointEntry dummyCenter = new PointEntry(center, "");
        ArrayList<PointPointEntriesPair> pairs = new ArrayList<>();
        for (Point p : entries) {
            PointEntry dummy = new PointEntry(p, "dummy");
            PointPointEntriesPair pair = new PointPointEntriesPair(dummyCenter, dummy);
            pairs.add(pair);
        }
        pairs.sort(new PointComparator().reversed());
//        pairs = (ArrayList<PointPointEntriesPair>) pairs.subList(0, k);
        ArrayList<Point> results = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            PointPointEntriesPair pair = pairs.get(i);
            results.add(pair.getPoint2().getPoint());
        }

        results.forEach(p -> System.out.println(p));
        return results;
    }

    /**
     * This method performs the infamous k-nearest-neighbors algorithm iteratively
     * and returns a hash set that contains them
     *
     * @param root   The root node to start the search from
     * @param center The point from which we are trying to find the nearest neighbors from
     * @param k      The number of nearest neighbors that should be found
     * @return       A max heap with all the nearest neighbors
     */
    public static PriorityQueue<PointPointEntriesPair> knnQuery(final Node root, final Point center, final int k) {
        PriorityQueue<PointPointEntriesPair> maxHeap = new PriorityQueue<>(k, new PointComparator());
//        LinkedList<RectangleEntry> searchFront = new LinkedList<>();
        PointEntry dummyEntry = new PointEntry(center, "dummy entry");
        _knnQuery(root, dummyEntry, k, maxHeap);
//        System.out.println("Knn query for size: " + k);
        //maxHeap.forEach(p -> System.out.println(p.getPoint2().getPoint()));
        maxHeap.forEach(p -> System.out.println(IO.loadRecordFromFile(p.getPoint2().getRecord_ID())));
        return maxHeap;
    }

    /**
     * This method performs the infamous k-nearest-neighbors algorithm iteratively
     * and returns a hash set that contains them
     *
     * @param root   The root node to start the search from
     * @param center The point from which we are trying to find the nearest neighbors from
     * @param k      The number of nearest neighbors that should be found
     * @return       A max heap with all the nearest neighbors
     */
    private static PriorityQueue<PointPointEntriesPair> _knnQuery(final Node root, final PointEntry center,
                                                                  final int k, PriorityQueue<PointPointEntriesPair> maxHeap) {
        // if node is noleaf
        // find the closest rectangle and add it to the search front
        // the question now is, how many rectangles should we put in the search front ?
        // perform knn query in search front

        // if node is leaf
        // find the closest point
        // add it to results
        // update max distance (why??? doesn't the maxHeap take care of that?)
        // if the distance from the point we're checking currently is LESS than the
        // distance from the top element of the max heap
        // pop the top element from the max heap
        // insert the currently explored point to the max heap
        // suddenly, we have a maximum distance!
        //System.out.println("KNN QUERY - ITERATIVE\n\n\n");
        ArrayList<Node> searchFront = new ArrayList<>();
        searchFront.add(root);
        /*for (final ListIterator<Node> iterator = searchFront.listIterator(); iterator.hasNext();) */
        for (int i = 0; i < searchFront.size(); i++) {
            Node node = searchFront.get(i);
            /*Node node = iterator.next();*/
            if (!node.leaf) { // if the root node is not a leaf
                // find the closest rectangle
                NoLeafNode noleaf = (NoLeafNode) node;
                if (maxHeap.size() < k) { // if we have no available value of maximum distance
//                    RectangleEntry chosen = noleaf.getRectangleEntries().get(0); // get a base estimate
                    // essentially, if the maxHeap is not yet full, but nor is it empty
                    // we need to keep adding nodes for search, sorted by their distance from the center point
                    ArrayList<PointRectanglePair> pointRectanglePairs = new ArrayList<>();
                    for (RectangleEntry rectangleEntry : noleaf.getRectangleEntries()) {
                        // find the minimum possible distance
                        // between the give center point and the rectangles of the current node.
                        // iterate over all rectangles and get the minimum distance
                        /*if (center.getPoint().distance(rectangleEntry) < center.getPoint().distance(chosen)) {
                            chosen = rectangleEntry; // choose it
                        }*/
                        PointRectanglePair prpair = new PointRectanglePair(center, rectangleEntry);
                        pointRectanglePairs.add(prpair);
                        //System.out.println(" noleaf - maxHeap size < k Adding prpair " + prpair.getPointEntry().toString() + ' ' + prpair.getRectangleEntry().toString());
                    }
                    pointRectanglePairs.sort(new PointRectangleComparator());
                    for (PointRectanglePair p: pointRectanglePairs) {
                        if (!searchFront.contains(p.getRectangleEntry().getChild())) {
                            searchFront.add(p.getRectangleEntry().getChild());

                        }
                    }
//                    searchFront.add(chosen.getChild());
                } /*else if (maxHeap.size() < k){ // else if the maxHeap is not yet full, but nor is it empty
                    // we need to keep adding nodes for search, sorted by their distance from the center point


                } */else if (maxHeap.size() == k){
//                   // if the max heap is full
                     // add rectangles which are IN the radius
                    for (RectangleEntry rectangleEntry : noleaf.getRectangleEntries()) {
                        if (center.getPoint().distance(rectangleEntry) < maxHeap.peek().distance() &&
                                !searchFront.contains(rectangleEntry.getChild())) {
                            searchFront.add(rectangleEntry.getChild());
                        }
                    }
//                    System.out.println("something is already in max heap");
//
                }

//                System.out.println("Reached in non leaf node! Line 326");
            } else { // if we're dealing with a leaf node
                LeafNode leaf = (LeafNode) node;
                /*if (maxHeap.isEmpty()) { // if we have no point yet
                    PointEntry p = leaf.getPointEntries().get(0);
                    PointPointPair pair = new PointPointPair(center, leaf.getPointEntries().get(0).getPoint());
                    maxHeap.add(pair);
                    System.out.println("adding  due to  EMPTY heap " + pair);
                    addedSomethingNew = true;
                }*/
                if (maxHeap.size() < k) {
                    for (PointEntry pointEntry : leaf.getPointEntries()) {
                        // iterate over all point entries. find the ones that have distance LESS THAN the
                        // head of the max heap
                        PointPointEntriesPair cp = new PointPointEntriesPair(center, pointEntry);
                        if (/*maxHeap.peek().distance() > cp.distance() &&*/ maxHeap.size() < k && !maxHeap.contains(cp)) {
                            maxHeap.add(cp);
                            //System.out.println("added due to hallf empty heap " + cp);
                        }
                    }
                } /*else*/

                // if the max heap is full, and we have found another point
                // of which its distance is LESS THAN the head of the  max heap,
                // replace the head with that point
                if (maxHeap.size() == k) {
                    boolean changedSomething = true;
                    while (changedSomething) {
                        changedSomething = false;
                        for (PointEntry pointEntry : leaf.getPointEntries()) {
                            PointPointEntriesPair cp = new PointPointEntriesPair(center, pointEntry);
                            if (maxHeap.peek().distance() > cp.distance() && !maxHeap.contains(cp)) {
                                PointPointEntriesPair par = maxHeap.poll();
                           // System.out.println("Polled " + par);
                                maxHeap.add(cp);
                            //System.out.println("added after polling: " + cp);
                                //maxHeap.forEach(p -> System.out.println(p.getPoint2().getPoint().toString()));
                                changedSomething = true;
                             //   System.out.println("Something changed, let's try again");
                            }
                        }
                    }
                }
                // now after all these ifs, there is certainly something in the max heap
                // add nodes in the search front to check next time
               // System.out.println("Reached in leaf node! line 354");
                if (!root.leaf) {
                    for (RectangleEntry rectangleEntry : ((NoLeafNode) root).getRectangleEntries()) {
                        if (center.getPoint().distance(rectangleEntry) < maxHeap.peek().distance() && !searchFront.contains(rectangleEntry.getChild())) {
                            searchFront.add(rectangleEntry.getChild());
                        }
                    }
                }

            }
        }


        return maxHeap;
    }
}
