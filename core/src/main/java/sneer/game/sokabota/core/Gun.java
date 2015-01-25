package sneer.game.sokabota.core;

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
		return ">";
	}

	void fireIfOn() {
		if (isOn)
			BeamCrossing.produce(square, direction);
	}
	
}
