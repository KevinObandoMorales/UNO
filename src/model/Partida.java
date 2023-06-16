package model;

//Creamos la clase partida y sus valores.
public class Partida {
	private int id;
	private int id_carta;

	// Constructores

	// construct vacio
	public Partida() {
	}

	// construct con variables asignados
	public Partida(int id, int id_carta) {
		this.id = id;
		this.id_carta = id_carta;
	}

	// GETTERs y SETTERs
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_carta() {
		return id_carta;
	}

	public void setId_carta(int id_carta) {
		this.id_carta = id_carta;
	}

	// Crear toString como resultado final.
	@Override
	public String toString() {
		return "Partida [id=" + id + ", id_carta=" + id_carta + "]";
	}

}
