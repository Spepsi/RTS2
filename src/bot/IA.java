package bot;

import java.util.Vector;

import model.Game;
import model.GameTeam;
import model.Objet;
import model.Player;
import units.*;
import units.Character;
public class IA extends Player {
	
	/*
	 * Tous les objectifs sont r�alis�s en m�me temps mais ne peut r�alis� qu'un objectif � la fois
	 */
		
	public IA(int id, String name, GameTeam gameteam) {
		super(id, name, gameteam);
		
	}

	Vector<Objectif> strategy; // Liste de tous les objectifs
		
	public int team;
	
	/*
	 * Renvoie un objectif � r�aliser 
	 */
	public Objectif findObjective(){
		return null;
	}
	
	/*
	 * Assign people to missions
	 */
	public void assign(){
		
		return;
	}
	
	public Vector<Character> getUnits(){
		return Game.g.plateau.characters;
	}
	
	public Vector<Objet> get(String query){
		/*
		 * Create A Json query 
		 */
		Vector<Objet> result = new Vector<Objet>();
		
		
		return null;
		
	}
	
}
