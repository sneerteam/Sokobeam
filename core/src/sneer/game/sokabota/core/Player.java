package sneer.game.sokabota.core;

import sneer.gameengine.grid.Thing;

public class Player extends Thing {
	
	public static final Player P1 = new Player(1);
	public static final Player P2 = new Player(2);
	public static final Player P3 = new Player(3);
	public static final Player P4 = new Player(4);

	private final int number;
	
	private Player(int number) {
		this.number = number;
	}
	
	@Override
	public String toString() {
		return "" + number;
	}
}
