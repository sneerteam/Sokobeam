package sneer.gameengine.grid;

import java.util.HashMap;
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
}