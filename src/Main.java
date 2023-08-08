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

    public static ArrayList<Node> inorder(Node root){
        ArrayList<Node> result = new ArrayList<>();

        if (!root.leaf) {
            LinkedList<RectangleEntry> rectangles = ((NoLeafNode) root).getRectangleEntries();
            for (RectangleEntry rectangleEntry : rectangles) {
                inorder(rectangleEntry.getChild());
            }
        } else {
            for (PointEntry pointEntry : ((LeafNode) root).getPointEntries()) {
                System.out.println(pointEntry.getPoint());
            }
        }

        return result;
    }

    public static ArrayList<Node> preorder(Node root){
        ArrayList<Node> result = new ArrayList<>();

        return result;
    }
}
