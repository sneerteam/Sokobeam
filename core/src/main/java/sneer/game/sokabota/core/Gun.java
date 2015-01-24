package sneer.game.sokabota.core;

import sneer.gameengine.grid.Direction;
import sneer.gameengine.grid.Thing;

public class Gun extends Thing {

	private boolean isOn = false;

	Gun(Direction dir) {
		direction = dir;
	}
	
	@Override
	public String toString() {
		return ">";
	}

	public void toggle() {
		isOn = !isOn;
		if (isOn)
			new Beam(square.neighbor(direction), direction);
	}

}
