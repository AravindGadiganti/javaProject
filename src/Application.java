import java.util.List;
import java.util.Scanner;

public class Application {
    static UserDetailsManagers userManager = new UserDetailsManagers();

    /**
     * Main method is to execute the methods according to functionality.
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        login(in);
    }

    /**
     * Method is to login into application
     *
     * @param in:user's input.
     */
    public static void login(Scanner in) {
        String loginUserData;
        System.out.println("Enter the username");
        String userName = in.next();
        loginUserData = userManager.read(userName);
        if (loginUserData.equals("null")) {
            System.out.println("User doesn't exist.Please login with valid credentials. ");
            login(in);
        }
        String[] userData = loginUserData.split("\\|");
        if (userData[0].equals(userName) && userData[2].equalsIgnoreCase("User")) {
            System.out.println("Enter the password");
            String password = in.next();
            if (userData[1].equals(password)) {
                chooseOptionPage(in, userName);
            } else {
                System.out.println("Login failed");
                login(in);
            }
        } else if (userData[0].equals(userName) && userData[2].equalsIgnoreCase("Employee")) {
            System.out.println("Enter the password");
            String password = in.next();
            if (userData[1].equals(password)) {
                chooseOptionPage(in, userName);
            } else {
                System.out.println("Login failed");
                login(in);
            }
        } else {
            System.out.println("Please enter valid username");
        }

    }

    /**
     * Method is to choose options from the menu displayed
     *
     * @param in:user's input.
     * @param user      :User's data
     */
    public static void chooseOptionPage(Scanner in, String user) {
        System.out.println("Choose an option");
        if (user.equalsIgnoreCase("Employee")) {
            System.out.println("1. Add new user details(Employee or User)");
            System.out.println("2. Add account details for existing user");
        } else {
            System.out.println("1. Perform transactions");
            System.out.println("2. Change password");
            System.out.println("3. Change phone number");
        }
        System.out.println("0. logout");
        optionOperation(in, user);
    }

    /**
     * Method is to perform opertions for the user selected options from the menu displayed
     *
     * @param in:user's input.
     * @param user      :User's data
     */
    public static void optionOperation(Scanner in, String user) {
        AccountManager accountManager = new AccountManager();
        String accountsData = userManager.read(user);
        int choice = in.nextInt();
        if (accountsData.split("\\|")[2].equalsIgnoreCase("Employee")) {
            choice = choice + 3;
        }
        switch (choice) {
            case 0:
                System.out.println("Exiting the system");
                break;
            case 1:
                System.out.println("Choose your account:");
                int i = 1;
                int accountChoice;
                List<String> userAccounts = accountManager.findUserAccounts(user);
                for (String userAccountFile : userAccounts) {
                    String[] accountNumber =userAccountFile.replace(".txt","").split("_");
                    System.out.println(i + ")  " + accountNumber[1]);
                    i++;
                }
                accountChoice = in.nextInt();
                if (accountChoice <= userAccounts.size()) {
                    String finalUserAccount = userAccounts.get(accountChoice - 1);
                    accountOperations(in, finalUserAccount, user);
                } else {
                    System.out.println("Enter a valid option");
                    chooseOptionPage(in, user);
                }
                break;
            case 2:
                userManager.changePassword(in, accountsData);
                System.out.println("Password is changed");
                login(in);
                break;
            case 3:
                String number = userManager.changePhoneNumber(in, accountsData);
                System.out.println("Phone number is changed to " + number);
                System.out.println("Press 1 to display previous options");
                int backMenu = in.nextInt();
                if (backMenu == 1)
                    chooseOptionPage(in, user);
                else
                    System.out.println("Logged out Successfully");
                login(in);
                break;
            case 4:
                if (!user.equalsIgnoreCase("Employee")) {
                    System.out.println("Please choose a valid option");
                    optionOperation(in, user);
                } else {
                    String newUser = userManager.createUserDetails(in);
                    String[] usedData = newUser.split("\\|");
                    System.out.println("User " + usedData[0] + " is created");
                    if(usedData[2].equalsIgnoreCase("User")) {
                        accountManager.createUserAccount(in, newUser);
                        System.out.println("Account is created");
                    }
                    login(in);
                    break;
                }
            case 5:
                if (!user.equalsIgnoreCase("Employee")) {
                    System.out.println("Please choose a valid option");
                    optionOperation(in, user);
                } else {
                    System.out.println("Enter username");
                    String name = in.next();
                    String modifiedUser = userManager.read(name);
                    String[] userData = modifiedUser.split("\\|");
                    accountManager.createUserAccount(in, userData[0]);
                    System.out.println("Account is created");
                    login(in);
                    break;
                }
            default:
                System.out.println("Please choose a valid option");
                optionOperation(in, user);
        }

    }

    /**
     * Method is to perform account related opertions for the user selected options from the menu displayed
     *
     * @param in:user's               input.
     * @param finalUserAccount:User's data
     * @param user                    :User's data
     */
    public static void accountOperations(Scanner in, String finalUserAccount, String user) {
        int choice;

        AccountManager accountManager = new AccountManager();
        String accountData = accountManager.readFile(accountManager.accountDir + finalUserAccount);
        String[] finalUserData = accountData.split("\\|");
        System.out.println("Please choose the transaction to perform");
        System.out.println("1) Display your balance");
        System.out.println("2) Deposit money to your account");
        System.out.println("3) Withdraw money from your account");
        System.out.println("4) Transfer money to other accounts");
        System.out.println("5) Display transactions statement");
        if (finalUserData[0].equals("chequing")) {
            System.out.println("6) Pay utility");
        }
        System.out.println("Choose any other number for previous menu");
        choice = in.nextInt();
        switch (choice) {
            case 1:
                double balance = Double.parseDouble(finalUserData[2]);
                System.out.println("Your account balance is " + balance);
                accountOperations(in, finalUserAccount, user);
                break;
            case 2:
                balance = accountManager.deposit(in, finalUserAccount);
                System.out.println("Amount deposited to account");
                System.out.println("Balance after deposit is " + balance);
                accountOperations(in, finalUserAccount, user);
                break;
            case 3:
                balance = accountManager.withDraw(in, finalUserAccount);
                System.out.println("Amount is withdrawn");
                System.out.println("Balance after withdraw is " + balance);
                accountOperations(in, finalUserAccount, user);
                break;
            case 4:
                balance = accountManager.transfer(in, finalUserAccount);
                System.out.println("Amount is transferred");
                System.out.println("Balance after transfer is " + balance);
                accountOperations(in, finalUserAccount, user);
                break;

            case 5:
                String[] accountNumbert = finalUserAccount.replace(".txt","").split("_");
                accountManager.accountTransaction(Integer.parseInt(accountNumbert[1]));
                accountOperations(in, finalUserAccount, user);
                break;
            case 6:
                if (!finalUserData[0].equals("chequing")) {
                    System.out.println("Please use a valid option");
                    chooseOptionPage(in, user);
                    break;

                }
                balance = accountManager.payUtility(in, finalUserAccount);
                System.out.println("Amount is transferred");
                System.out.println("Balance after transfer is " + balance);

                accountOperations(in, finalUserAccount, user);
                break;

            default:
                chooseOptionPage(in, user);
        }
    }
}



