import java.util.List;

// record that stores data for viewing a table and generating a report for the table
// label field: holds the text shown in the main menu
// sql field: holds the database command to run
// prompts field: holds the input prompts shown to the user
public record Query(String label, String sql, List<String> prompts) {

    // Constructor for a Query
    // String... allows us to pass multiple number of prompts
    public static Query of(String label, String sql, String... prompts){

        //  Builds the query and adds all prompts into a list
        return new Query(label, sql, List.of(prompts));
    }
}
