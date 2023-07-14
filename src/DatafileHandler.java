import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

public class DatafileHandler {

    private static final int BLOCK_SIZE = 32768;
    private static int currentSizeofBlock = 32768;
    private static int blocksCounter = 0;
    private static int nextSlot = 0;
    private static int totalBytes = 0;
    private static int numberOfCoords;
    private static final String PROPERTIES_DELIMITER =":";

    /**
     * We need this property to insert it in block 0.
     */
    private static int recordsCounter = 0;

    public DatafileHandler(Point record) {
        numberOfCoords = 2;
        saveToFile(record.toString());
    }

    /**
     * This method calculates the size in bytes of the record.
     * @param input
     * @return
     */
    private static int calculateSizeofString(String input) {
        final byte[] utf8Bytes = input.getBytes(StandardCharsets.UTF_8);
        totalBytes += utf8Bytes.length;
        return utf8Bytes.length;
    }

    private static void blocksCounterInc() {
        blocksCounter++;
    }

    private static void slotsCounterInc() {
        nextSlot++;
    }

    private static void recordsCounterInc() {
        recordsCounter++;
    }

    /**
     * This method creates a new block in datafile and resets slots counter.
     */
    public static void createNewBlock() {
        OutputStreamWriter writer;
        try {
            writer =
                    new OutputStreamWriter(new FileOutputStream("datafile.txt", true),
                            StandardCharsets.UTF_8);
            writer.write("\n\nblock" + blocksCounter + "\n");
            writer.close();
            nextSlot = 0;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method creates the file and inserts the block 0.
     */
    public static void initializeFile() {
        OutputStreamWriter writer;
        try {
            writer =
                    new OutputStreamWriter(new FileOutputStream("datafile.txt"),
                            StandardCharsets.UTF_8);

            writer.write("block0\n");

            StringBuilder newData = new StringBuilder();
            newData.append(blocksCounter);
            newData.append(PROPERTIES_DELIMITER);
            newData.append(recordsCounter);
            newData.append(PROPERTIES_DELIMITER);
            newData.append(totalBytes);
            newData.append(PROPERTIES_DELIMITER);
            newData.append(numberOfCoords);
            newData.append("\n\n\n\n\n\n\n");

            String data = newData.toString();

            writer.write(data);

            writer.close();
            blocksCounterInc();
            createNewBlock();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This method checks if a block has space to add a record.
     * @param input
     */
    public static void checkSize(String input) {
        int sizeofRecord = calculateSizeofString(input);
        if (currentSizeofBlock >= sizeofRecord) {
            currentSizeofBlock -= sizeofRecord;
            slotsCounterInc();
        } else {
            currentSizeofBlock = BLOCK_SIZE;
            currentSizeofBlock -= sizeofRecord;
            blocksCounterInc();
            createNewBlock();
        }

    }

    /**
     * This method inserts in datafile the new record
     * @param input
     */
    public static void saveToFile(String input) {
        checkSize(input);
        try {
            if (blocksCounter == 0) {
                initializeFile();
            } else {
                //me true 2i parametro kanei append
                OutputStreamWriter writer =
                        new OutputStreamWriter(new FileOutputStream("datafile.txt", true),
                                StandardCharsets.UTF_8);

                writer.write(input);
                writer.write("|");

                writer.close();

                recordsCounterInc();
                slotsCounterInc();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public static String getRecordId(){
        int temp = nextSlot-1;
        return blocksCounter + "_" + temp;
    }

    /**
     * This method updates the data of the block 0.
     * We have to call it when we have entered all the data.
     */
    public static void finalizeFile() {
        try {
            RandomAccessFile file = new RandomAccessFile("datafile.txt", "rw");

            // seek to the beginning of the file
            file.seek(0);

            // write new data to the file
            // isos kai na min einai se UTF, alla den mas peirazei
            // to writeUTF method den doulevei kala
            file.writeBytes("block0\n");

            StringBuilder newData = new StringBuilder();
            newData.append(blocksCounter);
            newData.append(PROPERTIES_DELIMITER);
            newData.append(recordsCounter);
            newData.append(PROPERTIES_DELIMITER);
            newData.append(totalBytes);
            newData.append(PROPERTIES_DELIMITER);
            newData.append(numberOfCoords);
            newData.append("\n\n");
            String data = newData.toString();

            file.writeBytes(data);

            file.close();
        } catch (Exception e) {
            throw new RuntimeException();
        } ;
    }

}
