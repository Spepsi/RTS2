package IA;

public class Report {

	int teamVictory;
	int remainingUnits;
	
	
	
	public Report(int teamVictory,int remainingUnits){
		this.teamVictory = teamVictory;
		this.remainingUnits = remainingUnits;
	}
	
	public String toString(){
		String result = "";
		result+=("teamVictory :  "+ teamVictory+ " ");
		result+=("remainingUnits :  "+ teamVictory);
		return result;
	}
}