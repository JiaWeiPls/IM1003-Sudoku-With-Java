import java.awt.*; // Uses AWT's Layout Managers

import java.awt.event.*; // Uses AWT's Event Handlers
import javax.swing.*; // Uses Swing's Container/Components
import java.util.Random;

/**
 * The Sudoku game. To solve the number puzzle, each row, each column, and each
 * of the nine 3×3 sub-grids shall contain all of the digits from 1 to 9
 */
public class Sudoku extends JFrame {
	// Name-constants for the game properties
	public static final int GRID_SIZE = 9; // Size of the board
	public static final int SUBGRID_SIZE = 3; // Size of the sub-grid

	// Name-constants for UI control (sizes, colors and fonts)
	public static final int CELL_SIZE = 60; // Cell width/height in pixels
	public static final int CANVAS_WIDTH = CELL_SIZE * GRID_SIZE;
	public static final int CANVAS_HEIGHT = CELL_SIZE * GRID_SIZE;
	// Board width/height in pixels
	public static Color OPEN_CELL_BGCOLOR = Color.YELLOW;
	public static Color OPEN_CELL_TEXT_YES = new Color(0, 255, 0); // RGB
	public static Color OPEN_CELL_TEXT_NO = Color.RED;
	public static Color CLOSED_CELL_BGCOLOR = new Color(240, 240, 240); // RGB
	public static Color CLOSED_CELL_TEXT = Color.BLACK;
	public static Font FONT_NUMBERS = new Font("Monospaced", Font.BOLD, 20);
	public static Font FONT_STATUS = new Font("Monospaced", Font.BOLD, 15);
	// public static boolean win = false;

	// The game board composes of 9x9 JTextFields,
	// each containing String "1" to "9", or empty String
	public static Color color1b = new Color(132, 179, 255);
	public static Color color2b = new Color(51, 255, 255);
	public static Color color1p = new Color(102, 204, 0);
	public static Color color2p = new Color(204, 255, 153);
	public static Color color1pl = new Color(160, 160, 160);
	public static Color color2pl = new Color(224, 224, 224);
	public static Color color1pp = new Color(255, 0, 127);
	public static Color color2pp = new Color(255, 102, 255);
	public static Color theme1 = color1p;
	public static Color theme2 = color2p;

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

	private int progress, health;

	private boolean[][] OriginalMasks = new boolean[9][9];
	private int[][] OriginalPuzzle = new int[9][9];

