import java.util.*;

public class Main {

    static int M;
    static int m;
    static Node root;
    static RectangleEntry rootEntry;

    public static void main(String[] args) {
        // An theloume polla shmeia
        String inputFile = "assets/mapWest.osm";
        LinkedList<PointEntry> entries = IO.loadInput(inputFile);
        entries.addAll(IO.loadInput("assets/mapCenter.osm"));
        entries.addAll(IO.loadInput("assets/mapUnis.osm"));
        entries.addAll(IO.loadInput("assets/mapEast.osm"));
        //System.out.println(entries.size());
        //String inputFile = "assets/map.osm";

        root = new Node();

        while (true) {
            System.out.println("type 1 if you want to load from file the last index");
            System.out.println("type 2 if you want to create a new index with bulk loading");
            System.out.println("type 3 if you want to create a new index node-by-node");
            Scanner input = new Scanner(System.in);
            int answer = input.nextInt();
            //int answer = 3; //debug
            if (answer == 1) {
                root = IndexfileDemo.loadFromFile();
                if (root == null) {
                    System.out.println("There isn't a saved index!");
                    continue;
                }
                break;
            } else if (answer == 2) {
                //LinkedList<PointEntry> entries = IO.loadInput(inputFile);

                M = (int) Math.pow(2, Math.ceil(Math.log10(entries.size())));
                m = (int) Math.ceil(0.4 * M); // 2 <= m <= M/2
                if (M < 4) M = 4;
                if (m < 2) m = 2; // defensive programming

                long startTime = System.currentTimeMillis();
                root = BulkLoading.bulkLoading(entries, M);
                long endTime = System.currentTimeMillis();
                System.out.println("Time to create with bulk in seconds:");
                System.out.println(endTime-startTime);
                break;
            } else if (answer == 3) {
                //LinkedList<PointEntry> entries = IO.loadInput(inputFile);

                M = (int) Math.pow(2, Math.ceil(Math.log10(entries.size())));
                m = (int) Math.ceil(0.4 * M); // 2 <= m <= M/2
                if (M < 4) M = 4;
                if (m < 2) m = 2; // defensive programming

                long startTime = System.currentTimeMillis();
                //minimum number of entries in a node = m
                Iterator<PointEntry> it = entries.iterator();
                LinkedList<PointEntry> init = new LinkedList<>();
                for (int i = 0; i < m; i++) {
                    init.add(it.next());
                }

                root = new LeafNode(init);
                root.setRoot(true);
                for (PointEntry pe : ((LeafNode) root).getPointEntries()) {
                    pe.setContainer(root);
                }
                rootEntry = new RectangleEntry((LeafNode) root);
                root.setParent(rootEntry);

                //int counter = m; //debug

                while (it.hasNext()) {
                    AlgorithmInsert.insert(it.next());
                    /*System.out.println(++counter); //debug
                    deapthFirstPrint(root, 0);*/
                }

                long endTime = System.currentTimeMillis();
                System.out.println("Time to create with bulk in seconds:");
                System.out.println(endTime-startTime);
                break;
            }
        }

        while (true) {
            System.out.println("type 0 if to continue");
            System.out.println("type 1 if you want to run range query");
            System.out.println("type 2 if you want to run knn");
            System.out.println("type 3 if you want to run skyline");
            Scanner input = new Scanner(System.in);
            int answer = input.nextInt();
            if (answer == 1) {
                System.out.println("Give lower left x:");
                double startX = input.nextDouble();
                System.out.println("Give lower left y:");
                double startY = input.nextDouble();
                System.out.println("Give upper right x:");
                double endX = input.nextDouble();
                System.out.println("Give upper right y:");
                double endY = input.nextDouble();

                Rectangle myQuery = new Rectangle(new Point(startX, startY), new Point(endX, endY));

                long startTime = System.currentTimeMillis();

                RangeQuery.rangeQuery(root, myQuery);

                long endTime = System.currentTimeMillis();
                System.out.println("Time to run range query in ms:");
                System.out.println(endTime-startTime);

                startTime = System.currentTimeMillis();

                RangeQuery.serial(myQuery);

                endTime = System.currentTimeMillis();
                System.out.println("Time to run serial range query in ms:");
                System.out.println(endTime-startTime);

                break;
            } else if (answer == 2) {
                System.out.println("X:");
                double x = input.nextDouble();
                System.out.println("Y:");
                double y = input.nextDouble();

                System.out.println("How many neighbors:");
                int k = input.nextInt();

                long startTime = System.currentTimeMillis();

                KNNQuery.knnQuery(root, new Point(x, y), k);

                long endTime = System.currentTimeMillis();
                System.out.println("Time to run Knn in ms:");
                System.out.println(endTime-startTime);

                startTime = System.currentTimeMillis();

                KNNQuery.knnSerialQuery(new Point(x, y), IO.loadEverything(), k);

                endTime = System.currentTimeMillis();
                System.out.println("Time to run serial range query in ms:");
                System.out.println(endTime-startTime);

                break;
            } else if (answer == 3) {
                BBS.runSkyline(root).forEach(pe -> System.out.println(IO.loadRecordFromFile(pe.getRecord_ID())));
                break;
            }
            else {
                break;
            }
        }

        System.out.println("Do you want the input for tree visualization? Y/N");
        Scanner input = new Scanner(System.in);
        if (input.nextLine().equalsIgnoreCase("Y")) {
            System.out.println(PrettyPrinter.printRStarTree(root));
        }

        System.out.println("Do you want to save the index? Y/N");
        if (input.nextLine().equalsIgnoreCase("Y")) {
            IndexfileDemo.saveToFile(root);
        }
    }
}
