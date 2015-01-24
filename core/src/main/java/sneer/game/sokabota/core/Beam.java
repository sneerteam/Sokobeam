package sneer.game.sokabota.core;

import sneer.gameengine.grid.Direction;
import sneer.gameengine.grid.Square;
import sneer.gameengine.grid.Thing;

public class Beam extends Thing {

	Beam(Square square, Direction dir) {
		if (square != null && square.accept(this))
			new Beam(square.neighbor(dir), dir);
	}
	
	@Override
	protected void collideWith(Thing other) {
		if (other instanceof Player)
			((Player)other).die();
	}
	
	@Override
	public String toString() {
		return "-";
	}
}
