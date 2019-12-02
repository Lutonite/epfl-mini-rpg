package ch.epfl.cs107.play.game.arpg.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Foreground;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.arpg.actor.Grass;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Route extends ARPGArea {
	
	@Override
	public String getTitle() {
		return "zelda/Route";
	}

	@Override
	protected void createArea() {
		this.registerActor(new Background(this));
		this.registerActor(new Foreground(this));
		
		this.registerActor(new Door("zelda/Ferme", new DiscreteCoordinates(18, 15),
				Logic.TRUE, this, Orientation.UP,
				new DiscreteCoordinates(0, 15), new DiscreteCoordinates(0, 16)));
		
		this.registerActor(new Door("zelda/Village", new DiscreteCoordinates(29, 18),
				Logic.TRUE, this, Orientation.DOWN,
				new DiscreteCoordinates(9, 0), new DiscreteCoordinates(10, 0)));

		for (int i = 5; i <= 7; i++) {
			for (int j = 6; j <= 11; j++) {
				this.registerActor(new Grass(this, Orientation.UP, new DiscreteCoordinates(i, j)));
			}
		}
	}
}
