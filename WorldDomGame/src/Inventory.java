import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class Inventory extends JPanel {

    private Player player;
    private int x, y;  // Position of the inventory
    private int width = 300;
    private int height;
    private int arcRadius = 10;
    private boolean visible = false; // Initially hidden

    public Inventory(Player player) {
        this.player = player;
        setOpaque(false); // Make the panel transparent so it doesn't cover the game
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!visible) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Calculate height dynamically
        int resourceRows = ResourceType.values().length;
        int weaponRows = player.getWeapons().size() + 1; // Add one for title
        int totalRows = resourceRows + weaponRows + 3; // Resources, weapons, titles, and spacing
        height = totalRows * 20 + 40; // Calculate total height based on rows

        x = (getParent().getWidth() - width) - 20; // 20px padding from right
        y = (70 + getParent().getHeight() - 200) / 2 - height / 2;

        // Draw rounded rectangle for inventory background
        g2d.setColor(new Color(0x202020)); // Slightly darker background
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(x, y, width, height, arcRadius, arcRadius);
        g2d.fill(roundedRectangle);
        g2d.setColor(Color.WHITE);
        g2d.draw(roundedRectangle);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));

        int currentY = y + 25; // Start drawing inside the panel with padding

        // Draw Title
        drawCenteredString(g2d, "Inventory",  currentY);
        currentY += 25;

        // Draw Resources
        drawCenteredString(g2d, "Resources:", currentY);
        currentY += 20;
        for (ResourceType resourceType : ResourceType.values()) {
            int amount = player.getResources().getOrDefault(resourceType, 0);
            drawCenteredString(g2d, resourceType.name() + ": " + amount, currentY);
            currentY += 20;
        }
        currentY += 10; // Add some spacing

        // Draw Weapons
        drawCenteredString(g2d,"Weapons:", currentY);
        currentY += 20;
        for (Weapon weapon : player.getWeapons()) {
            String weaponName = weapon.getName();

            if (weapon == player.getSelectedWeapon()) {
                g2d.setColor(Color.YELLOW); // Highlight selected weapon
                drawCenteredString(g2d, weaponName, currentY); // Draw again in yellow
                g2d.setColor(Color.WHITE); // Reset color
            } else {
                drawCenteredString(g2d, weaponName, currentY);
            }
            currentY += 20;
        }
    }

    private void drawCenteredString(Graphics2D g, String text, int y) {
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        int centeredX = x + (width - textWidth) / 2;
        g.drawString(text, centeredX, y);
    }
}
