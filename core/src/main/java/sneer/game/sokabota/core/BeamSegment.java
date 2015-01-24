package sneer.game.sokabota.core;

import java.util.Set;

import sneer.gameengine.grid.Direction;
import sneer.gameengine.grid.Square;
import sneer.gameengine.grid.Thing;

public class BeamSegment extends Thing {

	BeamSegment(Square square, Direction dir, Set<BeamSegment> beam) {
		if (square != null && square.accept(this)) {
			beam.add(this);
			new BeamSegment(square.neighbor(dir), dir, beam);
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
		return "-";
	}
}
