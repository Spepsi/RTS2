package events;

import java.util.Random;
import java.util.Vector;

import main.Main;
import model.Game;
import plateau.Objet;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Sound;

import utils.Utils;

public abstract class Event {

	Vector<Sound> sounds = new Vector<Sound>();
	Sound soundPlaying;
	EventNames name;
	int roundLaunched;
	public Objet parent;
	float power;
	float duration;
	boolean playSound = false;

	public Event(final Objet parent){
		this.parent = parent;
		this.roundLaunched = Game.g.round;
		// TODO : Make it generic with dataFile

	}

	public String toString(){
		return this.getName().name();
	}
	public boolean isNewEvent(){
		return Game.g.round-roundLaunched==0;
	}

	public int getGameTeam(){
		return parent.getTeam().id;
	}


	public abstract boolean play(Graphics g);


	//	public abstract void draw(Graphics g);


	public static Sound getRandomSound (Vector<Sound> v) {
		Random generator = new Random();
		//Filter by not is playing

		if(v.size()==1){
			return v.get(0);
		}
		int rnd = generator.nextInt(v.size() - 1);
		return v.get(rnd); // Cast the vector value into a String object
	}

	public void playSound(){
		if(this.sounds.size()>0 && !playSound){
			soundPlaying = getRandomSound(this.sounds);
			if(soundPlaying != null){
				if(this instanceof EventDeath){
					soundPlaying.play(1f,Game.g.options.soundVolume*ratioDistance(((EventDeath)this).x,((EventDeath)this).y));
				} else {
					soundPlaying.play(1f,Game.g.options.soundVolume*ratioDistance());
				}
			}
		playSound = true;
		}
	}


	protected float ratioDistance(){
		return Math.min(1f, Math.max(0f, (500f*Main.ratioSpace)/Utils.distance(parent.x, parent.y, (Game.g.Xcam+Game.g.resX/2), (Game.g.Ycam+Game.g.resY/2))));
	}
	protected float ratioDistance(float x, float y){
		return Math.min(1f, Math.max(0f, (500f*Main.ratioSpace)/Utils.distance(x, y, (Game.g.Xcam+Game.g.resX/2), (Game.g.Ycam+Game.g.resY/2))));
	}

	public EventNames getName(){
		return name;
	}

}
