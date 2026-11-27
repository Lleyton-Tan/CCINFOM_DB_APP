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

                case 2:

                    updateRecord();

                case 3:

                    deleteRecord();

                case 4:

                    viewsMenu();

                case 5:

                    reportsMenu();

                case 6:

                    return;

                default:

                    System.out.println("Invalid choice!");
            }
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

            String sql = "INSERT INTO menu_item (menu_item_code, name, price,"
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

    public void updateRecord(){


    }

    public void deleteRecord(){


    }

    public void viewsMenu(){


    }

    public void reportsMenu(){


    }
}
