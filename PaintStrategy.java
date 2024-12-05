package apprentissage;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This class defines a painting strategy for the simulation.
 * 
 * 
 * @author karinemoussaoui9@gmail.com
 *
 */
public class PaintStrategy {
    private MobileElementManager manager;
    private Image catImage;
    private Image foodImage;
    private Image obstaclesImage;

    public PaintStrategy(MobileElementManager manager) {
        this.manager = manager;
    

        // Load the cat, food, and obstacles images using ImageIO with improved error handling
        try {
            File catImageFile = new File("src/images/cute.png");
            File foodImageFile = new File("src/images/fraise.gif");
            File obstaclesImageFile = new File("src/images/mytreee.png");

            if (!catImageFile.exists() || !foodImageFile.exists() || !obstaclesImageFile.exists()) {
                System.err.println("Error: One or more image files do not exist.");
                return;
            }

            // Read the original cat image
            BufferedImage originalCatImage = ImageIO.read(catImageFile);

            // Resize the cat image using AffineTransform
            int catRectangleWidth = GameConfiguration.BLOCK_SIZE;
            int catRectangleHeight = GameConfiguration.BLOCK_SIZE;

            double catScaleX = (double) catRectangleWidth / originalCatImage.getWidth();
            double catScaleY = (double) catRectangleHeight / originalCatImage.getHeight();

            AffineTransform catTransform = new AffineTransform();
            catTransform.scale(catScaleX, catScaleY);

            catImage = new BufferedImage(catRectangleWidth, catRectangleHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D catGraphics = (Graphics2D) catImage.getGraphics();
            catGraphics.drawImage(originalCatImage, catTransform, null);
            catGraphics.dispose();

            // Read the food image
            BufferedImage originalFoodImage = ImageIO.read(foodImageFile);

            // Resize the food image using AffineTransform
            int foodRectangleWidth = GameConfiguration.BLOCK_SIZE;
            int foodRectangleHeight = GameConfiguration.BLOCK_SIZE;

            double foodScaleX = (double) foodRectangleWidth / originalFoodImage.getWidth();
            double foodScaleY = (double) foodRectangleHeight / originalFoodImage.getHeight();

            AffineTransform foodTransform = new AffineTransform();
            foodTransform.scale(foodScaleX, foodScaleY);

            foodImage = new BufferedImage(foodRectangleWidth, foodRectangleHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D foodGraphics = (Graphics2D) foodImage.getGraphics();
            foodGraphics.drawImage(originalFoodImage, foodTransform, null);
            foodGraphics.dispose();
            

            // Read the obstacles image
            BufferedImage originalObstaclesImage = ImageIO.read(obstaclesImageFile);

            // Resize the obstacles image using AffineTransform
            int obstaclesRectangleWidth = GameConfiguration.BLOCK_SIZE;
            int obstaclesRectangleHeight = GameConfiguration.BLOCK_SIZE;

            double obstaclesScaleX = (double) obstaclesRectangleWidth / originalObstaclesImage.getWidth();
            double obstaclesScaleY = (double) obstaclesRectangleHeight / originalObstaclesImage.getHeight();

            AffineTransform obstaclesTransform = new AffineTransform();
            obstaclesTransform.scale(obstaclesScaleX, obstaclesScaleY);

            obstaclesImage = new BufferedImage(obstaclesRectangleWidth, obstaclesRectangleHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D obstaclesGraphics = (Graphics2D) obstaclesImage.getGraphics();
            obstaclesGraphics.drawImage(originalObstaclesImage, obstaclesTransform, null);
            obstaclesGraphics.dispose();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading the images: " + e.getMessage());
        }
    }

    public void paint(Map map, Graphics graphics) {
        int blockSize = GameConfiguration.BLOCK_SIZE;
        Block[][] blocks = map.getBlocks();
        for (Obstacles obstacle : manager.getObstacles()) {
            paintRandomObstacles(obstacle, graphics);
        }
        paintRemainingFoods(manager.getChat(), graphics);
        for (int lineIndex = 0; lineIndex < map.getLineCount(); lineIndex++) {
            Color customColor = new Color(118, 135, 155); // Light blue color
            for (int columnIndex = 0; columnIndex < map.getColumnCount(); columnIndex++) {
                Block block = blocks[lineIndex][columnIndex];

                graphics.setColor(customColor);
                graphics.fillRect(block.getColumn() * blockSize, block.getLine() * blockSize, blockSize, blockSize);
            }
        }
    }

    public void paint(Chat chat, Graphics graphics) {
        int blockSize = GameConfiguration.BLOCK_SIZE;

        Block chatPosition = chat.getPosition(); // Get the cat's position from the Chat object

        int x = chatPosition.getColumn();
        int y = chatPosition.getLine();

        // Calculate the position to center the cat within the rectangle
        int centeredX = x * blockSize + (blockSize - catImage.getWidth(null)) / 2;
        int centeredY = y * blockSize + (blockSize - catImage.getHeight(null)) / 2;

        // Draw the resized cat image at its centered position
        graphics.drawImage(catImage, centeredX, centeredY, null);
    }

    public void paintRandomObstacles(Obstacles obstacle, Graphics graphics) {
        int numObstacles = 6;
        int blockSize = GameConfiguration.BLOCK_SIZE;

        // Positions fixes pour les trois cases de nourriture
        int[][] obstaclePositions = {{1,1},{1,10},{8,3},{4, 5}, {3, 8}, {8, 12}};

        for (int i = 0; i < numObstacles; i++) {
            int[] position = obstaclePositions[i];
            int x = position[0];
            int y = position[1];

            // Calculate the position to center the obstacles within the rectangle
            int centeredX = x * blockSize + (blockSize - obstaclesImage.getWidth(null)) / 2;
            int centeredY = y * blockSize + (blockSize - obstaclesImage.getHeight(null)) / 2;

            // Draw the resized obstacles image at its centered position
            graphics.drawImage(obstaclesImage, centeredX, centeredY, null);
        }
    }

    public void paintRemainingFoods(Chat chat, Graphics graphics) {
        int blockSize = GameConfiguration.BLOCK_SIZE;

        for (Food food : manager.getFood()) {
            Block foodPosition = food.getPosition();

            // Only paint the food if it hasn't been eaten
            if (!foodPosition.equals(chat.getPosition())) {
                int x = foodPosition.getColumn();
                int y = foodPosition.getLine();

                // Calculate the position to center the food within the rectangle
                int centeredX = x * blockSize + (blockSize - foodImage.getWidth(null)) / 2;
                int centeredY = y * blockSize + (blockSize - foodImage.getHeight(null)) / 2;

                // Draw the resized food image at its centered position
                graphics.drawImage(foodImage, centeredX, centeredY, null);
            }
        }
    }
    

}

