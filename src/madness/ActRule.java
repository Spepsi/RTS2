package madness;


public enum ActRule implements java.io.Serializable{
	
	no_attack_building("Impossible d'attaquer les b�timents ennemis"),
	no_building_production("Impossible de produire dans les b�timents"),
	no_tower_attack("Les tours n'attaquent pas"),
	no_defense_building("Impossible de d�fendre ses b�timents"),
	sudden_death("Gilles arrive !");
	
	public String description;
	
	ActRule(String string){
		this.description = string;
	}
	

}
