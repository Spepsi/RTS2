package plateau;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Circle;
import org.newdawn.slick.geom.Shape;

import data.Attributs;
import data.AttributsChange;
import events.EventAttackDamage;
import events.EventHandler;
import events.EventNames;
import main.Main;
import model.Game;
import ressources.Images;
import utils.ObjetsList;

public strictfp class BurningArea extends SpellEffect{

	public float remainingTime = 200f;
	public float totalTime = 200f;
	public float startTime = 0.03f;
	public float endTime = 0.3f;
	public float size;

	public BurningArea(int launcher, Objet fireball, float size, Plateau plateau){
		super(plateau);
		this.setName(ObjetsList.BurningAreaEffect);
		this.type = 2;
		this.size = size;
		this.toDrawOnGround = true;
		this.setLifePoints(1f);
		plateau.addSpell(this);
		this.team = plateau.getById(launcher).getTeam();
		this.setCollisionBox(createShape(fireball, size));
		this.setX(fireball.getX());
		this.setY(fireball.getY());
		this.remainingTime = this.totalTime;
		EventHandler.addEvent(EventNames.BurningArea, this, plateau);
	}
	
	public static Shape createShape(Objet t, float size){
		return new Circle(t.getX(),t.getY(),size);
	}



	public void action(Plateau plateau){
		this.remainingTime-=10f*Main.increment;
		if(this.remainingTime<=0f){
			this.setLifePoints(-1f);
		}
	}

	public void collision(Character c, Plateau plateau){
		if(this.getLifePoints()>0 && c.getTeam()!=this.team && plateau.getRound()%20==0){
			c.setLifePoints(c.getLifePoints()-1f, plateau);
		}
	}

}
