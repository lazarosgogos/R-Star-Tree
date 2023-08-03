import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

/** Class that handles input and output functions. Includes loading data to memory,
 *  saving the R* tree to the disk, etc. */
public class IO {
    /** Element structure: id, lat, long */
    private static final boolean debug = false;

    private static String[] block0;

    public static LinkedList<PointEntry> loadInput (String filePath) {
        //i lista poy epistrefo me ta entries
        LinkedList<PointEntry> ret = new LinkedList<>();
        try {
            File inputFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;

            dBuilder = dbFactory.newDocumentBuilder();

            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();

            String expression = "/osm/node/tag[@k='name']";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(
                    doc, XPathConstants.NODESET);

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node nNode = nodeList.item(i);
                Element prosoxi = (Element) nNode;
//                System.out.println("\nCurrent Element :" + prosoxi.getAttribute("v"));

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode.getParentNode();
                    if (debug) {
                        System.out.println("id :" + eElement.getAttribute("id"));
                        System.out.println("lat :" + eElement.getAttribute("lat"));
                        System.out.println("lon :" + eElement.getAttribute("lon"));
                    }

                    //pairnoyme ta stoixeia toy points
                    String longitude = eElement.getAttribute("lon");
                    String latitude = eElement.getAttribute("lat");
                    ArrayList<String> strings = new ArrayList<>();
                    strings.add(latitude);
                    strings.add(longitude);
                    long id = Long.parseLong(eElement.getAttribute("id"));
                    String name = prosoxi.getAttribute("v");

                    //dimioyrgoume data record me ta stoixeia
                    Point r = new Point(id, name, strings);

                    // Stelnoume to DataRecord ston handler
                    new DatafileHandler(r);

                    // Mas epistrefete to recordId block_slot
                    String recordID = DatafileHandler.getRecordId();

                    // stelnoyme long lat kai recId(block+slot) gia IndexRecord
                    PointEntry px = new PointEntry(r, recordID);

                    ret.add(px);
                }
            }
            DatafileHandler.finalizeFile();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        }
        return ret;
    }

//    public static DataRecord loadRecordFromFile(String recordId) {
//        String[] recordIdArr = recordId.split("_");
//        String searchTerm = "block" + recordIdArr[0];
//        int index = Integer.parseInt(recordIdArr[1]);
//
//        String blockStr = "";
//        try {
//            File file = new File("datafile.txt");
//            BufferedReader reader = new BufferedReader(new FileReader(file));
//
//            String line;
//            int lineNumber = 0;
//            while ((line = reader.readLine()) != null) {
//                lineNumber++;
//                if (line.contains(searchTerm)) {
//                    blockStr = reader.readLine();
//                    break;
//                }
//            }
//
//            reader.close();
//        } catch (Exception e) {
//            throw new RuntimeException();
//        }
//
//        String[] block = blockStr.split("\\|");
//
//        String[] record = block[index].split(":");
//
//        float[] coor = new float[2];
//        coor[0]= Float.parseFloat(record[1]);
//        coor[1]= Float.parseFloat(record[2]);
//        return new DataRecord(record[3], record[0], coor);
//    }
//
//    public static int loadCoordsFromBlock0() {
//        String searchTerm = "block0";
//        if (block0 == null) {
//            String blockStr="";
//            try {
//                File file = new File("datafile.txt");
//                BufferedReader reader = new BufferedReader(new FileReader(file));
//
//                String line;
//                int lineNumber = 0;
//                while ((line = reader.readLine()) != null) {
//                    lineNumber++;
//                    if (line.contains(searchTerm)) {
//                        blockStr = reader.readLine();
//                        break;
//                    }
//                }
//
//                reader.close();
//            } catch (Exception e) {
//                throw new RuntimeException();
//            }
//
//            block0 = blockStr.split(":");
//        }
//        return Integer.parseInt(block0[3]);
//        }
}
