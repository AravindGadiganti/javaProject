import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ReadWrite {

    /**
     * Method is tp read data from the given file.
     *
     * @param filePath:Path of the file
     */
    public String readFile(String filePath) {
        Scanner read;
        String value = "null";
        File file = new File(filePath);
        try {
            read = new Scanner(file);
            value = read.next();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }

        return value;
    }

    /**
     * Method is tp write data from the given file.
     *
     * @param fileName:Path of the file
     * @param data:Data needs to write into file
     */
    public void writeFile(String fileName, String data) {

        File myObj = new File(fileName);
        try {
            FileWriter myWriter = new FileWriter(myObj);
            myWriter.flush();
            myWriter.write(data);
            myWriter.close();
            System.out.println("Successfully wrote to the file:" + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred in file:" + fileName);
            e.printStackTrace();
        }

    }

}
