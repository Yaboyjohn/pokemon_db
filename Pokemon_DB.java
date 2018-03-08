import java.sql.*;
import java.util.Scanner;

public class Pokemon_DB {
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

    public enum COMMAND {
        GET, HELP, INSERT, DELETE, UPDATE, EXIT, ALL, DELETEALL, AVG, MAX, MIN, SIZE, SQL, SORT;
    }

    public enum CATEGORY {
        ITEM, HP, ATTACK, DEFENSE, SPATTACK, SPDEFENSE, SPEED, MOVE;

        public String toString() {
            if (this.equals(CATEGORY.HP)) return "hp";
            else if (this.equals(CATEGORY.ATTACK)) return "attack";
            else if (this.equals(CATEGORY.DEFENSE)) return "defense";
            else if (this.equals(CATEGORY.SPATTACK)) return "spattack";
            else if (this.equals(CATEGORY.SPDEFENSE)) return "spdefense";
            else if (this.equals(CATEGORY.SPEED)) return "speed";
            else return null;
        }
    }

    private Connection connect() {
        // SQLite connection string
        String url = "jdbc:sqlite:pokemon.db";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }
    private static void createNewDatabase(String fileName) {

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

    public void createNewTable(String tableName) {
        // SQLite connection string
        String url = "jdbc:sqlite:pokemon.db";

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

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteTable(String tableName) {
        String url = "jdbc:sqlite:pokemon.db";
        String sql = "DROP TABLE IF EXISTS " + tableName;
        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public void selectAll(String tableName){
        String emptyCheck = "SELECT count(*) FROM " + tableName;
        //String sql = "SELECT id, name, hp, attack, defense, spattack, spdefense, speed, move1, move2, move3, move4 FROM " + tableName;
        String sql = "SELECT * FROM " + tableName + " ORDER BY name";

        try (Connection conn = this.connect();
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

    public void selectColumn(String colName, String tableName) {
        String sql = "SELECT name" + colName + " FROM " + tableName;
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
             while (rs.next()) {
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
    }

    public int selectID(int ID, String tableName) {
        String sql = "SELECT * FROM " + tableName + " WHERE id = " + ID + " ORDER BY ID";
        int count = 0;
        try (Connection conn = this.connect();
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


    public int selectName(String pokemon_name, String tableName) {
        String sql = "SELECT * FROM " + tableName + " WHERE name LIKE " + "'" + pokemon_name + "'";
        int count = 0;
        try (Connection conn = this.connect();
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
        try (Connection conn = this.connect();
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

    public int selectMove(String move, String tableName) {
        String sql = "SELECT * FROM " + tableName + " WHERE move1 LIKE " + "'" + move + "' OR " +
                "move2 LIKE " + "'" + move + "' OR " +  "move3 LIKE " + "'" + move + "' OR "
                +  "move4 LIKE " + "'" + move + "' ORDER BY name";
        int count = 0;
        try (Connection conn = this.connect();
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
        try (Connection conn = this.connect();
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

    public void insert(String name, String item, int hp, int attack, int defense,
                       int spattack, int spdefense, int speed, String move1, String move2,
                       String move3, String move4) {
        String sql = "INSERT INTO pokemon(name, item, hp, attack, defense, spattack, spdefense, speed, move1, move2, move3, move4) " +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = this.connect();
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
        try (Connection conn = this.connect();
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

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // set the corresponding param
            pstmt.setInt(1, id);
            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteall(String tableName) {
        String sql = "DELETE FROM " + tableName;

        try (Connection conn = this.connect();
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
        try (Connection conn = this.connect();
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
        try (Connection conn = this.connect();
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
                "ID", "Name", "Item", "HP", "Attack", "Defense", "Sp.Attack", "Sp. Defg", "Speed", "Move 1",
                "Move 2", "Move 3", "Move 4");
        try (Connection conn = this.connect();
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
        try (Connection conn = this.connect();
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
        try (Connection conn = this.connect();
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
                "ID", "Name", "Item", "HP", "Attack", "Defense", "Sp.Attack", "Sp. Defg", "Speed", "Move 1",
                "Move 2", "Move 3", "Move 4");
        try (Connection conn = this.connect();
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

    public static void insertToTable(Pokemon_DB db, String pokemon_name) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Item: ");
        String item = scan.nextLine();
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
    }

    public static COMMAND parseCommand(String input) {
        String[] inputArray = input.split(" ");
        String com = inputArray[0];
        if (com.equals("get")) return COMMAND.GET;
        if (com.equals("help")) return COMMAND.HELP;
        if (com.equals("insert")) return COMMAND.INSERT;
        if (com.equals("delete")) return COMMAND.DELETE;
        if (com.equals("update")) return COMMAND.UPDATE;
        if (com.equals("exit")) return COMMAND.EXIT;
        if (com.equals("all")) return COMMAND.ALL;
        if (com.equals("deleteall")) return COMMAND.DELETEALL;
        if (com.equals("avg")) return COMMAND.AVG;
        if (com.equals("max")) return COMMAND.MAX;
        if (com.equals("min")) return COMMAND.MIN;
        if (com.equals("size")) return COMMAND.SIZE;
        if (com.equals("sql")) return COMMAND.SQL;
        if (com.equals("sort")) return COMMAND.SORT;
        else return null;
    }

    public static CATEGORY parseCategory(String input) {
        String[] inputArray = input.split(" ");
        String com = inputArray[1];
        if (com.equals("item")) return CATEGORY.ITEM;
        if (com.equals("hp")) return CATEGORY.HP;
        if (com.equals("attack")) return CATEGORY.ATTACK;
        if (com.equals("defense")) return CATEGORY.DEFENSE;
        if (com.equals("spattack")) return CATEGORY.SPATTACK;
        if (com.equals("spdefense")) return CATEGORY.SPDEFENSE;
        if (com.equals("speed")) return CATEGORY.SPEED;
        if (com.equals("move")) return CATEGORY.MOVE;
        else return null;
    }

    public static String getArgument(String input) {
        String[] inputArray = input.split(" ");
        if (inputArray.length == 3) {
            return inputArray[2];
        } else if (inputArray.length == 4) {
            return inputArray[2] + " " + inputArray[3];
        }
        return null;
    }

    public static String[] parseStat(String input) {
        String[] res = new String[2];
        String[] inputArray = input.split(" ");
        res[0] = inputArray[2];
        res[1] = inputArray[3];
        return res;
    }

    public static void main(String[] args) {
        Pokemon_DB app = new Pokemon_DB();
        app.connect();
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("MAIN>");
            String input = scan.nextLine();
            String argument = getArgument(input);
            COMMAND command = parseCommand(input);
            String[] inputArray = input.split(" ");
            if (command == null) {
                System.out.println("Invalid Command");
            }
            else if (command.equals(COMMAND.SORT)) {
                if (inputArray.length != 2) {
                    System.out.println("Invalid Arguments");
                } else {
                    CATEGORY stat = parseCategory(input);
                    app.sort(stat.toString(), "pokemon");
                }
            }
            else if (command.equals(COMMAND.SIZE)) {
                app.size("pokemon");
            }
            else if (command.equals(COMMAND.AVG)) {
                app.avg("pokemon");
            }
            else if (command.equals(COMMAND.SQL)) {
                System.out.println("PLease enter your sql statement");
                System.out.print("SQL>");
                String sql = scan.nextLine();
                app.sql(sql);
            }
            else if (command.equals(COMMAND.MAX)) {
                if (inputArray.length !=2) {
                    System.out.println("Invalid Arguments");
                } else {
                    CATEGORY stat = parseCategory(input);
                    if (stat.equals(null)) System.out.println("Invalid Argument");
                    else {
                        app.max(stat.toString(), "pokemon");
                    }
                }
            }
            else if (command.equals(COMMAND.MIN)) {
                if (inputArray.length !=2) {
                    System.out.println("Invalid Arguments");
                } else {
                    CATEGORY stat = parseCategory(input);
                    if (stat.equals(null)) System.out.println("Invalid Argument");
                    else {
                        app.min(stat.toString(), "pokemon");
                    }
                }
            }
            else if (command.equals(COMMAND.GET)) {
                if (inputArray.length < 2 || inputArray.length >= 5) {
                    System.out.println("Invalid Arguments");
                }
                // This is getting a single pokemon ex. get suicune
                else if (input.equals("get mr. mime")) {
                    int count = app.selectName("Mr. Mime", "pokemon");
                    if (count <= 0) {
                        System.out.println("This pokemon is not in the database");
                    }
                }
                else if (inputArray.length == 2) {
                    int count = app.selectName(inputArray[1], "pokemon");
                    if (count <= 0) {
                        System.out.println("This pokemon is not in the database");
                    }  
                }
                // This can be anything other than name ex. get item lum berry
                else {
                    CATEGORY category = parseCategory(input);
                    if (category == null) {
                        System.out.println("Invalid arguments");
                    }
                    else if (category.equals(CATEGORY.ITEM)) {
                        int count = app.selectItem(argument, "pokemon");
                        if (count == 0) {
                            System.out.println("This item is not in the database");
                        }
                    }
                    else if (category.equals(CATEGORY.MOVE)) {
                        int count = app.selectMove(argument, "pokemon");
                        if (count == 0) {
                            System.out.println("No pokemon satisfied the given parameters");
                        }
                    }
                    // get hp > 300, get hp = 300, get hp >= 300
                    else if (category.equals(CATEGORY.HP) || category.equals(CATEGORY.ATTACK)|| category.equals(CATEGORY.DEFENSE) ||
                            category.equals(CATEGORY.SPATTACK) || category.equals(CATEGORY.SPDEFENSE) || category.equals(CATEGORY.SPEED)) {
                        String stat = inputArray[1];
                        String operator = parseStat(input)[0];
                        int num = Integer.parseInt(parseStat(input)[1]);
                        int count = app.selectStat(stat, operator, num, "pokemon");
                        if (count == 0) {
                            System.out.println("This item is not in the database");
                        }
                    } else {
                        System.out.println("Invalid Arguments");
                    }
                }
            }
            else if (command.equals(COMMAND.HELP)) {
                System.out.println("INSERT HELP GUIDE HERE");
            }
            else if (command.equals(COMMAND.INSERT)) {
                insertToTable(app, inputArray[1]);
            }
            else if (command.equals(COMMAND.DELETE)) {
                // valid arguments check
                if (inputArray.length != 2) {
                    System.out.println("Invalid arguments");
                } else {
                    // they knew the id
                    // ex. delete 3
                    try {
                        int count = app.selectID(Integer.parseInt(inputArray[1]), "pokemon");
                        if (count == 0) {
                            System.out.println("This ID is not in the database");
                        } else {
                            app.delete(Integer.parseInt(inputArray[1]), "pokemon");
                            System.out.println("DELETED");
                        }
                    } catch (java.lang.NumberFormatException n) {
                        // we don't know specific id prompt user for it
                        // ex.delete milotic
                        // POKEMON MAY NOT EVEN EXIST
                        int count = app.selectName(inputArray[1], "pokemon");
                        if (count == 0) {
                            System.out.println("This pokemon is not in the database");
                        } else {
                            System.out.println("Please enter the ID of the pokemon you want to update");
                            System.out.print("DELETE>");
                            String usr_input = scan.nextLine();
                            if (usr_input.equals("cancel")) {
                                System.out.println("DELETE CANCELLED");
                            } else {
                                // if they pass in a string, program errors
                                // ID may not even be in the database
                                int ID = Integer.parseInt(usr_input);
                                app.delete(ID, "pokemon");
                                System.out.println("DELETED");
                            }
                        }
                    }
                }
            }
            else if (command.equals(COMMAND.UPDATE)) {
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
                        int count = app.selectID(Integer.parseInt(inputArray[1]), "pokemon");
                        if (count == 0) {
                            System.out.println("This ID is not in the database");
                        } else {
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
                                        app.update(ID, category, val, "pokemon", false);
                                    } else {
                                        app.update(ID, category, val, "pokemon", true);
                                    }
                                }
                                System.out.println("UPDATED");
                            }
                        }
                    } catch (java.lang.NumberFormatException n) {
                        // we don't know specific id prompt user for it
                        // ex.update milotic
                        // POKEMON MAY NOT EVEN EXIST
                        int count = app.selectName(inputArray[1], "pokemon");
                        if (count == 0) {
                            System.out.println("This pokemon is not in the database");
                        } else {
                            System.out.println("Please enter the ID of the pokemon you want to update");
                            System.out.print("UPDATE>");
                            String usr_input = scan.nextLine();
                            if (usr_input.equals("cancel")) System.out.println("UPDATE CANCELLED");
                            else {
                                int ID = Integer.parseInt(usr_input);
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
                                            app.update(ID, category, val, "pokemon", false);
                                        } else {
                                            app.update(ID, category, val, "pokemon", true);
                                        }
                                    }
                                    System.out.println("UPDATED");
                                }
                            }
                        }
                    }
                } else {
                    System.out.println("Invalid Argument");
                }
            }
            else if (command.equals(COMMAND.ALL)) {
                app.selectAll("pokemon");
            }
            else if (command.equals(COMMAND.DELETEALL)) {
                System.out.println("Are you sure? (y/n)");
                System.out.print("DELETEALL>");
                String answer = scan.nextLine();
                if (answer.equals("y")) {
                    app.deleteall("pokemon");
                    System.out.println("All entries in pokemon table deleted");
                } else if (answer.equals("n")) {
                    System.out.println("Cancelled");
                } else {
                    System.out.println("Invalid Response, returning to main");
                }
            }
            else if (command.equals(COMMAND.EXIT)) {
                return;
            }
        }
    }
}
