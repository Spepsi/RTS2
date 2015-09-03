package model;


import org.newdawn.slick.geom.Circle;

public class Spear extends ContactWeapon {
	
	
	public Spear(Plateau p ,Character owner){
		// Parameters
		this.weight = 0.2f;
		this.damage = 6f;
		this.chargeTime = 6f;
		float extraSize = 10f;
		
		//
		
		this.p = p;
		p.addEquipmentObjets(this);
		this.state = 0f;
		this.lifePoints = 1f;
		this.collisionBox = new Circle(owner.getX(),owner.getY(),owner.collisionBox.getBoundingCircleRadius()+extraSize);
		this.setOwner(owner);
		this.name = "Sword";
		this.sound = p.sounds.sword;
		
	}
	

	
}
