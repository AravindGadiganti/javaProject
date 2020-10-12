import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class UserDetailsManagers extends ReadWrite {

    private final static String userFileDir = "./Resource/Accounts/";

    /**
     * Method is to take user data and write them into file
     *
     * @param in:user input
     */

    public String createUserDetails(Scanner in) {
        System.out.println("Enter the user name");
        final String username = in.next();
        System.out.println("Enter the password");
        String password = in.next();
        System.out.println("Enter the user type employee or user");
        String type = in.next();
        System.out.println("Enter the phone number");
        String phoneNumber = in.next();
        System.out.println("Enter the Email ID");
        String emailId = in.next();
        System.out.println("Enter the Address");
        String address = in.next();
        String contentToWrite = username + "|" + password + "|" + type + "|" + phoneNumber + "|" + emailId + "|" + address;
        Path path = Paths.get(userFileDir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeFile(userFileDir + username + ".txt", contentToWrite);
        return contentToWrite;
    }

    /**
     * Method is to read user data file.
     *
     * @param username:user's username
     * @return String
     */
    public String read(final String username) {
        return readFile(userFileDir + username + ".txt");
    }

    /**
     * Method is to allow user to change password and modify user data file.
     *
     * @param in:user's    input
     * @param value:user's data
     */
    public void changePassword(Scanner in, String value) {
        System.out.println("Enter your old password");
        String password = in.next();
        String[] data = value.split("\\|");
        if (data[1].equals(password)) {
            System.out.println("Enter your new password");
            String newPassword = in.next();
            data[1] = newPassword;
            String newValue = String.join("|", data);
            writeFile(userFileDir + data[0]+".txt", newValue);
        } else {
            System.out.println("Please enter the correct password");
        }
    }

    /**
     * Method is to allow user to change phone number and modify user data file.
     *
     * @param in:user's    input
     * @param value:user's data
     */
    public String changePhoneNumber(Scanner in, String value) {
        System.out.println("Enter your new phone number");
        String phoneNumber = in.next();
        String[] data = value.split("\\|");
        data[3] = phoneNumber;
        String newValue = String.join("|", data);
        writeFile(userFileDir + data[0]+".txt", newValue);
        return phoneNumber;
    }

}
