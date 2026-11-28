import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args){

        try (Connection conn = DBConnections.getConnection()) {

            System.out.println("DB Connected!");

        } catch (SQLException e) {

            System.out.println("Could not connect: " + e.getMessage());

            return;
        }

        Menu_Item_Table.createTable();

        new MainMenu().start();

    }
}
