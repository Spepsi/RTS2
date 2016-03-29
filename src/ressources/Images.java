package ressources;

import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Point;

import buildings.Building;
import buildings.BuildingsList;
import display.DisplayRessources;
import main.Main;
import model.Data;
import nature.Tree;
import tests.FatalGillesError;
import units.Character;


public class Images {

	private HashMap<String, Image> images;
	private HashMap<String, Image> oldimages;
	private HashMap<String, HashMap<String, Image>> imagesUnits;
	private HashMap<String, Image> sand;	


	public Images(){
		this.images = new HashMap<String, Image>();
		this.loadRepertoire("ressources/images/");
		this.initialize();
		this.initializeUnits();
	}

	private void initialize() {
		// import other images that will serve during the game
		// icons
		HashMap<String, Image> toPut = new HashMap<String, Image>();
		for(String im : this.images.keySet()){
			if(im.contains("icon") || im.contains("tech")){
				toPut.put(im+"buildingsize", this.images.get(im).getScaledCopy(Building.sizeXIcon,Building.sizeXIcon));
			}
		}
		this.images.putAll(toPut);
		// ressources
		int taille = DisplayRessources.taille;
		this.images.put("imagegolddisplayressources",this.images.get("imagegold").getSubImage(7*taille ,15*taille ,taille, taille));
		this.images.put("imagefooddisplayressources",this.images.get("imagegold").getSubImage(7*taille ,taille ,taille, taille));
		this.images.put("rectselectsizebuilding",this.images.get("rectselect").getScaledCopy(4f));
		this.images.put("imagepop", this.images.get("imagepop").getScaledCopy(32,32));

		// buildings
		this.resizeBuilding("academy");
		this.resizeBuilding("barrack");
		this.resizeBuilding("headquarters");
		this.resizeBuilding("mill");
		this.resizeBuilding("mine");
		this.resizeBuilding("stable");
		this.resizeBuilding("tower");
		this.resizeBuilding("university");

		// bullets
		this.images.put("arrow",this.images.get("arrow").getScaledCopy(2f*Main.ratioSpace));

		// trees
		for(String im : this.images.keySet()){
			if(im.contains("tree")){		
				this.images.put(im, this.images.get(im).getScaledCopy(Tree.coeffDraw));
			}
		}
		this.initializeSand();
	}

	public void initializeSand(){

		Image im = this.images.get("sandtile");
		this.sand = new HashMap<String, Image> ();
		HashMap<String, Image> temp = new HashMap<String, Image>();
		temp.put("3", im.getSubImage(128*0, 128*0, 128, 128));
		temp.put("4", im.getSubImage(128*8, 128*2, 128, 128));
		temp.put("2", im.getSubImage(128*0, 128*1, 128, 128));
		temp.put("8", im.getSubImage(128*4, 128*4, 128, 128));
		temp.put("6", im.getSubImage(128*4, 128*3, 128, 128));
		temp.put("7", im.getSubImage(128*6, 128*0, 128, 128));
		temp.put("1", im.getSubImage(128*1, 128*0, 128, 128));
		temp.put("5", im.getSubImage(128*3, 128*4, 128, 128));

		// special earth
		//sand.put("A111", im.getSubImage(128*4+50, 128*4+50, 128, 128).getScaledCopy((int)Map.stepGrid, (int)Map.stepGrid));
		this.sand.put("E", im.getSubImage(256, 256, 256, 256));
		int w = 256;
		int h = 256;

		for(int a = 1; a<=8; a++){
			for(int b = 1 ; b<=8; b++){
				for(int c = 1; c<=8; c++){
					for(int d = 1; d<=8; d++){
						if(isValid(a,b,c,d)){
							try {
								im = new Image(w, h);
								Graphics g = im.getGraphics();
								g.drawImage(temp.get(""+a), 0, 0);
								temp.get(""+b).rotate(90);
								g.drawImage(temp.get(""+b), w/2, 0);
								temp.get(""+b).rotate(-90);
								temp.get(""+c).rotate(180);
								g.drawImage(temp.get(""+c), w/2, h/2);
								temp.get(""+c).rotate(-180);
								temp.get(""+d).rotate(270);
								g.drawImage(temp.get(""+d), 0, h/2);
								temp.get(""+d).rotate(-270);
								g.flush();
								this.sand.put(""+a+""+b+""+c+""+d, im);
							} catch (SlickException e) {
								e.printStackTrace();
							}   

						}
					}
				}
			}
		}
	}

	private boolean isValid(int a, int b, int c, int d){
		return isValid(a,b) && isValid(b,c) && isValid(c,d) && isValid(d,a);
	}

	private boolean isValid(int a, int b){
		switch(a){
		case 1: return b==2 || b==3;
		case 2: return b==1 || b==4 || b==5 || b==8;
		case 3: return b==2 || b==3;
		case 4: return b==1 || b==4 || b==5 || b==8;
		case 5: return b==6 || b==7;
		case 6: return b==1 || b==4 || b==5 || b==8;
		case 7: return b==6 || b==7;
		case 8: return b==1 || b==4 || b==5 || b==8;
		}
		return false;
	}

	public void resizeBuilding(String s){
		Point p = Data.getSize(s);
		this.images.put("building"+s+"blue",this.images.get("building"+s+"blue")
				.getScaledCopy((int)(2*p.getX()/1.8), (int)(3*p.getY()/(2))));
		if(!s.equals("headquarters"))
			this.images.put("building"+s+"neutral",this.images.get("building"+s+"neutral")
					.getScaledCopy((int)(2*p.getX()/1.8), (int)(3*p.getY()/(2))));
		this.images.put("building"+s+"red",this.images.get("building"+s+"red")
				.getScaledCopy((int)(2*p.getX()/1.8), (int)(3*p.getY()/(2))));

	}

