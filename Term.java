import edu.princeton.cs.algs4.StdIn;

import java.util.Arrays;
import java.util.Comparator;

public class Term implements Comparable<Term> {

    private final String query; // stores the specific query string
    private final long weight; // stores associated weight of the query

    // Initializes a term with the given query string and weight.
    public Term(String query, long weight) {
        if (query == null) throw new IllegalArgumentException(
                "Query cannot be null.");
        if (weight < 0) throw new IllegalArgumentException(
                "Weight cannot be negative.");
        this.query = query;
        this.weight = weight;
    }

    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder() {
        return new ReverseWeightComparator();
    }

    // Nested class that compares two terms by their weights, in reverse
    private static class ReverseWeightComparator implements Comparator<Term> {
        // Compares two terms by their weights in reverse order
        public int compare(Term t1, Term t2) {
            return Long.compare(t2.weight, t1.weight);
        }
    }

    // Compares the two terms in lexicographic order,
    // but using only the first r characters of each query.
    public static Comparator<Term> byPrefixOrder(int r) {
        if (r < 0) throw new IllegalArgumentException("Negative values for r "
                                                              + "are illegal.");
        return new PrefixRComparator(r);
    }

    private static class PrefixRComparator implements Comparator<Term> {
        private final int r; // the first r characters to consider in comparing

        // Initializes a PrefixRComparator with the given r
        public PrefixRComparator(int r) {
            this.r = r;
        }

        // Compares two terms by their lexicographic order, but only the first
        // r characters
        public int compare(Term t1, Term t2) {
            for (int i = 0; i < r; i++) {
                if (i >= t1.query.length() && i < t2.query.length())
                    return t1.query.length() - t2.query.length();
                if (i >= t2.query.length() && i < t1.query.length())
                    return t1.query.length() - t2.query.length();
                if (i >= t1.query.length() &&
                        t1.query.length() == t2.query.length()) return 0;
                if (t1.query.charAt(i) > t2.query.charAt(i)) return 1;
                else if (t1.query.charAt(i) < t2.query.charAt(i)) return -1;
            }
            return 0;
        }
    }

    // Compares the two terms in lexicographic order by query.
    public int compareTo(Term that) {
        return this.query.compareTo(that.query);
    }

    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString() {
        return String.valueOf(weight) + "\t" + query;
    }

    // unit testing (required)
    public static void main(String[] args) {
        int n = StdIn.readInt(); // number of queries
        Term[] terms = new Term[n];
        System.out.println("Original list of terms:");
        for (int i = 0; i < n; i++) {
            int w = StdIn.readInt();
            String q = StdIn.readLine().replace("\t", "");
            terms[i] = new Term(q, w);
            System.out.println(terms[i]);
        }

        // Natural order sort
        Arrays.sort(terms);
        System.out.println("--------------\nArray sorted lexicographically:");
        for (int i = 0; i < n; i++) {
            System.out.println(terms[i]);
        }

        // Reverse weight order sort
        Arrays.sort(terms, Term.byReverseWeightOrder());
        System.out.println("--------------\nArray sorted in reverse "
                                   + "weight order:");
        for (int i = 0; i < n; i++) {
            System.out.println(terms[i]);
        }

        // Sort lexicographically by first 3 characters
        Arrays.sort(terms, Term.byPrefixOrder(3));
        System.out.println("--------------\nArray sorted lexicographically"
                                   + "by first 3 characters:");
        for (int i = 0; i < n; i++) {
            System.out.println(terms[i]);
        }

        // Sort lexicographically by first 3 characters + reverse weight order
        Arrays.sort(terms, Term.byPrefixOrder(3).
                               thenComparing(Term.byReverseWeightOrder()));
        System.out.println("--------------\nArray sorted lexicographically"
                                   + "by first 3 characters and then in reverse"
                                   + "weight order:");
        for (int i = 0; i < n; i++) {
            System.out.println(terms[i]);
        }

        // Sort reverse weight order + lexicographically by first 3 characters
        Arrays.sort(terms, Term.byReverseWeightOrder().
                               thenComparing(Term.byPrefixOrder(3)));
        System.out.println("--------------\nArray sorted in reverse weight "
                                   + "order then lexicographically by first 3 "
                                   + "characters: ");
        for (int i = 0; i < n; i++) {
            System.out.println(terms[i]);
        }

    }

}
