import java.sql.*;

public class Menu_Item_Table {

    private static final String[] Status = {"Available", "Unavailable"};
    private static final String[] Category = {"Beverage", "Sides", "Dessert", "Main"};

    public void menu(){

        while(true){

            System.out.println("\n--- MENU_ITEM ---");
            System.out.println("[1] Create New Record");
            System.out.println("[2] Update Existing Record");
            System.out.println("[3] Delete Existing Record");
            System.out.println("[4] Views Menu");
            System.out.println("[5] Reports");
            System.out.println("[6] Back To Main Menu");

            int choice = Input.readInt("Choice: ");

            switch(choice){

                case 1:

                    createRecord();

                    break;

                case 2:

                    updateRecord();

                    break;

                case 3:

                    deleteRecord();

                    break;

                case 4:

                    viewsMenu();

                    break;

                case 5:

                    reportsMenu();

                    break;

                case 6:

                    return;

                default:

                    System.out.println("Invalid choice!");
            }
        }
    }

    // initializes the menu_item table
    public static void createTable(){

        try(Connection con = DBConnections.getConnection();

            Statement st = con.createStatement()){

            st.executeUpdate("""
            CREATE TABLE IF NOT EXISTS menu_item (
                menu_item_code INT PRIMARY KEY,
                menu_item_name VARCHAR(100) NOT NULL,
                price DECIMAL(10,2) NOT NULL,
                category VARCHAR(50),
                description VARCHAR(255),
                availability_status ENUM('Available', 'Unavailable') NOT NULL DEFAULT 'Available'
            )
            """);
        }

        catch(SQLException e){

            System.out.println("Error on setting up menu_item table: " + e.getMessage());
        }
    }

    public void createRecord(){

        String menuItemName = Input.readRequired("Menu item name: ");
        String menuItemPrice = Input.readDecimal("Menu item price: ");
        String menuItemCategory = Input.readChoice("Menu item category", Category);
        String menuItemDescription = Input.readLine("Menu Item Description: ");
        String availabilityStatus = Input.readChoice("Availability status", Status);

        // opens a connection to MySQL
        try (Connection con = DBConnections.getConnection()) {

            // holds the new menu item's code
            int nextCode;

            // asks MySQL for the highest code currently in the table
            try (Statement st = con.createStatement();

                 ResultSet rs = st.executeQuery(
                         "SELECT COALESCE(MAX(CAST(menu_item_code AS UNSIGNED)), 1000) FROM menu_item")) {

                // moves to the first row of the result
                rs.next();

                // reads that number and adds 1 -> 1001, 1002
                nextCode = rs.getInt(1) + 1;
            }

            String sql = "INSERT INTO menu_item (menu_item_code, menu_item_name, price,"
                    + "category, description, availability_status)"
                    + "VALUES (?, ?, ?, ?, ?, ?)";

            // prepares the statement
            try(PreparedStatement ps = con.prepareStatement(sql)){

                // sets the first ? to the new menu item code
                ps.setInt(1, nextCode);
                // sets the second ? to the menu item name
                ps.setString(2, menuItemName);
                // sets the third ? to the menu item price
                ps.setString(3, menuItemPrice);
                // sets the fourth ? to the category
                ps.setString(4, menuItemCategory);

                // Description is optional set to null if no description was typed
                if(menuItemDescription.isEmpty()){

                    ps.setNull(5, Types.VARCHAR);
                }
                else{

                    ps.setString(5, menuItemDescription);
                }

                ps.setString(6, availabilityStatus);

                // runs the insert
                ps.executeUpdate();

                System.out.println("Record created. Menu item code: " + nextCode);
            }
        }
        catch (SQLException e){

            System.out.println("Error: " + e.getMessage());
        }
    }

    // updates a row/record
    public void updateRecord(){

        String code = String.valueOf(Input.readInt("Menu item code to update: "));
        System.out.println("\nLeave a field blank and press Enter to keep its current value.");

        String name = Input.readLine("New name: ");
        String price = Input.readOptionalDecimal("New Menu item price: ");
        String category = Input.readOptionalChoice("New category", Category);
        String description = Input.readLine("New description: ");
        String status = Input.readOptionalChoice("New availability status", Status);

        // COALESCE(NULLIF(?, ''), column): if the input is blank, keep the old value
        int rows = update("UPDATE menu_item SET "
                + "menu_item_name = COALESCE(NULLIF(?, ''), menu_item_name), "
                + "price = COALESCE(NULLIF(?, ''), price), "
                + "category = COALESCE(NULLIF(?, ''), category), "
                + "description = COALESCE(NULLIF(?, ''), description), "
                + "availability_status = COALESCE(NULLIF(?, ''), availability_status) "
                + "WHERE menu_item_code = ?",
                name, price, category, description, status, code);

        // at least 1 row was changed
        if(rows > 0){

            System.out.println("Record updated!");
        }
        else if(rows == 0){

            System.out.println("No record found!");
        }
    }

