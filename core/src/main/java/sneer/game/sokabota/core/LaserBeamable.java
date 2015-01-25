package sneer.game.sokabota.core;

import sneer.gameengine.grid.Direction;

public interface LaserBeamable {

	void takeBeam(Direction dir);

	void cleanLasers();

}
