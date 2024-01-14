package constants;

public class ExpConstants {
    
    public static class Constants {
        public static int getRequiredEXP (int level) {
            if (level == 0) return 10;
            return level * 10 + 25;
        }

        public static int getRequiredMaxCombination (int level) {
            if (level < 5)
                return 50;
            else if (level < 10)
                return 45;
            else if (level < 20)
                return 40;
            else if (level < 25)
                return 30;
            else return 25;
        }
    }
}
