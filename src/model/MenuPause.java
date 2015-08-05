package model;

import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class MenuPause extends Menu {
	
	public MenuPause(Game game){
		this.game = game;
		this.items = this.createHorizontalCentered(3, this.game.resX, this.game.resY);
		this.items.get(0).name = "Continuer";
		this.items.get(1).name = "Recommencer";
		this.items.get(2).name = "Quitter";
	}
	
	public void callItems(Input i){
		for(int j=0; j<items.size(); j++){
			if(items.get(j).isClicked(i))
				callItem(j);
		}
	}
	public void callItem(int i){
		switch(i){
		case 0:
			this.game.quitMenu();
			break;
		case 1: 
			try {
				this.game.init(this.game.app);
			} catch (SlickException e) {
				e.printStackTrace();
			}
			break;
		case 2:
			this.game.app.exit();
			break;
		default:		
		}
	}
}
