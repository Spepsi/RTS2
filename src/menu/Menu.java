package menu;

import java.util.Vector;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;

import model.Game;
import model.Sounds;
import multiplaying.InputObject;

public abstract class Menu {

	public Vector<Menu_Item> items;
	public Vector<Menu_Item> itemsSelected;
	public Game game;
	public Sounds sounds;
	public Music music;
	public void callItems(Input i){
		for(int j=0; j<items.size(); j++){
			if(items.get(j).isClicked(i))
				callItem(j);
		}
	}
	public void callItems(InputObject im){
		for(int j=0; j<items.size(); j++){
			if(items.get(j).isClicked(im))
				callItem(j);
		}
	}

	public void callItem(int i){

	}

	public Vector<Menu_Item> createHorizontalCentered(int i, float sizeX, float sizeY){
		Vector<Menu_Item> items = new Vector<Menu_Item>();
		float unitY = sizeY/(9f+3f*i);
		for(int j=0;j<i;j++){
			//items.add(new Menu_Item(sizeX/3f,5*unitY+3f*j*unitY,sizeX/3f,2*unitY,""));
		}
		return items;
	}

	public void draw(Graphics g){
		int i = 0;
		int j = 0;
		while(i<this.game.resX+this.game.images.grassTexture.getWidth()){
			while(j<this.game.resY+this.game.images.grassTexture.getHeight()){
				g.drawImage(this.game.images.grassTexture, i,j);
				j+=this.game.images.grassTexture.getHeight();
			}
			i+=this.game.images.grassTexture.getWidth();
			j= 0;
		}
		//g.translate(-game.Xcam,-game.Ycam);
		for(int k=0; k<this.items.size(); k++){
			this.items.get(k).draw(g);
		}
	}

	
	public void update(Input i){
		
	}
	public void update(InputObject im){
		
	}
}
