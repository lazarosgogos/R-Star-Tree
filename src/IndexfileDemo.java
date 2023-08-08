import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class IndexfileDemo {
    public static Node loadFromFile() {
        Node root = null;
        try {
            FileInputStream fileIn = new FileInputStream("indexfile.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            root = (Node) in.readObject();
            in.close();
            fileIn.close();
        } catch (Exception e) {
        }

        return root;
    }

    public static void saveToFile(Node root) {
        try {
            FileOutputStream fileOut =
                    new FileOutputStream("indexfile.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(root);
            out.close();
            fileOut.close();
            System.out.println();
            System.out.println("Index has been saved!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    public static void printPostOrderAndInOrder(Node root) {
        System.out.println();

        ArrayList<Node> postOrder = new ArrayList<>();
        postOrder = initPostOrder(root);
        for (Node n : postOrder){
            if (n instanceof NoLeafNode){
                System.out.println(((NoLeafNode) n).getRectangleEntries().get(0).getRectangle());
            }
            else if (n instanceof LeafNode){
                System.out.println(((LeafNode) n).getPointEntries().get(0).getPoint());
            }
        }

        System.out.println();

        ArrayList<Node> inOrder = initInOrder(root);
        for (Node n : inOrder) {
            if (n instanceof NoLeafNode) {
                System.out.println(((NoLeafNode) n).getRectangleEntries().get(0).getRectangle());
            } else if (n instanceof LeafNode) {
                System.out.println(((LeafNode) n).getPointEntries().get(0).getPoint());
            }
        }
    }

    public static ArrayList<Node> initPostOrder(Node root){
        ArrayList<Node> result = new ArrayList<>();

        postOrder(root, result);

        return result;
    }

    public static void postOrder(Node root, ArrayList<Node> result){

        if (!root.leaf) {
            LinkedList<RectangleEntry> rectangles = ((NoLeafNode) root).getRectangleEntries();
            for (RectangleEntry rectangleEntry : rectangles) {
                postOrder(rectangleEntry.getChild(), result);
            }
        }
        result.add(root);
    }

    public static ArrayList<Node> initInOrder(Node root){
        ArrayList<Node> result = new ArrayList<>();

        inOrder(root, result);

        return result;
    }

    public static void inOrder(Node root, ArrayList<Node> result){

        if (!root.leaf) {
            LinkedList<RectangleEntry> rectangles = ((NoLeafNode) root).getRectangleEntries();
            int numOfChildren = rectangles.size();
            for (int i=0; i<numOfChildren-1; i++){
                inOrder(rectangles.get(i).getChild(), result);
            }
            result.add(root);
            inOrder(rectangles.get(numOfChildren-1).getChild(), result);
        } else {
            result.add(root);
        }
    }
    */
}
