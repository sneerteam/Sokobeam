package sneer.game.sokabota.core;

import static sneer.gameengine.grid.Direction.UP;
import static sneer.gameengine.grid.Direction.DOWN;
import static sneer.gameengine.grid.Direction.LEFT;
import static sneer.gameengine.grid.Direction.RIGHT;
import sneer.gameengine.grid.Direction;
import sneer.gameengine.grid.Thing;

public class Gun extends Thing {

	public boolean isOn = false;

	Gun(Direction dir) {
		direction = dir;
	}
	
	@Override
	public boolean push(Direction dir) {
		if (super.push(dir)) return true;
		toggle();
		return false;
	}

	public void toggle() {
		isOn = !isOn;
	}

	@Override
	public String toString() {
		if (direction == UP   ) return "A";
		if (direction == DOWN ) return "V";
		if (direction == LEFT ) return "<";
		if (direction == RIGHT) return ">";
		throw new IllegalStateException();
	}

	void fireIfOn() {
		if (isOn)
			BeamCrossing.produce(square, direction);
	}
	
}
