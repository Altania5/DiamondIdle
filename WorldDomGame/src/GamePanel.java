import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Font;
import java.awt.event.*;


public class GamePanel extends JPanel implements Runnable {

    public SolarSystem solarSystem;
    private GameWindow gameWindow;
    private SolarSystem milkyWay;
    private Thread gameThread;
    private boolean running = false;
    public Planet selectedPlanet;
    private Hud hud;
    private Player player;
    private Inventory inventory;

    public GamePanel(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        inventory = new Inventory(player);
        setFocusable(true);
        requestFocus();

        player = new Player("Conqueror", this);

        if (!player.getWeapons().isEmpty()) {
            player.setSelectedWeapon(player.getWeapons().get(0));
        }

        inventory = new Inventory(player);
        add(inventory);

        addMouseListener(new MouseAdapter() {

        @Override
        public void mouseClicked(MouseEvent e) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            selectedPlanet = null;  // Reset selectedPlanet *before* checking for clicks

            for (Planet p : solarSystem.getPlanets()) {
                Image planetImage = p.getImage();
                if (planetImage != null) {
                    int imageX = (getWidth() - planetImage.getWidth(null)) / 2;
                    int centerY = (70 + getHeight() - 200) / 2 - (planetImage.getHeight(null) / 2);  // Use the correct centerY
        
                    int planetLeft = imageX;
                    int planetRight = imageX + planetImage.getWidth(null);
                    int planetTop = centerY;           // Use centerY for top
                    int planetBottom = centerY + planetImage.getHeight(null); // Use centerY for bottom
        
        
                    if (mouseX >= planetLeft && mouseX <= planetRight &&
                            mouseY >= planetTop && mouseY <= planetBottom) {
                        selectedPlanet = p;
                        hud.setSelectedPlanet(p);
                        break; // Exit loop after finding clicked planet
                    }
        
                } else {
                   System.err.println("Image for planet " + p.getName() + " not loaded.");
                }
            }


                if (selectedPlanet != null && selectedPlanet.getHealth() > 0) { // Check AFTER loop
                    int damage = player.getAttackPower();
                    if (player.getSelectedWeapon() != null) {
                        damage += player.getSelectedWeapon().getAttackPower();
                    }
                    player.attackPlanet(selectedPlanet, damage);
                    if (selectedPlanet.isDestroyed()) {
                        solarSystem.removePlanet(selectedPlanet);
                        selectedPlanet = null;
                        hud.setSelectedPlanet(null); 
                    }
                    hud.repaint(); 
                    } else { // No planet clicked
                        //Check if inventory button was clicked
                        if (e.getX() >= 5 && e.getX() <= 60 && e.getY() >= 5 && e.getY() <= 25) {
                            hud.setShowInventory(!hud.isShowInventory());
                            hud.repaint(); 
                        }
                    }
                }
            });

            addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                char keyChar = e.getKeyChar();
                if (keyChar == 'i' || keyChar == 'I') {  // Check for both lowercase and uppercase 'i'
                    hud.setShowInventory(!hud.isShowInventory()); // Toggle inventory visibility
                    hud.repaint();
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER && gameWindow.getCurrentState() == GameWindow.GameState.TITLE) {
                    gameWindow.updateGameState(GameWindow.GameState.GAME);
                }

                if (e.getKeyCode() == KeyEvent.VK_I) {
                    inventory.setVisible(!inventory.isVisible());
                    repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Not used
            }
        });

        Weapon drill = new Weapon("Drill", 1);

        player.acquireWeapon(drill);

        player.setSelectedWeapon(drill);

        hud = new Hud(this, player);
        hud.setBounds(0, 0, 700, 500); // Set the bounds to cover the entire GamePanel
        add(hud);

        Planet earth = new Planet("Earth", "WorldDomGame\\res\\planets\\2211302432.png", new ArrayList<>(Arrays.asList(ResourceType.ORE, ResourceType.GAS, ResourceType.STONE))); // Changed image path
        Planet murcury = new Planet("Mercury", "WorldDomGame\\res\\planets\\2211302432 (1).png", new ArrayList<>(Arrays.asList(ResourceType.ORE, ResourceType.GAS, ResourceType.STONE)));
        Planet venus = new Planet("Venus", "WorldDomGame\\res\\planets\\2211302432 (2).png", new ArrayList<>(Arrays.asList(ResourceType.ORE, ResourceType.GAS, ResourceType.STONE)));
        Planet mars = new Planet("Mars", "WorldDomGame\\res\\planets\\2211302432 (1).png", new ArrayList<>(Arrays.asList(ResourceType.ORE, ResourceType.GAS, ResourceType.STONE)));

        milkyWay = new SolarSystem("Milky Way");
        milkyWay.clear();
        milkyWay.addPlanet(earth);
        milkyWay.addPlanet(murcury);
        milkyWay.addPlanet(venus);
        milkyWay.addPlanet(mars);

        this.solarSystem = milkyWay;
        milkyWay.setCurrentPlanet(1);
        this.selectedPlanet = milkyWay.getSelectedPlanet();
        hud.setSelectedPlanet(selectedPlanet);
        startGame();
    }

    private void startGame() {
        if (gameThread == null || !running) {
            gameThread = new Thread(this);
            gameThread.start();
            running = true;
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerTick = 1000000000D / 60D; // 60 updates per second

        long lastTimer = System.currentTimeMillis();
        double delta = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / nsPerTick;
            lastTime = now;

            boolean shouldRender = false;

            while (delta >= 1) {
                delta -= 1;
                updateGame(); // Update game logic
                shouldRender = true;
            }

            // Limit the frame rate to save CPU
            if (shouldRender) {
                repaint();
            }

            if (System.currentTimeMillis() - lastTimer >= 1000) {
                lastTimer += 1000;
                // One second has passed - you can update a timer or FPS counter here
            }

            try {
                Thread.sleep(2); // Sleep to limit the frame rate
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
        if (gameWindow.getCurrentState() == GameWindow.GameState.TITLE) {
            drawTitleScreen(g);
        }

        if (gameWindow.getCurrentState() == GameWindow.GameState.GAME) {
            for (Planet p : solarSystem.getPlanets()) {
                Image planetImage = p.getImage();
                if (planetImage != null) {
                    int imageX = (getWidth() - planetImage.getWidth(null)) / 2;
    
                    // Calculate the center position between the HUD bars
                    int centerY = (70 + getHeight() - 200) / 2 - (planetImage.getHeight(null) / 2); 
                                                                //70 offset is top bar and 200 offset is bottom bar
    
                    g.drawImage(planetImage, imageX, centerY, this);
                } else {
                    System.err.println("Image not loaded for planet: " + p.getName());
                }
            hud.paintComponent(g);
            }
        }

        if (inventory.isVisible()) {
            inventory.paintComponent(g);
        }
    }

    private void drawTitleScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight()); // Fill the background with black

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("Galactic Conqueror", getWidth() / 2 - 200, getHeight() / 2 - 50); // Center the title

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        g.drawString("Press ENTER to start", getWidth() / 2 - 100, getHeight() / 2 + 50); // Center the start message
    }
}