package tests;

import java.util.Vector;

import main.Main;
import model.Game;
import multiplaying.InputHandler;
import multiplaying.InputObject;
import multiplaying.MultiReceiver;
import multiplaying.MultiSender;

public class Test {
	
	public static int dernierRoundRecu = 0;
	public static int nbMessageRecu = 0;
	public static long reveilSender = 0L;
	
	public static void testRoundCorrect(Game g, Vector<InputObject> ims) throws FatalGillesError{
		/**
		 * Teste si tous les rounds envoy�s dans ims correspondent au bon round dans game
		 * appel� dans l'update de Game avant l'update de plateau
		 */
		
		for(InputObject io : ims){
			if(io.round+Main.nDelay!=g.round){
				throw new FatalGillesError("roundCorrect");
			}
		}
	}
	public static void testNombreInput(Game g, Vector<InputObject> ims) throws FatalGillesError{
		/**
		 * Teste si le nombre d'inputs vaut bien soit 0 soit le nombre de joueurs
		 * appel� dans l'update de Game avant l'update de plateau
		 */
		if(!(ims.size()==g.players.size() || ims.size()==0)){
			throw new FatalGillesError("nombre d'input incorrect");
		}
	}
	public static void testRoundInputHandler(InputHandler ih, Game g) throws FatalGillesError{
		/**
		 * Teste si tous les inputs dans l'inputHandler ne sont pas outdat�s
		 * appel� � chaque nouvel input ajout� au handler
		 */
		for(InputObject io : ih.getInputs()){
			if(io.round+Main.nDelay+1<g.round)
				throw new FatalGillesError("messages non supprim�s");
		}
	}
	public static void testSizeSender(MultiSender ms) throws FatalGillesError{
		/**
		 * Teste si le d�pot d'envoi du Multi Sender n'est pas satur�
		 * appel� au d�but de l'update de Game 
		 */
		int maximalAutorise = 3;
		if(!ms.game.isInMenu && ms.depot.size()>maximalAutorise)
			throw new FatalGillesError("sender satur�  -  round : "+ms.game.round+" \ntaille: "+ms.depot.size()+"\nmaximal autorise : " + maximalAutorise);
	}
	public static void testDelayReceiver(MultiReceiver mr) throws FatalGillesError{
		/**
		 * Teste si l'�cart entre deux messages n'est pas sup�rieur � la tol�rance du delay
		 * appel� � chaque it�ration du thread du MultiSender
		 */
		int a = (int) (System.currentTimeMillis()-mr.tempsReception);
		if(!mr.g.isInMenu && a>(Main.nDelay*1000f/Main.framerate) && mr.nbReception>10){
			throw new FatalGillesError("d�lai d'attente d�pass� : \n   attente : "+a+"\n maximum autoris� : "+Main.nDelay*1000f/Main.framerate);
		}
	}
	public static void testOrderedMessages(int round) throws FatalGillesError{
		/**
		 * Teste si le message re�u est bien de round sup�rieur au dernier message re�u
		 * appel� � la r�ception de chaque message en jeu
		 */
		if(dernierRoundRecu!=0 && dernierRoundRecu>=round){
			throw new FatalGillesError("message non ordon�:\n    re�u : " + round+" \n    et dernier : " + dernierRoundRecu);
		}
		dernierRoundRecu = round;
	}
	public static void testNombreMessagesRecus(int round) throws FatalGillesError{
		/**
		 * Teste si tous les messages sont re�us � une tol�rance pr�s
		 * appel� � la r�ception de chaque message en jeu
		 */
		int seuil = 3, diff =Math.abs(round - nbMessageRecu); 
		if(diff>seuil){
			throw new FatalGillesError("messages non re�us : \n     au moins " + diff+" messages perdus");
		}
		nbMessageRecu++;
	}
	public static void testIfInputInMessage(String message) throws FatalGillesError{
		/**
		 * Teste si le message contient bien un input
		 * appel� � la r�ception de chaque message en jeu
		 */
		if(Game.g.round<5)
			return;
		String[] tab = message.split("\\%");
		boolean ok = false;
		for(int i =0; i<tab.length;i++){
			if(tab[i].length()>0 && tab[i].substring(0,1).equals("1")){
				ok = true;
				break;
			}
		}
		if(!ok){
			throw new FatalGillesError("pas d'input dans le message \n      message : "+message+"\n au round : "+ Game.g.round);
		}
	}
	public static void testReveilSender(long time) throws FatalGillesError{
		reveilSender = time;
		if(Game.g.isInMenu || Game.g.round<10){
			return;
		}
		if(Math.abs(reveilSender - time)>10000L){
			throw new FatalGillesError("Sender en panne de r�veil");
		}
	}
	public static void testSendEmptyMessages(String message) throws FatalGillesError{
		if(message.length()==0)
			throw new FatalGillesError("tentative d'envoi de message vide");
	}
	public static void testReceiveEmptyMessage(String message) throws FatalGillesError{
		boolean b = true;
		for(int i=0; i<message.length()-1; i++){
			b = b && message.charAt(i) == message.charAt(i+1);
			if(!b)
				break;
		}
		if(b)
			throw new FatalGillesError("reception d'un message vide");
	}
	
}
