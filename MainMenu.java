import java.awt.*; // Uses AWT's Layout Managers
import java.awt.event.*; // Uses AWT's Event Handlers
import javax.swing.*; // Uses Swing's Container/Components
import java.util.Random;

/**
 * The Sudoku game. To solve the number puzzle, each row, each column, and each
 * of the nine 3×3 sub-grids shall contain all of the digits from 1 to 9
 */
public class MainMenu extends JFrame {

	public MainMenu() {

		SoundBGM.init();
		SoundBGM.volume = SoundBGM.Volume.HIGH;
		SoundBGM.MENU.play();

		Container cp = getContentPane();
		cp.setLayout(new BorderLayout());

		JPanel p2 = new JPanel();

		JPanel p1 = new JPanel();
		p1.add(new JLabel(new ImageIcon("logo.png")));

		JButton easy = new JButton("EASY");
		easy.setToolTipText("5 blanks: Easiest game of your life");
		easy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Sudoku(1);
				SoundBGM.MENU.stop();
				dispose();
			}
		});
		JButton medium = new JButton("MEDIUM");
		medium.setToolTipText("10 blanks: Easier than online submission");
		medium.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Sudoku(2);
				SoundBGM.MENU.stop();
				dispose();
			}
		});
		JButton hard = new JButton("HARD");
		hard.setToolTipText("20 blanks: Still easier than online submission");
		hard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Sudoku(3);
				SoundBGM.MENU.stop();
				dispose();
			}
		});
		JButton campaign = new JButton("CAMPAIGN");
		campaign.setToolTipText("Progress through all 4 difficulties, with no chance of going back");
		campaign.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new SudokuCAM();
				SoundBGM.MENU.stop();
				dispose();
			}
		});
		JButton insane = new JButton("INSANE");
		insane.setToolTipText("40 blanks: A little crazier");
		insane.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Sudoku(4);
				SoundBGM.MENU.stop();
				dispose();
			}
		});
		JButton quit = new JButton("EXIT");
		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int a = JOptionPane.showConfirmDialog(null, "Exit game?");
				if (a == JOptionPane.YES_OPTION) {
					SoundBGM.MENU.stop();
					System.exit(1);
				}
			}
		});

		p2.add(easy);
		p2.add(medium);
		p2.add(hard);
		p2.add(insane);
		p2.add(campaign);
		p2.add(quit);
		cp.add(p1, "North");
		cp.add(p2, "South");
		pack();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Handle window closing
		setTitle("Sudoku");
		setVisible(true);

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
