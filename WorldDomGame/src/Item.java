
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Item {
    private String name;
    private String imagePath;
    private BufferedImage image;



    public Item(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
        loadImage(); // Load image upon Item creation
    }


    private void loadImage() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(imagePath)) {
            if (is != null) {
                this.image = ImageIO.read(is);
            } else {
                System.err.println("Image not found: " + imagePath);
            }
        } catch (IOException e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
    }



    // Getters for name and image
    public String getName() {
        return name;
    }


    public BufferedImage getImage() {
        return image;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;  // If same reference, they are equal
        if (obj == null || getClass() != obj.getClass()) return false; // Null check and class check
        Item other = (Item) obj;  // Cast to Item
        return this.name.equals(other.name); //Items are equal if names are equal
    }

    @Override
    public int hashCode() {
        return name.hashCode(); // Must override hashCode for consistency with equals
    }
}