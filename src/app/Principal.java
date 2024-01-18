package app;

public class Principal {

	public static void main(String[] args) {

		Vista vista = new Vista();
		Model model = new Model();
		Sesion sesion = new Sesion();
		Controlador controlador = new Controlador(vista, model, sesion);

	}

}
