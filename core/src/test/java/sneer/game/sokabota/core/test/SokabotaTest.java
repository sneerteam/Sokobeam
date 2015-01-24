package sneer.game.sokabota.core.test;

import org.junit.Assert;
import org.junit.Test;

import sneer.game.sokabota.core.Sokabota;

public class SokabotaTest extends Assert {

	private Sokabota subject;
	
	@Test
	public void simpleMovement() {
		subject = new Sokabota(
				"W1 ");
		
		tap(1,  "*1 ");

		tap(1,  "W1*");
		
		scene(  "W 1");
	}
	
	@Test
	public void movementAttemptToDistantObjectFails() {
		subject = new Sokabota(
				"1  >");
		
		tap(1,  "1  *");

		scene(  "1  >");
	}
	
	@Test
	public void pushingBoxes() {
		subject = new Sokabota(
				"1b ");
		
		tap(1,  "1* ");

		scene(  " 1b");
	}
	
	@Test
	public void deathByLaser() {
		subject = new Sokabota(
				"1   ",
				">  2");
		
		tap(1,	"1   ",
				"*  2");
		
		tap(1,	"1   ",
				"*--+");
		
		scene(  "1   ",
				">  +");
		
	}

	@Test
	public void victory() {
		subject = new Sokabota(
				"1!",
				" 2");

		assertFalse(subject.isVictorious());
		
		tap(1,	"1*",
				" 2");
		
		tap(2,	" *",
				" 2");
		
		scene(  " !",
				"  ");
		
		assertTrue(subject.isVictorious());
	}

	
	
	private void tap(int player, String... expected) {
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