    // removes a row/record
    public void deleteRecord(){

        String code = String.valueOf(Input.readInt("Menu item code to delete: "));

        if(!Input.readLine("Delete " + code + "? (y/n): ").equalsIgnoreCase("y")){

            System.out.println("Cancelling deletion of record");

            return;
        }

        int rows = update("DELETE FROM menu_item WHERE menu_item_code = ?", code);

        if(rows > 0){

            System.out.println("Record with menu item code " + code + " deleted");
        }
        else if(rows == 0){

            System.out.println("No record found with menu item code " + code + ".");
        }
    }

    // shows the possible views for the table
    public void viewsMenu(){

        while(true){

            System.out.println("\n--- MENU_ITEM VIEWS ---");
            System.out.println("[1] All Records");
            System.out.println("[2] View By Availability");
            System.out.println("[3] View By Category");
            System.out.println("[4] View By Price Range");
            System.out.println("[5] Back");

            int choice = Input.readInt("Choice: ");

            switch(choice){

                case 1:

                    show("SELECT * FROM menu_item");

                    break;

                case 2: {

                    String choiceStatus = Input.readChoice("Status", Status);

                    show("SELECT * FROM menu_item WHERE availability_status = ?", choiceStatus);

                    break;
                }

                case 3: {

                    String choiceCategory = Input.readChoice("Category", Category);

                    show("SELECT * FROM menu_item WHERE category = ?", choiceCategory);

                    break;
                }

                case 4: {

                    String min_price = Input.readDecimal("Minimum price: ");
                    String max_price = Input.readDecimal("Maximum price: ");

                    show("SELECT * FROM menu_item WHERE price BETWEEN ? AND ? ", min_price, max_price);

                    break;
                }

                case 5:

                    return;

                default:

                    System.out.println("Invalid choice!");

            }
        }
    }

    public void reportsMenu(){


    }

    // runs insert, update, and delete. blank values are replaced as NULL
    // returns the number of rows changed
    private int update(String sql, String... params){

        try(Connection con = DBConnections.getConnection();

            PreparedStatement ps = con.prepareStatement(sql)){

            // Fills each ? in order (position numbers start at 1)
            for(int i = 0; i< params.length; i++){

                // A blank value becomes NULL
                if(params[i].isEmpty()){

                    ps.setNull(i + 1, Types.VARCHAR);
                }
                else{

                    ps.setString(i + 1, params[i]);
                }
            }

            //runs the command and returns how many rows it affected
            return ps.executeUpdate();
        }
        catch(SQLException e){

            System.out.println("Error: " + e.getMessage());

            return -1;
        }
    }

    // runs a SELECT statement and prints the result as a table
    private void show(String sql, String... params){

        try(Connection con = DBConnections.getConnection();

            PreparedStatement ps = con.prepareStatement(sql)){

            // Fills each ? with the values passed in
            for(int i = 0; i< params.length; i++){

                ps.setString(i + 1, params[i]);
            }

            // runs the SELECT rs holds the rows that came back
            try(ResultSet rs = ps.executeQuery()){

                // metadata is the info about the result (column names and number of columns)
                ResultSetMetaData md = rs.getMetaData();

                int cols = md.getColumnCount();

                System.out.println();

                // prints the column names as a header, each padded to 22 characters wide
                for(int i = 1; i <= cols; i++){

                    System.out.printf("%-22s", md.getColumnLabel(i));
                }

                // prints a divider
                System.out.println("\n" + "-".repeat(22 * cols));

                int rows = 0;

                // rs.next() moves to the next row
                while(rs.next()){

                    // Prints every column of this row
                    for(int i = 1; i <= cols; i++){

                        System.out.printf("%-22s", rs.getString(i));
                    }

                    System.out.println();

                    rows++;
                }

                // prints how many rows were found
                System.out.println("(" + rows + " rows)");
            }
        }

        catch(SQLException e){

            System.out.println("Error: " + e.getMessage());
        }
    }
}
