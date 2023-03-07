import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class BinarySearchDeluxe {

    // Returns the index of the first key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key>
            comparator) {
        if (a == null || key == null || comparator == null) throw new
                IllegalArgumentException("Arguments cannot be null.");
        int lo = 0;
        int hi = a.length - 1;
        int tempMid = -1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int compare = comparator.compare(a[mid], key);
            if (compare < 0) lo = mid + 1;
            else if (compare > 0) hi = mid - 1;
            else {
                if (mid - 1 < 0) return mid;
                tempMid = mid;
                hi = mid - 1;
            }
        }
        return tempMid;
    }

    // Returns the index of the last key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key>
            comparator) {
        if (a == null || key == null || comparator == null) throw new
                IllegalArgumentException("Arguments cannot be null.");
        int lo = 0;
        int hi = a.length - 1;
        int tempMid = -1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int compare = comparator.compare(a[mid], key);
            if (compare < 0) lo = mid + 1;
            else if (compare > 0) hi = mid - 1;
            else {
                if (mid + 1 >= a.length) return mid;
                tempMid = mid;
                lo = mid + 1;
            }
        }
        return tempMid;
    }

    // unit testing (required)
    public static void main(String[] args) {
        String[] a = { "A", "A", "C", "G", "G", "T" };
        Comparator<String> comparator = String.CASE_INSENSITIVE_ORDER;
        StdOut.println(Arrays.toString(a));
        int firstIndex = BinarySearchDeluxe.firstIndexOf(a, "G", comparator);
        int lastIndex = BinarySearchDeluxe.lastIndexOf(a, "G", comparator);
        StdOut.println("First appearance of 'G': " + firstIndex);
        StdOut.println("Last appearance of 'G': " + lastIndex);

        firstIndex = BinarySearchDeluxe.firstIndexOf(a, "Z", comparator);
        lastIndex = BinarySearchDeluxe.lastIndexOf(a, "Z", comparator);
        StdOut.println("First appearance of 'Z': " + firstIndex);
        StdOut.println("Last appearance of 'Z': " + lastIndex);

        firstIndex = BinarySearchDeluxe.firstIndexOf(a, "C", comparator);
        lastIndex = BinarySearchDeluxe.lastIndexOf(a, "C", comparator);
        StdOut.println("First appearance of 'C': " + firstIndex);
        StdOut.println("Last appearance of 'C': " + lastIndex);
    }
}
