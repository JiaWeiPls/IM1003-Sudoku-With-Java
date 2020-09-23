import java.util.Random;

public class Board {

    protected int[][] puzzle;
    protected boolean[][] masks =
      {{false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false},
       {false, false, false, false, false, false, false, false, false}};
    private static final int GRID_SIZE = 9;
    public Random rand = new Random();

    public Board(int difficulty) {   

        //Generate Generic Puzzle[][]
        int[][] puzzle = new int[9][9];
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

        RandomIntMatrix(puzzle);

        //Generate Generic Masks[][]
        GenerateMasks(difficulty);

        RandomBoolMatrix(masks);
        BoolSubGridSwap(masks);
    }    

    public Board() {   

        //Generate Generic Puzzle[][]
        int[][] puzzle = new int[9][9];
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

        RandomIntMatrix(puzzle);

        //Generate Generic Masks[][]
        GenerateMasks(1);

        RandomBoolMatrix(masks);
        BoolSubGridSwap(masks);
    }

    public int[][] getPuzzle() {
        return puzzle;
    }

    public boolean[][] getMasks() {
        return masks;
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

    public void PrintBoolMatrix(boolean[][] matrix) {
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

    public void RandomBoolMatrix(boolean[][] matrix) {
        System.out.println();
        System.out.println("Original Boolean Matrix");
        System.out.println();
        PrintBoolMatrix(matrix);

        //Swap Rows
        int randomRow = rand.nextInt(3);
        int rowSwap = (randomRow + 2) % 3;

        for (int row = 0; row < GRID_SIZE; ++row) {
            for (int col = 0; col < GRID_SIZE; ++col) {
                if (row == randomRow) {
                    boolean temp = matrix[row][col];
                    matrix[row][col] = matrix[rowSwap][col];
                    matrix[rowSwap][col] = temp;    
                } else if (row == randomRow + 3) {
                    boolean temp = matrix[row][col];
                    matrix[row][col] = matrix[rowSwap + 3][col];
                    matrix[rowSwap + 3][col] = temp;
                } else if (row == randomRow + 6) {
                    boolean temp = matrix[row][col];
                    matrix[row][col] = matrix[rowSwap + 6][col];
                    matrix[rowSwap + 6][col] = temp;   
                } else {
                }
            }
        }
             
        System.out.println();
        System.out.println("Swapped Row with " + randomRow);
        System.out.println();
        PrintBoolMatrix(matrix);                

        //Swap Columns
        int randomCol = rand.nextInt(3);
        while (randomCol == randomRow) {
            randomCol = rand.nextInt(3);
        }
        int colSwap = (randomCol + 2) % 3;

        for (int row = 0; row < GRID_SIZE; ++row) {
            for (int col = 0; col < GRID_SIZE; ++col) {
                if (col == randomCol) {
                    boolean temp = matrix[row][col];
                    matrix[row][col] = matrix[row][colSwap];
                    matrix[row][colSwap] = temp;
                } else if (col == randomCol + 3) {
                    boolean temp = matrix[row][col];
                    matrix[row][col] = matrix[row][colSwap + 3];
                    matrix[row][colSwap + 3] = temp;
                } else if (col == randomCol + 6) {
                    boolean temp = matrix[row][col];
                    matrix[row][col] = matrix[row][colSwap + 6];
                    matrix[row][colSwap + 6] = temp;
                } else {             
                }
            }
        }
             
        System.out.println();
        System.out.println("Swapped Col with " + randomCol);
        System.out.println();
        PrintBoolMatrix(matrix);
    }

    public void BoolSubGridSwap(boolean[][] a) {
        boolean rtemp;
        int r1 = rand.nextInt(3)*3;
        int r2 = (r1 + 6)%9;
        for(int i1 = r1, i2 = r2; i1 <= r1 + 2 && i2 <= r2 + 2; i1++, i2++) {
            for(int j=0;j<9;j++) {
                rtemp=a[i1][j];
                a[i1][j]=a[i2][j];
                a[i2][j]=rtemp;
            }
        }   

        System.out.println();
        System.out.println("Swapped SubRow " + r1 + " with " + r2);
        System.out.println();
        PrintBoolMatrix(a);           

        boolean ctemp;
        int c1 = rand.nextInt(3)*3;
        while (c1 == r1) {
            c1 = rand.nextInt(3)*3;
        }
        int c2 = (c1 + 6)%9;
        for(int j1 = c1, j2 = c2; j1 <= c1 + 2 && j2 <= c2 + 2; j1++, j2++) {
            for(int i=0;i<9;i++) {
                ctemp=a[i][j1];
                a[i][j1]=a[i][j2];
                a[i][j2]=ctemp;
            }
        }

        System.out.println();
        System.out.println("Swapped SubCol " + c1 + " with " + c2);
        System.out.println();
        PrintBoolMatrix(a);       
    }  

    public void GenerateMasks(int difficulty) {
        final int EASY = 5;
        final int MEDIUM = 10;
        final int HARD = 20;

        switch (difficulty) {
            case 1: 
                System.out.println();
                System.out.println("Mode " + difficulty + ": Easy");
                //Easy: 5 blanks
                masks[0][5] = masks[1][8] = masks[4][2] = masks[5][4] = masks[8][1] = true;
                /*
               {{false, false, false, false, false, true, false, false, false},
                {false, false, false, false, false, false, false, false, true},
                {false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false},
                {false, false, true, false, false, false, false, false, false},
                {false, false, false, false, true, false, false, false, false},
                {false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, false, false, false},
                {false, true, false, false, false, false, false, false, false}};
                */
                break;            
            case 2: 
                System.out.println();
                System.out.println("Mode " + difficulty + ": Medium");
                //Easy: 10 blanks
                masks[0][5] = masks[1][8] = masks[4][2] = masks[5][4] = masks[8][1] = true;
                masks[0][0] = masks[2][1] = masks[2][4] = masks[5][8] = masks[7][6] = true;
                /*
               {{true, false, false, false, false, true, false, false, false},
                {false, false, false, false, false, false, false, false, true},
                {false, true, false, false, true, false, false, false, false},
                {false, false, false, false, false, false, false, false, false},
                {false, false, true, false, false, false, false, false, false},
                {false, false, false, false, true, false, false, false, true},
                {false, false, false, false, false, false, false, false, false},
                {false, false, false, false, false, false, true, false, false},
                {false, true, false, false, false, false, false, false, false}};
                */
                break;            
            case 3: 
                System.out.println();
                System.out.println("Mode " + difficulty + ": Easy");
                //Hard: 20 blanks
                masks[0][5] = masks[1][8] = masks[4][2] = masks[5][4] = masks[8][1] = true;
                masks[0][0] = masks[2][1] = masks[2][4] = masks[5][8] = masks[7][6] = true;
                masks[0][2] = masks[2][2] = masks[5][0] = masks[6][2] = masks[7][3] = true;
                masks[8][3] = masks[1][6] = masks[3][6] = masks[4][5] = masks[8][7] = true;
                /*
               {{true, false, true, false, false, true, false, false, false},
                {false, false, false, false, false, false, true, false, true},
                {false, true, true, false, true, false, false, false, false},
                {false, false, false, false, false, false, true, false, false},
                {false, false, true, false, false, true, false, false, false},
                {true, false, false, false, true, false, false, false, true},
                {false, false, true, false, false, false, false, false, false},
                {false, false, false, true, false, false, true, false, false},
                {false, true, false, true, false, false, false, true, false}};
                */
                break;
            default:
                System.out.println();
                System.out.println("Error");   
        }
    }
}