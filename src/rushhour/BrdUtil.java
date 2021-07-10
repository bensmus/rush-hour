package rushhour;

import java.util.*;

// BrdUtil is used for working with board charArray. 
public class BrdUtil {

    static char[][] emptyBoard() {
        char[][] board = new char[6][6];
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 6; x++) {
                board[y][x] = '.';
            }
        }
        return board;
    }

    static ArrayList<Character> xslice(char[][] array2D, int y, int xmin, int xmax) {
        ArrayList<Character> array = new ArrayList<Character>();
        char[] row = array2D[y];
        for (int i = xmin; i < xmax; i++) {
            array.add(row[i]);
        }
        return array;
    }

    static ArrayList<Character> yslice(char[][] array2D, int x, int ymin, int ymax) {
        ArrayList<Character> array = new ArrayList<Character>();
        for (int i = ymin; i < ymax; i++) {
            char[] row = array2D[i];
            array.add(row[x]);
        }
        return array;
    }

    // Returns a new array where values at coordinates 
    // have been changed to setChar.
    static char[][] setAt(char[][] array2D, int[][] coors, char setChar) {
        char[][] outArray2D = new char[6][6];

        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 6; x++) {
                outArray2D[y][x] = array2D[y][x];
            }
        }

        for (int[] coor : coors) {
            outArray2D[coor[0]][coor[1]] = setChar;
        }

        return outArray2D;
    }

    // Shift coordinates by a certain amount, 
    // in a direction determined by horiz and the sign of moveAmount.
    static int[][] shiftCoors(int[][] coors, int moveAmount, boolean horiz) {
        int[][] shiftedCoors = new int[coors.length][2];
        for (int n = 0; n < coors.length; n++) {
            if (horiz) {
                // add to the second coordinate (x)
                shiftedCoors[n][0] = coors[n][0];
                shiftedCoors[n][1] = coors[n][1] + moveAmount;
            } else {
                // add to the first coordinate (y)
                shiftedCoors[n][0] = coors[n][0] + moveAmount;
                shiftedCoors[n][1] = coors[n][1];
            }
        }
        return shiftedCoors;
    }

    public static void main(String[] args) {
        // Test shiftCoors
        // int[][] coors = {
        //     {1, 2}, {3, 4}, {5, 6}
        // };
        // System.out.println(Arrays.deepToString(coors));
        // System.out.println(Arrays.deepToString(
        //     shiftCoors(coors, -1, false)));

    }
}
