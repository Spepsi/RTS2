package display;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import bonus.Bonus;
import control.InputObject;
import control.KeyMapper.KeyEnum;
import control.Player;
import data.Attributs;
import main.Main;
import model.Colors;
import model.Game;
import plateau.Building;
import plateau.Character;
import plateau.NaturalObjet;
import plateau.Objet;
import plateau.Plateau;
import plateau.Team;
import ressources.GraphicElements;
import ressources.Images;
import spells.Spell;
import system.Debug;
import utils.ObjetsList;
import utils.Utils;

public class Interface {

	public static float ratioMinimapX = 1/6f;
	public static float ratioSelectionX = 1/8f;
	public static float ratioSpellX = 1/12f;
	public static float ratioBarVertX = 1/32f;

	public static float nbRoundInit = 3*Main.framerate;
	public static float debut = nbRoundInit/4, duree = debut;

	// selection bar
	public static float startXSelectionBar = 0;
	public static float startYSelectionBar = Game.resY-Game.resX*ratioSelectionX;
	public static float sizeXSelectionBar = Game.resX*ratioSelectionX;
	public static float sizeYSelectionBar = sizeXSelectionBar;

	// action bar
	public static float startXActionBar = 0;
	public static float sizeXActionBar = ratioBarVertX*Game.resX;
	public static float sizeYActionBar = 5*ratioBarVertX*Game.resX;
	public static float startYActionBar = Game.resY - sizeYActionBar - sizeYSelectionBar;
	public static float yActionBar = startYActionBar+sizeYActionBar;
	public static boolean mouseOnActionBar;
	public static int prodIconNbY = 5;
	public static int prodIconNbX = 2;
	public static boolean[][] toDrawDescription = new boolean[prodIconNbY][prodIconNbX];

	// top bar

	private static int gold, food;

	public static float ratioSizeGoldX = 1/13f;
	public static float ratioSizeTimerX = 1/12f;
	public static float ratioSizeGoldY = 1/19f;
	public static float ratioSizeTimerY = 1/15f;

	public static float startYDescription = 0;
	public static float ratioSizeDescriptionX = 1/3f;
	public static float ratioSizeChoiceX = 1/6f;
	public static boolean mouseOnTopBar;
	public static Vector<Icon> iconChoice;

	private static float debutC = nbRoundInit/4;
	private static float debut1 = 3*nbRoundInit/8;
	private static float debut2 = nbRoundInit/2;
	private static float dureeDescente = nbRoundInit/8;

	// minimap
	public static float startXMiniMap = Game.resX*(1-ratioMinimapX)+3; 
	public static float startX2MiniMap = Game.resX*(1-ratioMinimapX)+3;
	public static float offsetDrawX;
	public static float sizeXMiniMap = Game.resX*ratioMinimapX-6, sizeYMiniMap = sizeXMiniMap;
	public static float startYMiniMap, startY2MiniMap = Game.resY-ratioMinimapX*Game.resX+3;
	public static float widthMiniMap;
	public static float heightMiniMap;
	public static float ratioWidthMiniMap;
	public static float ratioHeightMiniMap;
	private static float debutGlissade = nbRoundInit/4;
	private static float dureeGlissade = nbRoundInit/4;

	//card choice
	private static float startYCardChoiceBar;
	private static float sizeXCardChoiceBar = sizeXActionBar;
	private static float sizeYCardChoiceBar;
	public static Vector<Icon> cardChoice = new Vector<Icon>();

	//killing spree offest
	public static float offsetYkillingSpree = -150f;


	// Spell with click handling
	public static boolean spellOk = true;
	public static boolean spellRelease = false;
	public static Integer spellLauncher;
	public static Integer spellTarget;
	public static float spellX,spellY;
	public static ObjetsList spellCurrent = null;


	///////
	// Update mathods
	///////

	public static void update(InputObject im, Plateau plateau){
		updateActionInterface(im, plateau);
		updateTopInterface(im.xOnScreen, im.yOnScreen, plateau);
		updateMinimap(im, plateau);
	}

	public static void updateActionInterface(InputObject im, Plateau plateau){
		for(int i = 0 ; i<prodIconNbY;i++){
			for(int j = 0 ; j<prodIconNbX; j++){
				toDrawDescription[i][j] = false;
			}
		}
		float xMouse = im.xOnScreen;
		float yMouse = im.yOnScreen;
		// draw items descriptions
		if (isMouseOnActionBar(xMouse, yMouse)) {
			int mouseOnItem = (int) ((yMouse - startYActionBar) / (sizeYActionBar / prodIconNbY));
			int yItem = xMouse>startXActionBar + sizeXActionBar? 1:0;
			if (mouseOnItem >= 0 && mouseOnItem < prodIconNbY){
				toDrawDescription[mouseOnItem][yItem] = true;
				if(im.isPressed(KeyEnum.LeftClick)){
					im.pressed.remove(KeyEnum.LeftClick);
					if(Player.selection.size()>0 && plateau.getById(Player.selection.get(0)) instanceof Character){
						Character c = (Character) plateau.getById(Player.selection.get(0)); 
						Spell s = c.getSpell(mouseOnItem);
						if(s != null && s.getAttribut(Attributs.needToClick)>0){
							spellLauncher = c.id;
							spellCurrent = s.name;
						}
					} else {
						im.pressProd(mouseOnItem);
					}
				}
				if(im.isDown(KeyEnum.LeftClick)){
					im.down.remove(KeyEnum.LeftClick);
				}
			}
		}
		
		// handle spell
		if(Player.selection.size()>0 && plateau.getById(Player.selection.get(0)) instanceof Character){
			for(int i = 0; i<prodIconNbY; i++){
				if(im.isPressedProd(i)){
					Character c = (Character) plateau.getById(Player.selection.get(0)); 
					Spell s = c.getSpell(i);
					
					if(s!=null && s.getAttribut(Attributs.needToClick)>0 && c.canLaunch(i)){
						spellLauncher = c.id;
						spellCurrent = s.name;
					}
					im.pressed.remove(KeyEnum.valueOf("Prod"+i));
				}
			}
		}
		if(spellCurrent!=null){
			spellX = im.x;
			spellY = im.y;
		}
		if(im.pressed.size()>0 && spellCurrent!=null ){
			if(im.isPressed(KeyEnum.LeftClick) && Player.selection.size()>0 && plateau.getById(Player.selection.get(0)) instanceof Character){
				// check if launch spell
				Character c = (Character) plateau.getById(Player.selection.get(0)); 
				if(c.getSpellState(spellCurrent)>=c.getSpell(spellCurrent).getAttribut(Attributs.chargeTime)){
					im.spell = spellCurrent;
					im.idSpellLauncher = spellLauncher;
					if(spellTarget!=null){
						im.idObjetMouse = spellTarget;
					} else {
						im.idObjetMouse = -1;
					}
					im.pressed.remove(KeyEnum.LeftClick);
					spellRelease = true;
					resetCurrentSpell();
				}
			} else {
				resetCurrentSpell();
			}
			if(im.isDown(KeyEnum.LeftClick)){
				im.down.remove(KeyEnum.LeftClick);
			}
		}
		if(spellRelease==true){
			if(im.down.contains(KeyEnum.LeftClick)){
				im.down.remove(KeyEnum.LeftClick);
			} else {
				spellRelease = false;
			}
			if(im.pressed.contains(KeyEnum.LeftClick)){
				im.pressed.remove(KeyEnum.LeftClick);
			}
		}
	}

