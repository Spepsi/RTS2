package model;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Circle;

public class Bow extends RangeWeapon{
	
	public Bow(Plateau p,Character owner){
		this.p = p;
		this.weight = 0.2f;
		this.lifePoints = 1f;
		p.addEquipmentObjets(this);
		this.chargeTime = 5f;
		this.name = "Bow";
		this.state = this.chargeTime;
		this.range = 100f;
		this.damage = 3f;
		this.collisionBox = new Circle(owner.getX(),owner.getY(),this.range);
		this.setOwner(owner);

	}
	public void action(){
		if(this.state<this.chargeTime+2f){
			this.state += 0.1f;
		}
		// Test if owner 
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
				
				new Arrow(this.p,this.owner,this.damage);
				this.state = 0f;
			}
		}
	}
}
