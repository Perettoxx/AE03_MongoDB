package app;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JOptionPane;

import org.bson.Document;

public class Controlador {

	Vista vista;
	Model model;
	Sesion sesion;

	/**
	 * Funció que prepara totes les pantalles i panels per el correct funcionament de l'aplicació.
	 * A la classe Vista: executa la preparacio previa, oculta tots els panels i deshabilita els botons de jugar i records (no s'ha iniciat sessió encara)
	 * Importa també les imatges de la BDD
	 */
	public void Inicial() {
		vista.preparacionPrevia();
		vista.ocultarPanelRecords();
		vista.ocultarPanelTemporizador();
		vista.ocultarPanelJuego();
		vista.ocultarPanelSesion();
		vista.deshabilitarJugarRecords();
		model.importarImagenes();
	}
	
	/**
	 * Funció que oculta tots els panels
	 */
	public void ocultarTodo() {
		vista.ocultarPanelSesion();
		vista.ocultarPanelJuego();
		vista.ocultarPanelTemporizador();
		vista.ocultarPanelRecords();
	}

	/**
	 * Funció que escolta el click sobre el botó de registre dels botons laterals
	 */
	public void Registro() {
		vista.getBtnRegistrar().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				ocultarTodo();
				vista.ocultarIniSesion();
				vista.mostrarRegistrar();
				vista.mostrarPanelSesion();
			}
		});
	}

	/**
	 * Funció que escolta el click sobre el botó de registrarse al panel de registre
	 */
	public void RegistroUsu() {
		vista.getBtnRegistrarUsu().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (model.RegistrarUsuario(vista.getTxtUsuario().getText(), vista.getTxtContra().getText())) {
					System.out.println("Se ha iniciado sesión de manera correcta");
					JOptionPane.showMessageDialog(vista, "Registro de usuario completado correctamente", "Registro usuario", JOptionPane.INFORMATION_MESSAGE);
					ocultarTodo();
				} else {
					System.err.println("Algo ha pasado en el registro del usuario");
				}
			}
		});
	}

	/**
	 * Funció que escolta el click sobre el botó de inici de sessió dels botons laterals
	 */
	public void InicioSesion() {
	    vista.getBtnIniSesion().addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            if (!sesion.getSesionIniciada()) {
	            	ocultarTodo();
	                vista.ocultarRegistrar();
	                vista.mostrarIniSesion();
	                vista.mostrarPanelSesion();
	            } else {
	                // Ventana emergente indicando que la sesión ya está iniciada
	                JOptionPane.showMessageDialog(vista, "La sesión ya está iniciada. Si quiere iniciar sesión de nuevo, reinicie la aplicación.", "Sesión iniciada", JOptionPane.INFORMATION_MESSAGE);
	            }
	        }
	    });
	}

	/**
	 * Funció que escolta el click sobre el botó de inici de sessió al panel d'inici de sessió
	 */
	public void InicioSesionUsu() {
		vista.getBtnIniSesionUsu().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (model.IniSesionUsuario(vista.getTxtUsuario().getText(), vista.getTxtContra().getText())) {
					System.out.println("Se ha iniciado sesión de manera correcta");
					vista.habilitarJugarRecords();
					sesion = new Sesion(vista.getTxtUsuario().getText(), vista.getTxtContra().getText());
					sesion.setSesionIniciada(true);
					ocultarTodo();
				} else {
					System.err.println("Algo ha pasado en el inicio de sesión del usuario");
				}
			}
		});
	}

	/**
	 * Funció que escolta el click sobre el botó de joc dels botons laterals.
	 * En cas de no estar habilitat no fa res.
	 */
	public void Jugar() {
		vista.getBtnJugar().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!vista.getBtnJugar().isEnabled()) {
					// La tarjeta está deshabilitada, no hacer nada
					return;
				}
				ocultarTodo();
				vista.mostrarPanelJuego();
			}
		});

	}
	
	/**
	 * Funció que escolta el click sobre el botó de joc al panel de joc.
	 * Depenent de la selecció del radio button s'executa el joc en 2x4 o en 4x4.
	 * També escolta si la partida ha finalitzat per mostrar un missatfe per guardar el temps, comparar aquest amb la resta i deshabilitar el botó de joc per que no es puga jugar una altra vegada.
	 * NOTA: Solament compara el registre amb la resta si es vol guardar el registre!
	 */
	public void JugarPartida() {
		vista.getBtnJugarPartida().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(vista.getRadio2x4().isSelected()) {
					vista.mostrarPanelTemporizador();
					vista.juego(vista.preparacionJuego(false));
				} else if (vista.getRadio4x4().isSelected()){
					vista.mostrarPanelTemporizador();
					vista.juego(vista.preparacionJuego(true));
				} else {
					System.err.println("Algo raro ha pasado con la selección del modo");
				}
			}
		});
		
		vista.setJuegoFinalizado(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent evt) {
	            int respuesta = JOptionPane.showConfirmDialog(null, "¿Quieres guardar el registro?", "Guardar Registro", JOptionPane.YES_NO_OPTION);
	            if (respuesta == JOptionPane.YES_OPTION) {
	                if (model.compararTiempo(vista.getDificultad(), vista.getTiempo())) {
	                    String mensaje = "¡ENHORABUENA, ERES EL NUEVO RECORD!";
	                    JOptionPane.showMessageDialog(null, mensaje, "Rompe records", JOptionPane.INFORMATION_MESSAGE);
	                } else {
	                    System.out.println("El tiempo no es el record");
	                }
	                model.guardarTiempo(sesion.getUsername(), vista.getDificultad(), vista.getTiempo());
	            }
	            vista.getBtnJugar().setEnabled(false);
	            ocultarTodo();
	        }
	    });
	}

	/**
	 * Funció que escolta el click sobre el botó de records dels botons laterals.
	 * En cas de no estar habilitat no fa res.
	 */
	public void Records() {
		vista.getBtnRecords().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!vista.getBtnRecords().isEnabled()) {
					// La tarjeta está deshabilitada, no hacer nada
					return;
				}
				ocultarTodo();
				vista.mostrarPanelRecords();
				
				List<Document> registros8 = model.seleccionRecords(8);
	            List<Document> registros16 = model.seleccionRecords(16);

	            // Mostrar registros en las tablas correspondientes
	            vista.importarDatos(vista.getTable8(), registros8);
	            vista.importarDatos(vista.getTable16(), registros16);
			}
		});
	}

	/**
	 * Funció que executa la resta de funcions
	 * @param vista Vista
	 * @param model Model
	 * @param sesion Sesion
	 */
	public Controlador(Vista vista, Model model, Sesion sesion) {
		this.vista = vista;
		this.model = model;
		this.sesion = sesion;
		// SE EJECUTA SIEMPRE AL PRINCIPIO
		Inicial();
		//REGISTRO
		Registro();
		RegistroUsu();
		//INICIO DE SESIÓN
		InicioSesion();
		InicioSesionUsu();
		//JUGAR
		Jugar();
		JugarPartida();
		//RECORDS
		Records();
	}
}