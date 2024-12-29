import javax.swing.JFrame;

public class GameWindow extends JFrame {

    private GamePanel gamePanel;
    private GameState currentState;
    public static int WIDTH = 800;
    public static int HEIGHT = 600;

    public GameWindow() {
        setTitle("Galactic Conqueror");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        requestFocus();
        setVisible(true);

        gamePanel = new GamePanel(this);
        add(gamePanel);

        setCurrentState(GameState.TITLE);
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
        repaint();
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new GameWindow().setVisible(true);
        });
    }
}