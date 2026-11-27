import java.util.Scanner;
import java.math.BigDecimal;
import java.util.Arrays;

public class Input {

    // Shared scanner object for the whole project
    private static final Scanner scanner = new Scanner(System.in);

    // Shows a prompt, then returns whatever the user typed (extra spaces removed)
    public static String readLine(String prompt) {

        System.out.print(prompt);

        return scanner.nextLine();

    }

    // Keeps prompting for an input until the user types one (for NOT NULL fields)
    public static String readRequired(String prompt) {

        while (true) {

            String s = readLine(prompt);

            if (!s.isEmpty()) return s;

            System.out.println("This field cannot be empty.");
        }
    }

    // Reads a String anc checks if it is a valid integer
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

    //Reads a String and checks if it is a valid decimal
    public static String readDecimal(String prompt) {

        while (true) {

            String s = readLine(prompt);

            try {

                if (new BigDecimal(s).signum() >= 0) return s;

                System.out.println("Input must be greater than or equal to 0!");

            } catch (NumberFormatException e) {

                System.out.println("Please enter a valid number!");
            }
        }
    }

    // Reads and only accepts if the input is in one of the given options, ignotes upper or lowercase
    public static String readChoice(String prompt, String... options) {
        while (true) {

            String s = readLine(prompt + " " + Arrays.toString(options) + ": ");

            for (String o : options) if (o.equalsIgnoreCase(s)) return o;

            System.out.println("Please choose one of: " + String.join(", ", options));
        }
    }

}
