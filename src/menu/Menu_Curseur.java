package menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import model.Game;
import multiplaying.InputObject;

public class Menu_Curseur extends Menu_Item{


	public Image curseur;
	public float value;
	public float decalage;
	public boolean isSelected;

	public Menu_Curseur(float x, float y, String name, Image im, Image curseur, Game g,float value){
		super(x,y,name,im,im,g);
		this.curseur = curseur;
		this.value = value;
		this.name = "image manquante";
		this.sizeX = this.image.getWidth();
		this.sizeY = this.image.getHeight();
		this.decalage = this.sizeX/11f;
	}

	public void draw(Graphics g){
		if(this.image!=null && this.curseur !=null){
			g.drawImage(this.image,x-this.image.getWidth()/2f, y-this.image.getHeight()/2f);
			g.drawImage(this.curseur, x+decalage+value*(this.sizeX-2*decalage)-image.getWidth()/2f-curseur.getWidth()/2f, y-this.image.getHeight()/2f);
		}
		else{
			g.drawString(this.name, x, y);
		}

	}


	public void update(InputObject im){
		if(this.isMouseOver(im) && im.pressedLeftClick){
			value = Math.min(1, Math.max(0,(im.xMouse-x-decalage+this.image.getWidth()/2f)/(this.sizeX-2*decalage)));
			isSelected = true;
		} else if (isSelected && im.leftClick){
			value = Math.min(1, Math.max(0,(im.xMouse-x-decalage+this.image.getWidth()/2f)/(this.sizeX-2*decalage)));
		} else {
			isSelected = false;
		}
	}
}