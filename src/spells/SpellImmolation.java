package spells;

import java.util.Vector;

import org.newdawn.slick.Graphics;

import control.InputObject;
import data.Attributs;
import events.Events;
import model.Game;
import model.Objet;
import units.Character;
import utils.ObjetsList;

public class SpellImmolation extends Spell{

	
	public SpellImmolation(){
		this.name = ObjetsList.Immolation;
	}

	public void launch(Objet target, Character launcher){

		launcher.isImmolating = true;
		launcher.remainingTime = this.getAttribut(Attributs.totalTime);
		launcher.spells = new Vector<ObjetsList>();
		Game.g.events.addEvent(Events.Immolation, target);
	}


	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok) {
		// TODO Auto-generated method stub
		
	}
}
