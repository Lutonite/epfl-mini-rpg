package ch.epfl.cs107.play.game.arpg;

import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.arpg.actor.FlyableEntity;
import ch.epfl.cs107.play.window.Window;

public class ARPGBehavior extends AreaBehavior {

	public enum ARPGCellType {
		NULL(0, false),
		WALL(-16777216, false),
		IMPASSABLE(-8750470, false),
		INTERACT(-256, true),
		DOOR(-195580, true),
		WALKABLE(-1, true);
		
		final int type;
		final boolean isWalkable;
		
		ARPGCellType(int type, boolean isWalkable) {
			this.type = type;
			this.isWalkable = isWalkable;
		}
		
		private static ARPGCellType toType(int type) {
			for (ARPGCellType p : ARPGCellType.values()) {
				if (p.type == type) {
					return p;
				}
			}
			return NULL;
		}
	}
	
	/**
	 * Constructor for the ARPGBehavior
	 * @param window the window
	 * @param name the name of the window
	 */
	public ARPGBehavior(Window window, String name) {
		super(window, name);
		
		for (int x = 0; x < this.getWidth(); x++) {
			for (int y = 0; y < this.getHeight(); y++) {
				ARPGCellType cellType = ARPGCellType.toType(getRGB(getHeight()-1-y, x));
				this.setCell(x, y, new ARPGCell(x, y, cellType));
			}
		}
	}

	public class ARPGCell extends AreaBehavior.Cell {

		private final ARPGCellType type;
		
		/**
		 * Constructor for the ARPGCell
		 * @param x the coordinate x
		 * @param y the coordinate y
		 * @param type the type of the cell
		 * @see ARPGCellType
		 */
		private ARPGCell(int x, int y, ARPGCellType type) {
			super(x, y);
			this.type = type;
		}

		public boolean isDoor() { return type == ARPGCellType.DOOR; }

		@Override
		public boolean isCellInteractable() { return true; }
		@Override
		public boolean isViewInteractable() { return true; }
		@Override
		public void acceptInteraction(AreaInteractionVisitor v) {}
		@Override
		protected boolean canLeave(Interactable entity) { return true; }
		@Override
		protected boolean canEnter(Interactable entity) {
			if (entity instanceof FlyableEntity && ((FlyableEntity) entity).canFly())
				return type.isWalkable || type.equals(ARPGCellType.IMPASSABLE);
			else if (hasNonTraversableContent())
				return false;
			else
				return type.isWalkable;
		}
	}	
}
