import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;

public class KNNQuery {
    private static class PointComparator implements Comparator<PointPointPair> {


        @Override
        public int compare(PointPointPair pair1, PointPointPair pair2) {

            double d1 = pair1.distance(); // distance of p1 from start of axes
            double d2 = pair2.distance(); // distance of p2 from start of axes
            return Double.compare(d2, d1);
        }

        @Override
        public Comparator<PointPointPair> reversed() {
            return Comparator.super.reversed();
        }
    }

    private static class PointPointPair {
        private Point point1, point2;

        public PointPointPair(Point p1, Point p2) {
            this.point1 = p1;
            this.point2 = p2;
        }

        public Point getPoint1() {
            return point1;
        }

        public Point getPoint2() {
            return point2;
        }

        public double distance() {
            double x2 = point2.getX();
            double x1 = point1.getX();
            double y2 = point2.getY();
            double y1 = point1.getY();
            return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
        }

        @Override
        public String toString() {
            return new StringBuilder().
                    append("PointPair{").
                    append("point1=").
                    append(point1).
                    append(", point2=").
                    append(point2).
                    append('}').
                    toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PointPointPair that = (PointPointPair) o;
            return Objects.equals(point1, that.point1) && Objects.equals(point2, that.point2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(point1, point2);
        }
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
    public static PriorityQueue<PointPointPair> knnQuery(final Node root, final Point center, final int k) {
        PriorityQueue<PointPointPair> maxHeap = new PriorityQueue<>(k, new PointComparator());
//        LinkedList<RectangleEntry> searchFront = new LinkedList<>();
        maxHeap = _knnQuery(root, center, k, maxHeap);
        System.out.println("Knn query for size: " + k);
        maxHeap.forEach(p -> System.out.println(p.toString()));
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
    public static PriorityQueue<PointPointPair> _knnQuery(final Node root, final Point center,
                                                          final int k, PriorityQueue<PointPointPair> maxHeap) {
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

        ArrayList<Node> searchFront = new ArrayList<>();
        searchFront.add(root);
        /*for (final ListIterator<Node> iterator = searchFront.listIterator(); iterator.hasNext();) */
        for (int i = 0; i < searchFront.size(); i++) {
            boolean addedSomethingNew = false;
            Node node = searchFront.get(i);
            /*Node node = iterator.next();*/
            if (!node.leaf) { // if the root node is not a leaf
                // find the closest rectangle
                NoLeafNode noleaf = (NoLeafNode) node;
                if (maxHeap.isEmpty()) { // if we have no available value of maximum distance
                    RectangleEntry chosen = noleaf.getRectangleEntries().get(0); // get a base estimate
                    for (RectangleEntry rectangleEntry : noleaf.getRectangleEntries()) {
                        // find the minimum possible distance
                        if (center.distance(rectangleEntry) < center.distance(chosen)) { // iterate over all rectangles and get the minimum distance
                            chosen = rectangleEntry; // choose it
                        }
                    }
                    searchFront.add(chosen.getChild());
                    addedSomethingNew = true;

                } else {
//                   // if we already have something in the max heap
                    // add rectangles which are IN the radius
                    for (RectangleEntry rectangleEntry : noleaf.getRectangleEntries()) {
                        if (center.distance(rectangleEntry) < maxHeap.peek().distance()) {
                            searchFront.add(rectangleEntry.getChild());
                            addedSomethingNew = true;
                        }
                    }
//                    System.out.println("something is already in max heap");
//
                }
//                System.out.println("Reached in non leaf node! Line 326");
            } else { // if we're dealing with a leaf node
                LeafNode leaf = (LeafNode) node;
                addedSomethingNew = false;
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
                        PointPointPair cp = new PointPointPair(center, pointEntry.getPoint());
                        if (/*maxHeap.peek().distance() > cp.distance() &&*/ maxHeap.size() < k && !maxHeap.contains(cp)) {
                            maxHeap.add(cp);
//                            System.out.println("added due to hallf empty heap " + cp);
                            addedSomethingNew = true;
                        }
                    }
                } /*else*/

                // if the max heap is full, and we have found another point
                // of which its distance is LESS THAN the head of the  max heap,
                // replace the head with that point
                boolean changedSomething = true;
                while (changedSomething) {
                    changedSomething = false;
                    for (PointEntry pointEntry : leaf.getPointEntries()) {
                        PointPointPair cp = new PointPointPair(center, pointEntry.getPoint());
                        if (maxHeap.peek().distance() > cp.distance() && !maxHeap.contains(cp)) {
                            PointPointPair par = maxHeap.poll();
//                            System.out.println("Polled " + par);
                            maxHeap.add(cp);
//                            System.out.println("added after polling: " + cp);
                            addedSomethingNew = true;
                            changedSomething = true;
                        }
                    }
//                    System.out.println("Something changed, let's try again");
                }
                // now after all these ifs, there is certainly something in the max heap
                // add nodes in the search front to check next time
//                System.out.println("Reached in leaf node! line 354");
                if (!root.leaf) {
                    for (RectangleEntry rectangleEntry : ((NoLeafNode) root).getRectangleEntries()) {
                        if (center.distance(rectangleEntry) < maxHeap.peek().distance() && !searchFront.contains(rectangleEntry.getChild())) {
                            searchFront.add(rectangleEntry.getChild());
                        }
                    }
                }

            }
//            if (!addedSomethingNew)
//                break;
        }


        return maxHeap;
    }
}
