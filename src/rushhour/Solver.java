package rushhour;

import java.io.*;
import java.util.*;

public class Solver {
    private static char[][] getCharArray(String inputPath) {
        char[][] array2D = new char[6][6];

        Scanner scanner;

        try {
            scanner = new Scanner(new File(inputPath));
            for (int y = 0; y < 6; y++) {
                String line = scanner.next();

                for (int x = 0; x < 6; x++) {
                    array2D[y][x] = line.charAt(x);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return array2D;
    }

    public static void solveFromFile(String inputPath, String outputPath) {

        // Declare Brd from file, it has no parent charArray and no move to get to it.
        Brd current = new Brd(getCharArray(inputPath), null, null);

        // Declare Storage (hashMap wrapper for checking if a Brd state has been visited).
        Storage visited = new Storage();

        // Declare PQueue (priority queue for CBrd - Compressed Boards).
        PQueue q = new PQueue();

        // Current is the current Board state.
        // Loop until we have reached a solved state.
        while (!current.isSolved()) {

            // Mark current vertex/Board as visited.
            visited.put(new CBrd(current));

            // Add the future board positions that we have not visited already.
            // NOTE: queue only stores CBrd objects.
            q.add(current.futureCBrds(), visited);

            // Overwrite current Board with newly polled.
            current = new Brd(q.poll());
        }

        // current isSolved (Yay!)
        // print it triumphantly if you want
        // System.out.println(Arrays.deepToString(current.charArray));

        CBrd compressedSolution = new CBrd(current);
        visited.put(compressedSolution);

        // Finding the state's parent state, then its parent state,
        // etc. until we reach null parent.
        visited.outputFile(compressedSolution.state, outputPath);
    }

    public static void main(String[] args) {
        solveFromFile("puzzles/A00.txt", "solutions/A00.sol");
    }
}