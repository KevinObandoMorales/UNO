package model;

//Creamos la clase jugador y sus valores.
public class Jugador {
	private int id; // Este id es AUTO_INCREMENT del Base de Datos
	private String usuario;
	private String password;
	private String nombre;
	private int partidas;
	private int ganadas;

	// Constructores

	// construct vacio
	public Jugador() {
	}

	// construct con variables asignados
	public Jugador(String usuario, String password, String nombre, int partidas, int ganadas) {
		this.usuario = usuario;
		this.password = password;
		this.nombre = nombre;
		this.partidas = partidas;
		this.ganadas = ganadas;
	}

	// GETTERs y SETTERs
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getPartidas() {
		return partidas;
	}

	public void setPartidas(int partidas) {
		this.partidas = partidas;
	}

	public int getGanadas() {
		return ganadas;
	}

	public void setGanadas(int ganadas) {
		this.ganadas = ganadas;
	}

	// Crear toString como resultado final.
	@Override
	public String toString() {
		return "Jugador [id=" + id + ", usuario=" + usuario + ", password=" + password + ", nombre=" + nombre
				+ ", partidas=" + partidas + ", ganadas=" + ganadas + "]";
	}

}
