package sneer.game.sokabota.core;

import static sneer.gameengine.grid.Direction.DOWN;
import static sneer.gameengine.grid.Direction.UP;
import sneer.gameengine.grid.Direction;
import sneer.gameengine.grid.Square;
import sneer.gameengine.grid.Thing;


public class BeamCrossing extends Thing implements LaserBeamable {

	public Disposable horizontalBeam = null;
	public Disposable verticalBeam = null;
	
	
	static Disposable produce(Square origin, Direction dir) {
		Square square = origin.neighbor(dir);
		if (square == null) return null;
		
		if (square.thing instanceof LaserBeamable)
			return ((LaserBeamable)square.thing).takeBeam(dir);
		
		BeamCrossing next = new BeamCrossing();
		if (!square.accept(next)) return null;
		return next.takeBeam(dir);
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
	public Disposable takeBeam(Direction dir) {
		if (dir == UP || dir == DOWN) {
			verticalBeam = propagate(verticalBeam, dir);
			return verticalBeam;
		} else {
			horizontalBeam = propagate(horizontalBeam, dir);
			return horizontalBeam;
		}
	}
	
	private Disposable propagate(Disposable old, Direction dir) {
		if (old != null) throw new IllegalStateException();
		
		final Disposable next = produce(square, dir);
		return new Disposable() { @Override public void dispose() {
			if (next != null) next.dispose();
			if (this == horizontalBeam) {
				horizontalBeam = null;
			}
			if (this == verticalBeam) {
				verticalBeam = null;
			}
			if (verticalBeam == null && horizontalBeam == null)
				disappear();
		}};
	}

	@Override
	public String toString() {
		if (horizontalBeam != null && verticalBeam != null) return "+";
		if (horizontalBeam != null) return "-";
		if (verticalBeam   != null) return "|";
		throw new IllegalStateException();
	}
}
