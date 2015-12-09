package buildings;


import model.Plateau;
import units.Character;

public class BonusSpeed extends Bonus{


	public BonusSpeed(Plateau p , float x , float y){
		this.initialize(p, x, y);
		this.image = this.p.g.images.bonusSpeed;
		this.bonus = 20f;
	}

	public void action(){
		this.state+=0.1f;
		if(!bonusPresent && this.state>timeRegen){
			this.bonusPresent =true;
			this.state= 0f;
		}
		else if(bonusPresent && this.state>this.animationStep){
			this.animation=(this.animation+1)%4;
			this.state= 0f;
		}
	}

	public void collision(Character c){
		
		if(this.bonusPresent && c.getTeam()==this.getTeam()){
			c.maxVelocity +=this.bonus;
			this.bonusPresent =false;
			this.state = 0f;
			this.sound.play(1f, this.p.g.options.soundVolume);
			this.setTeam(0);
			this.potentialTeam = 0;
			this.constructionPoints=0f;
		}

	}




}