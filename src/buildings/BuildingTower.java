package buildings;

import java.util.Vector;

import org.newdawn.slick.geom.Rectangle;

import bullets.Fireball;
import model.Checkpoint;
import model.Game;
import model.Plateau;
import model.Utils;
import units.Character;

public class BuildingTower extends Building{


	public float chargeTime;
	public boolean canAttack;
	public Character target;
	public float damage;

	public BuildingTower(Plateau p,Game g,float x, float y){
		p.addBuilding(this);
		this.constructionPoints = 0f;
		teamCapturing = 0;
		this.x = x;
		this.y = y;
		this.p =p;
		this.g =g;
		this.setTeam(0);
		this.damage = 20f;
		this.maxLifePoints = 20f;
		this.lifePoints = this.maxLifePoints;
		this.chargeTime = 5f;
		this.sizeX = 220f; 
		this.sizeY = 220f;
		this.collisionBox= new Rectangle(0,0,sizeX,sizeY);
		this.selection_circle = this.p.g.images.selection_rectangle.getScaledCopy(4f);
		this.selectionBox = this.collisionBox;
		this.setXY(x, y);
		this.image = this.p.g.images.tent;
		this.imageNeutre = this.p.g.images.tent;
		
		this.sight = 300f;
		this.rallyPoint = new Checkpoint(p,this.x,this.y+this.sizeY/2);
		this.updateImage();
		canAttack = false;
	}

	public void action(){
		if(!this.canAttack)
			this.setCharge(this.charge+0.1f);
		if(this.charge>this.chargeTime && this.getGameTeam().id!=0){
			this.canAttack = true;
			this.charge = 0f;

		}

		if(canAttack){
			if(target==null){
				Vector<Character> target= this.p.getEnnemiesInSight(this);
				if(target.size()>0){
					this.target = target.get(0);
				}
			}

			//Launch a fireball on target
			if(target!=null && Utils.distance(this, this.target)<this.sight){
				new Fireball(this.p,this,this.getTarget().getX(),this.getTarget().getY(),this.getTarget().getX()-this.getX(),this.getTarget().getY()-this.getY(),this.damage,-1);
				this.canAttack= false;
				this.charge = 0f;
			}
		}

	}

	public Character getTarget(){
		return this.target;
	}
}
