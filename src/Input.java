import java.util.Scanner;

public class Input {

    // Shared scanner object for the whole project
    private static final Scanner scanner = new Scanner(System.in);

    // Shows a prompt, then returns whatever the user typed (extra spaces removed)
    public static String readLine(String prompt) {

        System.out.print(prompt);

        return scanner.nextLine();

    }

    // Reads an integer
    public static int readInt(String prompt){

        while(true){

            try{

                // converts the scanned string into an integer
                return Integer.parseInt(readLine(prompt));
            }
            catch (NumberFormatException e){

                System.out.println("Please enter a number!");
            }
        }
    }
}
