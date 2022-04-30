package pokemon;

import pokemon.commands.GetCommand;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.*;
import java.util.Map.Entry;

public class Pokemon_DB {
    DatabaseUtils databaseUtils = new DatabaseUtils();

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    //USAGE: System.out.println(ANSI_RED + "This text is red!" + ANSI_RESET);
    public static final String GREEN = "\033[0;32m";   // GREEN
    public static final String RESET = "\033[0m";  // Text Reset
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String BLACK_BOLD = "\033[1;30m";


    // Both these variables are used for the COUNT command
    public static int total = 0;
    public static HashMap<String, Integer> pokemonCountMap = new HashMap<>();

    // Contains the name of the main database table we are working with
    public static String currTable = "pokemon";


    public static String rightPadding(String str, int num) {
        return String.format("%1$-" + num + "s", str);
    }



    public void insert(String name, String item, int hp, int attack, int defense,
                       int spattack, int spdefense, int speed, String move1, String move2,
                       String move3, String move4) {
        String sql = "INSERT INTO pokemon(name, item, hp, attack, defense, spattack, spdefense, speed, move1, move2, move3, move4) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = databaseUtils.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, item);
            pstmt.setInt(3, hp);
            pstmt.setInt(4, attack);
            pstmt.setInt(5, defense);
            pstmt.setInt(6, spattack);
            pstmt.setInt(7, spdefense);
            pstmt.setInt(8, speed);
            pstmt.setString(9, move1);
            pstmt.setString(10, move2);
            pstmt.setString(11, move3);
            pstmt.setString(12, move4);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

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

    public void delete(int id, String tableName) {
        String sql = "DELETE FROM " + tableName + " WHERE id = ?";

        try (Connection conn = databaseUtils.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAll(String tableName) {
        String sql = "DELETE FROM " + tableName;

        try (Connection conn = databaseUtils.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void avg(String tableName) {
        String sql = "SELECT ROUND(AVG(hp),2) as hp, ROUND(AVG(attack),2) as attack, ROUND(AVG(defense),2) as defense, " +
                "ROUND(AVG(spattack),2) as spattack, ROUND(AVG(spdefense),2) as spdefense, ROUND(AVG(speed),2) as speed FROM " + tableName;
        System.out.printf("%-10s%-10s%-10s%-10s%-10s%-10s\n",
                "HP", "Attack", "Defense", "Sp.Attack", "Sp.Def", "Speed");
        try (Connection conn = databaseUtils.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
             while (rs.next()) {
                 System.out.printf("%-10s%-10s%-10s%-10s%-10s%-10s\n",
                        rs.getDouble("hp"),
                        rs.getDouble("attack"),
                        rs.getDouble("defense"),
                        rs.getDouble("spattack"),
                        rs.getDouble("spdefense"),
                        rs.getDouble("speed"));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void max(String stat, String tableName) {
        String sql = "SELECT * FROM " + tableName + " ORDER BY " + stat + " DESC LIMIT 1";
        System.out.printf("%-8s%-13s%-15s%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
                "ID", "Name", "Item", "HP", "Attack", "Defense", "Sp.Attack", "Sp.Def", "Speed", "Move 1",
                "Move 2", "Move 3", "Move 4");
        try (Connection conn = databaseUtils.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
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

    public void min(String stat, String tableName) {
        String sql = "SELECT * FROM " + tableName + " ORDER BY " + stat + " ASC LIMIT 1";
        System.out.printf("%-8s%-13s%-15s%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
                "ID", "Name", "Item", "HP", "Attack", "Defense", "Sp.Attack", "Sp. Def", "Speed", "Move 1",
                "Move 2", "Move 3", "Move 4");
        try (Connection conn = databaseUtils.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
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

    public void sql(String sql) {
        try (Connection conn = databaseUtils.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql))
        {
            while (rs.next()) {
                ResultSetMetaData meta = rs.getMetaData();
                int numColumns = meta.getColumnCount();
                for (int i = 1; i <= numColumns; i++) {
                    String colType = meta.getColumnTypeName(i);
                    if (colType.equals("TEST") || colType.equals("TEXT")) System.out.print(rs.getString(i) + "|");
                    else if (colType.equals("INTEGER")) System.out.print(rs.getInt(i) + "|");
                }
                System.out.println("");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void size(String tableName) {
        String sql = "SELECT COUNT(*) as count FROM " + tableName;
        try (Connection conn = databaseUtils.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
             System.out.println("Num Entries: " + rs.getInt("count"));
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void sort(String stat, String tableName) {
        String sql = "SELECT * FROM " + tableName + " ORDER BY " + stat + " ASC";
        System.out.printf("%-8s%-13s%-15s%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
                "ID", "Name", "Item", "HP", "Attack", "Defense", "Sp.Attack", "Sp. Def", "Speed", "Move 1",
                "Move 2", "Move 3", "Move 4");
        try (Connection conn = databaseUtils.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
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

    public String setString(int stat, double avgStat) {
        String res;
        if (stat > avgStat) res = ANSI_GREEN + stat + ANSI_RESET + rightPadding("", 10 - Integer.toString(stat).length());
        else if (stat < avgStat) res = ANSI_RED + stat + ANSI_RESET + rightPadding("", 10 - Integer.toString(stat).length());
        else {
            res = ANSI_BLUE + stat + ANSI_RESET + rightPadding("", 10 - Integer.toString(stat).length());
        }
        return res;
    }

    public void maxStatIndividualPokemon(String pokemon, String tableName) {
        String sql = "select max(hp) as hp, max(attack) as attack, max(defense) as defense, max(spattack) as spattack," +
                " max(spdefense) as spdefense, max(speed) as speed from " + tableName + " where name like '" + pokemon + "'";
        try (Connection conn = databaseUtils.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            while (rs.next()) {
                System.out.printf("%36s%-10s%-10s%-10s%-10s%-10s%-10s\n",
                        pokemon.substring(0, 1).toUpperCase() + pokemon.substring(1) + " max: ",
                        rs.getInt("hp"),
                        rs.getInt("attack"),
                        rs.getInt("defense"),
                        rs.getInt("spattack"),
                        rs.getInt("spdefense"),
                        rs.getInt("speed"));

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String formatString(String category, String value) {
        if (category.equals("hp") || category.equals("attack") || category.equals("defense") ||
                category.equals("spattack") || category.equals("spdefense") || category.equals("speed")) {
            return BLACK_BOLD + value + ANSI_RESET + rightPadding("", 10 - value.length());
        } else if (category.equals("id")) {
            return BLACK_BOLD + value + ANSI_RESET + rightPadding("", 8 - value.length());
        } else if (category.equals("name")) {
            return BLACK_BOLD + value + ANSI_RESET + rightPadding("", 13 - value.length());
        } else if (category.equals("item")) {
            return BLACK_BOLD + value + ANSI_RESET + rightPadding("", 15 - value.length());
        } else {
            return BLACK_BOLD + value + ANSI_RESET + rightPadding("", 13 - value.length());
        }

    }

    /**
     *
     * @param pokemon (The pokemon this function retrieves to compare agains the passed in stats)
     * @param mode
     * @param tableName
     * prints out all the entries of the pokemon passed in and compares the stats of each entry the query returns to the averages
     * of the other pokemon whose stats we also passed in
     */
    public void avgSelectName(String pokemon, double avgHp, double avgAttack, double avgDef,
                              double avgSpattack, double avgSpdef, double avgSpeed, int mode, String tableName) {
        String sql = null;
        if (mode == 0 || mode  == 2 || mode == -1) {
            //compare [pokemon1] all (mode 0) OR compare [pokemon1] [pokemon2] (mode 2)
            sql = "SELECT * FROM " + tableName + " WHERE name LIKE " + "'" + pokemon + "'" + " ORDER BY item";
            try (Connection conn = databaseUtils.connect();
                 Statement stmt  = conn.createStatement();
                 ResultSet rs    = stmt.executeQuery(sql)){
                int hp;
                int attack;
                int defense;
                int spattack;
                int spdefense;
                int speed;

                System.out.printf("%-8s%-13s%-15s%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
                        "ID", "Name", "Item", "HP", "Attack", "Defense", "Sp.Attack", "Sp.Def", "Speed", "Move 1",
                        "Move 2", "Move 3", "Move 4");
                while (rs.next()) {
                    hp = rs.getInt("hp");
                    attack = rs.getInt("attack");
                    defense = rs.getInt("defense");
                    spattack = rs.getInt("spattack");
                    spdefense = rs.getInt("spdefense");
                    speed = rs.getInt("speed");
                    String printedHp = setString(hp, avgHp);
                    String printedAttack = setString(attack, avgAttack);
                    String printedDefense = setString(defense, avgDef);
                    String printedSpattack = setString(spattack, avgSpattack);
                    String printedSpdefense = setString(spdefense, avgSpdef);
                    String printedSpeed = setString(speed, avgSpeed);
                    System.out.printf("%-8s%-13s%-15s%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("item"),
                            printedHp,
                            printedAttack,
                            printedDefense,
                            printedSpattack,
                            printedSpdefense,
                            printedSpeed,
                            rs.getString("move1"),
                            rs.getString("move2"),
                            rs.getString("move3"),
                            rs.getString("move4"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } else if (mode == 1) {
            //1 = compare all [pokemon1]
            sql = "SELECT * FROM " + tableName;
            try (Connection conn = databaseUtils.connect();
                 Statement stmt  = conn.createStatement();
                 ResultSet rs    = stmt.executeQuery(sql)){
                int hp;
                int attack;
                int defense;
                int spattack;
                int spdefense;
                int speed;

                System.out.printf("%-8s%-13s%-15s%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
                        "ID", "Name", "Item", "HP", "Attack", "Defense", "Sp.Attack", "Sp.Def", "Speed", "Move 1",
                        "Move 2", "Move 3", "Move 4");
                while (rs.next()) {
                    hp = rs.getInt("hp");
                    attack = rs.getInt("attack");
                    defense = rs.getInt("defense");
                    spattack = rs.getInt("spattack");
                    spdefense = rs.getInt("spdefense");
                    speed = rs.getInt("speed");
                    String printedHp = setString(hp, avgHp);
                    String printedAttack = setString(attack, avgAttack);
                    String printedDefense = setString(defense, avgDef);
                    String printedSpattack = setString(spattack, avgSpattack);
                    String printedSpdefense = setString(spdefense, avgSpdef);
                    String printedSpeed = setString(speed, avgSpeed);
                    System.out.printf("%-8s%-13s%-15s%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("item"),
                            printedHp,
                            printedAttack,
                            printedDefense,
                            printedSpattack,
                            printedSpdefense,
                            printedSpeed,
                            rs.getString("move1"),
                            rs.getString("move2"),
                            rs.getString("move3"),
                            rs.getString("move4"));
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else {
            // compare [id]
            // get all entries corresponding to the pokemon we want EXCEPT the entry with the id we passed in
            // we need to translate the mode back to the one we passed in
            sql = "SELECT * from " + tableName + " where name like '" + pokemon + "' and id != " + (mode-5);
            String idPokemon = "SELECT * from " + tableName + " where id = " + (mode-5);
            try (Connection conn = databaseUtils.connect();
                 Statement stmt  = conn.createStatement();
                 ResultSet rs    = stmt.executeQuery(sql);
                 Statement idQuery = conn.createStatement();
                 ResultSet rs2 = idQuery.executeQuery(idPokemon)){
                int hp;
                int attack;
                int defense;
                int spattack;
                int spdefense;
                int speed;

                System.out.printf("%-8s%-13s%-15s%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
                        "ID", "Name", "Item", "HP", "Attack", "Defense", "Sp.Attack", "Sp.Def", "Speed", "Move 1",
                        "Move 2", "Move 3", "Move 4");
                while (rs.next()) {
                    hp = rs.getInt("hp");
                    attack = rs.getInt("attack");
                    defense = rs.getInt("defense");
                    spattack = rs.getInt("spattack");
                    spdefense = rs.getInt("spdefense");
                    speed = rs.getInt("speed");
                    String printedHp = setString(hp, avgHp);
                    String printedAttack = setString(attack, avgAttack);
                    String printedDefense = setString(defense, avgDef);
                    String printedSpattack = setString(spattack, avgSpattack);
                    String printedSpdefense = setString(spdefense, avgSpdef);
                    String printedSpeed = setString(speed, avgSpeed);
                    System.out.printf("%-8s%-13s%-15s%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("item"),
                            printedHp,
                            printedAttack,
                            printedDefense,
                            printedSpattack,
                            printedSpdefense,
                            printedSpeed,
                            rs.getString("move1"),
                            rs.getString("move2"),
                            rs.getString("move3"),
                            rs.getString("move4"));
                }
                System.out.printf("%-8s%-13s%-15s%-10s%-10s%-10s%-10s%-10s%-10s%-13s%-13s%-13s%-13s\n",
                        formatString("id", Integer.toString(rs2.getInt("id"))),
                        formatString("name", rs2.getString("name")),
                        formatString("item", rs2.getString("item")),
                        formatString("hp", Integer.toString(rs2.getInt("hp"))),
                        formatString("attack", Integer.toString(rs2.getInt("attack"))),
                        formatString("defense", Integer.toString(rs2.getInt("defense"))),
                        formatString("spattack", Integer.toString(rs2.getInt("spattack"))),
                        formatString("spdefense", Integer.toString(rs2.getInt("spdefense"))),
                        formatString("speed", Integer.toString(rs2.getInt("speed"))),
                        formatString("move1", rs2.getString("move1")),
                        formatString("move2", rs2.getString("move2")),
                        formatString("move3", rs2.getString("move3")),
                        formatString("move4", rs2.getString("move4")));
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        }

    }

    /** Modes:
     *
     * @param pokemon1
     * @param pokemon2
     * @param mode (0 = compare [pokemon1] all, 1 = compare all [pokemon1], 2 = compare [pokemon1] [pokemon2])
     * @param tableName
     * @return
     */
    public int compare(String pokemon1, String pokemon2, int mode, String tableName) {
        if (pokemon1.equals("") || pokemon1 == null) return -1;

        // compare suicune
        if (pokemon2 == null) {
            String averages = "select * from (select ROUND(AVG(hp),2) as hp, ROUND(avg(attack),2) as attack, ROUND(avg(defense),2) as defense, " +
                    "ROUND(avg(spattack),2) as spattack, ROUND(avg(spdefense),2) as spdefense, ROUND(avg(speed),2) as speed" +
                    " from (select * from " + tableName + " where name like '" + pokemon1  + "'))";
            // query returns 1 line containing just the average stats of pokemon1
            try (Connection conn = databaseUtils.connect();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(averages)){
                while (rs.next()) {
                    //assign the avgs of pokemon 1 to these variables
                    double hp = rs.getDouble("hp");
                    double attack = rs.getDouble("attack");
                    double defense = rs.getDouble("defense");
                    double spattack = rs.getDouble("spattack");
                    double spdefense = rs.getDouble("spdefense");
                    double speed = rs.getDouble("speed");
                    // Store the averages in variables
                    // CALL THE UPDATED SELECT NAME THAT TAKES IN AVERAGES AND DOES COLORED PRINTING
                    avgSelectName(pokemon1, hp, attack, defense, spattack, spdefense, speed, mode, currTable);

                    // AT THE VERY END CALL THIS PRINT STATEMENT
                    System.out.printf("%36s%-10s%-10s%-10s%-10s%-10s%-10s\n",
                            pokemon1.substring(0, 1).toUpperCase() + pokemon1.substring(1) + " avg: ",
                            hp,
                            attack,
                            defense,
                            spattack,
                            spdefense,
                            speed);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        } else if (pokemon2.equals("id")) {
            //compare [id]
            // we pass in ID for mode
            // get the 1 line corresponding to the pokemon whose ID we passed in
            String sql = "select * from pokemon where id = " + mode;
            String averages = "select * from (select ROUND(AVG(hp),2) as hp, ROUND(avg(attack),2) as attack, ROUND(avg(defense),2) as defense, " +
                    "ROUND(avg(spattack),2) as spattack, ROUND(avg(spdefense),2) as spdefense, ROUND(avg(speed),2) as speed" +
                    " from (select * from " + tableName + " where name like '" + pokemon1  + "'))";
            try (Connection conn = databaseUtils.connect();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql);
                 Statement averStmt = conn.createStatement();
                 ResultSet rs2 = averStmt.executeQuery(averages)) {
                while (rs.next()) {
                    //assign the stats of the ID pokmeon to these variables
                    int hp = rs.getInt("hp");
                    int attack = rs.getInt("attack");
                    int defense = rs.getInt("defense");
                    int spattack = rs.getInt("spattack");
                    int spdefense = rs.getInt("spdefense");
                    int speed = rs.getInt("speed");
                    // CALL THE UPDATED SELECT NAME THAT TAKES IN AVERAGES AND DOES COLORED PRINTING
                    //we increment mode by 5 to make sure it doesn't conflict with modes 0,1,2
                    avgSelectName(pokemon1, hp, attack, defense, spattack, spdefense, speed, mode+5, currTable);

                    // AT THE VERY END CALL THIS PRINT STATEMENT
                    System.out.printf("%36s%-10s%-10s%-10s%-10s%-10s%-10s\n",
                            pokemon1.substring(0, 1).toUpperCase() + pokemon1.substring(1) + " avg: ",
                            rs2.getDouble("hp"),
                            rs2.getDouble("attack"),
                            rs2.getDouble("defense"),
                            rs2.getDouble("spattack"),
                            rs2.getDouble("spdefense"),
                            rs2.getDouble("speed"));
                    maxStatIndividualPokemon(pokemon1, currTable);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }


        } else {
            // a valid 2nd arg was passed in
            if (mode == 0) {
                // compare suicune all (compare only suicunes against averages of all pokemon)
                String sql = "select * from (select ROUND(AVG(hp),2) as hp, ROUND(avg(attack),2) as attack, ROUND(avg(defense),2) as defense, " +
                        "ROUND(avg(spattack),2) as spattack, ROUND(avg(spdefense),2) as spdefense, ROUND(avg(speed),2) as speed from  " + currTable + ")";
                try (Connection conn = databaseUtils.connect();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)){
                    while (rs.next()) {
                        double hp = rs.getDouble("hp");
                        double attack = rs.getDouble("attack");
                        double defense = rs.getDouble("defense");
                        double spattack = rs.getDouble("spattack");
                        double spdefense = rs.getDouble("spdefense");
                        double speed = rs.getDouble("speed");

                        // the stats passed in are what the averages of [pokemon1] are comparing against
                        avgSelectName(pokemon1, hp, attack, defense, spattack, spdefense, speed, mode, currTable);

                        System.out.printf("%36s%-10s%-10s%-10s%-10s%-10s%-10s\n",
                                "All pokemon avg: ",
                                hp,
                                attack,
                                defense,
                                spattack,
                                spdefense,
                                speed);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            } else if (mode == 1){ //compare all [pokemon1]
                String sql = "select * from (select ROUND(AVG(hp),2) as hp, ROUND(avg(attack),2) as attack, ROUND(avg(defense),2) as defense, " +
                        "ROUND(avg(spattack),2) as spattack, ROUND(avg(spdefense),2) as spdefense, ROUND(avg(speed),2) as speed" +
                        " from (select * from " + tableName + " where name like '" + pokemon2  + "'))";
                try (Connection conn = databaseUtils.connect();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)){
                    while (rs.next()) {
                        double hp = rs.getDouble("hp");
                        double attack = rs.getDouble("attack");
                        double defense = rs.getDouble("defense");
                        double spattack = rs.getDouble("spattack");
                        double spdefense = rs.getDouble("spdefense");
                        double speed = rs.getDouble("speed");

                        // the stats passed in are what the averages of [pokemon1] are comparing against
                        avgSelectName(null, hp, attack, defense, spattack, spdefense, speed, mode, currTable);
                        System.out.printf("%36s%-10s%-10s%-10s%-10s%-10s%-10s\n",
                                pokemon2.substring(0, 1).toUpperCase() + pokemon2.substring(1) + " avg: ",
                                hp,
                                attack,
                                defense,
                                spattack,
                                spdefense,
                                speed);
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                // compare [pokemon1] [pokemon2]
                String sql = "select * from (select ROUND(AVG(hp),2) as hp, ROUND(avg(attack),2) as attack, ROUND(avg(defense),2) as defense, " +
                        "ROUND(avg(spattack),2) as spattack, ROUND(avg(spdefense),2) as spdefense, ROUND(avg(speed),2) as speed" +
                        " from (select * from " + tableName + " where name like '" + pokemon2  + "'))";
                try (Connection conn = databaseUtils.connect();
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(sql)){
                    while (rs.next()) {
                        double hp = rs.getDouble("hp");
                        double attack = rs.getDouble("attack");
                        double defense = rs.getDouble("defense");
                        double spattack = rs.getDouble("spattack");
                        double spdefense = rs.getDouble("spdefense");
                        double speed = rs.getDouble("speed");

                        // the stats passed in are what the averages of [pokemon1] are comparing against
                        avgSelectName(pokemon1, hp, attack, defense, spattack, spdefense, speed, mode, currTable);
                        System.out.printf("%36s%-10s%-10s%-10s%-10s%-10s%-10s\n",
                                pokemon2.substring(0, 1).toUpperCase() + pokemon2.substring(1) + " avg: ",
                                hp,
                                attack,
                                defense,
                                spattack,
                                spdefense,
                                speed);
                    }
                    maxStatIndividualPokemon(pokemon2, currTable);
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        // compare suicune all (compare all suicune entries with averages of all in db)
        return 0;
    }

    public static void insertToTable(Pokemon_DB db, String pokemon_name) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Item: ");
        String item = scan.nextLine();
        try {
            System.out.print("HP: ");
            int hp = Integer.parseInt(scan.nextLine());
            System.out.print("Attack: ");
            int attack = Integer.parseInt(scan.nextLine());
            System.out.print("Defense: ");
            int defense = Integer.parseInt(scan.nextLine());
            System.out.print("Special Attack: ");
            int spAttack = Integer.parseInt(scan.nextLine());
            System.out.print("Special Defense: ");
            int spDefense = Integer.parseInt(scan.nextLine());
            System.out.print("Speed: ");
            int speed = Integer.parseInt(scan.nextLine());
            System.out.print("Move 1: ");
            String move1 = scan.nextLine();
            System.out.print("Move 2: ");
            String move2 = scan.nextLine();
            System.out.print("Move 3: ");
            String move3 = scan.nextLine();
            System.out.print("Move 4: ");
            String move4 = scan.nextLine();
            db.insert(pokemon_name, item, hp, attack, defense, spAttack, spDefense, speed, move1, move2, move3, move4);
            System.out.println("SAVED");
        } catch (java.lang.NumberFormatException ex) {
            System.out.println("Inputs for stats must be integers");
            System.out.println("Insert cancelled");
        }
    }

    public boolean exists(String columnName, String value, String tableName) {
        String sql = "select * from " + tableName + " where " + columnName + " like '" + value + "'";
        int count = 0;
        try (Connection conn = databaseUtils.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            while (rs.next()) {
                count++;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return count != 0;

    }

    /** returns the first word in the input */
    public static Enums.COMMAND parseCommand(String input) {
        String[] inputArray = input.split(" ");
        String com = inputArray[0].toUpperCase();
        try {
            return Enums.COMMAND.valueOf(com);
        } catch (java.lang.IllegalArgumentException ex) {
            return null;
        }
    }

    /** returns the second word in input **/
    public static Enums.CATEGORY parseCategory(String input) {
        String[] inputArray = input.split(" ");
        String com = inputArray[1];
        if (com.equals("item")) return Enums.CATEGORY.ITEM;
        if (com.equals("hp")) return Enums.CATEGORY.HP;
        if (com.equals("attack")) return Enums.CATEGORY.ATTACK;
        if (com.equals("defense")) return Enums.CATEGORY.DEFENSE;
        if (com.equals("spattack")) return Enums.CATEGORY.SPATTACK;
        if (com.equals("spdefense")) return Enums.CATEGORY.SPDEFENSE;
        if (com.equals("speed")) return Enums.CATEGORY.SPEED;
        if (com.equals("move")) return Enums.CATEGORY.MOVE;
        if (com.equals("id")) return Enums.CATEGORY.ID;
        else return null;
    }

    /** returns the rest of the input after the second word **/
    public static String getArgument(String input) {
        String[] inputArray = input.split(" ");
        if (inputArray.length == 3) {
            return inputArray[2];
        } else if (inputArray.length == 4) {
            return inputArray[2] + " " + inputArray[3];
        } else if (inputArray.length == 5) {
            return inputArray[2] + " " + inputArray[3] + " " + inputArray[4];
        }
        return null;
    }

    /** returns a String[] where the first elem is the stat and the second is the value of the stat **/
    public static String[] parseStat(String input) {
        String[] res = new String[2];
        String[] inputArray = input.split(" ");
        res[0] = inputArray[2];
        res[1] = inputArray[3];
        return res;
    }

    public void backFillHashMap(String tableName) {
        pokemonCountMap.clear();
        total = 0;
        String sql = "SELECT * FROM " + tableName + " ORDER BY name";
        try (Connection conn = databaseUtils.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                String name = rs.getString("name");
                if (pokemonCountMap.containsKey(name)) {
                    pokemonCountMap.put(name, pokemonCountMap.get(name)+1);
                    total++;
                } else {
                    pokemonCountMap.put(name, 1);
                    total++;
                }
            }
            Map<String, Integer> sortedMap = sortMap(pokemonCountMap, true);
            printMap(sortedMap, total);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private static Map<String, Integer> sortMap(Map<String, Integer> map, boolean order) {
        List<Entry<String, Integer>> list = new LinkedList<Entry<String, Integer>>(map.entrySet());
        // Sorting the list based on values
        Collections.sort(list, new Comparator<Entry<String, Integer>>() {
            public int compare(Entry<String, Integer> o1,
                               Entry<String, Integer> o2) {
                if (order) {
                    return o1.getValue().compareTo(o2.getValue());
                } else {
                    return o2.getValue().compareTo(o1.getValue());
                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    public static void printMap(Map<String, Integer> map, int total) {
        //DecimalFormat numberFormat = new DecimalFormat("#.00");
        for (Entry<String, Integer> entry : map.entrySet()) {
            DecimalFormat df = new DecimalFormat("#.##%");
            System.out.printf("%-11s%-2s%-4s%-2s%-10s\n",
                    entry.getKey(), "|", entry.getValue(), "|", df.format((double) entry.getValue() / total));
        }
        System.out.println("Total: " + total);
        System.out.println("Unique Pokemon: " + map.size());
    }

    public static void main(String[] args) {
        Pokemon_DB app = new Pokemon_DB();
        GetCommand selectCommand = new GetCommand();
        app.databaseUtils.connect();
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("MAIN>");
            String input = scan.nextLine();
            String argument = getArgument(input);
            Enums.COMMAND command = parseCommand(input);

            String[] inputArray = input.split(" ");
            if (command == null) {
                System.out.println("Invalid Command. Type in 'help' for a guide. swag");
            }
            else if (command.equals(Enums.COMMAND.SORT)) {
                if (inputArray.length != 2) {
                    System.out.println("Wrong number of arguments. The SORT command follows the form: sort [stat]");
                    continue;
                }
                Enums.CATEGORY stat = parseCategory(input);
                if (stat == null)  {
                    System.out.println("An invalid stat was passed in. Only hp, attack, defense, spattack, " +
                            "spdefense, speed are accepted.");
                    continue;
                }
                app.sort(stat.toString(), currTable);

            }
            else if (command.equals(Enums.COMMAND.COUNT)) {
                app.backFillHashMap(currTable);
            }
            else if (command.equals(Enums.COMMAND.COMPARE)) {
                if (inputArray.length < 2) {
                    System.out.println("Wrong number of arguments. The COMPARE command follows the " +
                            "form: compare [pokemon1] [pokemon2] where either pokemon1 or pokemon2 can be 'all' but not both");
                    continue;
                }
                // ex. compare suicune
                String arg1 = inputArray[1];
                if (inputArray.length == 2) {
                    try {
                        int ID = Integer.parseInt(arg1);
                        // compare one pokemon against averages of all other variations of that pokemon
                        // compare all entries of pokemon with red/green if stat higher/lower than stats of id pokemon
                        // at the bottom include the id pokemon stat line with red/green for higher lower than avg stats of that pokemon
                        // at very bottom put max/avg stats of
                        String pokemon_name = selectCommand.selectNameById(ID, currTable);
                        app.compare(pokemon_name, "id", ID, currTable);

                    } catch (NumberFormatException e) {
                        if (!app.exists("name", arg1, currTable)) {
                            System.out.println("This pokmeon does not exist in the database");
                        } else {
                            app.compare(arg1, null, -1, currTable);
                        }
                    }
                }
                // ex. compare suicune latias OR compare suicune all
                else if (inputArray.length == 3) {
                    String arg2 = inputArray[2];
                    boolean valid2ndPokemon = app.exists("name", arg2, currTable) || arg2.equals("all");
                    boolean valid1stPokemon = app.exists("name", arg1, currTable) || arg1.equals("all");
                    if (!valid1stPokemon) System.out.println("The 1st pokemon passed in does not exist in the database");
                    else if (!valid2ndPokemon) System.out.println("The 2nd pokemon passed in does not exist in the database");
                    else if (arg1.equals("all")) {
                        //compare all suicune
                        app.compare(arg1, arg2, 1, currTable);
                    }
                    else {
                        if (arg2.equals("all")) {
                            //compare suicune all
                            app.compare(arg1, arg2, 0, currTable);
                        } else {
                            //compare suicune latias
                            if (arg1.equals(arg2)) {
                                System.out.println("Both pokemon are the same. Please enter 2 different pokemon");
                                continue;
                            }
                            app.compare(arg1, arg2, 2, currTable);
                            app.compare(arg2, arg1, 2, currTable);
                        }
                    }
                } else {
                    System.out.println("Wrong number of arguments. The COMPARE command follows the " +
                            "form: compare [pokemon1] [pokemon2] OR compare [pokemon1] all");
                }
            }
            else if (command.equals(Enums.COMMAND.SIZE)) {
                app.size(currTable);
            }
            else if (command.equals(Enums.COMMAND.AVG)) {
                app.avg(currTable);
            }
            else if (command.equals(Enums.COMMAND.SQL)) {
                System.out.println("PLease enter your sql statement");
                System.out.print("SQL>");
                String sql = scan.nextLine();
                app.sql(sql);
            }
            else if (command.equals(Enums.COMMAND.MAX)) {
                if (inputArray.length !=2) {
                    System.out.println("Wrong number of arguments. The MAX command follows the form: max [stat]");
                    continue;
                }
                Enums.CATEGORY stat = parseCategory(input);
                if (stat == null) {
                    System.out.println("An invalid stat was passed in. Only hp, attack, defense, spattack, " +
                            "spdefense, speed are accepted.");
                    continue;
                }
                app.max(stat.toString(), currTable);
            }
            else if (command.equals(Enums.COMMAND.MIN)) {
                if (inputArray.length !=2) {
                    System.out.println("Wrong number of arguments. The MIN command follows the form: min [stat]");
                    continue;
                }
                Enums.CATEGORY stat = parseCategory(input);
                if (stat == null) {
                    System.out.println("An invalid stat was passed in. Only hp, attack, defense, spattack, " +
                            "spdefense, speed are accepted.");
                    continue;
                }
                app.min(stat.toString(), currTable);
            }
            else if (command.equals(Enums.COMMAND.GET)) {
                if (inputArray.length < 2) {
                    System.out.println("Too few arguments. For a guide, type help");
                }
                // Edge case for Mr. Mime
                else if (input.equals("get mr. mime")) {
                    if (!app.exists("name", "Mr. Mime", currTable)) {
                        System.out.println("This pokemon is not in the database");
                        continue;
                    }
                    selectCommand.selectName("Mr. Mime", true, currTable);
                }
                // This is getting a single pokemon ex. get suicune
                else if (inputArray.length == 2) {
                    if (!app.exists("name", inputArray[1], currTable)) {
                        System.out.println("This pokemon is not in the database");
                        continue;
                    }
                    selectCommand.selectName(inputArray[1], true, currTable);
                }
                // This can be anything other than name ex. get item lum berry
                else {
                    Enums.CATEGORY category = parseCategory(input);
                    if (category == null) {
                        System.out.println("Invalid category. Acceptable categories are: item, id, move, hp, attack," +
                                " defense, spattack, spdefense, speed.");
                    }
                    else if (category.equals(Enums.CATEGORY.ITEM)) {
                        if (inputArray.length > 4)  {
                            System.out.println("Wrong number of arguments. The GET item command follows the form: get item [item name]. " +
                                    "Item name may be two words but no more than that.");
                            continue;
                        }
                        if (!app.exists("item", argument.replaceAll("'", "''"), currTable)) {
                            System.out.println("This item is not in the database");
                            continue;
                        }
                        selectCommand.selectItem(argument.replaceAll("'", "''"), currTable);
                    }
                    else if (category.equals(Enums.CATEGORY.MOVE)) {
                        if (inputArray.length > 4 && !input.equals("get move hi jump kick")) {
                            System.out.println("Wrong number of arguments. The GET item command follows the form: get item [item name]. " +
                                    "Item name may be two words but no more than that unless the move is 'hi jump kick'.");
                            continue;
                        }
                        if (!app.exists("move1", argument, currTable) && !app.exists("move2", argument, currTable) &&
                                !app.exists("move3", argument, currTable) && !app.exists("move4", argument, currTable)) {
                            System.out.println("This move is not in the database");
                            continue;
                        }
                        selectCommand.selectMove(argument, currTable);
                    }
                    // ex. get hp > 300, get hp = 300, get hp >= 300
                    else if (category.equals(Enums.CATEGORY.HP) || category.equals(Enums.CATEGORY.ATTACK)|| category.equals(Enums.CATEGORY.DEFENSE) ||
                            category.equals(Enums.CATEGORY.SPATTACK) || category.equals(Enums.CATEGORY.SPDEFENSE) || category.equals(Enums.CATEGORY.SPEED)) {
                        String stat = inputArray[1];
                        String operator = parseStat(input)[0];
                        int num = Integer.parseInt(parseStat(input)[1]);
                        // @TODO add validity check on operator. Currently "get attack * 100" and "get attack & 100" both work. Restrict operator to <,>,=
                        int count = selectCommand.selectStat(stat, operator, num, currTable);
                        if (count == 0) {
                            System.out.println("No pokemon satisfied the provided criteria.");
                        }
                    } else {
                        System.out.println("Invalid Arguments");
                    }
                }
            }
            else if (command.equals(Enums.COMMAND.HELP)) {
                System.out.println("INSERT HELP GUIDE HERE");
            }
            else if (command.equals(Enums.COMMAND.INSERT)) {
                if (inputArray.length == 3) {
                    if ((inputArray[1] + " " + inputArray[2]).equals("mr. mime")) {
                        insertToTable(app, "Mr. Mime"); } else {
                        System.out.println("Wrong number of arguments. The INSERT command follows the form: insert [pokemon]");
                        continue;
                    }
                }
                if (inputArray.length != 2) {
                    System.out.println("Wrong number of arguments. The INSERT command follows the form: insert [pokemon]");
                    continue;
                }
                String pokemonName = inputArray[1].substring(0, 1).toUpperCase() + inputArray[1].substring(1);
                insertToTable(app, pokemonName);
            }
            else if (command.equals(Enums.COMMAND.DELETE)) {
                // valid arguments check
                if (inputArray.length != 2) {
                    System.out.println("Wrong number of arguments. The DELETE command follows the form: delete [pokemon] OR delete [id]");
                    continue;
                }
                // they knew the id
                // ex. delete 3
                try {
                    int id = Integer.parseInt(inputArray[1]);
                    if (!app.exists("ID", Integer.toString(id), currTable)) System.out.println("This ID is not in the database");
                    else {
                        app.delete(Integer.parseInt(inputArray[1]), currTable);
                        System.out.println("DELETED");
                    }
                } catch (java.lang.NumberFormatException n) {
                    // we don't know specific id prompt user for it
                    // ex.delete milotic
                    if (!app.exists("name", inputArray[1], currTable)) System.out.println("This pokemon is not in the database");
                    else {
                        selectCommand.selectName(inputArray[1], false, currTable);
                        System.out.println("Please enter the ID of the pokemon you want to update");
                        System.out.print("DELETE>");
                        String usr_input = scan.nextLine();
                        if (usr_input.equals("cancel")) {
                            System.out.println("DELETE CANCELLED");
                        } else {
                            // if they pass in a string, program errors
                            // ID may not even be in the database
                            int ID = Integer.parseInt(usr_input);
                            app.delete(ID, currTable);
                            System.out.println("DELETED");
                        }
                    }
                }
            }
            else if (command.equals(Enums.COMMAND.UPDATE)) {
                // once again there may be multiple, need to have user specify which one by ID
                // ex. update [pokemon_name]
                // print which one? while listing all of them
                // specify the id
                // then do [column] = [new val]
                if (inputArray.length != 2) {
                    System.out.println("Invalid arguments");
                }
                if (inputArray.length == 2) {
                    try {
                        int id = Integer.parseInt(inputArray[1]);
                        if (!app.exists("ID", inputArray[1], currTable)) System.out.println("This ID is not in the database");
                        else {
                            selectCommand.selectPokemonByID(id, currTable);
                            int ID = Integer.parseInt(inputArray[1]);
                            System.out.println("Please enter which categories you wish to update");
                            System.out.print("UPDATE>");
                            String category_args = scan.nextLine();
                            String[] categories = category_args.split(" ");
                            if (categories[0].equals("cancel")) {
                                System.out.println("UPDATE CANCELLED");
                            } else {
                                // user specificed categories may not even be real categories
                                for (String category : categories) {
                                    System.out.println("Category: " + category);
                                    System.out.println("Enter new value: ");
                                    System.out.print("UPDATE>");
                                    String val = scan.nextLine();
                                    if (category.equals("item") || category.equals("name") || category.equals("move1") ||
                                            category.equals("move2") || category.equals("move3") || category.equals("move4")) {
                                        app.update(ID, category, val, currTable, false);
                                    } else {
                                        app.update(ID, category, val, currTable, true);
                                    }
                                }
                                System.out.println("UPDATED");
                            }
                        }
                    } catch (java.lang.NumberFormatException n) {
                        // we don't know specific id prompt user for it
                        // ex.update milotic
                        String pokemon_name = inputArray[1];
                        if (!app.exists("name", pokemon_name, currTable)) System.out.println("This pokemon is not in the database");
                        else {
                            selectCommand.selectName(pokemon_name, false, currTable);
                            System.out.println("Please enter the ID of the pokemon you want to update");
                            System.out.print("UPDATE>");
                            String usr_input = scan.nextLine();
                            if (usr_input.equals("cancel")) System.out.println("UPDATE CANCELLED");
                            else {
                                try {
                                    int ID = Integer.parseInt(usr_input);
                                    // check the ID corresponds to the same pokemon we want to update
                                    if (!app.exists("ID", Integer.toString(ID), currTable)) {
                                        System.out.println("This ID does not correspond to any pokemon in the database. Cancelling update.");
                                        continue;
                                    }
                                    String requested_pokemon = selectCommand.selectNameById(ID, currTable).toLowerCase();
                                    System.out.println("name: " + pokemon_name + " req: " + requested_pokemon);
                                    if (!requested_pokemon.equals(pokemon_name)) {
                                        System.out.println("The ID you entered does not match any IDs of the pokemon you want to update. Cancelling update");
                                        continue;
                                    }
                                    System.out.println("Please enter which categories you wish to update");
                                    System.out.print("UPDATE>");
                                    String category_args = scan.nextLine();
                                    String[] categories = category_args.split(" ");
                                    if (category_args.equals("cancel")) System.out.println("UPDATE CANCELLED");
                                    else {
                                        for (String category : categories) {
                                            System.out.println("Category: " + category);
                                            System.out.println("Enter new value: ");
                                            System.out.print("UPDATE>");
                                            String val = scan.nextLine();
                                            if (category.equals("item") || category.equals("name") || category.equals("move1") ||
                                                    category.equals("move2") || category.equals("move3") || category.equals("move4")) {
                                                app.update(ID, category, val, currTable, false);
                                            } else {
                                                app.update(ID, category, val, currTable, true);
                                            }
                                        }
                                        System.out.println("UPDATED");
                                    }

                                } catch (java.lang.NumberFormatException s) {
                                    System.out.println("Input was not an integer. Cancelling update");
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Invalid Argument");
                }
            }
            else if (command.equals(Enums.COMMAND.ALL)) {
                if (inputArray.length == 1) {
                    selectCommand.selectAll(currTable, false);
                } else if (inputArray.length == 2 && inputArray[1].equals("id")) {
                    selectCommand.selectAll(currTable, true);
                } else {
                    System.out.println("Invalid Command");
                }
            }
            else if (command.equals(Enums.COMMAND.DELETEALL)) {
                System.out.println("Are you sure? (y/n)");
                System.out.print("DELETEALL>");
                String answer = scan.nextLine();
                if (answer.equals("y")) {
                    app.deleteAll(currTable);
                    System.out.println("All entries in pokemon table deleted");
                } else if (answer.equals("n")) {
                    System.out.println("Cancelled");
                } else {
                    System.out.println("Invalid Response, returning to main");
                }
            }
            else if (command.equals(Enums.COMMAND.EXIT)) {
                return;
            }
        }
    }
}
