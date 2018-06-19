import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.text.*;
import java.util.*;

import javax.swing.JButton;
import javax.swing.JFrame;

import java.net.*;
public class Server extends JFrame implements ActionListener{
	JButton exit;
	int postep=50;
	
	public void startServer() throws IOException 
    {
        ServerSocket ss = new ServerSocket(5056);
		Server sServer = new Server();
		
		setTitle("Serwer Przeciagania Liny");
		setSize(200,200);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);
		
		exit = new JButton("Exit");
		exit.setBounds(50,50,100,50);
		add(exit);
		exit.addActionListener(this);
        while (true) 
        {
            Socket s = null;
             
            try
            {
                s = ss.accept();
                 
                System.out.println("Po³¹czono klienta ");
                 
                DataInputStream dis = new DataInputStream(s.getInputStream());
                DataOutputStream dos = new DataOutputStream(s.getOutputStream());
 
                Thread t = new ClientHandler(s, dis, dos,postep,sServer );
                t.start();
                 
            }
            catch (Exception e){
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

class ClientHandler extends Thread 
{
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;
    int postepC;
	Server server;
     
 
    // Constructor
    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos, int postepC, Server server) 
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
        this.postepC = postepC;
        this.server = server;
       }
 
    @Override
    public void run() 
    {
        int received;
        int toreturn;
        while (true) 
        {
            try {
 
                dos.write(server.postep);
                 
                received = dis.read();
                 
                if(received == 5)
                { 
                	System.out.println("Wylaczam polaczenie");
                   this.s.close();
                    break;
                }
                 
                 
                switch (received) {
                 
                    case 0:
                    	server.postep=server.postep-1;
                        break;
                         
                    case 1 :
                    	server.postep=server.postep+1;
                        break;
                    case 2 :
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
         
        try
        {
            this.dis.close();
            this.dos.close();
             
        }catch(IOException e){
            e.printStackTrace();
        }
    }
 
 }