	public static void updateTopInterface(float xMouse, float yMouse, Plateau plateau){
		mouseOnTopBar = isMouseOnTopBar(xMouse, yMouse);
		if(iconChoice==null){
			return;
		}
		for(Icon icon : iconChoice){
			icon.update(xMouse, yMouse);
		}
	}

	public static void updateMinimap(InputObject im, Plateau plateau){
		if(isMouseOnMiniMap(im.xOnScreen, im.yOnScreen)){
			im.isOnMiniMap = true;

			im.x = (int) Math.floor((im.xOnScreen-startXMiniMap)/ratioWidthMiniMap);
			im.y = (int) Math.floor((im.yOnScreen-startYMiniMap)/ratioHeightMiniMap);
		}
	}

	public static void updateRatioMiniMap(Plateau plateau){

		if(plateau.maxX>plateau.maxY){
			widthMiniMap = sizeXMiniMap;
			heightMiniMap = widthMiniMap*plateau.maxY/plateau.maxX;
			startXMiniMap = startX2MiniMap;
			startYMiniMap = startY2MiniMap + (sizeYMiniMap-heightMiniMap)/2;
		} else {
			heightMiniMap = sizeYMiniMap;			
			widthMiniMap = heightMiniMap*plateau.maxX/plateau.maxY;
			startXMiniMap = startX2MiniMap + (sizeXMiniMap-widthMiniMap)/2;
			startYMiniMap = startY2MiniMap;
		}
		ratioWidthMiniMap = widthMiniMap/plateau.maxX;
		ratioHeightMiniMap = heightMiniMap/plateau.maxY;
	}

	///////
	// Draw mathods
	///////

	public static Graphics draw(Graphics g, Camera camera, Plateau plateau){
		// Draw Background :


		// Draw image according to size

		//g.drawImage(background,x,y-6f);

		// ACTIONS, Spells  and production
		drawSpell(g, camera, plateau);
		drawActionInterface(g, plateau);
		drawSelectionInterface(g, plateau);
		drawTopInterface(g, plateau);
		drawMiniMap(g, camera, plateau);

		//spell.draw(g);


		return g;
	}

