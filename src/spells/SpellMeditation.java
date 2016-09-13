package spells;

import java.util.Vector;

import org.newdawn.slick.Graphics;

import control.InputObject;
import data.Attributs;
import events.Events;
import model.Character;
import model.Game;
import model.Objet;
import utils.ObjetsList;

public class SpellMeditation extends Spell{

	
	public SpellMeditation(){
		this.name = ObjetsList.Meditation;
	}

	public void launch(Objet target, Character launcher){
		launcher.addSpellEffect(new Meditation(launcher,target));
		launcher.canMove = false;
		Game.g.triggerEvent(Events.Meditation, launcher);
	}

	@Override
	public void drawCast(Graphics g, Objet target, float x, float y, Character launcher, boolean ok) {
		// TODO Auto-generated method stub
		
	}
}