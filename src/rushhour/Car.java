package rushhour;

import java.util.Arrays;
import java.util.stream.IntStream;

// Car is used extensively in the Brd class for the heuristic
// and for the future Brd generation, as well as for compression 
// in the BrdUtil class. 
public class Car {
    char character;
    int[] topleft;
    boolean horiz;
    int[][] coors;

    // Shift coordinate.
    private static int[] shift(int[] coor, boolean horiz) {
        int[] shifted = new int[2];
        if (horiz) {
            shifted[0] = coor[0];
            shifted[1] = coor[1] + 1;
        } else {
            shifted[0] = coor[0] + 1;
            shifted[1] = coor[1];
        }
        return shifted;
    }

    // Character names imply certain car lengths.
    // Don't have to store car lengths.
    private static boolean isThree(char character) {
        if (character == 'X') {
            return false;
        }
        return character >= 'O';
    }

    // How much can the car be moved without leaving board?
    public int[] moveAmounts() {
        // A moveAmounts array has values ranging from -4 to +4.

        // Getting the minimum and maximum bounds on moveamounts.
        int i = horiz ? 1 : 0;
        int min = 0 - coors[0][i];
        int max = 5 - coors[coors.length - 1][i];

        // Not moving is not a valid option so need to remove 0 moveamounts.
        return IntStream.rangeClosed(min, max).filter(n -> n != 0).toArray();
    }

    // This type of Car initialization is used to convert an integer into 
    // a Car object.
    public Car(char character, int[] topleft, boolean horiz) {
        this.character = character;
        this.topleft = topleft;
        this.horiz = horiz;

        if (isThree(character)) {
            coors = new int[3][2];
            coors[0] = topleft;
            coors[1] = shift(coors[0], horiz);
            coors[2] = shift(coors[1], horiz);
        } else {
            coors = new int[2][2];
            coors[0] = topleft;
            coors[1] = shift(coors[0], horiz);
        }
    }

    // This type of Car initialization is used when
    // extrating information from a charArray.
    public Car(char character, int[][] coors) {
        this.character = character;
        topleft = coors[0];

        // Checking for constant y coordinate.
        horiz = coors[0][0] == coors[1][0];
        this.coors = coors;
    }

    public static void main(String[] args) {
        int[] topleft = { 0, 2 };
        Car car = new Car('A', topleft, true);
        System.out.println(Arrays.toString(car.moveAmounts()));
    }
}
