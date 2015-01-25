package sneer.game.sokabota.core;

import static sneer.gameengine.grid.Direction.DOWN;
import static sneer.gameengine.grid.Direction.LEFT;
import static sneer.gameengine.grid.Direction.RIGHT;
import static sneer.gameengine.grid.Direction.UP;

import java.util.HashMap;
import java.util.Map;

import sneer.gameengine.grid.Game;
import sneer.gameengine.grid.Square;
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
		if (character.equals("b")) return new Box();

		if (character.equals("1")) return addPlayer(1);
		if (character.equals("2")) return addPlayer(2);
		if (character.equals("3")) return addPlayer(3);
		if (character.equals("4")) return addPlayer(4);

		if (character.equals("A")) return new Gun(UP);
		if (character.equals("V")) return new Gun(DOWN);
		if (character.equals(">")) return new Gun(RIGHT);
		if (character.equals("<")) return new Gun(LEFT);

		if (character.equals("/" )) return new Mirror("/");
		if (character.equals("\\")) return new Mirror("\\");

		throw new IllegalStateException("Unknown character: " + character);
	}

	private Thing addPlayer(int p) {
		Player ret = new Player(p);
		playersByNumber.put(p, ret);
		return ret;
	}

	public void tap(int player, int line, int col) {
		playersByNumber.get(player).tap(scene[line][col]);
		updateLasers();
	}

	private void updateLasers() {
		for (int lin = 0; lin < scene.length; lin++)
			for (int col = 0; col < scene[0].length; col++)
				cleanLasers(scene[lin][col]);

		for (int lin = 0; lin < scene.length; lin++)
			for (int col = 0; col < scene[0].length; col++)
				updateLasers(scene[lin][col]);
	}

	private void cleanLasers(Square square) {
		if (square.thing instanceof LaserBeamable)
			((LaserBeamable)square.thing).cleanLasers();
		
	}

	private void updateLasers(Square square) {
		if (square.thing instanceof Gun)
			((Gun)square.thing).fireIfOn();
	}

	public boolean isVictorious() {
		for (Player p : playersByNumber.values())
			if (!p.isVictorious()) return false;
		return true;
	}

}
