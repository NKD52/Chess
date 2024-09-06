import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Objects;

public class Coordinate {
    
        int row;
        int col;
    
        public int getRow() {
            return row;
        }

        public int getCol() {
            return col;
        }

        public Coordinate(int row, int col) {
            this.row = row;
            this.col = col;
        }
    
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Coordinate that = (Coordinate) obj;
            return row == that.row && col == that.col;
        }
    
        @Override
        public int hashCode() {
            return Objects.hash(row, col);
        }
    
        @Override
        public String toString() {
            return "(" + row + ", " + col + ")";
        }
    
}
