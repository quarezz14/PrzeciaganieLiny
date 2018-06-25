import java.io.IOException;

public class ClientMain {

	public static void main(String[] args) throws IOException {
		Client klient = new Client();
		klient.startClient(klient);

	}

}
