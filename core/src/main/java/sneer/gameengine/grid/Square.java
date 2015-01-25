package sneer.gameengine.grid;

import static sneer.gameengine.grid.Direction.UP;
import static sneer.gameengine.grid.Direction.DOWN;
import static sneer.gameengine.grid.Direction.LEFT;
import static sneer.gameengine.grid.Direction.RIGHT;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Square extends Utils {

	public Thing thing;

	Map<Direction, Square> neighbors = new HashMap<Direction, Square>();

	public int line;
	public int column;

	
	public Square(int line, int column) {
		this.line = line;
		this.column = column;
	}


	public boolean isEmpty() {	return thing == null; }
	
	
	void put(Thing newThing) {
		if (thing != null) oops("you cannot put " + newThing + " into a square that already contains a " + thing + ".");
		
		Square oldSquare = newThing.square;
		if (oldSquare != null) oldSquare.thing = null;
		
		thing = newThing;
		thing.square = this;
	}
	
	
	public void remove(Thing thing) {
		if (this.thing != thing) oops("Removing the wrong thing.");
		thing.square = null;
		this.thing = null;
	}
	
	
	public boolean accept(Thing other) {
		if (other == null) oops("Square cannot accept a null thing.");

		if (thing == null) {
			put(other);
			return true;
		}
		
		Thing t = thing;
		other.collideWith(t);
		t.collideWith(other);

		if (thing == null && !other.hasDisappeared){
			put(other);
			return true;
		} else
			return false;
	}

	
	public Square neighbor(Direction direction) {
		return neighbors.get(direction);
	}
	void setNeighbor(Direction direction, Square neighbor) {
		neighbors.put(direction, neighbor);
	}
	
	@Override
	public String toString() {
		return thing == null ? " " : thing.toString();
	}


	public Direction neighborDirection(Square dest) {
		if (neighbors.get(UP   ) == dest) return UP;
		if (neighbors.get(DOWN ) == dest) return DOWN;
		if (neighbors.get(LEFT ) == dest) return LEFT;
		if (neighbors.get(RIGHT) == dest) return RIGHT;
		return null;
	}


	public boolean isNeighbor(Square other) {
		return neighbors.containsValue(other);
	}


	public boolean hasClearPathTo(Square dest) {
		return hasClearPathTo(this, dest, new HashSet<Square>());
	}


	private boolean hasClearPathTo(Square orig, Square dest, HashSet<Square> visited) {
		if (this == dest) return true;
		if (visited.contains(this)) return false;
		visited.add(this);

		if (this != orig && thing != null) return false;
		for (Square n : neighbors.values())
			if (n.hasClearPathTo(orig, dest, visited)) return true;
		return false;
	}
}