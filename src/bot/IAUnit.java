package bot;

import java.util.Vector;

import data.Attributs;
import model.Building;
import model.Objet;
import model.Character;
import utils.ObjetsList;

public class IAUnit {

	
	protected Objet objet;
	private IA ia;
	
	
	public IAUnit(Objet o, IA ia){
		this.objet = o;
		this.ia = ia;

	}

	public ObjetsList getName(){
		return objet.name;
	}
	public Class<? extends Objet> getType(){
		return objet.getClass();
	}
	
	public float getX(){
		return objet.getX();
	}
	public float getY(){
		return objet.getY();
	}
	public int getId(){
		return objet.id;
	}
	public float getLifepoints(){
		return objet.lifePoints;
	}
	public int getGameTeam(){
		return this.objet.getGameTeam().id;
	}
	Objet getObjet(){
		return this.objet;
	}
	public float getAttribut(Attributs a){
		return objet.getAttribut(a);
	}
	public Vector<String> getAttributList(Attributs a){
		return objet.getAttributList(a);
	}
	public String getAttributString(Attributs a){
		return objet.getAttributString(a);
	}
	
	public boolean clickIn(float x , float y){
		return objet.collisionBox.contains(x, y);
	}
	
	public IA getIA(){
		return ia;
	}
	public Vector<ObjetsList> getProductionList(){
		
		if(objet instanceof Building){
			return ((Building) objet).getProductionList();
		}
		return new Vector<ObjetsList>();
	}
	public Vector<ObjetsList> getResearchList(){
		
		if(objet instanceof Building){
			return ((Building) objet).getTechnologyList();
		}
		return new Vector<ObjetsList>();
	}
	
	public static float distance(IAUnit u1,IAUnit u2){
		return (float) Math.sqrt((u1.getX()-u2.getX())*(u1.getX()-u2.getX())+(u1.getY()-u2.getY())*(u1.getY()-u2.getY()));
	}
	
	
	public Vector<ObjetsList> getSpells(){
		if(getObjet() instanceof Character){
			return getObjet().getSpellsName();
		}
		return new Vector<ObjetsList>();
	}
	
	
	public IAUnit getNearestNeutral(ObjetsList o){
		Vector<IAUnit> enemies = getIA().getNature();
		float minDist = -1;
		IAUnit best =null;
		for(IAUnit enemy : enemies){
			float dist = IAUnit.distance(enemy, this);
			if(enemy.getName()==o && (minDist==-1 || dist<minDist)){
				minDist = dist;
				best = enemy;
			}
		}
		return best;
	}
	
	public IAUnit getNearestAlly(ObjetsList o){
		Vector<IAAllyObject> enemies = getIA().getUnits();
		float minDist = -1;
		IAUnit best =null;
		for(IAAllyObject enemy : enemies){
			float dist = IAUnit.distance(enemy, this);
			if(enemy.getName()==o && (minDist==-1 || dist<minDist)){
				minDist = dist;
				best = enemy;
			}
		}
		return best;
	}
	
	public IAUnit getNearestEnemy(ObjetsList o){
		Vector<IAUnit> enemies = getIA().getEnemies();
		float minDist = -1;
		IAUnit best =null;
		for(IAUnit enemy : enemies){
			float dist = IAUnit.distance(enemy, this);
			if(enemy.getName()==o && (minDist==-1 || dist<minDist)){
				minDist = dist;
				best = enemy;
			}
		}
		return best;
	}
	
	public IAUnit getNearest(ObjetsList o ){
		Vector<IAUnit> enemies = new Vector<IAUnit>();
		enemies.addAll(getIA().getUnits());
		enemies.addAll(getIA().getEnemies());
		enemies.addAll(getIA().getNature());
		float minDist = -1;
		IAUnit best =null;
		for(IAUnit enemy : enemies){
			float dist = IAUnit.distance(enemy, this);
			if(enemy.getName()==o && (minDist==-1 || dist<minDist)){
				minDist = dist;
				best = enemy;
			}
		}
		return best;	
	}
	
	public IAUnit getTarget(){
		return new IAUnit(this.objet.getTarget(),this.ia);
	}
	public boolean hasTarget(){
		return objet.getTarget()!=null;
	}
	
	public boolean equals(Object o){
		if(o instanceof IAUnit){
			return this.getId()==((IAUnit)o).getId();
		}
		return false;
	}
	
	// All things that you can produce (tech,units,spells ...)
	public Vector<ObjetsList> getAllProductions(){
		Vector<ObjetsList> result = new Vector<ObjetsList>();
		result.addAll(getProductionList());
		result.addAll(getResearchList());
		result.addAll(getSpells());
		return result;
	}
	
	
	
	
}