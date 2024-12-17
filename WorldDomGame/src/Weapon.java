public class Weapon {
    private String name;
    private int attackPower;

    public Weapon(String name, int attackPower) {
        this.name = name;
        this.attackPower = attackPower;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getAttackPower() {
        return attackPower;
    }
}
