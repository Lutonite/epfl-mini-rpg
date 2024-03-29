package ch.epfl.cs107.play.game.arpg.actor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import ch.epfl.cs107.play.game.actor.SoundAcoustics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.arpg.Test;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.CaveDoor;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.Grass;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.collectable.ArrowItem;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.collectable.Bow;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.collectable.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.collectable.Coin;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.collectable.Heart;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.collectable.Staff;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.collectable.Sword;
import ch.epfl.cs107.play.game.arpg.actor.battle.DamageType;
import ch.epfl.cs107.play.game.arpg.actor.battle.monster.MonsterEntity;
import ch.epfl.cs107.play.game.arpg.actor.battle.weapon.Arrow;
import ch.epfl.cs107.play.game.arpg.actor.battle.weapon.MagicWaterProjectile;
import ch.epfl.cs107.play.game.arpg.actor.battle.weapon.Projectile;
import ch.epfl.cs107.play.game.arpg.actor.npc.King;
import ch.epfl.cs107.play.game.arpg.actor.npc.NPC;
import ch.epfl.cs107.play.game.arpg.actor.puzzle.PressurePlate;
import ch.epfl.cs107.play.game.arpg.area.ARPGArea;
import ch.epfl.cs107.play.game.arpg.handler.ARPGInteractionVisitor;
import ch.epfl.cs107.play.game.keybindings.KeyboardAction;
import ch.epfl.cs107.play.game.keybindings.KeyboardEventListener;
import ch.epfl.cs107.play.game.keybindings.KeyboardEventRegister;
import ch.epfl.cs107.play.game.keybindings.StaticKeyboardEventListener;
import ch.epfl.cs107.play.game.inventory.Inventory;
import ch.epfl.cs107.play.game.inventory.InventoryItem;
import ch.epfl.cs107.play.game.rpg.actor.Dialog;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.rpg.actor.Sign;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Canvas;

public class ARPGPlayer extends Player implements Inventory.Holder {

	private enum Behavior {
		IDLE,
		ATTACK_WITH_SWORD,
		ATTACK_WITH_BOW,
		ATTACK_WITH_STAFF,
		DEAD
	}
	
	private final static int ANIMATION_DURATION = 2;
	private final static int BASE_MONEY = 100;
	private final static float INVINCIBILITY_TIME = 1.5f;
	private final static float SPEED_BOW = 0.8f;
	private final static float SPEED_STAFF = 0.8f;

	// ARPG Stuff
	private ARPGPlayerHandler handler;
	private ARPGInventory inventory;
    private ARPGPlayerStatusGUI gui;
    private ARPGInventoryGUI inventoryGui;
    private ARPGItem currentHoldingItem;

    // Animations
	private final Animation[] animationsIdle;
	private final Animation[] animationsWithBow;
	private final Animation[] animationsWithStaff;
	private final Animation[] animationsWithSword;

	// Keyboard events
    private KeyboardEventRegister keyboardRegister;

	// Attributes
	private float hp;
	private float maxHp;
	private float invicibilityTime;
	private float lastTookDamage;
	private boolean isInventoryOpen;
    private boolean didDamage;
    private float speedBow;
    private float speedStaff;
    private Behavior behavior;
    private Dialog dialog;
    private boolean isDialog;
    private boolean hasDamageSoundActivated;
    private boolean shownGameOverForeground;

    private Shop currentShop;

	// Keyboard Events used for the player
	private class CycleItemKeyEventListener implements StaticKeyboardEventListener {
		@Override
		public void onKeyEvent() {
			cycleCurrentInventoryItem();
		}
	}
	
	private class OpenInventoryEventListener implements StaticKeyboardEventListener {
		@Override
		public void onKeyEvent() {
			if (Objects.nonNull(currentShop) && currentShop.isShopOpened()) {
				currentShop.closeShop();
			} else {
				isInventoryOpen = !isInventoryOpen;
			}
		}
	}

