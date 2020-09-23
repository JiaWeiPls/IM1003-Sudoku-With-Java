import java.awt.*; // Uses AWT's Layout Managers
import java.awt.event.*; // Uses AWT's Event Handlers
import javax.swing.*; // Uses Swing's Container/Components
import java.util.Random;

/**
 * The Sudoku game. To solve the number puzzle, each row, each column, and each
 * of the nine 3×3 sub-grids shall contain all of the digits from 1 to 9
 */
public class Sudoku3 extends JFrame {
	// Name-constants for the game properties
	public static final int GRID_SIZE = 9; // Size of the board
	public static final int SUBGRID_SIZE = 3; // Size of the sub-grid

	// Name-constants for UI control (sizes, colors and fonts)
	public static final int CELL_SIZE = 60; // Cell width/height in pixels
	public static final int CANVAS_WIDTH = CELL_SIZE * GRID_SIZE;
	public static final int CANVAS_HEIGHT = CELL_SIZE * GRID_SIZE;
	// Board width/height in pixels
	public static final Color OPEN_CELL_BGCOLOR = Color.YELLOW;
	public static final Color OPEN_CELL_TEXT_YES = new Color(0, 255, 0); // RGB
	public static final Color OPEN_CELL_TEXT_NO = Color.RED;
	public static final Color CLOSED_CELL_BGCOLOR = new Color(240, 240, 240); // RGB
	public static final Color CLOSED_CELL_TEXT = Color.BLACK;
	public static final Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);
	public static final Font FONT_STATUS = new Font("Monospaced", Font.BOLD, 15);
	// public static boolean win = false;

	// The game board composes of 9x9 JTextFields,
	// each containing String "1" to "9", or empty String
	private JTextField[][] tfCells = new JTextField[GRID_SIZE][GRID_SIZE];

	private Random rand = new Random();

	// Puzzle to be solved and the mask (which can be used to control the
	// difficulty level).
	// Hardcoded here. Extra credit for automatic puzzle generation
	// with various difficulty levels.
	private int[][] puzzle = { { 5, 3, 4, 6, 7, 8, 9, 1, 2 }, { 6, 7, 2, 1, 9, 5, 3, 4, 8 },
			{ 1, 9, 8, 3, 4, 2, 5, 6, 7 }, { 8, 5, 9, 7, 6, 1, 4, 2, 3 }, { 4, 2, 6, 8, 5, 3, 7, 9, 1 },
			{ 7, 1, 3, 9, 2, 4, 8, 5, 6 }, { 9, 6, 1, 5, 3, 7, 2, 8, 4 }, { 2, 8, 7, 4, 1, 9, 6, 3, 5 },
			{ 3, 4, 5, 2, 8, 6, 1, 7, 9 } };
	// For testing, open only 2 cells.
	private boolean[][] masks = { { false, false, false, false, false, true, false, false, false },
			{ false, false, false, false, false, false, false, false, true },
			{ false, false, false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false, false, false },
			{ false, false, false, false, false, false, false, false, false } };

	private boolean[][] colour = { { false, false, false, true, true, true, false, false, false },
			{ false, false, false, true, true, true, false, false, false },
			{ false, false, false, true, true, true, false, false, false },
			{ true, true, true, false, false, false, true, true, true },
			{ true, true, true, false, false, false, true, true, true },
			{ true, true, true, false, false, false, true, true, true },
			{ false, false, false, true, true, true, false, false, false },
			{ false, false, false, true, true, true, false, false, false },
			{ false, false, false, true, true, true, false, false, false } };
	private Time time = new Time(0, 0, 0);

	private int progress;

	private boolean[][] OriginalMasks = new boolean[9][9];
	private int[][] OriginalPuzzle = new int[9][9];

	/**
	 * Constructor to setup the game and the UI Components
	 */
	public Sudoku3(int difficulty) {

		SoundFX.init();
		SoundFX.volume = SoundFX.Volume.HIGH;
		SoundFX.BOOT.play();

		InitGame(difficulty);

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				OriginalMasks[i][j] = masks[i][j];
				OriginalPuzzle[i][j] = puzzle[i][j];
			}
		}

		SoundFX.init();
		SoundFX.volume = SoundFX.Volume.HIGH;
		SoundFX.INIT.play();

		// Container cp = getContentPane();
		// Panel cp = new Panel();
		Container cp = getContentPane();
		cp.setLayout(new BorderLayout(9, 10)); // 9x9 GridLayout
		cp.setPreferredSize(new Dimension(9 * CELL_SIZE + CELL_SIZE / 2, 10 * CELL_SIZE));

		// Allocate a common listener as the ActionEvent listener for all the
		// JTextFields
		// ... [TODO 3] (Later) ....
		// [TODO 3]
		InputListener listener = new InputListener();

		JLabel status = new JLabel("STATUS");
		status.setFont(FONT_STATUS);
		status.setHorizontalAlignment(SwingConstants.CENTER);
		status.setVerticalAlignment(SwingConstants.CENTER);

		new Thread() {
			int counter1 = 10;

			public void run() {
				while (counter1 >= 0) {
					int correctcounter = 0;
					for (int i = 0; i < GRID_SIZE; ++i) {
						for (int j = 0; j < GRID_SIZE; ++j) {
							if (masks[i][j] == false) {
								correctcounter++;
							}
						}
					}
					int blanks = 81 - correctcounter;

					status.setText("Time: " + (time.nextMilli()) + "    Progress: " + correctcounter + "/81"
							+ "    Blanks: " + blanks);
					try {
						Thread.sleep(9);
					} catch (Exception r) {
					}
				}
			}
		}.start();

		JProgressBar pb = new JProgressBar(SwingConstants.VERTICAL);
		pb.setMinimum(0);
		pb.setMaximum(100);
		pb.setStringPainted(true);
		pb.setForeground(Color.blue);
		pb.setPreferredSize(new Dimension(CELL_SIZE / 2, CANVAS_HEIGHT));

		new Thread() {
			int counter1 = 10;

			public void run() {
				while (counter1 >= 0) {
					int correctcounter = 0;
					for (int i = 0; i < GRID_SIZE; ++i) {
						for (int j = 0; j < GRID_SIZE; ++j) {
							if (masks[i][j] == false) {
								correctcounter++;
							}
						}
					}
					correctcounter = (int) ((double) correctcounter / 81.0 * 100);
					pb.setValue(correctcounter);
					try {
						Thread.sleep(9);
					} catch (Exception r) {
					}
				}
			}
		}.start();

		JPanel p1 = new JPanel();
		p1.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));
		p1.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));

		for (int row = 0; row < GRID_SIZE; ++row) {
			for (int col = 0; col < GRID_SIZE; ++col) {
				tfCells[row][col] = new JTextField(); // Allocate element of array
				p1.add(tfCells[row][col]);
				if (colour[row][col]) {
					tfCells[row][col].setBackground(new Color(132, 179, 255));
				} else {
					tfCells[row][col].setBackground(new Color(112, 155, 255));
				}
			}
		}

		// Construct 9x9 JTextFields and add to the content-pane
		for (int row = 0; row < GRID_SIZE; ++row) {
			for (int col = 0; col < GRID_SIZE; ++col) {
				// ContentPane adds JTextField
				if (masks[row][col]) {
					tfCells[row][col].setText(""); // set to empty string
					tfCells[row][col].setEditable(true);
					tfCells[row][col].setBackground(OPEN_CELL_BGCOLOR);

					// Add ActionEvent listener to process the input
					// ... [TODO 4] (Later) ...
					// [TODO 4]
					tfCells[row][col].addActionListener(listener); // For all editable rows and cols

				} else {
					tfCells[row][col].setText(puzzle[row][col] + "");
					tfCells[row][col].setEditable(false);
					// tfCells[row][col].setBackground(CLOSED_CELL_BGCOLOR);
					tfCells[row][col].setForeground(CLOSED_CELL_TEXT);
				}
				// Beautify all the cells
				tfCells[row][col].setHorizontalAlignment(JTextField.CENTER);
				tfCells[row][col].setFont(FONT_NUMBERS);
			}
		}

		// Set the size of the content-pane and pack all the components
		// under this container.
		cp.add(p1, "Center");
		cp.add(pb, "East");
		cp.add(status, "South");
		// cp.add(prog, "Center");
		pack();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Handle window closing
		setTitle("Sudoku");
		setVisible(true);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(CLOSED_CELL_BGCOLOR);
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);

		JMenu mnNewGame_1 = new JMenu("New Game");
		mnNewMenu.add(mnNewGame_1);

		JMenuItem mntmEasy = new JMenuItem("Easy");
		mntmEasy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResetDifficultyPuzzle(1);
				time.setTime(0, 0, 0);
				for (int row = 0; row < 9; row++) {
					for (int col = 0; col < 9; col++) {
						OriginalPuzzle[row][col] = puzzle[row][col];
						OriginalMasks[row][col] = masks[row][col];
					}
				}
			}
		});
		mnNewGame_1.add(mntmEasy);

		JMenuItem mntmMedium = new JMenuItem("Medium");
		mntmMedium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResetDifficultyPuzzle(2);
				time.setTime(0, 0, 0);
				for (int row = 0; row < 9; row++) {
					for (int col = 0; col < 9; col++) {
						OriginalPuzzle[row][col] = puzzle[row][col];
						OriginalMasks[row][col] = masks[row][col];
					}
				}

			}
		});
		mnNewGame_1.add(mntmMedium);

		JMenuItem mntmHard = new JMenuItem("Hard");
		mntmHard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResetDifficultyPuzzle(3);
				time.setTime(0, 0, 0);
				for (int row = 0; row < 9; row++) {
					for (int col = 0; col < 9; col++) {
						OriginalPuzzle[row][col] = puzzle[row][col];
						OriginalMasks[row][col] = masks[row][col];
					}
				}
			}
		});
		mnNewGame_1.add(mntmHard);

		JMenuItem mntmResetGame = new JMenuItem("Reset Game");
		mntmResetGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ResetPuzzle();

			}
		});
		mnNewMenu.add(mntmResetGame);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(JFrame.EXIT_ON_CLOSE);
			}
		});
		mnNewMenu.add(mntmExit);

		JMenu mnNewGame = new JMenu("Options");
		menuBar.add(mnNewGame);

		JCheckBoxMenuItem chckbxmntmSound = new JCheckBoxMenuItem("Sound");
		chckbxmntmSound.setSelected(true);
		chckbxmntmSound.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AbstractButton SoundButton = (AbstractButton) e.getSource();
				boolean selected = SoundButton.getModel().isSelected();
				if (SoundButton.isSelected()) {
					SoundFX.volume = SoundFX.Volume.HIGH;
					// CORRECT("correctans.wav"), // explosion
					// WRONG("wrongans.wav"), // gong
					// BOOT("Boot.wav"), // bullet
					/*
					 * INIT("Gameinit.wav"), WIN("winwait.wav"), BGM("BGM.wav"),
					 * SILENT("Silent.wav");
					 */
					SoundBGM.BGM.play();
				} else {
					SoundFX.volume = SoundFX.Volume.HIGH;
					SoundBGM.BGM.stop();
				}

			}
		});
		mnNewGame.add(chckbxmntmSound);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		setVisible(true);

		try {
			Thread.sleep(2000);
		} catch (Exception y) {
		}
		;

		SoundBGM.init();
		SoundBGM.volume = SoundBGM.Volume.LOW;
		SoundBGM.BGM.play();

	}

	public void InitGame(int difficulty) {

		// Board game = new Board(difficulty);
		Puzzle pz = new Puzzle();
		Masks mk = new Masks(difficulty);

		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				puzzle[i][j] = pz.getPuzzle()[i][j];
				masks[i][j] = mk.getMasks()[i][j];
			}
		}
	}

	public void ResetPuzzle() {
		for (int row = 0; row < GRID_SIZE; ++row) {
			for (int col = 0; col < GRID_SIZE; ++col) {
				if (masks[row][col] == false && colour[row][col] == false) {
					tfCells[row][col].setBackground(new Color(112, 155, 255));
				} else if (masks[row][col] == false && colour[row][col] == true) {
					tfCells[row][col].setBackground(new Color(132, 179, 255));
				}
			}
		}
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				puzzle[row][col] = OriginalPuzzle[row][col];
				masks[row][col] = OriginalMasks[row][col];
				if (masks[row][col]) {
					tfCells[row][col].setText(""); // set to empty string
					tfCells[row][col].setEditable(true);
					tfCells[row][col].setBackground(OPEN_CELL_BGCOLOR);

				} else {
					tfCells[row][col].setText(puzzle[row][col] + "");
					tfCells[row][col].setEditable(false);
					// tfCells[row][col].setBackground(CLOSED_CELL_BGCOLOR);
					tfCells[row][col].setForeground(CLOSED_CELL_TEXT);
				}
			}
		}
	}

	public void ResetDifficultyPuzzle(int difficulty) {
		InitGame(difficulty);
		for (int row = 0; row < GRID_SIZE; ++row) {
			for (int col = 0; col < GRID_SIZE; ++col) {
				if (masks[row][col] == false && colour[row][col] == false) {
					tfCells[row][col].setBackground(new Color(112, 155, 255));
				} else if (masks[row][col] == false && colour[row][col] == true) {
					tfCells[row][col].setBackground(new Color(132, 179, 255));
				}
			}
		}
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				if (masks[row][col]) {
					tfCells[row][col].setText(""); // set to empty string
					tfCells[row][col].setEditable(true);
					tfCells[row][col].setBackground(OPEN_CELL_BGCOLOR);

				} else {
					tfCells[row][col].setText(puzzle[row][col] + "");
					tfCells[row][col].setEditable(false);
					// tfCells[row][col].setBackground(CLOSED_CELL_BGCOLOR);
					tfCells[row][col].setForeground(CLOSED_CELL_TEXT);
				}
			}
		}
	}

	/*
	 * /** The entry main() entry method public static void main(String[] args) { //
	 * [TODO 1] (Now) // Check Swing program template on how to run the constructor
	 * new Sudoku(); }
	 */

	// Define the Listener Inner Class
	// ... [TODO 2] (Later) ...
	// [TODO 2]
	// Inner class to be used as ActionEvent listener for ALL JTextFields
	private class InputListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// All the 9*9 JTextFileds invoke this handler. We need to determine
			// which JTextField (which row and column) is the source for this invocation.
			int rowSelected = -1;
			int colSelected = -1;

			// Get the source object that fired the event
			JTextField source = (JTextField) e.getSource();
			// Scan JTextFileds for all rows and columns, and match with the source object
			boolean found = false;
			for (int row = 0; row < GRID_SIZE && !found; ++row) {
				for (int col = 0; col < GRID_SIZE && !found; ++col) {
					if (tfCells[row][col] == source) {
						rowSelected = row;
						colSelected = col;
						found = true; // break the inner/outer loops
					}
				}
			}

			/*
			 * [TODO 5] 1. Get the input String via
			 * tfCells[rowSelected][colSelected].getText() 2. Convert the String to int via
			 * Integer.parseInt(). 3. Assume that the solution is unique. Compare the input
			 * number with the number in the puzzle[rowSelected][colSelected]. If they are
			 * the same, set the background to green (Color.GREEN); otherwise, set to red
			 * (Color.RED).
			 */
			for (int row = 0; row < GRID_SIZE; ++row) {
				for (int col = 0; col < GRID_SIZE; ++col) {
					if (masks[row][col] == false && colour[row][col] == false) {
						tfCells[row][col].setBackground(new Color(112, 155, 255));
					} else if (masks[row][col] == false && colour[row][col] == true) {
						tfCells[row][col].setBackground(new Color(132, 179, 255));
					}
				}
			}

			CheckSudoku(rowSelected, colSelected);

			/*
			 * [TODO 6] Check if the player has solved the puzzle after this move. You could
			 * update the masks[][] on correct guess, and check the masks[][] if any input
			 * cell pending.
			 */
			int wincounter = 0;
			boolean win = false;
			for (int i = 0; i < GRID_SIZE; ++i) {
				for (int j = 0; j < GRID_SIZE; ++j) {
					if (masks[i][j] == false) {
						wincounter += 1;
					}
				}
			}
			if (wincounter == GRID_SIZE * GRID_SIZE) {
				win = true;
				SoundFX.init();
				SoundFX.volume = SoundFX.Volume.HIGH;
				SoundBGM.BGM.stop();
				SoundFX.WIN.play();

				JOptionPane.showMessageDialog(null, ("Congratulation!  Time taken: " + time));

			}
		}
	}

	public void CheckSudoku(int rowSelected, int colSelected) {
		int input = Integer.parseInt(tfCells[rowSelected][colSelected].getText());
		int subGridRow = rowSelected / 3;
		int subGridCol = colSelected / 3;

		if (puzzle[rowSelected][colSelected] == input) {
			masks[rowSelected][colSelected] = false;
			tfCells[rowSelected][colSelected].setBackground(OPEN_CELL_TEXT_YES);
			SoundFX.init();
			SoundFX.volume = SoundFX.Volume.HIGH;
			SoundFX.CORRECT.play();
		} else {
			// tfCells[rowSelected][colSelected].setBackground(OPEN_CELL_TEXT_NO);
			for (int row = 0; row < GRID_SIZE; ++row) {
				if (puzzle[row][colSelected] == input && masks[row][colSelected] == false) {
					tfCells[row][colSelected].setBackground(OPEN_CELL_TEXT_NO);
					SoundFX.init();
					SoundFX.volume = SoundFX.Volume.HIGH;
					SoundFX.WRONG.play();
				}
			}
			for (int col = 0; col < GRID_SIZE; ++col) {
				if (puzzle[rowSelected][col] == input && masks[rowSelected][col] == false) {
					tfCells[rowSelected][col].setBackground(OPEN_CELL_TEXT_NO);
					SoundFX.init();
					SoundFX.volume = SoundFX.Volume.HIGH;
					SoundFX.WRONG.play();
				}
			}
			for (int row = subGridRow * 3; row <= subGridRow * 3 + 2; ++row) {
				for (int col = subGridCol * 3; col <= subGridCol * 3 + 2; ++col) {
					if (puzzle[row][col] == input && masks[row][col] == false) {
						tfCells[row][col].setBackground(OPEN_CELL_TEXT_NO);
						SoundFX.init();
						SoundFX.volume = SoundFX.Volume.HIGH;
						SoundFX.WRONG.play();
					}
				}
			}

		}
	}

}