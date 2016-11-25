import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.net.*;

public class Client extends JFrame implements ActionListener{
	
	DatagramSocket clientSocket, server;
	InetAddress IPAddress;
	JTextField txtfield;
	JTextArea txtarea;
	JButton button1, button2;
	byte[] serverbffr,clientbffr;
	int seqNum, packetNo;
	
	public Client (int port) throws IOException{
		
		this.setSize(300,300);
		this.setTitle("Client");
		this.setBackground(Color.black);
		txtarea=new JTextArea();
		txtarea.setBackground(Color.black);
		txtarea.setForeground(Color.green);
		this.add(txtarea,BorderLayout.CENTER);
		button2= new JButton("Get message of the day");
		this.add(button2, BorderLayout.SOUTH);
		button2.addActionListener(this);
		this.setVisible(true);
		serverbffr=new byte[1037];
		clientbffr=new byte[1037];
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try{		
			clientSocket=new DatagramSocket();	
			server =new DatagramSocket(port);
			packetNo = 0;
			while(true){
				byte[] receiveData = new byte[1037];
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				server.receive(receivePacket);
				String msg=new String(receivePacket.getData());
				packetNo++;
				seqNum = (int) msg.charAt(msg.length() - 1);
				String message= "Packet " + packetNo + " Received | SeqNum = " + seqNum ; 
				clientbffr=message.getBytes();
				DatagramPacket sendpack=new DatagramPacket(clientbffr,clientbffr.length,InetAddress.getLocalHost(), 6556);
				clientSocket.send(sendpack);
				txtarea.append("\nMyself: "+message);
				txtarea.append("\nServer: " + msg);
				}	
			}
		catch(SocketException e){
			System.out.println("SocketException error");
		}
	}
	
	public void actionPerformed(ActionEvent e){
		
		try{
			if(e.getActionCommand()=="Get message of the day"){
				String message="Get message of the day";
				clientbffr=message.getBytes();
				DatagramPacket sendpack=new DatagramPacket(clientbffr,clientbffr.length,InetAddress.getLocalHost(),6556);
				clientSocket.send(sendpack);
				txtarea.append("\nMyself: "+message);
				txtfield.setText("");
				}
			}
		catch(Exception a){}
	}

	public DatagramPacket createPacket(String type) throws IOException, UnknownHostException{
		
	    IPAddress = InetAddress.getByName("localhost");
		String request =type;
		byte[] sendData = new byte[1037];
		sendData = request.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress,9876);
		return sendPacket;
	}

	public void send(DatagramPacket packet) throws IOException{
		
		clientSocket.send(packet);
	}

	 public String packetString(DatagramPacket packet) throws UnsupportedEncodingException{

		byte[] data = new byte[1037];
		data=packet.getData();
		String result = new String (data,0,packet.getLength());
		return result;
	}

	public DatagramPacket receiveData() throws IOException, UnknownHostException{
		
		byte[] receiveData = new byte[1037];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		String modifiedSentence = new String(receivePacket.getData());
        return receivePacket;
	}	
	
	public void close(){

		clientSocket.close();
	}		
}