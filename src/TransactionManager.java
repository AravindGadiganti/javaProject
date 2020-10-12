import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

public class TransactionManager extends ReadWrite {
    private static final String BASE_DIR = "./Resource/Transaction/";

    /**
     * Method is to deposit money to users accountNumber and modify recode transaction in user file.
     *
     * @param accountNumber:User's account number
     * @param amount:Amount        to be deposit
     * @param beforeBalance:Amount before deposit
     */
    public String deposit(String accountNumber, Double amount, Double beforeBalance) {
        UUID uuid = UUID.randomUUID();
        String date = new Date().toLocaleString().replace(" ", "");
        String transactionType = "deposit";
        String preBalance = beforeBalance + "";
        double balance = beforeBalance + amount;
        Path path = Paths.get(BASE_DIR + accountNumber);
        if (!Files.exists(path)) {
            //dir.mkdir();
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String depositData = amount + "|" + date + "|" + transactionType + "|"
                + preBalance + "|" + balance + "|" + uuid.toString();
        writeFile(BASE_DIR + accountNumber + "/" + transactionType + "_" + uuid.toString() + ".txt", depositData);
        return depositData;
    }

    /**
     * Method is to withdraw money to users accountNumber and modify recode transaction in user file.
     *
     * @param accountNumber:User's accountNumber number
     * @param amount:Amount        to be deposit
     * @param beforeBalance:Amount before deposit
     */
    public String withdraw(String accountNumber, Double amount, Double beforeBalance) {
        UUID uuid = UUID.randomUUID();
        String date = new Date().toLocaleString().replace(" ", "");
        String transactionType = "withdraw";
        String preBalance = beforeBalance + "";
        double balance = beforeBalance - amount;
        Path path = Paths.get(BASE_DIR + accountNumber);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String depositData = amount + "|" + date + "|" + transactionType + "|"
                + preBalance + "|" + balance + "|" + uuid.toString();
        writeFile(BASE_DIR + accountNumber + "/" + transactionType +"_"+ uuid.toString()+".txt", depositData);
        return depositData;
    }

    /**
     * Method is to get transactions from user file and display all transactions of user.
     *
     * @param accountNumber:User's accountNumber number
     */
    public void findTransactions(Integer accountNumber) {
        String dirLocation = BASE_DIR + accountNumber + "/";
        try {
            File dir = new File(dirLocation);
            File[] files;
            files = dir.listFiles();
            int i = 0;
            System.out.println("Transaction Statement for account " + accountNumber + " is:");
            assert files != null;
            for (File file : files) {
                String transactionData = readFile(file.getAbsolutePath());
                i++;
                String[] fileData = transactionData.split("\\|");
                System.out.println(i + ".Transaction:" + fileData[5]);
                System.out.println("Amount " + fileData[0] + " " + fileData[2] + " on " + fileData[1]);
            }
        }
        catch(Exception e){
            System.out.println("No transactions found.");
        }
    }

    /**
     * Method is to get transaction counts in user file.
     *
     * @param accountNumber:User's accountNumber number
     */
    public int getTransactions(String accountNumber) {
        String dirLocation = BASE_DIR + accountNumber + "/";
        File dir = new File(dirLocation);
        File[] files;
        files = dir.listFiles();
        assert files != null;
        return files.length;
    }

}
	
    	
 

