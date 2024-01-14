package entities;

import java.util.Random;

public class Enemy extends Entity {

    private final int MAX_DIFFICULTY = 20;
    private int difficulty;
    private Random rand;

    private boolean aggresiveMode;
    
    public Enemy (int hp, int difficulty) {

        super(hp);
        this.difficulty = difficulty;
        rand = new Random(System.currentTimeMillis());

        aggresiveMode = false;
    }
    
    public void setAggressive(boolean b) {
        aggresiveMode = b;
    }

    public boolean getAggressive() {
        return aggresiveMode;
    }

    @Override
    public void dealDamage (Entity entity, int dmg) {
        entity.receiveDamage(dmg);
    }

    public boolean tryToAttack() {
        
        int value = rand.nextInt(MAX_DIFFICULTY);
        if (value < difficulty) {
            return true;
        }

        return false;
    }
}
