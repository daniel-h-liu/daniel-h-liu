import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

public class KdTreeST<Value> {

    private Node root; // root of KdTreeST
    private int size; // size of KdTreeST

    private class Node {
        private Point2D key; // key associated with node
        private Value val; // val associated with node
        private Node left, right; // pointers to left and right nodes
        private RectHV rect; // corresponding bounding box rectangle
        private boolean compare; // true = compare x coords, false = compare y

        // Node constructor
        public Node(Point2D key, Value val, boolean compare, RectHV rect) {
            this.key = key;
            this.val = val;
            this.compare = compare;
            this.rect = rect;
        }

        // compare to another Point2D - useful in traversing KdTree
        //      in the case where the two points are equal (in either x or y
        //      depending on the value of compare), a negative number is
        //      returned and the equivalence case is dealt with in that specific
        //      context (ex: put() and get())
        public int compareTo(Point2D other) {
            if (compare) {
                if (this.key.x() - other.x() <= 0) return -1;
                else return 1;
            }
            else {
                if (this.key.y() - other.y() <= 0) return -2;
                else return 2;
            }
        }

        // String version of Node for testing
        public String toString() {
            return key + ": " + rect + ", val = " + val;
        }
    }

    // construct an empty symbol table of points
    public KdTreeST() {
        size = 0;
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of points
    public int size() {
        return size;
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null)
            throw new IllegalArgumentException("Null argument.");
        size++;
        if (root == null) {
            root = new Node(p, val, true, new RectHV(Double.NEGATIVE_INFINITY,
                                                     Double.NEGATIVE_INFINITY,
                                                     Double.POSITIVE_INFINITY,
                                                     Double.POSITIVE_INFINITY));
            return;
        }
        root = put(root, null, p, val);
    }

    // helper recursive function for put()
    private Node put(Node curr, Node prev, Point2D p, Value val) {
        if (curr == null) {
            int cmp = prev.compareTo(p);
            // create new node with  bounding box based on parent node's box and
            // how it compares coordinate-wise
            if (cmp == -1) return new Node(p, val, !prev.compare, new
                    RectHV(prev.key.x(), prev.rect.ymin(),
                           prev.rect.xmax(), prev.rect.ymax()));
            else if (cmp == 1) return new Node(p, val, !prev.compare, new
                    RectHV(prev.rect.xmin(), prev.rect.ymin(),
                           prev.key.x(), prev.rect.ymax()));
            else if (cmp == -2) return new Node(p, val, !prev.compare, new
                    RectHV(prev.rect.xmin(), prev.key.y(),
                           prev.rect.xmax(), prev.rect.ymax()));
            else return new Node(p, val, !prev.compare, new
                        RectHV(prev.rect.xmin(), prev.rect.ymin(),
                               prev.rect.xmax(), prev.key.y()));
        }
        int cmp = curr.compareTo(p);
        if (cmp > 0) curr.left = put(curr.left, curr, p, val);
        else if (cmp < 0) {
            if (curr.key.equals(p)) { // update value instead of adding new node
                curr.val = val;
                size--; // account for size increase earlier
            }
            else curr.right = put(curr.right, curr, p, val);
        }
        return curr;
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null key.");
        return get(root, p);
    }

    // helper recursive function for get()
    private Value get(Node node, Point2D key) {
        if (node == null) return null;
        int cmp = node.compareTo(key); // traverse the tree based on x/y coords
        if (cmp > 0) return get(node.left, key);
        else {
            if (node.key.equals(key)) return node.val; // node with key found
            else return get(node.right, key);
        }
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null key.");
        return get(p) != null;
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        Queue<Point2D> points = new Queue<Point2D>();
        Queue<Node> nodes = new Queue<Node>();
        nodes.enqueue(root);
        while (!nodes.isEmpty()) {
            Node node = nodes.dequeue();
            if (node == null) continue;
            points.enqueue(node.key);
            nodes.enqueue(node.left);
            nodes.enqueue(node.right);
        }
        return points;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("Null rectangle.");
        Queue<Point2D> matching = new Queue<Point2D>();
        range(rect, root, matching);
        return matching;
    }

