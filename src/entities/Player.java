package entities;

public class Player extends Entity {
    
    public Player (int hp) {

        super(hp);
    }

    @Override
    public void dealDamage (Entity entity, int dmg) {
        entity.receiveDamage(dmg);
    }
}
