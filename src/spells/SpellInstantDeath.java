package spells;

import main.Main;
import model.Game;
import model.GameTeam;
import model.Objet;
import model.Plateau;
import model.Player;
import units.Character;
import utils.Utils;

public class SpellInstantDeath extends Spell{

	public float remainingTime;
	public float width;

	public SpellInstantDeath(GameTeam gameteam){
		this.chargeTime = 450f;
		this.width = 15f*Main.ratioSpace;
		this.name = "Instant Death";
		this.icon = Game.g.images.get("spellInstantDeath");
		this.range = 200f*Main.ratioSpace;
		this.damage = 1f;
		this.remainingTime = 250f;
		this.gameteam = gameteam;
		this.needToClick=true;
	}

	public void launch(Objet target, Character launcher){
		// Check if target intersect an ennemy
		Objet h = target;
		
		for(Character c : Game.g.plateau.characters){
			if(c.collisionBox.contains(target.collisionBox)){
				h =c;
			}
		}

		if(h instanceof Character && h.getTeam()!=launcher.getTeam() && launcher!=h && this.range>=Utils.distance(h, launcher)){
			h.lifePoints = 0f;
			
		}
	}

}


