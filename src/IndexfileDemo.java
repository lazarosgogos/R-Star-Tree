import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.LinkedList;

public class IndexfileDemo {

    public static void indexfile(Node root){

        ArrayList<Node> postOrder = new ArrayList<>();
//        try {
//            FileInputStream fileIn = new FileInputStream("indexfile.ser");
//            ObjectInputStream in = new ObjectInputStream(fileIn);
//            postOrder = (ArrayList<Node>) in.readObject();
//            in.close();
//            fileIn.close();
//        } catch (IOException i) {
//            i.printStackTrace();
//        } catch (ClassNotFoundException c) {
//            System.out.println("Array class not found");
//            c.printStackTrace();
//        }

        postOrder = initPostOrder(root);
        for (Node n : postOrder){
            System.out.println(n);
        }

        System.out.println();

        ArrayList<Node> inOrder = initInOrder(root);
        for (Node n : inOrder){
            System.out.println(n);
        }

//        try {
//            FileOutputStream fileOut =
//                    new FileOutputStream("indexfile.ser");
//            ObjectOutputStream out = new ObjectOutputStream(fileOut);
//            out.writeObject(postOrder);
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
