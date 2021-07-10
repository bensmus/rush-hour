package rushhour;

import java.util.*;

// PQueue stores CBrd objects. Polling from PQueue is
// how we determine the next Board state. 
public class PQueue {
    PriorityQueue<CBrd> queue = new PriorityQueue<>();

    // "added" return variable is to see if we are able to successfully
    // add cBrd to the priorty queue. Not necessary but was used in debugging.
    public boolean add(List<CBrd> cBrds, Storage visited) {
        boolean added = false;
        for (CBrd cBrd : cBrds) {
            if (!visited.isVisited(cBrd)) {
                queue.add(cBrd);
                added = true;
            }
        }
        return added;
    }

    // Poll is another word for pop, I guess. 
    public CBrd poll() {
        return queue.poll();
    }

    public static void main(String[] args) {
        // Brd brd = new Brd(Solver.getCharArray("puzzles/A00.txt"), null, null);
        // ArrayList<CBrd> future = brd.futureCBrds();
        // Storage visited = new Storage();
        // PQueue q = new PQueue();
        // q.add(future, visited);
    }
}
