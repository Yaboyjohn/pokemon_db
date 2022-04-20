package pokemon;

public class Enums {
    public enum COMMAND {
        GET, HELP, INSERT, DELETE, UPDATE, EXIT, ALL, DELETEALL, AVG, MAX, MIN, SIZE, SQL, SORT, COMPARE, COUNT
    }

    public enum CATEGORY {
        ITEM, HP, ATTACK, DEFENSE, SPATTACK, SPDEFENSE, SPEED, MOVE, ID;

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
}