	private class UseInventoryKeyItemEventListener implements StaticKeyboardEventListener {
		@Override
		public void onKeyEvent() {
			if (!isDisplacementOccurs())
				useInventoryItem();
		}

		@Override
		public void onKeyReleasedEvent(KeyboardAction previousAction) {
			if (currentHoldingItem != null && currentHoldingItem.equals(ARPGItem.BOW) && behavior.equals(Behavior.ATTACK_WITH_BOW)) {
				Arrow arrow = new Arrow(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
				if (possess(ARPGItem.ARROW) && speedBow >= SPEED_BOW && getOwnerArea().canEnterAreaCells(arrow, getFieldOfViewCells())) {
					if (inventory.getAmountOf(ARPGItem.ARROW) >= 1) {
						getOwnerArea().registerActor(arrow);
						inventory.removeEntry(ARPGItem.ARROW, 1);
					}
				}
				speedBow = 0;
				behavior = Behavior.IDLE;
				animationsWithBow[getOrientation().opposite().ordinal()].reset();
			} else if (currentHoldingItem != null && currentHoldingItem.equals(ARPGItem.STAFF) && behavior.equals(Behavior.ATTACK_WITH_STAFF)) {
				Projectile projectile = new MagicWaterProjectile(getOwnerArea(), getOrientation(), getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
				if (speedStaff >= SPEED_STAFF && getOwnerArea().canEnterAreaCells(projectile, getFieldOfViewCells()))
					getOwnerArea().registerActor(projectile);
				behavior = Behavior.IDLE;
				animationsWithStaff[getOrientation().opposite().ordinal()].reset();
			}
		}
	}

	private class MoveOrientateKeyEventListener implements KeyboardEventListener {
		@Override
		public List<KeyboardAction> getActions() {
			return Arrays.asList(
				KeyboardAction.MOVE_DOWN,
				KeyboardAction.MOVE_LEFT,
				KeyboardAction.MOVE_UP,
				KeyboardAction.MOVE_RIGHT
			);
		}

		@Override
		public void onKeyEvent(KeyboardAction action) {
			if (!isInventoryOpen && !(Objects.nonNull(currentShop) && currentShop.isShopOpened())) {
				switch (action) {
					case MOVE_LEFT:
						moveOrientate(Orientation.LEFT);
						break;
					case MOVE_UP:
						moveOrientate(Orientation.UP);
						break;
					case MOVE_RIGHT:
						moveOrientate(Orientation.RIGHT);
						break;
					case MOVE_DOWN:
						moveOrientate(Orientation.DOWN);
						break;
					default:
						break;
				}
			}
		}
	}

	private class InventoryKeyEventListener implements KeyboardEventListener {
		@Override
		public List<KeyboardAction> getActions() {
			return Arrays.asList(
					KeyboardAction.MOVE_DOWN,
					KeyboardAction.MOVE_LEFT,
					KeyboardAction.MOVE_UP,
					KeyboardAction.MOVE_RIGHT,
					KeyboardAction.ACCEPT_DIALOG
			);
		}

		@Override
		public void onKeyEvent(KeyboardAction action) {
			if (action == KeyboardAction.ACCEPT_DIALOG && !Behavior.DEAD.equals(behavior)) {
				if (Objects.nonNull(currentShop) && currentShop.isShopOpened()) {
					currentShop.buy(inventory);
				}
			} else {
				if (isInventoryOpen) {
					inventoryGui.selectionUpdate(action);
				} else if (Objects.nonNull(currentShop) && currentShop.isShopOpened()) {
					currentShop.selectionUpdate(action);
				}
			}
		}
	}

	private class CheatKeysEventListener implements StaticKeyboardEventListener {
		@Override
		public void onKeyEvent() {
			if (Test.MODE) {
				getOwnerArea().registerActor(new Bomb(getOwnerArea(), Orientation.UP, getCurrentMainCellCoordinates().jump(getOrientation().toVector())));
			}
		}
	}

	private class AcceptDeathDialogEventListener implements StaticKeyboardEventListener {
		@Override
		public void onKeyEvent() {
			if (Behavior.DEAD.equals(behavior)) {
				if (getOwnerArea() instanceof  ARPGArea) ((ARPGArea)getOwnerArea()).restart();
			}
		}
	}

	/**
	 * Constructor for the ARPGPlayer
	 * @param area the area
	 * @param orientation the orientation
	 * @param coordinates the coordinates in the area
	 */
	public ARPGPlayer(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		super(area, orientation, coordinates);

		handler = new ARPGPlayerHandler();
		inventory = new ARPGInventory(BASE_MONEY);
		maxHp = 5.f;
		hp = maxHp;
		invicibilityTime = 0;
		lastTookDamage = 0;
		isInventoryOpen = false;
		didDamage = false;
		speedBow = 0;
		speedStaff = 0;
		behavior = Behavior.IDLE;
		isDialog = false;
		hasDamageSoundActivated = true;
		shownGameOverForeground = false;
		
		keyboardRegister = new KeyboardEventRegister(getOwnerArea().getKeyboard());
		keyboardRegister.registerKeyboardEvent(KeyboardAction.CYCLE_INVENTORY, new CycleItemKeyEventListener());
		keyboardRegister.registerKeyboardEvent(KeyboardAction.USE_CURRENT_ITEM, new UseInventoryKeyItemEventListener());
		keyboardRegister.registerKeyboardEvent(KeyboardAction.OPEN_INVENTORY, new OpenInventoryEventListener());
		keyboardRegister.registerKeyboardEvent(KeyboardAction.CHEAT_SPAWN_BOMB, new CheatKeysEventListener());
		keyboardRegister.registerKeyboardEvent(KeyboardAction.ACCEPT_DIALOG, new AcceptDeathDialogEventListener());
		keyboardRegister.registerKeyboardEvents(new MoveOrientateKeyEventListener(),true);
		keyboardRegister.registerKeyboardEvents(new InventoryKeyEventListener());

		if (!inventory.addEntry(ARPGItem.BOMB, 3)) System.out.println("Base inventory items could not be added.");
		cycleCurrentInventoryItem();

		if (Test.MODE) {
			inventory.addEntry(ARPGItem.CASTLE_KEY, 1);
			inventory.addEntry(ARPGItem.STAFF, 1);
			inventory.addEntry(ARPGItem.BOW, 1);
			inventory.addEntry(ARPGItem.ARROW, 99);
			inventory.addEntry(ARPGItem.SWORD, 1);
			inventory.addEntry(ARPGItem.BOMB, 96);
			inventory.addMoney(9899);
		}

		Sprite[][] sprites = RPGSprite.extractSprites("zelda/player", 4, 1, 2, this , 16, 32, new Orientation[]
			{Orientation.UP , Orientation.LEFT , Orientation.DOWN, Orientation.RIGHT});
		animationsIdle = RPGSprite.createAnimations(ANIMATION_DURATION, sprites);
		
		Sprite[][] spritesWithBow = RPGSprite.extractSprites("zelda/player.bow", 4, 2, 2, this , 32, 32, new Vector(-0.5f, 0), new Orientation[]
			{Orientation.UP , Orientation.DOWN , Orientation.LEFT, Orientation.RIGHT});
		animationsWithBow = RPGSprite.createAnimations(ANIMATION_DURATION * 2, spritesWithBow, false);
		
		Sprite[][] spritesWithStaff = RPGSprite.extractSprites("zelda/player.staff_water", 4, 2, 2, this , 32, 32, new Vector(-0.5f, 0), new Orientation[]
				{Orientation.UP , Orientation.DOWN , Orientation.LEFT, Orientation.RIGHT});
		animationsWithStaff = RPGSprite.createAnimations(ANIMATION_DURATION * 2, spritesWithStaff, false);
		
		Sprite[][] spritesWithSword = RPGSprite.extractSprites("zelda/player.sword", 4, 2, 2, this , 32, 32, new Vector(-0.5f, 0), new Orientation[]
				{Orientation.UP , Orientation.DOWN , Orientation.LEFT, Orientation.RIGHT});
		animationsWithSword = RPGSprite.createAnimations(ANIMATION_DURATION, spritesWithSword, false);

		gui = new ARPGPlayerStatusGUI(this);
		inventoryGui = new ARPGInventoryGUI(this.inventory, "Inventory");
	}

	/**
	 * This method is used to change the current selected item
	 */
	private void cycleCurrentInventoryItem() {
		List<InventoryItem> list = inventory.getItemList();
		list.removeIf(item -> item.equals(ARPGItem.ARROW));
		if (list.size() == 0) currentHoldingItem = null;
		else {
			int cur = list.indexOf(currentHoldingItem);
			if (cur == list.size() - 1) currentHoldingItem = (ARPGItem) list.get(0);
			else currentHoldingItem = (ARPGItem) list.get(cur + 1);
		}
	}

	/**
	 * This method is used to active the current selected item
	 */
	private void useInventoryItem() {
		if (currentHoldingItem == null) return;
		switch (currentHoldingItem) {
			case BOMB:
                Bomb bomb = new Bomb(getOwnerArea(), Orientation.UP, getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
                if (getOwnerArea().canEnterAreaCells(bomb, Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector())))) {
                    getOwnerArea().registerActor(bomb);
                    inventory.removeEntry(ARPGItem.BOMB, 1);
                    if (!possess(ARPGItem.BOMB)) cycleCurrentInventoryItem();
                }
				break;
			case BOW:
				behavior = Behavior.ATTACK_WITH_BOW;
				didDamage = false;
				break;
			case SWORD:
				behavior = Behavior.ATTACK_WITH_SWORD;
				didDamage = false;
				break;
			case STAFF:
				behavior = Behavior.ATTACK_WITH_STAFF;
				didDamage = false;
				break;
			case CASTLE_KEY:
			default:
				break;
		}
	}
	
