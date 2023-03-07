import java.util.*;
import java.io.*;

class Main {
  public static void main(String[] args) throws FileNotFoundException {
    ID3 algo = new ID3();
    List<ArrayList<String>> list = algo.loadCSV("play.csv");
    ArrayList<String> headers = list.remove(0);
    algo.printFile(list, headers);
    Node root = new Node("Tree");
    Tree tree = new Tree(root);
    algo.runID3Helper(list, root, headers);
    tree.print();
  }
} 