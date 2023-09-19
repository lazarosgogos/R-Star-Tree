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
}
