package events;

import java.util.Vector;

import org.newdawn.slick.Graphics;

import control.Player;
import plateau.Objet;
import plateau.Character;

import plateau.Plateau;

public strictfp class EventHandler {

	private static Vector<Event> events = new Vector<Event>();
	private static boolean isInit;

	public static void init(){
		events = new Vector<Event>();
		isInit=true;
	}
	

	public static void render(Graphics g, Plateau plateau, boolean topLayer){
		Vector<Event> toRemove1 = new Vector<Event>();
		boolean isVisible;
		for(Event e : events){
			if(e.topLayer!=topLayer){
				continue;
			}
			isVisible = plateau.isVisibleByTeam(Player.team, e.parent);
			if(e.isDesynchro(plateau) || !e.play(g, plateau, isVisible)){
				toRemove1.addElement(e);
			}
		}
		events.removeAll(toRemove1);
		if(!topLayer){
			return;
		}
	}
	
	public static void addEvent(EventNames name,Objet parent, Plateau plateau){
		if(!isInit){
			return;
		}
		events.addElement(name.createEvent(parent, plateau));
	}
	
	public static void addEvent(Event event, Plateau plateau){
		if(plateau==null){
			return;
		}
		events.addElement(event);
	}
	
	public static void addEventBuildingTaking(Character parent, Objet building, Plateau plateau){
		if(!isInit){
			return;
		}
		EventBuildingTakingGlobal ebgt = null;
		for(Event e : events){
			if(e instanceof EventBuildingTakingGlobal && ((EventBuildingTakingGlobal)e).parent.getId()==building.getId()){
				ebgt = ((EventBuildingTakingGlobal)e);
				ebgt.addAttacker(parent.team.id);
				break;
			}
		}
		for(Event e : events){
			if(e instanceof EventBuildingTaking && e.parent.getId() == parent.getId() && ((EventBuildingTaking)e).idTarget == building.getId()){
				if(!((EventBuildingTaking)e).isActive){
					((EventBuildingTaking)e).isActive = true;
				}
				return;
			}
		}
		events.addElement(new EventBuildingTaking(parent, plateau, ebgt));
	}
	
}
