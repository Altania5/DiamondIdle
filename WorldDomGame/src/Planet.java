import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Planet {
    private String name;
    private boolean isSelected;
    private Image image;
    private int health;
    private List<ResourceType> requiredResourcesToDrop; // List of resources to drop before 50% health
    private boolean resourcesDropped = false; // Flag to track if resources have been dropped

    public Planet(String name, String imagePath, List<ResourceType> requiredResourcesToDrop) {
        this.name = name;
        this.image = loadImage(imagePath);
        this.health = 1000;
        this.requiredResourcesToDrop = requiredResourcesToDrop;
    }

    public Planet(String name) {
        this.name = name;
        this.image = loadRandomImage();
        this.health = 1000;
        this.requiredResourcesToDrop = generateRequiredResources();
    }

    private Image loadImage(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Image loadRandomImage() {
        try {
            File folder = new File("res/planets");
            File[] listOfFiles = folder.listFiles();
            Random random = new Random();
            if (listOfFiles != null && listOfFiles.length > 0) {
                // Randomly select a file
                File file = listOfFiles[random.nextInt(listOfFiles.length)];
                return ImageIO.read(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<ResourceType> generateRequiredResources() {
        List<ResourceType> resources = new ArrayList<>();
        Random random = new Random();
        int numResources = 1 + random.nextInt(3); // Random number of resources between 1 and 3

        for (int i = 0; i < numResources; i++) {
            resources.add(ResourceType.values()[random.nextInt(ResourceType.values().length)]);
        }

        return resources;
    }

    // Getters and setters for the fields

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public Image getImage() {
        return image;
    }

    public int getHealth() {
        return health;
    }

    public void damage(int x, Player player){
        health -= x;

        // Drop resources based on health
        dropResources(player);
    }

    private void dropResources(Player player) {
        Random random = new Random();
        if (random.nextInt(100) < 75) { // 50% chance to drop resources
            resourcesDropped = true; // Set flag to prevent further drops
            int numResources = 1 + random.nextInt(3); // Drop between 1 and 3 resources
            for (int i = 0; i < numResources; i++) {
                ResourceType resourceType = getRandomResourceType(random);
                System.out.println(name + " dropped " + resourceType.name() + ".");
                player.gainResources(resourceType, 1); // Add 1 of the resource type to the player's inventory
            }
        }
    }

    private ResourceType getRandomResourceType(Random random) {
        // Calculate drop probabilities based on rarity
        int totalRarity = 0;
        for (ResourceType type : ResourceType.values()) {
            totalRarity += type.getRarity();
        }
    
        int randomValue = random.nextInt(totalRarity) + 1; // Generate a random value within the total rarity range
    
        int currentRarity = 0;
        for (ResourceType type : ResourceType.values()) {
            currentRarity += type.getRarity();
            if (randomValue <= currentRarity) {
                return type;
            }
        }
        // If no resource is selected, return a default resource (e.g., ORE)
        return ResourceType.STONE;
    }
    // Other methods as needed
}
