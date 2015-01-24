package sneer.game.sokabota.core;

import static sneer.gameengine.grid.Direction.DOWN;
import static sneer.gameengine.grid.Direction.LEFT;
import static sneer.gameengine.grid.Direction.RIGHT;
import static sneer.gameengine.grid.Direction.UP;

import java.util.Set;

import sneer.gameengine.grid.Direction;
import sneer.gameengine.grid.Thing;

public class Mirror extends Thing {

	String orientation;
	
	public Mirror(String orientation) {
		this.orientation = orientation;
	}
	
	void reflectLaserGoing(Direction in, Set<BeamSegment> beam) {
		Direction out = null;
		if (in == RIGHT && orientation.equals("/" )) out = UP;
		if (in == LEFT  && orientation.equals("/" )) out = DOWN;
		if (in == UP    && orientation.equals("/" )) out = RIGHT;
		if (in == DOWN  && orientation.equals("/" )) out = LEFT;
		
		if (in == RIGHT && orientation.equals("\\")) out = DOWN;
		if (in == LEFT  && orientation.equals("\\")) out = UP;
		if (in == UP    && orientation.equals("\\")) out = LEFT;
		if (in == DOWN  && orientation.equals("\\")) out = RIGHT;
		
		new BeamSegment(square.neighbor(out), out, beam);
	}
	
	@Override
	public String toString() {
		return orientation;
	}

}
