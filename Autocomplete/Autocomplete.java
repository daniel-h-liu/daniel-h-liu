import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class Autocomplete {

    private Term[] terms; // immutable copy of AutoComplete terms
    private int firstIndex; // index of the first term with matching prefix
    private int lastIndex; // index of the last term with matching prefix

    // Initializes the data structure from the given array of terms.
    public Autocomplete(Term[] terms) {
        if (terms == null) throw new IllegalArgumentException(
                "Null array is an illegal argument.");
        int n = terms.length;
        firstIndex = -1;
        // to make immutable, copy references over
        this.terms = new Term[n];
        for (int i = 0; i < n; i++) {
            if (terms[i] == null) throw new IllegalArgumentException(
                    "Null entry in array.");
            this.terms[i] = terms[i];
        }
        Arrays.sort(this.terms);
    }

    // Returns all terms that start with the given prefix,
    // in descending order of weight.
    public Term[] allMatches(String prefix) {
        int range = numberOfMatches(prefix);
        Term[] matching = new Term[range];
        if (range <= 0) return matching;
        for (int i = firstIndex; i <= lastIndex; i++) {
            matching[i - firstIndex] = terms[i];
        }
        Arrays.sort(matching, Term.byReverseWeightOrder());
        return matching;
    }

    // Returns the number of terms that start with the given prefix.
    public int numberOfMatches(String prefix) {
        Term prefixTerm = new Term(prefix, 0);
        Term[] tCopy = new Term[terms.length];
        for (int i = 0; i < terms.length; i++) {
            tCopy[i] = terms[i];
        }
        firstIndex = BinarySearchDeluxe.firstIndexOf(tCopy, prefixTerm,
                                                     Term.byPrefixOrder(
                                                             prefix.length()));
        lastIndex = BinarySearchDeluxe.lastIndexOf(tCopy, prefixTerm,
                                                   Term.byPrefixOrder(
                                                           prefix.length()));
        if (firstIndex == -1) return 0;
        return lastIndex - firstIndex + 1;
    }

    // unit testing (required)
    public static void main(String[] args) {

        // read in the terms from a file
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        Term[] terms = new Term[n];
        for (int i = 0; i < n; i++) {
            long weight = in.readLong();           // read the next weight
            in.readChar();                         // scan past the tab
            String query = in.readLine();          // read the next query
            terms[i] = new Term(query, weight);    // construct the term
        }

        // read in queries from standard input + print the top k matching terms
        int k = Integer.parseInt(args[1]);
        Autocomplete autocomplete = new Autocomplete(terms);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            Term[] results = autocomplete.allMatches(prefix);
            StdOut.printf("%d matches\n", autocomplete.numberOfMatches(prefix));
            for (int i = 0; i < Math.min(k, results.length); i++)
                StdOut.println(results[i]);
        }
    }

}
