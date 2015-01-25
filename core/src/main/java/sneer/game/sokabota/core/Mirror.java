package sneer.game.sokabota.core;

import static sneer.gameengine.grid.Direction.DOWN;
import static sneer.gameengine.grid.Direction.LEFT;
import static sneer.gameengine.grid.Direction.RIGHT;
import static sneer.gameengine.grid.Direction.UP;
import sneer.gameengine.grid.Direction;
import sneer.gameengine.grid.Thing;

public class Mirror extends Thing implements LaserBeamable {

	public String orientation;
	public boolean hasUpperReflection;
	public boolean hasLowerReflection;
	
	public Mirror(String orientation) {
		this.orientation = orientation;
	}
	
	@Override
	public void takeBeam(Direction in) {
		Direction out = null;
		if (in == RIGHT && orientation.equals("/" )) out = UP;
		if (in == LEFT  && orientation.equals("/" )) out = DOWN;
		if (in == UP    && orientation.equals("/" )) out = RIGHT;
		if (in == DOWN  && orientation.equals("/" )) out = LEFT;
		
		if (in == RIGHT && orientation.equals("\\")) out = DOWN;
		if (in == LEFT  && orientation.equals("\\")) out = UP;
		if (in == UP    && orientation.equals("\\")) out = LEFT;
		if (in == DOWN  && orientation.equals("\\")) out = RIGHT;
		
		takeBeam(in, out);
	}

	private void takeBeam(Direction in, Direction out) {
		if (in == UP || out == UP) {
			hasUpperReflection = true;
		} else {
			hasLowerReflection = true;
		}
		BeamCrossing.produce(square, out);
	}
	
	@Override
	public void cleanLasers() {
		hasUpperReflection = false;
		hasLowerReflection = false;
	}

	@Override
	public String toString() {
		return orientation;
	}

}
