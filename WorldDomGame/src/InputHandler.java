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
            gamePanel.getInventory().setVisible(!gamePanel.getInventory().isVisible());
            gamePanel.repaint();
        }

        if (window.getCurrentState() == GameState.TITLE && e.getKeyCode() == KeyEvent.VK_ENTER) {
            window.setCurrentState(GameState.GAME);
            gamePanel.startGame(); // You might need to create this method in GamePanel
        } 

        if (e.getKeyCode() == KeyEvent.VK_F) {
            gamePanel.showFPS = gamePanel.showFPS = !gamePanel.showFPS;
            gamePanel.repaint();
        }

        if (e.getKeyCode() == KeyEvent.VK_H) {
            gamePanel.showHitboxes = !gamePanel.showHitboxes;
            gamePanel.repaint();
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
        gamePanel.handleMousePressed(e);
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