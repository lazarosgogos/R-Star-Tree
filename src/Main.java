import java.util.*;

public class Main {

    static int M;

    public static void main(String[] args) {
        //725 records

//        //203
//        entries.addAll(IO.loadInput("assets/mapCenter.osm"));
//
//        //623
//        entries.addAll(IO.loadInput("assets/mapUnis.osm"));
//
//        //725
//        entries.addAll(IO.loadInput("assets/mapEast.osm"));

//        //2276 total

        String inputFile = "assets/mapCenter.osm";

        Node root = new Node();

        while (true) {
            System.out.println("type 1 if you want to load from file the last index");
            System.out.println("type 2 if you want to create a new index with bulk loading");
            System.out.println("type 3 if you want to create a new index");
            Scanner input = new Scanner(System.in);
            int answer = input.nextInt();
            if (answer == 1) {
                root = IndexfileDemo.loadFromFile();
                if (root == null) {
                    System.out.println("There isn't a saved index!");
                    continue;
                }
                break;
            } else if (answer == 2) {
                LinkedList<PointEntry> entries = IO.loadInput(inputFile);
                M = (int) Math.pow(2, Math.ceil(Math.log10(entries.size())));

                root = BulkLoading.bulkLoading(entries, M);
                break;
            } else if (answer == 3) {
                LinkedList<PointEntry> entries = IO.loadInput(inputFile);
                M = (int) Math.pow(2, Math.ceil(Math.log10(entries.size())));

                System.out.println((IO.loadRecordFromFile("1_2")));

                /*
                System.out.println(new Rectangle(new Point(2.4, 2.4), new Point(3, 3))
                        .getOverlap(new Rectangle(new Point(2.6, 2.6), new Point(4, 2.8))));

                 */
                break;
            }
        }

        //deapthFirstPrint(root,0);
        //BBS.runSkyline(root).forEach(pe -> System.out.println(pe.getPoint()));

        // KNN Query
//        Point p = new Point(5, 5);
//        int k = 5;
//        System.out.println("Knn query for size: " + k);
//        KNNQuery.knnQuery(root, p, 5);

      /* System.out.println("Now running range query");
        Rectangle myQuery = new Rectangle(40.60987f, 22.96f, 40.61f, 22.967f);
        RangeQuery.rangeQuery(root, myQuery);*/

        System.out.println("Do you want to save the index? Y/N");
        Scanner input = new Scanner(System.in);
        if (input.nextLine().equalsIgnoreCase("Y")) {
            IndexfileDemo.saveToFile(root);
        }
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




    public static void remove(String record_ID) {
        (IO.loadRecordFromFile(record_ID)).getCoords(); // gia na broume se poio LeafNode einai
        // me kapoio find paromoio me to pos briskei i insert na to balei isos
        LeafNode foundNode = null;
        PointEntry foundPE = null;

        if (foundNode.getPointEntries().size() - 1 >= Math.round(0.4 * M)) {
            foundNode.getPointEntries().remove(foundPE);
        } else {
            foundNode.getPointEntries().remove(foundPE); // remove the record
            HashSet<PointEntry> temp = new HashSet<>(foundNode.getPointEntries()); // get the rest for reInsert
            ((NoLeafNode) foundNode.getParent().getContainer()).getRectangleEntries().remove(foundNode.getParent()); // diagrafoume to Rectangle poy pleon den einai arketa megalo
            if (((NoLeafNode) foundNode.getParent().getContainer()).getRectangleEntries().size() < Math.round(0.4 * M)) {
                // an kai o gonios gamietai, krima
            }

        }

    }
}
