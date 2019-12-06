package ch.epfl.cs107.play.game.arpg.actor.collectable;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class CoinCollectable extends CollectableAreaEntity {
	
	public CoinCollectable(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position, "zelda/coin");
	}
	
	public int getMoneyBack() {
		return 50;
	}
	
	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor)v).interactWith(this);
    }
}
