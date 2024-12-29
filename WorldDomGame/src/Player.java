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
    private ArrayList<Resource> resources;
    private Map<ResourceType, Integer> rareResources = new HashMap<>();

    public Player(String name, GamePanel gamePanel, Inventory inventory) {
        this.name = name;
        this.currentObjective = new Objective("Gain enough resources to build the Space Station.");
        this.attackPower = 0;
        this.resources = new ArrayList<>();
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
        InventorySlot[][] inventorySlots = inventory.inventorySlots;

        // Attempt to add to existing stacks
        for (int row = 0; row < inventorySlots.length; row++) {
            for (int col = 0; col < inventorySlots[row].length; col++) {
                ItemStack currentItemStack = inventorySlots[row][col].getItemStack();
                if (currentItemStack != null && currentItemStack.getItem() instanceof Resource) {
                    Resource resource = (Resource) currentItemStack.getItem();
                    if (resource.getResourceType() == resourceType) {
                        int spaceAvailable = 999 - currentItemStack.getQuantity();
                        int amountToAdd = Math.min(amount, spaceAvailable);
                        currentItemStack.setQuantity(currentItemStack.getQuantity() + amountToAdd);
                        amount -= amountToAdd;

                        // Repaint the slot
                        inventorySlots[row][col].repaint();

                        if (amount == 0) {
                            return; // All resources added
                        }
                    }
                }
            }
        }

        // If we get here, no existing stack or all stacks are full, find an empty slot
        for (int row = 0; row < inventorySlots.length; row++) {
            for (int col = 0; col < inventorySlots[row].length; col++) {
                if (inventorySlots[row][col].getItemStack() == null) {
                    Resource resource = new Resource("Resource", "", resourceType); // Create new Resource object
                    ItemStack newItemStack = new ItemStack(resource, Math.min(amount, 999));
                    inventorySlots[row][col].setItemStack(newItemStack);
                    amount -= newItemStack.getQuantity();

                    // Repaint the slot
                    inventorySlots[row][col].repaint();

                    if (amount == 0) {
                        return; // All resources added
                    }
                }
            }
        }
    }
    
    public Inventory getInventory() {
        return inventory;
    }

    public void gainRareResources(ResourceType resourceType, int amount) {
        rareResources.put(resourceType, rareResources.getOrDefault(resourceType, 0) + amount);
        System.out.println(name + " gained " + amount + " rare " + resourceType.name() + ".");
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

    public ArrayList<Resource> getResources() {
        return resources;
    }

    public void acquireResource(Resource resource) {
        resources.add(resource);
        inventory.updateInventory(this); // Notify the inventory to update
    }
}
