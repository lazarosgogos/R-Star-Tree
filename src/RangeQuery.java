import java.util.HashSet;
import java.util.LinkedList;

public class RangeQuery {
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

    public static void serial(Rectangle myQuery) {
        LinkedList<Point> list = IO.loadEverything();
        for (Point p : list){
            if (myQuery.contains(p)){
                System.out.println(p);
            }
        }
    }

}
