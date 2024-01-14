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

    public void setHP(int value) {
        HP = value;
    }
    
    public void setAliveStatus (boolean b) {
        this.isAlive = b;
    }

    public boolean isAlive () {
        return HP > 0;
    }

    public void receiveDamage (int dmg) {
        this.HP -= dmg;
    }

    public void heal(int amount) {
        this.HP += amount;
    }

    // abstract methods
    public abstract void dealDamage (Entity entity, int dmg);
}
