package rushhour;

import java.util.*;
import java.util.stream.Collectors;

// Brd is short for Board, and is used to 
// represent a rush hour board state. 
// All the other classes are built around
// this one. 
public class Brd {

    // Where did I come from? 
    // Every Brd has a parentBrd and 
    // the move made to get from parentBrd.
    char[][] charArrayParent;
    String move;

    char[][] charArray;
    HashMap<Character, Car> carMap;

    // Used in the beginning of the program
    // as well as in the futureCBrds method.
    // futureCBrds first generates futureBrds, then converts to CBrds.
    public Brd(char[][] charArray, char[][] charArrayParent, String move) {
        this.charArray = charArray;
        this.charArrayParent = charArrayParent;
        this.move = move;
        this.carMap = Converter.toCarMap(this.charArray);
    }

    // Used to initialize board to poll from 
    // priority queue.
    public Brd(CBrd cBrd) {
        this.carMap = Converter.toCarMap(cBrd.state);
        this.charArray = Converter.toCharArray(this.carMap);
        this.charArrayParent = Converter.toCharArray(Converter.toCarMap(cBrd.parent));
        this.move = cBrd.move;
    }

    public boolean isSolved() {
        return charArray[2][5] == 'X';
    }

    // Used in the heuristic.
    private boolean isBlocker(Car car) {
        // is the car... 
        // (1) not horizontal 
        // (2) ahead of car 
        // (3) has the y coordinate that could block exit

        Car red = carMap.get('X');

        boolean vertical = !car.horiz;

        // Comparing the x coordinates of the car 
        // and the end of the red car.
        boolean ahead = car.coors[0][1] > red.coors[1][1];

        // Checking if the car has 2 as a y-coordinate
        boolean has2 = false;
        for (int[] coor : car.coors) {
            if (coor[0] == 2) {
                has2 = true;
                break;
            }
        }

        return vertical && ahead && has2;
    }

    // The heuristic generating function.
    public int MTS() {
        // MTS stands for Approximate Moves To Solution
        // It underestimates Moves To Solution.

        // Getting the first component of MTS:
        // distance of redCar from exit.
        Car red = carMap.get('X');
        int out = 5 - red.coors[red.coors.length - 1][1];

        // Get a set of all the cars that are blockers
        Set<Car> blockers = carMap.values().stream().filter(car -> isBlocker(car)).collect(Collectors.toSet());

        for (Car blocker : blockers) {

            // Move the blocker out of the way
            for (int moveAmount : blocker.moveAmounts()) {
                Car movedBlocker = new Car(blocker.character,
                        BrdUtil.shiftCoors(blocker.coors, moveAmount, blocker.horiz));
                if (!isBlocker(movedBlocker)) {
                    // Moved the blocker out of the way but created a certain amount of collisions
                    out += Math.abs(moveAmount) + collisionCount(blocker, moveAmount);
                    break;
                }
            }
        }

        return out;
    }

    private static char getDirectionChar(int moveAmount, boolean horiz) {
        if (horiz) {
            if (moveAmount > 0) {
                return 'R';
            } else {
                return 'L';
            }
        } else {
            if (moveAmount > 0) {
                return 'D';
            } else {
                return 'U';
            }
        }
    }

    // How many cars does the car have to move through to 
    // move moveAmount squares?
    // Used in the heuristic.
    private int collisionCount(Car car, int moveAmount) {

        // Section of charArray that to examine for collisions.
        ArrayList<Character> examine;

        if (car.horiz) {
            int y = car.coors[0][0];
            int xmin, xmax;
            if (moveAmount > 0) {
                xmin = car.coors[car.coors.length - 1][1] + 1;
                xmax = xmin + moveAmount;
            } else {
                xmax = car.coors[0][1];
                xmin = xmax + moveAmount;
            }

            examine = BrdUtil.xslice(charArray, y, xmin, xmax);

        } else {
            int x = car.coors[0][1];
            int ymin, ymax;
            if (moveAmount > 0) {
                ymin = car.coors[car.coors.length - 1][0] + 1;
                ymax = ymin + moveAmount;
            } else {
                ymax = car.coors[0][0];
                ymin = ymax + moveAmount;
            }

            examine = BrdUtil.yslice(charArray, x, ymin, ymax);

        }

        int collisions = 0;
        for (Character character : examine) {
            if (character != '.') {
                collisions++;
            }
        }

        return collisions;
    }

    // Generates all possible future boards
    // then returns a list of them compressed.
    public ArrayList<CBrd> futureCBrds() {

        ArrayList<CBrd> out = new ArrayList<>();

        // For each car, get the one step, 
        // no collision moves it can make.
        for (Car car : carMap.values()) {
            for (int moveAmount : car.moveAmounts()) {
                // is moveAmount onestep?
                if (Math.abs(moveAmount) <= 1) {
                    // is moveAmount no collision?
                    if (collisionCount(car, moveAmount) == 0) {
                        // Generate a string code for the moveAmount.
                        char directionChar = getDirectionChar(moveAmount, car.horiz);
                        char[] letters = { car.character, directionChar };
                        String move = new String(letters);

                        // Then, with shifted coors, charArray -> Brd -> CBrd 
                        int[][] shiftedCoors = BrdUtil.shiftCoors(car.coors, moveAmount, car.horiz);
                        char[][] newCharArray = BrdUtil.setAt(charArray, car.coors, '.');
                        newCharArray = BrdUtil.setAt(newCharArray, shiftedCoors, car.character);

                        // new Brd(charArray, parentCharArray, move)
                        Brd brd = new Brd(newCharArray, charArray, move);
                        out.add(new CBrd(brd));
                    }
                }
            }
        }

        return out;
    }

    public static void main(String[] args) {
        // String tests:
        // char directionChar = getDirectionChar(1, false);
        // char letters[] = {'A', directionChar};
        // String move = new String(letters);
        // System.out.println(move);

        // Brd converstion tests:
        // Get a Brd from file, turn it into CBrd, get Brd back.
        // Brd brd = new Brd(Solver.getCharArray("puzzles/A00.txt"), null, null);
        // CBrd cBrd = new CBrd(brd);
        // Brd brd2 = new Brd(cBrd);
        // check brd2 parameters: charArray, charArrayParent, move, carMap
        // System.out.println(Arrays.deepToString(brd2.charArray));

        // futureCBrds tests:
        // Brd brd = new Brd(Solver.getCharArray("puzzles/A00.txt"), null, null);
        // ArrayList<CBrd> future = brd.futureCBrds();
        // System.out.println(future);
        // for (CBrd cBrd : future) {
        //     Brd decompressedBrd = new Brd(cBrd);
        //     System.out.println(cBrd.move);
        //     System.out.println(Arrays.deepToString(decompressedBrd.charArray));
        // }

        // MTS tests:
        // Brd brd = new Brd(Solver.getCharArray("puzzles/A00.txt"), null, null);
        // System.out.println(brd.MTS());

        // Brd futureBrd = new Brd(brd.futureCBrds().get(2));
        // System.out.println(Arrays.deepToString(futureBrd.charArray));
        // System.out.println(futureBrd.MTS());

    }
}
