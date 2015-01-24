package sneer.game.sokabota.core;

import sneer.gameengine.grid.Direction;
import sneer.gameengine.grid.Thing;

public class Wall extends Thing {

	@Override
	public boolean push(Direction dir) {
		return false;
	}
	
	@Override
	public String toString() {
		return "W";
	}
	
}
