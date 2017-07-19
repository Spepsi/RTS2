package multiplaying;

import java.io.Serializable;

import org.newdawn.slick.Color;

import main.Main;
import model.Game;

public class ChatMessage implements Serializable{

	public String nickname;
	public String message;
	public int remainingTime;
	public Color color;
	
	public static int basicTime = Main.framerate*2;
	public static int multiTime = Main.framerate*7;

	public ChatMessage(String message, String nickname, Color color) {
		this.message = message;
		this.nickname = nickname;
		this.color = color;
		this.remainingTime = multiTime;
	}
	
	public ChatMessage(String message){
		this.message = message;
		this.nickname = "";
		this.color = Color.white;
		this.remainingTime = basicTime;
	}
	
	public enum MessageType{
		NOTENOUGHFOOD,
		BUILDINGTAKEN,
		RESEARCHCOMPLETE,
		NOTENOUGHMANA,
		NOTENOUGHPOP,
		BUILDINGUNTAKABLE,
		UNDERATTACK,
		UNITCOMPLETE;
		
	}

	public static ChatMessage getById(MessageType s){
		switch(s){
		case NOTENOUGHFOOD :
//			Game.g.sounds.get("messageWrong").play(1f,Game.g.options.soundVolume );
			return new ChatMessage("Pas assez de nourriture");
		case BUILDINGTAKEN:
//			Game.g.sounds.get("buildingTaken").play(1f,Game.g.options.soundVolume );
			return new ChatMessage("B�timent captur�");
		case RESEARCHCOMPLETE : 
//			Game.g.sounds.get("techDiscovered").play(1f,Game.g.options.soundVolume );
			return new ChatMessage("Technologie d�couverte");
		case NOTENOUGHMANA :
//			Game.g.sounds.get("messageWrong").play(1f,Game.g.options.soundVolume );
			return new ChatMessage("Pas assez de mana");
		case NOTENOUGHPOP :
//			Game.g.sounds.get("messageWrong").play(1f,Game.g.options.soundVolume );
			return new ChatMessage("Limite de population atteinte");
		case BUILDINGUNTAKABLE :
//			Game.g.sounds.get("messageWrong").play(1f,Game.g.options.soundVolume );
			return new ChatMessage("Capture impossible");
		case UNDERATTACK :
//			Game.g.sounds.get("messageWrong").play(1f,Game.g.options.soundVolume );
			return new ChatMessage("Vous �tes attaqu� !");
		case UNITCOMPLETE :
//			Game.g.sounds.get("messageWrong").play(1f,Game.g.options.soundVolume );
			return new ChatMessage("Unit� produite");
		default : 
			return null;
		}
	}
	


}
