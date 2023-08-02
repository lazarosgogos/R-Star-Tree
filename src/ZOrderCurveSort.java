import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ZOrderCurveSort {
    // Function to calculate the Z-order value for a given (x, y) point
    private static int zOrderValue(int x, int y, int numBits) {
        int value = 0;
        int z = 10;
        for (int i = 0; i < numBits; i++) {
            value |= (x & 1 << i) << i | (y & 1 << i) << (i + 1) | (z & 1 << i) << (i + 2);
        }
        return value;
    }

    private static int zOrderValueNDimensions(ArrayList<Integer> coords, int numBits){
        int value = 0;

        for (int i = 0; i < numBits; i++) {
            for (int j = 0; j < coords.size(); j++) {
                value |= (coords.get(j) & 1 << i) << (i + j);
            }
//            value |= (x & 1 << i) << i | (y & 1 << i) << (i + 1) | (z & 1 << i) << (i + 2);
        }
        return value;
    }

    // Comparator to compare two points based on their Z-order value
    private static class ZOrderComparator implements Comparator<PointEntry> {
        private int numBits;

        public ZOrderComparator(int numBits) {
            this.numBits = numBits;
        }

        @Override
        public int compare(PointEntry e1, PointEntry e2) {
            int z1; // = zOrderValue(e1.getPoint().xs, e1.getPoint().ys, numBits);
            int z2; // = zOrderValue(e2.getPoint().xs, e2.getPoint().ys, numBits);
            z1 = zOrderValueNDimensions(e1.getPoint().getCoordsIntegers(), numBits);
            z2 = zOrderValueNDimensions(e2.getPoint().getCoordsIntegers(), numBits);
            return Integer.compare(z1, z2);
        }
    }

    // Function to sort (x, y) points using Z-order curve
    public static List<PointEntry> sortPoints(List<PointEntry> points) {
//        int maxBits = getMaxBits(points);
        int maxBits = 32 * points.get(0).getPoint().getCoords().size();
        points.sort(new ZOrderComparator(maxBits));
        return points;
    }

    // Helper function to determine the maximum number of bits required
    /*private static int getMaxBits(List<PointEntry> points) {
        int maxBits = 0;
        for (PointEntry entry : points) {
            maxBits = Math.max(maxBits, Math.max(Integer.bitCount(entry.getPoint().xs), Integer.bitCount(entry.getPoint().ys)));
        }
        return maxBits;
    }*/
}

//public class ZOrderCurveSort {
//    // Function to calculate the Z-order value for a given (x, y) point
//    private static int zOrderValue(int x, int y, int numBits) {
//        int value = 0;
//        for (int i = 0; i < numBits; i++) {
//            value |= (x & 1 << i) << i | (y & 1 << i) << (i + 1);
//        }
//        return value;
//    }
//
//    // Comparator to compare two points based on their Z-order value
//    private static class ZOrderComparator implements Comparator<Point> {
//        private int numBits;
//
//        public ZOrderComparator(int numBits) {
//            this.numBits = numBits;
//        }
//
//        @Override
//        public int compare(Point p1, Point p2) {
//            int z1 = zOrderValue(p1.xs, p1.ys, numBits);
//            int z2 = zOrderValue(p2.xs, p2.ys, numBits);
//            return Integer.compare(z1, z2);
//        }
//    }
//
//    // Function to sort (x, y) points using Z-order curve
//    public static List<Point> sortPoints(List<Point> points) {
//        int maxBits = getMaxBits(points);
//        points.sort(new ZOrderComparator(maxBits));
//        return points;
//    }
//
//    // Helper function to determine the maximum number of bits required
//    private static int getMaxBits(List<Point> points) {
//        int maxBits = 0;
//        for (PointEntry point : points) {
//            maxBits = Math.max(maxBits, Math.max(Integer.bitCount(point.xs), Integer.bitCount(point.ys)));
//        }
//        return maxBits;
//    }
//}
