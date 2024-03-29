package ch.epfl.cs107.play.game.arpg.handler;

import ch.epfl.cs107.play.game.arpg.ARPGBehavior.ARPGCell;
import ch.epfl.cs107.play.game.arpg.actor.ARPGPlayer;
import ch.epfl.cs107.play.game.arpg.actor.Shop;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.Bomb;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.CastleDoor;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.CaveDoor;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.FireSpell;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.Grass;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.collectable.ArrowItem;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.collectable.Bow;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.collectable.CastleKey;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.collectable.Coin;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.collectable.Heart;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.collectable.Staff;
import ch.epfl.cs107.play.game.arpg.actor.areaentity.collectable.Sword;
import ch.epfl.cs107.play.game.arpg.actor.battle.monster.MonsterEntity;
import ch.epfl.cs107.play.game.arpg.actor.battle.weapon.Arrow;
import ch.epfl.cs107.play.game.arpg.actor.battle.weapon.MagicWaterProjectile;
import ch.epfl.cs107.play.game.arpg.actor.npc.King;
import ch.epfl.cs107.play.game.arpg.actor.npc.NPC;
import ch.epfl.cs107.play.game.arpg.actor.puzzle.Lever;
import ch.epfl.cs107.play.game.arpg.actor.puzzle.PressurePlate;
import ch.epfl.cs107.play.game.rpg.actor.Sign;
import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;

public interface ARPGInteractionVisitor extends RPGInteractionVisitor {

	default void interactWith(ARPGCell cell) {}
	
	default void interactWith(ARPGPlayer player) {}
	
	default void interactWith(Grass grass) {}

	default void interactWith(Bomb bomb) {}
	
	default void interactWith(Coin coin) {}
	
	default void interactWith(Heart heart) {}
	
	default void interactWith(CastleKey key) {}

	default void interactWith(CastleDoor castleDoor) {}
	
	default void interactWith(Bow bow) {}

	default void interactWith(Staff staff) {}
	
	default void interactWith(MonsterEntity monster) {}
	
	default void interactWith(Arrow arrow) {}
	
	default void interactWith(ArrowItem arrow) {}
	
	default void interactWith(MagicWaterProjectile projectile) {}
	
	default void interactWith(Lever activator) {}
	
	default void interactWith(Sword sword) {}

	default void interactWith(FireSpell fireSpell) {}

	default void interactWith(PressurePlate pressurePlate) {}

	default void interactWith(Sign sign) {}
	
	default void interactWith(King king) {}

	default void interactWith(CaveDoor caveDoor) {}

	default void interactWith(NPC npc) {}

	default void interactWith(Shop shopper) {}
	
}
