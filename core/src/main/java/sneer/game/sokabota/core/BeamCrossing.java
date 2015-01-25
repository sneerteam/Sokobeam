package sneer.game.sokabota.core;

import static sneer.gameengine.grid.Direction.DOWN;
import static sneer.gameengine.grid.Direction.UP;
import sneer.gameengine.grid.Direction;
import sneer.gameengine.grid.Square;
import sneer.gameengine.grid.Thing;


public class BeamCrossing extends Thing implements LaserBeamable {

	public boolean hasHorizontalBeam = false;
	public boolean hasVerticalBeam   = false;
	
	
	static void produce(Square origin, Direction dir) {
		Square square = origin.neighbor(dir);
		if (square == null) return;
		
		if (square.thing instanceof LaserBeamable) {
			((LaserBeamable)square.thing).takeBeam(dir);
			return;
		}
		
		BeamCrossing next = new BeamCrossing();
		if (!square.accept(next)) return;
		next.takeBeam(dir);
	}
	
	private BeamCrossing() {}

	@Override
	protected void collideWith(Thing other) {
		if (other instanceof Player)
			((Player)other).die();
		if (other instanceof Box)
			other.disappear();
		
	}

	@Override
	public void takeBeam(Direction dir) {
		if (dir == UP || dir == DOWN) {
			hasVerticalBeam = true;
		} else {
			hasHorizontalBeam = true;
		}
		produce(square, dir);
	}

	@Override
	public void cleanLasers() {
		disappear();		
	}

	@Override
	public String toString() {
		if (hasHorizontalBeam && hasVerticalBeam) return "+";
		if (hasHorizontalBeam) return "-";
		if (hasVerticalBeam) return "|";
		throw new IllegalStateException();
	}

}