	/**
	 * This method is used to move and orientate the player
	 * @param orientation the orientation
	 */
	private void moveOrientate(Orientation orientation){
		if (getOrientation() == orientation)
			move(ANIMATION_DURATION * 2);
		else orientate(orientation);
	}

	/**
	 * This method is used to handle when the player took a hit
	 * @param damage the damage
	 */
	public final void takeDamage(float damage) {
		if (lastTookDamage <= 0) {
			if (hp - damage > 0) {
				hp -= damage;
				lastTookDamage = damage;
				invicibilityTime = INVINCIBILITY_TIME;
				hasDamageSoundActivated = false;
			} else {
				lastTookDamage = hp;
				hp = 0;
				behavior = Behavior.DEAD;
			}
		}
	}
	
	/**
	 * This method is used to handle when the player took a hit of 0.5 heart
	 */
	public final void takeDamage() { takeDamage(0.5f); }

	/**
	 * Return last took damage
	 * @return last took damage
	 */
	public float tookDamage() { return lastTookDamage; }

	// INVENTORY
	
	/**
	 * return the current selected item
	 * @return the current selected item
	 */
	public ARPGItem getCurrentItem() { return currentHoldingItem; }
	
	/**
	 * Return the money in the inventory
	 * @return the money in the inventory
	 */
	public int getInventoryMoney() {
		return inventory.getMoney();
	}
	
