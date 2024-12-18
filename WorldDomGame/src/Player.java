import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private String name;
    private Objective currentObjective;
    private int attackPower;
    private ArrayList<Weapon> weapons;
    private Weapon selectedWeapon; // Add a selectedWeapon field
    private Map<ResourceType, Integer> resources;
    private Map<ResourceType, Integer> rareResources = new HashMap<>();

    public Player(String name, GamePanel gamePanel) {
        this.name = name;
        this.currentObjective = new Objective("Gain enough resources to build the Space Station.");
        this.attackPower = 0; // Base attack power
        this.resources = new HashMap<>();
        this.rareResources = new HashMap<>();
        this.weapons = new ArrayList<>();
        this.selectedWeapon = null; // Initialize selectedWeapon to null
    }

    public Objective getCurrentObjective() {
        return currentObjective;
    }

    public void setCurrentObjective(Objective currentObjective) {
        this.currentObjective = currentObjective;
    }

    public String getName() {
        return name;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public Map<ResourceType, Integer> getResources() {
        return resources;
    }

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public Weapon getSelectedWeapon() {
        return selectedWeapon;
    }

    public void setSelectedWeapon(Weapon weapon) {
        this.selectedWeapon = weapon;
    }

    // Methods for interacting with planets
    public void attackPlanet(Planet planet, int damage) {
        planet.damage(damage, this);
        System.out.println(name + " attacked " + planet.getName() + " for " + damage + " damage.");
    }


    // Methods for managing resources
    public void gainResources(ResourceType resourceType, int amount) {
        resources.put(resourceType, resources.getOrDefault(resourceType, 0) + amount);
        System.out.println(name + " gained " + amount + " " + resourceType.name() + ".");
    }

    public void gainRareResources(ResourceType resourceType, int amount) {
        rareResources.put(resourceType, rareResources.getOrDefault(resourceType, 0) + amount);
        System.out.println(name + " gained " + amount + " rare " + resourceType.name() + ".");
    }

    public void spendResources(ResourceType resourceType, int amount) {
        if (resources.getOrDefault(resourceType, 0) >= amount) {
            resources.put(resourceType, resources.get(resourceType) - amount);
            System.out.println(name + " spent " + amount + " " + resourceType.name() + ".");
        } else {
            System.out.println(name + " does not have enough " + resourceType.name() + ".");
        }
    }

    public void spendRareResources(ResourceType resourceType, int amount) {
        if (rareResources.getOrDefault(resourceType, 0) >= amount) {
            rareResources.put(resourceType, rareResources.get(resourceType) - amount);
            System.out.println(name + " spent " + amount + " " + resourceType.name() + ".");
        } else {
            System.out.println(name + " does not have enough " + resourceType.name() + ".");
        }
    }

    // Methods for managing weapons
    public void acquireWeapon(Weapon weapon) {
        weapons.add(weapon);
        System.out.println(name + " acquired a " + weapon.getName() + ".");
    }

    public void discardWeapon(Weapon weapon) {
        if (weapons.contains(weapon)) {
            weapons.remove(weapon);
            System.out.println(name + " discarded a " + weapon.getName() + ".");
        } else {
            System.out.println(name + " does not have that weapon.");
        }
    }
}
