package events;

import java.util.Vector;

import model.Objet;

import org.newdawn.slick.Graphics;

public class EventQueue {

	Vector<Event> events = new Vector<Event>();
	
	
	public EventQueue(){
		
	}
	

	
	public void render(Graphics g){
		
		Vector<Event> toRemove = new Vector<Event>();
		for(Event e : events){
			if(!e.play(g)){
				toRemove.addElement(e);
			}
		}
		events.removeAll(toRemove);
	}
	
	public void addEvent(Events name,Objet o){
		this.events.addElement(new Event(name,o));
		
	}
	
	
}
