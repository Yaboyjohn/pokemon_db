package pokemon;

import java.sql.*;

public class DatabaseUtils {
    final String dbUrl = "jdbc:sqlite:pokemon.db";
    /**
     * establishes db connection with the hardcoded pokemon database (
     * @return connection object
     */
    public Connection connect() {
        // SQLite connection string
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbUrl);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * creates a new database from fileName argument. Meant to be used once to setup the db initially
     * @param fileName
     */
    public static void createNewDatabase(String fileName) {
        String url = "jdbc:sqlite:" + fileName;
        try (Connection conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * creates a new table for the hardcoded pokemon db with a preset schema
     * @param tableName
     */
    public void createNewTable(String tableName) {
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS " + tableName + "(\n"
                + "	id integer PRIMARY KEY,\n"
                + "	name text NOT NULL,\n"
                + "	item text NOT NULL, \n"
                + "	hp integer NOT NULL,\n"
                + "	attack integer NOT NULL,\n"
                + "	defense integer NOT NULL,\n"
                + "	spattack integer NOT NULL,\n"
                + "	spdefense integer NOT NULL,\n"
                + "	speed integer NOT NULL,\n"
                + "	move1 text,\n"
                + "	move2 text, \n"
                + "	move3 text,\n"
                + "	move4 text\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * deletes the table passed in from the hardcoded pokemon db
     * @param tableName
     */
    public void deleteTable(String tableName) {
        String sql = "DROP TABLE IF EXISTS " + tableName;
        try (Connection conn = DriverManager.getConnection(dbUrl);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
