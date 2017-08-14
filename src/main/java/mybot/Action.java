package mybot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import bot.IA;
import bot.IAUnit;

public class Action {
	final static GsonBuilder builder = new GsonBuilder();
	public static final Gson gson = builder.create();
	Action parent;
	float x;
	float y;
	int target = -1;
	int subject = -1;
	public Verb action;
	public enum Verb{
		attack,
		move,
		produce,
		build,
	}
	List<IAUnit> complement;
	public static List<Action> parse(String data){
		Vector<Map<String, Object>> d = gson.fromJson(data, new Vector<HashMap<String, Object>>().getClass());
		List<Action> actions = new Vector<Action>();
		for(int i=0; i<d.size(); i++){
			actions.add(new Action(d.get(i)));
		}
		return actions;
	}
	public Action(Map<String, Object> toParse){

		for(String key : toParse.keySet()){
			System.out.println(key);
			switch(key){
				case "x":
					this.x = (float) toParse.get(key);
					break;
				case "y":
					this.x = (float) toParse.get(key);
					break;
				case "action":
					this.action = Verb.valueOf(((String) toParse.get(key)));
					break;
				case "target":
					this.target = (int) Math.round((double) toParse.get(key));
					break;
				case "subject" :
					this.subject  =  (int) Math.round((double)toParse.get(key));
					break;
			}
		}
	}
	public Action(){
		
	}

	public void play(IA ia){
		ia.select(subject);
		if(action==Verb.attack){
			ia.rightClick(target);							
		}
	}
}
