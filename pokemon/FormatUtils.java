package pokemon;

/**
 * This class contains all the formatting helper functions
 */
public class FormatUtils {
    //USAGE: System.out.println(ANSI_RED + "This text is red!" + ANSI_RESET);
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    public static final String BLACK_BOLD = "\033[1;30m";


    /**
     * Helper function used in formatString function
     * @param str
     * @param paddingAmount
     * @return
     */
    public static String rightPadding(String str, int paddingAmount) {
        return String.format("%1$-" + paddingAmount + "s", str);
    }

    /**
     * Used in COMPARE commands to highlight better/worse/equal stats with green/red/blue coloring respectively
     * @param statValue stat value that is currently being compared
     * @param avgStatValue the average stat value for the desired stat
     * @return
     */
    public String setStatComparisonColors(int statValue, double avgStatValue) {
        String res;
        if (statValue > avgStatValue) res = ANSI_GREEN + statValue + ANSI_RESET + rightPadding("", 10 - Integer.toString(statValue).length());
        else if (statValue < avgStatValue) res = ANSI_RED + statValue + ANSI_RESET + rightPadding("", 10 - Integer.toString(statValue).length());
        else {
            res = ANSI_BLUE + statValue + ANSI_RESET + rightPadding("", 10 - Integer.toString(statValue).length());
        }
        return res;
    }

    /**
     * Used in COMPARE [pokemon ID] command to bold the pokemon data of the pokemon whose ID we used
     * @param category
     * @param value
     * @return
     */
    public String setBoldForBaselinePokemon(String category, String value) {
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
}
