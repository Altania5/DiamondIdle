public class Weapon extends Item {

    private int attackPower;

    public Weapon(String name, int attackPower, String imagePath) {
        super(name, imagePath);
        this.attackPower = attackPower;
    }

    public int getAttackPower() {
        return attackPower;
    }
}
