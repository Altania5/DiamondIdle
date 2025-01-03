import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class Hud extends JPanel {
    private Planet selectedPlanet;
    private Player player;
    private GamePanel gamePanel;
    public boolean showInventory = false;

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

        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, 50, gamePanel.getWidth(), 2);

        String objectiveText = "Objective: " + player.getCurrentObjective().getDescription();

        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        FontMetrics fm = g2.getFontMetrics(new Font("Arial", Font.BOLD, 14)); 
        g2.dispose(); 

        int objectiveWidth = fm.stringWidth(objectiveText) + 20;
        int objectiveHeight = fm.getHeight() + 10;
        int arcRadius = 10;
        int x = gamePanel.getWidth() - objectiveWidth - 20;
        int y = 20;

        g2d.setColor(new Color(0x202020));
        RoundRectangle2D objectiveRect = new RoundRectangle2D.Float(x, y, objectiveWidth, objectiveHeight, arcRadius, arcRadius);
        g2d.fill(objectiveRect);

        g2d.setColor(Color.WHITE);
        g2d.draw(objectiveRect);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString(objectiveText, x + 10, y + fm.getAscent() + 5);

        g2d.setColor(Color.DARK_GRAY);
        g2d.fillRect(0, gamePanel.getHeight() - 200, gamePanel.getWidth(), 2);

        if (selectedPlanet != null) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Planet: " + selectedPlanet.getName(), 20, 20);
            g.drawString("Health: " + selectedPlanet.getHealth(), 20, 40);

            // Draw the hitbox on top of the planet image
            if(gamePanel.showHitboxes) { 
                Ellipse2D hitbox = selectedPlanet.getHitbox();
                g2d.setColor(Color.RED);
                g2d.draw(hitbox);
            }
        }
    }
}
