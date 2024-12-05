package apprentissage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * 
 * 
 * @author karinemoussaoui9@gmail.com
 *
 */
public class MobileElementManager {
	private static final int MAX_FOOD = 6;
    private int generatedFoodCount = 0;
    private Map map;
    private Chat chat;
    private GameDisplay gameDisplay;
    private List<Food> food = new ArrayList<>();
    private List<Obstacles> obstacle = new ArrayList<>();
    private int currentLevel;

    public MobileElementManager(Map map) {
        this.map = map;
    }
    
    public boolean isAllFoodEaten() {
        return food.isEmpty();
    }

    public void generateFood() {
        // Clear existing food
        food.clear();

        // Generate new food using fixed positions
        for (int i = 0; i < fixedFoodPositions.length; i++) {
            int[] position = fixedFoodPositions[i];
            int x = position[0];
            int y = position[1];
            food.add(new Food(new Block(x, y)));
        }
    }

    public void setGameDisplay(GameDisplay gameDisplay) {
        this.gameDisplay = gameDisplay;
    }
    private int[][] fixedFoodPositions = {
            {0, 0}, {9, 2}, {2, 5}, {5, 9}
    };


    public void set(Chat chat) {
        this.chat = chat;
    }

    public void add(Food foods) {
        food.add(foods);
    }

    public void add(Obstacles obstacles) {
        obstacle.add(obstacles);
    }

    public void moveLeftChat() {
        moveChat(-1, 0);
    }

    public void moveUpChat() {
        moveChat(0, -1);
    }

    public void moveDownChat() {
        moveChat(0, 1);
    }

    public void moveRightChat() {
        moveChat(1, 0);
    }

    private void moveChat(int deltaX, int deltaY) {
        Block currentPosition = chat.getPosition();
        Block newPosition = map.getBlock(currentPosition.getLine() + deltaY, currentPosition.getColumn() + deltaX);

        if (newPosition != null && !obstacle.contains(new Obstacles(newPosition))) {
            // Check if there is food at the new position
            Iterator<Food> iterator = food.iterator();
            while (iterator.hasNext()) {
                Food currentFood = iterator.next();
                if (currentFood.getPosition().equals(newPosition)) {
                    // Cat ate the food, remove it
                    iterator.remove();
                    // Update score or perform other actions
                }
            }

            chat.setPosition(newPosition);
        }
    }


    public synchronized void nextRound() {
        if (generatedFoodCount < MAX_FOOD) {
            generateFood();
            generatedFoodCount++;
        }

        generateObstacles();
        checkCollisionWithFood();
    }

  
    private void generateObstacles() {
        int randomColumn = getRandomNumber(0, GameConfiguration.COLUMN_COUNT - 1);
        Block position = new Block(0, randomColumn);
        Obstacles obstacle = new Obstacles(position);
        add(obstacle);
    }

    public Chat getChat() {
        return chat;
    }

    public List<Food> getFood() {
        return food;
    }

    public List<Obstacles> getObstacles() {
        return obstacle;
    }

    private static int getRandomNumber(int min, int max) {
        return (int) (Math.random() * (max + 1 - min)) + min;
    }

    public void removeFood(Food food) {
        if (this.food.remove(food)) {
            generatedFoodCount--; // Decrement the counter when removing a food item
            if (gameDisplay != null) {
                gameDisplay.repaint();
            }
            System.out.println("Food removed successfully");
        } else {
            System.out.println("Food not found");
        }
    }

    public synchronized void removeFoodAtPosition(Block position) {
        Food foodToRemove = null;

        // Find the food item at the specified position
        for (Food currentFood : food) {
            if (currentFood.getPosition().equals(position)) {
                foodToRemove = currentFood;
                break;
            }
        }

        // Remove the identified food item
        if (foodToRemove != null) {
            food.remove(foodToRemove);
            removeFood(foodToRemove);

            // Notify the GameDisplay to repaint
            if (gameDisplay != null) {
                gameDisplay.repaint();
            }

            System.out.println("Food at position " + position + " removed visually.");
        } else {
            System.out.println("No food found at position " + position);
        }
    }
    // In your MobileElementManager class, modify the checkCollisionWithFood method
    private synchronized void checkCollisionWithFood() {
        Block catPosition = chat.getPosition();

        // Check if there is food at the cat's position and remove it
        removeFoodAtPosition(catPosition);
    }


