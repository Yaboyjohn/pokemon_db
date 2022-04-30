package pokemon.commands;

import pokemon.DatabaseUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class contains all the logic for the implementation of the GET command
 */
public class GetCommand {
    DatabaseUtils databaseUtils = new DatabaseUtils();

    /**
     * Usage: All
     * Retrieves every entry in the DB and returns it in alphabetical order
     * @param tableName
     * @param orderById
     */
    public void selectAll(String tableName, boolean orderById){
        String emptyCheckQuery = "SELECT count(*) FROM " + tableName;
        String sql;
        if (orderById) {
            sql = "SELECT * FROM " + tableName + " ORDER BY id";
        } else {
            sql = "SELECT * FROM " + tableName + " ORDER BY name";
        }
        try (Connection conn = databaseUtils.connect();
             // statement that will execute actual select sql query
             Statement stmt  = conn.createStatement();

             // statement that will execute emptyCheck sql query
             Statement stmt2 = conn.createStatement();

             ResultSet emptyCheck = stmt2.executeQuery(emptyCheckQuery);
             ResultSet rs    = stmt.executeQuery(sql)){

            if (!emptyCheck.next()) {
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

    /**
     * Used internally for the update command
     * Retrieves all info about single pokemon from their ID
     * @param ID
     * @param tableName
     * @return 400|Vaporeon|lum berry|443|166|156|327|268|149|surf|ice beam|acid armor|rest|
     */
    public int selectPokemonByID(int ID, String tableName) {
        String sql = "i " + tableName + " WHERE id = " + ID + " ORDER BY ID";
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

    /**
     * Used internally for the update command
     * Retrieves only name about single pokemon from their ID
     * @param pokemon_id
     * @param tableName
     * @return Vaporeon|
     */
    public String selectNameById(int pokemon_id, String tableName) {
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


    /**
     * Usage: Get [pokemon name]
     * Retrieves all variants of a pokemon
     * @param pokemon_name
     * @param orderByItem boolean value denoting whether or not to order by item. Is set to True for simple Get cases (eg. get suicune). Is set to False for delete or update cases
     * @param tableName
     * @return number of variants of a pokemon (this return value is not used anywhere)
     *
     * We order by item to make it easier compare similar variants for GET command
     * Eg, Results without ordering (note that without ordering, the results are returned in ID order)
     * 3|Suicune|chesto berry|404|167|266|306|266|206|surf|ice beam|calm mind|rest|
     * 18|Suicune|chesto berry|367|153|292|220|292|190|surf|ice beam|calm mind|rest|
     * 70|Suicune|brightpowder|341|167|266|306|266|269|surf|ice beam|bite|reflect|
     * 75|Suicune|chesto berry|383|167|308|237|308|206|surf|ice beam|calm mind|rest|
     * 181|Suicune|lum berry|341|167|266|306|266|269|surf|ice beam|rain dance|roar|
     * 311|Suicune|lum berry|341|186|266|306|266|242|surf|ice beam|rain dance|roar|
     * 399|Suicune|chesto berry|341|186|266|306|266|242|surf|double team|calm mind|rest|
     * 431|Suicune|lum berry|404|167|266|306|266|206|surf|ice beam|calm mind|icy wind|
     * 432|Suicune|leftovers|383|167|308|216|338|206|toxic|dive|double team|protect|
     * 520|Suicune|chesto berry|341|167|266|306|266|269|surf|double team|calm mind|rest|
     * 561|Suicune|lum berry|404|186|266|306|266|185|surf|ice beam|calm mind|icy wind|
     * 608|Suicune|brightpowder|341|186|266|306|266|242|surf|ice beam|bite|reflect|
     *
     * Results with ordering
     * 70|Suicune|brightpowder|341|167|266|306|266|269|surf|ice beam|bite|reflect|
     * 608|Suicune|brightpowder|341|186|266|306|266|242|surf|ice beam|bite|reflect|
     * 3|Suicune|chesto berry|404|167|266|306|266|206|surf|ice beam|calm mind|rest|
     * 18|Suicune|chesto berry|367|153|292|220|292|190|surf|ice beam|calm mind|rest|
     * 75|Suicune|chesto berry|383|167|308|237|308|206|surf|ice beam|calm mind|rest|
     * 399|Suicune|chesto berry|341|186|266|306|266|242|surf|double team|calm mind|rest|
     * 520|Suicune|chesto berry|341|167|266|306|266|269|surf|double team|calm mind|rest|
     * 432|Suicune|leftovers|383|167|308|216|338|206|toxic|dive|double team|protect|
     * 181|Suicune|lum berry|341|167|266|306|266|269|surf|ice beam|rain dance|roar|
     * 311|Suicune|lum berry|341|186|266|306|266|242|surf|ice beam|rain dance|roar|
     * 431|Suicune|lum berry|404|167|266|306|266|206|surf|ice beam|calm mind|icy wind|
     * 561|Suicune|lum berry|404|186|266|306|266|185|surf|ice beam|calm mind|icy wind|
     *
     */
    public int selectName(String pokemon_name, boolean orderByItem, String tableName) {
        String sql = null;
        if (orderByItem) sql = "SELECT * FROM " + tableName + " WHERE name LIKE " + "'" + pokemon_name + "'" + " ORDER BY item";
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

    /**
     * Usage: get [item name]
     * Retrieves all pokemon that hold the desired item
     * @param item
     * @param tableName
     * @return number of pokemon that hold the desired item (this return value is not used anywhere)
     */
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return count;
    }

    /**
     * Usage: get [move name]
     * Retrieves all pokemon that know the desired move
     * @param move
     * @param tableName
     * @return number of pokemon that know the desired move (this return value is not used anywhere)
     */
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

    /**
     * Usage: get [stat] [operator] [value] (eg. get attack > 300)
     * Retrieves all pokemon that hold the desired item
     * @param stat can be one of hp, attack, defense, spattack, spdefense, speed. The valid stat check is handled in main pokemon class
     * @param operator can be one of <, >, =
     * @param num value of the desired stat
     * @param tableName
     * @return number of pokemon that hold the desired item (this return value is not used anywhere)
     */
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
