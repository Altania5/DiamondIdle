import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
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
    private List<ResourceType> requiredResourcesToDrop;
    private boolean resourcesDropped = false;
    public int x, y;
    private int radius;
    private Ellipse2D hitbox;

    public Planet(int x, int y, int radius, String name, String imagePath, List<ResourceType> requiredResourcesToDrop) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.name = name;
        loadImage(imagePath);
        this.health = 1000;
        this.requiredResourcesToDrop = requiredResourcesToDrop;
        generateHitbox();
        
    }

    // Update existing constructor to include x, y, and radius
    public Planet(int x, int y, int radius, String name) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.name = name;
        this.image = loadRandomImage();
        this.health = 1000;
        this.requiredResourcesToDrop = generateRequiredResources();
        generateHitbox();
    }

    public Ellipse2D getBounds() {
        return new Ellipse2D.Float(x - radius, y - radius, 2 * radius, 2 * radius);
    }

    private void loadImage(String imagePath) {
        try {
            this.image = ImageIO.read(new File(imagePath)); // Use imagePath to load the image
        } catch (IOException e) {
            System.err.println("Error loading image for planet: " + name);
            e.printStackTrace();
        }
    }

    public void generateHitbox() {
        if (image != null) {
            int diameter = Math.max(image.getWidth(null), image.getHeight(null));
            int centerX = x + image.getWidth(null) / 2 - diameter / 2;
            int centerY = y + image.getHeight(null) / 2 - diameter / 2;
            hitbox = new Ellipse2D.Float(centerX, centerY, diameter, diameter);
        } else {
            System.err.println("Image not loaded for planet: " + name);
            hitbox = new Ellipse2D.Float(x - radius, y - radius, 2 * radius, 2 * radius);
        }
    }
    

    public Ellipse2D getHitbox() {
        return hitbox;
    }

    private Image loadRandomImage() {
        try {
            File folder = new File("res/planets");
            File[] listOfFiles = folder.listFiles();
            Random random = new Random();
            if (listOfFiles != null && listOfFiles.length > 0) {
                File file = listOfFiles[random.nextInt(listOfFiles.length)];
                return ImageIO.read(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void showHitboxes(Graphics2D g2d) {
        if (image != null) {
            // Get the hitbox
            Ellipse2D hitbox = getHitbox();
    
            // Draw the hitbox
            g2d.setColor(Color.RED);
            g2d.draw(hitbox);
        } else {
            // Handle case where the image is not loaded
            System.err.println("Image not loaded for planet: " + name);
        }
    }

    private List<ResourceType> generateRequiredResources() {
        List<ResourceType> resources = new ArrayList<>();
        Random random = new Random();
        int numResources = 1 + random.nextInt(3);

        for (int i = 0; i < numResources; i++) {
            resources.add(ResourceType.values()[random.nextInt(ResourceType.values().length)]);
        }

        return resources;
    }

    public boolean isDestroyed() {
        return health <= 0;
    }

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

        dropResources(player);
    }

    private void dropResources(Player player) {
        Random random = new Random();
        if (random.nextInt(100) < 75) {
            resourcesDropped = true;
            int numResources = 1 + random.nextInt(3);
            for (int i = 0; i < numResources; i++) {
                ResourceType resourceType = getRandomResourceType(random);
                System.out.println(name + " dropped " + resourceType.name() + ".");
                player.gainResources(resourceType, 1);
            }
        }
    }

    public static ResourceType getRandomResourceType(Random random) {
        int totalRarity = 0;
        for (ResourceType type : ResourceType.values()) {
            totalRarity += type.getRarity();
        }

        int randomValue = random.nextInt(totalRarity) + 1;
        int currentRarity = 0;
        for (ResourceType type : ResourceType.values()) {
            currentRarity += type.getRarity();
            if (randomValue <= currentRarity) {
                return type;
            }
        }

        return ResourceType.STONE; // Default return value (should ideally never reach here)
    }
}
