package apprentissage;

import apprentissage.Block;



/**
 * 
 * 
 * @author karinemoussaoui9@gmail.com
 *
 */
public abstract class MobileElement {
    private Block position;

    public MobileElement(Block position) {
        this.position = position;
    }

    public Block getPosition() {
        return position;
    }

    public void setPosition(Block position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        MobileElement other = (MobileElement) obj;
        return this.position.equals(other.position);
    }
}



