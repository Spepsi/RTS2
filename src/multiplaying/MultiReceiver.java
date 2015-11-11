package multiplaying;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import model.Game;

public class MultiReceiver extends Thread{

	Game g;
	int port;
	public int received = 0;

	public DatagramSocket server;
	byte[] message;
	DatagramPacket packet;


	// DEBUGGING
	private boolean debug = false;
	public boolean debugValidation = true;

	public MultiReceiver(Game g, int port){
		this.g = g;
		this.port = port;
	}

	@Override
	public void run(){
		try{
			this.server = new DatagramSocket(port);
			if(debug)
				System.out.println("Cr�ation d'un receiver - " + port);
			while(!server.isClosed()){
				message = new byte[2000];
				packet = new DatagramPacket(message, message.length);
				try{
					server.receive(packet);
				} catch(java.net.SocketException e){
					break;
				}
				String msg = new String(packet.getData());
				if(debug) System.out.println("port : " + port + " message received: " + msg);
				this.g.nbPaquetReceived++;
				if(msg.length()>0){
					int c = Integer.parseInt(msg.substring(0,1));
					switch(c){
					case 2: 
						if(!this.g.host){
							this.g.addressHost = packet.getAddress();
						}
						this.g.connexions.add(msg.substring(1, msg.length()));
						break;
					case 3:
						//Multi with sending inputs
						if(msg.length()>1){
							if(msg.substring(1, 2).equals("I")){
								InputObject io = new InputObject(msg.substring(2, msg.length()),g);
								
								if(debugValidation){
									System.out.println("MultiReceiver line 63 input received for round "+ this.g.round);
								}
								//A message coming from other players is automatically validated for yourself
								io.validate();
								//Send the validation for other players
								this.g.sendInputToPlayer(io.player, io.getMessageValidationToSend());
								this.g.inputsHandler.addToInputs(io);
							}
							//If validation message
							else if(msg.substring(1, 2).equals("V")){
								//Get the corresponding round and player
								String rawInput = msg.substring(1);
								
								if(debugValidation){
									System.out.println("MultiReceiver line 69 validation received for round "+ this.g.round);
									System.out.println("raw input V : "+rawInput);
								}
								String[] valMessage = rawInput.split("\\|");
								int round = Integer.parseInt(valMessage[1]);
								int idPlayer = Integer.parseInt(valMessage[2]);
								// Ressources partag� le vecteur d'inputs de la mailbox..
								this.g.inputsHandler.validate(round, g.getPlayerById(idPlayer));
							}
						}
						break;
					default:
					}
				}
			}

		} catch (SocketException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
