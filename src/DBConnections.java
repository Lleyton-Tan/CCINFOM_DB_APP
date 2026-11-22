// Used to read files (db.properties)
import java.io.File;
import java.io.FileInputStream;

// Used for throwing exceptions when file is not found
import java.io.IOException;

// Used to connect to the MySQL database
import java.sql.Connection;

// Creates the database connection
import java.sql.DriverManager;

// throws an error for database problems when a wrong password or MySQL is not running
import java.sql.SQLException;

// Reads the key=value lines from db.properties text file
import java.util.Properties;


public class DBConnections {

    public static Connection getConnection() throws SQLException{

        // creates the properties object
        Properties props = new Properties();

        // opens the file from your project root
        try(FileInputStream in = new FileInputStream("db.properties")){

            // reads every key=value line in the db.properties into props
            props.load(in);

        }catch (IOException e){

            throw new RuntimeException("Could not read dp.properties file", e);
        }

        // Opens a connection to the MySQL database using the url, username, and password from db.properties
        return DriverManager.getConnection(props.getProperty("db.url"), props.getProperty("db.user"), props.getProperty("db.password"));
    }
}
