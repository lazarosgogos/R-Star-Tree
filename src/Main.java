import java.util.*;

public class Main {

    public static void main(String[] args) {
        //725 records
        LinkedList<PointEntry> entries = IO.loadInput("assets/mapTest.osm");

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

        int M = 4;

        Node root = bulkLoading(sortedEntries, M);

        //dfs(root,0);
        BBS.runSkyline(root).forEach(pe -> System.out.println(pe.getPoint()));

        // KNN Query
//        Point p = new Point(5, 5);
//        int k = 5;
//        System.out.println("Knn query for size: " + k);
//        KNNQuery.knnQuery(root, p, 5);

      /* System.out.println("Now running range query");
        Rectangle myQuery = new Rectangle(40.60987f, 22.96f, 40.61f, 22.967f);
        RangeQuery.rangeQuery(root, myQuery);*/
    }

    public static Node bulkLoading(List<PointEntry> sortedEntries, int M){

        LinkedList<LinkedList> leaves = bottomUpLevel0(sortedEntries, M);

        LinkedList<LinkedList> rootNodes = iterative(leaves.get(0), M, leaves.get(1));

        Node root = new Node();
        for (NoLeafNode noLeafNode : (LinkedList<NoLeafNode>) rootNodes.get(0)) {
            root = noLeafNode;
            noLeafNode.setRoot(true);
        }
        return root;
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
