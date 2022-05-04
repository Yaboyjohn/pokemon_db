package pokemon.commands;

import pokemon.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class that contains logic for the UPDATE command
 */
public class UpdateCommand {
    DatabaseUtils databaseUtils = new DatabaseUtils();

    /**
     * Implementation for UPDATE command
     * @param id ID of the pokemon in the DB
     * @param colName name of the column we want to update
     * @param newVal new value we want to insert
     * @param tableName name of table we want to update
     * @param isStat True if the colName is a pokemon stat, False if the colName is a not stat related
     */
    public void update(int id, String colName, String newVal, String tableName, boolean isStat) {
        String sql = "UPDATE " + tableName + " SET " + colName + " = ? where id = ?" ;
        try (Connection conn = databaseUtils.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            if (isStat) {
                pstmt.setInt(1, Integer.parseInt(newVal));
                pstmt.setInt(2, id);
            } else {
                pstmt.setString(1, newVal);
                pstmt.setInt(2, id);
            }
            // update
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
