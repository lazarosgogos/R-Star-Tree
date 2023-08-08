import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class BulkLoading {

    public static Node bulkLoading(List<PointEntry> entries, int M){

        List<PointEntry> sortedEntries = ZOrderCurveSort.sortPoints(entries);

        LinkedList<LinkedList> leaves = bottomUpLevel0(sortedEntries, M);

        LinkedList<LinkedList> rootNodes = iterative(leaves.get(0), M, leaves.get(1));

        Node root = new Node();
        for (NoLeafNode noLeafNode : (LinkedList<NoLeafNode>) rootNodes.get(0)) {
            root = noLeafNode;
            noLeafNode.setRoot(true);
        }
        return root;
    }

    public static LinkedList<LinkedList> iterative(LinkedList nodeList, int M, LinkedList<RectangleEntry> rectangleEntriesListIn) {
        LinkedList<LinkedList> result = bottomUp(nodeList, M, rectangleEntriesListIn);
        while (result.get(0).size() != 1) {
            result = bottomUp(result.get(0), M, result.get(1));
        }
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

        LinkedList<LinkedList> returnable = new LinkedList<>();
        returnable.add(leafNodesList);
        returnable.add(rectangleEntriesListOut);
        return returnable;
    }
}
