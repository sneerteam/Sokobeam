package sneer.game.sokabota.core;

import static sneer.gameengine.grid.Direction.UP;
import static sneer.gameengine.grid.Direction.DOWN;
import static sneer.gameengine.grid.Direction.LEFT;
import static sneer.gameengine.grid.Direction.RIGHT;

import java.util.Set;

import sneer.gameengine.grid.Direction;
import sneer.gameengine.grid.Square;
import sneer.gameengine.grid.Thing;

public class BeamSegment extends Thing {

	public final Direction direction;

	BeamSegment(Square square, Direction dir, Set<BeamSegment> beam) {
		this.direction = dir;
		if (square == null) return;
		if (square.accept(this)) {
			beam.add(this);
			new BeamSegment(square.neighbor(dir), dir, beam);
		} else {
			if (square.thing instanceof Mirror)
				((Mirror)square.thing).reflectLaserGoing(dir, beam);
		}
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
}
