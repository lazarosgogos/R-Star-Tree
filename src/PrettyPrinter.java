import java.util.ArrayList;
import java.util.List;

public class PrettyPrinter {
    public static String printRStarTree(Node root) {
        ArrayList<PointEntry> points = new ArrayList<>();
        ArrayList<RectangleEntryLevelPair> rectangles = new ArrayList<>();
        traverseRStarTree(root, points, rectangles, 0);
        StringBuilder pointsStringBuilder = new StringBuilder();
        for (PointEntry p : points) {
            pointsStringBuilder.append(p.getPoint().toPlottable());
            pointsStringBuilder.append(',');
        }
        int lastIndexOfComma = pointsStringBuilder.lastIndexOf(",");
        String pointsString = pointsStringBuilder.substring(0, lastIndexOfComma);
        StringBuilder rectanglesStringBuilder = new StringBuilder();
        for (RectangleEntryLevelPair r : rectangles){
            rectanglesStringBuilder.append(r.toPlottable());
            rectanglesStringBuilder.append(',');
        }
        lastIndexOfComma = rectanglesStringBuilder.lastIndexOf(",");
        String rectanglesString = rectanglesStringBuilder.substring(0, lastIndexOfComma);

        return pointsString + '\n' + rectanglesString;
    }

    private static void traverseRStarTree(Node node, List<PointEntry> points, List<RectangleEntryLevelPair> rectangles, int depth) {
        if (node == null) return;

        /*
         * For every rectangle entry
         * get its lower and its upper point
         * and save it in an array
         * for every point entry
         * just get its coords
         * and add them to an array
         * then we'll deal with plotting in matplotlib
         */

        /*for (Node child : node.childNodes) {
            traverseRStarTree(child, treeData, depth + 1);
        }*/
        // Recursively traverse child nodes
        if (node instanceof NoLeafNode) {
            NoLeafNode n = (NoLeafNode) node;
            // add rectangles in the list
            for (RectangleEntry re : n.getRectangleEntries()) {
                RectangleEntryLevelPair p = new RectangleEntryLevelPair(re, depth);
                rectangles.add(p);
            }

//            rectangles.addAll(n.getRectangleEntries());

            // now go into the sub-rectangles
            for (RectangleEntry re : n.getRectangleEntries()) {
                Node child = re.getChild();
                // and get all their data as well
                traverseRStarTree(child, points, rectangles, depth + 1);
            }
//            traverseRStarTree(child, treeData, depth + 1);
        } else if (node instanceof LeafNode) { // unless we've hit a leaf node
            LeafNode n = (LeafNode) node;
            /*for (PointEntry pe: n.getPointEntries()) {
//                traverseRStarTree(child, treeData, depth + 1);
                // Append node data to the list in a structured way
                points.add(pe);
            }*/
            // get the data from the points
            points.addAll(n.getPointEntries());
        }
    }

    private static class RectangleEntryLevelPair{
        private RectangleEntry r;
        private int level;

        public RectangleEntryLevelPair(RectangleEntry r, int level) {
            this.r = r;
            this.level = level;
        }

        public RectangleEntry getR() {
            return r;
        }

        public void setR(RectangleEntry r) {
            this.r = r;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String toPlottable(){
            return level + ":" + getR().getRectangle().toPlottable();
        }

    }

}
