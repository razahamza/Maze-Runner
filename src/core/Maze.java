package core;

/* Hamza Raza
 * 
 * 
 */
import gui.Panel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Maze extends JFrame {

	//contructor to add main panel and set JFrame visible
	public Maze() {
		Panel panel = new Panel();

		setTitle("Maze Phaze");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(panel.WIDTH, panel.HEIGHT);
		setLocationRelativeTo(null);
		setResizable(false);
		add(panel);
		addNotify();
		setVisible(true);
	}

	//calls this class to run the constructor allowing it to initialize the frame.
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Maze();
			}
		});
	}
}
