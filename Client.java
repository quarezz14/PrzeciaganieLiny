import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class Client extends JFrame implements ActionListener {
	JButton exit, dodaj, odswiez, odejmij;
	JProgressBar postep;
	int p;

	public Client() {

		setTitle("Client Przeciagania Liny");
		setSize(800, 400);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		dodaj = new JButton("Dodaj punkt");
		dodaj.setBounds(100, 100, 300, 100);
		add(dodaj);
		dodaj.addActionListener(this);

		odejmij = new JButton("Odejmij punkt");
		odejmij.setBounds(400, 100, 300, 100);
		add(odejmij);
		odejmij.addActionListener(this);

		exit = new JButton("Exit");
		exit.setBounds(390, 200, 100, 50);
		add(exit);
		exit.addActionListener(this);

		odswiez = new JButton("Odswiez");
		odswiez.setBounds(290, 200, 100, 50);
		add(odswiez);
		odswiez.addActionListener(this);

		postep = new JProgressBar(0, 100);
		postep.setStringPainted(true);
		postep.setValue(0);
		postep.setBounds(50, 50, 700, 50);
		add(postep);

	}

	DataInputStream dis;
	DataOutputStream dos;
	Socket s;

	public void startClient(Client client) throws IOException {
		try {

			InetAddress ip = InetAddress.getByName("localhost");

			s = new Socket(ip, 5056);

			dis = new DataInputStream(s.getInputStream());
			dos = new DataOutputStream(s.getOutputStream());
			Thread t2 = new Pobieranie(dis, client);
			t2.start();

			// closing resources
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Dodaj punkt")) {
			try {
				dos.write(1);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (e.getActionCommand().equals("Odejmij punkt")) {
			try {
				dos.write(0);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (e.getActionCommand().equals("Exit")) {
			System.out.println("Closing this connection : " + s);
			try {
				dos.write(5);
				s.close();
				dis.close();
				dos.close();
				System.exit(0);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("Connection closed");
		}
		if (e.getActionCommand().equals("Odswiez")) {
			try {
				dos.write(2);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}

	}

}

class Pobieranie extends Thread {
	DataInputStream dis;
	Client client;

	public Pobieranie(DataInputStream dis, Client client) {
		this.client = client;
		this.dis = dis;
	}

	public void run() {
		while (true) {
			try {
				client.p = dis.read();
				client.postep.setValue(client.p);
			} catch (IOException e) {
				stop();
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}