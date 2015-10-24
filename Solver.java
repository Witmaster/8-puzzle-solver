import edu.princeton.cs.algs4.*;
import java.util.Comparator;

public class Solver {
    private class SearchNode implements Comparable<SearchNode> 
    {
        public Board board;
        public int turn;
        public SearchNode previous;
        public SearchNode(Board thisBoard, SearchNode previousBoard, int currentTurn)
        {
            this.board = thisBoard;
            this.turn = currentTurn;
            this.previous = previousBoard;
        }
        public int compareTo(Solver.SearchNode that)
        {
            if (this.board.manhattan() + this.turn < that.board.manhattan() + that.turn)
                return -1;
            if (this.board.manhattan() + this.turn > that.board.manhattan() + that.turn)
                return 1;
            return 0;
        }
        public Comparator<SearchNode> order()
        {
            return new Comparator<SearchNode>() 
            {
                @Override
                public int compare(SearchNode first, SearchNode second)
                {
                    return first.compareTo(second);
                }
            };
        }
    }
    
    private edu.princeton.cs.algs4.MinPQ<SearchNode> queue;
    private int moves = 0;
    private boolean solvable = true;
    private java.util.ArrayDeque<Board> solution = new java.util.ArrayDeque<Board>();
    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
        if (initial == null)
            throw (new java.lang.NullPointerException());
        solvable = isSolvableGet(initial.toString());
        if (!solvable)
        {
            moves = -1;
            solution = null;
            return;
        }
        else
        {
            SearchNode currentNode = new SearchNode(initial, null, 0);
            queue = new MinPQ<SearchNode>(10, currentNode.order());
            queue.insert(currentNode);
            while (!currentNode.board.isGoal())
            {
                currentNode = queue.delMin();
                if (currentNode.board.isGoal())
                {
                    break;
                }
                moves = currentNode.turn + 1;
                Iterable<Board> neighbors = currentNode.board.neighbors();
                for (Board item : neighbors)
                {
                    if (currentNode.previous != null)
                    {
                        if (!item.equals(currentNode.previous.board))
                            queue.insert(new SearchNode(item, currentNode, moves));
                    }
                    else
                        queue.insert(new SearchNode(item, currentNode, moves));
                }
            }
            // write path from start to goal into Iterable<Board> var
            while (currentNode != null)
            {
                solution.addFirst(currentNode.board);
                currentNode = currentNode.previous;
            }
            moves = solution.size() - 1;
        }
    }
    public boolean isSolvable()            // is the initial board solvable?
    {
        return solvable;
    }
    
    private boolean isSolvableGet(String damnRestrictions)
    { //they forced me to do this ;-(
        String[] facepalm = damnRestrictions.split("\\s+");
        int length = Integer.parseInt(facepalm[0]);
        int[] aux = new int[length * length];
        int auxIndex = 0;
        for (int index1 = 1; index1 < facepalm.length; index1++)
        {
            aux[auxIndex] = Integer.parseInt(facepalm[index1]);
        }
        int inversionCount = 0;
        int zeroIndex = -1;
        for (; auxIndex > aux.length / 2; auxIndex--)
        {
            for (int index = aux.length; index >= aux.length / 2; index--) // check right part for lesser values
            {
                if (aux[auxIndex] == 0)
                {
                    zeroIndex = index;
                    continue;
                }
                if (aux[index] != 0)
                {
                    if (aux[index] < aux[auxIndex])
                        inversionCount++;
                }
            }
        }
        //use key as max number of possible smaller keys, then substract   
        //number of smaller keys to the left of it = number of inversions
        for (; auxIndex >= 0; auxIndex--)
        {
            int temp = 0;
            for (int index = 0; index < auxIndex; index++)
            {
                temp = aux[auxIndex] - 1;
                if (aux[auxIndex] == 0)
                {
                    zeroIndex = index;
                    temp = 0;
                    continue;
                }
                if (aux[index] != 0)
                {
                    if (aux[index] < aux[auxIndex])
                        temp--;
                }
            }
            inversionCount += temp;
        } 
        
        if (length % 2 == 0)
        {
            if ((zeroIndex / length) % 2 == 0)
            {
                if (inversionCount % 2 == 0)
                    return true;
            }
            else
            {
                if (inversionCount % 2 != 0)
                    return true;
            }
        }
        else
        {
            if (inversionCount % 2 == 0)
                return true;
        }
        return false;
    }
    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
        return moves;
    }
    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
        return solution;
    }
    public static void main(String[] args) {

    // create initial board from file
    In in = new In(args[0]);
    int N = in.readInt();
    int[][] blocks = new int[N][N];
    for (int i = 0; i < N; i++)
        for (int j = 0; j < N; j++)
            blocks[i][j] = in.readInt();
    Board initial = new Board(blocks);

    // solve the puzzle
    Solver solver = new Solver(initial);

    // print solution to standard output
    if (!solver.isSolvable())
        StdOut.println("No solution possible");
    else {
        StdOut.println("Minimum number of moves = " + solver.moves());
        for (Board board : solver.solution())
            StdOut.println(board);
    }
}
}