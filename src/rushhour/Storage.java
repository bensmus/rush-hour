package rushhour;

import java.util.*;
import java.io.*;

// Used in the HashMap of Storage.
// Just stores two variables.
class Value {
    public HashSet<Integer> parent;
    public String move;

    public Value(HashSet<Integer> parent, String move) {
        this.parent = parent;
        this.move = move;
    }
}

// Storage is used so that we don't visit states that we have visited before. 
// This is to avoid cycles/infinte loops.
public class Storage {
    HashMap<HashSet<Integer>, Value> hashMap = new HashMap<>();

    public void put(CBrd c) {
        // Splitting the CBrd into its components and
        // storing them in the HashMap.
        // Value is not null for root put, but it may store two null values.
        hashMap.put(c.state, new Value(c.parent, c.move));
    }

    // Determine whether c was visited
    // i.e is c a key in the HashMap.
    public boolean isVisited(CBrd c) {
        return hashMap.containsKey(c.state);
    }

    // Since we get solution from "parent chain", it will be backwards.
    // Also, all moves are of one move, but it is more readable
    // to do XR2 instead of XR XR. 
    // This function formats the move list accordingly. 
    private static ArrayList<String> formatMoves(ArrayList<String> moves) {

        // the output of our function
        ArrayList<String> movesFormatted = new ArrayList<>();

        // the current string we are reading
        String currentString = moves.get(moves.size() - 1);

        // how many times that string has appeared
        int currentCount = 1;

        for (int i = moves.size() - 2; i > -1; i--) {
            String move = moves.get(i);
            if (move.equals(currentString)) {
                currentCount++;
            } else {
                movesFormatted.add(currentString + currentCount);
                currentString = move;
                currentCount = 1;
            }
        }

        movesFormatted.add(currentString + currentCount);
        return movesFormatted;
    }

    // Get the solution for rush hour from Storage, by looking at "parent chain".
    // Write the moves in the solution to the file.
    public void outputFile(HashSet<Integer> state, String outputPath) {
        ArrayList<String> moves = new ArrayList<>();

        while (true) {
            HashSet<Integer> parent = hashMap.get(state).parent;
            if (parent == null) {
                break;
            }
            moves.add(hashMap.get(state).move);
            state = parent;
        }

        moves = formatMoves(moves);

        try {
            FileWriter fileWriter = new FileWriter(outputPath);
            for (String move : moves) {
                fileWriter.write(move + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
        }
    }
}
