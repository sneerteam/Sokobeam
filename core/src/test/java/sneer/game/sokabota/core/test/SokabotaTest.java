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
				" 1 ");
		
		tap(P1, " 1*");
		
		scene(  "  1");
	}
	
	@Test
	public void deathByLaser() {
		subject = new Sokabota(
				"1   ",
				">  2");
		
		tap(P1,	"1   ",
				"*  2");
		
		tap(P1,	"1   ",
				"*--+");
		
		scene(  "1   ",
				">  +");
		
	}

	
	
	private void tap(Player player, String... scene) {
		int tapLine = -1;
		String lineWithTap = null;
		for (int line = 0; line < scene.length; line++) {
			if (scene[line].contains("*")) {
				lineWithTap = scene[line];
				tapLine = line;
				break;
			}
		}
		int tapCol = lineWithTap.indexOf('*');

		scene[tapLine] = scene[tapLine].replace('*', subject.sceneAsText()[tapLine].charAt(tapCol));
		
		scene(scene);
		
		subject.tap(player, tapLine, tapCol);
	}

	
	private void scene(String... expected) {
		assertArrayEquals(expected, subject.sceneAsText());
	}



}
