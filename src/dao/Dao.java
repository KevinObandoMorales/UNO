package dao;

// Importamos las funciones del SQL
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Importamos Carta y Jugador del package MODEL
import model.Carta;
import model.Jugador;

public class Dao {

	// Primero hacemos una conexion con la BBDD. Para obtener los datos que
	// necesitamos.
	private Connection c;

	public static final String SCHEMA_NAME = "uno";
	public static final String CONNECTION = "jdbc:mysql://localhost:3306/" + SCHEMA_NAME;
	public static final String USER_CONNECTION = "root";
	public static final String PASS_CONNECTION = "";

	// Creamos el metodo CRUD (Crear, Leer, Actualizar y Borrar)
	// Asignamos las tablas del BBDD de cada funcion.
	public static final String GET_ALL_JUGADOR = "select * from jugador";
	public static final String ID_JUGADOR = "select id from jugador";
	public static final String USUARIO_JUGADOR = "select usuario from jugador";
	public static final String GET_ALL_CARD_JUGADOR = "select id,numero,color from carta where id_jugador =";
	public static final String GET_INFO_JUGADOR = "select * from jugador where id =";
	public static final String INSERT_JUGADOR = "insert into jugador (id, usuario, password, nombre, partidas, ganadas) values (?,?,?,?,?,?)";
	public static final String INSERT_CARD = "insert into carta (id, id_jugador, numero, color) values (?,?,?,?)";

	// Connectar en la Base de Datos
	public void connect() {
		String url = CONNECTION;
		String user = USER_CONNECTION;
		String pass = PASS_CONNECTION;
		try {
			c = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) { // Hacemos un catch para avisarnos de que nos falta algo.
			System.err.println(
			"[ERROR] - Activa XAMPP / WAMPP o MAMP y tener el MySQL abierto con la Base de Datos seleccionado.");
		}
	}

	// Desconnectar en la Base de Datos
	public void disconnect() throws SQLException {
		if (c != null) {
			c.close();
		}
	}

	// Insertar jugadores y guardarlos en la BBDD.
	public void addJugador(Jugador jugador) throws SQLException {
		try (PreparedStatement dataPlayer = c.prepareStatement(INSERT_JUGADOR)) {
			dataPlayer.setInt(1, jugador.getId());
			dataPlayer.setString(2, jugador.getUsuario());
			dataPlayer.setString(3, jugador.getPassword());
			dataPlayer.setString(4, jugador.getNombre());
			dataPlayer.setInt(5, jugador.getPartidas());
			dataPlayer.setInt(6, jugador.getGanadas());
			dataPlayer.executeUpdate();
		}
	}

	// Insertar cartas del Jugador y guardarlos en la BBDD.
	public void addCard2Jugador(Carta carta) throws SQLException {
		try (PreparedStatement dataCard = c.prepareStatement(INSERT_CARD)) {
			dataCard.setInt(1, carta.getId());
			dataCard.setInt(2, carta.getId_jugador());
			dataCard.setString(3, carta.getNumber());
			dataCard.setString(4, carta.getColor());
			dataCard.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// Contar las columnas de la tabla Jugador en la BBDD.
	public int countColumnJugador(int numColumnJugador) throws SQLException {
		int resul = 0;
		connect();
		try (Statement st = c.createStatement()) {
			try (ResultSet rs = st.executeQuery(GET_ALL_JUGADOR)) {
				while (rs.next()) {
					numColumnJugador++;
					resul = numColumnJugador;
				}
				rs.close();
			}
			st.close();
		}
		disconnect();
		return resul;
	}

	// Mostar nombre del Jugador.
	public void showNameJugador() throws SQLException {
		try (Statement st = c.createStatement()) {
			try (ResultSet rs = st.executeQuery(USUARIO_JUGADOR)) {
				while (rs.next()) {
					System.out.println("Jugador: " + rs.getString("usuario"));
				}
				rs.close();
			}
			st.close();
		}
	}

	// Mostrar cartas del Jugador.
	public void showCardsJugador(int idJugador, String usuario) throws SQLException {
		System.out.println("Cartas en mano de [" + usuario + "]\n");
		try (Statement st = c.createStatement()) {
			try (ResultSet rs = st.executeQuery(GET_ALL_CARD_JUGADOR + "'" + idJugador + "'")) {
				while (rs.next()) {
					System.out.println("Carta " + rs.getInt("id") + ", Color: " + rs.getNString("color")
							+ ", Number: " + rs.getString("numero"));
				}
				rs.close();
			}
			st.close();
		}
	}

	// Mostrar informacion del Jugador.
	public void showInfoJugador(int idJugador) throws SQLException {
		try (Statement st = c.createStatement()) {
			try (ResultSet rs = st.executeQuery(GET_INFO_JUGADOR + "'" + idJugador + "'")) {
				while (rs.next()) {
					System.out.println("Usuario: " + rs.getString("usuario") );
					System.out.println("Nombre: " + rs.getString("nombre") );
					System.out.println("Partidas: " + rs.getInt("partidas") );
					System.out.println("Ganadas: " + rs.getInt("ganadas") );
				}
				rs.close();
			}
			st.close();
		}
	}

	// Metodo, dar un punto ganada del Jugador.
	public void addGanada(int idJugador) throws SQLException {
		int numGanadas = 0;
		try (Statement st = c.createStatement()) {
			try (ResultSet rs = st.executeQuery(GET_INFO_JUGADOR + "'" + idJugador + "'")) {
				while (rs.next()) {
					numGanadas = rs.getInt("ganadas");
					numGanadas++;
				}
				rs.close();
			}
			// Insertar un punto valor en la tabla ganadas de la BBDD del Jugador.
			String query = "UPDATE jugador SET ganadas =" + "'" + numGanadas + "'" + " WHERE id=" + "'" + idJugador
					+ "'";
			st.execute(query);
			st.close();
			System.out.println("Valor ganadas actualizada correctamente.");
			st.close();
		}
	}

	// Metodo, eliminar cartas del Jugador
	public void deleteCartas(int idJugador, int idCarta) {
		try (Statement st = c.createStatement()) {
			String query = "DELETE FROM carta where id_jugador=" + "'" + idJugador + "'" + " and id =" + "'" + idCarta
					+ "'";
			st.execute(query);
			st.close();
			System.out.println("Carta eleminada de la DB");
			st.close();
		} catch (SQLException e) { // Advertencia
			// TODO Auto-generated catch block
			System.err.println("[ERROR] - Carta no eliminada");
		}
	}

	public Connection getConexion() {
		return c;
	}

	public void setConexion(Connection c) {
		this.c = c;
	}

}
