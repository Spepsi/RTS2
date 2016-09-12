package model;

import java.util.Vector;

import org.newdawn.slick.Color;

import data.Data;
import madness.ObjectiveImmolation;
import madness.ObjectiveMadness;

public class GameTeam {

	
	public Data data;
	
	public int id;
	public Civilisation civ;
	public String civName;
	public String colorName;
	public int food;
	public int gold;
	public int special;
	public int pop;
	public Building hq ;
	public int maxPop;
	public Color color;
	private int madness;
	
	// Objectives of madness
	private Vector<ObjectiveMadness> objectivesMadness = new Vector<ObjectiveMadness>();
	
	
	public GameTeam(Vector<Player> players, int id, int civ) {
		switch(civ){
		case 0 : civName = "Dualists"; this.civ = new Civilisation("dualists",this);this.objectivesMadness.addElement(new ObjectiveImmolation(this));break;
		case 1 : civName = "Zinaids";this.civ = new Civilisation("zinaids",this);break;
		case 2 : civName = "kitano";this.civ = new Civilisation("kitanos",this);break;
		default:
		}
		
		this.id = id;
		this.data = new Data(id,this.civ.name);
		if(id==0){
			color = Colors.team0;
			colorName = "neutral";
		}
		else if(id==1){
			color = Colors.team1;
			colorName = "blue";
		}
		else if(id==2){
			color = Colors.team2;
			colorName = "red";
		}
		
		this.maxPop = 15;
		this.gold = 50;
		this.food = 100;
	}
	
	public int getMadness(){
		return madness;
	}
	
	public void addMadness(int add){
		madness = madness+add;
	}
	
	public void successObjective(ObjectiveMadness om){
		objectivesMadness.remove(om);
	}
	public void successObjectives(Vector<ObjectiveMadness> om){
		objectivesMadness.removeAll(om);
	}
	public Vector<ObjectiveMadness> getObjectivesMadness(){
		return objectivesMadness;
	}
	
	
}
