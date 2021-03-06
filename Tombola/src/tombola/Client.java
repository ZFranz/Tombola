package tombola;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;

public class Client {

	protected Shell shlClient;
	private Socket s;
	private PrintWriter out;
	private Display display;
	private Table table;
	private UUID uuid;
	private Button btnAmbo;
	private Button btnTerna;
	private Button btnQuaterna;
	private Button btnCinquina;
	private Button btnTombola;
	Cartella c = new Cartella();

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Client window = new Client();
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
		shlClient.open();
		shlClient.layout();
		while (!shlClient.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public void stampa(int num) {
		System.out.println("uuid: " + uuid + "\tNumero: " + num + " presente.");
	}

	public void mark(String num) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				Color green = display.getSystemColor(SWT.COLOR_GREEN);

				// coloro la cella del numero estratto a caso
				for (int i = 0; i < 3; i++) {
					TableItem item = table.getItem(i);
					for (int j = 0; j < 10; j++) {
						if (item.getText(j).equals(num)) {
							item.setBackground(j, green);
							break;
						}
					}
				}
			}
		});
	}

	public void drawTable() {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				int numeri[] = c.getNumeri();

				for (int r = 0; r < 3; r++) {
					String output = "";
					int d = 0;
					for (int c1 = 0; c1 < 5; c1++) {
						int index = r * 5 + c1;
						int num = numeri[index];

						// spazi per i numeri mancanti
						int _d = (int) ((double) num / 10.0);
						if (num == 90) { // il 90 va nella colonna degli 80
							_d = 8;
						}
						for (int i = 1; i < _d - d + (c1 == 0 ? 1 : 0); i++) {
							output += "  -";
						}
						d = _d;

						// stampa numero
						output += String.format("%02d%c", num, '-');
					}
					String[] parts = output.split("-");

					TableItem item = new TableItem(table, SWT.NONE);
					for (int i = 0; i < parts.length; i++) {
						item.setText(i, parts[i]);
					}
				}
			}
		});
	}

	public void enableAmbo() {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				btnAmbo.setEnabled(true);
			}
		});
	}

	public void enableTerna() {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				btnTerna.setEnabled(true);
			}
		});
	}

	public void enableQuaterna() {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				btnQuaterna.setEnabled(true);
			}
		});
	}

	public void enableCinquina() {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				btnCinquina.setEnabled(true);
			}
		});
	}

	public void enableTombola() {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				btnTombola.setEnabled(true);
			}
		});
	}

	public void alert_partita() {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageBox mb = new MessageBox(shlClient);
				mb.setText("Alert");
				mb.setMessage("La partita � gi� iniziata");
				mb.open();
			}
		});
	}

	public void prize_ambo() {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageBox mb = new MessageBox(shlClient);
				mb.setText("Hai vinto");
				mb.setMessage("Hai Vinto l\'ambo");
				mb.open();
			}
		});
	}

	public void prize_ambo_won(String message) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageBox mb = new MessageBox(shlClient);
				if (message.equals(uuid.toString())) {
					mb.setText("Hai vinto");
					mb.setMessage("Hai gi� riscattato l\'ambo.");
				} else {
					mb.setText("Non hai vinto");
					mb.setMessage("L\'ambo � gi� stato riscattato da: " + message);
				}
				mb.open();
			}
		});
	}

	public void prize_terna() {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageBox mb = new MessageBox(shlClient);
				mb.setText("Hai vinto");
				mb.setMessage("Hai Vinto la terna");
				mb.open();
			}
		});
	}

	public void prize_terna_won(String message) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageBox mb = new MessageBox(shlClient);
				if (message.equals(uuid.toString())) {
					mb.setText("Hai vinto");
					mb.setMessage("Hai gi� riscattato la terna.");
				} else {
					mb.setText("Non hai vinto");
					mb.setMessage("La terna � gi� stato riscattato da: " + message);
				}
				mb.open();
			}
		});
	}

	public void prize_quaterna() {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageBox mb = new MessageBox(shlClient);
				mb.setText("Hai vinto");
				mb.setMessage("Hai Vinto la quaterna");
				mb.open();
			}
		});
	}

	public void prize_quaterna_won(String message) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageBox mb = new MessageBox(shlClient);
				if (message.equals(uuid.toString())) {
					mb.setText("Hai vinto");
					mb.setMessage("Hai gi� riscattato la quaterna.");
				} else {
					mb.setText("Non hai vinto");
					mb.setMessage("La quaterna � gi� stato riscattato da: " + message);
				}
				mb.open();
			}
		});
	}

	public void prize_cinquina() {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageBox mb = new MessageBox(shlClient);
				mb.setText("Hai vinto");
				mb.setMessage("Hai Vinto la cinquina");
				mb.open();
			}
		});
	}

	public void prize_cinquina_won(String message) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageBox mb = new MessageBox(shlClient);
				if (message.equals(uuid.toString())) {
					mb.setText("Hai vinto");
					mb.setMessage("Hai gi� riscattato la cinquina.");
				} else {
					mb.setText("Non hai vinto");
					mb.setMessage("La cinquina � gi� stato riscattato da: " + message);
				}
				mb.open();
			}
		});
	}

	public void prize_tombola() {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageBox mb = new MessageBox(shlClient);
				mb.setText("Hai vinto");
				mb.setMessage("Hai Vinto la tombola");
				mb.open();
			}
		});
	}

	public void prize_tombola_won(String message) {
		display.asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageBox mb = new MessageBox(shlClient);
				if (message.equals(uuid.toString())) {
					mb.setText("Hai vinto");
					mb.setMessage("Hai gi� riscattato la tombola.");
				} else {
					mb.setText("Non hai vinto");
					mb.setMessage("La tombola � gi� stato riscattato da: " + message);
				}
				mb.open();
			}
		});
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlClient = new Shell();
		shlClient.setBackground(SWTResourceManager.getColor(0, 255, 204));
		shlClient.setSize(310, 280);
		shlClient.setText("Client");

		Button btnGeneraId = new Button(shlClient, SWT.NONE);
		Button btnConnessione = new Button(shlClient, SWT.NONE);
		Label lblId = new Label(shlClient, SWT.NONE);
		Label lblTombola = new Label(shlClient, SWT.NONE);
		table = new Table(shlClient, SWT.BORDER | SWT.FULL_SELECTION);
		TableColumn tblclmnColumn1 = new TableColumn(table, SWT.NONE);
		TableColumn tblclmnColumn2 = new TableColumn(table, SWT.NONE);
		TableColumn tblclmnColumn3 = new TableColumn(table, SWT.NONE);
		TableColumn tblclmnColumn4 = new TableColumn(table, SWT.NONE);
		TableColumn tblclmnColumn5 = new TableColumn(table, SWT.NONE);
		TableColumn tblclmnColumn6 = new TableColumn(table, SWT.NONE);
		TableColumn tblclmnColumn7 = new TableColumn(table, SWT.NONE);
		TableColumn tblclmnColumn8 = new TableColumn(table, SWT.NONE);
		TableColumn tblclmnColumn9 = new TableColumn(table, SWT.NONE);
		btnAmbo = new Button(shlClient, SWT.NONE);
		btnTerna = new Button(shlClient, SWT.NONE);
		btnQuaterna = new Button(shlClient, SWT.NONE);
		btnCinquina = new Button(shlClient, SWT.NONE);
		btnTombola = new Button(shlClient, SWT.NONE);

		btnGeneraId.setBounds(10, 10, 75, 25);
		btnGeneraId.setText("Genera ID");
		btnGeneraId.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				btnGeneraId.setEnabled(false);
				// Generating UUID
				uuid = UUID.randomUUID();

				lblId.setText("ID: " + uuid);
				btnConnessione.setEnabled(true);
			}
		});

		btnConnessione.setBounds(91, 10, 85, 25);
		btnConnessione.setText("Connessione");
		btnConnessione.setEnabled(false);
		btnConnessione.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				table.removeAll();
				btnConnessione.setEnabled(false);

				// Si connette al server e crea il socket
				try {
					s = new Socket("localhost", 9999);
					// Crea un thread di ascolto dei messaggi a cui passer�
					ClientReceiver cr = new ClientReceiver(s, Client.this, c, uuid);
					// il socket
					out = new PrintWriter(s.getOutputStream(), true);
					out.println("cartella");
					// la classe grafica
					cr.start();
				} catch (UnknownHostException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		lblId.setBounds(10, 43, 275, 15);
		lblId.setText("ID");
		lblId.setBackground(SWTResourceManager.getColor(0, 255, 204));

		lblTombola.setBounds(10, 64, 55, 15);
		lblTombola.setText("Tombola");
		lblTombola.setBackground(SWTResourceManager.getColor(0, 255, 204));

		table.setBounds(10, 85, 275, 85);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

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

		btnAmbo.setBounds(10, 176, 85, 25);
		btnAmbo.setText("Ambo");
		btnAmbo.setEnabled(false);
		btnAmbo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// invia il messaggio al server
				out.println("vincita");
				out.println("Ambo");
				out.println(uuid);
			}
		});

		btnTerna.setBounds(101, 176, 92, 25);
		btnTerna.setText("Terna");
		btnTerna.setEnabled(false);
		btnTerna.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// invia il messaggio al server
				out.println("vincita");
				out.println("Terna");
				out.println(uuid);
			}
		});

		btnQuaterna.setBounds(200, 176, 85, 25);
		btnQuaterna.setText("Quaterna");
		btnQuaterna.setEnabled(false);
		btnQuaterna.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// invia il messaggio al server
				out.println("vincita");
				out.println("Quaterna");
				out.println(uuid);
			}
		});

		btnCinquina.setBounds(10, 207, 134, 25);
		btnCinquina.setText("Cinquina");
		btnCinquina.setEnabled(false);
		btnCinquina.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// invia il messaggio al server
				out.println("vincita");
				out.println("Cinquina");
				out.println(uuid);
			}
		});

		btnTombola.setBounds(150, 207, 134, 25);
		btnTombola.setText("Tombola");
		btnTombola.setEnabled(false);
		btnTombola.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// invia il messaggio al server
				out.println("vincita");
				out.println("Tombola");
				out.println(uuid);
			}
		});
	}
}
