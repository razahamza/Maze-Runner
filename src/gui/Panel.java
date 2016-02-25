package gui;

/* Hamza Raza
 * 
 * 
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JPanel;
import javax.swing.Timer;

import objects.Level;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

public class Panel extends JPanel implements ActionListener, KeyListener,
		MouseListener {

	//delay timer
	Timer timer;

	//width, height of panel and jframe
	public final int WIDTH = 800;
	public final int HEIGHT = 875;

	//array of the object Level to allow more then one level in the game
	Level[] levels = {
			new Level(new Point(620, 10), new Point(25, 720), false),
			new Level(new Point(620, 10), new Point(675, 750), false),
			new Level(new Point(680, 10), new Point(675, 690), false),
			new Level(new Point(20, 10), new Point(341, 340), false) };

	//location of player (red block)
	int px;
	int py;

	//speed of the red block
	int sx, sy;
	int speed;
	int delay = 15;

	int lives;

	//variable to hold the start time of the game
	long start;

	boolean extreme = false;

	String menu = "main";
	String score = "";

	//audio and input stream variables
	InputStream mainIS = null;
	AudioStream mainAS = null;

	InputStream clickIS = null;
	AudioStream clickAS = null;

	//constructor to initialise main components
	public Panel() {
		setDoubleBuffered(true);
		//add listeners to the panel
		addKeyListener(this);
		addMouseListener(this);

		readLevels();
		setBoundarys();

		//initialise audio and input streams
		try {
			mainIS = new FileInputStream(new File("Music//Main.wav"));
			mainAS = new AudioStream(mainIS);

			clickIS = new FileInputStream(new File("Music//Pop.wav"));
			clickAS = new AudioStream(clickIS);
		} catch (IOException e) {
			e.printStackTrace();
		}

		//timer starts for the actionlistener delay
		timer = new Timer(delay, this);
		timer.start();
	}

	//Purpose: iterate through each level object and use the map to set and store the boundaries
	//Pre: None
	//Post: N/A
	public void setBoundarys() {
		for (Level level : levels) {
			int x = 0;
			int y = 0;
			List<Rectangle> boundary = new ArrayList<Rectangle>();
			for (int i = 0; i < level.getMap().length; i++) {
				x = 0;
				for (int k = 0; k < level.getMap()[i].length; k++) {
					if (level.getMap()[i][k] == '#') {
						boundary.add(new Rectangle(x + 1, y + 1, 39, 39));
					}
					x += 40;
				}
				y += 40;
			}
			//sets the boundaries of the level object
			level.setBoundary(boundary);
		}
	}

	//Purpose: iterate through each level object and use input from file to set and store the map
	//Pre: None
	//Post: N/A
	public void readLevels() {
		try {
			Scanner reader = new Scanner(new File("Data//Levels.txt"));
			for (Level level : levels) {
				char[][] map = new char[20][20];
				s: while (reader.hasNextLine()) {
					for (int i = 0; i < map.length; i++) {
						String line = reader.nextLine();
						if (line.contains("-"))
							break s;
						for (int k = 0; k < map[i].length; k++) {
							map[i][k] = line.charAt(k);
						}
					}
				}
				//sets the map of the level object
				level.setMap(map);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	//used to render text
	private final RenderingHints rh = new RenderingHints(
			RenderingHints.KEY_TEXT_ANTIALIASING,
			RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

	//Purpose: to show graphics on the jpanel
	//Pre: None
	//Post: N/A
	public void paint(Graphics g) {
		super.paint(g);
		//set rendering hints
		((Graphics2D) g).setRenderingHints(this.rh);

		//menu is the interface, it checks what menu equals to draw that interface
		if (menu.equalsIgnoreCase("main")) {
			g.setColor(Color.WHITE);
			g.drawRect(0, 0, WIDTH, HEIGHT);
			g.fillRect(0, 0, WIDTH, HEIGHT);

			g.setFont(new Font("Century Gothic", Font.BOLD, 48));
			g.setColor(Color.BLACK);
			centerText("Maze Phaze", WIDTH, HEIGHT / 3, g);
			centerText("Mode Selection", WIDTH, HEIGHT, g);
			centerText("Instructions", WIDTH, HEIGHT + 150, g);
		} else if (menu.equalsIgnoreCase("mode")) {
			g.setColor(Color.WHITE);
			g.drawRect(0, 0, WIDTH, HEIGHT);
			g.fillRect(0, 0, WIDTH, HEIGHT);

			g.setFont(new Font("Century Gothic", Font.BOLD, 48));
			g.setColor(Color.BLACK);
			centerText("Easy", WIDTH, HEIGHT - 300, g);
			centerText("Medium", WIDTH, HEIGHT - 150, g);
			centerText("Hard", WIDTH, HEIGHT, g);
			centerText("Extreme", WIDTH, HEIGHT + 150, g);
			g.drawString("Back", 10, HEIGHT - 40);
		} else if (menu.equalsIgnoreCase("help")) {
			g.setColor(Color.WHITE);
			g.drawRect(0, 0, WIDTH, HEIGHT);
			g.fillRect(0, 0, WIDTH, HEIGHT);

			g.setFont(new Font("Century Gothic", Font.BOLD, 26));
			g.setColor(Color.BLACK);
			centerText("Welcome to Maze Phaze!", WIDTH, 50, g);
			centerText("The rules are simple. Slide across the white path ",
					WIDTH, 140, g);
			centerText("and reach the blue box to advance to the next level.",
					WIDTH, 230, g);
			centerText(
					"You have 3 lives. Make sure to stay ON the white path.",
					WIDTH, 320, g);
			centerText(
					"You will lose a life if you touch the black boundaries.",
					WIDTH, 410, g);
			centerText(
					"There are 4 levels. Each level increases in difficulty.",
					WIDTH, 500, g);
			centerText(
					"You can select upto 4 modes. Each mode increases in speed.",
					WIDTH, 590, g);
			centerText("The mode EXTREME disables your breaks.", WIDTH, 680, g);
			centerText("Enjoy!", WIDTH, 770, g);

			centerText("Use arrow keys to move.", WIDTH, 1000, g);

			g.setFont(new Font("Century Gothic", Font.BOLD, 48));
			g.drawString("Back", 10, HEIGHT - 40);
		} else if (menu.equalsIgnoreCase("ingame")) {
			score = toElapsedString();
			int x = 0;
			int y = 0;
			g.setColor(Color.BLACK);
			//a double for loop is used to draw the map
			for (int i = 0; i < getLevel(levels).getMap().length; i++) {
				x = 0;
				for (int k = 0; k < getLevel(levels).getMap()[i].length; k++) {
					if (getLevel(levels).getMap()[i][k] == '#') {
						g.drawRect(x, y, 40, 40);
						g.fillRect(x, y, 40, 40);
					}
					x += 40;
				}
				y += 40;
			}

			g.setColor(Color.BLACK);
			g.drawRect(-5, 812, WIDTH, 30);
			g.setColor(new Color(0, 0, 0, 200));
			g.fillRect(-5, 812, WIDTH, 30);

			//draws the character
			g.setColor(Color.RED);
			g.drawRect(px, py, 20, 20);
			g.fillRect(px, py, 20, 20);

			//draws the blue character
			if (getLevel(levels).equals(levels[3])) {
				g.setColor(Color.BLUE);
				g.drawRect(getLevel(levels).getEndX(), getLevel(levels)
						.getEndY(), 40, 40);
				g.fillRect(getLevel(levels).getEndX(), getLevel(levels)
						.getEndY(), 40, 40);
			} else {
				g.setColor(Color.BLUE);
				g.drawRect(getLevel(levels).getEndX(), getLevel(levels)
						.getEndY(), 20, 20);
				g.fillRect(getLevel(levels).getEndX(), getLevel(levels)
						.getEndY(), 20, 20);
			}

			//draws the lives and scores
			g.setFont(new Font("Calibri", Font.BOLD, 24));
			g.setColor(Color.CYAN);
			outlineText(g, "Lives: " + lives, 10, HEIGHT - 40, Color.BLACK);
			g.setColor(Color.GREEN);
			outlineText(g, "Time: " + score, WIDTH - 132, HEIGHT - 40,
					Color.BLACK);

			//checks collision with edges or boundaries
			if (isInBoundary()) {
				//subtracts 1 life
				lives--;
				sx = 0;
				sy = 0;
				if (lives > 0) {
					//resets start position
					px = getLevel(levels).getStartX();
					py = getLevel(levels).getStartY();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					menu = "end";
				}
			}

			//checks collision with blue character
			if (isAtEnd()) {
				if (getNextLevel(levels) != null) {
					//if there is a next level it will set it to true and the current level to false so it can change levels
					getNextLevel(levels).setCurrent(true);
					getLevel(levels).setCurrent(false);
					//reset start position for the new level
					px = getLevel(levels).getStartX();
					py = getLevel(levels).getStartY();
				} else {
					menu = "won";
				}
			}
		} else if (menu.equalsIgnoreCase("end") || menu.equalsIgnoreCase("won")) {
			//stops the music
			playMain(false);
			//re initializes the streams
			try {
				mainIS = new FileInputStream(new File("Music//Main.wav"));
				mainAS = new AudioStream(mainIS);
			} catch (IOException e) {
				e.printStackTrace();
			}
			g.setColor(Color.WHITE);
			g.drawRect(0, 0, WIDTH, HEIGHT);
			g.fillRect(0, 0, WIDTH, HEIGHT);

			g.setFont(new Font("Century Gothic", Font.BOLD, 48));
			g.setColor(Color.BLACK);
			centerText("Game Over", WIDTH, HEIGHT - 100, g);
			if (menu.equalsIgnoreCase("won")) {
				centerText("Congratulations!", WIDTH, HEIGHT + 50, g);
				centerText("You finished it in " + score, WIDTH, HEIGHT + 200,
						g);
			}
			centerText("Main Menu", WIDTH, 2 * HEIGHT - 125, g);
		}
	}

	//Purpose: to play a click sound
	//Pre: None
	//Post: N/A
	public void playClick() {
		//starts the audio
		AudioPlayer.player.start(clickAS);
		//re initializes the streams
		try {
			clickIS = new FileInputStream(new File("Music//Pop.wav"));
			clickAS = new AudioStream(clickIS);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Purpose: to play the background music
	//Pre: 1 boolean parameter
	//Post: N/A
	public void playMain(boolean start) {
		if (start)
			//starts the audio
			AudioPlayer.player.start(mainAS);
		else
			//stops the audio
			AudioPlayer.player.stop(mainAS);
	}

	//Purpose: to get the time elapsed
	//Pre: None
	//Post: returns long value
	public long getElapsed() {
		return System.currentTimeMillis() - start;
	}

	//Purpose: to get the time elapsed in a string form
	//Pre: None
	//Post: returns string value
	public String toElapsedString() {
		return format(getElapsed());
	}

	//Purpose: to format the time
	//Pre: 1 long parameter
	//Post: returns String parameter
	public static String format(final long time) {
		final StringBuilder t = new StringBuilder();
		//calculates the seconds and minutes
		final long total_secs = time / 1000;
		final long total_mins = total_secs / 60;
		final int secs = (int) total_secs % 60;
		final int mins = (int) total_mins % 60;

		//adds to the string builder the time in proper format
		if (mins < 10) {
			t.append("0");
		}
		t.append(mins);
		t.append(":");
		if (secs < 10) {
			t.append("0");
		}
		t.append(secs);
		return t.toString();
	}

	//Purpose: to outline a text
	//Pre: 1 Graphics parameter, 1 String parameter, 2 float parameter, 1 Color parameter
	//Post: N/A
	public static void outlineText(Graphics g, String s, float x, float y,
			Color c) {
		Graphics2D g2 = (Graphics2D) g;
		Color origColor = g.getColor();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);

		g2.setColor(c);
		int t = 1;
		g2.drawString(s, x + t, y + t);
		g2.drawString(s, x + t, y - t);
		g2.drawString(s, x - t, y + t);
		g2.drawString(s, x - t, y - t);

		g2.drawString(s, x + t, y);
		g2.drawString(s, x, y + t);
		g2.drawString(s, x - t, y);
		g2.drawString(s, x, y - t);

		g2.setColor(origColor);
		g2.drawString(s, x, y);
	}

	//Purpose: to get the current level
	//Pre: unlimited number of Level parameters
	//Post: returns a Level value
	public Level getLevel(Level... levels) {
		//iterates through each level and checks which one set current to true
		for (Level level : levels) {
			if (level.isCurrent())
				return level;
		}
		return null;
	}

	//Purpose: to get the next level
	//Pre: unlimited number of Level parameters
	//Post: returns a Level value
	public Level getNextLevel(Level... levels) {
		//iterates through each level and checks which one set current to true and then iterates one more level and returns that level
		boolean next = false;
		for (Level level : levels) {
			if (next)
				return level;
			if (level.isCurrent()) {
				next = true;
			}
		}
		return null;
	}

	//Purpose: to center text on screen
	//Pre: 1 String parameter, 2 int parameters, 1 Graphics parameter
	//Post: N/A
	public void centerText(String s, int w, int h, Graphics g) {
		FontMetrics fm = g.getFontMetrics();
		int x = (w - fm.stringWidth(s)) / 2;
		int y = (fm.getAscent() + (h - (fm.getAscent() + fm.getDescent())) / 2);
		g.drawString(s, x, y);
	}

	//Purpose: to check if the red block collided with the blue block
	//Pre: None
	//Post: returns a boolean value
	public boolean isAtEnd() {
		Rectangle rec = new Rectangle(getLevel(levels).getEndX(), getLevel(
				levels).getEndY(), 20, 20);
		if (rec.contains(new Point(px, py))
				|| rec.contains(new Point(px + 20, py))
				|| rec.contains(new Point(px + 20, py + 20))
				|| rec.contains(new Point(px, py + 20))) {
			return true;
		}
		return false;
	}

	//Purpose: to check if the red block collided with the edge or any boundary
	//Pre: None
	//Post: returns a boolean value
	public boolean isInBoundary() {
		if (px < 0 || py < 0 || px + 20 > WIDTH || py + 20 > HEIGHT)
			return true;
		for (Rectangle b : getLevel(levels).getBoundary()) {
			if (b.contains(px, py) || b.contains(px + 20, py)
					|| b.contains(px + 20, py + 20) || b.contains(px, py + 20))
				return true;
		}
		return false;
	}

	//Purpose: to move the red block
	//Pre: None
	//Post: N/A
	public void move() {
		px += sx;
		py += sy;
	}

	//Purpose: to allow the keylistener to work
	//Pre: None
	//Post: N/A
	//Referenced in Works Cited
	public void addNotify() {
		super.addNotify();
		requestFocus();
	}

	//Purpose: to call the method to move the redblock and then repaint on screen
	//Pre: 1 ActionEvent parameter
	//Post: N/A
	public void actionPerformed(ActionEvent arg0) {
		move();
		repaint();
	}

	//Purpose: to allow the arrow keys to move the character in different directions at different speeds
	//Pre: 1 KeyEvent parameter
	//Post: N/A
	public void keyPressed(KeyEvent arg0) {
		if (menu.equalsIgnoreCase("ingame")) {
			int key = arg0.getKeyCode();

			//switch between the keycode to see which key is pressed
			switch (key) {
			case KeyEvent.VK_UP:
				sy = -speed;
				if (extreme)
					sx = 0;
				break;
			case KeyEvent.VK_DOWN:
				sy = speed;
				if (extreme)
					sx = 0;
				break;
			case KeyEvent.VK_RIGHT:
				sx = speed;
				if (extreme)
					sy = 0;
				break;
			case KeyEvent.VK_LEFT:
				sx = -speed;
				if (extreme)
					sy = 0;
				break;
			}
		}
	}

	//Purpose: to allow the charater to break if he lets go of a key
	//Pre: 1 KeyEvent parameter
	//Post: N/A
	public void keyReleased(KeyEvent arg0) {
		if (menu.equalsIgnoreCase("ingame") && !extreme) {
			int key = arg0.getKeyCode();

			//switch between the keycode to see which key is pressed
			switch (key) {
			case KeyEvent.VK_UP:
				sy = 0;
				break;
			case KeyEvent.VK_DOWN:
				sy = 0;
				break;
			case KeyEvent.VK_RIGHT:
				sx = 0;
				break;
			case KeyEvent.VK_LEFT:
				sx = 0;
				break;
			}
		}
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	//Purpose: to check whether the mouse was clicked as depending on where it was clicked to do a certain task
	//Pre: 1 MouseEvent parameter
	//Post: N/A
	public void mouseClicked(MouseEvent arg0) {
		//checks if anything was clicked in the interfaces and does tasks accordingly
		if (menu.equalsIgnoreCase("main")) {
			if (new Rectangle(257, 423, 320, 32).contains(arg0.getPoint())) {
				playClick();
				menu = "mode";
			} else if (new Rectangle(270, 495, 260, 35).contains(arg0
					.getPoint())) {
				playClick();
				menu = "help";
			} else {
				return;
			}
		} else if (menu.equalsIgnoreCase("mode")) {
			lives = 3;
			start = System.currentTimeMillis();
			levels[0].setCurrent(true);
			px = getLevel(levels).getStartX();
			py = getLevel(levels).getStartY();
			extreme = false;
			if (new Rectangle(350, 270, 110, 45).contains(arg0.getPoint())) {
				playClick();
				speed = 3;
				menu = "ingame";
			} else if (new Rectangle(300, 350, 190, 35).contains(arg0
					.getPoint())) {
				playClick();
				speed = 4;
				menu = "ingame";
			} else if (new Rectangle(350, 420, 100, 35).contains(arg0
					.getPoint())) {
				playClick();
				speed = 5;
				menu = "ingame";
			} else if (new Rectangle(310, 495, 180, 30).contains(arg0
					.getPoint())) {
				playClick();
				speed = 5;
				extreme = true;
				menu = "ingame";
			} else if (new Rectangle(15, 800, 115, 35)
					.contains(arg0.getPoint())) {
				playClick();
				menu = "main";
				return;
			} else {
				return;
			}
			playMain(true);
		} else if (menu.equalsIgnoreCase("help")) {
			if (new Rectangle(15, 800, 115, 35).contains(arg0.getPoint())) {
				playClick();
				menu = "main";
				return;
			}
		} else if (menu.equalsIgnoreCase("end") || menu.equalsIgnoreCase("won")) {
			if (new Rectangle(270, 795, 250, 35).contains(arg0.getPoint())) {
				playClick();
				menu = "main";
			} else {
				return;
			}
		}
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
