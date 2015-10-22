package menu;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import model.Game;
import model.Map;
import multiplaying.MultiReceiver;
import multiplaying.MultiSender;

public class MenuMulti extends Menu {

	Image title;
	public Image marbre;
	public Image host;
	public Image join;
	public Image back;

	public Image hostSelected;
	public Image joinSelected;
	public Image backSelected;

	public boolean inHost, inJoin;

	//TODO upgrading multiplayer
	public int cooldown;
	public int cooldown1;
	
	public float startXGames;
	float startY;
	float stepY;
	float ratioReso;
	
	public Vector<OpenGames> openGames;

	public MenuMulti(Game game){
		this.music = game.musics.menu;
		this.music.loop();
		this.music.setVolume(game.options.musicVolume);
		this.game = game;
		this.items = new Vector<Menu_Item>();
		startY = 100f+0.1f*this.game.resX;
		stepY = 0.15f*this.game.resY;
		startXGames = 3f*this.game.resX/8;
		ratioReso = this.game.resX/2800f;
		try {
			this.host = new Image("pics/menu/host.png").getScaledCopy(ratioReso);
			this.hostSelected = new Image("pics/menu/hostselected.png").getScaledCopy(ratioReso);
			this.join= new Image("pics/menu/join.png").getScaledCopy(ratioReso);
			this.joinSelected= new Image("pics/menu/joinselected.png").getScaledCopy(ratioReso);
			this.back = new Image("pics/menu/back.png").getScaledCopy(ratioReso);
			this.backSelected = new Image("pics/menu/backselected.png").getScaledCopy(ratioReso);
			this.marbre = new Image("pics/menu/marbre.png");
			float startX = 2f*this.game.resX/8-this.host.getWidth()/2;
			this.items.addElement(new Menu_Item(startX,startY,this.host,this.hostSelected,this.game));
			this.items.addElement(new Menu_Item(startX,startY+1*stepY,this.join,this.joinSelected,this.game));
			this.items.addElement(new Menu_Item(startX,startY+2*stepY,this.back,this.backSelected,this.game));
			this.items.addElement(new Menu_Item(startXGames,startY,this.marbre,this.marbre,this.game));
			this.items.lastElement().selectionable = false;

		} catch (SlickException e1) {
			e1.printStackTrace();
		}
		//		}
		try{
			this.sounds = game.sounds;
			this.title = new Image("pics/menu/goldtitle.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	public void callItem(int i){
		switch(i){
		case 0:
			inHost = true;
			break;
		case 1:
			inJoin = true;
			break;
		case 2:
			this.game.setMenu(this.game.menuIntro);
			break;
		default:		
		}
	}

	public void draw(Graphics g){

		g.setColor(Color.black);
		g.fillRect(0, 0, this.game.resX, this.game.resY);
		for(Menu_Item item: this.items){
			item.draw(g);
		}
		g.drawImage(this.title, this.game.resX/2f-this.title.getWidth()/2, 50f+0.05f*this.game.resY-this.title.getHeight()/2);
		g.setColor(Color.white);
		if(inHost || inJoin){
			g.drawString("waiting for someone", this.game.resX/2f-g.getFont().getWidth("waiting for someone")/2f, this.game.resY-g.getFont().getHeight("waiting for someone")-2f);
		}
		g.setColor(Color.black);
		g.drawString("Open games: ", this.startXGames+70f, startY+50f);
	}

	public void update(Input i){
		if(inHost && i.isKeyPressed(Input.KEY_ESCAPE)){ 
			inHost = false;
			this.game.connexionSender.interrupt();
		}
		if(inHost){
			this.game.host = true;
			game.inMultiplayer = true;
			if(cooldown==0){
				if(!game.connexionSender.isAlive()){
					game.connexionSender.start();
				}
				String s="";
				try {
					s = InetAddress.getLocalHost().getHostAddress();
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				String[] tab = s.split("\\.");
				s = "";
				for(int k=0; k<tab.length-1;k++){
					s += tab[k]+".";
				}
				String thisAddress;
				try {
					thisAddress = InetAddress.getLocalHost().getHostAddress();
					for(int ip=0; ip<255;ip++){
						if(!thisAddress.equals(s+""+ip)){
							this.game.connexionSender.address = InetAddress.getByName(s+""+ip);
							//this.game.toSendConnexions.addElement("2"+thisAddress+","+this.game.options.nickname);
							this.game.toSendConnexions.addElement("2"+thisAddress);
						}
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				cooldown++;
			}else if(cooldown<=60){
				cooldown++;
			} else {
				cooldown=0;				
			}
			if(this.game.connexions.size()>0){
				try {
					this.game.addressClient = InetAddress.getByName(this.game.connexions.get(0));
					this.game.outputSender = new MultiSender(this.game.addressClient,this.game.portOutput,this.game.toSendOutputs,this.game);
					this.game.inputReceiver = new MultiReceiver(this.game,this.game.portInput);
					this.game.outputSender.start();
					this.game.inputReceiver.start();
					this.music = game.musics.imperial;
					this.music.loop();
					this.music.setVolume(game.options.musicVolume);
					Map.updateMap(3, game);
					this.game.quitMenu();
				} catch (UnknownHostException e1) {
					e1.printStackTrace();
				}
			}
		} else if(inJoin) {
			game.inMultiplayer = true;
			game.host = false;
			try {
				game.addressClient = InetAddress.getLocalHost();
			} catch (UnknownHostException e2) {
				e2.printStackTrace();
			}
			if(this.game.connexions.size()>0){
				this.game.connexionSender = new MultiSender(game.addressHost,game.portConnexion,game.toSendConnexions,this.game);
				game.connexionSender.start();
				this.game.inputSender = new MultiSender(this.game.addressHost,this.game.portInput,this.game.toSendInputs,this.game);
				this.game.outputReceiver = new MultiReceiver(this.game,this.game.portOutput);
				this.game.inputSender.start();
				this.game.outputReceiver.start();

				this.game.toSendConnexions.addElement("2"+this.game.addressClient.getHostAddress());
				this.game.toSendConnexions.addElement("2"+this.game.addressClient.getHostAddress());
				this.game.toSendConnexions.addElement("2"+this.game.addressClient.getHostAddress());
				try{
					Thread.sleep(5);
				} catch(InterruptedException e) { }
				game.inMultiplayer = true;
				game.currentPlayer = 2;
				this.music = game.musics.imperial;
				this.music.loop();
				this.music.setVolume(game.options.musicVolume);
				Map.updateMap(3, game);
				this.game.quitMenu();
			}
		} else if(i!=null){
			if(i.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
				callItems(i);
				this.game.sounds.menuItemSelected.play(1f,game.options.soundVolume);
			}
			for(Menu_Item item: this.items){
				item.update(i);
			}			
		}
	} 
	
	public class OpenGames{
		
		String hostName;
		InetAddress hostAddress;
		
		public OpenGames(String hostName, InetAddress hostAddress) {
			this.hostName = hostName;
			this.hostAddress = hostAddress;
		}
		
	}
}
