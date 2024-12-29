import java.awt.Rectangle;
import java.awt.event.*;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {

    private GamePanel gamePanel;
    private Inventory inventory;
    private GameWindow window;

    private ItemStack draggedItemStack = null;
    private int dragOffsetX, dragOffsetY;

    public InputHandler(GamePanel gamePanel, GameWindow window) {
        this.gamePanel = gamePanel;
        this.inventory = gamePanel.getInventory();
        this.window = window;
        gamePanel.addKeyListener(this);
        gamePanel.addMouseListener(this);
        gamePanel.addMouseMotionListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_I) {
            gamePanel.toggleInventoryVisibility();
        }

        if (window.getCurrentState() == GameState.TITLE && e.getKeyCode() == KeyEvent.VK_ENTER) {
            window.setCurrentState(GameState.GAME);
            gamePanel.startGame();
        } 

        if (e.getKeyCode() == KeyEvent.VK_F) {
            gamePanel.showFPS = gamePanel.showFPS = !gamePanel.showFPS;
            gamePanel.repaint();
        }

        if (e.getKeyCode() == KeyEvent.VK_H) {
            gamePanel.toggleHitboxes();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        gamePanel.handleMouseClicked(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (inventory.isVisible()) {
            // Iterate through the slots and check if the mouse press is within any slot
            for (int row = 0; row < inventory.rows; row++) {
                for (int col = 0; col < inventory.columns; col++) {
                    InventorySlot slot = inventory.inventorySlots[row][col];
                    if (isWithinSlot(e, slot)) {
                        slot.handleMousePress(e);
                        // If the item is being dragged, store the original row and column
                        if (inventory.draggedItemStack != null) {
                            inventory.draggedItemStackRow = row;
                            inventory.draggedItemStackCol = col;
                        }
                        return; // Exit after handling the press in the relevant slot
                    }
                }
            }
        }
    }

    private boolean isWithinSlot(MouseEvent e, InventorySlot slot) {
        Rectangle bounds = slot.getBounds();
        return bounds.contains(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        gamePanel.handleMouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        gamePanel.handleMouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        gamePanel.handleMouseMoved(e);
    }
}