public class Weapon extends Item {
    private int attackPower;

    public Weapon(String name, String imagePath) {
        super(name, imagePath);
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public int getAttackPower() {
        return attackPower;
    }
}