package sneer.game.sokabota.core;

import sneer.gameengine.grid.Direction;
import sneer.gameengine.grid.Thing;

public class Gun extends Thing {

	private Disposable beam;

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
		if (beam == null) {
			beam = BeamCrossing.produce(square, direction);
		} else {
			beam.dispose();
			beam = null;
		}
	}

	@Override
	public String toString() {
		return ">";
	}
	
}