	/**
	 * Return the amount of hp
	 * @return ther amount of hp
	 */
	public float getHp() { return hp; }
	
	/**
	 * Return the amount of the maximum hp
	 * @return the amount of the maximum hp
	 */
	public float getMaxHp() { return maxHp; }

	/**
	 * This method is used to add hp to the player
	 * @param hp the amount of hp
	 */
	private void addHp(float hp) {
		this.hp += hp;
		
		if (this.hp > maxHp)
			this.hp = maxHp;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		keyboardRegister.update();

		if (!behavior.equals(Behavior.DEAD)) {
			if (behavior.equals(Behavior.ATTACK_WITH_BOW)) {
				speedBow += deltaTime;
				animationsWithBow[getOrientation().opposite().ordinal()].update(deltaTime);
			} else if (behavior.equals(Behavior.ATTACK_WITH_STAFF)) {
				speedStaff += deltaTime;
				animationsWithStaff[getOrientation().opposite().ordinal()].update(deltaTime);
			} else if (behavior.equals(Behavior.ATTACK_WITH_SWORD)) {
				animationsWithSword[getOrientation().opposite().ordinal()].update(deltaTime);
			}

			if (isDisplacementOccurs()) {
				isDialog = false;
				behavior = Behavior.IDLE;
				animationsWithSword[getOrientation().opposite().ordinal()].reset();
				animationsIdle[getOrientation().opposite().ordinal()].update(deltaTime);
			} else {
				animationsIdle[getOrientation().opposite().ordinal()].reset();
			}

			if (currentHoldingItem == null && inventory.getItemList().size() > 0) {
				cycleCurrentInventoryItem();
			}

			// System.out.println(getCurrentMainCellCoordinates());

			if (invicibilityTime > 0) {
				invicibilityTime -= deltaTime;
			} else if (invicibilityTime < 0) {
				invicibilityTime = 0;
				lastTookDamage = 0;
			}

			if (Objects.nonNull(currentShop) && !currentShop.isShopOpened()) {
				currentShop = null;
			}

		} else {
			if (!shownGameOverForeground) {
				getOwnerArea().registerActor(new GameOver(getOwnerArea()));
				shownGameOverForeground = true;
			}
		}
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates().jump(getOrientation().toVector()));
	}

