import java.util.*;

public class Node{
  private List<Node> children;
  private String value;

  public Node(String value){
        this.value = value;
        children = new ArrayList<Node>();
  }
  public String getValue(){ return value; }
  public List<Node> getChildren(){ return children; }
    
  public void setValue(String v){ value = v; }
  public void addChild(Node n){
    children.add(n);
  }

  public void print(){
    print("",true);
  }
  public void print(String prefix, boolean isTail) {
    System.out.println(prefix + (isTail ? "\\-- " : "|-- ") + value);
    for (int i = 0; i < children.size() - 1; i++) {
      children.get(i).print(prefix + (isTail ? " " : "| "), false);
    }
    if (children.size() > 0) {
      children.get(children.size() - 1).print(prefix + (isTail ?" " : "| "), true);
    }
  }

}