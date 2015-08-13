package multiplaying;

import java.io.IOException;
import java.net.*;
import java.util.Vector;

import model.Game;

public class MultiReceiver extends Thread{

	Game g;
	int port;
	public int received = 0;
	
	@SuppressWarnings("resource")
	public DatagramSocket server;
	byte[] message;
	DatagramPacket packet;
	
	// DEBUGGING
	private boolean debug = true;

	public MultiReceiver(Game g, int port){
		this.g = g;
		this.port = port;
	}

	@Override
	public void run(){
		try{
			this.server = new DatagramSocket(port);
			
			while(true){
				message = new byte[800];
				packet = new DatagramPacket(message, message.length);
				server.receive(packet);
				received++;
				String msg = new String(packet.getData());
				if(debug) System.out.println("message received: " + msg);
				if(msg.length()>0){
					int c = Integer.parseInt(msg.substring(0,1));
					switch(c){
					case 0: InputModel im = new InputModel(msg.substring(1, msg.length()));this.g.inputs.add(im);break;
					case 1: OutputModel om = new OutputModel(msg.substring(1, msg.length()));this.g.outputs.add(om);break;
					case 2: this.g.connexions.add(msg.substring(1, msg.length()));
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
