import java.util.*;
public class Tree{
  Node root;

  public Tree(Node n){
    root = n;
  }
  public Node getRoot(){ return root; }

  public boolean isEmpty(){
        if(root == null) return true;
        return false;
  }

  public void print(){
    root.print();
  }
}