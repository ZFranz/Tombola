package tombola;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

public class Server {

	protected Shell shlServer;
	// Array di PrintWriter
	static ArrayList<PrintWriter> clientList = new ArrayList<PrintWriter>();
	private static Display display;
	private Table tblTabellone;
	private int num = 1;
	private ArrayList<String> numeri = new ArrayList<String>();
	private boolean controllo = true;
	private static boolean inizia_partita = true;
	private static boolean controllo_ambo = true;
	private static boolean controllo_terna = true;
	private static boolean controllo_quaterna = true;
	private static boolean controllo_cinquina = true;
	private static boolean controllo_tombola = true;
	private static Table tblVincitori;
	private static String amboWinner = "";
	private static String ternaWinner = "";
	private static String quaternaWinner = "";
	private static String cinquinaWinner = "";
	private static String tombolaWinner = "";

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Server window = new Server();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		display = Display.getDefault();
		createContents();
		shlServer.open();
		shlServer.layout();
		while (!shlServer.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	private static class ServerThread extends Thread {
		Socket s;

		// Il costruttore deve ricevere il socket su cui lavorare
		public ServerThread(Socket s) {
			this.s = s;
		}

		@Override
		public void run() {
			super.run();
			// Resta in attesa dei messaggi del client
			try {
				BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				PrintWriter out = new PrintWriter(s.getOutputStream(), true);
				// Quando riceve un messaggio
				while (true) {
					String message = in.readLine();

					if (message.equals("cartella")) {
						if (inizia_partita) {
							out.println("partita in attesa");
						} else {
							out.println("partita iniziata");
						}
					}
					if (message.equals("vincita")) {
						message = in .readLine();
						switch (message) {
						case "Ambo":
							if (controllo_ambo) {
								message = in.readLine();
								out.println("vincita");
								out.println("vincita ambo");
								setAmboWinner(message);
								controllo_ambo = false;
							} else {
								out.println("vincita");
								out.println("vincita ambo riscattata");
								out.println(amboWinner);
							}
							break;

						case "Terna":
							if (controllo_terna) {
								message = in.readLine();
								out.println("vincita");
								out.println("vincita terna");
								setTernaWinner(message);
								controllo_terna = false;
							} else {
								out.println("vincita");
								out.println("vincita terna riscattata");
								out.println(ternaWinner);
							}
							break;

						case "Quaterna":
							if (controllo_quaterna) {
								message = in.readLine();
								out.println("vincita");
								out.println("vincita quaterna");
								setQuaternaWinner(message);
								controllo_quaterna = false;
							} else {
								out.println("vincita");
								out.println("vincita quaterna riscattata");
								out.println(quaternaWinner);
							}
							break;

						case "Cinquina":
							if (controllo_cinquina) {
								message = in.readLine();
								out.println("vincita");
								out.println("vincita cinquina");
								setCinquinaWinner(message);
								controllo_cinquina = false;
							} else {
								out.println("vincita");
								out.println("vincita cinquina riscattata");
								out.println(cinquinaWinner);
							}
							break;

						case "Tombola":
							if (controllo_tombola) {
								message = in.readLine();
								out.println("vincita");
								out.println("vincita tombola");
								setTombolaWinner(message);
								controllo_tombola = false;
							} else {
								out.println("vincita");
								out.println("vincita tombola riscattata");
								out.println(tombolaWinner);
							}
							break;
						}
					}

					// Manda il messaggio a tutti i client
					/*
					 * for (PrintWriter printWriter : clientList) {
					 * printWriter.println(message); }
					 */
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}
	
	public static void setAmboWinner(String uuid) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				TableItem item = tblVincitori.getItem(0);
				item.setText(1, uuid);
				amboWinner = uuid;
			}
		});
	}
	
