package sneer.gameengine.grid;

public class Thing extends Utils {

	public Square square;
	public Direction direction = none;
	public boolean hasDisappeared;

	////////////////////////////////////////////// Can override:
	
	protected void act() {}
	protected int millisToWait() { return -1; }

	protected void collideWith(Thing other) {}

	
	////////////////////////////////////////////// Can call:

	public boolean step() {
		if (hasDisappeared) return false;
		Square neighbor = square.neighbor(direction);
		if (neighbor == null) return false;
		return neighbor.accept(this);
	}
	public void disappear() { hasDisappeared = true; if (square != null) square.remove(this); }
	public void drop(Thing other) {
		Square s = square;
		disappear();
		s.accept(other);
	}
	
	public void gameOver() {
		System.exit(0);
	}
	
	@Override
	public String toString() {
		throw new IllegalStateException("" + getClass() + " must implement toString.");
	}
	
	public boolean push(Direction dir) {
		Square neighbor = square.neighbor(dir);
		return neighbor != null && neighbor.accept(this);
	}
	
}
