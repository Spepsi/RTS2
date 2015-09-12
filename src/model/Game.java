package model;
import java.net.InetAddress;
import java.util.Vector;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.util.Log;

import buildings.Building;
import bullets.Bullet;
import display.BottomBar;
import display.TopBar;
import menu.Menu;
import menu.MenuIntro;
import menu.MenuPause;
import multiplaying.*;
import multiplaying.ConnectionModel.*;
import nature.Tree;
import nature.Water;
import spells.SpellEffect;
import units.Character;

public class Game extends BasicGame 
{	

	public int idChar = 0;
	public int idBullet = 0;

	// Music and sounds
	public float soundVolume;
	public float volume;
	public Music mainMusic ;
	public Music musicStartGame;
	public Sounds sounds;
	public Images images;
	public boolean playStartMusic = true;

	// Constants
	public Map map;
	public Image background ;
	public Constants constants;

	// Bars
	public BottomBar bottomBars;
	public TopBar topBars;
	public float relativeHeightBottomBar = 1f/6f;
	public float relativeHeightTopBar = 1f/20f;

	// Selection
	public Rectangle selection;
	public boolean new_selection;
	public Vector<Objet> objets_selection=new Vector<Objet>();

	// Resolution : 
	public float resX;
	public float resY;

	// Plateau
	public Plateau plateau ;
	public AppGameContainer app;
	public Vector<Player> players = new Vector<Player>();
	public int currentPlayer = 1;


	// Network and multiplaying
	public boolean inMultiplayer;
	public long startTime;
	public int portConnexion = 6113;
	public int portInput = 6114;
	public int portOutput = 6115;
	public int portChat = 2347;
	// Host and client
	public InetAddress addressHost;
	public InetAddress addressClient;
	public Vector<InputModel> inputs = new Vector<InputModel>();
	public Vector<InputModel> toRemoveInputs = new Vector<InputModel>();
	public Vector<String> toSendInputs = new Vector<String>();
	public Vector<OutputModel> outputs = new Vector<OutputModel>();
	public Vector<OutputModel> toRemoveOutputs = new Vector<OutputModel>();
	public Vector<String> toSendOutputs = new Vector<String>();
	public Vector<String> connexions = new Vector<String>();
	public Vector<String> toSendConnexions = new Vector<String>();
	public int timeValue;
	// Sender and Receiver
	public MultiReceiver inputReceiver = new MultiReceiver(this,portInput);
	public MultiSender inputSender = new MultiSender(this.app,this,addressHost,portInput,this.toSendInputs);
	public MultiReceiver outputReceiver = new MultiReceiver(this,portOutput);
	public MultiSender outputSender = new MultiSender(this.app,this,addressClient, portOutput, this.toSendOutputs);
	public MultiReceiver connexionReceiver = new MultiReceiver(this,portConnexion);
	public MultiSender connexionSender;
	public boolean isHost;
	//Debugging network
	public int toAdd = 0;
	public int toRemove = 0;

	// Menus
	public Menu menuPause;
	public Menu menuIntro;
	public Menu menuCurrent = null;
	public boolean isInMenu = false;