	@Override
	public boolean takeCellSpace() {
		return true;
	}

	@Override
	public boolean isCellInteractable() {
		return true;
	}

	@Override
	public boolean isViewInteractable() {
		return true;
	}

	@Override
	public void draw(Canvas canvas) {
		if (!Behavior.DEAD.equals(behavior)) {
			if (behavior.equals(Behavior.ATTACK_WITH_BOW))
				animationsWithBow[getOrientation().opposite().ordinal()].draw(canvas);
			else if (behavior.equals(Behavior.ATTACK_WITH_STAFF))
				animationsWithStaff[getOrientation().opposite().ordinal()].draw(canvas);
			else if (behavior.equals(Behavior.ATTACK_WITH_SWORD) && !animationsWithSword[getOrientation().opposite().ordinal()].isCompleted())
				animationsWithSword[getOrientation().opposite().ordinal()].draw(canvas);
			else {
				behavior = Behavior.IDLE;
				animationsIdle[getOrientation().opposite().ordinal()].draw(canvas);
				animationsWithSword[getOrientation().opposite().ordinal()].reset();
			}

			if (isInventoryOpen) {
				inventoryGui.draw(canvas);
			}
			if (!(Objects.nonNull(currentShop) && currentShop.isShopOpened()) && !isInventoryOpen && !isDialog) {
				gui.draw(canvas);
			}
			if (isDialog && !(Objects.nonNull(currentShop) && currentShop.isShopOpened()) && !isInventoryOpen) {
				dialog.draw(canvas);
			}
		}
	}

	@Override
	public boolean wantsCellInteraction() {
		return true;
	}

	@Override
	public boolean wantsViewInteraction() {
		return (
				!isInventoryOpen && !(Objects.nonNull(currentShop) && currentShop.isShopOpened()) &&
				(KeyboardAction.VIEW_INTERACTION.getAssignedButton(getOwnerArea().getKeyboard()).isPressed() ||
						behavior.equals(Behavior.ATTACK_WITH_SWORD))
		);
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((ARPGInteractionVisitor)v).interactWith(this);
	}

	@Override
	public boolean possess(InventoryItem item) {
		return inventory.isInInventory(item);
	}

	private class ARPGPlayerHandler implements ARPGInteractionVisitor {
		
