import java.nio.file.attribute.AclEntryType;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

class Pair {
    Entry e;
    double f;

    Pair(RectangleEntry e, double f) {
        this.e = e;
        this.f = f;
    }


    public double getF() {
        return f;
    }

    @Override
    public String toString() {
        return "<" + e.toString() + "," + f + ">";
    }
}

class PairComparator implements Comparator<Pair> {
    @Override
    public int compare(Pair pair1, Pair pair2) {
        return Double.compare(pair1.getF(), pair2.getF());
    }
}

public class BBS {

    public static double mindist(RectangleEntry re) {
        Rectangle mbr = re.getRectangle();
        double result = 0;
        for (double coord : mbr.getStartPoint().getCoords()) {
            result += coord;
        }
        return result;
    }

    public static HashSet<PointEntry> runSkyline(Node root) {
        HashSet<PointEntry> set = new HashSet<>();
        PriorityQueue<Pair> heap = new PriorityQueue<>(new PairComparator());
        return skyline(root, heap, set);
    }

    static HashSet<PointEntry> skyline(Node root, PriorityQueue<Pair> heap, HashSet<PointEntry> set) {
        for (RectangleEntry re : ((NoLeafNode) root).getRectangleEntries()) {
            //System.out.println(re.getRectangle());
            heap.add(new Pair(re, mindist(re)));
        }
        while (!heap.isEmpty()) {
            Entry currentEntry = heap.poll().e;
            Node current = ((RectangleEntry) currentEntry).getChild();
            if (current instanceof NoLeafNode) {
                if (check(current, set)) {
                    skyline(current, heap, set);
                }
            } else {
                HashSet<PointEntry> temp = findNonDominatedPoints(((LeafNode) current).getPointEntries());
                set.addAll(temp);
            }

        }

        return finalize(set);
    }

    // complexity O(n)
    public static boolean check(Node current, HashSet<PointEntry> set) {
        Point tempPoint1;
        Point tempPoint2;
        for (PointEntry pe : set) {
            tempPoint1 = pe.getPoint();
            Rectangle tempRectangle = current.parent.getRectangle();
            tempPoint2 = new Point(tempRectangle.getStartPoint().getCoords());
            if (isDominated(tempPoint1, tempPoint2)) return false;
        }
        return true;
    }

    public static boolean isDominated(Point p1, Point p2) {
        boolean out = true;
        boolean in = false;

        int n = p1.getCoords().size();

        for (int i = 0; i < n; i++) {
            out &= p1.getCoords().get(i) <= p2.getCoords().get(i);
            in |= p1.getCoords().get(i) < p2.getCoords().get(i);
        }
        return out && in;
    }

    // complexity O(M^2)
    public static HashSet<PointEntry> findNonDominatedPoints(LinkedList<PointEntry> list) {
        HashSet<PointEntry> nonDominated = new HashSet<>();
        for (PointEntry p1 : list) {
            boolean isNonDominated = true;
            for (PointEntry p2 : list) {
                if (p1 != p2 && isDominated(p2.getPoint(), p1.getPoint())) {
                    isNonDominated = false;
                    break;
                }
            }
            if (isNonDominated) {
                nonDominated.add(p1);
            }
        }
        return nonDominated;
    }

    // complexity O(M^2)
    public static HashSet<PointEntry> finalize(HashSet<PointEntry> list) {
        HashSet<PointEntry> nonDominated = new HashSet<>();
        for (PointEntry p1 : list) {
            boolean isNonDominated = true;
            for (PointEntry p2 : list) {
                if (p1 != p2 && isDominated(p2.getPoint(), p1.getPoint())) {
                    isNonDominated = false;
                    break;
                }
            }
            if (isNonDominated) {
                nonDominated.add(p1);
            }
        }
        return nonDominated;
    }

}
