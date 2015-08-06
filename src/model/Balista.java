package model;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Circle;

public class Balista extends RangeWeapon{

	public Balista(Plateau p ,Character owner){
		// Parameters

		this.range = 200f;
		this.weight = 0.6f;
		this.state = 30f;
		this.chargeTime = 20f;
		this.damage = 8f;
		this.p = p;
		this.name = "Balista";
		this.lifePoints = 1f;
		p.addEquipmentObjets(this);
		this.collisionBox = new Circle(owner.getX(),owner.getY(),range);
		this.setOwner(owner);

	}


	public void action(){
		// Test if owner 
		if(this.state<this.chargeTime+2f){
			this.state += 0.1f;
		}
		if(this.owner==null){
			return;
		}
		// update x and y
		this.setXY(this.owner.getX(), this.owner.getY());
		// Test if target
		if(!(this.owner.getTarget() instanceof Character)){
			return;
		}
		Character target =(Character) this.owner.getTarget();
		if(this.owner.team==target.team){
			return;
		}
		if(target.lifePoints<=0f){
			this.owner.setTarget(null);
			this.state=0f;
			return;
		}
		// Launch bullet
		if(this.state>this.chargeTime){
			// Launch a bullet
			Circle circle = new Circle(this.getX(),this.getY(),this.range);

			if(target.collisionBox.intersects(circle)){
				new Fireball(this.p,this.owner,this.damage);
				this.state = 0f;
			}
		}
	}

}
