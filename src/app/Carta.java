package app;

import javax.swing.JButton;

public class Carta {
	private JButton boton;
	private String rutaImagen;
	
	public Carta(JButton boton, String rutaImagen) {
		this.boton = boton;
		this.rutaImagen = rutaImagen;
	}
	
	public JButton getBoton() {
		return boton;
	}
	public void setBoton(JButton boton) {
		this.boton = boton;
	}
	
	public String getRutaImagen() {
		return rutaImagen;
	}
	public void setRutaImagen(String rutaImagen) {
		this.rutaImagen = rutaImagen;
	}

}