	private void loadRepertoire(String name){
		File repertoire = new File(name);
		File[] files=repertoire.listFiles();
		String s;
		Image im;
		try {
			for(int i=0; i<files.length; i++){
				s = files[i].getName();
				if(s.contains(".png")){
					// on load l'image
					s = s.substring(0, s.length()-4);
					im = new Image(name+s+".png");
					this.images.put(s.toLowerCase(),im);
					//					f = Images.class.getField(s);
					//					f.set(this, im);
					//this.images.put(s, new Image(name+s+".png"));
				} else if(s.contains(".jpg")){
					// on load l'image
					s = s.substring(0, s.length()-4);
					im = new Image(name+s+".jpg");
					this.images.put(s,im);
					//this.images.put(s, new Image(name+s+".jpg"));
				} else if(s.contains(".svg")){
					// on load l'image
					s = s.substring(0, s.length()-4);
					im = new Image(name+s+".svg");
					this.images.put(s,im);
					//this.images.put(s, new Image(name+s+".svg"));
				} else if (!s.contains(".") && !s.equals("unit")){
					// nouveau r�pertoire
					this.loadRepertoire(name+s+"/");

				}
			} 
		} catch (SlickException | SecurityException | IllegalArgumentException  e) {
			e.printStackTrace();
		}
	}

	public Image get(String name){
		if(this.images.containsKey(name.toLowerCase())){
			return this.images.get(name.toLowerCase());
		} else {
			System.out.println("Error : trying to load an non-existing image : "+name);
			try {
				throw new FatalGillesError("non-existing image : "+name);
			} catch (FatalGillesError e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private void initializeUnits(){
		File repertoire = new File("ressources/images/unit");
		File[] files=repertoire.listFiles();
		String s,s2,s4;
		this.imagesUnits = new HashMap<String, HashMap<String, Image>>();
		this.imagesUnits.put("spearman", new HashMap<String, Image>());
		this.imagesUnits.put("crossbowman", new HashMap<String, Image>());
		this.imagesUnits.put("knight", new HashMap<String, Image>());
		this.imagesUnits.put("priest", new HashMap<String, Image>());
		this.imagesUnits.put("inquisitor", new HashMap<String, Image>());
		Image im;
		int imageHeight, imageWidth;
		try {
			for(int i=0; i<files.length; i++){
				s = files[i].getName();
				if(s.contains(".png")){
					// on load l'image
					im = new Image("ressources/images/unit/"+s);
					imageHeight = im.getHeight()/4;
					imageWidth = im.getWidth()/5;
					s = s.substring(0, s.length()-4);
					for(String s1 : this.imagesUnits.keySet()){
						if(s.toLowerCase().contains(s1)){
							s2 = (s.toLowerCase().startsWith("attack") ? "1" : "0");
							s4 = (s.toLowerCase().endsWith("blue") ? "1" : "2");
							for(int orientation =0; orientation<4; orientation++){
								for(int animation=0; animation<5; animation++){
									this.imagesUnits.get(s1).put(s2+""+orientation+""+animation+""+s4, 
											im.getSubImage(imageWidth*animation,imageHeight*(int)orientation,imageWidth,imageHeight)
											.getScaledCopy(120*Main.ratioSpace/imageWidth));
								}
							}
						}
					}
					this.images.put(s.toLowerCase(),im);
				}
			} 
			HashMap<String, Image> toAdd = new HashMap<String, Image>();
			for(String s1 : this.imagesUnits.keySet()){
				toAdd.clear();
				if(this.imagesUnits.get(s1).size()==40){
					for(String s3 : this.imagesUnits.get(s1).keySet()){
						toAdd.put("1"+s3.substring(1), this.imagesUnits.get(s1).get(s3));
					}
				}
				this.imagesUnits.get(s1).putAll(toAdd);
			}
		} catch (SlickException | SecurityException | IllegalArgumentException  e) {
			e.printStackTrace();
		}
	}

	public Image getUnit(String name, int direction, int animation, int team, boolean attack){
		if(this.imagesUnits.containsKey(name) && this.imagesUnits.get(name).containsKey((attack?"1":"0")+direction+""+animation+"" +team)){
			return this.imagesUnits.get(name).get((attack?"1":"0")+direction+""+animation+"" +team);
		} else {
			try {
				throw new FatalGillesError("images d'unit� non existante : "+name+":"+animation+" "+direction+" " +attack+" "+team);
			} catch (FatalGillesError e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void activateGdBMode() {
		Image GdB = this.images.get("gilles");
		this.oldimages = images;
		this.images = new HashMap<String, Image>();
		for(String s : this.oldimages.keySet()){
			this.images.put(s,GdB);
		}
	}

	public void deactivateGdBMode(){
		this.images.clear();
		this.images = this.oldimages;
	}

	// about sand tiles
	
	public Image getSand(String s){
		return this.sand.get(s);
	}

	public void updateScaleSend(float stepGrid) {
		HashMap<String, Image> temp = new HashMap<String, Image>();
		for(String s : this.sand.keySet()){
			temp.put(s, sand.get(s).getScaledCopy((int)(stepGrid), (int)(stepGrid)));
		}
		this.sand = temp;
		this.images.put("watertile", this.images.get("watertile").getScaledCopy((int)(2*stepGrid), (int)(2*stepGrid)));
		this.images.put("watertile2", this.images.get("watertile2").getScaledCopy((int)(stepGrid), (int)(stepGrid)));
	}
}
