package rushhour;

import java.util.*;

// CBrd is the compressed form of Brd, stored directly in PQueue and indirectly in Storage.
// CBrd has no functionality but contains the same amount of information. 
public class CBrd implements Comparable<CBrd> {

    // Compressed description of where the cars are located on board.
    HashSet<Integer> state;

    // Same but for the parent board.
    HashSet<Integer> parent;

    // Heuristic. Used to determine precedence in the PQueue.
    int MTS;

    // The move to get from parentState to state.
    String move;

    public CBrd(Brd current) {
        state = Converter.toIntegerSet(current.carMap);

        // We can have a board where charArrayParent is null- the starting board!
        if (current.charArrayParent == null) {
            parent = null;
        } else {
            HashMap<Character, Car> carMapParent = Converter.toCarMap(current.charArrayParent);
            parent = Converter.toIntegerSet(carMapParent);
        }

        MTS = current.MTS();
        move = current.move;
    }

    // Setting a natural ordering for CBrd so that the priority queue
    // knows what to prioritize.
    @Override
    public int compareTo(CBrd other) {
        if (this.MTS < other.MTS) {
            return -1;
        }
        return 1;
    }

    public static void main(String[] args) {
    }
}
