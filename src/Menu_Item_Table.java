import java.sql.*;

public class Menu_Item_Table {

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
