import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Map;

public class Hud extends JPanel {
    private Planet selectedPlanet;
    private Player player;
    private GamePanel gamePanel;
    public boolean showInventory = false; // Flag to control inventory visibility

    public Hud(GamePanel gamePanel, Player player) {
        super();
        this.gamePanel = gamePanel;
        this.player = player;

        setLayout(null);
    }

    public boolean isShowInventory() {
        return showInventory;
    }
    
    public void setShowInventory(boolean showInventory) {
        this.showInventory = showInventory;
    }
    
    public void setSelectedPlanet(Planet planet) {
        this.selectedPlanet = planet;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (selectedPlanet != null) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Planet: " + selectedPlanet.getName(), 20, 20);
            g.drawString("Health: " + selectedPlanet.getHealth(), 20, 40);
        }

        if (showInventory) {
            int inventoryWidth = 300;
            int inventoryHeight = 200;  // Start with a base height
            int arcRadius = 20; // Radius for rounded corners
            int x = (gamePanel.getWidth() - inventoryWidth) / 2;
            int y = (gamePanel.getHeight() - inventoryHeight) / 2; // Center vertically


            // Calculate rows needed for resources and weapons
            int resourceRows = ResourceType.values().length;
            int weaponRows = player.getWeapons().size() + 1; // Add one for title


            int totalRows = resourceRows + weaponRows + 3; // Resources, weapons, titles, and spacing
            inventoryHeight = totalRows * 20 + 40; // Calculate total height based on rows

            y = (gamePanel.getHeight() - inventoryHeight) / 2; // Recalculate y with adjusted height



            // Draw rounded rectangle for inventory background
            g2d.setColor(new Color(0x202020)); //Slightly darker background
            RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(x, y, inventoryWidth, inventoryHeight, arcRadius, arcRadius);
            g2d.fill(roundedRectangle);

            g2d.setColor(Color.WHITE);
            g2d.draw(roundedRectangle); // White border

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));

            int currentY = y + 25; // Start drawing inside the panel with padding

            // Draw Title
            drawCenteredString(g2d, "Inventory", x, currentY, inventoryWidth);
            currentY += 25;

            // Draw Resources
            drawCenteredString(g2d, "Resources:", x, currentY, inventoryWidth);
            currentY += 20;
            for (ResourceType resourceType : ResourceType.values()) {
                int amount = player.getResources().getOrDefault(resourceType, 0);
                drawCenteredString(g2d, resourceType.name() + ": " + amount, x, currentY, inventoryWidth);
                currentY += 20;
            }
            currentY += 10;

             // Draw Weapons
            drawCenteredString(g2d,"Weapons:", x, currentY, inventoryWidth);
            currentY += 20;
            for (Weapon weapon : player.getWeapons()) {
                drawCenteredString(g2d, weapon.getName(), x, currentY, inventoryWidth);
                if (weapon == player.getSelectedWeapon()) {
                    g2d.setColor(Color.YELLOW); // Highlight selected weapon
                    drawCenteredString(g2d, weapon.getName(), x, currentY, inventoryWidth);
                    g2d.setColor(Color.WHITE); // Reset color
                }
                currentY += 20;
            }
        }
        
    }
    
    private void drawCenteredString(Graphics2D g, String text, int x, int y, int width) {
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int centeredX = x + (width - textWidth) / 2;
        g.drawString(text, centeredX, y);
    }
}
