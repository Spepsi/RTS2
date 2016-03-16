package tests;

import java.util.Vector;

import main.Main;
import model.Game;
import multiplaying.InputHandler;
import multiplaying.InputObject;
import multiplaying.MultiReceiver;
import multiplaying.MultiSender;

public class Test {
	
	public static void testRoundCorrect(Game g, Vector<InputObject> ims) throws FatalGillesError{
		for(InputObject io : ims){
			if(io.round+Main.nDelay!=g.round){
				throw new FatalGillesError("roundCorrect");
			}
		}
	}
	public static void testNombreInput(Game g, Vector<InputObject> ims) throws FatalGillesError{
		if(!(ims.size()==g.players.size() || ims.size()==0)){
			throw new FatalGillesError("nombre d'input incorrect");
		}
	}
	public static void testRoundInputHandler(InputHandler ih, Game g) throws FatalGillesError{
		for(InputObject io : ih.getInputs()){
			if(io.round+Main.nDelay+1<g.round)
				throw new FatalGillesError("messages non supprim�s");
		}
	}
	public static void testSizeSender(MultiSender ms) throws FatalGillesError{
		int maximalAutorise = 3;
		if(!ms.game.isInMenu && ms.depot.size()>maximalAutorise)
			throw new FatalGillesError("sender satur�  -  round : "+ms.game.round+" \ntaille: "+ms.depot.size()+"\nmaximal autorise : " + maximalAutorise);
	}
	public static void testDelayReceiver(MultiReceiver mr) throws FatalGillesError{
		int a = (int) (System.currentTimeMillis()-mr.tempsReception);
		if(!mr.g.isInMenu && a>(Main.nDelay*1000f/Main.framerate) && mr.nbReception>10){
			throw new FatalGillesError("d�lai d'attente d�pass� : \n   attente : "+a+"\n maximum autoris� : "+Main.nDelay*1000f/Main.framerate);
		}
	}
}
