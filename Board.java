import edu.princeton.cs.algs4.StdRandom;

public class Board
{
    private int[] board;
    private int[] blank;
    private int length;
    private int hammingmemo = 0;
    private int manhattanmemo = 0;
    private java.util.ArrayList<Board> neighborhood = new java.util.ArrayList<Board>();
    
    public Board(int[][] blocks)           // construct a board from an N-by-N array of blocks
    {                                      // (where blocks[i][j] = block in row i, column j)
        if (blocks == null)
            throw (new java.lang.NullPointerException());
        board = new int[blocks.length*blocks.length];
        length = blocks.length;
        int lineIndex = 0;
        for (int index1 = 0; index1 < blocks.length; index1++)
        {
            for (int index2 = 0; index2 < blocks.length; index2++)
            {
                board[lineIndex++] = blocks[index1][index2];
                if (blocks[index1][index2] == 0)
                    blank = new int[]{index1, index2};
                else
                {
                    if (board[lineIndex - 1] != (lineIndex))
                        hammingmemo++; //count hamming score
                // count manhattan score
                    int xindex = (board[lineIndex - 1] - 1)/length;
                    int yindex = board[lineIndex - 1] - 1 - xindex*length;
                    int xgoal = (lineIndex - 1) / length;
                    int ygoal = lineIndex - 1 - xgoal*length;
                    manhattanmemo += Math.abs(xgoal - xindex); //add vertical distance to goal
                    manhattanmemo += Math.abs(ygoal - yindex); //add horizontal distance to goal
                }
            }   
        }
    }
    public int dimension()                 // board dimension N
    {
        return length;
    }
    public int hamming()                   // number of blocks out of place
    {
        return hammingmemo;
    }
    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        return manhattanmemo;
    }
    
    public boolean isGoal()                // is this board the goal board?
    {
        return (this.hamming() == 0);
    }
    public Board twin()                    // a board that is obtained by exchanging any pair of blocks
    {
        int[][] temp = new int[length][length];
        for (int ind1 = 0; ind1 < length; ind1++)
        {
            for (int ind2 = 0; ind2 < length; ind2++)
            {
                temp[ind1][ind2] = board[ind1 * length + ind2];
            }
        }
        int rnd1 = StdRandom.uniform(0, temp.length);
        while (temp[rnd1/length][rnd1 - ((rnd1/length)*length)] == 0)
            rnd1 = StdRandom.uniform(0, temp.length);
        int rnd2 = (rnd1 < board.length) ? rnd1 + 1 : rnd1 - 1;
        while (temp[rnd2/length][rnd2 - ((rnd2/length)*length)] == 0)
            rnd2 += (rnd2 < rnd1) ? -1 : 1;
        int buffer = temp[rnd2/length][rnd2 - ((rnd2/length)*length)];
        temp[rnd2/length][rnd2 - ((rnd2/length)*length)] = temp[rnd1/length][rnd1 - ((rnd1/length)*length)];
        temp[rnd1/length][rnd1 - ((rnd1/length)*length)] = buffer;
        return new Board(temp);
    }
    public boolean equals(Object y)        // does this board equal y?
    {
        if (y == null)
            return false;
        return this.toString().equals(y.toString());
    }
    private enum Directions { up, down, left, right }
    public Iterable<Board> neighbors()     // all neighboring boards
    {
        if (neighborhood.isEmpty())
        { //swap returns int[][] copy of board[][] with blank shifted accordingly
            if (blank[0] - 1 >= 0)
                neighborhood.add(new Board(swap(Directions.up)));
            if (blank[0] + 1 < length)    
                neighborhood.add(new Board(swap(Directions.down))); 
            if (blank[1] - 1 >= 0)
                neighborhood.add(new Board(swap(Directions.left)));
            if (blank[1] + 1 < length)
                neighborhood.add(new Board(swap(Directions.right)));
        }
        return neighborhood;
    }
    private int[][] swap(Directions direction) 
    {
        int[][] result = new int[length][length];
        for (int index1 = 0; index1 < length; index1++)
        {
            for (int index2 = 0; index2 < length; index2++)
            {
                result[index1][index2] = board[index1 * length + index2];
            }
        }
        switch (direction)
        {
            case up:
                result[blank[0]][blank[1]] = result[blank[0] - 1][blank[1]];
                result[blank[0] - 1][blank[1]] = 0;
                break;
            case down:
                result[blank[0]][blank[1]] = result[blank[0] + 1][blank[1]];
                result[blank[0] + 1][blank[1]] = 0;
                break;
            case left:
                result[blank[0]][blank[1]] = result[blank[0]][blank[1] - 1];
                result[blank[0]][blank[1] - 1] = 0;
                break;
            case right:
                result[blank[0]][blank[1]] = result[blank[0]][blank[1] + 1];
                result[blank[0]][blank[1] + 1] = 0;
                break;
            default:
                break;
        }
        return result;
    }
    public String toString()               // string representation of this board (in the output format specified below)
    {
        StringBuffer result = new StringBuffer();
        result.append(this.dimension() + "\n");
        for (int index1 = 0; index1 < length; index1++)
        {
            for (int index2 = 0; index2 < length; index2++)
            {
                if (board[index1 * length + index2] < 10)
                    result.append(" ");
                result.append(board[index1 * length + index2] + " ");
            }
            result.append("\n");
        }
        return result.toString();
    }

    public static void main(String[] args) // unit tests (not graded)
    { }
}