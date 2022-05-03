package pokemon;

/**
 * Class that holds all the functions that parse the text passed into text prompts
 */
public class ParseUtils {
    /**
     *
     * @param inputArray eg. ["get", "item", "king's", "rock"]
     * @return first word in input as ENUM
     */
    public static Enums.COMMAND parseCommand(String[] inputArray) {
        String command = inputArray[0].toUpperCase();
        try {
            return Enums.COMMAND.valueOf(command);
        } catch (java.lang.IllegalArgumentException ex) {
            return null;
        }
    }

    /**
     *
     * @param inputArray eg. ["get", "item", "king's", "rock"]
     * @return second word in input as ENUM (eg. ITEM)
     */
    public static Enums.CATEGORY parseCategory(String[] inputArray) {
        String category = inputArray[1];
        if (category.equals("item")) return Enums.CATEGORY.ITEM;
        if (category.equals("hp")) return Enums.CATEGORY.HP;
        if (category.equals("attack")) return Enums.CATEGORY.ATTACK;
        if (category.equals("defense")) return Enums.CATEGORY.DEFENSE;
        if (category.equals("spattack")) return Enums.CATEGORY.SPATTACK;
        if (category.equals("spdefense")) return Enums.CATEGORY.SPDEFENSE;
        if (category.equals("speed")) return Enums.CATEGORY.SPEED;
        if (category.equals("move")) return Enums.CATEGORY.MOVE;
        if (category.equals("id")) return Enums.CATEGORY.ID;
        else return null;
    }

    /**
     *
     * @param inputArray eg. ["get", "item", "king's", "rock"]
     * @return rest of input array after second word as one string (eg. "king's rock")
     */
    public static String getArgument(String[] inputArray) {
        if (inputArray.length == 3) {
            return inputArray[2];
        } else if (inputArray.length == 4) {
            return inputArray[2] + " " + inputArray[3];
        } else if (inputArray.length == 5) {
            return inputArray[2] + " " + inputArray[3] + " " + inputArray[4];
        }
        return null;
    }
}
