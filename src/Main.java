import java.util.*;

public class Main {

    public static void main(String[] args) {
        // Create a list of points
//        List<Point> points = new ArrayList<>();
//        points.add(new Point(7, 7));
//        points.add(new Point(4, 6));
//        points.add(new Point(7, 3));
//        points.add(new Point(3, 1));
//
//        // Sort the points using Z-order curve
//        List<Point> sortedPoints = ZOrderCurveSort.sortPoints(points);
//
//        // Print the sorted points
//        for (Point point : sortedPoints) {
//            System.out.println(point);

        //725 records
        LinkedList<PointEntry> entries = IO.loadInput("assets/mapTestInts.osm");

//        for (PointEntry pe : entries){
//            System.out.println(pe.getPoint());
//        }

//        //203
//        entries.addAll(IO.loadInput("assets/mapCenter.osm"));
//
//        //623
//        entries.addAll(IO.loadInput("assets/mapUnis.osm"));
//
//        //725
//        entries.addAll(IO.loadInput("assets/mapEast.osm"));

//        //2276 total

        List<PointEntry> sortedEntries = ZOrderCurveSort.sortPoints(entries);

//        System.out.println("/");
//        for (PointEntry pe : sortedEntries){
//            System.out.println(pe.getPoint());
//        }

        int M = 4;
//        int num_of_nodes = entries.size() / M + 1;

//        LinkedList<LeafNode> leafNodesList = new LinkedList<>(); //list with level 0 nodes
//        LinkedList<RectangleEntry> rectangleEntriesList = new LinkedList<>(); //list for the next iteration, 1 rectangle entry in [level 1 node] is 1 [level 0 node]
//
//        Iterator<PointEntry> iterator = sortedEntries.iterator(); //list with sorted point entries to fill level 0 nodes
//        for (int i = 0; i < num_of_nodes; i++) { //posa nodes
//            while (iterator.hasNext()) {
//                LinkedList<PointEntry> tempList = new LinkedList<>(); //list with M elements
//
//                for (int j = 0; j < M; j++) { //16ades
//                    if (iterator.hasNext()) tempList.add(iterator.next());
//                }
//
//                LeafNode tempNode = new LeafNode(tempList); //create leaf node with M entries
//                for (PointEntry pointEntry : tempList) { //update node's entries about their container
//                    pointEntry.setContainer(tempNode);
//                }
//
//                leafNodesList.add(tempNode); // add leaf node to list with level 0 nodes
//
//                RectangleEntry tempRect = new RectangleEntry(tempNode); //create rectangle and connect it with leaf node
//                tempNode.setParent(tempRect); //connect leaf node with rectangle
//
//                rectangleEntriesList.add(tempRect); //add rectangle to list with level 0 rectangles
//            }
//        }
//        System.out.println(leafNodesList.size());

//        int N = leafNodesList.size() / M + 1;
//        LinkedList<RectangleEntry> rectangleEntriesList2 = new LinkedList<>();
//        LinkedList<NoLeafNode> noLeafNodesList = new LinkedList<>();
//
//        //dimioyrgo mia lista me NoLeafNodes kai mia lista me rectangles
//        Iterator<RectangleEntry> iterator2 = rectangleEntriesList.iterator();
//        for (int i = 0; i < N; i++) { //posa nodes
//            while (iterator2.hasNext()) {
//                LinkedList<RectangleEntry> tempList = new LinkedList<>();
//                for (int j = 0; j < M; j++) { //16ades
//                    if (iterator2.hasNext()) tempList.add(iterator2.next());
//                }
//
//                NoLeafNode tempNode = new NoLeafNode(tempList);
//                for (RectangleEntry rectangleEntry : tempList) {
//                    rectangleEntry.setContainer(tempNode);
//                }
//                noLeafNodesList.add(tempNode);
//
//                LinkedList<Node> temp2 = new LinkedList<>();
//
//                for (RectangleEntry rectangleEntry : tempNode.getRectangleEntries()) {
//                    temp2.add(rectangleEntry.getChild());
//                }
//                tempNode.setChildren(temp2);
//
//                RectangleEntry tempRect = new RectangleEntry(tempNode);
//                tempNode.setParent(tempRect);
//                rectangleEntriesList2.add(tempRect);
//            }
//        }
//
//        System.out.println(noLeafNodesList.size());

//        int N2 = noLeafNodesList.size() / M + 1;
//        LinkedList<RectangleEntry> rectangleEntriesList3 = new LinkedList<>();
//        LinkedList<NoLeafNode> noLeafNodesList2 = new LinkedList<>();
//        //dimioyrgo mia lista me NoLeafNodes kai mia lista me rectangles
//        Iterator<RectangleEntry> iterator3 = rectangleEntriesList2.iterator();
//        for (int i = 0; i < N2; i++) { //posa nodes
//            while (iterator3.hasNext()) {
//                LinkedList<RectangleEntry> tempList = new LinkedList<>();
//                for (int j = 0; j < M; j++) { //16ades
//                    if (iterator3.hasNext()) tempList.add(iterator3.next());
//                }
//
//                NoLeafNode tempNode = new NoLeafNode(tempList);
//                for (RectangleEntry rectangleEntry : tempList) {
//                    rectangleEntry.setContainer(tempNode);
//                }
//                noLeafNodesList2.add(tempNode);
//
//                LinkedList<Node> temp2 = new LinkedList<>();
//
//                for (RectangleEntry rectangleEntry : tempNode.getRectangleEntries()) {
//                    temp2.add(rectangleEntry.getChild());
//                }
//                tempNode.setChildren(temp2);
//
//                RectangleEntry tempRect = new RectangleEntry(tempNode);
//                tempNode.setParent(tempRect);
//                rectangleEntriesList3.add(tempRect);
//            }
//        }
//
//        System.out.println(noLeafNodesList2.size());

        LinkedList<LinkedList> leaves = bottomUpLevel0(sortedEntries, M);

        LinkedList<LinkedList> rootNodes = iterative(leaves.get(0), M, leaves.get(1));

        Node root = new Node();
        for (NoLeafNode noLeafNode : (LinkedList<NoLeafNode>) rootNodes.get(0)) {
            root = noLeafNode;
            noLeafNode.setRoot(true);
        }

        //dfs(root,0);
//        BBS.runSkyline(root).forEach(pe -> System.out.println(pe.getPoint()));
        Point p = new Point(5, 5);
        knnQuery(root, p, 4);

      /* System.out.println("Now running range query");
        Rectangle myQuery = new Rectangle(40.60987f, 22.96f, 40.61f, 22.967f);
        rangeQuery(root, myQuery);*/
    }

