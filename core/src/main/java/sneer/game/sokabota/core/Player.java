package sneer.game.sokabota.core;

import sneer.gameengine.grid.Direction;
import sneer.gameengine.grid.Square;
import sneer.gameengine.grid.Thing;

public class Player extends Thing {
	
	private final int number;
	private boolean isDead = false;
	private boolean isVictorious = false;
	
	public Player(int number) {
		this.number = number;
	}

    public int number() { return number; }

	@Override
	public String toString() {
		return isDead ? "@" : "" + number;
	}

	public void tap(Square dest) {
		if (dest.accept(this)) return;
		if (hasDisappeared) return;
		Direction dir = square.neighborDirection(dest);
		if (dir == null) return; //Not neighbor

		Thing obstacle = dest.thing;
		if (obstacle.push(dir))
			dest.accept(this);
	}

	@Override
	protected void collideWith(Thing other) {
		if (other instanceof ExitDoor) {
			isVictorious = true;
			disappear();
		}
	}
	
	void die() {
		isDead = true;
	}

	boolean isVictorious() {
		return isVictorious;
	}
}
