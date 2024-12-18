import javax.swing.JFrame;

public class GameWindow extends JFrame {

    private GamePanel gamePanel;
    private GameState currentState;

    public static int WIDTH = 800;
    public static int HEIGHT = 600;

    public enum GameState {
        TITLE,
        GAME
    }

    public GameWindow() {
        setTitle("Galactic Conqueror");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the JFrame on the screen
        setResizable(false); // Prevent the JFrame from being resized
        requestFocus(); // Request focus to the JFrame
        setVisible(true); // Make the JFrame visible

        gamePanel = new GamePanel(this);
        add(gamePanel); // Add the game panel to the JFrame

        setCurrentState(GameState.TITLE); // Start in the title state
    }

    public GameState getCurrentState() {
        return currentState;
        
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
        
    }

    public int getWidth() {
        return WIDTH;
    }

    public int getHeight() {
        return HEIGHT;
    }

    public void updateGameState(GameState newState) {
        setCurrentState(newState);
        repaint(); // Repaint to reflect the state change
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new GameWindow().setVisible(true);
        });
    }
}