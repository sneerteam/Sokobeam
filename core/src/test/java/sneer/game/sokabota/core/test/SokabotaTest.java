package sneer.game.sokabota.core.test;

import static sneer.game.sokabota.core.Player.P1;

import org.junit.Assert;
import org.junit.Test;

import sneer.game.sokabota.core.Player;
import sneer.game.sokabota.core.Sokabota;

public class SokabotaTest extends Assert {

	private Sokabota subject;
	
	@Test
	public void simpleMovement() {
		subject = new Sokabota(
				"WWWWW",
				"W 1 W",
				"WWWWW");
		tap(P1,
				"WWWWW",
				"W 1+W",
				"WWWWW");
		assertArrayEquals(new String[]{
				"WWWWW",
				"W  1W",
				"WWWWW"}, subject.sceneAsText());
	}

	private void tap(Player player, String... scene) {
		int tapLine = -1;
		String lineWithTap = null;
		for (int line = 0; line < scene.length; line++) {
			if (scene[line].contains("+")) {
				lineWithTap = scene[line];
				tapLine = line;
				break;
			}
		}
		int tapCol = lineWithTap.indexOf('+');

		scene[tapLine] = scene[tapLine].replace('+', subject.sceneAsText()[tapLine].charAt(tapCol));
		
		assertArrayEquals(scene, subject.sceneAsText());
		
		subject.tap(player, tapLine, tapCol);
	}

}
