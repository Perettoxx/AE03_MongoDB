package app;

import java.util.List;

public class Juego {
	private List<Carta> tarjetas;
	private boolean[] pulsadas = {false, false};
	
	public Juego (List<Carta> tarjetas) {
		this.tarjetas = tarjetas;
	}

	public List<Carta> getTarjetas() {
		return tarjetas;
	}

	public void setTarjetas(List<Carta> tarjetas) {
		this.tarjetas = tarjetas;
	}
	
	public boolean[] getPulsadas() {
		return pulsadas;
	}
	
	public void setPulsadas(boolean[] pulsadas) {
		this.pulsadas = pulsadas;
	}
}
