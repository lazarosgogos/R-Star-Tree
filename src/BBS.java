import java.nio.file.attribute.AclEntryType;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;

class Pair {
    Entry e;
    double f;

    Pair(PointEntry e, double f) {
        this.e = e;
        this.f = f;
    }

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
        return mbr.xStart + mbr.yStart;
    }

    public static double mindist(PointEntry pe) {
        return pe.getPoint().getX() + pe.getPoint().getY();
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
                set.add(findMin(((LeafNode) current).getPointEntries()));
            }

        }

        return set;
    }

    public static boolean check(Node current, HashSet<PointEntry> set) {
        for (PointEntry pe : set) {
            if (pe.getPoint().getX() < current.parent.getRectangle().xStart
                    && pe.getPoint().getY() < current.parent.getRectangle().yStart) {
                return false;
            }
        }
        return true;
    }

    public static PointEntry findMin(LinkedList<PointEntry> list) {
        double min = mindist(list.get(0));
        PointEntry minPE = list.get(0);
        for (PointEntry pe : list) {
            if (mindist(pe) < min) {
                min = mindist(pe);
                minPE = pe;
            }
        }
        return minPE;
    }

}
