package app;

public class Sesion {

    private boolean iniciada;
    private String username;
    private String pass;

    public Sesion() {
    }

    public Sesion(String username, String pass) {
        this.iniciada = false;
        this.username = username;
        this.pass = pass;
    }

    /**
     * Funció que mostra les dades de la sesió actual
     * @return String amb la informació de la sesió
     */
    public String sesionToString() {
        String texto = "Sesión iniciada: " + iniciada +
                       ", Usuario: " + username +
                       ", Contraseña: " + pass;
        return texto;
    }

    public void setSesionIniciada(Boolean valor) {
        iniciada = valor;
    }

    public boolean getSesionIniciada() {
        return iniciada;
    }
    
    public String getUsername() {
    	return username;
    }
}
