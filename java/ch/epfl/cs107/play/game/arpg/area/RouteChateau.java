package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.CastleDoor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class RouteChateau extends ARPGArea {
	
	@Override
	public String getTitle() {
		return "zelda/RouteChateau";
	}

	@Override
	protected void createArea() {
		this.registerActor(new Background(this));
		this.registerActor(new Foreground(this));
		
		this.registerActor(new Door("zelda/Route", new DiscreteCoordinates(9, 18),
				Logic.TRUE, this, Orientation.DOWN,
				new DiscreteCoordinates(9, 0), new DiscreteCoordinates(10, 0)));
		
		this.registerActor(new CastleDoor("zelda/Chateau", new DiscreteCoordinates(7, 1), this, Orientation.UP,
				new DiscreteCoordinates(9, 13), new DiscreteCoordinates(10, 13)));
	}
}
