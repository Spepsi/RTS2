package display;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import buildings.BuildingProduction;
import buildings.BuildingTech;
import model.ActionObjet;
import model.Game;
import model.Utils;

import units.Character;


public class SelectionInterface extends Bar {

	float startX, startY;
	float sizeXBar;

	public BottomBar parent;
	public Game game;
	public SelectionInterface(BottomBar parent){
		this.game = parent.p.g;
		this.parent = parent;
		this.startX = 0;
		this.startY = parent.p.g.resY-parent.p.g.resX*parent.ratioSelectionX;
		this.sizeX = parent.p.g.resX*parent.ratioSelectionX;
		this.sizeY = sizeX;
		this.x = 0;
		this.y = 0;
	}

	public Graphics draw(Graphics g){

		// Draw the selection of current player
		g.setColor(Color.red);
		// Draw 4 separations
		g.setColor(Color.white);

		// variable de travail sizeVerticalBar
		float sVB = parent.ratioBarVertX*parent.p.g.resX;
		for(int i=0; i<5; i++){
			g.setColor(Color.darkGray);
			// TODO: faire des carr�s gris
		}

		// Draw building state
		if(this.parent.p.g.currentPlayer.selection.size()>0 && this.parent.p.g.currentPlayer.selection.get(0) instanceof BuildingProduction ){

			BuildingProduction b = (BuildingProduction) this.parent.p.g.currentPlayer.selection.get(0);

			this.sizeXBar = (Math.min(4,b.queue.size()+1))*(sVB+2)+3;
			Utils.drawNiceRect(g, game.currentPlayer.getGameTeam().color, startX+sizeX-4, parent.p.g.resY-sVB, sizeXBar, sVB+4);
			Utils.drawNiceRect(g, game.currentPlayer.getGameTeam().color, startX-4, startY, sizeX+4, sizeY+4);
			
			int compteur = 0;
			if(b.queue.size()>0){
				for(int q : b.queue){
					Image icone = this.parent.p.g.images.get("icon"+b.productionList.get(q).name);
					if(compteur ==0){
						//Show icons
						//Show production bar
						g.drawImage(icone,startX+this.sizeX/4, startY+this.sizeY/4,startX+sizeX-5, startY + sizeY-5,0,0,512,512);
						g.setColor(Color.white);
						String s = b.productionList.get(q).name;
						g.drawString(s, startX+sizeX/2-parent.p.g.font.getWidth(s)/2f, startY+sizeY/8f-parent.p.g.font.getHeight(s)/2f);
						g.fillRect(startX+this.sizeX/16, startY+this.sizeY/4 +10f, sizeX/8f,3*sizeY/4-20f);
						g.setColor(Color.gray);
						g.fillRect(startX+this.sizeX/16, startY+this.sizeY/4 +10f, sizeX/8f,3*sizeY/4-20f);
						g.setColor(parent.p.g.currentPlayer.getGameTeam().color);
						g.fillRect(startX+this.sizeX/16, startY+this.sizeY/4+10f+b.charge*(3*sizeY/4-20f)/b.productionList.get(q).time, sizeX/8f,3*sizeY/4-20f-b.charge*(3*sizeY/4-20)/b.productionList.get(q).time);
					}
					else{
						g.drawImage(icone,this.x+this.sizeX+5+(sVB)*(compteur-1), parent.p.g.resY-sVB+3f, this.x+this.sizeX+(sVB)*(compteur), parent.p.g.resY-1,0f,0f,512f,512f);
					}
					compteur ++;
				}
			}
		} else if(this.parent.p.g.currentPlayer.selection.size()>0 && this.parent.p.g.currentPlayer.selection.get(0) instanceof BuildingTech ){
			BuildingTech b = (BuildingTech) this.parent.p.g.currentPlayer.selection.get(0);
//			this.sizeXBar = (b.queue.size()+1)*(sVB+2);
//			Utils.drawNiceRect(g, game.currentPlayer.getGameTeam().color, startX+sizeX-4, parent.p.g.resY-sVB, 5*(sVB+2), sVB+4);
			Utils.drawNiceRect(g, game.currentPlayer.getGameTeam().color, startX-4, startY, sizeX+4, sizeY+4);
			if(b.queue!=null){
				Image icone = b.queue.icon;
				//Show icons
				//Show production bar
				g.drawImage(icone,startX+this.sizeX/4, startY+this.sizeY/4,startX+sizeX-5, startY + sizeY-5,0,0,512,512);
				g.setColor(Color.white);
				String s = b.queue.tech.name;
				g.drawString(s, startX+sizeX/2-parent.p.g.font.getWidth(s)/2f, startY+sizeY/8f-parent.p.g.font.getHeight(s)/2f);
				g.fillRect(startX+this.sizeX/16, startY+this.sizeY/4 +10f, sizeX/8f,3*sizeY/4-20f);
				g.setColor(Color.gray);
				g.fillRect(startX+this.sizeX/16, startY+this.sizeY/4 +10f, sizeX/8f,3*sizeY/4-20f);
				g.setColor(parent.p.g.currentPlayer.getGameTeam().color);
				g.fillRect(startX+this.sizeX/16, startY+this.sizeY/4+10f+b.charge*(3*sizeY/4-20f)/b.queue.tech.prodTime, sizeX/8f,3*sizeY/4-20f-b.charge*(3*sizeY/4-20)/b.queue.tech.prodTime);
			}
		}else if(this.parent.p.g.currentPlayer.selection.size()>0 && this.parent.p.g.currentPlayer.selection.get(0) instanceof Character ){

			Character c;
			int compteur = 0;
			int nb = this.parent.p.g.currentPlayer.selection.size()-1;

			this.sizeXBar = (Math.min(nb+1, 5))*(sVB+2)+2;
			Utils.drawNiceRect(g, game.currentPlayer.getGameTeam().color, startX+sizeX-4, parent.p.g.resY-sVB, sizeXBar, sVB+4);
			Utils.drawNiceRect(g, game.currentPlayer.getGameTeam().color, startX-4, startY, sizeX+4, sizeY+4);
			for(ActionObjet a : this.parent.p.g.currentPlayer.selection){
				c = (Character) a;
				Image icone = a.image;
				int imageWidth = a.image.getWidth()/5;
				int imageHeight = a.image.getHeight()/4;
				float r = a.collisionBox.getBoundingCircleRadius();
				if(compteur ==0){
					//Show icons
					//Show production bar
					g.setColor(Color.darkGray);
					g.fillRect(startX+this.sizeX/4, startY+this.sizeY/4,3*sizeX/4-5, 3*sizeY/4-5);
					g.setColor(Color.white);
					g.drawRect(startX+this.sizeX/4, startY+this.sizeY/4,3*sizeX/4-5, 3*sizeY/4-5);
					g.drawImage(icone,startX+this.sizeX/4, startY+this.sizeY/4,startX+sizeX-5, startY + sizeY-5,imageWidth*c.animation,0,imageWidth*c.animation+imageWidth,imageHeight);
					g.setColor(Color.white);
					String s = a.name;
					g.drawString(s, startX+sizeX/2-parent.p.g.font.getWidth(s)/2f, startY+sizeY/8f-parent.p.g.font.getHeight(s)/2f);
					g.fillRect(startX+this.sizeX/16, startY+this.sizeY/4 +10f, sizeX/8f,3*sizeY/4-20f);
					g.setColor(Color.darkGray);
					g.fillRect(startX+this.sizeX/16, startY+this.sizeY/4 +10f, sizeX/8f,3*sizeY/4-20f);
					float x = a.lifePoints/a.maxLifePoints;
					g.setColor(new Color((1f-x),x,0));
					g.fillRect(startX+this.sizeX/16, startY+this.sizeY/4+10f+(a.maxLifePoints-a.lifePoints)*(3*sizeY/4-20f)/a.maxLifePoints, sizeX/8f,3*sizeY/4-20f-(a.maxLifePoints-a.lifePoints)*(3*sizeY/4-20f)/a.maxLifePoints);
					g.setColor(Color.white);
					g.drawRect(startX+this.sizeX/16, startY+this.sizeY/4 +10f, sizeX/8f,3*sizeY/4-20f);
				}
				else{
					int x1,y1,x2,y2;
					if(nb>5){
						x1 = (int) (this.x+this.sizeX+5+(sVB)*(compteur-1)*4/(nb-1));
						y1 = (int) (parent.p.g.resY-sVB+3f);
						x2 = (int) (x1+sVB);
						y2 = (int) (y1+sVB);						
					} else {
						x1 = (int) (this.x+this.sizeX+5+(sVB)*(compteur-1));
						y1 = (int) (parent.p.g.resY-sVB+3f);
						x2 = (int) (x1+sVB);
						y2 = (int) (y1+sVB);
					}
					float x = a.lifePoints/a.maxLifePoints;
					g.setColor(Color.darkGray);
					g.fillRect(x1, y1, x2-x1, y2-y1);
					g.setColor(new Color((1f-x),x,0));
					float diff = x*(y2-y1);
					g.fillRect(x1, y1-diff+(y2-y1), (x2-x1)/5, diff);
					g.drawImage(icone,x1, y1, x2, y2, imageWidth*c.animation, 0, imageWidth*c.animation+imageWidth, imageHeight);
					g.setColor(Color.white);
					g.drawRect(x1, y1, x2-x1, y2-y1);
				}
				compteur ++;
			}
		} else {
			Utils.drawNiceRect(g, game.currentPlayer.getGameTeam().color, startX-4, startY, sizeX+4, sizeY+4);
		}


		return g;
	}
}
