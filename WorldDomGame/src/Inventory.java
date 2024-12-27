import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;

public class Inventory extends JPanel {

    private Player player;
    private int x, y;
    private int width = 300;
    private int height;
    private int arcRadius = 10;
    private boolean visible = false;
    private int rows = 3;
    private int columns = 13;
    private int padding = 10; 
    public ItemStack[][] inventoryGrid = new ItemStack[rows][columns]; // Example: 9x4 grid


    public Inventory() {
        setOpaque(false);
    }

    public void updateInventory(Player player) {
        this.player = player; // Ensure the inventory has the latest player data
        repaint(); // Trigger a repaint to reflect the updated inventory
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public ItemStack[][] getInventoryGrid() {
        return inventoryGrid;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!visible) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Get parent dimensions
        int parentWidth = getParent().getWidth();
        int parentHeight = getParent().getHeight();

        // Inventory background panel
        int panelWidth = parentWidth;
        int panelHeight = parentHeight / 3;
        int x = 0;
        int y = parentHeight - panelHeight;

        g2d.setColor(new Color(0x202020));
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(x, y, panelWidth, panelHeight, arcRadius, arcRadius);
        g2d.fill(roundedRectangle);
        g2d.setColor(Color.WHITE);
        g2d.draw(roundedRectangle);

        // Calculate cell dimensions and padding
        int totalPaddingWidth = (columns + 1) * padding;
        int totalPaddingHeight = (rows + 1) * padding;

        int availableWidth = panelWidth - totalPaddingWidth;
        int availableHeight = panelHeight - totalPaddingHeight;

        // Determine the size of the square cell
        int cellSide = Math.min(availableWidth / columns, availableHeight / rows);

        // Calculate cell positions
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                // Calculate cell position with padding
                int cellX = x + padding + col * (cellSide + padding);
                int cellY = y + padding + row * (cellSide + padding);

                // Draw the square cell
                g2d.setColor(new Color(0x404040));
                g2d.fillRect(cellX, cellY, cellSide, cellSide);

                ItemStack item = inventoryGrid[row][col];
                if (item != null) {
                    // Draw the item circle
                    int circleDiameter = Math.max(0, cellSide - 2 * padding);
                    int circleX = cellX + (cellSide - circleDiameter) / 2;
                    int circleY = cellY + (cellSide - circleDiameter) / 2;

                    g2d.setColor(getItemColor(item.type));
                    g2d.fillOval(circleX, circleY, circleDiameter, circleDiameter);

                    // Center the text
                    g2d.setColor(Color.WHITE);
                    String quantityText = String.valueOf(item.quantity);
                    FontMetrics fm = g2d.getFontMetrics();
                    int textWidth = fm.stringWidth(quantityText);
                    int textHeight = fm.getHeight();
                    int textX = cellX + (cellSide - textWidth) / 2;
                    int textY = cellY + cellSide - (textHeight / 2) - fm.getDescent();

                    g2d.drawString(quantityText, textX, textY);
                }
            }
        }
    }

    //Helper to give color to the circle.
    private Color getItemColor(ResourceType type) {
        // Assign colors based on resource type as needed
        switch (type) {
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
    }
}