import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class InventorySlot extends JPanel {
    private ItemStack itemStack;
    private int x, y;
    private int cellSide;
    private int padding = 5; // Padding within the slot
    private Inventory inventory; // Reference to the parent Inventory
    private boolean showHitbox = false;

    public InventorySlot(int x, int y, int cellSide, Inventory inventory) {
        this.x = x;
        this.y = y;
        this.cellSide = cellSide;
        this.inventory = inventory;
        setOpaque(false); // Make the slot transparent

        // Add a mouse listener to detect clicks
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePress(e);
            }
        });
    }

    public void setShowHitbox(boolean showHitbox) {
        this.showHitbox = showHitbox;
        repaint();
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        repaint();
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void handleMousePress(MouseEvent e) {
        if (itemStack != null) {
            Item item = itemStack.getItem();
            if (item != null) {
                // Check if the item is a Resource
                if (item instanceof Resource) {
                    System.out.println("Clicked on: " + ((Resource) item).getResourceType().name());
                } else {
                    System.out.println("Clicked on: " + item.getName());
                }
                inventory.setDraggedItemStack(itemStack, e.getX(), e.getY()); // Notify Inventory of drag
            }
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the slot
        g2d.setColor(new Color(0x404040)); // Example slot color
        g2d.fillRect(0, 0, cellSide, cellSide);

        if (itemStack != null) {
            Item item = itemStack.getItem();
            if (item != null) {
                // Draw item image or colored circle
                BufferedImage img = item.getImage();
                if (img != null) {
                    int imageX = padding;
                    int imageY = padding;
                    int imageWidth = cellSide - 2 * padding;
                    int imageHeight = cellSide - 2 * padding;
                    g2d.drawImage(img, imageX, imageY, imageWidth, imageHeight, null);
                } else {
                    g2d.setColor(getItemColor(item));
                    Ellipse2D.Double circle = new Ellipse2D.Double(padding, padding, cellSide - 2 * padding, cellSide - 2 * padding);
                    g2d.fill(circle);
                }

                // Draw quantity text
                g2d.setColor(Color.WHITE);
                String quantityText = String.valueOf(itemStack.getQuantity());
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(quantityText);
                int textX = cellSide - textWidth - padding;
                int textY = cellSide - fm.getDescent();
                g2d.drawString(quantityText, textX, textY);
            }
        }
        if (showHitbox) {
            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(new Rectangle2D.Double(0, 0, cellSide, cellSide));
        }
    }

    // Helper method to get item color (you can customize this)
    private Color getItemColor(Item item) {
        if (item instanceof Resource) {
            ResourceType resourceType = ((Resource) item).getResourceType();
            switch (resourceType) {
                case ORE:
                    return Color.GRAY;
                case GAS:
                    return Color.CYAN;
                case CRYSTAL:
                    return Color.MAGENTA;
                case ENERGY:
                    return Color.YELLOW;
                case GEM:
                    return Color.GREEN;
                case STONE:
                default:
                    return Color.LIGHT_GRAY;
            }
        } else if (item instanceof Weapon) {
            return Color.RED;
        }
        return Color.BLACK;
    }
}