    /**
     * Why does this class exist, you say?
     * This class is used in the recursive part of the knnQuery
     * In Java a value cannot be passed by reference, only by value
     * Hence a wrapper as such is needed to ensure that we are able to find the
     * maximum distance and perform the KNN query.
     * It's a very simple class with only one variable, a constructor, a getter and a setter.
     */
    private static class DoubleWrapper {

        private double dist;

        DoubleWrapper(double d) {
            dist = d;
        }

        public double getDist() {
            return dist;
        }

        public void setDist(double dist) {
            this.dist = dist;
        }


    }

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
            return Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
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
    }

    private static class PointRectanglePair {
        private RectangleEntry r;
        private Point p;

        public PointRectanglePair(RectangleEntry r, Point p) {
            this.r = r;
            this.p = p;
        }

        public double distance() {
            return p.distance(r);
        }
    }

    private static class RectangleEntryComparator implements Comparator<PointRectanglePair> {


        @Override
        public int compare(PointRectanglePair pr1, PointRectanglePair pr2) {
            return Double.compare(pr1.distance(), pr2.distance());
        }

        @Override
        public Comparator<PointRectanglePair> reversed() {
            return Comparator.super.reversed();
        }
    }

    public static PriorityQueue<PointPointPair> knnQuery(final Node root, final Point center, final int k) {
        PriorityQueue<PointPointPair> maxHeap = new PriorityQueue<>(k, new PointComparator());
//        LinkedList<RectangleEntry> searchFront = new LinkedList<>();
        _knnQuery(root, center, k, maxHeap);
        System.out.println("Knn query for size: " + k);
        maxHeap.forEach(p -> System.out.println(p.toString()));
        return maxHeap;
    }

    /**
     * This method performs the infamous k-nearest-neighbors algorithm  recursively
     * and returns a hash set that contains them
     *
     * @param root
     * @param center
     * @param k      The number of nearest neighbors that should be found
     * @return
     */
    public static HashSet<Point> _knnQuery(final Node root, final Point center,
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

        // TODO this _knnQuery method does NOT work yet
//        boolean addedSomethingNew = false;
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
                            System.out.println("added due to hallf empty heap " + cp);
                            addedSomethingNew = true;
                        }
                    }
                } /*else*/

                    // if the max heap is full, and we have found another point
                    // of which its distance is LESS THAN the head of the  max heap,
                    // replace the head with that point
                    for (PointEntry pointEntry : leaf.getPointEntries()) {
                        PointPointPair cp = new PointPointPair(center, pointEntry.getPoint());
                        if (maxHeap.peek().distance() > cp.distance() && !maxHeap.contains(cp)) {
                            PointPointPair par = maxHeap.poll();
                            System.out.println("Polled " + par);
                            maxHeap.add(cp);
                            System.out.println("added after polling: " + cp);
                            addedSomethingNew = true;
                        }
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
            if (!addedSomethingNew)
                break;
        }


