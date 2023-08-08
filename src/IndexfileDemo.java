import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class IndexfileDemo {

    public static Node indexfile(){

        Node savedroot = new Node();
        try {
            FileInputStream fileIn = new FileInputStream("indexfile.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            savedroot = (Node) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Array class not found");
            c.printStackTrace();
        }

        /*
        ArrayList<Node> postOrder = new ArrayList<>();
        postOrder = initPostOrder(savedroot);
        for (Node n : postOrder){
            if (n instanceof NoLeafNode){
                System.out.println(((NoLeafNode) n).getRectangleEntries().get(0).getRectangle());
            }
            else if (n instanceof LeafNode){
                System.out.println(((LeafNode) n).getPointEntries().get(0).getPoint());
            }
        }

        System.out.println();

        ArrayList<Node> inOrder = initInOrder(savedroot);
        for (Node n : inOrder){
            if (n instanceof NoLeafNode){
                System.out.println(((NoLeafNode) n).getRectangleEntries().get(0).getRectangle());
            }
            else if (n instanceof LeafNode){
                System.out.println(((LeafNode) n).getPointEntries().get(0).getPoint());
            }
        }*/

        return savedroot;
//        try {
//            FileOutputStream fileOut =
//                    new FileOutputStream("indexfile.ser");
//            ObjectOutputStream out = new ObjectOutputStream(fileOut);
//            out.writeObject(root);
//            out.close();
//            fileOut.close();
//            System.out.println("Serialized data is saved");
//        } catch (IOException i) {
//            i.printStackTrace();
//        }
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
}
