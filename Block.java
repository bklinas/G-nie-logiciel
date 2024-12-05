package apprentissage;

import java.util.Objects;


/**
 * 
 * 
 * @author karinemoussaoui9@gmail.com
 *
 */
public class Block {
    private int line;
    private int column;

    public Block(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        return "Block [line=" + line + ", column=" + column + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Block other = (Block) obj;
        return this.line == other.line && this.column == other.column;
    }

    @Override
    public int hashCode() {
        return Objects.hash(line, column);
    }
}
