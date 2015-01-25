package sneer.game.sokabota.core;

import static sneer.gameengine.grid.Direction.DOWN;
import static sneer.gameengine.grid.Direction.LEFT;
import static sneer.gameengine.grid.Direction.RIGHT;
import static sneer.gameengine.grid.Direction.UP;
import sneer.gameengine.grid.Direction;
import sneer.gameengine.grid.Thing;

public class Mirror extends Thing implements LaserBeamable {

	public String orientation;
	public Disposable upperReflection;
	public Disposable lowerReflection;
	
	public Mirror(String orientation) {
		this.orientation = orientation;
	}
	
	@Override
	public Disposable takeBeam(Direction in) {
		Direction out = null;
		if (in == RIGHT && orientation.equals("/" )) out = UP;
		if (in == LEFT  && orientation.equals("/" )) out = DOWN;
		if (in == UP    && orientation.equals("/" )) out = RIGHT;
		if (in == DOWN  && orientation.equals("/" )) out = LEFT;
		
		if (in == RIGHT && orientation.equals("\\")) out = DOWN;
		if (in == LEFT  && orientation.equals("\\")) out = UP;
		if (in == UP    && orientation.equals("\\")) out = LEFT;
		if (in == DOWN  && orientation.equals("\\")) out = RIGHT;
		
		return reflection(in, out);
	}

	private Disposable reflection(Direction in, Direction out) {
		if (in == UP || out == UP) {
			upperReflection = reflection(upperReflection, in, out);
			return upperReflection;
		} else {
			lowerReflection = reflection(lowerReflection, in, out);
			return lowerReflection;
		}
	}

	private Disposable reflection(Disposable old, Direction in,	Direction out) {
		if (old != null) throw new IllegalStateException();
		final Disposable nextSegment = BeamCrossing.produce(square, out);
		
		return new Disposable() { @Override public void dispose() {
			nextSegment.dispose();
			if (upperReflection == this) {
				upperReflection = null;
				return;
			}
			if (lowerReflection == this) {
				lowerReflection = null;
				return;
			}
			throw new IllegalStateException();
		}};
	}

	@Override
	public String toString() {
		return orientation;
	}

}
