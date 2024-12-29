import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Font;
import java.awt.event.*;

public class GamePanel extends JPanel implements Runnable {

    public SolarSystem solarSystem;
    private InputHandler inputHandler;
    private GameWindow gameWindow;
    private SolarSystem milkyWay;
    private Thread gameThread;
    private boolean running = false;
    public Planet selectedPlanet;
    private Hud hud;
    private Player player;
    private Inventory inventory;
    public boolean showFPS = true;
    private int fps = 0;
    public boolean showHitboxes = false;

    public GamePanel(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        inputHandler = new InputHandler(this, gameWindow);
        setFocusable(true);
        requestFocus();

        inventory = new Inventory();
        add(inventory);

        player = new Player("Conqueror", this, inventory);

        Drill drill = new Drill();
        player.acquireWeapon(drill);
        player.setSelectedWeapon(drill);

        inventory.updateInventory(player);

        hud = new Hud(this, player);
        hud.setBounds(0, 0, 700, 500);
        add(hud);

        // In GamePanel constructor:
        Planet earth = new Planet(350, 150, 50, "Earth", "WorldDomGame\\res\\planets\\2211302432.png", new ArrayList<>(Arrays.asList(ResourceType.ORE, ResourceType.GAS, ResourceType.STONE)));
        Planet murcury = new Planet(300, 150, 30, "Mercury", "WorldDomGame\\res\\planets\\2211302432 (1).png", new ArrayList<>(Arrays.asList(ResourceType.ORE, ResourceType.GAS, ResourceType.STONE)));
        Planet venus = new Planet(500, 200, 40, "Venus", "WorldDomGame\\res\\planets\\2211302432 (2).png", new ArrayList<>(Arrays.asList(ResourceType.ORE, ResourceType.GAS, ResourceType.STONE)));
        Planet mars = new Planet(700, 250, 35, "Mars", "WorldDomGame\\res\\planets\\2211302432 (1).png", new ArrayList<>(Arrays.asList(ResourceType.ORE, ResourceType.GAS, ResourceType.STONE)));

        milkyWay = new SolarSystem("Milky Way");
        milkyWay.clear();
        milkyWay.addPlanet(earth);

        this.solarSystem = milkyWay;
        milkyWay.setCurrentPlanet(1);
        this.selectedPlanet = milkyWay.getSelectedPlanet();
        hud.setSelectedPlanet(selectedPlanet);
        startGame();
    }

    public void handleMouseClicked(MouseEvent e) {
        if (inventory.isVisible() && inventory.getMousePosition() != null) {
            inventory.handleClick(e);
            return;
        }

        Planet clickedPlanet = null; 
        for (Planet planet : solarSystem.getPlanets()) {
            if (planet.getHitbox().contains(e.getPoint())) { 
                clickedPlanet = planet;
                break;
            }
        }

        if (clickedPlanet != null) {
        selectedPlanet = clickedPlanet;
        hud.setSelectedPlanet(selectedPlanet);
        } else {
            selectedPlanet = null;
            hud.setSelectedPlanet(null);
        }

        if (selectedPlanet != null) {
            if (player.getSelectedWeapon() == null) {
                System.out.println("No weapon selected!");
                return;
            }

            // Check if the click is within the hitbox
            if (selectedPlanet.getHitbox().contains(e.getPoint())) {
                int damage = player.getAttackPower() + player.getSelectedWeapon().getAttackPower();
                player.attackPlanet(selectedPlanet, damage);

                if (selectedPlanet.isDestroyed()) {
                    solarSystem.removePlanet(selectedPlanet);
                    selectedPlanet = null;
                    hud.setSelectedPlanet(null);
                }
                hud.repaint();
            }
        }
    }

    public void handleMousePressed(MouseEvent e) {
        if (inventory.isVisible() && inventory.getMousePosition() != null) {
            inventory.handleMousePress(e);
        }
    }
    
    public void handleMouseReleased(MouseEvent e) {
        if (inventory.isVisible() && inventory.getMousePosition() != null) {
            inventory.handleMouseReleased(e);
        }
    }
    
    public void handleMouseDragged(MouseEvent e) {
        if (inventory.isVisible() && inventory.getMousePosition() != null) {
            inventory.handleMouseDrag(e);
        }
    }

    public void handleMouseMoved(MouseEvent e) {
        // Not used in this example, but you can add logic if needed
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void startGame() {
        if (gameThread == null || !running) {
            gameThread = new Thread(this);
            gameThread.start();
            running = true;
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / 60D;

        long lastTimer = System.currentTimeMillis();
        double delta = 0;
        int frames = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            boolean shouldRender = false;

            while (delta >= 1) {
                delta -= 1;
                updateGame();
                shouldRender = true;
            }

            if (shouldRender) {
                frames++;
                repaint();
            }

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                fps = frames;
                frames = 0;
            }

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateGame() {
        
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameWindow.getCurrentState() == GameState.TITLE) {
            drawTitleScreen(g);
        }

        if (showFPS) { // Draw FPS only if enabled
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 12));
            g.drawString("FPS: " + fps, getParent().getWidth() - 80, 15); // Display FPS at top-left
        }

        if (gameWindow.getCurrentState() == GameState.GAME) {
        for (Planet p : solarSystem.getPlanets()) {
            if (showHitboxes) {
                p.showHitboxes((Graphics2D) g); //Correct location for drawing hitboxes
            }

            Image planetImage = p.getImage();
            if (planetImage != null) {
                int imageX = p.x;
                int imageY = p.y;

                g.drawImage(planetImage, imageX, imageY, this); 
                p.generateHitbox();

            } else {
                System.err.println("Image not loaded for planet: " + p.getName());
            }
            hud.paintComponent(g); // Make sure hud is painted *after* the planets.
        }
    }

        if (inventory.isVisible()) {
            inventory.paintComponent(g);
        }
    }

    private void drawTitleScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("Galactic Conqueror", getWidth() / 2 - 200, getHeight() / 2 - 50);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("Press ENTER to start", getWidth() / 2 - 100, getHeight() / 2 + 50);
    }
}
