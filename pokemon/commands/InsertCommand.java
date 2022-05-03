package pokemon.commands;

import pokemon.DatabaseUtils;
import pokemon.Pokemon_DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class InsertCommand {
    DatabaseUtils databaseUtils = new DatabaseUtils();

    /**
     * Helper method for insertToTable method that contains the actual SQL statement to insert into table
     */
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

    public void insertToTable(String pokemon_name) {
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
            insert(pokemon_name, item, hp, attack, defense, spAttack, spDefense, speed, move1, move2, move3, move4);
            System.out.println("SAVED");
        } catch (java.lang.NumberFormatException ex) {
            System.out.println("Inputs for stats must be integers");
            System.out.println("Insert cancelled");
        }
    }
}
