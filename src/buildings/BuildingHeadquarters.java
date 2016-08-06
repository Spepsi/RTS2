package buildings;

import java.util.Vector;

import data.Attributs;
import main.Main;
import model.Game;
import technologies.DualistAge2;
import technologies.DualistAge3;
import technologies.DualistBonusFood;
import technologies.DualistBonusGold;
import technologies.DualistContact2;
import technologies.DualistContact3;
import technologies.DualistEagleView;
import technologies.DualistExplosion;
import technologies.DualistHealth2;
import technologies.DualistHealth3;
import technologies.DualistRangeAttack2;
import technologies.DualistRangeAttack3;
import technologies.DualistShield2;
import technologies.DualistShield3;
import technologies.Technologie;

public class BuildingHeadquarters extends BuildingTech {


	public int age=1;
	boolean isProducing;
	public Vector<Technologie> techsDiscovered;
	public Vector<Technologie> allTechs;
	
	
	public BuildingHeadquarters(float f, float h, int team) {
		// Init ProductionList
		this.hq = this;
		this.productionList = new Vector<Technologie>();
		this.allTechs = new Vector<Technologie>();
		this.setTeam(team);
		
		this.getGameTeam().hq = this;
		
			// AGING
			DualistAge2 d2 = new DualistAge2(this.getTeam());
			this.allTechs.addElement(d2);
			DualistAge3 d3 = new DualistAge3(this.getTeam());
			this.allTechs.addElement(d3);
			d3.techRequired=d2;
			
			// SIGHT TECH
			DualistEagleView ev =new DualistEagleView(this.getTeam());
			this.allTechs.addElement(ev);
			ev.techRequired = d2;
			
			// RESSOURCES BONUS
			DualistBonusFood d4 = new DualistBonusFood(this.getTeam());
			this.allTechs.addElement(d4);

			DualistBonusGold bg = new DualistBonusGold(this.getTeam());
			this.allTechs.addElement(bg);

			
			// EXPLOSION TECH
			DualistExplosion ex = new DualistExplosion(this.getTeam());
			this.allTechs.addElement(ex);
			ex.techRequired=d2;
			
			// SHIELD TECH
			DualistShield2 s2 = new DualistShield2(this.getTeam());
			this.allTechs.addElement(s2);
			DualistShield3 s3 = new DualistShield3(this.getTeam());
			s3.techRequired = s2;
			this.allTechs.addElement(s3);
			
			// HEALTH TECH
			DualistHealth2 h2 = new DualistHealth2(this.getTeam());
			this.allTechs.addElement(h2);
			DualistHealth3 h3 = new DualistHealth3(this.getTeam());
			h3.techRequired = h2;
			this.allTechs.addElement(h3);
			
			// CONTACT WEAPON TECH
			DualistContact2 c2 = new DualistContact2(this.getTeam());
			this.allTechs.addElement(c2);
			DualistContact3 c3 = new DualistContact3(this.getTeam());
			c3.techRequired = c2;
			this.allTechs.addElement(c3);
			
			
			// RANGE WEAPON TECH
			DualistRangeAttack2 r2 = new DualistRangeAttack2(this.getTeam());
			this.allTechs.addElement(r2);
			DualistRangeAttack3 r3 = new DualistRangeAttack3(this.getTeam());
			r3.techRequired = r2;
			this.allTechs.addElement(r3);
			
	
		
		
		this.queue = null;
		teamCapturing= getTeam();
		this.name = "headquarters";
		type= 5;

		this.initialize(f,h);
		this.constructionPoints = this.getAttribut(Attributs.maxLifepoints);
		// List of potential production 
		this.techsDiscovered = new Vector<Technologie>();
		this.updateProductionList();
		

	}

	public void changeTech(Vector<Integer> techs){
		for(Integer q : techs){
			boolean useful = true;
			for(Technologie t : this.techsDiscovered){
				if(t.id==q){
					useful = false;
				}
			}
			if(useful){
				this.techTerminate(Technologie.technologie(q, getTeam()));
			}
		}
	}

	public void action(){
		giveUpProcess();
		if(underAttackRemaining>0f){
			this.underAttackRemaining-=Main.increment;
		}
		else{
			this.underAttack = false;
		}
		//Do the action of Barrack
		//Product, increase state of the queue
		// If enough faith create archange
		
		// CES LIGNES SERVAIENT A CREER UN ARCHANGE, ET JE LES AI SUPPRIME, LOL
//		if(this.getGameTeam().civ==0 && this.getGameTeam().special>=UnitsList.Archange.specialPrice){
//			
//			this.getGameTeam().data.create(UnitsList.Archange, this.x, this.y+this.sizeY/2);
//			this.getGameTeam().special=0;
//		}
		// voil�, plus d'archanges
		
		if(this.queue!=null){
			if(!this.isProducing){
				this.isProducing = true;
			}
			this.animation+=2f;
			if(animation>120f)
				animation = 0;
			this.setCharge(this.charge+Main.increment);
			if(this.charge>=this.queue.tech.prodTime){
				this.techTerminate(this.queue);
			}
		}
		else if(this.isProducing){
			this.isProducing = false;
			this.animation = 0;
		}


	}
	

	public void updateAllProductionList(){
		for(Building b : Game.g.plateau.buildings){
			if(b instanceof BuildingTech && b.getTeam()==this.getTeam()){
				((BuildingTech)b).updateProductionList();
			}
		}
	}
	
	

	
}
