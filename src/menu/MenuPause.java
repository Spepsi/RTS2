package menu;

import java.util.Vector;

import org.newdawn.slick.Image;

import model.Game;
import model.Map;
import model.Plateau;
import multiplaying.MultiReceiver;
import multiplaying.MultiSender;

public class MenuPause extends Menu {

	public Image newGame;
	public Image multiplayer;
	public Image options;
	public Image exit;
	public Image newGameSelected;
	public Image multiplayerSelected;
	public Image optionsSelected;
	public Image exitSelected;
	public int selected = -1;

	public MenuPause(Game game){
		super(game);
		this.game = game;
		this.items = new Vector<Menu_Item>();
		float startY = 0.50f*this.game.resY;
		float stepY = 0.09f*this.game.resY;
		float startX = this.game.resX/2;
		
		// handling items
		this.items.addElement(new Menu_Item(startX,startY,"unjoueur",this.game,true));
		this.items.addElement(new Menu_Item(startX,startY+1f*stepY,"multijoueur",this.game,true));
		this.items.addElement(new Menu_Item(startX,startY+2f*stepY,"options",this.game,true));
		this.items.addElement(new Menu_Item(startX,startY+3f*stepY,"quitter",this.game,true));

		//		}
	}

	public void callItem(int i){
		switch(i){
		case 0:
			this.game.inMultiplayer = false;
			this.game.initializePlayers();
			game.menuMapChoice.initializeMenuPlayer();
			Map.updateMap(0, game);
			this.game.setMenu(this.game.menuMapChoice);
			break;
		case 1:
			this.game.receiver = new MultiReceiver(this.game);
			this.game.receiver.start();
			this.game.sender = new MultiSender(this.game);
			this.game.sender.start();
			this.game.setMenu(this.game.menuMulti);
			break;
		case 2:
			this.game.setMenu(this.game.menuOptions);
			break;
		case 3: 
			this.game.app.exit();
			break;
		default:		
		}
	}

}
