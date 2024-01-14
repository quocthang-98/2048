package constants;

public class AnimationConstants {
    
    public static class MoveConstants {
        public static final int IDLE = 0;
        public static final int ATTACK = 1;
        public static final int BEING_HIT = 2;
        public static final int USING_ABILITY = 3;
        public static final int DYING = 4;
        public static final int DEAD = 5;

        public static int getSpriteAmount (int movementCode) {
            switch (movementCode) {
                case IDLE:
                case DEAD:
                    return 6;
                case USING_ABILITY:
                    return 5;
                case DYING:
                    return 4;
                case BEING_HIT:
                case ATTACK:
                    return 3;
                default:
                    return 0;
            }
        }

        public static boolean isLoop (int movementCode) {
            switch (movementCode) {
                case IDLE:
                case DEAD:
                    return true;
                case BEING_HIT:
                case USING_ABILITY:
                case ATTACK:
                case DYING:
                    return false;
                default:
                    return false;
            }
        }
    }
}
