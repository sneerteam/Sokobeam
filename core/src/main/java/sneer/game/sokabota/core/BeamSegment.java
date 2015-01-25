package sneer.game.sokabota.core;

import static sneer.gameengine.grid.Direction.DOWN;
import static sneer.gameengine.grid.Direction.LEFT;
import static sneer.gameengine.grid.Direction.RIGHT;
import static sneer.gameengine.grid.Direction.UP;
import sneer.gameengine.grid.Direction;
import sneer.gameengine.grid.Square;
import sneer.gameengine.grid.Thing;


public class BeamSegment extends Thing implements Disposable {

	public final Direction direction;
	public Disposable nextSegment;
	
	
	static Disposable create(Square origin, Direction dir) {
		Square square = origin.neighbor(dir);
		if (square == null) return null;
		if (square.thing instanceof LaserBeamable)
			return ((LaserBeamable)square.thing).takeBeam(dir);
		BeamSegment next = new BeamSegment(dir);
		if (!square.accept(next)) return null;
		next.propagate();
		return next;
	}
	
	private BeamSegment(Direction direction) {
		this.direction = direction;
	}

	private void propagate() {
		nextSegment = create(square, direction);
	}
	
	@Override
	protected void collideWith(Thing other) {
		if (other instanceof Player)
			((Player)other).die();
		if (other instanceof Box)
			other.disappear();
	}
	
	@Override
	public String toString() {
		if (direction == UP   ) return "|";
		if (direction == DOWN ) return "|";
		if (direction == LEFT ) return "-";
		if (direction == RIGHT) return "-";
		throw new IllegalStateException();
	}

	@Override
	public void dispose() {
		if (nextSegment != null) nextSegment.dispose();
		disappear();
	}
}
