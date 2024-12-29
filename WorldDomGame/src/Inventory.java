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
    public int rows = 3;
    public int columns = 13;
    private int padding = 10;
    public InventorySlot[][] inventorySlots = new InventorySlot[rows][columns];
    private ItemStack selectedWeaponItemStack;
    private int mouseX, mouseY;
    public int draggedItemStackRow, draggedItemStackCol;

    // Drag and drop
    public ItemStack draggedItemStack = null;
    private int dragOffsetX, dragOffsetY;

    public Inventory() {
        setOpaque(false);
        setFocusable(true);
        setLayout(null); // Use null layout to position slots manually

        // Initialize inventory slots
        initializeSlots();

        setPreferredSize(new Dimension(600, 200));
    }

    public void setDraggedItemStack(ItemStack itemStack, int offsetX, int offsetY) {
        draggedItemStack = itemStack;
        dragOffsetX = offsetX;
        dragOffsetY = offsetY;
        repaint();
    }

    public void handleMouseDragged(MouseEvent e) {
        if (draggedItemStack != null) {
            mouseX = e.getX() - dragOffsetX;
            mouseY = e.getY() - dragOffsetY;
            repaint();
        }
    }

    public void handleMouseReleased(MouseEvent e) {
        if (draggedItemStack != null) {
            InventorySlot targetSlot = getInventorySlotAt(e.getX(), e.getY());
            if (targetSlot != null) {
                // Swap items in the slots
                InventorySlot originSlot = inventorySlots[draggedItemStackRow][draggedItemStackCol];
                ItemStack targetItemStack = targetSlot.getItemStack();
    
                targetSlot.setItemStack(draggedItemStack);
                originSlot.setItemStack(targetItemStack);
            }
    
            draggedItemStack = null;
            repaint();
        }
    }

    private int calculateCellSide() {
        Dimension size = getPreferredSize();
        int totalPaddingWidth = (columns + 1) * padding;
        int totalPaddingHeight = (rows + 1) * padding;
        int availableWidth = size.width - totalPaddingWidth;
        int availableHeight = size.height - totalPaddingHeight;
        int cellSide = Math.min(availableWidth / columns, availableHeight / rows);

        return cellSide;
    }

    public void updateInventory(Player player) {
        this.player = player;
    
        // Clear existing items in slots
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                inventorySlots[row][col].setItemStack(null);
            }
        }
    
        // Update slots with player's items
        int slotIndex = 0;
        for (Weapon weapon : player.getWeapons()) {
            if (slotIndex < rows * columns) {
                int row = slotIndex / columns;
                int col = slotIndex % columns;
                inventorySlots[row][col].setItemStack(new ItemStack(weapon, 1));
                slotIndex++;
            } else {
                break; // Inventory is full
            }
        }
    
        for (Resource resource : player.getResources()) {
            if (slotIndex < rows * columns) {
                int row = slotIndex / columns;
                int col = slotIndex % columns;
                inventorySlots[row][col].setItemStack(new ItemStack(resource, 1)); // Assuming quantity 1 for now
                slotIndex++;
            } else {
                break; // Inventory is full
            }
        }
    
        repaint();
    }

    public void toggleHitboxes(boolean showHitboxes) {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                inventorySlots[row][col].setShowHitbox(showHitboxes);
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

    //Update getInventoryGrid
    public ItemStack[][] getInventoryGrid() {
        ItemStack[][] grid = new ItemStack[rows][columns];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                grid[row][col] = inventorySlots[row][col].getItemStack();
            }
        }
        return grid;
    }

    public void initializeSlots() {
        int cellSide = calculateCellSide();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int cellX = padding + col * (cellSide + padding);
                int cellY = padding + row * (cellSide + padding);
                inventorySlots[row][col] = new InventorySlot(cellX, cellY, cellSide, this);
                inventorySlots[row][col].setBounds(cellX, cellY, cellSide, cellSide);
                add(inventorySlots[row][col]); // Add the slot to the Inventory panel
            }
        }
    }

    public InventorySlot getInventorySlotAt(int x, int y) {
        int cellSide = calculateCellSide();
        if (cellSide > 0) {
            int col = (x - padding) / (cellSide + padding);
            int row = (y - padding) / (cellSide + padding);
    
            if (row >= 0 && row < rows && col >= 0 && col < columns) {
                return inventorySlots[row][col];
            }
        }
        return null; // Not over any slot
    }

    public void drawInventory(Graphics g, int parentWidth, int parentHeight) {

        if (!visible) {
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the background panel
        int panelWidth = parentWidth;
        int panelHeight = parentHeight / 3;
        int x = 0;
        int y = parentHeight - panelHeight;
        g2d.setColor(new Color(0x202020));
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(x, y, panelWidth, panelHeight, arcRadius, arcRadius);
        g2d.fill(roundedRectangle);
        g2d.setColor(Color.WHITE);
        g2d.draw(roundedRectangle);

        // Draw dragged item if any
        if (draggedItemStack != null) {
            int cellSide = calculateCellSide();
            g.drawImage(draggedItemStack.getItem().getImage(), mouseX, mouseY, cellSide, cellSide, null);
        }
    }
}