    // helper recursive method for range()
    private void range(RectHV rect, Node curr, Queue<Point2D> matching) {
        if (curr == null || !rect.intersects(curr.rect)) return;

        Point2D p = curr.key;
        if (rect.contains(p)) matching.enqueue(p);
        // recurse through left and right subtrees if query rectangle is in
        // that half plane
        if (curr.compare) {
            if (p.x() <= rect.xmax()) range(rect, curr.right, matching);
            if (p.x() >= rect.xmin()) range(rect, curr.left, matching);
        }
        else {
            if (p.y() <= rect.ymax()) range(rect, curr.right, matching);
            if (p.y() >= rect.ymin()) range(rect, curr.left, matching);
        }
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("Null point.");
        if (root == null) return null;
        return nearest(p, root, root.key);
    }

    // helper recursive method for nearest()
    private Point2D nearest(Point2D query, Node curr, Point2D champion) {
        if (curr == null) return champion;
        // existing champion is closer than any possible node in curr's bounding
        // box, so prune this subtree
        if (curr.rect.distanceSquaredTo(query) >=
                champion.distanceSquaredTo(query))
            return champion;

        Point2D p = curr.key;
        if (p.distanceSquaredTo(query) <=
                champion.distanceSquaredTo(query)) // update closest neighbor
            champion = curr.key;

        // recurse in the direction of the query point first to hopefully
        // find a new champion, then recurse through the other subtree
        int cmp = curr.compareTo(query);
        if (cmp < 0) {
            champion = nearest(query, curr.right, champion);
            champion = nearest(query, curr.left, champion);
        }
        else {
            champion = nearest(query, curr.left, champion);
            champion = nearest(query, curr.right, champion);
        }
        return champion;
    }

    // unit testing (required)
    public static void main(String[] args) {
        KdTreeST<Integer> st = new KdTreeST<Integer>();
        System.out.println("Currently empty?: " + st.isEmpty());

        // read in the terms from a file
        String filename = args[0];
        In in = new In(filename);
        int i = 0;
        while (in.hasNextLine()) {
            st.put(new Point2D(in.readDouble(), in.readDouble()), i);
            in.readLine();
            i++;
        }
        Iterable<Point2D> ps = st.points();
        System.out.println("After adding in points: " + ps +
                                   ", size = " + st.size());

        // Contains and Get Tests
        double minX = Double.POSITIVE_INFINITY, minY = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY, maxY = Double.NEGATIVE_INFINITY;
        for (Point2D p : ps) {
            System.out.println("Check if contains " + p + ": "
                                       + st.contains(p));
            System.out.println("Testing get(): " + st.get(p));
            if (p.x() < minX) minX = p.x();
            if (p.x() > maxX) maxX = p.x();
            if (p.y() < minY) minY = p.y();
            if (p.y() > maxY) maxY = p.y();
        }
        double midX = (minX + maxX) / 2.0;
        double midY = (minY + maxY) / 2.0;

        // Range Tests
        RectHV r1 = new RectHV(minX, minY, maxX, maxY);
        System.out.println("Points in rect " + r1 + ": " + st.range(r1));
        RectHV r2 = new RectHV(minX, minY, midX, midY);
        System.out.println("Points in rect " + r2 + ": " + st.range(r2));
        RectHV r3 = new RectHV(midX, midY, maxX, maxY);
        System.out.println("Points in rect " + r3 + ": " + st.range(r3));
        RectHV r4 = new RectHV(midX / 2.0, midY / 2.0,
                               (3 * midX) / 2.0, (3 * midY) / 2.0);
        System.out.println("Points in rect " + r4 + ": " + st.range(r4));

        // Nearest Neighbor Tests
        System.out.println("Point nearest (" + minX + ", " + minY + "): " +
                                   st.nearest(new Point2D(minX, minY)));
        System.out.println("Point nearest (" + maxX + ", " + maxY + "): " +
                                   st.nearest(new Point2D(maxX, maxY)));
        System.out.println("Point nearest (" + midX + ", " + midY + "): " +
                                   st.nearest(new Point2D(midX, midY)));
    }
}
