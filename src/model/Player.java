package model;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Vector;

import bot.IA;
import control.InputObject;
import madness.Objective;

public class Player {
	public Vector<Objet> selection;

	
	//INPUTS TO BE APPLIED ON THIS ROUND
	public Vector<InputObject> inputs;
	
	public int id;
	private GameTeam gameteam;
	public String nickname;
	
//	public BottomBar bottomBar;
	
	public boolean isReady;
	
	//Network
	public InetAddress address;
	
	
	
	// IA
	public IA ia;
	
	
	public Player(int id,String name, GameTeam gameteam) {
		this.initialize(id, name, gameteam);
		
	
	}
	

	
	
	
	public void initialize(int id , String name , GameTeam gameteam){
		this.id = id;
		this.nickname = name;
		this.selection = new Vector<Objet>();
		this.gameteam = gameteam;
		
		this.inputs = new Vector<InputObject>();
	}
	public int getTeam(){
		return gameteam.id;
	}
	public GameTeam getGameTeam(){
		return gameteam;
	}
	public void setTeam(int team){
		
		this.gameteam = Game.g.teams.get(team);
	}
	
	public void action(){
		if(ia!=null){			
			this.ia.action();
		}	
		
		
//		// Handle madness objectives
//		Vector<Objective> toRemove = new Vector<ObjectiveMadness>();
//		for(Objective m : this.getGameTeam().getObjectiveMadness()){
//			m.action();
//			
//			if(m.isCompleted()){
//				// Add all tech choices 
//				this.getGameTeam().civ.cardSelection.addAll(m.techAllowed);
//				toRemove.add(m);
//			}
//		}
//
//		this.getGameTeam().successObjectives(toRemove);
	}
	
	public void initIA(IA ia){
		this.ia = ia;
		
	}
	public String toString(){
		String s ="";
		s+="id:"+id+";";
		s+="food:"+gameteam.food+";";
		return s;
		
	}
	public void parsePlayer(String s){
		String[] u1 = s.split("\\|");
		
		// Find the right player :
		String result = "";
		for(int i = 0;i<u1.length;i++){
			HashMap<String,String> hs = new HashMap<String,String>();
			String[] inter = u1[i].split(";");
			for(int j = 0; j<inter.length;j++){
				String[] r = inter[j].split(":");
				hs.put(r[0], r[1]);
			}
			if(hs.containsKey("id")){
				if(Integer.parseInt(hs.get("id"))==this.id){
					result = u1[i];
					break;
				}
			}
		}
		//SEPARATION BETWEEN KEYS
		
		String[] u = result.split(";");
		HashMap<String,String> hs = new HashMap<String,String>();
		for(int i=0;i<u.length;i++){
			String[] r = u[i].split(":");
			hs.put(r[0], r[1]);
		}
		if(hs.containsKey("food")){
			gameteam.food=Integer.parseInt(hs.get("food"));
		}

		if(hs.containsKey("team")){
			for(GameTeam t : Game.g.teams){
				if(t.id == Integer.parseInt(hs.get("team"))){
					this.gameteam = t;
					
				}
			}
		}
	}
	
	public void addToPlay(InputObject in){
		this.inputs.addElement(in);
	}

	public boolean equals(Player p){
		return this.id==p.id;
	}

	
}
