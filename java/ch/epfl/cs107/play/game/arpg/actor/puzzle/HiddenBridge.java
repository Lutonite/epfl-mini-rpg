package ch.epfl.cs107.play.game.arpg.actor.puzzle;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Canvas;

public class HiddenBridge extends AreaEntity implements Actionable {

	/**
	 * Constructor for the HiddenBridge
	 * @param area the area
	 * @param orientation the orientation
	 * @param position the position in the area
	 */
	public HiddenBridge(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
		
		isActivated = false;
		hasSoundPlayed = true;
		
		sprite = new Sprite("zelda/bridge", 4.f, 3.f, this);
		sprite.setDepth(-Float.MAX_VALUE);
	}

	private Sprite sprite;
	private boolean isActivated;
	private boolean hasSoundPlayed;

	@Override
	public void draw(Canvas canvas) {
		if (isActivated)
			sprite.draw(canvas);
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(new DiscreteCoordinates(getCurrentMainCellCoordinates().x + 1, getCurrentMainCellCoordinates().y + 1));
	}

	@Override
	public boolean takeCellSpace() {
		return !isActivated;
	}

	@Override
	public boolean isCellInteractable() {
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		return false;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {}

	@Override
	public void activeEntity() {
		isActivated = !isActivated;
		hasSoundPlayed = false;
	}

	@Override
	public void bip(Audio audio) {
		if (!hasSoundPlayed) {
			hasSoundPlayed = true;
			audio.playSound(audio.getSound(ResourcePath.getSounds("transactionOk")), false, 0.65f, false, false, false);
		}
	}
}
