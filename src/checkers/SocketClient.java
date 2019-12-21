package checkers;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.BufferedOutputStream;

public class SocketClient {
	private String address = "127.0.0.1";
	//private String address = "192.168.1.197";
	//private String address = "140.138.49.83";
	private int port = 8765;

	void init(String ip, int port) {
		this.port = port;
		address = ip;
		System.out.println("ip:" + address);
	}

	public SocketClient() {

		Socket client = new Socket();
		InetSocketAddress isa = new InetSocketAddress(this.address, this.port);
		try {
			client.connect(isa, 10000);
			BufferedOutputStream out = new BufferedOutputStream(client.getOutputStream());
			out.write("Wongyihe Game Starts".getBytes());
			out.flush();
			out.close();
			out = null;
			//System.out.println("ip:" + address);
			client.close();
			client = null;

		} catch (java.io.IOException e) {
			System.out.println("Socket連線有問題 !");
			System.out.println("IOException :" + e.toString());
		}
	}

	public void sent(String s) {
		
		Socket client = new Socket();
		InetSocketAddress isa = new InetSocketAddress(this.address, this.port);
		try {
			client.connect(isa, 10000);
			BufferedOutputStream out = new BufferedOutputStream(client.getOutputStream());
			out.write(s.getBytes());
			out.flush();
			out.close();
			out = null;
			System.out.println("ip:" + address);
			System.out.println("Send From Client: " + s);

			client.close();
			client = null;

		} catch (java.io.IOException e) {
			System.out.println("Socket連線有問題 !");
			System.out.println("IOException :" + e.toString());
		}
		
	}
}