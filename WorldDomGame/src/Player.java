import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {
    private Inventory inventory;
    private String name;
    private Objective currentObjective;
    private int attackPower;
    private ArrayList<Weapon> weapons;
    private Weapon selectedWeapon;
    private Map<ResourceType, Integer> resources;
    private Map<ResourceType, Integer> rareResources = new HashMap<>();

    public Player(String name, GamePanel gamePanel, Inventory inventory) {
        this.name = name;
        this.currentObjective = new Objective("Gain enough resources to build the Space Station.");
        this.attackPower = 0;
        this.resources = new HashMap<>();
        this.rareResources = new HashMap<>();
        this.weapons = new ArrayList<>();
        this.selectedWeapon = null;
        this.inventory = inventory;
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

    public void attackPlanet(Planet planet, int damage) {
        planet.damage(damage, this);
        System.out.println(name + " attacked " + planet.getName() + " for " + damage + " damage.");
    }

    public void gainResources(ResourceType resourceType, int amount) {
        ItemStack[][] inventoryGrid = inventory.getInventoryGrid();
    
        for (int row = 0; row < inventoryGrid.length; row++) {
            for (int col = 0; col < inventoryGrid[row].length; col++) {
                if (inventoryGrid[row][col] != null && inventoryGrid[row][col].getItem() instanceof Resource && ((Resource) inventoryGrid[row][col].getItem()).getResourceType() == resourceType) {
                    int spaceAvailable = 999 - inventoryGrid[row][col].quantity;
                    int amountToAdd = Math.min(amount, spaceAvailable);
                    inventoryGrid[row][col].quantity += amountToAdd;
                    amount -= amountToAdd;
                    if (amount == 0) {
                        inventory.repaint(); // Repaint when changes occur.
                        return; // All resources added
                    }
                }
            }
        }
    
        // If we get here, no existing stack or all stacks are full
        for (int row = 0; row < inventoryGrid.length; row++) {
            for (int col = 0; col < inventoryGrid[row].length; col++) {
                if (inventoryGrid[row][col] == null) {
                    Resource resource = new Resource(resourceType); // Create Resource object
                    inventoryGrid[row][col] = new ItemStack(resource, Math.min(amount, 999)); // Use resource in ItemStack constructor
                    amount -= Math.min(amount, 999);
                    if (amount == 0) {
                        inventory.repaint(); // Repaint when changes occur.
                        return;
                    }
                }
            }
        }
            inventory.updateInventory(this); //Call repaint inside updateInventory().
    }

    public Inventory getInventory() {
        return inventory;
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

    public void acquireWeapon(Weapon weapon) {
        weapons.add(weapon);
        inventory.updateInventory(this); //Update the inventory here
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