	public void quitMenu(){
		this.isInMenu = false;
		this.menuCurrent = null;
		app.setClearEachFrame(true);
		if(!this.musicStartGame.playing()){
			this.musicStartGame.play();
			this.musicStartGame.setVolume(this.volume);
		}
		this.plateau.Xcam = this.plateau.maxX/2 - this.resX/2;
		this.plateau.Ycam = this.plateau.maxY/2 -this.resY/2;
	}
	public void setMenu(Menu m){
		this.menuCurrent = m;
		this.isInMenu = true;
		app.setClearEachFrame(false);
	}


	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException 
	{
		g.translate(-plateau.Xcam,- plateau.Ycam);
		// g repr�sente le pinceau
		//g.setColor(Color.black);
		int i = 0;
		int j = 0;
		while(i<this.plateau.maxX+this.background.getWidth()){
			while(j<this.plateau.maxY+this.background.getHeight()){
				g.drawImage(this.background, i,j);
				j+=this.background.getHeight();
			}
			i+=this.background.getWidth();
			j= 0;
		}
		if(isInMenu){
			g.translate(plateau.Xcam, plateau.Ycam);
			this.menuCurrent.draw(g);
			return;
		}
		
		g.setColor(Color.black);
		g.fillRect(this.plateau.maxX, 0, this.plateau.maxX, this.plateau.maxY);
		g.fillRect(0, this.plateau.maxY, this.plateau.maxX, this.plateau.maxY);
		//g.fillRect(0,0,gc.getScreenWidth(),gc.getScreenHeight());


		// Draw the selection of your team 
		for(ActionObjet o: plateau.selection.get(currentPlayer)){
			o.drawIsSelected(g);
		}
		//Creation of the drawing Vector
		Vector<Objet> toDraw = new Vector<Objet>();
		Vector<Objet> toDrawAfter = new Vector<Objet>();
		// Draw the Action Objets
		for(Character o : plateau.characters){
			//o.draw(g);
			if(plateau.isVisibleByPlayer(currentPlayer, o))
				toDrawAfter.add(o);
		}
		for(ActionObjet o : plateau.equipments){
			//o.draw(g);
			toDraw.add(o);
		}
		// Draw the natural Objets

		for(NaturalObjet o : this.plateau.naturalObjets){
			o.draw(g);
		}
		// Draw the enemy generators
		for(Building e : this.plateau.buildings){
			if(plateau.isVisibleByPlayer(currentPlayer, e))
				toDrawAfter.add(e);
			else
				toDraw.add(e);
		}
		Utils.triY(toDraw);
		Utils.triY(toDrawAfter);
		// determine visible objets
		for(Objet o: toDraw)
			o.draw(g);
		plateau.drawFogOfWar(g);
		for(Objet o: toDrawAfter)
			o.draw(g);
		for(Bullet o : plateau.bullets){
			if(plateau.isVisibleByPlayer(currentPlayer,o))
				o.draw(g);
		}
		for(SpellEffect a: plateau.spells){
			//if(plateau.isVisibleByPlayer(currentPlayer, a))
			a.draw(g);
		}
		// Draw the selection :
		for(int player=1; player<3; player++){
			if(this.plateau.rectangleSelection.get(player) !=null){
				if(player==currentPlayer){
					g.setColor(Color.green);
				} else
					g.setColor(Color.red);
				g.draw(this.plateau.rectangleSelection.get(player));
			}
		}
		// Draw bottom bar
		g.translate(plateau.Xcam, plateau.Ycam);
		if(this.bottomBars!=null)
			this.bottomBars.draw(g);
		if(this.topBars!=null)
			this.topBars.draw(g);
		// Draw messages
		String s = this.plateau.messages.get(this.currentPlayer);
		if(s!=null){
			g.setColor(Color.red);
			Font f = g.getFont();
			float width = f.getWidth(s);
			float height = f.getHeight(s);
			g.drawString(s, (this.resX-width)/2f, this.bottomBars.y-10f-height/2f);
			if(this.plateau.messagesRemainingTime.get(this.currentPlayer)>=74){
				this.plateau.sounds.fireball.play();
			}
		}
	}
	// Do our logic 
	@Override
	public synchronized void update(GameContainer gc, int t) throws SlickException 
	{	
		InputModel im=null;
		Vector<InputModel> ims = new Vector<InputModel>();
		//System.out.println(this.plateau.characters);
		if(inMultiplayer){

			//Utils.printCurrentState(this.plateau);
			// The game is in multriplaying mode
			if(isHost){
				/* Multiplaying Host Pipeline
				 * 1 - take the input of t-5 client and host
				 * 2 - perform action() and update()
				 * 3 - send the output of the action and update step
				 */

				// Defining the clock
				timeValue = (int)(System.currentTimeMillis() - startTime)/(this.constants.FRAMERATE);

				// 1 - take the input of client and host
				for(int player = 1; player<players.size(); player++){
					if(player!=currentPlayer){
						if(this.inputs.size()>0){
							im = this.inputs.get(0);
							this.inputs.clear();
							//this.inputs.remove(0);
//							if(this.inputs.size()>0){
//								im.mix(this.inputs.get(0));
//								this.inputs.remove(0);
//							}
							ims.add(im);
							this.players.get(im.team).bottomBar.update(im.resX, im.resY);
							this.players.get(im.team).topBar.update(im.resX, im.resY);
						}
					} else {
						im = new InputModel(timeValue,currentPlayer,gc.getInput(),(int) plateau.Xcam,(int) plateau.Ycam,(int)resX,(int)resY);
						ims.add(im);
					}
				}

				// 2 - perform action() and update()
				OutputModel om = null;
				if(isInMenu){
					this.menuCurrent.update(ims.get(0));
				} else {
					om = this.plateau.update(ims);
				}

				// 3 - send the output of the action and update step;
				this.toSendOutputs.addElement(om.toString());

			} else if(!isHost){
				/* Multiplaying Client Pipeline
				 * 1 - send the input to host
				 * 2 - take the output from t-5
				 * 3 - update from the output file
				 */

				// Defining the clock
				timeValue = (int)(System.currentTimeMillis() - startTime)/(this.constants.FRAMERATE);

				// 1 - send the input to host
				im = new InputModel(timeValue,currentPlayer,gc.getInput(),(int) plateau.Xcam,(int) plateau.Ycam,(int)resX,(int)resY);

				this.toSendInputs.addElement(im.toString());

				// 2 - take the output
				OutputModel om = null;
				if(this.outputs.size()>0){
					om = this.outputs.get(0);
					om = this.outputs.remove(0);
				}
				// 3 - update from the output file
				this.outputReceiver.lock = false;
				this.plateau.updateFromOutput(om, im);


			}
		} else if (!inMultiplayer){
			// If not in multiplayer mode, dealing with the common input
			ims.add(new InputModel(0,1,gc.getInput(),(int) plateau.Xcam,(int)Math.floor(plateau.Ycam),(int)resX,(int)resY));
			// updating the game
			if(isInMenu){
				this.menuCurrent.update(ims.get(0));
			} else {
				this.plateau.update(ims);
			}

		}

	}

