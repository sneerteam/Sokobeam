package sneer.gameengine.grid;

import java.util.Random;

class Utils {

	static Direction up = Direction.UP;
	static Direction down = Direction.DOWN;
	static Direction left = Direction.LEFT;
	static Direction right = Direction.RIGHT;
	static Direction none = Direction.NONE;
	
	static <T> T oops(String message) {
		System.err.println("\n\n");
		throw new Oops(message);
	}

	static Random randomGenerator = new Random();
	static int random(int max) {
		return randomGenerator.nextInt(max + 1);
	}
}

