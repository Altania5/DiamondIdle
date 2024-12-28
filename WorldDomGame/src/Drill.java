

public class Drill extends Weapon{

    public Drill() {
        super("Drill", 1, "WorldDomGame/res/weapons/drill.png"); // Adjust image path as needed
    }

    // Drill-specific methods (if any)
    // For example, a special attack:

    public void specialAttack(Planet planet, Player player) { // Planet and Player are passed in
        if (planet != null && player != null) { // Check for null to prevent NullPointerException
            int damage = getAttackPower() * 2;
            planet.damage(damage, player);
            System.out.println(player.getName() + " used Drill's special attack on " + planet.getName() + " for " + damage + " damage!");
        } else {
            System.err.println("Error: Planet or Player is null in Drill.specialAttack()");
        }
    }
}
