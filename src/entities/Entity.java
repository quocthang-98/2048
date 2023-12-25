package entities;

public abstract class Entity {
    
    protected int HP;
    protected boolean isAlive;

    public Entity (int hp) {
        this.HP = hp;
        isAlive = true;
    }

    public int getHP () {
        return this.HP;
    }
    
    public void setAliveStatus (boolean b) {
        this.isAlive = b;
    }

    public boolean getAliveStatus () {
        return isAlive;
    }

    public void receiveDamage (int dmg) {
        this.HP -= dmg;
    }

    // abstract methods
    public abstract void dealDamage (Entity entity, int dmg);
}
