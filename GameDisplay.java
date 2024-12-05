package apprentissage;

import java.awt.Graphics;
import javax.swing.JPanel;

/**
 * 
 * 
 * @author karinemoussaoui9@gmail.com
 *
 */
public class GameDisplay extends JPanel {

    private static final long serialVersionUID = 1L;
    private boolean gameOver = false;
    private Map map;
    private MobileElementManager manager;
    private PaintStrategy paintStrategy;

    public GameDisplay(Map map, MobileElementManager manager) {
        this.map = map;
        this.manager = manager;
        this.paintStrategy = new PaintStrategy(manager);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        paintStrategy.paint(map, g);

        Chat chat = manager.getChat();
        paintStrategy.paint(chat, g);

        // Iterate through the food items and paint them
        for (Food food : manager.getFood()) {
            paintStrategy.paintRemainingFoods(chat, g);
        }

        for (Obstacles obstacle : manager.getObstacles()) {
            paintStrategy.paintRandomObstacles(obstacle, g);
        }
        
    }
    public void setMap(Map map) {
        this.map = map;
    }

    public void setManager(MobileElementManager manager) {
        this.manager = manager;
        this.paintStrategy = new PaintStrategy(manager);
    }
    
}
