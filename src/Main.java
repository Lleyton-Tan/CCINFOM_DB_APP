import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    void main(){

        try (Connection conn = DBConnections.getConnection()) {

            System.out.println("DB Connected!");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