/*
        if (!root.leaf) {// if the node is not a leaf
            // find the closest rectangle
            NoLeafNode node = (NoLeafNode) root;
            LinkedList<RectangleEntry> rectangleEntries = node.getRectangleEntries();
            if (maxHeap.isEmpty()) {
                // search for the closest rectangle
                final DoubleWrapper distance = new DoubleWrapper(-1);

                RectangleEntry nextRect = rectangleEntries.get(0);
                for (RectangleEntry r : rectangleEntries) {
                    if (distance.getDist() == -1 || center.distance(r) < center.distance(nextRect)) {
                        // update minimum distance
                        distance.setDist(center.distance(r));
                        // update the next rectangle that will be checked
                        nextRect = r;
                    }
                }
                // TODO This line here is a problem !!!
                _knnQuery(nextRect.getChild(), center, k, maxHeap);
            } else { // if the maxHeap already contains an element
                // that means we already have a search radius
                // act accordingly
                // search for the closest rectangle, with a max distance taken from the head of the max heap
                RectangleEntry nextRect = rectangleEntries.get(0);
                LinkedList<RectangleEntry> searchFront = new LinkedList<>();
                for (RectangleEntry r : rectangleEntries)
                    // if the distance to this rectangle r is inside our radius
                    // do search
                    if (center.distance(r) < maxHeap.peek().distance())
                        searchFront.add(r);
                searchFront.forEach(rectangleEntry -> _knnQuery(rectangleEntry.getChild(), center, k, maxHeap));
            }
        } else { // if root is leaf node
            LeafNode node = (LeafNode) root;
            LinkedList<PointEntry> pointEntries = node.getPointEntries();
            for (PointEntry pE : pointEntries) {
                if (maxHeap.size() < k) {// populate the max heap
                    maxHeap.offer(new PointPair(center, pE.getPoint()));
                }
                // else if we are at size == k
                // and the distance to the currently checked point entry is less than the head of the max heap
                else if (center.distance(pE) < maxHeap.peek().distance()) {
                    // if the distance from point pE is LESS
                    // than what the maximum of the minimum distances
                    // we've already found
                    // pop the top element of the heap
                    // and add the newly found point
                    maxHeap.poll();
                    maxHeap.offer(new PointPair(center, pE.getPoint()));
                }

            }
        }
        */
        return null;
    }

    /**
     * A function that performs a query in a given rectangle and retrieves all point entries in it
     *
     * @param root  The root node to start from.
     * @param query The Rectangle in which the elements are needed
     * @return The point entries inside the query rectangle
     */
    public static HashSet<Point> rangeQuery(Node root, final Rectangle query) {
        HashSet<Point> result = new HashSet<>();
        LinkedList<Node> search = new LinkedList<>();
        _rangeQuery(root, query, result, search);
        // print results - TEMP PRINT
        result.forEach(r -> System.out.println(r.toString()));
        return result;
    }

    /**
     * This is a recursive function that retrieves the point entries inside a rectangle
     */
    private static HashSet<Point> _rangeQuery(Node root, final Rectangle query,
                                              HashSet<Point> result, LinkedList<Node> search) {
        // search from root
        // if it's a no-leaf-node
        //  search only in the rectangles that overlap the query rectangle
        // if it's a leaf-node
        //  search which point entries are inside the query rectangle and add them to the result
        // search recursively? Yes

        if (root.leaf) {
            root = (LeafNode) root;
            ((LeafNode) root).getPointEntries().forEach(p -> {
                if (query.contains(p) && !result.contains(p.getPoint())) {
                    result.add(p.getPoint());
//                    System.out.println("\t\tAdding " + p.getPoint().toString());
                }
            });
        } else { // if root is not a leaf
            ((NoLeafNode) root).getRectangleEntries().forEach(r -> { // for each rectangle entry
                if (r.getRectangle().overlaps(query)) // if there is an overlap
                    search.add(r.getChild()); // populate. add that rectangle for further inspection
            });
            for (int i = 0; i < search.size(); i++) {
                _rangeQuery(search.pop(), query, result, search);
            }
        }


        return result;
    }

    public static void dfs(Node root, int level) {
        if (!root.leaf) {
            LinkedList<RectangleEntry> rectangles = ((NoLeafNode) root).getRectangleEntries();
            for (RectangleEntry rectangleEntry : rectangles) {
                System.out.println("You are in Level: " + level);
                System.out.println(rectangleEntry.getRectangle());
                System.out.println("Going in...");
                dfs(rectangleEntry.getChild(), level + 1);
            }
        } else {
            System.out.println("Start Leaf Node");
            for (PointEntry pointEntry : ((LeafNode) root).getPointEntries()) {
                System.out.println(pointEntry.getPoint());
            }
            System.out.println("End Leaf Node");
        }
    }

    public static LinkedList<LinkedList> iterative(LinkedList nodeList, int M, LinkedList<RectangleEntry> rectangleEntriesListIn) {
        LinkedList<LinkedList> result = bottomUp(nodeList, M, rectangleEntriesListIn);
//        System.out.println(result.get(0).size());
        while (result.get(0).size() != 1) {
            result = bottomUp(result.get(0), M, result.get(1));
        }
//        System.out.println(result.get(0).size());

        return result;
    }

    public static LinkedList<LinkedList> bottomUp(LinkedList nodeList, int M, LinkedList<RectangleEntry> rectangleEntriesListIn) {

        int N2 = nodeList.size() / M + 1;
        LinkedList<RectangleEntry> rectangleEntriesListOut = new LinkedList<>();
        LinkedList<NoLeafNode> noLeafNodesList = new LinkedList<>();
        //dimioyrgo mia lista me NoLeafNodes kai mia lista me rectangles
        Iterator<RectangleEntry> iterator3 = rectangleEntriesListIn.iterator();
        for (int i = 0; i < N2; i++) { //posa nodes
            while (iterator3.hasNext()) {
                LinkedList<RectangleEntry> tempList = new LinkedList<>();
                for (int j = 0; j < M; j++) { //16ades
                    if (iterator3.hasNext()) tempList.add(iterator3.next());
                }

                NoLeafNode tempNode = new NoLeafNode(tempList);
                for (RectangleEntry rectangleEntry : tempList) {
                    rectangleEntry.setContainer(tempNode);
                }
                noLeafNodesList.add(tempNode);

                LinkedList<Node> temp2 = new LinkedList<>();

                for (RectangleEntry rectangleEntry : tempNode.getRectangleEntries()) {
                    temp2.add(rectangleEntry.getChild());
                }
                tempNode.setChildren(temp2);

                RectangleEntry tempRect = new RectangleEntry(tempNode);
                tempNode.setParent(tempRect);
                rectangleEntriesListOut.add(tempRect);
            }
        }

        LinkedList<LinkedList> returnable = new LinkedList<>();
        returnable.add(noLeafNodesList);
        returnable.add(rectangleEntriesListOut);
        return returnable;
    }

    public static LinkedList<LinkedList> bottomUpLevel0(List<PointEntry> sortedEntries, int M) {

        int num_of_nodes = sortedEntries.size() / M + 1;

        LinkedList<LeafNode> leafNodesList = new LinkedList<>(); //list with level 0 nodes
        LinkedList<RectangleEntry> rectangleEntriesListOut = new LinkedList<>(); //list for the next iteration, 1 rectangle entry in [level 1 node] is 1 [level 0 node]

        Iterator<PointEntry> iterator = sortedEntries.iterator(); //list with sorted point entries to fill level 0 nodes
        for (int i = 0; i < num_of_nodes; i++) { //posa nodes
            while (iterator.hasNext()) {
                LinkedList<PointEntry> tempList = new LinkedList<>(); //list with M elements

                for (int j = 0; j < M; j++) { //16ades
                    if (iterator.hasNext()) tempList.add(iterator.next());
                }

                LeafNode tempNode = new LeafNode(tempList); //create leaf node with M entries
                for (PointEntry pointEntry : tempList) { //update node's entries about their container
                    pointEntry.setContainer(tempNode);
                }

                leafNodesList.add(tempNode); // add leaf node to list with level 0 nodes

                RectangleEntry tempRect = new RectangleEntry(tempNode); //create rectangle and connect it with leaf node
                tempNode.setParent(tempRect); //connect leaf node with rectangle

                rectangleEntriesListOut.add(tempRect); //add rectangle to list with level 0 rectangles
            }
        }
//        System.out.println(leafNodesList.size());


        LinkedList<LinkedList> returnable = new LinkedList<>();
        returnable.add(leafNodesList);
        returnable.add(rectangleEntriesListOut);
        return returnable;
    }

}