    private List<Block> aStarPath;

    public synchronized void moveCatAutonomously() {
        if (aStarPath == null || aStarPath.isEmpty()) {
            // Recalculate the A* path when the path is not available or has been consumed
            calculateAStarPath();
        }

        if (aStarPath != null && !aStarPath.isEmpty()) {
            // Move the cat along the A* path
            Block nextPosition = aStarPath.remove(0);
            chat.setPosition(nextPosition);
        }
    }

    private void calculateAStarPath() {
        Chat chat = getChat();
        List<Food> foods = getFood();

        if (!foods.isEmpty()) {
            Food nearestFood = foods.get(0);

            for (Food food : foods) {
                Block catPosition = chat.getPosition();
                Block foodPosition = food.getPosition();

                int distanceToNearest = Math.abs(nearestFood.getPosition().getColumn() - catPosition.getColumn())
                        + Math.abs(nearestFood.getPosition().getLine() - catPosition.getLine());

                int distanceToCurrent = Math.abs(food.getPosition().getColumn() - catPosition.getColumn())
                        + Math.abs(food.getPosition().getLine() - catPosition.getLine());

                if (distanceToCurrent < distanceToNearest) {
                    nearestFood = food;
                }
            }

            Block catPosition = chat.getPosition();
            Block foodPosition = nearestFood.getPosition();

            // Use A* algorithm to find the path to the nearest food
            aStarPath = findPath(catPosition, foodPosition);
        }
    }

    private List<Block> findPath(Block start, Block goal) {
        List<Block> path = new ArrayList<>();
        PriorityQueue<AStarNode> openSet = new PriorityQueue<>();
        List<Block> closedSet = new ArrayList<>();

        openSet.add(new AStarNode(start, null, 0, calculateH(start, goal)));

        while (!openSet.isEmpty()) {
            AStarNode current = openSet.poll();

            if (current.getPosition().equals(goal)) {
                // Reconstruct the path
                while (current.getParent() != null) {
                    path.add(current.getPosition());
                    current = current.getParent();
                }

                // Reverse the path to start from the beginning
                Collections.reverse(path);
                return path;
            }

            closedSet.add(current.getPosition());

            for (Block neighbor : getNeighbors(current.getPosition())) {
                if (closedSet.contains(neighbor)) {
                    continue; // Skip the neighbor if it's already in the closed set
                }

                int tentativeG = current.getG() + 1;

                AStarNode neighborNode = new AStarNode(neighbor, current, tentativeG, calculateH(neighbor, goal));

                if (!openSet.contains(neighborNode) || tentativeG < neighborNode.getG()) {
                    openSet.remove(neighborNode);
                    openSet.add(neighborNode);
                }
            }
        }

        return Collections.emptyList(); // No path found
    }

    private int calculateH(Block node, Block goal) {
        // Use Manhattan distance heuristic
        return Math.abs(node.getLine() - goal.getLine()) + Math.abs(node.getColumn() - goal.getColumn());
    }

    private List<Block> getNeighbors(Block position) {
        List<Block> neighbors = new ArrayList<>();

        int line = position.getLine();
        int column = position.getColumn();

        // Check and add valid neighbors
        if (line > 0) neighbors.add(map.getBlock(line - 1, column));
        if (line < map.getLineCount() - 1) neighbors.add(map.getBlock(line + 1, column));
        if (column > 0) neighbors.add(map.getBlock(line, column - 1));
        if (column < map.getColumnCount() - 1) neighbors.add(map.getBlock(line, column + 1));

        return neighbors;
    }

    private static class AStarNode implements Comparable<AStarNode> {
        private Block position;
        private AStarNode parent;
        private int g; // Cost from the start node to this node
        private int h; // Estimated cost from this node to the goal node

        public AStarNode(Block position, AStarNode parent, int g, int h) {
            this.position = position;
            this.parent = parent;
            this.g = g;
            this.h = h;
        }

        public Block getPosition() {
            return position;
        }

        public AStarNode getParent() {
            return parent;
        }

        public int getG() {
            return g;
        }

        public int getH() {
            return h;
        }

        public int getF() {
            return g + h;
        }

        @Override
        public int compareTo(AStarNode other) {
            return Integer.compare(getF(), other.getF());
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            AStarNode aStarNode = (AStarNode) obj;
            return position.equals(aStarNode.position);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position);
        }
    }
}
