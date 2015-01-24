package sneer.game.sokabota.core;

import static sneer.gameengine.grid.Direction.DOWN;
import static sneer.gameengine.grid.Direction.LEFT;
import static sneer.gameengine.grid.Direction.RIGHT;
import static sneer.gameengine.grid.Direction.UP;

import java.util.HashMap;
import java.util.Map;

import sneer.gameengine.grid.Game;
import sneer.gameengine.grid.Thing;


public class Sokabota extends Game {

	Map<Integer, Player> playersByNumber = new HashMap<Integer, Player>();

	public Sokabota(String... scene) {
		setScene(scene);
	}

	@Override
	public Thing thingRepresentedBy(String character) {
		if (character.equals("!")) return new ExitDoor();
		if (character.equals("W")) return new Wall();

		if (character.equals("1")) return addPlayer(1);
		if (character.equals("2")) return addPlayer(2);
		if (character.equals("3")) return addPlayer(3);
		if (character.equals("4")) return addPlayer(4);

		if (character.equals("A")) return new Gun(UP);
		if (character.equals("V")) return new Gun(DOWN);
		if (character.equals(">")) return new Gun(RIGHT);
		if (character.equals("<")) return new Gun(LEFT);
		
		throw new IllegalStateException("Unknown character: " + character);
	}

	private Thing addPlayer(int p) {
		Player ret = new Player(p);
		playersByNumber.put(p, ret);
		return ret;
	}

	public void tap(int player, int line, int col) {
		playersByNumber.get(player).tap(scene[line][col]);
	}

	public boolean isVictorious() {
		for (Player p : playersByNumber.values())
			if (!p.isVictorious()) return false;
		return true;
	}

}
