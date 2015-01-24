package sneer.game.sokabota.core;

import static sneer.game.sokabota.core.Player.P1;
import sneer.gameengine.grid.Direction;
import sneer.gameengine.grid.Game;
import sneer.gameengine.grid.Thing;


public class Sokabota extends Game {

	static public String[] initialScene;

	public void move(Player player, Direction d) {
		player.direction = d;
		player.step();
	}

	@Override
	public void start() {
		setScene(initialScene);
	}

	@Override
	public Thing thingRepresentedBy(String character) {
		if (character.equals("W")) return new Wall();
		if (character.equals("1")) return P1;
		throw new IllegalStateException("Unknown character: " + character);
	}

	

}
