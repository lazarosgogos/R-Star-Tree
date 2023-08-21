import java.util.*;

public class Main {

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
                int M = (int) Math.pow(2, Math.ceil(Math.log10(entries.size())));

                root = BulkLoading.bulkLoading(entries, M);
                break;
            } else if (answer == 3) {
                LinkedList<PointEntry> entries = IO.loadInput(inputFile);
                int M = (int) Math.pow(2, Math.ceil(Math.log10(entries.size())));


                System.out.println(new Rectangle(new Point(2.4, 2.4), new Point(3, 3))
                        .getOverlap(new Rectangle(new Point(2.6, 2.6), new Point(4, 2.8))));
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


    public static void insertData(PointEntry entry) {
        int leafLevel = 0;
        insert(leafLevel, entry);
    }

    public static void insert(int leafLevel, PointEntry entry) {
        Node root = null;
        int M = 4;
        boolean split = false;
        LeafNode N = (LeafNode) ChooseSubtree.chooseSubtree(root, entry);
        if (N.getPointEntries().size() < M) {
            N.addEntry(entry);
        }
        if (N.getPointEntries().size() == M) {
            split = overflowTreatment(leafLevel);
        }
        if (split) {
            //overflowTreatment() kai sta NoLeafNodes
        }
    }

    public static boolean overflowTreatment(int leafLevel) {
//        if () {
//            reInsert();
//        } else {
//            AlgorithmSplit.split();
            return true;
//        }
        //return false;
    }

    public static void reInsert(RectangleEntry e) {
        Node N = null;
        int M = 4;
        if (N instanceof NoLeafNode) {
            HashMap<RectangleEntry, Double> distances = new HashMap<RectangleEntry, Double>();
            for (RectangleEntry entry : ((NoLeafNode) N).getRectangleEntries()) { //RI1
                distances.put(entry, entry.getRectangle().getCenter().distance(N.parent.getRectangle().getCenter()));
            }
            LinkedList<Double> list = new LinkedList<>();
            list.addAll(distances.values());
            Collections.sort(list); //RI2
            long p = Math.round(0.3 * M);
            LinkedList<Double> trash = new LinkedList<>();
            for (int i = 0; i < p; i++) { //RI3
                trash.add(list.removeLast());
            }
            for (double value : trash) {
                for (RectangleEntry key : distances.keySet()) {
                    if (distances.get(key) == value) {
                        reInsert(key);
                    }
                }
            }
        } else {
            //((LeafNode) N).getPointEntries()
        }
    }
}
