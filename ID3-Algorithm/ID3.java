import java.util.*;
import java.io.*;

public class ID3 extends Object {
  // loadCSV File
  public List<ArrayList<String>> loadCSV(String path) throws FileNotFoundException {
    List<ArrayList<String>> list = new ArrayList<>();
    Scanner sc = new Scanner(new File(path));
    while (sc.hasNextLine()) {
      list.add(new ArrayList<String>(Arrays.asList(sc.nextLine().split(","))));
    }
    return list;
  }

  // get col
  public List<String> getColumn(List<ArrayList<String>> dataTable, int colNum) {
    List<String> column = new ArrayList<>();
    for (int i = 0; i < dataTable.size(); i++) {
      column.add(dataTable.get(i).get(colNum));
    }
    return column;
  }

  // remove col
  public List<ArrayList<String>> removeColumn(List<ArrayList<String>> dataTable, int colNum) {
    List<ArrayList<String>> data = new ArrayList<>();
    for (int i = 0; i < dataTable.size(); i++) {
      data.add(new ArrayList<>());
      for (int j = 0; j < dataTable.get(i).size(); j++) {
        if (j != colNum) {
          data.get(i).add(dataTable.get(i).get(j));
        }
      }
    }
    return data;
  }

  // log 2
  public double log2(double num) {
    double result = (double) (Math.log(num) / Math.log(2));
    return result;
  }

  // calc entropy
  public double calculateEntropy(double numOccurences, double numValues) {
    if (numOccurences == numValues || numOccurences == 0) {
      return 0;
    }
    double ans = 0;
    double yes = numOccurences / numValues;
    double no = (numValues - numOccurences) / numValues;
    ans = -(yes) * log2(yes) - (no) * log2(no);
    return ans;
  }

  // entropyInColumn
  public double entropyInColumn(List<ArrayList<String>> table, int column) {
    double sum = 0.0;
    double numOccurences = 0.0;
    double yesValues = 0.0;
    List<String> col = getColumn(table, column);
    List<String> uni = getUniqueAttributes(table, column);
    for (int i = 0; i < uni.size(); i++) { // pick attribute
      String a = uni.get(i);
      for (int j = 0; j < col.size(); j++) {
        if (a.equals(col.get(j))) {
          numOccurences++;
          if (getOutputValue(table.get(j)).equals("Yes")) {
            yesValues++;
          }
        }
      }
      sum += calculateEntropy(yesValues, numOccurences);

      numOccurences = 0.0;
      yesValues = 0.0;
    }
    return sum;
  }

  // copy list
  public List<ArrayList<String>> copyList(List<ArrayList<String>> list) {
    List<ArrayList<String>> copy = new ArrayList<ArrayList<String>>();
    for (int i = 0; i < list.size(); i++) {
      copy.add(new ArrayList<String>());
      for (int j = 0; j < list.get(i).size(); j++) {
        copy.get(i).add(list.get(i).get(j));
      }
    }
    return copy;
  }

  // areOutputValuesEqual
  public boolean areOutputValuesEqual(List<ArrayList<String>> list) {
    for (int i = 1; i < list.size(); i++) {
      if (list.get(i).get(list.get(0).size() - 1) != list.get(i - 1).get(list.get(0).size() - 1))
        return false;
    }
    return true;
  }

  // gainInColumn
  public double gainInColumn(List<ArrayList<String>> table, int column) {
    List<String> col = getColumn(table, column);
    List<String> uni = getUniqueAttributes(table, column);
    double total = col.size();
    double subTotal = 0.0;
    double yesValues = 0.0;
    double gain = 0.0;
    for (String i : uni) {
      subTotal = 0.0;
      yesValues = 0.0;
      for (int j = 0; j < col.size(); j++) {
        //subTotal++;
        if (i.equals(col.get(j))) {
          subTotal++;
          if (getOutputValue(table.get(j)).equals("Yes")) {
            yesValues++;
          }
        }
        
        //System.out.println("Gain: " + gain);
      }
      gain += (subTotal / total) * calculateEntropy(yesValues, subTotal);
    }
    return gain;
  }

  // getLargestFrequencyInColumn
  public String getLargestFrequencyInColumn(List<ArrayList<String>> list, int colNum) {
    HashMap<String, Integer> freq = new HashMap<>();
    for (int i = 0; i < list.size(); i++) {
      String s = list.get(i).get(colNum);
      if (freq.containsKey(s)) {
        freq.put(s, freq.get(s) + 1);
      } else {
        freq.put(s, 1);
      }
    }
    String ans = "";
    int max = 0;
    for (String s : freq.keySet()) {
      if (freq.get(s) > max) {
        max = freq.get(s);
        ans = s;
      }
    }
    return ans;
  }

  // getOutputValue
  public String getOutputValue(ArrayList<String> data) {
    return data.get(data.size() - 1);
  }

  // getUniqueAttributes
  public List<String> getUniqueAttributes(List<ArrayList<String>> dataTable, int colNum) {
    List<String> col = new ArrayList<>();
    for (int i = 0; i < dataTable.size(); i++) {
      if (!col.contains(dataTable.get(i).get(colNum)))
        col.add(dataTable.get(i).get(colNum));
    }
    return col;
  }

