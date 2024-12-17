import javax.swing.JPanel;
import java.awt.Graphics;
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


    public GamePanel(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        setFocusable(true);
        requestFocus();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();

                for (Planet p : solarSystem.getPlanets()) {
                    int imageX = (getWidth() - p.getImage().getWidth(null)) / 2;
                    int imageY = (getHeight() - p.getImage().getHeight(null)) / 2;


                    int planetLeft = imageX;
                    int planetRight = imageX + p.getImage().getWidth(null);
                    int planetTop = imageY;
                    int planetBottom = imageY + p.getImage().getHeight(null);
                    

                    if (mouseX >= planetLeft && mouseX <= planetRight &&
                            mouseY >= planetTop && mouseY <= planetBottom) {
                        selectedPlanet = p; // Set selectedPlanet in GamePanel
                        hud.setSelectedPlanet(p); // Update the HUD's selectedPlanet
                        System.out.println("Clicked on " + p.getName());

                        if (selectedPlanet != null && selectedPlanet.getHealth() > 0) {
                            int damage = player.getAttackPower();
                            if (player.getSelectedWeapon() != null) {
                                damage += player.getSelectedWeapon().getAttackPower();
                            }
                            player.attackPlanet(selectedPlanet, damage);
                            System.out.println("Planet health " + selectedPlanet.getName() + ": " + selectedPlanet.getHealth());
                            hud.repaint(); // Repaint the Hud to show changes
                        }
                        break; // Important: exit after finding clicked Planet
                    }
                }

                if (selectedPlanet == null) { // No planet clicked 
                    // Check if the click was on the inventory button
                    if (e.getX() >= 5 && e.getX() <= 60 && e.getY() >= 5 && e.getY() <= 25) {
                        hud.setShowInventory(!hud.isShowInventory()); // Use getter and setter
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
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // Not used
            }
        });

        player = new Player("Conqueror", this);

        if (!player.getWeapons().isEmpty()) {
            player.setSelectedWeapon(player.getWeapons().get(0));
        }

        Weapon laser = new Weapon("Laser", 10);
        player.acquireWeapon(laser);

        player.setSelectedWeapon(laser);

        hud = new Hud(this, player);
        hud.setBounds(0, 0, 700, 500); // Set the bounds to cover the entire GamePanel
        add(hud);

        Planet murcury = new Planet("Mercury");
        Planet venus = new Planet("Venus");
        Planet earth = new Planet("Earth", "res/planets/2211302432.png", new ArrayList<>(Arrays.asList(ResourceType.ORE, ResourceType.GAS, ResourceType.STONE)));
        Planet mars = new Planet("Mars");

        milkyWay = new SolarSystem("Milky Way");
        milkyWay.clear();
        milkyWay.addPlanet(murcury);
        milkyWay.addPlanet(venus);
        milkyWay.addPlanet(earth);
        milkyWay.addPlanet(mars);

        this.solarSystem = milkyWay;
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
        // Update game logic here
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameWindow.getCurrentState() == GameWindow.GameState.TITLE) {
            drawTitleScreen(g);
        } else if (gameWindow.getCurrentState() == GameWindow.GameState.GAME) {
            // Draw game elements here
            for (Planet p : solarSystem.getPlanets()) {
                int imageX = (getWidth() - p.getImage().getWidth(null)) / 2;
                int imageY = (getHeight() - p.getImage().getHeight(null)) / 2;
                g.drawImage(p.getImage(), imageX, imageY, this);
            }
            hud.paintComponent(g);
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