	public static void setTernaWinner(String uuid) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				TableItem item = tblVincitori.getItem(1);
				item.setText(1, uuid);
				ternaWinner = uuid;
			}
		});
	}
	
	public static void setQuaternaWinner(String uuid) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				TableItem item = tblVincitori.getItem(2);
				item.setText(1, uuid);
				quaternaWinner = uuid;
			}
		});
	}
	
	public static void setCinquinaWinner(String uuid) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				TableItem item = tblVincitori.getItem(3);
				item.setText(1, uuid);
				cinquinaWinner = uuid;
			}
		});
	}
	
	public static void setTombolaWinner(String uuid) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				TableItem item = tblVincitori.getItem(4);
				item.setText(1, uuid);
				tombolaWinner = uuid;
			}
		});
	}

	public void DrawTable() {
		tblTabellone.removeAll();
		for (int i = 0; i < 9; i++) {
			if (num < 10) {
				TableItem tableItem = new TableItem(tblTabellone, SWT.NONE);
				tableItem.setText(0, "0" + Integer.toString(num++));
				tableItem.setText(1, "0" + Integer.toString(num++));
				tableItem.setText(2, "0" + Integer.toString(num++));
				tableItem.setText(3, "0" + Integer.toString(num++));
				tableItem.setText(4, "0" + Integer.toString(num++));
				tableItem.setText(5, "0" + Integer.toString(num++));
				tableItem.setText(6, "0" + Integer.toString(num++));
				tableItem.setText(7, "0" + Integer.toString(num++));
				tableItem.setText(8, "0" + Integer.toString(num++));
				tableItem.setText(9, Integer.toString(num++));
			} else {
				TableItem tableItem = new TableItem(tblTabellone, SWT.NONE);
				tableItem.setText(0, Integer.toString(num++));
				tableItem.setText(1, Integer.toString(num++));
				tableItem.setText(2, Integer.toString(num++));
				tableItem.setText(3, Integer.toString(num++));
				tableItem.setText(4, Integer.toString(num++));
				tableItem.setText(5, Integer.toString(num++));
				tableItem.setText(6, Integer.toString(num++));
				tableItem.setText(7, Integer.toString(num++));
				tableItem.setText(8, Integer.toString(num++));
				tableItem.setText(9, Integer.toString(num++));
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlServer = new Shell();
		shlServer.setSize(340, 490);
		shlServer.setText("Server");

		Color green = display.getSystemColor(SWT.COLOR_GREEN);
		Button btnAvviaServer = new Button(shlServer, SWT.NONE);
		Button btnAvviaPartita = new Button(shlServer, SWT.NONE);
		Label lblTabellone = new Label(shlServer, SWT.NONE);
		tblTabellone = new Table(shlServer, SWT.BORDER);
		tblTabellone.setLinesVisible(true);
		TableColumn tblclmnColumn0 = new TableColumn(tblTabellone, SWT.NONE);
		TableColumn tblclmnColumn1 = new TableColumn(tblTabellone, SWT.NONE);
		TableColumn tblclmnColumn2 = new TableColumn(tblTabellone, SWT.NONE);
		TableColumn tblclmnColumn3 = new TableColumn(tblTabellone, SWT.NONE);
		TableColumn tblclmnColumn4 = new TableColumn(tblTabellone, SWT.NONE);
		TableColumn tblclmnColumn5 = new TableColumn(tblTabellone, SWT.NONE);
		TableColumn tblclmnColumn6 = new TableColumn(tblTabellone, SWT.NONE);
		TableColumn tblclmnColumn7 = new TableColumn(tblTabellone, SWT.NONE);
		TableColumn tblclmnColumn8 = new TableColumn(tblTabellone, SWT.NONE);
		TableColumn tblclmnColumn9 = new TableColumn(tblTabellone, SWT.NONE);
		Button btnEstraiUnNumero = new Button(shlServer, SWT.NONE);
		Label lblListaVincitori = new Label(shlServer, SWT.NONE);
		tblVincitori = new Table(shlServer, SWT.BORDER);
		TableColumn tblclmnVincita = new TableColumn(tblVincitori, SWT.NONE);
		TableColumn tblclmnVincitore = new TableColumn(tblVincitori, SWT.NONE);

		btnAvviaServer.setBounds(10, 10, 85, 25);
		btnAvviaServer.setText("Avvia Server");
		btnAvviaServer.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnAvviaServer.setEnabled(false);
				btnAvviaPartita.setEnabled(true);
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						// Crea un server socket in ascolto
						try {
							ServerSocket ss = new ServerSocket(9999);
							System.out.println("Server up and running.");
							while (true) {
								Socket s = ss.accept();
								// Aggiunge ad un vettore di client il nuovo
								// client
								PrintWriter out = new PrintWriter(s.getOutputStream(), true);
								clientList.add(out);
								// Per ogni connessione crea un socket e un
								// thread che lo gestisca
								ServerThread st = new ServerThread(s);
								st.start();
								// Ritorna in ascolto/attesa
							}
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				});
				thread.start();
			}
		});

		btnAvviaPartita.setBounds(101, 10, 75, 25);
		btnAvviaPartita.setText("Avvia Partita");
		btnAvviaPartita.setEnabled(false);
		btnAvviaPartita.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				inizia_partita = false;
				btnEstraiUnNumero.setEnabled(true);
				btnAvviaPartita.setEnabled(false);
			}
		});

		lblTabellone.setBounds(10, 41, 55, 15);
		lblTabellone.setText("Tabellone");

		final TableEditor editor = new TableEditor(tblTabellone);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;

		tblTabellone.setBounds(10, 62, 305, 200);
		tblTabellone.setHeaderVisible(true);

		tblclmnColumn0.setResizable(false);
		tblclmnColumn0.setWidth(30);

		tblclmnColumn1.setResizable(false);
		tblclmnColumn1.setWidth(30);

		tblclmnColumn2.setResizable(false);
		tblclmnColumn2.setWidth(30);

		tblclmnColumn3.setResizable(false);
		tblclmnColumn3.setWidth(30);

		tblclmnColumn4.setResizable(false);
		tblclmnColumn4.setWidth(30);

		tblclmnColumn5.setResizable(false);
		tblclmnColumn5.setWidth(30);

		tblclmnColumn6.setResizable(false);
		tblclmnColumn6.setWidth(30);

		tblclmnColumn7.setResizable(false);
		tblclmnColumn7.setWidth(30);

		tblclmnColumn8.setResizable(false);
		tblclmnColumn8.setWidth(30);

		tblclmnColumn9.setResizable(false);
		tblclmnColumn9.setWidth(30);

		btnEstraiUnNumero.setBounds(10, 268, 305, 25);
		btnEstraiUnNumero.setText("Estrai un numero");
		btnEstraiUnNumero.setEnabled(false);
		btnEstraiUnNumero.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// estraggo un numero compreso tra 1 e 90 e lo inserisco in un
				// arraylist per evitare che venga estratto nuovamente
				int num = 0;
				controllo = true;
				while (controllo) {
					num = (int) (Math.random() * 90 + 1);
					if (numeri.size() == 0) {
						numeri.add(Integer.toString(num));
						controllo = false;
					} else {
						for (int i = 0; i < numeri.size(); i++) {
							if (numeri.get(i).equals(Integer.toString(num))) {
								break;
							} else if (i == (numeri.size() - 1)) {
								numeri.add(Integer.toString(num));
								controllo = false;
							}
						}
					}
				}

				System.out.println("Estratto il numero: " + num);

				// coloro la cella del numero estratto a caso
				for (int i = 0; i < 9; i++) {
					TableItem item = tblTabellone.getItem(i);
					for (int j = 0; j < 10; j++) {
						if (num < 10) {
							if (item.getText(j).equals("0" + Integer.toString(num))) {
								item.setBackground(j, green);
								break;
							}
						} else {
							if (item.getText(j).equals(Integer.toString(num))) {
								item.setBackground(j, green);
								break;
							}
						}
					}
				}

				// Manda il messaggio a tutti i client
				for (PrintWriter printWriter : clientList) {
					if (num < 10) {
						printWriter.println("numero");
						printWriter.println("0" + num);
					} else {
						printWriter.println("numero");
						printWriter.println(num);
					}
				}
			}
		});

		DrawTable();

		lblListaVincitori.setBounds(10, 299, 85, 15);
		lblListaVincitori.setText("Lista vincitori");

		tblVincitori.setBounds(10, 320, 305, 123);
		tblVincitori.setHeaderVisible(true);
		tblVincitori.setLinesVisible(true);

		tblclmnVincita.setWidth(60);
		tblclmnVincita.setText("Vincita");

		tblclmnVincitore.setWidth(240);
		tblclmnVincitore.setText("Vincitore");

		for (int i = 0; i < 5; i++) {
			TableItem item = new TableItem(tblVincitori, SWT.NONE);
			switch (i) {
			case 0:
				item.setText(0, "Ambo");
				break;

			case 1:
				item.setText(0, "Terna");
				break;

			case 2:
				item.setText(0, "Quaterna");
				break;

			case 3:
				item.setText(0, "Cinquina");
				break;

			case 4:
				item.setText(0, "Tombola");
				break;
			}
		}
	}
}
