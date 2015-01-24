package sneer.game.sokabota.core.test;

import static sneer.game.sokabota.core.Player.P1;
import static sneer.game.sokabota.core.Player.P2;

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

	@Test
	public void victory() {
		subject = new Sokabota(
				"1  !",
				"   2");

		assertFalse(subject.isVictorious());
		
		tap(P1,	"1  *",
				"   2");
		
		tap(P2,	"   *",
				"   2");
		
		scene(  "   !",
				"    ");
		
		assertTrue(subject.isVictorious());
	}

	
	
	private void tap(Player player, String... expected) {
		int tapLine = findTapLine(expected);
		int tapCol  = expected[tapLine].indexOf('*');

		expected[tapLine] = replaceTapWithActualThing(tapLine, tapCol, expected);
		scene(expected);
		
		subject.tap(player, tapLine, tapCol);
	}

	
	private int findTapLine(String... expected) {
		for (int line = 0; line < expected.length; line++)
			if (expected[line].contains("*"))
				return line;
		
		throw new IllegalStateException();
	}

	
	private String replaceTapWithActualThing(int tapLine, int tapCol, String... scene) {
		return scene[tapLine].replace('*', subject.sceneAsText()[tapLine].charAt(tapCol));
	}

	
	private void scene(String... expected) {
		assertArrayEquals(expected, subject.sceneAsText());
	}



}
