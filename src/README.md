# Rush Hour Solver
## Given a starting board, look at all future boards, and see what move we can make that brings us closest to the solution. 
Each board is assigned a rating, and we get a graph traversal algorithm for a graph that we construct on the fly. 

## The Heuristic Function
The Heuristic function *y* = h(*board*):
1. Start with y = distance of the red car from the exit. 
2. Get a list of cars that are blocking the path of the red car.
3. For each of those cars, *y* += *number of moves that those cars have to make to clear the path for the red car + number of collisions those cars induce as they clear the path for the red car*
4. return *y*

## The Datastructures Used
Priority queue -> PQueue.java
- add and pop compressed forms of board states

Hash Table -> Storage.java
- stores all visited board states as well as their parent states and the move required
to get from parent state to child state

The board state itself -> Brd.java
- contains a 2D array of characters, a Hash Table that maps characters to their Car objects (wrapper for coordinates with additional functionality).

To save memory, board state was never directly stored in the Hash Table or in the Priority Queue. Information about the cars on the board was extracted and then compressed. Here is an example of a 2D array of characters being compressed into a set of car objects and then a set of bit arrays/integers:

``` 
2D array of characters:
[[ A A . . ]
 [ B B B . ]
 [ C . . . ]
 [ C . . . ]]

Car objects:
Car(character = A, top left coordinate = [0, 0], orientation = horizontal)  
Car(character = B, top left coordinate = [1, 0], orientation = horizontal)  
Car(character = C, top left coordinate = [2, 0], orientation = vertical)    

```

Transforming `Car(character = A, top left coordinate = [0, 0], orientation = horizontal)` to integer:
- First 8 bits store character ASCII: `01000001`
- Next 3 bits store y of coordinate: `000`
- Next 3 bits store x of coordinate: `000`
- Next bit stores orientation : `1`
- `0b10000010000001 = 8321`

```
Integers:
8321
8481
8624
```

## Project organization
- Brd.java, CBrd.java (compressed board), Car.java, PQueue.java, and Storage.java implement datastructures used in the algorithm.

- BrdUtil.java and Converter.java are classes that are more like groups of functions (by category). 

    - BrdUtil.java has functions that do operations on coordinates in the Brd class

    - Converter.java converts datastructures from one to the other

- Solver.java contains the algorithm and a private method for converting a file into a character array.

## Challenges in the project
- Converting between datatypes and learning about bit operations was challenging.
- The hardest part was probably generating future boards.
- I initially did not have a separate BrdUtil.java file but adding it made everything more clear.
- I had a really dumb bug where I was comparing strings with '==' and since i'm used to Python I couldn't figure out what was going on for a while. 

