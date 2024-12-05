package apprentissage;

import apprentissage.Block;

/**
 * 
 * 
 * @author karinemoussaoui9@gmail.com
 *
 */
public class Food {
    private Block position;

    public Food(Block position) {
        this.position = position;
    }

    public Block getPosition() {
        return position;
    }

   
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Food other = (Food) obj;
        return this.position.equals(other.position);
    }
  
    
}



