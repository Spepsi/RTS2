package events;

import org.newdawn.slick.Graphics;

import plateau.Objet;
import plateau.Plateau;
import ressources.Sounds;

public strictfp class EventFrozen extends Event {

	public EventFrozen(Objet parent, Plateau plateau) {
		super(parent, plateau);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean play(Graphics g, Plateau plateau, boolean toDraw) {
		// TODO Auto-generated method stub
		Sounds.playSoundAt("ice_spell", parent.getX(), parent.getY());
		return false;
	}

}
