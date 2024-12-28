import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

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
    private ItemStack selectedWeaponItemStack;


    public Inventory() {
        setOpaque(false);
        setFocusable(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isVisible()) return; 
                int clickedRow = -1;
                int clickedCol = -1;
                int cellSide = getCellSide(); 
                if (cellSide > 0) {  
                    for (int row = 0; row < rows; row++) {
                        for (int col = 0; col < columns; col++) {
                            int cellX = padding + col * (cellSide + padding);
                            int cellY = padding + row * (cellSide + padding);
                            if (e.getX() >= cellX && e.getX() < cellX + cellSide && 
                                e.getY() >= cellY && e.getY() < cellY + cellSide) {
    
                                clickedRow = row;
                                clickedCol = col;
                                break; 
                            }
                        }
                        if (clickedRow != -1) break; 
                    }
                } else {
                    System.err.println("Invalid cell dimensions");
                }
    
                if (clickedRow != -1 && clickedCol != -1) {
                    ItemStack clickedItemStack = inventoryGrid[clickedRow][clickedCol];
                    if (clickedItemStack != null && clickedItemStack.getItem() instanceof Weapon) {
    
                        // Deselect if the same weapon is clicked again
                        if (selectedWeaponItemStack != null && selectedWeaponItemStack == clickedItemStack) { 
                            selectedWeaponItemStack = null; 
                            player.setSelectedWeapon(null);
                        } else {
                            selectedWeaponItemStack = clickedItemStack;
                            Weapon selectedWeapon = (Weapon) clickedItemStack.getItem();
                            player.setSelectedWeapon(selectedWeapon);
                        }
                        repaint(); 
                    }
                }
            }
        });    
    }
    private int getCellSide() {
        int totalPaddingWidth = (columns + 1) * padding;
        int totalPaddingHeight = (rows + 1) * padding;
        int availableWidth = getWidth() - totalPaddingWidth;
        int availableHeight = getHeight() - totalPaddingHeight;
        int cellSide = Math.min(availableWidth / columns, availableHeight / rows);

        return cellSide;
    }

    public void updateInventory(Player player) {
       // Iterate through player's updated items/weapons and update the grid accordingly.
        inventoryGrid = new ItemStack[rows][columns]; // Clear existing grid.
        int currentRow = 0;
        int currentCol = 0;

        //Iterate through each weapon in players inventory.
        for (Weapon weapon : player.getWeapons()) {
            inventoryGrid[currentRow][currentCol] = new ItemStack(weapon, 1); // Assuming one weapon per slot for now.

            currentCol++;
            if (currentCol == columns) {
                currentCol = 0;
                currentRow++;
                if(currentRow == rows) {
                    break; //Inventory is full. Stop adding more items.
                }
            }
        }

       repaint();
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
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // Get parent dimensions
        int parentWidth = getParent().getWidth();
        int parentHeight = getParent().getHeight(); // Inventory background panel
        int panelWidth = parentWidth;
        int panelHeight = parentHeight / 3;
        int x = 0;
        int y = parentHeight - panelHeight;
        g2d.setColor(new Color(0x202020));
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(x, y, panelWidth, panelHeight, arcRadius, arcRadius);
        g2d.fill(roundedRectangle);
        g2d.setColor(Color.WHITE);
        g2d.draw(roundedRectangle); // Calculate cell dimensions and padding
        int totalPaddingWidth = (columns + 1) * padding;
        int totalPaddingHeight = (rows + 1) * padding;
        int availableWidth = panelWidth - totalPaddingWidth;
        int availableHeight = panelHeight - totalPaddingHeight; // Determine the size of the square cell
        int cellSide = Math.min(availableWidth / columns, availableHeight / rows); // Calculate cell positions
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int cellX = x + padding + col * (cellSide + padding);
                int cellY = y + padding + row * (cellSide + padding); // Draw the square cell
                g2d.setColor(new Color(0x404040));
                g2d.fillRect(cellX, cellY, cellSide, cellSide);
                ItemStack itemStack = inventoryGrid[row][col]; // Use itemStack instead of just item
                if (itemStack != null) {
                    Item item = itemStack.getItem();
                    if (item != null) {
                        BufferedImage img = item.getImage();
                        if (img != null) {
                            g2d.drawImage(img, cellX, cellY, cellSide, cellSide, null);
                        } else {
                            g2d.setColor(getItemColor(item)); // Use the helper function
                            Ellipse2D.Double circle = new Ellipse2D.Double(cellX, cellY, cellSide, cellSide);
                            g2d.fill(circle);
                        }
                        g2d.setColor(Color.WHITE);
                        String quantityText = String.valueOf(itemStack.quantity);
                        FontMetrics fm = g2d.getFontMetrics();
                        int textWidth = fm.stringWidth(quantityText);
                        int textX = cellX + cellSide - textWidth - padding;
                        int textY = cellY + cellSide - fm.getDescent();
                        g2d.drawString(quantityText, textX, textY);
                        if (itemStack == selectedWeaponItemStack) {
                            g2d.setColor(Color.WHITE);
                            g2d.setStroke(new BasicStroke(2));
                            g2d.drawRect(cellX, cellY, cellSide, cellSide);

                        }

                    }

                }

            }

        }

    }

    private Color getItemColor(Item item) { // Changed parameter type to Item
        if (item instanceof Resource) {
            ResourceType resourceType = ((Resource) item).getResourceType(); // Get the ResourceType from Resource
            // Assign colors based on resource type as needed
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
    
            return Color.RED; //Example color for a weapon
        }
        return Color.black; //Or return a default color
    }
}