package spells;

import main.Main;
import model.Game;
import model.GameTeam;
import model.Objet;
import model.Plateau;
import multiplaying.ChatMessage;
import units.Character;

public class SpellConversion extends Spell{

	public float faithCost;

	public SpellConversion(Plateau p, GameTeam gameteam){
		this.chargeTime = 200f;
		this.faithCost = 2f;
		this.name = "Conversion";
		this.icon = p.g.images.get("spellConversion");
		this.range = 50f*Main.ratioSpace;
		this.damage = 0f;
		this.gameteam = gameteam;
		this.needToClick=true;
		this.p = p;
	}

	public void launch(Objet target, Character launcher){
		Objet t = p.findTarget(target.x, target.y,launcher.getTeam());
		if(t instanceof Character && t.getTeam()!=launcher.getTeam()){
			if(launcher.getGameTeam().special>=this.faithCost){
				((Character)t).changeTeam(launcher.getTeam());
				launcher.getGameTeam().special-=this.faithCost;
			} else {
				// Messages
				if(this.gameteam.id==this.p.g.currentPlayer.getTeam()){
						this.p.g.sendMessage(ChatMessage.getById("faith",Game.g));
				}
			}
		}
	}
}
