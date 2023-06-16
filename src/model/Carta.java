package model;

import java.util.Random;

// Creamos la clase carta y sus valores.
public class Carta {
	private int id; // Este id es AUTO_INCREMENT del Base de Datos
	private int id_jugador;
	private String color;
	private String number;

	// ENUMs
	// Creamos los colores que necessitamos para el metodo.
	public enum Color {
		ROJO, AMARILLO, VERDE, AZUL, NEGRO;

		// Crear un metodo, para generar un COLOR aleatorio.
		static String getColorRandom() {
			Random r_color = new Random();
			int colorSelec = r_color.nextInt(5) + 0;
			return Color.values()[colorSelec].toString();
		}
	}

	// Creamos los numeros y las otras cartas especiales, para usar en el metodo
	// siguiente:
	public enum Numero {
		CERO, UNO, DOS, TRES, CUATRO, CINCO, SEIS, SIETE, OCHO, NUEVE, CAMBIO, MASDOS, SALTO, CAMBIOCOLOR, MASCUATRO;

		// Crear un metodo, para generar un NUMERO aleatorio.
		static String getNumberRandom() {
			Random r_number = new Random();
			int numSelec = r_number.nextInt(15) + 0;
			return Numero.values()[numSelec].toString();
		}
	}

	// Constructores

	// construct vacio
	public Carta() {
	};

	// construct con variables asignados
	public Carta(int id_jugador) {
		this.id_jugador = id_jugador;
		this.color = Color.getColorRandom();
		this.number = Numero.getNumberRandom();
	}

	// GETTERs y SETTERs
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId_jugador() {
		return id_jugador;
	}

	public void setId_jugador(int id_jugador) {
		this.id_jugador = id_jugador;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	// Crear toString como resultado final.
	@Override
	public String toString() {
		return "Carta [id=" + id + ", id_jugador=" + id_jugador + ", color=" + color + ", number=" + number + "]";
	}

}
