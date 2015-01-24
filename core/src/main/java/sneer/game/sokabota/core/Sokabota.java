package sneer.game.sokabota.core;

import static sneer.game.sokabota.core.Player.P1;
import static sneer.game.sokabota.core.Player.P2;
import static sneer.game.sokabota.core.Player.P3;
import static sneer.game.sokabota.core.Player.P4;
import static sneer.gameengine.grid.Direction.DOWN;
import static sneer.gameengine.grid.Direction.LEFT;
import static sneer.gameengine.grid.Direction.RIGHT;
import static sneer.gameengine.grid.Direction.UP;

import java.util.ArrayList;
import java.util.List;

import sneer.gameengine.grid.Game;
import sneer.gameengine.grid.Thing;


public class Sokabota extends Game {

	List<Player> players = new ArrayList<Player>();

	public Sokabota(String... scene) {
		setScene(scene);
	}

	@Override
	public Thing thingRepresentedBy(String character) {
		if (character.equals("!")) return new ExitDoor();
		if (character.equals("W")) return new Wall();

		if (character.equals("1")) return addPlayer(P1);
		if (character.equals("2")) return addPlayer(P2);
		if (character.equals("3")) return addPlayer(P3);
		if (character.equals("4")) return addPlayer(P4);

		if (character.equals("A")) return new Gun(UP);
		if (character.equals("V")) return new Gun(DOWN);
		if (character.equals(">")) return new Gun(RIGHT);
		if (character.equals("<")) return new Gun(LEFT);
		
		throw new IllegalStateException("Unknown character: " + character);
	}

	private Thing addPlayer(Player p) {
		players.add(p);
		return p;
	}

	public void tap(Player player, int line, int col) {
		player.tap(scene[line][col]);
	}

	public boolean isVictorious() {
		for (Player p : players)
			if (!p.isVictorious()) return false;
		return true;
	}

}