	/**
	 * Constructor to setup the game and the UI Components
	 */
	public Sudoku(int difficulty) {

		health = 10;

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
		cp.setPreferredSize(new Dimension(9 * CELL_SIZE + CELL_SIZE, 10 * CELL_SIZE));

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
		pb.setForeground(theme1);
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

		JProgressBar hp = new JProgressBar(SwingConstants.VERTICAL);
		hp.setMinimum(0);
		hp.setMaximum(100);
		hp.setStringPainted(true);
		hp.setForeground(OPEN_CELL_TEXT_NO);
		hp.setPreferredSize(new Dimension(CELL_SIZE / 2, CANVAS_HEIGHT));

		new Thread() {
			int counter1 = 1;

			public void run() {
				while (counter1 >= 0) {

					int healthperc = (int) ((double) health / 10 * 100);
					hp.setValue(healthperc);
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
					tfCells[row][col].setBackground(theme2);
				} else {
					tfCells[row][col].setBackground(theme1);
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
		cp.add(hp, "West");
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

		JMenuItem mntmMainMenu = new JMenuItem("Main Menu");
		mntmMainMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = JOptionPane.showConfirmDialog(null, "Go back to Main Menu?");
				if (a == JOptionPane.YES_OPTION) {
					new MainMenu();
					dispose();
					SoundBGM.BGM.stop();

				}
			}
		});
		mnNewMenu.add(mntmMainMenu);

		JMenu mnNewGame_1 = new JMenu("New Game");
		mnNewMenu.add(mnNewGame_1);

		JMenuItem mntmEasy = new JMenuItem("Easy");
		mntmEasy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int a = JOptionPane.showConfirmDialog(null, "Open new game?");
				if (a == JOptionPane.YES_OPTION) {
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					health = 10;

					ResetDifficultyPuzzle(1);
					time.setTime(0, 0, 0);
					for (int row = 0; row < 9; row++) {
						for (int col = 0; col < 9; col++) {
							OriginalPuzzle[row][col] = puzzle[row][col];
							OriginalMasks[row][col] = masks[row][col];
						}
					}
				}
			}
		});
		mnNewGame_1.add(mntmEasy);

		JMenuItem mntmMedium = new JMenuItem("Medium");
		mntmMedium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int a = JOptionPane.showConfirmDialog(null, "Open new game?");
				if (a == JOptionPane.YES_OPTION) {
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					health = 10;

					ResetDifficultyPuzzle(2);
					time.setTime(0, 0, 0);
					for (int row = 0; row < 9; row++) {
						for (int col = 0; col < 9; col++) {
							OriginalPuzzle[row][col] = puzzle[row][col];
							OriginalMasks[row][col] = masks[row][col];
						}
					}
				}
			}
		});
		mnNewGame_1.add(mntmMedium);

		JMenuItem mntmHard = new JMenuItem("Hard");
		mntmHard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int a = JOptionPane.showConfirmDialog(null, "Open new game?");
				if (a == JOptionPane.YES_OPTION) {
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					health = 10;

					ResetDifficultyPuzzle(3);
					time.setTime(0, 0, 0);
					for (int row = 0; row < 9; row++) {
						for (int col = 0; col < 9; col++) {
							OriginalPuzzle[row][col] = puzzle[row][col];
							OriginalMasks[row][col] = masks[row][col];
						}
					}
				}
			}
		});
		mnNewGame_1.add(mntmHard);

		JMenuItem mntmInsane = new JMenuItem("Insane");
		mntmInsane.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int a = JOptionPane.showConfirmDialog(null, "Open new game?");
				if (a == JOptionPane.YES_OPTION) {
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					health = 10;

					ResetDifficultyPuzzle(4);
					time.setTime(0, 0, 0);
					for (int row = 0; row < 9; row++) {
						for (int col = 0; col < 9; col++) {
							OriginalPuzzle[row][col] = puzzle[row][col];
							OriginalMasks[row][col] = masks[row][col];
						}
					}
				}
			}
		});
		mnNewGame_1.add(mntmInsane);

		JMenuItem mntmResetGame = new JMenuItem("Reset Game");
		mntmResetGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = JOptionPane.showConfirmDialog(null, "Reset game?");
				if (a == JOptionPane.YES_OPTION) {
					ResetPuzzle();
				}
			}
		});
		mnNewMenu.add(mntmResetGame);

		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = JOptionPane.showConfirmDialog(null, "Exit game?");
				if (a == JOptionPane.YES_OPTION) {
					System.exit(JFrame.EXIT_ON_CLOSE);
				}
			}
		});
		mnNewMenu.add(mntmExit);

		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);

		JMenu Theme = new JMenu("Theme");

		JMenuItem Default = new JMenuItem("Default");
		Default.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				theme1 = color1p;
				theme2 = color2p;
				pb.setForeground(theme1);
				// ResetPuzzle();
				for (int row = 0; row < GRID_SIZE; ++row) {
					for (int col = 0; col < GRID_SIZE; ++col) {
						if (masks[row][col] == false && colour[row][col] == false) {
							tfCells[row][col].setBackground(theme1);
						} else if (masks[row][col] == false && colour[row][col] == true) {
							tfCells[row][col].setBackground(theme2);
						}
					}
				}
				// time.setTime(0, 0, 0);
				/*
				 * for (int row = 0; row < 9; row++) { for (int col = 0; col < 9; col++) {
				 * OriginalPuzzle[row][col] = puzzle[row][col]; OriginalMasks[row][col] =
				 * masks[row][col]; } }
				 */

			}
		});
		Theme.add(Default);

		JMenuItem Blue = new JMenuItem("Ocean");
		Blue.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				theme1 = color1b;
				theme2 = color2b;
				pb.setForeground(theme1);
				// ResetPuzzle();
				for (int row = 0; row < GRID_SIZE; ++row) {
					for (int col = 0; col < GRID_SIZE; ++col) {
						if (masks[row][col] == false && colour[row][col] == false) {
							tfCells[row][col].setBackground(theme1);
						} else if (masks[row][col] == false && colour[row][col] == true) {
							tfCells[row][col].setBackground(theme2);
						}
					}
				}
				// time.setTime(0, 0, 0);
				/*
				 * for (int row = 0; row < 9; row++) { for (int col = 0; col < 9; col++) {
				 * OriginalPuzzle[row][col] = puzzle[row][col]; OriginalMasks[row][col] =
				 * masks[row][col]; } }
				 */
			}

		});
		Theme.add(Blue);

		JMenuItem Plain = new JMenuItem("Newspaper");
		Plain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				theme1 = color1pl;
				theme2 = color2pl;
				pb.setForeground(theme1);
				// ResetPuzzle();
				for (int row = 0; row < GRID_SIZE; ++row) {
					for (int col = 0; col < GRID_SIZE; ++col) {
						if (masks[row][col] == false && colour[row][col] == false) {
							tfCells[row][col].setBackground(theme1);
						} else if (masks[row][col] == false && colour[row][col] == true) {
							tfCells[row][col].setBackground(theme2);
						}
					}
				}
				// time.setTime(0, 0, 0);
				/*
				 * for (int row = 0; row < 9; row++) { for (int col = 0; col < 9; col++) {
				 * OriginalPuzzle[row][col] = puzzle[row][col]; OriginalMasks[row][col] =
				 * masks[row][col]; } }
				 */

			}
		});
		Theme.add(Plain);

		JMenuItem Pink = new JMenuItem("Sakura");
		Pink.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				theme1 = color1pp;
				theme2 = color2pp;
				pb.setForeground(theme1);
				// ResetPuzzle();
				for (int row = 0; row < GRID_SIZE; ++row) {
					for (int col = 0; col < GRID_SIZE; ++col) {
						if (masks[row][col] == false && colour[row][col] == false) {
							tfCells[row][col].setBackground(theme1);
						} else if (masks[row][col] == false && colour[row][col] == true) {
							tfCells[row][col].setBackground(theme2);
						}
					}
				}
				// time.setTime(0, 0, 0);
				/*
				 * for (int row = 0; row < 9; row++) { for (int col = 0; col < 9; col++) {
				 * OriginalPuzzle[row][col] = puzzle[row][col]; OriginalMasks[row][col] =
				 * masks[row][col]; } }
				 */

			}
		});
		Theme.add(Pink);

		mnOptions.add(Theme);

		JCheckBoxMenuItem chckbxmntmMusic = new JCheckBoxMenuItem("Music");
		chckbxmntmMusic.setSelected(true);
		chckbxmntmMusic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AbstractButton MusicButton = (AbstractButton) e.getSource();
				boolean selected = MusicButton.getModel().isSelected();
				if (MusicButton.isSelected()) {

					// CORRECT("correctans.wav"), // explosion
					// WRONG("wrongans.wav"), // gong
					// BOOT("Boot.wav"), // bullet
					/*
					 * INIT("Gameinit.wav"), WIN("winwait.wav"), BGM("BGM.wav"),
					 * SILENT("Silent.wav");
					 */
					SoundBGM.BGM.play();
				} else {

					SoundBGM.BGM.stop();
				}

			}
		});
		mnOptions.add(chckbxmntmMusic);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		JMenuItem rules = new JMenuItem("Game Rules");

		rules.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame f = new JFrame();
				JOptionPane.showMessageDialog(f, "The Basic Rules of Sudoku:\r\n" + "\r\n"
						+ "· There is only one valid solution to each Sudoku puzzle. The only way the puzzle can be considered solved correctly is when all 81 boxes contain numbers and the other Sudoku rules have been followed.\r\n"
						+ "\r\n"
						+ "· When you start a game of Sudoku, some blocks will be pre-filled for you. You cannot change these numbers in the course of the game.\r\n"
						+ "\r\n"
						+ "· Each column must contain all of the numbers 1 through 9 and no two numbers in the same column of a Sudoku puzzle can be the same.\r\n"
						+ "\r\n"
						+ "· Each row must contain all of the numbers 1 through 9 and no two numbers in the same row of a Sudoku puzzle can be the same.\r\n"
						+ "\r\n"
						+ "· Each block must contain all of the numbers 1 through 9 and no two numbers in the same block of a Sudoku puzzle can be the same.");

			}
		});
		mnHelp.add(rules);
		setVisible(true);

		JMenuItem instruction = new JMenuItem("Instructions");

		instruction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame f = new JFrame();
				JOptionPane.showMessageDialog(f, "Instructions:\r\n" + "\r\n"
						+ "· Key a number in the yellow boxes. Only numbers from 1 to 9 is allowed.\r\n" + "\r\n"
						+ "· Press \"Enter\" to key your answer in. You have to press \"Enter\" once for each yellow box that is filled. \r\n"
						+ "\r\n" + "· The progress bar will increase with every correct answer entered. \r\n" + "\r\n"
						+ "· Your life bar will deplete with each incorrect answer entered. So be careful! \r\n"
						+ "\r\n"
						+ "· The game will end when the progress bar is full or your life bar runs out. Have Fun!");

			}
		});
		JMenuItem mntmReveal = new JMenuItem("Reveal");
		mntmReveal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame RevFrame = new JFrame();
				int input = JOptionPane.showConfirmDialog(RevFrame, "Do you want to reveal all?", "Reveal",
						JOptionPane.YES_NO_OPTION);

				if (input == JOptionPane.YES_OPTION) {
					setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 0=yes, 1=no, 2=cancel
					RevealPuzzle();

				}
			}
		});
		mnHelp.add(mntmReveal);
		mnHelp.add(instruction);
		setVisible(true);

		JMenuItem tips = new JMenuItem("Tips");

		tips.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame f = new JFrame();
				JOptionPane.showMessageDialog(f, "Some tips to help you:\r\n" + "\r\n"
						+ "1) Key in ALL the answers in the yellowboxes first.\r\n" + "\r\n"
						+ "2) Press \"Enter\" for each yellow box once you have checked all the values in the yellow boxes. \r\n"
						+ "\r\n" + "3) This should help keep your health bar high and help you win the game! ");

			}
		});
		mnHelp.add(tips);
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

	public void RevealPuzzle() {
		for (int row = 0; row < GRID_SIZE; ++row) {
			for (int col = 0; col < GRID_SIZE; ++col) {
				if (masks[row][col] == false && colour[row][col] == false) {
					tfCells[row][col].setBackground(theme1);
				} else if (masks[row][col] == false && colour[row][col] == true) {
					tfCells[row][col].setBackground(theme2);
				}
			}
		}
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				puzzle[row][col] = OriginalPuzzle[row][col];
				masks[row][col] = OriginalMasks[row][col];
				if (masks[row][col]) {
					tfCells[row][col].setText(puzzle[row][col] + "");
					tfCells[row][col].setEditable(false);
					// tfCells[row][col].setBackground(CLOSED_CELL_BGCOLOR);
					tfCells[row][col].setForeground(CLOSED_CELL_TEXT);

				} else {
					tfCells[row][col].setText(puzzle[row][col] + "");
					tfCells[row][col].setEditable(false);
					// tfCells[row][col].setBackground(CLOSED_CELL_BGCOLOR);
					tfCells[row][col].setForeground(CLOSED_CELL_TEXT);
				}
			}
		}
		SoundFX.GAMEOVER.play();
		SoundBGM.BGM.stop();
		JFrame OvFrame = new JFrame();
		Object[] options = { "Main Menu", "Exit" };
		int input2 = JOptionPane.showOptionDialog(OvFrame, "Game Over, you gave up", "Game Over",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

		if (input2 == JOptionPane.YES_OPTION) {
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			dispose();
			SoundFX.GAMEOVER.stop();
			new MainMenu();
		} else {
			System.exit(JFrame.EXIT_ON_CLOSE);
		}

	}

	public void ResetPuzzle() {
		for (int row = 0; row < GRID_SIZE; ++row) {
			for (int col = 0; col < GRID_SIZE; ++col) {
				if (masks[row][col] == false && colour[row][col] == false) {
					tfCells[row][col].setBackground(theme1);
				} else if (masks[row][col] == false && colour[row][col] == true) {
					tfCells[row][col].setBackground(theme2);
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
		InputListener listener = new InputListener();
		ResetPuzzle();
		for (int row = 0; row < GRID_SIZE; ++row) {
			for (int col = 0; col < GRID_SIZE; ++col) {
				masks[row][col] = false;
				// ContentPane adds JTextField
				if (!masks[row][col]) {
					tfCells[row][col].setText(puzzle[row][col] + "");
					tfCells[row][col].setEditable(false);
					// tfCells[row][col].setBackground(CLOSED_CELL_BGCOLOR);
					tfCells[row][col].setForeground(CLOSED_CELL_TEXT);

				}

			}
		}

		InitGame(difficulty);
		for (int row = 0; row < GRID_SIZE; ++row) {
			for (int col = 0; col < GRID_SIZE; ++col) {
				if (masks[row][col] == false && colour[row][col] == false) {
					tfCells[row][col].setBackground(theme1);
				} else if (masks[row][col] == false && colour[row][col] == true) {
					tfCells[row][col].setBackground(theme2);
				}
			}
		}
		for (int row = 0; row < 9; row++) {
			for (int col = 0; col < 9; col++) {
				if (masks[row][col]) {
					tfCells[row][col].setText(""); // set to empty string
					tfCells[row][col].setEditable(true);
					tfCells[row][col].setBackground(OPEN_CELL_BGCOLOR);
					tfCells[row][col].addActionListener(listener);

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
						tfCells[row][col].setBackground(theme1);
					} else if (masks[row][col] == false && colour[row][col] == true) {
						tfCells[row][col].setBackground(theme2);
					}
				}
			}

			CheckSudoku(rowSelected, colSelected);

			/*
			 * stop [TODO 6] Check if the player has solved the puzzle after this move. You
			 * could update the masks[][] on correct guess, and check the masks[][] if any
			 * input cell pending.
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
				dispose();
				SoundFX.WIN.stop();
				new MainMenu();

			}
			if (health <= 0) {
				SoundFX.init();
				SoundFX.volume = SoundFX.Volume.HIGH;
				SoundBGM.BGM.stop();
				SoundFX.GAMEOVER.play();
				JOptionPane.showMessageDialog(null, ("Game Over!  Time taken: " + time));
				dispose();
				new MainMenu();
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
					health--;
				}
			}
			for (int col = 0; col < GRID_SIZE; ++col) {
				if (puzzle[rowSelected][col] == input && masks[rowSelected][col] == false) {
					tfCells[rowSelected][col].setBackground(OPEN_CELL_TEXT_NO);
					SoundFX.init();
					SoundFX.volume = SoundFX.Volume.HIGH;
					SoundFX.WRONG.play();
					health--;
				}
			}
			for (int row = subGridRow * 3; row <= subGridRow * 3 + 2; ++row) {
				for (int col = subGridCol * 3; col <= subGridCol * 3 + 2; ++col) {
					if (puzzle[row][col] == input && masks[row][col] == false) {
						tfCells[row][col].setBackground(OPEN_CELL_TEXT_NO);
						SoundFX.init();
						SoundFX.volume = SoundFX.Volume.HIGH;
						SoundFX.WRONG.play();
						health--;
					}
				}
			}
		}
	}
}