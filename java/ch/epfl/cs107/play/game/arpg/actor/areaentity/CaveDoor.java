package ch.epfl.cs107.play.game.arpg.actor.areaentity;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class CaveDoor extends Door {

    private Sprite doorClosed;
    private Sprite doorOpened;

    private boolean isOpened;

    /**
     * Constructor for the CaveDoor
     * @param destination the name of the area of the destination
	 * @param otherSideCoordinates the coordinates of the destination in the final area
	 * @param area the area
	 * @param orientation the orientation
	 * @param position the coordinates of the door in the area
	 * @param otherPositionsfor look position
	 */
    public CaveDoor(String destination, DiscreteCoordinates otherSideCoordinates, Area area,
                      Orientation orientation, DiscreteCoordinates position, DiscreteCoordinates ...otherPositions) {
        super(destination, otherSideCoordinates, Logic.TRUE, area, orientation, position, otherPositions);

        this.isOpened = false;

        doorOpened = new RPGSprite("zelda/cave.open", 1.5f, 1.5f, this, null, new Vector(-0.25f, 0));
        doorClosed = new RPGSprite("zelda/cave.close", 1.5f, 1.5f, this, null, new Vector(-0.25f, 0));
    }

    /**
	 * This method is used to change the state (opened, closed) of the door
	 * @return isOpened
	 */
    public void changeSignal() {
        isOpened = !isOpened;
    }

    @Override
    public boolean takeCellSpace() {
        return !isOpened;
    }

    @Override
    public boolean isViewInteractable() {
        return true;
    }

    @Override
    public void acceptInteraction(AreaInteractionVisitor v) {
        ((ARPGInteractionVisitor)v).interactWith(this);
    }

    @Override
    public void draw(Canvas canvas) {
        if (isOpened)
            doorOpened.draw(canvas);
        else
            doorClosed.draw(canvas);

        super.draw(canvas);
    }

}
