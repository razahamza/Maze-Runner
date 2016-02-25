package objects;

/* Hamza Raza
 * 
 * 
 */
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

public class Level {

	//declares variables
	private Point start;
	private Point end;
	private boolean current = false;
	private char[][] map;
	private List<Rectangle> boundary;

	//constructor to initialize variables when creating the object
	public Level(Point start, Point end, boolean current) {
		this.start = start;
		this.end = end;
		this.current = current;
	}

	//made get and set methods
	public int getStartX() {
		return start.x;
	}

	public int getStartY() {
		return start.y;
	}

	public int getEndX() {
		return end.x;
	}

	public int getEndY() {
		return end.y;
	}

	public void setMap(char[][] map) {
		this.map = map;
	}

	public char[][] getMap() {
		return map;
	}

	public void setCurrent(boolean visible) {
		current = visible;
	}

	public boolean isCurrent() {
		return current;
	}

	public void setBoundary(List<Rectangle> boundary) {
		this.boundary = boundary;
	}

	public List<Rectangle> getBoundary() {
		return boundary;
	}
}
