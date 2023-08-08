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

    private static int zOrderValueNDimensions(ArrayList<Integer> coords, int numBits) {
        int value = 0;

        for (int i = 0; i < numBits; i++) {
            for (int j = 0; j < coords.size(); j++) {
                value |= (coords.get(j) & 1 << i) << (i + j);
            }
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
            int z1 = zOrderValueNDimensions(e1.getPoint().getCoordsIntegers(), numBits);
            int z2 = zOrderValueNDimensions(e2.getPoint().getCoordsIntegers(), numBits);
            return Integer.compare(z1, z2);
        }
    }

    // Function to sort (x, y) points using Z-order curve
    public static List<PointEntry> sortPoints(List<PointEntry> points) {
        int maxBits = 32 * points.get(0).getPoint().getCoords().size();
        points.sort(new ZOrderComparator(maxBits));
        return points;
    }
}