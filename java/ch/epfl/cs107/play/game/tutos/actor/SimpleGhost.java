package ch.epfl.cs107.play.game.tutos.actor;

import java.awt.Color;

import ch.epfl.cs107.play.game.actor.Entity;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class SimpleGhost extends Entity {
	
	private Sprite sprite;
	private float energy;
	private TextGraphics hpText;
	
	public SimpleGhost(Vector position, String spriteName) {
		super(position);
		sprite = new Sprite(spriteName, 1, 1.f, this);
		energy = 10;
		hpText = new TextGraphics(Integer.toString((int)energy), 0.4f, Color.BLUE);
		hpText.setParent(this);
		this.hpText.setAnchor(new Vector(-0.3f, 0.1f));
	}
	
	public boolean isWeak() {
		return energy <= 0;
	}
	
	public void strengthen() {
		energy = 10;
	}

	@Override
	public void draw(Canvas canvas) {
		sprite.draw(canvas);
		hpText.draw(canvas);
	}
	
	@Override
	public void update(float deltaTime) {
		if (energy > 0)
			energy -= deltaTime;
		else
			energy = 0;
		
		hpText.setText(Integer.toString((int)Math.ceil(energy)));
	}
	
	public void moveUp() {
		setCurrentPosition(getPosition().add(0.f, 0.05f));
	}
	
	public void moveDown() {
		setCurrentPosition(getPosition().add(0.f, -0.05f));
	}
	
	public void moveLeft() {
		setCurrentPosition(getPosition().add(-0.05f, 0.f));
	}
	
	public void moveRight() {
		setCurrentPosition(getPosition().add(0.05f, 0.f));
	}
}