	public static void drawSelectionInterface(Graphics g, Plateau plateau){

		float sizeXBar;
		float x = 0;
		if(plateau.round<nbRoundInit)
			startXSelectionBar = Math.max(-Game.resX-10, Math.min(0, Game.resX*(plateau.round-debut-duree)/duree));

		// variable de travail sizeVerticalBar
		g.setLineWidth(1f);
		float sVB = ratioBarVertX*Game.resX;


		// Draw building state
		Vector<Integer> selection = Player.selection;
		if(selection.size()>0 && plateau.getById(selection.get(0)) instanceof Building ){

			Building b = (Building) plateau.getById(selection.get(0));

			sizeXBar = (Math.min(4,b.getQueue().size()+1))*(sVB+2)+3;
			Utils.drawNiceRect(g, Player.getTeam().color, startXSelectionBar+sizeXSelectionBar-4, Game.resY-sVB, sizeXBar, sVB+4);
			Utils.drawNiceRect(g, Player.getTeam().color, startXSelectionBar-4, startYSelectionBar, sizeXSelectionBar+4, sizeYSelectionBar+4);

			int compteur = 0;
			if(b.getQueue().size()>0){
				for(ObjetsList q : b.getQueue()){
					Image icone = Images.get("icon"+q.name());
					if(compteur ==0){
						//Show icons
						//Show production bar
						g.drawImage(icone,startXSelectionBar+sizeXSelectionBar/4, 
								startYSelectionBar+sizeYSelectionBar/4,
								startXSelectionBar+sizeXSelectionBar-5, startYSelectionBar + sizeYSelectionBar-5,0,0,512,512);
						g.setColor(Color.white);
						String s = Player.getTeam().data.getAttributString(q, Attributs.printName);
						Float prodTime = Player.getTeam().data.getAttribut(q, Attributs.prodTime);
						g.drawString(s, startXSelectionBar+sizeXSelectionBar/2-GraphicElements.font_main.getWidth(s)/2f, 
								startYSelectionBar+sizeYSelectionBar/8f-GraphicElements.font_main.getHeight(s)/2f);
						g.fillRect(startXSelectionBar+sizeXSelectionBar/16, 
								startYSelectionBar+sizeYSelectionBar/4 +10f, sizeXSelectionBar/8f,3*sizeYSelectionBar/4-20f);
						g.setColor(Color.gray);
						g.fillRect(startXSelectionBar+sizeXSelectionBar/16, 
								startYSelectionBar+sizeYSelectionBar/4 +10f, sizeXSelectionBar/8f,3*sizeYSelectionBar/4-20f);
						g.setColor(Player.getTeam().color);
						g.fillRect(startXSelectionBar+sizeXSelectionBar/16, 
								startYSelectionBar+sizeYSelectionBar/4+10f+b.charge*(3*sizeYSelectionBar/4-20f)/prodTime, 
								sizeXSelectionBar/8f,3*sizeYSelectionBar/4-20f-b.charge*(3*sizeYSelectionBar/4-20)/prodTime);
					}
					else{
						g.drawImage(icone,sizeXSelectionBar+5+(sVB)*(compteur-1), 
								Game.resY-sVB+3f, 
								sizeXSelectionBar+(sVB)*(compteur), 
								Game.resY-1,0f,0f,512f,512f);
					}
					compteur ++;
				}
			} else {
				g.setColor(Color.white);
				String s = b.getAttributString(Attributs.printName);
				g.drawString(s, startXSelectionBar+sizeXSelectionBar/2-GraphicElements.font_main.getWidth(s)/2f, 
						startYSelectionBar+sizeYSelectionBar/8f-GraphicElements.font_main.getHeight(s)/2f);
			}
			//		} else if(selection.size()>0 && selection.get(0) instanceof Building  ){
			//			Building b = (Building) selection.get(0);
			////			sizeXBar = (b.queue.size()+1)*(sVB+2);
			////			Utils.drawNiceRect(g, game.currentPlayer.getGameTeam().color, startX+Game.resX-4, parent.p.g.resY-sVB, 5*(sVB+2), sVB+4);
			//			Utils.drawNiceRect(g,  Selection.getTeam().color, startX-4, startY, Game.resX+4, sizeY+4);
			//			if(b.getQueueTechnologie()!=null){
			//				Image icone = Images.get(b.getQueueTechnologie().getIcon());
			//				//Show icons
			//				//Show production bar
			//				g.drawImage(icone,startX+Game.resX/4, startY+sizeY/4,startX+Game.resX-5, startY + sizeY-5,0,0,512,512);
			//				g.setColor(Color.white);
			//				String s = b.getQueueTechnologie().getName();
			//
			//				g.drawString(s, startX+Game.resX/2-GraphicElements.font_main.getWidth(s)/2f, startY+sizeY/8f-GraphicElements.font_main.getHeight(s)/2f);
			//				g.fillRect(startX+Game.resX/16, startY+sizeY/4 +10f, Game.resX/8f,3*sizeY/4-20f);
			//				g.setColor(Color.gray);
			//				g.fillRect(startX+Game.resX/16, startY+sizeY/4 +10f, Game.resX/8f,3*sizeY/4-20f);
			//				g.setColor( Selection.getTeam().color);
			//				g.fillRect(startX+Game.resX/16, startY+sizeY/4+10f+b.charge*(3*sizeY/4-20f)/b.getAttribut(b.getQueueTechnologie().objet, Attributs.foodCost), Game.resX/8f,3*sizeY/4-20f-b.charge*(3*sizeY/4-20)/b.getAttribut(b.getQueueTechnologie().objet, Attributs.foodCost));
			//			} else {
			//				g.setColor(Color.white);
			//				String s = b.getAttributString(Attributs.printName);
			//				g.drawString(s, startX+Game.resX/2-GraphicElements.font_main.getWidth(s)/2f, startY+sizeY/8f-GraphicElements.font_main.getHeight(s)/2f);
			//			}
		}else if(selection.size()>0 && plateau.getById(selection.get(0)) instanceof Character ){

			Character c;
			int compteur = 0;
			int nb = selection.size()-1;

			sizeXBar = (Math.min(nb+1, 5))*(sVB+2)+2;
			Utils.drawNiceRect(g, Player.getTeam().color, 
					startXSelectionBar+sizeXSelectionBar-4, Game.resY-sVB, sizeXBar, sVB+4);
			Utils.drawNiceRect(g, Player.getTeam().color, 
					startXSelectionBar-4, startYSelectionBar, sizeXSelectionBar+4, sizeYSelectionBar+4);
			for(Integer id : selection){
				Character a = (Character) plateau.getById(id);
				c = (Character) plateau.getById(id);
				Image icone = Images.get(c.name+"blue");
				int imageWidth = icone.getWidth()/5;
				int imageHeight = icone.getHeight()/4;
				//float r = a.collisionBox.getBoundingCircleRadius();
				if(compteur ==0){
					//Show icons
					//Show production bar
					g.setColor(Color.darkGray);
					g.fillRect(startXSelectionBar+sizeXSelectionBar/4, startYSelectionBar+sizeYSelectionBar/4,
							3*sizeXSelectionBar/4-5, 3*sizeYSelectionBar/4-5);
					g.setColor(Color.white);
					g.drawRect(startXSelectionBar+sizeXSelectionBar/4, startYSelectionBar+sizeYSelectionBar/4,
							3*sizeXSelectionBar/4-5, 3*sizeYSelectionBar/4-5);
					g.drawImage(icone,startXSelectionBar+sizeXSelectionBar/4, startYSelectionBar+sizeYSelectionBar/4,
							startXSelectionBar+sizeXSelectionBar-5, startYSelectionBar + sizeYSelectionBar-5,
							imageWidth*c.animation,0,imageWidth*c.animation+imageWidth,imageHeight);
					g.setColor(Color.white);
					String s = a.getAttributString(Attributs.printName);
					g.drawString(s, startXSelectionBar+sizeXSelectionBar/2-GraphicElements.font_main.getWidth(s)/2f, 
							startYSelectionBar+sizeYSelectionBar/8f-GraphicElements.font_main.getHeight(s)/2f);
					g.fillRect(startXSelectionBar+sizeXSelectionBar/16, startYSelectionBar+sizeYSelectionBar/4 +10f,
							sizeXSelectionBar/8f,3*sizeYSelectionBar/4-20f);
					g.setColor(Color.darkGray);
					g.fillRect(startXSelectionBar+sizeXSelectionBar/16, startYSelectionBar+sizeYSelectionBar/4 +10f, 
							sizeXSelectionBar/8f,3*sizeYSelectionBar/4-20f);
					float x_temp = a.lifePoints/a.getAttribut(Attributs.maxLifepoints);
					g.setColor(new Color((1f-x_temp),x_temp,0));
					g.fillRect(startXSelectionBar+sizeXSelectionBar/16, 
							startYSelectionBar+sizeYSelectionBar/4+10f+(a.getAttribut(Attributs.maxLifepoints)-a.lifePoints)*(3*sizeYSelectionBar/4-20f)/a.getAttribut(Attributs.maxLifepoints), 
							sizeXSelectionBar/8f,
							3*sizeYSelectionBar/4-20f-(a.getAttribut(Attributs.maxLifepoints)-a.lifePoints)*(3*sizeYSelectionBar/4-20f)/a.getAttribut(Attributs.maxLifepoints));
					g.setColor(Color.white);
					g.drawRect(startXSelectionBar+sizeXSelectionBar/16, startYSelectionBar+sizeYSelectionBar/4 +10f,
							sizeXSelectionBar/8f,3*sizeYSelectionBar/4-20f);
				}
				else{
					int x1,y1,x2,y2;
					if(nb>5){
						x1 = (int) (x+sizeXSelectionBar+5+(sVB)*(compteur-1)*4/(nb-1));
						y1 = (int) (Game.resY-sVB+3f);
						x2 = (int) (x1+sVB);
						y2 = (int) (y1+sVB);						
					} else {
						x1 = (int) (x+sizeXSelectionBar+5+(sVB)*(compteur-1));
						y1 = (int) (Game.resY-sVB+3f);
						x2 = (int) (x1+sVB);
						y2 = (int) (y1+sVB);
					}
					float x_temp = a.lifePoints/a.getAttribut(Attributs.maxLifepoints);
					g.setColor(Color.darkGray);
					g.fillRect(x1, y1, x2-x1, y2-y1);
					g.setColor(new Color((1f-x_temp),x_temp,0));
					float diff = x_temp*(y2-y1);
					g.fillRect(x1, y1-diff+(y2-y1), (x2-x1)/5, diff);
					g.drawImage(icone,x1, y1, x2, y2, imageWidth*c.animation, 0, imageWidth*c.animation+imageWidth, imageHeight);
					g.setColor(Color.white);
					g.drawRect(x1, y1, x2-x1, y2-y1);
				}
				compteur ++;
			}
		} else {
			Utils.drawNiceRect(g,  Player.getTeam().color, 
					startXSelectionBar-4, startYSelectionBar, sizeXSelectionBar+4, sizeYSelectionBar+4);
		}


	}