  // getValuesTotalWithTarget
  public int getValuesTotalWithTarget(List<String> allValuesIn, List<String> allTargetValuesIn, String item,
      String targetItem) {
    int count = 0;
    for (int i = 0; i < allValuesIn.size(); i++) {
      if (allValuesIn.get(i).equals(item) && allTargetValuesIn.get(i).equals(targetItem))
        count++;
    }
    return count;
  }

  // splitTableOnItem
  public List<ArrayList<String>> splitTableOnItem(List<ArrayList<String>> list, int colToFilter, String item) {
    List<ArrayList<String>> copy = copyList(list);
    for (int i = 0; i < copy.size(); i++) {
      if (!copy.get(i).get(colToFilter).equals(item)) {
        copy.remove(i);
        i--;
      }
    }
    return copy;
  }

  // printArrayList
  public void printFile(List<ArrayList<String>> list, List<String> categories) {
    list.add(0, new ArrayList<String>(categories));

    int length = 0;
    for (int i = 0; i < list.size(); i++) {
      for (int j = 0; j < list.get(i).size(); j++) {
        length = Math.max(length, list.get(i).get(j).length());
      }
    }
    printHelper(list, length);
    System.out.println();
    for (int i = 0; i < list.size(); i++) {
      for (int j = 0; j < list.get(i).size(); j++) {
        StringBuilder s = new StringBuilder(list.get(i).get(j));
        // s.insert(0," ");
        if (j == 0)
          s.insert(0, "|");

        int numSpaces = length - s.length() + 1;
        for (int k = 0; k < numSpaces; k++) {
          s.append(" ");
        }
        s.append("|");
        System.out.print(s.toString());
      }
      if (i == 0 || i == list.size() - 1)
        printHelper(list, length);
      System.out.println();
    }
    list.remove(0);
  }

  private void printHelper(List<ArrayList<String>> list, int length) {
    System.out.println();
    StringBuilder spaces = new StringBuilder();
    for (int k = 0; k < list.get(0).size(); k++) {
      spaces.append("+");
      int l = 0;
      if (k == 0)
        l = 1;
      for (; l < length + 1; l++) {
        spaces.append("-");
      }
    }
    spaces.append("+");
    System.out.print(spaces.toString());
  }

  // runID3
  public void runID3(List<ArrayList<String>> data, Node root) {
    List<ArrayList<String>> copy = copyList(data);
    List<String> categories = copy.remove(0);
    runID3Helper(copy, root, categories);
  }

  public void runID3Helper(List<ArrayList<String>> data, Node root, List<String> categories) {
    if (root == null || data.size() == 0 || data.get(0).size() <= 1) return; // base case
    System.out.println("Running ID3!");
    printFile(data, categories);
    // get Entropy of entire table
    double yesValues = 0.0;
    double total = 0.0;
    for(ArrayList<String> x : data){
      total++;
      String s = x.get(x.size()-1);
      if(s.equals("Yes")){
        yesValues++;
      }
    }
    double entropy = calculateEntropy(yesValues, total);
    if (entropy == 0) {
      System.out.println("All outputs are equal.");
      String output = getOutputValue(data.get(0));
      root.addChild(new Node(output));
      return;
    }
    System.out.println("Entropy: " + entropy);
    // find column with max gain
    int colWithMaxGain = 0;
    double gain = 0.0;
    for (int i = 0; i < data.get(0).size() - 1; i++) {
      double colGain = entropy - gainInColumn(data, i);
      System.out.println("Gain for " + categories.get(i) + ": " + colGain);
      if (gain < colGain) {
        gain = colGain;
        colWithMaxGain = i;
      }
    }
    System.out.println("Largest Gain: " + gain);
    System.out.println(categories.get(colWithMaxGain));
    // get unique attributes and add children nodes to root
    List<String> uni = getUniqueAttributes(data, colWithMaxGain);
    Node categ = new Node(categories.get(colWithMaxGain));
    root.addChild(categ);
    for (String s : uni) { // add each child and runID3 on them
      System.out.println("Adding Node...");
      Node child = new Node(s);
      categ.addChild(child);
      System.out.println("add branch for: " + s);
      System.out.println("_______________splitTableOnItem " + s);
      List<ArrayList<String>> newTable = splitTableOnItem(data, colWithMaxGain, child.getValue());
      printFile(newTable, categories);
      newTable = removeColumn(newTable, colWithMaxGain);
      // REMOVE HEADER FROM CATEGORY
      List<String> headers = copy1DArrayList(categories);
      headers.remove(colWithMaxGain);
      runID3Helper(newTable, child, headers);
    }
  }

  public List<String> copy1DArrayList(List<String> list) {
    List<String> copy = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      copy.add(list.get(i));
    }
    return copy;
  }
  // TODO: MAKE METHODS WORK WITH FIRST ROW/CATEGORIES, IT IS TOO MUCH WORK
  // OTHERWISE
}
