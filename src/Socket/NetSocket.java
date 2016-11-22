package Socket;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;
import Constant.*;


public class NetSocket {

	
	Socket socket;
	ServerSocket ss;
	BufferedReader br;
	PrintWriter pw;
	
	public NetSocket(int type){
		String address;
		int port = 0;
		address = (String)JOptionPane.showInputDialog("Address:", "127.0.0.1");
		if(address != null){
			port = Integer.parseInt(JOptionPane.showInputDialog("Port:", "7777"));
		}
		if(port != 0){
			try {
				if(type == C.SERVER){
					ss = new ServerSocket( port);
					socket = ss.accept();
				} else {
					ss = null;
					socket = new Socket(address, port);
				}
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String receive() throws IOException{
		String line = br.readLine();
		return line;
	}
	
	public void send(String s){
		pw.println(s);
		pw.flush();
	}
	
}
