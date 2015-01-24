package sneer.game.sokabota.core;

import sneer.gameengine.grid.Direction;
import sneer.gameengine.grid.Thing;

public class Gun extends Thing {

	Gun(Direction dir) {
		direction = dir;
	}
	
	@Override
	public String toString() {
		return ">";
	}

}
