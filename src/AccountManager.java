import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountManager extends ReadWrite {
    protected final String accountDir = "./Resource/Account_Activities/";

    /**
     * Method is to get calculate interest for the different account types.
     *
     * @param balance      : User's account balance.
     * @param accountType: user's account type.
     */
    public static double calculateInterest(double balance, String accountType) {
        double interestAmount = 0;
        if (accountType.equalsIgnoreCase("chequing")) {
            interestAmount = 0.2 * balance;
        } else if (accountType.equalsIgnoreCase("saving")) {
            interestAmount = 0.5 * balance;
        } else if (accountType.equalsIgnoreCase("Money Market")) {
            interestAmount = 0.75 * balance;
        }
        return interestAmount;
    }

    /**
     * Method is to create account for an user and store the users data into file.
     *
     * @param in   : User input
     * @param user : User's Username
     */
    public void createUserAccount(Scanner in, String user) {
        System.out.println("Enter Account type as checking or saving or MoneyMarket ");
        String accountType;
        accountType = in.next();
        if (accountType.equals("checking")) {
            System.out.println("Enter the Account number");
            int accountNumber = in.nextInt();
            System.out.println("Enter the Balance");
            double balance = in.nextDouble();
            Path path = Paths.get(accountDir);
            if (!Files.exists(path)) {
                try {
                    Files.createDirectories(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String contentToWrite = accountType + "|" + accountNumber + "|" + balance;
            writeFile(accountDir + user + "_" + accountNumber + ".txt", contentToWrite);
        } else if (accountType.equals("saving")) {
            System.out.println("Enter the Account number");
            int accountNumber = in.nextInt();
            System.out.println("Enter the Balance");
            double balance = in.nextDouble();
            double interest = calculateInterest(balance, "saving");
            balance = balance + interest;
            String contentToWrite = accountType + "|" + accountNumber + "|" + balance + "|" + interest;
            Path path = Paths.get(accountDir);
            if (!Files.exists(path)) {
                try {
                    Files.createDirectories(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            writeFile(accountDir + user + "_" + accountNumber+ ".txt", contentToWrite);
        } else if (accountType.equals("MoneyMarket")) {
            System.out.println("Enter the Account number");
            int accountNumber = in.nextInt();
            System.out.println("Enter the Balance");
            double balance = in.nextDouble();
            if (!(2500 < balance) && !(balance > 25000)) {
                System.out.println("Balance between 2500 and 25000 can be taken for Money Market Account");
            }
            double interest = calculateInterest(balance, "Money Market");
            balance = balance + interest;
            String contentToWrite = accountType + "|" + accountNumber + "|" + balance + "|" + interest;
            Path path = Paths.get(accountDir);
            if (!Files.exists(path)) {
                try {
                    Files.createDirectories(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            writeFile(accountDir + user + "_" + accountNumber+ ".txt", contentToWrite);
        } else {
            createUserAccount(in, user);
        }
    }

    /**
     * Method is to read users data into file.
     *
     * @param username      : Username
     * @param accountNumber : User's account number
     */
    public String read(final String username, String accountNumber) {
        return readFile(username + "_" + accountNumber);
    }

    /**
     * Method is to deposit money to the user account and modify user related files.
     *
     * @param in   : User's input
     * @param File : User's account file
     */
    public double deposit(Scanner in, String File) {
        AccountManager accountManager = new AccountManager();
        String[] depositUserData = accountManager.readFile(accountDir + File).split("\\|");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TransactionManager transactionManager = new TransactionManager();
        System.out.println("Please enter your amount to deposit");
        double balanceAmount = Double.parseDouble(depositUserData[2]);
        double amount = in.nextDouble();
        String transactionDetails = transactionManager.deposit(depositUserData[1], amount, balanceAmount);
        String[] transactionDetailsArray = transactionDetails.split("\\|");
        depositUserData[2] = transactionDetailsArray[4];
        String newValue = String.join("|", depositUserData);
        writeFile(accountDir + File, newValue);
        return Double.parseDouble(transactionDetailsArray[4]);
    }

    /**
     * Method is to withdraw money to the user account and modify user related files.
     *
     * @param in   : User's input
     * @param File : User's account file
     */
    public double withDraw(Scanner in, String File) {
        AccountManager accountManager = new AccountManager();
        String[] withdrawUserData = accountManager.readFile(accountDir + File).split("\\|");
        TransactionManager transactionManager = new TransactionManager();
        System.out.println("Please enter your amount to withdraw");
        double balanceAmount = Double.parseDouble(withdrawUserData[2]);
        double amount = in.nextDouble();
        String transactionDetails = transactionManager.withdraw(withdrawUserData[1], amount, balanceAmount);
        String[] transactionDetailsArray = transactionDetails.split("\\|");
        withdrawUserData[2] = transactionDetailsArray[4];
        String newValue = String.join("|", withdrawUserData);
        writeFile(accountDir + File, newValue);
        return Double.parseDouble(transactionDetailsArray[4]);
    }

    /**
     * Method is to transfer money to the receiver account from sender account and modify user related files.
     *
     * @param in       : User's input
     * @param userFile : User's account file
     */
    public double transfer(Scanner in, String userFile) {
        int trasanctionCount = 0;
        String[] senderUserData = readFile(accountDir + userFile).split("\\|");
        double balanceAmount = Double.parseDouble(senderUserData[2]);
        System.out.println("Enter the account number to transfer money");
        int toAccount = in.nextInt();
        System.out.println("Please enter the amount to transfer");
        double amount = in.nextDouble();
        TransactionManager transactionManager = new TransactionManager();
        try {
             trasanctionCount = transactionManager.getTransactions(senderUserData[1]);
        }
        catch(Exception ignored){
        }
        if (trasanctionCount > 6 && (senderUserData[1].equalsIgnoreCase("saving")
                || senderUserData[1].equalsIgnoreCase("Money Market"))) {
            //If transactions are more than 6 then 35$ fee will be applied.
            amount = amount + 35;
        }
        String transactionDetails = transactionManager.withdraw(senderUserData[1], amount, balanceAmount);
        String[] senderTransactionArray = transactionDetails.split("\\|");
        senderUserData[2] = senderTransactionArray[4];
        String newValue = String.join("|", senderUserData);
        writeFile(accountDir + userFile, newValue);
        //deposit to toAccount
        String userName = searchFilePath(toAccount);
        String accountNumber = toAccount + "";
        String[] recieverUserData = read(accountDir + userName, accountNumber + ".txt").split("\\|");
        double balanceAmountOther = Double.parseDouble(recieverUserData[2]);
        String depositeData = transactionManager.deposit(recieverUserData[1], amount, balanceAmountOther);
        String[] receiverTransactionArray = depositeData.split("\\|");
        recieverUserData[2] = receiverTransactionArray[4];
        String Value = String.join("|", recieverUserData);
        writeFile(accountDir + userName + "_" + accountNumber + ".txt", Value);
        return Double.parseDouble(senderTransactionArray[4]);
    }

    /**
     * Method is to pay utility bills and modify user related files.
     *
     * @param in   : User's input
     * @param File : User's account file
     */
    public double payUtility(Scanner in, String File) {
        AccountManager accountManager = new AccountManager();
        String[] senderUserData = accountManager.readFile(accountDir + File).split("\\|");
        TransactionManager transactionManager = new TransactionManager();
        System.out.println("Enter the bill amount");
        double balanceAmount = Double.parseDouble(senderUserData[2]);
        double amount = in.nextDouble();
        String transactionDetails = transactionManager.withdraw(senderUserData[1], amount, balanceAmount);
        String[] transactionDetailsArray = transactionDetails.split("\\|");
        senderUserData[2] = transactionDetailsArray[4];
        String newValue = String.join("|", senderUserData);
        writeFile(accountDir + File, newValue);
        return Double.parseDouble(transactionDetailsArray[4]);
    }

    /**
     * Method is to get the user name from the given account number.
     *
     * @param accountNumber : User's account number
     */
    public String searchFilePath(Integer accountNumber) {
        File dir = new File(accountDir);
        File[] files;
        String userName = null;
        files = dir.listFiles();
        assert files != null;
        for (File file : files) {
            String fileName = file.getName();
            if (fileName.endsWith("_" + accountNumber + ".txt")) {
                String[] name = fileName.split("_");
                userName = name[0];
            }
        }
        return userName;
    }

    /**
     * Method is to get the user accounts from the given user name.
     *
     * @param username : User's username
     */
    public List<String> findUserAccounts(String username) {
        List<String> userAccounts = new ArrayList<>();
        File dir = new File(accountDir);
        File[] files;
        files = dir.listFiles();
        assert files != null;
        for (File file : files) {
            String fileName = file.getName();
            if (fileName.startsWith(username + "_")) {
                userAccounts.add(fileName);
            }
        }
        return userAccounts;
    }

    /**
     * Method is to get the count of transanctions from the given account number.
     *
     * @param accountNumber : User's account number.
     */
    public void accountTransaction(Integer accountNumber) {
        TransactionManager transactionManager = new TransactionManager();
        transactionManager.findTransactions(accountNumber);
    }


}
