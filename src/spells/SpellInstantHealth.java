package spells;

import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Transform;

import model.ActionObjet;
import model.Checkpoint;
import model.Data;
import units.Character;
import model.Objet;
import model.Plateau;
import model.Player;
import model.Utils;

public class SpellInstantHealth extends Spell{

	public float remainingTime;
	public float width;

	public SpellInstantHealth(Plateau p, Player player){
		this.p = p;
		this.chargeTime = 450f;
		this.width = 15f;
		this.name = "Instant Health";
		this.icon = p.images.spellFirewall;
		this.range = 200f;
		this.damage = 1f;
		this.remainingTime = 250f;
		this.player = player;
		this.needToClick=true;
	}

	public void launch(Objet target, Character launcher){
		// Check if target intersect an ennemy
		Objet h = target;
		
		for(Character c : p.characters){
			if(c.collisionBox.contains(target.collisionBox)){
				h =c;
			}
		}

		if(h instanceof Character && target.team==launcher.team){
			h.lifePoints = ((Character) h ).maxLifePoints;
			
		}
	}

}


