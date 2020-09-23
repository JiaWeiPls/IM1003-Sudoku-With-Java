import java.util.Random;

public class Puzzle {

    private static int[][] puzzle;

    private static final int GRID_SIZE = 9;
    public Random rand = new Random();

    public Puzzle() {   

        //Generate Generic Puzzle[][]
        puzzle = new int[GRID_SIZE][GRID_SIZE];
        int k=1, n=1;
        for(int i=0;i<9;i++) {
            k=n;
            for(int j=0;j<9;j++) {
                if(k<=9) {
                    puzzle[i][j]=k;
                    k++;
                } else {
                    k=1;
                    puzzle[i][j]=k;
                    k++;
                }
            }
            n=k+3;
            if(k==10)
                n=4;
            if(n>9)
                n=(n%9)+1;
        }

        int cycle = rand.nextInt(10) + 1;
        for (int i = 0; i <= cycle; i++) {
            RandomIntMatrix(puzzle);
        }

    }    

    public int[][] getPuzzle() {
        return puzzle;
    }


    public void PrintIntMatrix(int[][] matrix) {
        System.out.println();
        for (int row = 0; row < GRID_SIZE; ++row) {
            for (int col = 0; col < GRID_SIZE; ++col) {
                System.out.print(matrix[row][col] + "  ");
            }
        System.out.println();
        }
    }    


    public void RandomIntMatrix(int[][] matrix) {
        System.out.println();
        System.out.println("Original Matrix");
        PrintIntMatrix(matrix);

        //Set new matrix
        int randomNum = rand.nextInt(9) + 1;
        for (int row = 0; row < GRID_SIZE; ++row) {
            for (int col = 0; col < GRID_SIZE; ++col) {
                matrix[row][col] = ((matrix[row][col] + randomNum) % 9) + 1;
            }
        }

        System.out.println();
        System.out.println("Randomised Number with " + randomNum);
        System.out.println();
        PrintIntMatrix(matrix);

        //Swap Rows
        int randomRow = rand.nextInt(3);
        int rowSwap = (randomRow + 2) % 3;


        for (int row = 0; row < GRID_SIZE; ++row) {
            for (int col = 0; col < GRID_SIZE; ++col) {
                if (row == randomRow) {
                    int temp = matrix[row][col];
                    matrix[row][col] = matrix[rowSwap][col];
                    matrix[rowSwap][col] = temp;
                } else if (row == randomRow + 3) {
                    int temp = matrix[row][col];
                    matrix[row][col] = matrix[rowSwap + 3][col];
                    matrix[rowSwap + 3][col] = temp;
                } else if (row == randomRow + 6) {
                    int temp = matrix[row][col];
                    matrix[row][col] = matrix[rowSwap + 6][col];
                    matrix[rowSwap + 6][col] = temp;
                } else {
                }
            }
        }
             
        System.out.println();
        System.out.println("Swapped Row with " + randomRow);
        System.out.println();
        PrintIntMatrix(matrix);          

        //Swap Columns
        int randomCol = rand.nextInt(3);
        while (randomCol == randomRow) {
            randomCol = rand.nextInt(3);
        }
        int colSwap = (randomCol + 2) % 3;
        for (int row = 0; row < GRID_SIZE; ++row) {
            for (int col = 0; col < GRID_SIZE; ++col) {
                if (col == randomCol) {
                    int temp = matrix[row][col];
                    matrix[row][col] = matrix[row][colSwap];
                    matrix[row][colSwap] = temp;    
                } else if (col == randomCol + 3) {
                    int temp = matrix[row][col];
                    matrix[row][col] = matrix[row][colSwap + 3];
                    matrix[row][colSwap + 3] = temp;
                } else if (col == randomCol + 6) {
                    int temp = matrix[row][col];
                    matrix[row][col] = matrix[row][colSwap + 6];
                    matrix[row][colSwap + 6] = temp;   
                } else {
                }
            }
        }
             
        System.out.println();
        System.out.println("Swapped Col with " + randomCol);
        System.out.println();
        PrintIntMatrix(matrix);
    }  
}