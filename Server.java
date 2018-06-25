import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;

import java.net.*;

public class Server extends JFrame implements ActionListener {
	JButton exit;
	int postep = 50;

	public void startServer() throws IOException {
		ServerSocket ss = new ServerSocket(5056);
		Server sServer = new Server();

		setTitle("Serwer Przeciagania Liny");
		setSize(200, 200);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		exit = new JButton("Exit");
		exit.setBounds(50, 50, 100, 50);
		add(exit);
		exit.addActionListener(this);
		while (true) {
			Socket s = null;

			try {
				s = ss.accept();

				System.out.println("Po³¹czono klienta ");

				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dos = new DataOutputStream(s.getOutputStream());

				Thread t = new ClientHandler(s, dis, dos, sServer);
				t.start();
				Thread t2 = new Aktualizator(dos, sServer);
				t2.start();

			} catch (Exception e) {
				s.close();
				e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("Exit")) {
			System.exit(0);
		}
	}
}

class ClientHandler extends Thread {
	final DataInputStream dis;
	final DataOutputStream dos;
	final Socket s;
	Server server;

	// Constructor
	public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, Server server) {
		this.s = s;
		this.dis = dis;
		this.dos = dos;
		this.server = server;
	}

	@Override
	public void run() {
		int received;
		int toreturn;
		while (true) {
			try {

				received = dis.read();

				if (received == 5) {
					System.out.println("Wylaczam polaczenie");
					this.s.close();
					break;
				}

				switch (received) {

				case 0:
					server.postep = server.postep - 1;
					break;

				case 1:
					server.postep = server.postep + 1;
					break;
				case 2:
					dos.write(server.postep);
					break;

				default:
					dos.writeUTF("Invalid input");
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			this.dis.close();
			this.dos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

class Aktualizator extends Thread {
	DataOutputStream dos;
	Server server;

	public Aktualizator(DataOutputStream dos, Server server) {

		this.dos = dos;
		this.server = server;
	}

	public void run() {
		while (true) {
			try {
				dos.write(server.postep);
			} catch (IOException e) {
				stop();
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}