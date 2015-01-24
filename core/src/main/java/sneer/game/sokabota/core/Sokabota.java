package sneer.game.sokabota.core;

import static sneer.game.sokabota.core.Player.P1;
import sneer.gameengine.grid.Game;
import sneer.gameengine.grid.Thing;


public class Sokabota extends Game {

	public Sokabota(String... scene) {
		setScene(scene);
	}

	@Override
	public Thing thingRepresentedBy(String character) {
		if (character.equals("W")) return new Wall();
		if (character.equals("1")) return P1;
		throw new IllegalStateException("Unknown character: " + character);
	}

	public void tap(Player player, int line, int col) {
		scene[line][col].accept(player);
	}

	
	

}
