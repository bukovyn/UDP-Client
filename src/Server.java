import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*; 
import java.util.*;
import javax.swing.*;
import java.net.*;

public class Server extends JFrame implements ActionListener{

	DatagramSocket serverSocket ;
	InetAddress IPAddress;
	JTextField txtfield;
	JTextArea txtarea;
	byte[] serverbffr,clientbffr;
	DatagramSocket client, server;
	int seqNum;
	
	public Server (int port) throws  SocketException{
	  
		this.setSize(300,300);
		this.setTitle("Server");
		txtfield=new JTextField(100);
		txtfield.setBackground(Color.white);
		txtfield.setForeground(Color.blue);
		this.add(txtfield,BorderLayout.NORTH);
		txtarea=new JTextArea();
		this.add(txtarea,BorderLayout.CENTER);
		txtarea.setBackground(Color.black);
		txtarea.setForeground(Color.green);
		this.setVisible(true);
		serverbffr=new byte[1037];
		clientbffr=new byte[1037];
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try{
			client=new DatagramSocket();
			server=new DatagramSocket(port);
			seqNum = 0;
			while(true){
         		DatagramPacket datapack=new DatagramPacket(clientbffr,clientbffr.length);
				server.receive(datapack);
				String msg=new String(datapack.getData());
				if (msg.contains("Get message of the day")){
					txtarea.append("\nClient: "+msg);
					String x = txtfield.getText();
					while(!x.equals("")){
						String message=getChunk(x) + " | SeqNum = " + seqNum;
						serverbffr=message.getBytes();
						DatagramPacket sendpack=new DatagramPacket(serverbffr,serverbffr.length,InetAddress.getLocalHost(),6555);
						client.send(sendpack);
						txtarea.append("\nMyself: "+getChunk(x));
						x = getRest(x);
						}
					}
				else{
					seqNum = (int) msg.charAt(msg.length() - 1);
						if(seqNum == 0){
							seqNum = 1;
						}
						else{
							seqNum = 0; 
						}
					txtarea.append("\nClient: "+msg);
				}
			}
		}
		
		catch(Exception e){}
	}
	
	public DatagramPacket createPacket(String type) throws IOException ,UnknownHostException{

	    IPAddress = InetAddress.getLocalHost();
		String request =type;
		byte[] sendData = new byte[1037];
		sendData = request.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress,9999);
		return sendPacket;
	}	
	
	public DatagramPacket receive() throws IOException{ 
		
		byte[] receiveData = new byte[1037]; 
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length); 
		serverSocket.receive(receivePacket);
		return receivePacket; 
	}
	
    public String packetString(DatagramPacket packet) throws UnsupportedEncodingException{

		byte[] data = new byte[1037];
		data=packet.getData();
		String result = new String (data,0,packet.getLength());
		return result;
	}

	public void sendData(DatagramPacket packet,String data) throws IOException{	
		
		InetAddress IPAddress = packet.getAddress(); 
		int port = packet.getPort(); 
		byte[] sendData = new byte[1037]; 
		sendData=data.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port); 
		serverSocket.send(sendPacket);
	}
	
	public String getRest(String message){
		
		if(message.length()>16){
				return message.substring(16);
		}
		else
			return "";
	}

	public String getChunk(String message){
		
		if(message.length()>16){
			return message.substring(0,16);
		}
		else
			return message;
	}
}