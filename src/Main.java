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

        int M = 4;

        Node root = BulkLoading.bulkLoading(entries, M);

        //deapthFirstPrint(root,0);
        //BBS.runSkyline(root).forEach(pe -> System.out.println(pe.getPoint()));

        ArrayList<Node> postOrder = initPostOrder(root);
        for (Node n : postOrder){
            System.out.println(n);
        }

        System.out.println();

        ArrayList<Node> inOrder = initInOrder(root);
        for (Node n : inOrder){
            System.out.println(n);
        }

        // KNN Query
//        Point p = new Point(5, 5);
//        int k = 5;
//        System.out.println("Knn query for size: " + k);
//        KNNQuery.knnQuery(root, p, 5);

      /* System.out.println("Now running range query");
        Rectangle myQuery = new Rectangle(40.60987f, 22.96f, 40.61f, 22.967f);
        RangeQuery.rangeQuery(root, myQuery);*/
    }
    public static void deapthFirstPrint(Node root, int level) {
        if (!root.leaf) {
            LinkedList<RectangleEntry> rectangles = ((NoLeafNode) root).getRectangleEntries();
            for (RectangleEntry rectangleEntry : rectangles) {
                System.out.println("You are in Level: " + level);
                System.out.println(rectangleEntry.getRectangle());
                System.out.println("Going in...");
                deapthFirstPrint(rectangleEntry.getChild(), level + 1);
            }
        } else {
            System.out.println("Start Leaf Node");
            for (PointEntry pointEntry : ((LeafNode) root).getPointEntries()) {
                System.out.println(pointEntry.getPoint());
            }
            System.out.println("End Leaf Node");
        }
    }

    public static ArrayList<Node> initPostOrder(Node root){
        ArrayList<Node> result = new ArrayList<>();

        postOrder(root, result);

        return result;
    }

    public static void postOrder(Node root, ArrayList<Node> result){

        if (!root.leaf) {
            LinkedList<RectangleEntry> rectangles = ((NoLeafNode) root).getRectangleEntries();
            for (RectangleEntry rectangleEntry : rectangles) {
                postOrder(rectangleEntry.getChild(), result);
            }
        }
        result.add(root);
    }

    public static ArrayList<Node> initInOrder(Node root){
        ArrayList<Node> result = new ArrayList<>();

        inOrder(root, result);

        return result;
    }

    public static void inOrder(Node root, ArrayList<Node> result){

        if (!root.leaf) {
            LinkedList<RectangleEntry> rectangles = ((NoLeafNode) root).getRectangleEntries();
            int numOfChildren = rectangles.size();
            for (int i=0; i<numOfChildren-1; i++){
                inOrder(rectangles.get(i).getChild(), result);
            }
            result.add(root);
            inOrder(rectangles.get(numOfChildren-1).getChild(), result);
        } else {
            result.add(root);
        }
    }
}
