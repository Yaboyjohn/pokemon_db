package pokemon.commands;

import pokemon.DatabaseUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SelectCommand {
    DatabaseUtils databaseUtils = new DatabaseUtils();

    /**
     * Usage: All
     * Retrieves every entry in the DB and returns it in alphabetical order
     * @param tableName
     * @param orderById
     */
    public void selectAll(String tableName, boolean orderById){
        String emptyCheck = "SELECT count(*) FROM " + tableName;
        String sql;
        if (orderById) {
            sql = "SELECT * FROM " + tableName + " ORDER BY id";
        } else {
            sql = "SELECT * FROM " + tableName + " ORDER BY name";
        }
        try (Connection conn = databaseUtils.connect();
             Statement stmt  = conn.createStatement();
             Statement stmt2 = conn.createStatement();
             ResultSet check = stmt2.executeQuery(emptyCheck);
             ResultSet rs    = stmt.executeQuery(sql)){

            if (!check.next()) {
                System.out.println(tableName + " table has no entries");
                return;
            }
            System.out.printf("%-8s%-13s%-15s%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
                    "ID", "Name", "Item", "HP", "Attack", "Defense", "Sp.Attack", "Sp.Def", "Speed", "Move 1",
                    "Move 2", "Move 3", "Move 4");
            // loop through the result set
            while (rs.next()) {
                System.out.printf("%-8s%-13s%-15s%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("item"),
                        rs.getInt("hp"),
                        rs.getInt("attack"),
                        rs.getInt("defense"),
                        rs.getInt("spattack"),
                        rs.getInt("spdefense"),
                        rs.getInt("speed"),
                        rs.getString("move1"),
                        rs.getString("move2"),
                        rs.getString("move3"),
                        rs.getString("move4"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public int selectID(int ID, String tableName) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = " + ID + " ORDER BY ID";
        int count = 0;
        try (Connection conn = databaseUtils.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            while (rs.next()) {
                count++;
                System.out.println(rs.getInt("id") +  "\t" +
                        rs.getString("name") + "\t" +
                        rs.getString("item") + "\t" +
                        rs.getInt("hp") + "\t" +
                        rs.getInt("attack") + "\t" +
                        rs.getInt("defense") + "\t" +
                        rs.getInt("spattack") + "\t" +
                        rs.getInt("spdefense") + "\t" +
                        rs.getInt("speed") + "\t" +
                        rs.getString("move1") + "        " +
                        rs.getString("move2") + "        " +
                        rs.getString("move3") + "        " +
                        rs.getString("move4") + "\t");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return count;
    }

    public String selectName(int pokemon_id, String tableName) {
        String sql = "SELECT name FROM " + tableName + " WHERE id = " + pokemon_id;
        String res = null;
        try (Connection conn = databaseUtils.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            res = rs.getString("name");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return res;
    }


    public int selectName(String pokemon_name, boolean ordered, String tableName) {
        String sql = null;
        if (ordered) sql = "SELECT * FROM " + tableName + " WHERE name LIKE " + "'" + pokemon_name + "'" + " ORDER BY item";
        else sql =  "SELECT * FROM " + tableName + " WHERE name LIKE " + "'" + pokemon_name + "'";
        int count = 0;
        try (Connection conn = databaseUtils.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            System.out.printf("%-8s%-13s%-15s%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
                    "ID", "Name", "Item", "HP", "Attack", "Defense", "Sp.Attack", "Sp.Def", "Speed", "Move 1",
                    "Move 2", "Move 3", "Move 4");
            while (rs.next()) {
                count++;
                System.out.printf("%-8s%-13s%-15s%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("item"),
                        rs.getInt("hp"),
                        rs.getInt("attack"),
                        rs.getInt("defense"),
                        rs.getInt("spattack"),
                        rs.getInt("spdefense"),
                        rs.getInt("speed"),
                        rs.getString("move1"),
                        rs.getString("move2"),
                        rs.getString("move3"),
                        rs.getString("move4"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return count;
    }

    public int selectItem(String item, String tableName) {
        String sql = "SELECT * FROM " + tableName + " WHERE item LIKE " + "'" + item + "' ORDER BY name";
        int count = 0;
        try (Connection conn = databaseUtils.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)) {
            System.out.printf("%-8s%-13s%-15s%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
                    "ID", "Name", "Item", "HP", "Attack", "Defense", "Sp.Attack", "Sp.Def", "Speed", "Move 1",
                    "Move 2", "Move 3", "Move 4");
            while (rs.next()) {
                count++;
                System.out.printf("%-8s%-13s%-15s%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("item"),
                        rs.getInt("hp"),
                        rs.getInt("attack"),
                        rs.getInt("defense"),
                        rs.getInt("spattack"),
                        rs.getInt("spdefense"),
                        rs.getInt("speed"),
                        rs.getString("move1"),
                        rs.getString("move2"),
                        rs.getString("move3"),
                        rs.getString("move4"));
            }
            //ANSI CODE
//            System.out.printf("%-8s%-13s" + BLACK_BOLD + "Item" + ANSI_RESET + rightPadding("", 15 - "item".length()) + "%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
//                    "ID", "Name", "HP", "Attack", "Defense", "Sp.Attack", "Sp.Def", "Speed", "Move 1",
//                    "Move 2", "Move 3", "Move 4");
//            while (rs.next()) {
//                count++;
//                System.out.printf("%-8s%-13s"  + ANSI_GREEN + rs.getString("item") + ANSI_RESET + rightPadding("", 15 - rs.getString("item").length()) + "%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
//                        rs.getInt("id"),
//                        rs.getString("name"),
//                        rs.getInt("hp"),
//                        rs.getInt("attack"),
//                        rs.getInt("defense"),
//                        rs.getInt("spattack"),
//                        rs.getInt("spdefense"),
//                        rs.getInt("speed"),
//                        rs.getString("move1"),
//                        rs.getString("move2"),
//                        rs.getString("move3"),
//                        rs.getString("move4"));
//            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return count;
    }

    public int selectMove(String move, String tableName) {
        String sql = "SELECT * FROM " + tableName + " WHERE move1 LIKE " + "'" + move + "' OR " +
                "move2 LIKE " + "'" + move + "' OR " +  "move3 LIKE " + "'" + move + "' OR "
                +  "move4 LIKE " + "'" + move + "' ORDER BY name";
        int count = 0;
        try (Connection conn = databaseUtils.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            System.out.printf("%-8s%-13s%-15s%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
                    "ID", "Name", "Item", "HP", "Attack", "Defense", "Sp.Attack", "Sp.Def", "Speed", "Move 1",
                    "Move 2", "Move 3", "Move 4");
            while (rs.next()) {
                count++;
                System.out.printf("%-8s%-13s%-15s%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("item"),
                        rs.getInt("hp"),
                        rs.getInt("attack"),
                        rs.getInt("defense"),
                        rs.getInt("spattack"),
                        rs.getInt("spdefense"),
                        rs.getInt("speed"),
                        rs.getString("move1"),
                        rs.getString("move2"),
                        rs.getString("move3"),
                        rs.getString("move4"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return count;
    }

    public int selectStat(String stat, String operator, int num, String tableName) {
        String sql = "SELECT * FROM " + tableName + " WHERE " + stat + " " + operator + " " + num + " ORDER BY " + stat + " DESC";
        int count = 0;
        try (Connection conn = databaseUtils.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            System.out.printf("%-8s%-13s%-15s%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
                    "ID", "Name", "Item", "HP", "Attack", "Defense", "Sp.Attack", "Sp.Def", "Speed", "Move 1",
                    "Move 2", "Move 3", "Move 4");
            while (rs.next()) {
                count++;
                System.out.printf("%-8s%-13s%-15s%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("item"),
                        rs.getInt("hp"),
                        rs.getInt("attack"),
                        rs.getInt("defense"),
                        rs.getInt("spattack"),
                        rs.getInt("spdefense"),
                        rs.getInt("speed"),
                        rs.getString("move1"),
                        rs.getString("move2"),
                        rs.getString("move3"),
                        rs.getString("move4"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return count;
    }
}