	public void newGame(boolean host){
		//Clean all variables
		this.plateau = new Plateau(this.constants,this.plateau.maxX,this.plateau.maxY,2,this);
		this.players = new Vector<Player>();
		this.players.add(new Player(this.plateau,0,0));
		this.players.add(new Player(this.plateau,1,0));
		this.players.add(new Player(this.plateau,2,0));
		
		if(host)
			this.map.createMapPhillipe(plateau,this.players);
		// Instantiate BottomBars for all players:
		for(int player=1; player<3; player++){
			new BottomBar(this.plateau,this.players.get(player),(int)this.resX,(int)this.resY);
			new TopBar(this.plateau,this.players.get(player),(int)this.resX,(int)this.resY);
		}
		
		this.bottomBars = this.players.get(currentPlayer).bottomBar;
		this.topBars = this.players.get(currentPlayer).topBar;
		selection = null;
	}
	public void newGame(ConnectionModel cm){
		//Clean all variables
		this.plateau.maxX = 2000f;
		this.plateau.maxY = 3000f;
		newGame(false);
		this.addressHost = cm.ia;
		for( ConnectionObjet co : cm.naturalObjets){
			if(co instanceof ConnectionTree){
				new Tree(co.x,co.y,this.plateau,((ConnectionTree) co).type);
			} else if(co instanceof ConnectionWater){
				new Water(co.x,co.y,((ConnectionWater)co).sizeX,((ConnectionWater)co).sizeY,this.plateau);
			}
		}
		this.currentPlayer = 2;
		this.bottomBars = this.players.get(currentPlayer).bottomBar;
		this.topBars = this.players.get(currentPlayer).topBar;
		this.bottomBars.update((int)resX, (int)resY);
	}
	// Init our Game objects
	@Override
	public void init(GameContainer gc) throws SlickException 
	{	

		Image cursor = new Image("pics/cursor.png");
		this.volume = 0.2f;
		this.soundVolume = 0.2f;
		gc.setMouseCursor(cursor.getSubImage(0, 0, 24, 64),5,16);
		mainMusic = new Music("music/ambiance.ogg");
		//mainMusic.setVolume(0.1f);
		//mainMusic.loop();
		this.sounds = new Sounds();
		this.images = new Images();
		

		this.plateau = new Plateau(this.constants,3000,3000,3,this);
		
		this.musicStartGame = new Music("music/nazi_start.ogg");
		this.players.add(new Player(this.plateau,0,0));
		this.players.add(new Player(this.plateau,1,0));
		this.players.add(new Player(this.plateau,2,0));
		
		


		this.background =  new Image("pics/grass1.jpg").getScaledCopy(0.6f);
		this.menuIntro = new MenuIntro(this);
		this.menuPause = new MenuPause(this);
		this.map = new Map();
		this.setMenu(menuIntro);
		this.startTime = System.currentTimeMillis();
		//		Thread t1 = new Thread(new InputListener(this, this.app, 0));
		//		t1.start();

		// Create background image manually


		try{
			Thread.sleep(10);
		} catch(InterruptedException e) {}
	}
	public Game ()
	{
		super("Ultra Mythe RTS 3.0");
	}
	public void setParams(Constants constants,float resX,float resY){
		this.constants = constants;
		this.resX = resX;
		this.resY = resY;
		//
	}
}
