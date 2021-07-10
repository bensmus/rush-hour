package rushhour;

import java.util.*;

// Converter converts between datatypes that encode the same amount of information
// For example, an integer and a Car object are both used to store the 
// same amount of information, but one is more usefull for doing operations on
// and the other is more useful for storage. 
public class Converter {

    private static int carToInt(Car car) {
        // implicit conversion to ascii value
        int compressed = car.character;

        compressed = compressed << 3 | car.topleft[0];
        compressed = compressed << 3 | car.topleft[1];
        compressed = compressed << 1 | (car.horiz ? 1 : 0);

        return compressed;
    }

    private static Car intToCar(int n) {

        // car.horiz
        int horizBit = n & 0b1;
        boolean horiz = horizBit == 1;

        // car.topleft
        int x = n >> 1 & 0b111;
        int y = n >> (1 + 3) & 0b111;
        int[] topleft = { y, x };

        // car.character
        char character = (char) (n >> (1 + 3 + 3));

        return new Car(character, topleft, horiz);
    }

    public static HashSet<Integer> toIntegerSet(HashMap<Character, Car> carMap) {
        HashSet<Integer> out = new HashSet<>();
        for (Character key : carMap.keySet()) {
            Car car = carMap.get(key);
            out.add(carToInt(car));
        }
        return out;
    }

    public static HashMap<Character, Car> toCarMap(HashSet<Integer> hashSet) {
        HashMap<Character, Car> out = new HashMap<>();
        for (int n : hashSet) {
            Car car = intToCar(n);
            out.put(car.character, car);
        }
        return out;
    }

    public static HashMap<Character, Car> toCarMap(char[][] charArray) {
        // e.g map 'A' -> [[0, 0], [0, 1], [0, 2]]
        // map used for intermediate step
        HashMap<Character, ArrayList<int[]>> coorMap = new HashMap<>();

        HashMap<Character, Car> out = new HashMap<>();

        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 6; x++) {

                // the current character and coordinate
                char character = charArray[y][x];
                int[] coor = { y, x };

                if (character != '.') {
                    if (coorMap.containsKey(character)) {
                        ArrayList<int[]> coors = coorMap.get(character);
                        coors.add(coor);
                    } else {
                        ArrayList<int[]> coors = new ArrayList<>();
                        coors.add(coor);
                        coorMap.put(character, coors);
                    }
                }
            }
        }

        for (char character : coorMap.keySet()) {
            ArrayList<int[]> coors = coorMap.get(character);
            int[][] coorsArray = coors.toArray(new int[0][0]);
            out.put(character, new Car(character, coorsArray));
        }

        return out;

    }

    // used when initializing Brd from CBrd
    public static char[][] toCharArray(HashMap<Character, Car> carMap) {
        char[][] board = BrdUtil.emptyBoard();

        // iterate through carMap to get all Car characters and coors
        for (char character : carMap.keySet()) {
            Car car = carMap.get(character);
            board = BrdUtil.setAt(board, car.coors, car.character);
        }
        return board;
    }

    public static void main(String[] args) {
        // Car tests
        // int[][] coors = { { 0, 3 }, { 0, 4 }, { 0, 5 } };
        // Car car = new Car('A', coors);
        // System.out.println(car.character);
        // System.out.println(car.topleft[0] + " " + car.topleft[1]);
        // System.out.println(car.horiz);
        // System.out.println(Integer.toString(carToInt(car), 2));

        // int n = 0b10000010000111;
        // Car car2 = intToCar(n);
        // System.out.println(car2.character);
        // System.out.println(car2.topleft[0] + " " + car.topleft[1]);
        // System.out.println(car2.horiz);
        // System.out.println(Integer.toString(carToInt(car2), 2));

        // test toCarMap  and toCharArray

        // char[][] charArray = Solver.getCharArray("puzzles/A00.txt");
        // HashMap<Character, Car> carMap = toCarMap(charArray);
        // for (char character : carMap.keySet()) {
        //     System.out.println(Character.toString(character) + " " + carMap.get(character).topleft[0] + " "
        //             + carMap.get(character).topleft[1]);
        // }
        // char[][] charArray2 = toCharArray(carMap);
        // for (int y = 0; y < 6; y++) {
        //     for (int x = 0; x < 6; x++) {
        //         System.out.print(charArray2[y][x]);
        //     }
        //     System.out.println();
        // }

        // Examples for README
        int[] topleft = { 0, 0 };
        System.out.println(carToInt(new Car('A', topleft, true)));
        System.out.println(Integer.toString(carToInt(new Car('A', topleft, true)), 2));

        int[] topleft2 = { 2, 0 };
        System.out.println(carToInt(new Car('B', topleft2, true)));
        System.out.println(Integer.toString(carToInt(new Car('A', topleft2, true)), 2));

        int[] topleft3 = { 3, 0 };
        System.out.println(carToInt(new Car('C', topleft3, false)));
        System.out.println(Integer.toString(carToInt(new Car('A', topleft3, false)), 2));

    }
}
