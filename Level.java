package apprentissage;

/**
 * 
 * 
 * @author karinemoussaoui9@gmail.com
 *
 */
import java.util.List;

public class Level {
    private List<int[]> foodPositions;

    public Level(List<int[]> foodPositions) {
        this.foodPositions = foodPositions;
    }

    public List<int[]> getFoodPositions() {
        return foodPositions;
    }
}

