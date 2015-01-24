package sneer.game.sokabota.core.test;

import static sneer.game.sokabota.core.Player.P1;
import static sneer.gameengine.grid.Direction.RIGHT;

import org.junit.Assert;
import org.junit.Test;

import sneer.game.sokabota.core.Sokabota;

public class SokabotaTest extends Assert {

	private Sokabota subject;
	
	@Test
	public void simpleMovement() {
		Sokabota.initialScene = new String[] {
				"WWWWW",
				"W 1 W",
				"WWWWW"};
		subject = new Sokabota();
		subject.move(P1, RIGHT);
		assertArrayEquals(new String[]{
				"WWWWW",
				"W  1W",
				"WWWWW"}, subject.sceneAsText());
	}

}
