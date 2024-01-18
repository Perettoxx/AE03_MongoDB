package app;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.bson.Document;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JTable;

public class Vista extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	// BOTONES INICIALES DE LA IZQUIERDA
	private JButton btnRegistrar;
	private JButton btnIniSesion;
	private JButton btnJugar;
	private JButton btnRecords;

	// PANEL REGISTRO/INICIO SESIÓN
	private JPanel panelSesion;
	private JTextField txtUsuario;
	private JTextField txtContra;

	private JLabel lblRegistrar;
	private JLabel lblIniSesion;
	private JButton btnRegistrarUsu;
	private JButton btnIniSesionUsu;

	// PANEL JUEGO
	private JPanel panelJuego;
	private JButton btnJuego0;
	private JButton btnJuego1;
	private JButton btnJuego2;
	private JButton btnJuego3;
	private JButton btnJuego4;
	private JButton btnJuego5;
	private JButton btnJuego6;
	private JButton btnJuego7;
	private JButton btnJuego8;
	private JButton btnJuego9;
	private JButton btnJuego10;
	private JButton btnJuego11;
	private JButton btnJuego12;
	private JButton btnJuego13;
	private JButton btnJuego14;
	private JButton btnJuego15;
	private JButton btnJugarPartida;
	private JRadioButton radio2x4;
	private JRadioButton radio4x4;

	// LOGICA JUEGO
	private Juego juego;
	private Carta carta1;
	private Carta carta2;
	private int contador = 0;

	// TEMPORIZADOR
	private Timer temporizador;
	private int segundosTranscurridos;
	private JPanel panelTemporizador;
	private JLabel lblTemporizador;

	// PARAMETROS PARA CONTROLADOR
	private int dificultad;
	private int tiempo;
	private ActionListener juegoFinalizado;

	// PANEL RECORDS
	private JPanel panelRecords;
	private JTable table8;
	private JTable table16;

	/**
	 * Funció que crea el model predeterminat de les taules on s'indica el tipus de
	 * informació que hi ha a les columnes
	 * 
	 * @return DefaultTableModel Retorna el model per a les taules
	 */
	private DefaultTableModel createTableModel() {
		DefaultTableModel model = new DefaultTableModel();
		model.addColumn("Usuario");
		model.addColumn("Dificultad");
		model.addColumn("Duracion");
		model.addColumn("Timestamp");
		return model;
	}

	public Vista() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnRegistrar = new JButton("Registrar");
		btnRegistrar.setBounds(50, 60, 145, 29);
		contentPane.add(btnRegistrar);

		btnIniSesion = new JButton("Iniciar sesión");
		btnIniSesion.setBounds(50, 115, 145, 29);
		contentPane.add(btnIniSesion);

		btnJugar = new JButton("Jugar");
		btnJugar.setBounds(50, 177, 145, 29);
		contentPane.add(btnJugar);

		btnRecords = new JButton("Records");
		btnRecords.setBounds(50, 235, 145, 29);
		contentPane.add(btnRecords);

		panelRecords = new JPanel();
		panelRecords.setBounds(270, 6, 600, 550);
		contentPane.add(panelRecords);
		panelRecords.setLayout(null);

		// Crear modelos separados para cada tabla
		DefaultTableModel model8 = createTableModel();
		DefaultTableModel model16 = createTableModel();

		// Para la tabla8
		table8 = new JTable(model8);
		table8.setBounds(5, 50, 590, 220);
		panelRecords.add(table8);

		// Para la tabla16
		table16 = new JTable(model16);
		table16.setBounds(5, 320, 590, 220);
		panelRecords.add(table16);

		JLabel lblUsuario8 = new JLabel("Usuario");
		lblUsuario8.setBounds(20, 30, 61, 16);
		panelRecords.add(lblUsuario8);

		JLabel lblDificultad8 = new JLabel("Dificultad");
		lblDificultad8.setBounds(160, 30, 72, 16);
		panelRecords.add(lblDificultad8);

		JLabel lblTiempo8 = new JLabel("Tiempo");
		lblTiempo8.setBounds(320, 30, 61, 16);
		panelRecords.add(lblTiempo8);

		JLabel lblFecha8 = new JLabel("Fecha");
		lblFecha8.setBounds(502, 30, 61, 16);
		panelRecords.add(lblFecha8);

		JLabel lblUsuario16 = new JLabel("Usuario");
		lblUsuario16.setBounds(20, 300, 61, 16);
		panelRecords.add(lblUsuario16);

		JLabel lblDificultad16 = new JLabel("Dificultad");
		lblDificultad16.setBounds(160, 300, 72, 16);
		panelRecords.add(lblDificultad16);

		JLabel lblTiempo16 = new JLabel("Tiempo");
		lblTiempo16.setBounds(320, 300, 61, 16);
		panelRecords.add(lblTiempo16);

		JLabel lblFecha16 = new JLabel("Fecha");
		lblFecha16.setBounds(502, 300, 61, 16);
		panelRecords.add(lblFecha16);

		panelJuego = new JPanel();
		panelJuego.setBounds(37, 60, 833, 460);
		contentPane.add(panelJuego);
		panelJuego.setLayout(null);

		btnJuego0 = new JButton("");
		btnJuego0.setBounds(286, 18, 100, 100);
		panelJuego.add(btnJuego0);

		btnJuego1 = new JButton("");
		btnJuego1.setBounds(422, 18, 100, 100);
		panelJuego.add(btnJuego1);

		btnJuego2 = new JButton("");
		btnJuego2.setBounds(566, 18, 100, 100);
		panelJuego.add(btnJuego2);

		btnJuego3 = new JButton("");
		btnJuego3.setBounds(709, 18, 100, 100);
		panelJuego.add(btnJuego3);

		btnJuego4 = new JButton("");
		btnJuego4.setBounds(286, 130, 100, 100);
		panelJuego.add(btnJuego4);

		btnJuego5 = new JButton("");
		btnJuego5.setBounds(422, 130, 100, 100);
		panelJuego.add(btnJuego5);

		btnJuego6 = new JButton("");
		btnJuego6.setBounds(566, 130, 100, 100);
		panelJuego.add(btnJuego6);

		btnJuego7 = new JButton("");
		btnJuego7.setBounds(709, 130, 100, 100);
		panelJuego.add(btnJuego7);

		btnJuego8 = new JButton("");
		btnJuego8.setBounds(286, 242, 100, 100);
		panelJuego.add(btnJuego8);

		btnJuego9 = new JButton("");
		btnJuego9.setBounds(422, 242, 100, 100);
		panelJuego.add(btnJuego9);

		btnJuego10 = new JButton("");
		btnJuego10.setBounds(566, 242, 100, 100);
		panelJuego.add(btnJuego10);

		btnJuego11 = new JButton("");
		btnJuego11.setBounds(709, 242, 100, 100);
		panelJuego.add(btnJuego11);

		btnJuego12 = new JButton("");
		btnJuego12.setBounds(286, 354, 100, 100);
		panelJuego.add(btnJuego12);

		btnJuego13 = new JButton("");
		btnJuego13.setBounds(422, 354, 100, 100);
		panelJuego.add(btnJuego13);

		btnJuego14 = new JButton("");
		btnJuego14.setBounds(566, 354, 100, 100);
		panelJuego.add(btnJuego14);

		btnJuego15 = new JButton("");
		btnJuego15.setBounds(709, 354, 100, 100);
		panelJuego.add(btnJuego15);

		btnJugarPartida = new JButton("Jugar partida");
		btnJugarPartida.setBounds(28, 328, 133, 29);
		panelJuego.add(btnJugarPartida);

		ButtonGroup grupoRadioButtons = new ButtonGroup();

		radio2x4 = new JRadioButton("2x4");
		radio2x4.setBounds(28, 369, 64, 23);
		panelJuego.add(radio2x4);
		grupoRadioButtons.add(radio2x4);

		radio4x4 = new JRadioButton("4x4");
		radio4x4.setBounds(90, 369, 64, 23);
		panelJuego.add(radio4x4);
		grupoRadioButtons.add(radio4x4);

		panelTemporizador = new JPanel();
		panelTemporizador.setBounds(35, 240, 200, 50);
		panelJuego.add(panelTemporizador);
		panelTemporizador.setLayout(null);

		lblTemporizador = new JLabel("");
		lblTemporizador.setBounds(10, 20, 180, 16);
		panelTemporizador.add(lblTemporizador);

		panelSesion = new JPanel();
		panelSesion.setBounds(290, 60, 580, 222);
		contentPane.add(panelSesion);
		panelSesion.setLayout(null);

		lblRegistrar = new JLabel("Registrarse");
		lblRegistrar.setBounds(258, 6, 70, 16);
		panelSesion.add(lblRegistrar);

		lblIniSesion = new JLabel("Iniciar Sesión");
		lblIniSesion.setBounds(258, 6, 84, 16);
		panelSesion.add(lblIniSesion);

		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setBounds(183, 63, 61, 16);
		panelSesion.add(lblUsuario);

		txtUsuario = new JTextField();
		txtUsuario.setBounds(275, 58, 137, 26);
		panelSesion.add(txtUsuario);
		txtUsuario.setColumns(10);

		JLabel lblContra = new JLabel("Contraseña:");
		lblContra.setBounds(183, 107, 80, 16);
		panelSesion.add(lblContra);

		txtContra = new JTextField();
		txtContra.setColumns(10);
		txtContra.setBounds(275, 102, 137, 26);
		panelSesion.add(txtContra);

		btnRegistrarUsu = new JButton("Registrarse");
		btnRegistrarUsu.setBounds(240, 159, 117, 29);
		panelSesion.add(btnRegistrarUsu);
		setVisible(true);

		btnIniSesionUsu = new JButton("Iniciar Sesión");
		btnIniSesionUsu.setBounds(240, 159, 117, 29);
		panelSesion.add(btnIniSesionUsu);

	}

	public JButton getBtnRegistrar() {
		return btnRegistrar;
	}

	public void setBtnRegistrar(JButton btnRegistrar) {
		this.btnRegistrar = btnRegistrar;
	}

	public JButton getBtnIniSesion() {
		return btnIniSesion;
	}

	public void setBtnIniSesion(JButton btnIniSesion) {
		this.btnIniSesion = btnIniSesion;
	}

	public JButton getBtnJugar() {
		return btnJugar;
	}

	public void setBtnJugar(JButton btnJugar) {
		this.btnJugar = btnJugar;
	}

	public JButton getBtnRecords() {
		return btnRecords;
	}

	public void setBtnRecords(JButton btnRecords) {
		this.btnRecords = btnRecords;
	}

	/**
	 * Funció que habilita el botons de jugar i records
	 */
	public void habilitarJugarRecords() {
		btnJugar.setEnabled(true);
		btnRecords.setEnabled(true);
	}

	/**
	 * Funció que deshabilita el botons de jugar i records
	 */
	public void deshabilitarJugarRecords() {
		btnJugar.setEnabled(false);
		btnRecords.setEnabled(false);
	}

	// PANEL JUEGO

	public JPanel getPanelJuego() {
		return panelJuego;
	}

	public void setPanelJuego(JPanel panelJuego) {
		this.panelJuego = panelJuego;
	}

	/**
	 * Funció que mostra el panel de joc
	 */
	public void mostrarPanelJuego() {
		panelJuego.setVisible(true);
	}

	/**
	 * Funció que oculta el panel de joc
	 */
	public void ocultarPanelJuego() {
		panelJuego.setVisible(false);
	}

	// PANEL TEMPORIZADOR

	public JPanel getPanelTemporizador() {
		return panelTemporizador;
	}

	public void setPanelTemporizador(JPanel panelTemporizador) {
		this.panelTemporizador = panelTemporizador;
	}

	/**
	 * Funció que mostra el panel del temporitzador
	 */
	public void mostrarPanelTemporizador() {
		panelTemporizador.setVisible(true);
	}

	/**
	 * Funció que oculta el panel del temporitzador
	 */
	public void ocultarPanelTemporizador() {
		panelTemporizador.setVisible(false);
	}

	// SELECCION DE MODO DE JUEGO

	public JButton getBtnJugarPartida() {
		return btnJugarPartida;
	}

	public void setBtnJugarPartida(JButton btnJugarPartida) {
		this.btnJugarPartida = btnJugarPartida;
	}

	public JRadioButton getRadio2x4() {
		return radio2x4;
	}

	public void setRadio2x4(JRadioButton radio2x4) {
		this.radio2x4 = radio2x4;
	}

	public JRadioButton getRadio4x4() {
		return radio4x4;
	}

	public void setRadio4x4(JRadioButton radio4x4) {
		this.radio4x4 = radio4x4;
	}

	// LOGICA DEL JUEGO

	/**
	 * Funció que: preselecciona el mode de joc de 2x4 (encamina el flux de joc),
	 * selecciona tots el botons per després ficar un nom a cada un i deshabilitar
	 * el mateix
	 */
	public void preparacionPrevia() {
		radio2x4.setSelected(true);
		List<JButton> botones = seleccionBotones(true);
		for (JButton boton : botones) {
			boton.setEnabled(false);
			boton.setName("btnJuego" + botones.indexOf(boton));
		}
	}

	/**
	 * Funció que prepara el joc: crea el tipus de data Carta, on s'afegeix el botó
	 * i la seua imatgep seleccionada de manera aleatoria
	 * 
	 * @param eleccion Si es false dona la selecció de botons per jugar una partida
	 *                 2x4. Si es true dona la selección de botons per jugar una
	 *                 partida 4x4.
	 * @return Retorna una lista del tipus Carta
	 */
	public List<Carta> preparacionJuego(boolean eleccion) {
		List<Carta> cartas = new ArrayList<>();
		List<JButton> botones = seleccionBotones(eleccion);
		List<String> imagenes = new ArrayList<>();
		if (botones.size() == 8 || botones.size() == 16) {
			// Obtener tantas imágenes como sean necesarias
			imagenes = Model.obtenerImagenesAleatorias(botones.size() / 2);

			// Duplicar las imágenes para formar parejas
			imagenes.addAll(new ArrayList<>(imagenes));

			// Barajar las imágenes para asignarlas aleatoriamente a los botones
			Collections.shuffle(imagenes);
		} else {
			System.err.println("La lista de botones debe tener una longitud de 8 o 16");
		}

		for (JButton boton : botones) {
			Carta carta = new Carta(boton, imagenes.get(botones.indexOf(boton)));
			cartas.add(carta);
			carta.getBoton().setEnabled(true);
		}

		return cartas;
	}

	/**
	 * Funció que donat una carta cambia el icon per la que te associada
	 * 
	 * @param carta Carta on es vol cambiar l'imatge
	 */
	private void asignarImagenABoton(Carta carta) {
		if (carta.getRutaImagen() != null) {
			try {
				// Cargar la imagen desde el directorio /img
				String rutaImagen = "img/" + carta.getRutaImagen();
				File archivoImagen = new File(rutaImagen);

				if (archivoImagen.exists()) {
					Image imagen = ImageIO.read(archivoImagen);
					ImageIcon icono = new ImageIcon(imagen);

					carta.getBoton().setIcon(icono);
					System.out.println("Imagen asignada a " + carta.getBoton().getName() + ": " + rutaImagen);
				} else {
					System.err.println("El archivo de imagen no existe: " + rutaImagen);
				}
			} catch (IOException e) {
				System.err.println("Error al cargar la imagen: " + e.getMessage());
			}
		} else {
			System.err.println("La ruta de la imagen es nula para " + carta.getBoton().getName());
		}
	}

	/**
	 * Funció que conté el joc. S'inicialitza la classe juego i el temporitzador per
	 * comptar el temps de joc. Donem valor a la dificultat i al contador, així com
	 * al array de boolean que controla el flux de joc. S'itera sobre la lista del
	 * tipus Carta que donem per parametre i escoltem possibles clicks. En cas de no
	 * estar deshabilitada es comprobara si alguna de les de 2 cartes auxiliars on
	 * guardem la informació de les cartes polsades es null. Si la primera es null
	 * (ninguna está polsada), s'aplicara la lógica del joc a la carta1. Si la
	 * primera no es null i la segona ho es, s'aplicará a la carta2. També fa una
	 * comprobació per no posibilitar que es polse 2 vegades sobre la mateixa carta.
	 * Després es comproba si son iguals o no.
	 * 
	 * @param cartas Llistat de cartes (botons del joc)
	 */
	public void juego(List<Carta> cartas) {
		juego = new Juego(cartas);

		// Inicializar el temporizador
		temporizador = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				segundosTranscurridos++;
				actualizarLabelTemporizador();
			}
		});

		dificultad = cartas.size();

		juego.setPulsadas(new boolean[] { false, false });
		contador = 0;

		// Restablecer el temporizador y el JLabel cada vez que inicia un nuevo juego
		lblTemporizador.setText("Tiempo: 0 segundos");
		reiniciarTemporizador();
		temporizador.start();

		for (Carta carta : cartas) {
			carta.getBoton().addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (!carta.getBoton().isEnabled()) {
						// La tarjeta está deshabilitada, no hacer nada
						return;
					}

					if (carta1 == null) {
						juego.setPulsadas(new boolean[] { true, false });
						carta1 = carta;
						asignarImagenABoton(carta1);
						System.out.println("Se ha pulsado la primera tarjeta");
					} else if (carta1 != null && carta2 == null) {
						if (!carta1.getBoton().equals(carta.getBoton())) {
							juego.setPulsadas(new boolean[] { true, true });
							carta2 = carta;
							asignarImagenABoton(carta2);
							System.out.println("Se ha pulsado la segunda tarjeta");

							Timer timer = new Timer(250, new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent evt) {
									comprobarTarjetas();
								}
							});
							timer.setRepeats(false);
							timer.start();
						} else {
							System.err.println("Se ha pulsado en la misma carta");
						}
					}
				}
			});
		}
	}

	/**
	 * Funció que conté la lógica de comprobació de les tarjetes. Agafa les cartes
	 * auxiliars i compara els valors de la ruta de imagen per comprobar si tenen la
	 * mateixa imagen. Si les rutes coincideixen els botons es deshabiliten i el
	 * contador que controla els encerts suma 1. Si les rutes no coincideixen les
	 * icones dels botons tornen al seu valor null. En qualsevol cas, les cartes
	 * auxiliars tornen a un valor null. També es comproba si es la última parella
	 * de targetes igualant el contador a la dificultat / 2 (si la dificultat es de
	 * 8 (2x4), el contador tindrá que arribar a 4 parelles) Si es el cas: para el
	 * temporitzador, iguala la variable local tiempo al temps de joc i executa la
	 * funció de final de partida.
	 */
	private void comprobarTarjetas() {
		if (carta1 != null && carta2 != null) {
			if (carta1.getBoton() != null && carta2.getBoton() != null) {
				if (carta1.getRutaImagen().equals(carta2.getRutaImagen())) {
					carta1.getBoton().setEnabled(false);
					carta2.getBoton().setEnabled(false);
					contador++;
					System.out.println("Son iguales. Has acertado.");
				} else {
					if (carta1.getBoton() != null) {
						carta1.getBoton().setIcon(null);
					}
					if (carta2.getBoton() != null) {
						carta2.getBoton().setIcon(null);
					}
					System.out.println("Son distintas. Prueba otra vez");
				}
				juego.setPulsadas(new boolean[] { false, false });
			} else {
				System.err.println("El botón de carta1 o carta2 es nulo.");
			}
			carta1 = null;
			carta2 = null;
			if (contador == dificultad / 2) {
				temporizador.stop();
				tiempo = segundosTranscurridos;
				mostrarFinalPartida();
			}
		}
	}

	/**
	 * Funció que conté les funcionalitats de final de joc. Envia informació de que
	 * el joc ha finalitzat amb un actionListener al controlador. Mostra un missatge
	 * de final de partida. Reinicia els valors del temporitzador, de les icones
	 * dels botons i fica null al joc i a les targetes auxiliars. També oculta el
	 * panel del temporitzador.
	 */
	public void mostrarFinalPartida() {
		if (juegoFinalizado != null) {
			juegoFinalizado.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
		}

		// Mostramos ventana emergente de finalización de la partida
		String mensaje = "¡La partida ha terminado!\n" + "Tiempo transcurrido: " + segundosTranscurridos + " segundos";
		JOptionPane.showMessageDialog(null, mensaje, "Fin de la partida", JOptionPane.INFORMATION_MESSAGE);

		// Reseteamos los valores
		reiniciarTemporizador();

		for (Carta carta : juego.getTarjetas()) {
			carta.getBoton().setIcon(null);
		}

		// Reiniciar variables relacionadas con el juego
		juego = null;
		carta1 = null;
		carta2 = null;

		// Restaurar la preparación previa
		preparacionPrevia();

		ocultarPanelTemporizador();
	}

	/**
	 * Funció que para el temporitzador, reinicia els segons del temps de joc i
	 * actualitza el JLabel del temporitzador
	 */
	private void reiniciarTemporizador() {
		temporizador.stop();
		segundosTranscurridos = 0;
		actualizarLabelTemporizador();
	}

	/**
	 * Funció que fica el text del temps de joc al JLabel del temporitzador
	 */
	private void actualizarLabelTemporizador() {
		lblTemporizador.setText("Tiempo: " + segundosTranscurridos + " segundos");
	}

	public void setJuegoFinalizado(ActionListener listener) {
		this.juegoFinalizado = listener;
	}

	public int getDificultad() {
		return dificultad;
	}

	public void setDificultad(int dificultad) {
		this.dificultad = dificultad;
	}

	public int getTiempo() {
		return tiempo;
	}

	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}

	// PANEL RECORDS

	SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

	/**
	 * Funció que importa les dades dels records desde una lista de tipus Document.
	 * Fa un parse del tipus de dada String a un tipus de dada SimpleDateFormat per
	 * donar format al timestamp de la partida.
	 * 
	 * @param table     Taula on es importaran les dades
	 * @param registros Lista de tipus Document que conté les dades a importar
	 */
	public void importarDatos(JTable table, List<Document> registros) {
		DefaultTableModel model = (DefaultTableModel) table.getModel();
		model.setRowCount(0); // Limpiar la tabla antes de agregar nuevos datos

		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

		for (Document registro : registros) {
			try {
				String timestampStr = registro.getString("timestamp");
				Date timestampDate = inputFormat.parse(timestampStr);

				Object[] rowData = { registro.getString("usuario"), registro.getInteger("dificultad"),
						registro.getInteger("duracion"), outputFormat.format(timestampDate) };
				model.addRow(rowData);
			} catch (Exception e) {
				// Manejar cualquier error al convertir el timestamp
				e.printStackTrace();
			}
		}
		deshabilitarEdicion(table8);
		deshabilitarEdicion(table16);
	}

	/**
	 * Funció que deshabilita la edició de la taula. L'usuari no pot editar la taula
	 * de qualsevol manera
	 * 
	 * @param table Taula a modificar
	 */
	public void deshabilitarEdicion(JTable table) {
		for (int i = 0; i < table.getRowCount(); i++) {
			for (int j = 0; j < table.getColumnCount(); j++) {
				table.setDefaultEditor(table.getColumnClass(j), null); // Desactiva el editor predeterminado
			}
		}
	}

	public JTable getTable8() {
		return table8;
	}

	public void setTable8(JTable table8) {
		this.table8 = table8;
	}

	public JTable getTable16() {
		return table16;
	}

	public void setTable16(JTable table16) {
		this.table16 = table16;
	}

	/**
	 * Funció que mostra el panel de records
	 */
	public void mostrarPanelRecords() {
		panelRecords.setVisible(true);
	}

	/**
	 * Funció que oculta el panel de records
	 */
	public void ocultarPanelRecords() {
		panelRecords.setVisible(false);
	}

	// BOTONES

	/**
	 * Funció que selecciona els botons amb els que es va a jugar i els fica a una lista de JButton que retorna.
	 * @param eleccion Eleccio del mode de joc. false per jugar 2x4 o true per jugar 4x4
	 * @return List<JButton> Lista de botons seleccionats
	 */
	public List<JButton> seleccionBotones(boolean eleccion) {
		List<JButton> botones = new ArrayList<>();

		if (eleccion) {
			botones.add(btnJuego0);
			botones.add(btnJuego1);
			botones.add(btnJuego2);
			botones.add(btnJuego3);
			botones.add(btnJuego4);
			botones.add(btnJuego5);
			botones.add(btnJuego6);
			botones.add(btnJuego7);
			botones.add(btnJuego8);
			botones.add(btnJuego9);
			botones.add(btnJuego10);
			botones.add(btnJuego11);
			botones.add(btnJuego12);
			botones.add(btnJuego13);
			botones.add(btnJuego14);
			botones.add(btnJuego15);
		} else {
			botones.add(btnJuego0);
			botones.add(btnJuego1);
			botones.add(btnJuego2);
			botones.add(btnJuego3);
			botones.add(btnJuego4);
			botones.add(btnJuego5);
			botones.add(btnJuego6);
			botones.add(btnJuego7);
		}

		return botones;
	}

	// PANEL SESIÓN

	public JPanel getPanelSesion() {
		return panelSesion;
	}

	public void setPanelSesion(JPanel panelSesion) {
		this.panelSesion = panelSesion;
	}

	/**
	 * Funció que mostra el panel de sesio
	 */
	public void mostrarPanelSesion() {
		panelSesion.setVisible(true);
	}

	/**
	 * Funció que oculta el panel de sesio
	 */
	public void ocultarPanelSesion() {
		panelSesion.setVisible(false);
	}

	public JTextField getTxtUsuario() {
		return txtUsuario;
	}

	public void setTxtUsuario(JTextField txtUsuario) {
		this.txtUsuario = txtUsuario;
	}

	public JTextField getTxtContra() {
		return txtContra;
	}

	public void setTxtContra(JTextField txtContra) {
		this.txtContra = txtContra;
	}

	public JLabel getLblRegistrar() {
		return lblRegistrar;
	}

	public void setLblRegistrar(JLabel lblRegistrar) {
		this.lblRegistrar = lblRegistrar;
	}

	public JButton getBtnRegistrarUsu() {
		return btnRegistrarUsu;
	}

	public void setBtnRegistrarUsu(JButton btnRegistrarUsu) {
		this.btnRegistrarUsu = btnRegistrarUsu;
	}

	/**
	 * Funció que mostra el JLabel i el JButton de registre
	 */
	public void mostrarRegistrar() {
		lblRegistrar.setVisible(true);
		btnRegistrarUsu.setVisible(true);
	}

	/**
	 * Funció que oculta el JLabel i el JButton de registre
	 */
	public void ocultarRegistrar() {
		lblRegistrar.setVisible(false);
		btnRegistrarUsu.setVisible(false);
	}

	public JLabel getLblIniSesion() {
		return lblIniSesion;
	}

	public void setLblIniSesion(JLabel lblIniSesion) {
		this.lblIniSesion = lblIniSesion;
	}

	public JButton getBtnIniSesionUsu() {
		return btnIniSesionUsu;
	}

	public void setBtnIniSesionUsu(JButton btnIniSesionUsu) {
		this.btnIniSesionUsu = btnIniSesionUsu;
	}

	/**
	 * Funció que mostra el JLabel i el JButton de inici de sesio
	 */
	public void mostrarIniSesion() {
		lblIniSesion.setVisible(true);
		btnIniSesionUsu.setVisible(true);
	}

	/**
	 * Funció que oculta el JLabel i el JButton de inici de sesio
	 */
	public void ocultarIniSesion() {
		lblIniSesion.setVisible(false);
		btnIniSesionUsu.setVisible(false);
	}
}
