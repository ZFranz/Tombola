package tombola;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.UUID;

public class ClientReceiver extends Thread {

	private Socket s;
	private Client c;
	private Cartella cart;
	private Vincita ultimaVincita;
	private UUID uuid;

	// Deve essere inizializzato con il socket e con il riferimento della parte
	// grafica
	public ClientReceiver(Socket s, Client c, Cartella cart, UUID uuid) {
		this.s = s;
		this.c = c;
		this.cart = cart;
		this.uuid = uuid;
		ultimaVincita = null;
	}

	@Override
	public void run() {
		super.run();
		// All' infinito resta in ascolto di nuovi messaggi nel socket
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
			while (true) {
				// Quando arriva un nuovo messaggio
				String message = in.readLine();
				if (message.equals("partita in attesa")) {
					c.drawTable();
				} else if (message.equals("partita iniziata")) {
					c.alert_partita();
				} else if (message.equals("numero")) {
					String num = in.readLine();
					// Legge il numero
					// Comunica alla grafica il nuovo numero
					c.mark(num);
					if (cart.numeroEstratto(Integer.parseInt(num))) {
						c.stampa(Integer.parseInt(num));
						final Vincita v = cart.vincitaMax();
						if (v == Vincita.next(ultimaVincita)) {
							Utility.info("La cartella di " + uuid + " vince: " + v.name());
							ultimaVincita = Vincita.next(ultimaVincita);
							switch (ultimaVincita) {
							case Ambo:
								c.enableAmbo();
								break;

							case Terna:
								c.enableTerna();
								break;

							case Quaterna:
								c.enableQuaterna();
								break;

							case Cinquina:
								c.enableCinquina();
								break;

							case Tombola:
								c.enableTombola();
								break;
							}
						}
					}
				} else if (message.equals("vincita")) {
					message = in.readLine();
					switch (message) {
					case "vincita ambo":
						c.prize_ambo();
						break;

					case "vincita ambo riscattata":
						message = in.readLine();
						c.prize_ambo_won(message);
						break;
						
					case "vincita terna":
						c.prize_terna();
						break;
						
					case "vincita terna riscattata":
						message = in.readLine();
						c.prize_terna_won(message);
						break;
						
					case "vincita quaterna":
						c.prize_quaterna();
						break;
						
					case "vincita quaterna riscattata":
						message = in.readLine();
						c.prize_quaterna_won(message);
						break;
						
					case "vincita cinquina":
						c.prize_cinquina();
						break;
						
					case "vincita cinquina riscattata":
						message = in.readLine();
						c.prize_cinquina_won(message);
						break;
						
					case "vincita tombola":
						c.prize_tombola();
						break;
						
					case "vincita tombola riscattata":
						message = in.readLine();
						c.prize_tombola_won(message);
						break;
					}
				}

			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
