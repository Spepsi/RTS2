package technologies;


import model.GameTeam;
import model.Plateau;
import units.Character;

public class DualistEagleView extends Technologie {

	public DualistEagleView(Plateau p, GameTeam gameteam) {
		this.id = 13;
		this.tech = Technologies.EagleView;
		this.initialize(p, gameteam,tech);
	}

	public void applyEffect(){
		// Va chercher le gameteam.data correspondant et ajoute le bonus ou ajoute tech concerné
		this.gameteam.data.knight.sight*=1.5;
		this.gameteam.data.priest.sight*=1.5;
		this.gameteam.data.inquisitor.sight*=1.5;
		this.gameteam.data.spearman.sight*=1.5;
		this.gameteam.data.crossbowman.sight*=1.5;
		// Age passing does nothing
		// Then update all existing units
		for(Character c : this.p.characters){
			if(c.getTeam() == this.gameteam.id){
				c.sight*=1.5;
			}

		}

	}
}
