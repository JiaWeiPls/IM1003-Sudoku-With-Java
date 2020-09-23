
import java.util.Random;

public class Masks {

    private boolean[][] masks =
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

    public Masks(int difficulty) {   

        GenerateMasks(difficulty);
        if (difficulty == 1 ||difficulty == 2 ||difficulty == 3 || difficulty == 0) {
            int cycle = rand.nextInt(10) + 1;
            for (int i = 0; i <= cycle; i++) {
                RandomBoolMatrix(masks);
                BoolSubGridSwap(masks);            
            }
        }
    }    

    public boolean[][] getMasks() {
        return masks;
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
                System.out.println("Mode " + difficulty + ": Hard");
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
            case 4: 
                System.out.println();
                System.out.println("Mode " + difficulty + ": Insane");
                //Hard: 20 blanks
                masks[0][5] = masks[1][8] = masks[4][2] = masks[5][4] = masks[8][1] = true;
                masks[0][0] = masks[2][1] = masks[2][4] = masks[5][8] = masks[7][6] = true;
                masks[0][2] = masks[2][2] = masks[5][0] = masks[6][2] = masks[7][3] = true;
                masks[8][3] = masks[1][6] = masks[3][6] = masks[4][5] = masks[8][7] = true;
                masks[3][0] = masks[4][0] = masks[5][1] = masks[6][3] = masks[6][4] = true;
                masks[8][4] = masks[1][7] = masks[3][7] = masks[4][6] = masks[8][8] = true;
                /*
               {{true, false, true, false, false, true, false, false, false},
                {false, false, false, false, false, false, true, true, true},
                {true, true, true, false, true, false, false, false, false},
                {false, false, false, false, false, false, true, false, false},
                {true, false, true, false, false, true, true, true, false},
                {true, false, false, false, true, false, false, false, true},
                {false, false, true, true, true, false, false, false, false},
                {false, false, false, true, false, false, true, false, false},
                {false, true, false, true, true, false, false, true, true}};
                */
                break;
            default:
                System.out.println();
                System.out.println("Mode 0: Debug");
                //Hard: 20 blanks
                masks[0][0] = masks[0][1] = true;
                masks[1][0] = masks[1][1] = true;
                masks[7][5] = masks[7][7] = true;
                masks[8][4] = masks[8][8] = true;
                break;  
        }
    }
}