	public static void drawActionInterface(Graphics g, Plateau plateau){
		float startY2;
		Image imageGold ;
		Image imageFood;
		float debut = nbRoundInit/4, duree = debut;
		float offset;
		// Production Bar
		float ratio =1f/prodIconNbY;

		offset = sizeXSelectionBar;
		float x = 0;
		startYActionBar = Game.resY - sizeYActionBar - sizeYSelectionBar;
		startY2 = Game.resY - sizeYSelectionBar;

		//		for(int i= 0 ; i<prodIconNbY; i++){
		//			for(int j= 0 ; j<prodIconNbX; j++){
		//				toDrawDescription[i][j] = false;
		//			}
		//		}

		imageGold = Images.get("imagegolddisplayressources");
		imageFood = Images.get("imagefooddisplayressources");


		// Draw the potential actions
		// Draw Separation (1/3 1/3 1/3) : 

		if(plateau.round<nbRoundInit)
			x = Math.max(-offset-10, Math.min(0, offset*(plateau.round-debut-duree)/duree));
		else
			x = 0;
		g.setLineWidth(1f);
		if(mouseOnActionBar && yActionBar>startYActionBar)
			yActionBar = startYActionBar+(yActionBar-startYActionBar)/5;
		if(!mouseOnActionBar && yActionBar<startY2)
			yActionBar = startY2+(yActionBar-startY2)/5;

		Utils.drawNiceRect(g,  Player.getTeam().color, x-4, yActionBar-5, 2*sizeXActionBar+4, sizeYActionBar+9);
		g.setColor(Color.darkGray);
		for(int i=0; i<5; i++){
			g.setColor(Color.darkGray);
			g.fillRect(x+2f, yActionBar+2f + i*sizeXActionBar, -7f+sizeXActionBar, -7f+sizeXActionBar);
			g.fillRect(x+2f+sizeXActionBar, yActionBar+2f + i*sizeXActionBar, -7f+sizeXActionBar, -7f+sizeXActionBar);
		}
		g.setColor(Color.white);

		Vector<Integer> selection = Player.selection;

		// Draw Production/Effect Bar
		if(selection.size()>0 && plateau.getById(selection.get(0)) instanceof Building){
			System.out.println(selection.get(0).getClass());
			mouseOnActionBar = true;
			Building b =(Building) plateau.getById(selection.get(0));
			//Print building capacities
			Vector<ObjetsList> ul = b.getProductionList(plateau);
			int limit = Math.min(5, ul.size());
			Font f = g.getFont();
			for(int i=0; i<limit;i++){ 
				g.drawImage(Images.get("icon"+ul.get(i)), x+2f, yActionBar+2f + ratio*i*sizeYActionBar, x-5f+sizeXActionBar, yActionBar-5f+ratio*i*sizeYActionBar+sizeXActionBar, 0, 0, 512,512);
				g.setColor(Color.white);
				g.setColor( Player.getTeam().color);
				g.drawRect(x+1f, yActionBar+1f + i*sizeXActionBar, -6f+sizeXActionBar, -6f+sizeXActionBar);
				if(ul.size()>i && toDrawDescription[i][0]){
					// GET PRICE
					g.translate(sizeXActionBar, 0f);
					Float foodPrice = getAttribut(ul.get(i), Attributs.foodCost);
					Float goldPrice = getAttribut(ul.get(i), Attributs.goldCost);
					Float faithPrice = getAttribut(ul.get(i), Attributs.faithCost);
					Float prodTime = getAttribut(ul.get(i), Attributs.prodTime);
					g.setColor(Color.white);
					g.drawString(ul.get(i).name(), x + ratio*sizeYActionBar+10f, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name())/2f);
					g.drawImage(imageFood,x + 3.6f*(sizeXActionBar+400f)/7 , yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name())/2f);
					g.drawString(": "+foodPrice,x + 3.95f*(sizeXActionBar+400f)/7, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name())/2f);
					g.drawImage(imageGold,x + 4.8f*(sizeXActionBar+400f)/7 , yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name())/2f);
					g.drawString(": "+goldPrice,x + 5.15f*(sizeXActionBar+400f)/7 , yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name())/2f);
					g.drawString("T: ",x + 6f*(sizeXActionBar+400f)/7, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name())/2f);
					g.drawString(Float.toString(prodTime),x + 6.35f*(sizeXActionBar+400f)/7 , yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name())/2f);
					g.translate(-sizeXActionBar, 0f);
				}
			}



			g.translate(sizeXActionBar, 0f);

			//Print building capacities
			Vector<ObjetsList> ul2 = b.getTechnologyList(plateau);
			limit = Math.min(5, ul2.size());
			for(int i=0; i<limit;i++){
				float goldCost = getAttribut(b.getTechnologyList(plateau).get(i),Attributs.goldCost);
				float foodCost = getAttribut(b.getTechnologyList(plateau).get(i),Attributs.foodCost);
				float faithCost = getAttribut(b.getTechnologyList(plateau).get(i),Attributs.faithCost);
				float prodTime = getAttribut(b.getTechnologyList(plateau).get(i),Attributs.prodTime);
				String icon = getAttributString(b.getTechnologyList(plateau).get(i),Attributs.nameIcon);
				System.out.println(icon);
				g.drawImage(Images.get(icon), x+2f, yActionBar+2f + ratio*i*sizeYActionBar, x-5f+sizeXActionBar, yActionBar-5f+ratio*i*sizeYActionBar+sizeXActionBar, 0, 0, 512,512);
				// CHANGE PUT PRICES
				g.setColor( Player.getTeam().color);
				g.drawRect(x+1f, yActionBar+1f + i*sizeXActionBar, -6f+sizeXActionBar, -6f+sizeXActionBar);
				if(ul2.size()>i && toDrawDescription[i][1]){
					g.setColor(Color.white);
					g.drawString(ul2.get(i).name(), x + ratio*sizeYActionBar+10f, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul2.get(i).name())/2f);
					g.drawImage(imageFood,x + 3.6f*(sizeXActionBar+400f)/7 , yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul2.get(i).name())/2f);
					g.drawString(": "+(int)foodCost,x + 3.95f*(sizeXActionBar+400f)/7, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul2.get(i).name())/2f);
					g.drawImage(imageGold,x + 4.8f*(sizeXActionBar+400f)/7 , yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul2.get(i).name())/2f);
					g.drawString(": "+(int)goldCost,x + 5.15f*(sizeXActionBar+400f)/7 , yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul2.get(i).name())/2f);
					g.drawString("T: ",x + 6f*(sizeXActionBar+400f)/7, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul2.get(i).name())/2f);
					g.drawString(Integer.toString(((int)prodTime)),x + 6.35f*(sizeXActionBar+400f)/7 , yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul2.get(i).name())/2f);
				}
			}
			g.translate(-sizeXActionBar, 0f);
		}
		else if(selection.size()>0 && plateau.getById(selection.get(0)) instanceof Character){
			mouseOnActionBar = true;
			Character b =(Character) plateau.getById(selection.get(0));
			//Print building capacities
			Vector<Spell> ul = b.getSpells();
			int limit = Math.min(5, ul.size());
			Vector<Float> state = b.getSpellsState();
			Font f = g.getFont();
			Image im;
			for(int i=0; i<limit;i++){ 
				if(state.get(i)==ul.get(i).getAttribut(Attributs.chargeTime)){
					g.setColor(Color.white);
				} else {
					g.setColor( Player.getTeam().color);
				}
				if(spellCurrent==b.getSpells().get(i).name){
					g.setColor(Color.orange);
				}
				g.drawRect(x+1f, yActionBar+1f + i*sizeXActionBar, -6f+sizeXActionBar, -6f+sizeXActionBar);
				im = Images.get("spell"+ul.get(i).name);
				g.drawImage(im, x+2f, yActionBar+2f + ratio*i*sizeYActionBar, x-5f+sizeXActionBar, yActionBar-5f+ratio*i*sizeYActionBar+sizeXActionBar, 0, 0, 512,512);
				Color c =  Player.getTeam().color;
				c.a = 0.8f;
				g.setColor(c);
				if(state.get(i)>10){
					float diffY = (int)((-5f+sizeXActionBar)*(state.get(i))/ul.get(i).getAttribut(Attributs.chargeTime));
					g.fillRect(x+2f, yActionBar+2f + ratio*i*sizeYActionBar+diffY, x-5f+sizeXActionBar, -5f+sizeXActionBar-diffY);
				}
				g.setColor(Color.white);

				if(ul.size()>i && toDrawDescription[i][0]){
					g.translate(sizeXActionBar, 0f);
					g.setColor(Color.white);
					if(ul.get(i).getAttribut(Attributs.chargeTime)>0)
						if(state.get(i)>=ul.get(i).getAttribut(Attributs.chargeTime))
							g.drawString(ul.get(i).name.name(), x + ratio*sizeYActionBar+10f, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name.name())/2f);
						else
							g.drawString(ul.get(i).name+" - "+(int)(100*state.get(i)/ul.get(i).getAttribut(Attributs.chargeTime))+"%", x + ratio*sizeYActionBar+10f, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name.name())/2f);
					else
						g.drawString(ul.get(i).name.name(), x + ratio*sizeYActionBar+10f, yActionBar + ratio*i*sizeYActionBar + ratio/2f*sizeYActionBar - f.getHeight(ul.get(i).name.name())/2f);
					g.translate(-sizeXActionBar, 0f);
				}

			}
		} else {
			mouseOnActionBar = false;
		}

	}

	public static void drawTopInterface(Graphics g, Plateau plateau){
		String s;
		float rX = Game.resX;
		float rY = Game.resY;
		float offset = ratioSizeTimerY*rY;
		float yCentral = Math.max(-offset-10,Math.min(0, offset*(plateau.round-debutC-dureeDescente)/dureeDescente));
		offset = ratioSizeGoldY*rY;
		float y1 = Math.max(-offset-10,Math.min(0, offset*(plateau.round-debut1-dureeDescente)/dureeDescente));
		float y2 = Math.max(-offset-10,Math.min(0, offset*(plateau.round-debut2-dureeDescente)/dureeDescente));

		if(food != Player.getTeam().food)
			food += (Player.getTeam().food-food)/5+Math.signum(Player.getTeam().food-food);

		// pop
		Utils.drawNiceRect(g, Player.getTeam().color,(1-ratioSizeTimerX)*rX/2-2*ratioSizeGoldX*rX,y1,ratioSizeGoldX*rX+4,ratioSizeGoldY*rY);
		s = ""+Player.getTeam().getPop(plateau) + "/" + Player.getTeam().getMaxPop(plateau);
		if(Player.getTeam().getPop(plateau)==Player.getTeam().getMaxPop(plateau)){
			g.setColor(Color.red);
		}else{
			g.setColor(Color.white);
		}
		g.drawString(s, (1-ratioSizeTimerX)*rX/2-ratioSizeGoldX*rX-10f-GraphicElements.font_main.getWidth(s), y1+ratioSizeGoldY*rY/2f-GraphicElements.font_main.getHeight("0")/2-3f);
		g.drawImage(Images.get("imagePop"), (1-ratioSizeTimerX)*rX/2-2*ratioSizeGoldX*rX+10, y1+ratioSizeGoldY*rY/2f-3-Images.get("imagefooddisplayressources").getHeight()/2);

		// food
		Utils.drawNiceRect(g, Player.getTeam().color,(1-ratioSizeTimerX)*rX/2-ratioSizeGoldX*rX,y1,ratioSizeGoldX*rX+4,ratioSizeGoldY*rY);
		s = ""+food;
		g.setColor(Color.white);
		g.drawString(s, (1-ratioSizeTimerX)*rX/2-10f-GraphicElements.font_main.getWidth(s), y1+ratioSizeGoldY*rY/2f-GraphicElements.font_main.getHeight("0")/2-3f);
		g.drawImage(Images.get("imagefooddisplayressources"), (1-ratioSizeTimerX)*rX/2-ratioSizeGoldX*rX+10, y1+ratioSizeGoldY*rY/2f-3-Images.get("imagefooddisplayressources").getHeight()/2);

		// timer
		Utils.drawNiceRect(g, Player.getTeam().color,(1-ratioSizeTimerX)*rX/2,yCentral,ratioSizeTimerX*rX,ratioSizeTimerY*rY);
		g.setColor(Color.white);
		s = "todo : timer";
		//		s = ""+Utils.displayTime((int) ((System.currentTimeMillis()-Game.gameSystem.startTime)/1000));
		g.drawString(s, rX/2-GraphicElements.font_main.getWidth(s)/2f, yCentral+2*ratioSizeTimerY*rY/3f-GraphicElements.font_main.getHeight(s)/2f);

		// timer kill
		Team gt = Player.getTeam();
		float opacity = 255f;
		float centerx = 70, centery = 70;
		float r = 25f;
		if(gt.nbKill>0){
			if(offsetYkillingSpree!=0){
				offsetYkillingSpree*=0.5f;
				if(offsetYkillingSpree>-1){
					offsetYkillingSpree = 0f;
				}
			}
			g.setColor(new Color(0f,0f,0f,opacity));
			g.fillOval(centerx-r-10, centery+offsetYkillingSpree-r-10, 2*r+20f, 2*r+20f);
			//g.setColor(new Color(0f,0f,0f,opacity));
			//g.fillOval(x-r-8f, y-offsetY-r-8f, 2*r+16f, 2*r+16f);
			//						g.setColor(Color.white);
			//						g.fillOval(x-r-2f, y-sizeY/2-r-2f, 2*r+4f, 2*r+4f);
			g.setColor(new Color(gt.color.r,gt.color.g,gt.color.b,opacity));
			float startAngle = 270f;
			float sizeAngle = (float)(1f*gt.timerKill*(360f)/gt.timerMaxKill);
			g.fillArc(centerx-r-8f, centery+offsetYkillingSpree-r-8f, 2*r+16f, 2*r+16f, startAngle, startAngle+sizeAngle);
			g.setColor(new Color(0f,0f,0f,opacity));
			g.fillOval(centerx-r, centery+offsetYkillingSpree-r, 2*r, 2*r);
			g.setColor(new Color(1f,1f,1f,opacity));
			g.drawString(""+gt.nbKill, centerx-GraphicElements.font_main.getWidth(""+gt.nbKill)/2, centery+offsetYkillingSpree-GraphicElements.font_main.getHeight(""+gt.nbKill)/2);
		} else {
			offsetYkillingSpree = -150f;
		}

	}

	public static void drawMiniMap(Graphics g, Camera camera, Plateau plateau){
		offsetDrawX = Math.max(0, Math.min(sizeXMiniMap+10, -sizeXMiniMap*(plateau.round-debutGlissade-dureeGlissade)/dureeGlissade));
		Utils.drawNiceRect(g,  Player.getTeam().color,startX2MiniMap+offsetDrawX-3, startY2MiniMap-3, sizeXMiniMap+9, sizeYMiniMap+9);
		g.setColor(Color.black);
		g.fillRect(startX2MiniMap+offsetDrawX, startY2MiniMap, sizeXMiniMap, sizeYMiniMap);
		// Find the high left corner
		float hlx = Math.max(startXMiniMap,startXMiniMap+ratioWidthMiniMap*camera.Xcam);
		float hly = Math.max(startYMiniMap,startYMiniMap+ratioHeightMiniMap*camera.Ycam);
		float brx = Math.min(startXMiniMap+widthMiniMap,startXMiniMap+ratioWidthMiniMap*(camera.Xcam+Game.resX));
		float bry = Math.min(startYMiniMap+heightMiniMap,startYMiniMap+ratioHeightMiniMap*(camera.Ycam+Game.resY));
		// Find the bottom right corner

		// Draw background
		g.setColor(new Color(0.1f,0.4f,0.1f));
		g.drawImage(Images.get("islandTexture"),startXMiniMap+offsetDrawX, startYMiniMap, startXMiniMap+offsetDrawX+widthMiniMap, startYMiniMap+heightMiniMap,0,0,Images.get("islandTexture").getWidth(),Images.get("islandTexture").getHeight());
		for(NaturalObjet q : plateau.naturalObjets){
			g.setColor(Color.green);
			g.fillRect(startXMiniMap+offsetDrawX+ratioWidthMiniMap*q.x-ratioWidthMiniMap*q.sizeX/2f, startYMiniMap+ratioHeightMiniMap*q.y-ratioHeightMiniMap*q.sizeY/2f,ratioWidthMiniMap*q.sizeX , ratioHeightMiniMap*q.sizeY);
		}
		// Draw units on camera 
		g.setAntiAlias(true);
		for(Character c : plateau.characters){		
			if(c.getTeam().id==2){
				if(plateau.isVisibleByTeam(Player.getTeamId(), c)){
					g.setColor(Colors.team2);
					float r = c.collisionBox.getBoundingCircleRadius();
					g.fillOval(startXMiniMap+offsetDrawX+ratioWidthMiniMap*c.x-ratioWidthMiniMap*r, startYMiniMap+ratioHeightMiniMap*c.y-ratioHeightMiniMap*r, 2f*ratioWidthMiniMap*r, 2f*ratioHeightMiniMap*r);
				}
			}
			else if(c.getTeam().id==1){
				if(plateau.isVisibleByTeam(Player.getTeamId(), c)){
					g.setColor(Colors.team1);
					float r = c.collisionBox.getBoundingCircleRadius();
					g.fillOval(startXMiniMap+offsetDrawX+ratioWidthMiniMap*c.x-ratioWidthMiniMap*r, startYMiniMap+ratioHeightMiniMap*c.y-ratioHeightMiniMap*r, 2f*ratioWidthMiniMap*r, 2f*ratioHeightMiniMap*r);
				}
			}
		}


		for(Bonus c : plateau.bonus){
			if(c.getTeam().id==0){
				g.setColor(Colors.team0);

			}
			if(c.getTeam().id==2){
				if(plateau.isVisibleByTeam(Player.getTeamId(), c)){
					g.setColor(Colors.team2);
				} else {
					g.setColor(Colors.team0);

				}
			}
			else if(c.getTeam().id==1){
				if(plateau.isVisibleByTeam(Player.getTeamId(), c)){
					g.setColor(Colors.team1);
				} else {
					g.setColor(Colors.team0);

				}
			}
			g.fillOval(startXMiniMap+offsetDrawX+ratioWidthMiniMap*(c.x-c.getAttribut(Attributs.size)/2f), 
					startYMiniMap+ratioHeightMiniMap*(c.y-c.getAttribut(Attributs.size)/2f), 
					ratioWidthMiniMap*c.getAttribut(Attributs.size), 
					ratioHeightMiniMap*c.getAttribut(Attributs.size));
		}
		g.setAntiAlias(false);
		for(Building c : plateau.buildings){
			if(c.getTeam().id==0){
				g.setColor(Colors.team0);

			}
			if(c.getTeam().id==2){
				if(plateau.isVisibleByTeam(Player.getTeamId(), c) || Debug.debugFog){
					g.setColor(Colors.team2);
				} else {
					g.setColor(Colors.team0);

				}
			}
			else if(c.getTeam().id==1){
				if(plateau.isVisibleByTeam(Player.getTeamId(), c) || Debug.debugFog){
					g.setColor(Colors.team1);
				} else {
					g.setColor(Colors.team0);

				}
			}
			g.fillRect(startXMiniMap+offsetDrawX+ratioWidthMiniMap*c.x-ratioWidthMiniMap*c.getAttribut(Attributs.sizeX)/2f, startYMiniMap+ratioHeightMiniMap*c.y-ratioHeightMiniMap*c.getAttribut(Attributs.sizeY)/2f, ratioWidthMiniMap*c.getAttribut(Attributs.sizeX), ratioHeightMiniMap*c.getAttribut(Attributs.sizeY));

			if(c.constructionPoints<c.getAttribut(Attributs.maxLifepoints) && (plateau.isVisibleByTeam(Player.getTeamId(), c) || Debug.debugFog)){
				float ratio = c.constructionPoints/c.getAttribut(Attributs.maxLifepoints); 
				if(c.potentialTeam==1){
					g.setColor(Colors.team1);
				}
				else if(c.potentialTeam==2){
					g.setColor(Colors.team2);
				}
				g.fillRect(startXMiniMap+offsetDrawX+ratioWidthMiniMap*c.x-ratioWidthMiniMap*c.getAttribut(Attributs.sizeX)/2f, startYMiniMap+ratioHeightMiniMap*c.y-ratioHeightMiniMap*c.getAttribut(Attributs.sizeY)/2f, ratio*(ratioWidthMiniMap*c.getAttribut(Attributs.sizeX)), ratioHeightMiniMap*c.getAttribut(Attributs.sizeY));
			}
		}

		// Draw rect of camera 
		g.setColor(Color.white);
		g.drawRect(hlx+offsetDrawX,hly,brx-hlx,bry-hly );
	}

	public static void drawSpell(Graphics g, Camera camera, Plateau plateau){
		if(spellCurrent!=null){
			Character characterSpellLauncher = (Character) plateau.getById(spellLauncher);
			g.translate(-camera.Xcam, -camera.Ycam);
			Spell s = characterSpellLauncher.getSpell(spellCurrent);
			s.drawCast(g, plateau.getById(spellTarget), spellX, spellY, characterSpellLauncher, true, plateau);
			g.translate(camera.Xcam, camera.Ycam);
		}
	}

	//////
	// Utils
	//////

	public static Float getAttribut(ObjetsList o, Attributs a){
		return Player.getTeam().data.getAttribut(o, a);
	}

	public static String getAttributString(ObjetsList o, Attributs a){
		return Player.getTeam().data.getAttributString(o, a);
	}

	public static boolean isMouseOnActionBar(float xMouse, float yMouse){
		return xMouse > startXActionBar && xMouse < startXActionBar + 2*sizeXActionBar
				&& yMouse > startYActionBar && yMouse < startYActionBar + sizeYActionBar;
	}

	public static boolean isMouseOnTopBar(float xMouse, float yMouse){
		return xMouse > (1-ratioSizeTimerX)*Game.resX/2-2*ratioSizeGoldX*Game.resX 
				&& xMouse < (1+ratioSizeTimerX)*Game.resX/2+2*ratioSizeGoldX*Game.resX
				&& yMouse >0 && yMouse < ratioSizeTimerY*Game.resY;
	}

	public static boolean isMouseOnMiniMap(float xMouse, float yMouse){
		return xMouse > startXMiniMap + offsetDrawX && xMouse < startXMiniMap+ widthMiniMap
				&& yMouse > startYMiniMap && yMouse < startYMiniMap + heightMiniMap;
	}

	public static void resetCurrentSpell(){
		spellCurrent = null;
		spellOk = false;
		spellLauncher = null;
		spellX = 0;
		spellY = 0;
	}
	
	///////
	// Icone
	///////

	public class Icon{
		private float x, y, sizeX, sizeY;
		private String image;
		private Vector<String> texte;
		private int foodCost, popCost;
		private int quartdecran;
		private float startXBulle, startYBulle, sizeXBulle, sizeYBulle;

		private static final float ratioSizeX=1/4f;
		private static final float ratioSizeY=1/8f;

		public boolean isMouseOnIt;

		private Image imagetemp;

		public Icon(float x, float y, float sizeX, float sizeY, String image, Vector<String> texte, int foodCost, int popCost){
			x = x;
			y = y;
			sizeX = sizeX;
			sizeY = sizeY;
			image = image;
			texte = texte;
			foodCost = foodCost;
			popCost = popCost;
			if(x<Game.resX/2){
				if(y<Game.resY/2){
					quartdecran = 7;
				} else {
					quartdecran = 1;					
				}
			} else {
				if(y<Game.resY/2){
					quartdecran = 9;
				} else {
					quartdecran = 3;					
				}
			}
			sizeXBulle = ratioSizeX*Game.resX;
			sizeYBulle = ratioSizeY*Game.resY;
			startXBulle = x-(quartdecran%6>1?sizeXBulle:0);
			startYBulle = y-(quartdecran/6>1?0:sizeYBulle);
		}

		public void changeXY(float x, float y){
			x = x;
			y = y;
			startXBulle = x-(quartdecran%6>1?sizeXBulle:0);
			startYBulle = y-(quartdecran/6>1?sizeYBulle:0);
		}

		public void update(float xMouse, float yMouse){
			isMouseOnIt = xMouse>x-sizeX/2 && xMouse<x+sizeX/2 && yMouse>y-sizeY/2 && yMouse<y+sizeY/2;
		}

		public void draw(Graphics g){
			if(isMouseOnIt){
				g.setColor(Color.darkGray);
			} else {
				g.setColor(Color.white);
			}
			g.fillRect(x-sizeX/2, y-sizeY/2, sizeX, sizeY);
			imagetemp = Images.get(image);
			g.drawImage(imagetemp, x-sizeX/2, y-sizeY/2, x+sizeX/2, y+sizeY/2, 
					0, 0, imagetemp.getWidth(), imagetemp.getHeight());


		}

		public void drawAfter(Graphics g){
			if(isMouseOnIt){
				Utils.drawNiceRect(g,  Player.getTeam().color, startXBulle, startYBulle, sizeXBulle, sizeYBulle);
				g.setColor(Color.darkGray);
				g.fillRect(startXBulle+5, startYBulle+5, sizeYBulle-10, sizeYBulle-10);
				g.drawImage(imagetemp, startXBulle+10, startYBulle+10, startXBulle+sizeYBulle-10, startYBulle+sizeYBulle-10, 
						0, 0, imagetemp.getWidth(), imagetemp.getHeight());
				g.setColor(Color.white);
				int i=0;
				for(String s : texte){
					g.drawString(s, startXBulle+sizeYBulle+10, startYBulle+5+i*g.getFont().getHeight("Hj"));
					i+=1;
				}
			}
		}
	}

	public static void init(Plateau plateau) {
		updateRatioMiniMap(plateau);
	}
}