		@Override
		public void interactWith(Door door) {
			setIsPassingADoor(door);
		}
		
		@Override
		public void interactWith(CastleDoor castleDoor) {
			if (wantsViewInteraction()) {
				if (currentHoldingItem.equals(ARPGItem.CASTLE_KEY))
					castleDoor.changeSignal();
			} else {
				setIsPassingADoor(castleDoor);
				castleDoor.changeSignal();
			}
		}

		@Override
		public void interactWith(CaveDoor caveDoor) {
			if (!wantsViewInteraction()) setIsPassingADoor(caveDoor);
 		}

		@Override
		public void interactWith(Grass grass) {
			if (behavior.equals(Behavior.ATTACK_WITH_SWORD))
				grass.cut(true);
		}

		@Override
		public void interactWith(MonsterEntity monster) {
			if (behavior.equals(Behavior.ATTACK_WITH_SWORD) && !didDamage) {
				monster.takeDamage(DamageType.PHYSICAL, 0.5f);
				didDamage = true;
			}
		}

		@Override
		public void interactWith(Coin coin) {
			inventory.addMoney(coin.getMoneyBack());
			getOwnerArea().unregisterActor(coin);
		}
		
		@Override
		public void interactWith(Heart heart) {
			if (hp < maxHp) {
				addHp(heart.getHeartBack());
				getOwnerArea().unregisterActor(heart);
			}
		}
		
		@Override
		public void interactWith(CastleKey key) {
			inventory.addEntry(ARPGItem.CASTLE_KEY, 1);
			getOwnerArea().unregisterActor(key);
		}
		
		@Override
		public void interactWith(Bow bow) {
			inventory.addEntry(ARPGItem.BOW, 1);
			getOwnerArea().unregisterActor(bow);
		}
		
		@Override
		public void interactWith(Staff staff) {
			inventory.addEntry(ARPGItem.STAFF, 1);
			getOwnerArea().unregisterActor(staff);
		}
		
		@Override
		public void interactWith(Sword sword) {
			inventory.addEntry(ARPGItem.SWORD, 1);
			sword.collect();
		}
		
		@Override
		public void interactWith(ArrowItem arrow) {
			inventory.addEntry(ARPGItem.ARROW, 1);
			getOwnerArea().unregisterActor(arrow);
		}

		@Override
		public void interactWith(PressurePlate pressurePlate) {
			pressurePlate.active();
		}

		@Override
		public void interactWith(Sign sign) {
			if (!isDialog) {
				dialog = new Dialog(sign.getTextMessage(), "zelda/dialog", getOwnerArea());
				isDialog = true;
			}
		}
		
		@Override
		public void interactWith(King king) {
			if (!isDialog) {
				king.setOrientation(getOrientation().opposite());
				dialog = new Dialog(king.getTextMessage(), "zelda/dialog", getOwnerArea());
				isDialog = true;
			}
		}
		
		@Override
		public void interactWith(NPC npc) {
			if (!isDialog) {
				npc.setOrientation(getOrientation().opposite());
				dialog = new Dialog(npc.getTextDialog(), "zelda/dialog", getOwnerArea());
				isDialog = true;
				npc.talked();
			}
		}

		@Override
		public void interactWith(Shop shopper) {
			if (wantsViewInteraction()) {
				shopper.openShop();
				currentShop = shopper;
			}
		}
	}

	@Override
	public void enterArea(Area area, DiscreteCoordinates position) {
		super.enterArea(area, position);
		if (area instanceof ARPGArea) ((ARPGArea)area).reactivateSounds();
	}

	@Override
	public void bip(Audio audio) {
		if (isPassingADoor()) {
			SoundAcoustics.stopAllSounds(audio);
		}

		if (!hasDamageSoundActivated) {
			audio.playSound(audio.getSound(ResourcePath.getSounds("custom/sw.damage")), false, 0.75f, false, false, false);
			hasDamageSoundActivated = true;
		}
	}

}
