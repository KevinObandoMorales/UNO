package service;

// Importamos lo que necessitamos.

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import exception.UNO_exception;
import java.sql.Connection;
// import java.sql.DriverManager;

// Imporamos las clases del MODEL y DAO.
import model.Carta;
import model.Jugador;
import dao.Dao;

public class UNO_services {

	// Llamamos la clase que contiene el DAO.
	Dao dao;

	public UNO_services() {
		dao = new Dao();
	}

	// Creamos un metodo init inicial para iniciar el programa.
	public void init() {
		Scanner sc = new Scanner(System.in);
		UNO_services uno_sv = new UNO_services();
		try {
			try {
				uno_sv.startGame(sc, uno_sv);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			try {
				dao.disconnect();
			} catch (SQLException e) {
				System.out.println("[Error] - Fallo en disconnect");
			}
		}
	}

	// Metodo de creacion de la cuenta del user, con login y registrar.
	public void startGame(Scanner sc, UNO_services uno_sv) throws SQLException {

		// Creamos los variables necesarios del Jugador.
		int selecNum;
		String user, password, nombre;
		int partidas = 0;
		int ganadas = 0;
		int idJugador = 0;
		int numColumnJugador = 0;
		int resul = 0;
		boolean isOk = true;
		boolean createUser;
		boolean boolExistUser = false;
		boolean boolExistPassword = false;

		// Llamamos la conexion del DAO.
		Connection c;

		// Creamos un menu selector con swicth, un bucle while y un try and catch
		while (isOk) {
			try {
				// Creamos las opciones del menu.
				System.out.println("\n--------------- UNO -----------------\n");
				System.out.println("1. Login\n2. Registrar\n3. Mostrar Jugadores\n4. Salir\n");
				System.out.println("-------------------------------------");
				selecNum = sc.nextInt();
				switch (selecNum) {

					case 1:
						// [1]-Login

						// Llamamos el metodo contar jugadores, si existe el Jugador en el Juego:
						resul = dao.countColumnJugador(numColumnJugador);

						if (resul == 0)
							System.err.println("User no encontrado. Debes registrarte antes de Iniciar.\n");

						else {
							// Si existe el Jugador, nos preguntaran el user y su password para acceder.
							System.out.println("Nombre del User para acceder:");
							sc.nextLine();
							user = sc.nextLine();
							System.out.println("Password del user:");
							password = sc.nextLine();
							// Comprobar si el nombre del user y el password son correctas o no.
							// Conectamos en la BBDD
							dao.connect();
							c = dao.getConexion(); // Llamamos la conexion del DAO
							System.out.println("\nBuscando... \nUsuario: " + user + " con Password: " + password);
							try (Statement st = c.createStatement()) {
								try (ResultSet rs = st.executeQuery("select usuario from jugador")) {
									while (rs.next()) {
										if (user.equalsIgnoreCase(rs.getString("usuario"))) {
											boolExistUser = true;
											// Se guardara la ID del jugador en la variable idJugador.
											dao.connect();
											c = dao.getConexion(); // Llamamos la conexion del DAO
											try (Statement st1 = c.createStatement()) {
												try (ResultSet loginSet = st1.executeQuery(
														"select id from jugador where usuario = " + "'" + user + "'")) {
													while (loginSet.next()) {
														idJugador = loginSet.getInt("id");
													}
													loginSet.close();
												}
												st1.close();
											}
											// desConectamos en la BBDD
											dao.disconnect();
										}
									}
									rs.close();
								}
								try (ResultSet rs = st.executeQuery("select password from jugador")) {
									while (rs.next()) {
										if (password.equalsIgnoreCase(rs.getString("password"))) {
											boolExistPassword = true;
										}
									}
									rs.close();
								}
								st.close();
							}
							// desConectamos en la BBDD
							dao.disconnect();
							// Si el usuario existe, el juego UNO se ejecutara.
							if ((boolExistUser == true) && (boolExistPassword == true)) {
								System.out.println("Usuario confirmado.\n");
								// Llamar al methodo start_UNO. y pasando el nombre del jugador al parametro.
								start_UNO(user, sc, uno_sv, idJugador, c);
							} else { // De lo contrario, se cerrara el programa.
								isOk = false;
								System.err.println("User y/o Password incorrecto/s. \n Cerrando programa.");
							}
						}
						break;

					case 2:
						// [2]-Registrar
						createUser = true;
						/*
						 * Creacion de users: Nos preguntara el nombre de user que eligamos, y una vez
						 * creado, las partidas y ganadas empiezan desde 0.
						 */
						System.out.println("Nombre del user:");
						sc.nextLine();
						user = sc.nextLine();
						dao.connect();
						c = dao.getConexion(); // Llamamos la conexion del DAO
						try (Statement st = c.createStatement()) {
							try (ResultSet rs = st.executeQuery("select usuario from jugador")) {
								while (rs.next()) {
									if (user.equalsIgnoreCase(rs.getString("usuario"))) {
										createUser = false;
									}
								}
								rs.close();
							}
							st.close();
						}
						// No se puede crear usuarios iguales. Ponemos un if / else.
						if (createUser == true) {
							dao.disconnect();
							System.out.println("Nombre del User:");
							nombre = sc.nextLine();
							System.out.println("Password del User:");
							password = sc.nextLine();
							// Crear nuevo objeto de jugador.
							addUser(new Jugador(user, password, nombre, partidas, ganadas));
							System.out.println("El Jugador [" + user + "] se ha registrado en la Base de Datos.\n");
						} else {
							System.err.println("Se ha detectado otro User con el mismo nombre.\n");
						}
						break;

					case 3:
						// [3]-Mostrar Jugadores
						// Se mostrara los Jugadores registrados.
						uno_sv.mostrarJugadores();
						break;

					case 4:
						// [4]-Salir
						isOk = false;
						System.out.println("Saliendo del programa.");
						break;
					default:
						throw new UNO_exception();
				}
			} catch (UNO_exception ex) {
				System.err.println("El numero selecionado no existe. Solo del 1 al 4.");
			}
		}
	}

	// Metodo para iniciar el juego UNO.
	private void start_UNO(String user, Scanner sc, UNO_services uno_sv, int idJugador, Connection c)
			throws SQLException {

		// Los variables:
		boolean isOk = true;
		boolean comproCart = false;

		System.out.println("\n\n\nBienvenido al UNO");
		dao.connect();
		c = dao.getConexion(); // Llamamos la conexion del DAO
		try (Statement st1 = c.createStatement()) {
			// Seleccionamos la id de la tabla carta donde el id_jugador,
			// es la id del jugador logeado del usuario.
			try (ResultSet loginSet = st1
					.executeQuery("select id from carta where id_jugador = " + "'" + idJugador + "'")) {
				while (loginSet.next()) {
					comproCart = true;
				}
				loginSet.close();
			}
			st1.close();
		}
		dao.disconnect();// desConectamos en la BBDD

		// El jugador tendra que robar 7 cartas, si tiene 0 cartas, porque es un nuevo
		// Jugador.
		if (comproCart == false) {
			int i = 0;
			System.out.println("Generando cartas...");
			while (i < 7) {
				Carta card = new Carta(idJugador);
				String color = card.getColor();
				String num = card.getNumber();

				// El CAMBIOCOLOR y MASCUATRO son las cartas negras.
				if (
				// No se creara la carta
				color.equals("NEGRO") && (num.equals("CERO") || num.equals("UNO") || num.equals("DOS")
						|| num.equals("TRES") || num.equals("CUATRO") || num.equals("CINCO") || num.equals("SEIS")
						|| num.equals("SIETE") || num.equals("OCHO") || num.equals("NUEVE") || num.equals("CAMBIO")
						|| num.equals("MASDOS") || num.equals("SALTO"))) {
				} else {
					// Se creara la carta;
					System.out.println("Color: " + color + " Number/Type: " + num);
					// Generamos cartas llamando al metodo insertCard2Jugador.
					uno_sv.insertCard2Jugador(card);
					i++;
				}
			}
			System.out.println("\nSe han generado las cartas.");
		}

		// Creamos las mecanicas del juego UNO.
		System.out.println("\n Jugador " + user + " con id " + idJugador);
		// Creamos un menu o selector del jugador:
		while (isOk) {
			System.out.println("\n" + "1. Ver cartas" + "\n2. Mi info " + "\n3. Tirar Cartas"
					+ "\n4. Obtener otra carta nueva");
			int sele;
			sele = sc.nextInt();
			switch (sele) {

				case 1:
					uno_sv.showCards(idJugador, user);
					break;

				case 2:
					uno_sv.infoJugador(idJugador);
					break;

				case 3:
					int idCart;
					// Ponemos el codigo de comprobacion del juego de las cartas.
					boolean if_Card_Exist = false;
					System.out.println("\nNum. ID de la carta para tirar:");
					idCart = sc.nextInt();
					dao.connect();
					c = dao.getConexion(); // Llamamos la conexion del DAO
					try (Statement st = c.createStatement()) {
						try (ResultSet idCard_Select = st.executeQuery("select * from carta where id_jugador = " + "'"
								+ idJugador + "'" + " and id = " + idCart)) {
							while (idCard_Select.next()) {
								if_Card_Exist = true;
								System.out.println("Carta tirada:");
								System.out.println("Carta " + idCard_Select.getString("id") + "\nColor: "
										+ idCard_Select.getString("color") + "\nNumero: "
										+ idCard_Select.getString("numero"));
								// Al tirar una carta, se eleminara la carta:
								uno_sv.deleteCard(idJugador, idCart);
								// Si sale un SALTO o CAMBIO → Eliminamos esa carta de la tabla
								// carta y partida y terminamos el programa.
								if (idCard_Select.getString("numero").equals("SALTO")
										|| idCard_Select.getString("numero").equals("CAMBIO")) {
									// Eliminar carta.
									uno_sv.deleteCard(idJugador, idCart);
									isOk = false;
									// Mostrar numero de las partidas ganadas del Jugador;
									uno_sv.partidaGanada(idJugador);
									System.out.println("Has ganado esta partida. Cerrando partida.");
								}
							}
							if (if_Card_Exist == false) {
								System.err.println("No existe este carta, intentalo de nuevo.");
							}
							idCard_Select.close();
						}
						st.close();
					}
					dao.disconnect();// Desconecar BBDD.
					break;

				case 4:
					getCard(idJugador, uno_sv);
					break;
				default:
					System.err.println("El numero selecionado no existe. Solo del 1 al 4.");
			}
		}
	}

	private void getCard(int idJugador, UNO_services uno_sv) {
		// Si el jugador no puede tirar cartas pues genera otra carta nueva.
		Carta card = new Carta(idJugador);
		String color = card.getColor();
		String num = card.getNumber();

		// El CAMBIOCOLOR y MASCUATRO son las cartas negras.

		if (
		// No se creara la carta si es negra y alguna de las otras combinaciones
		color.equals("NEGRO") && (num.equals("CERO") || num.equals("UNO") || num.equals("DOS")
				|| num.equals("TRES") || num.equals("CUATRO") || num.equals("CINCO") || num.equals("SEIS")
				|| num.equals("SIETE") || num.equals("OCHO") || num.equals("NUEVE") || num.equals("CAMBIO")
				|| num.equals("MASDOS") || num.equals("SALTO"))) {

			getCard(idJugador, uno_sv);
		} else {
			// Creara la carta y se evalua si es un MASDOS O MASCUATRO
			System.out.println("Color: " + color + " Number/Type: " + num);
			// Generamos cartas llamando al metodo insertCard2Jugador.
			try {
				uno_sv.insertCard2Jugador(card);
			} catch (SQLException e) {

			}
			if (num.equals("MASCUATRO")) {
				for (int i = 0; i < 4; i++) {
					getCard(idJugador, uno_sv);
				}
				System.out.println("Te ha salido un MASCUATRO, se ha añadido 4 a tu mazo");
			} else if (num.equals("MASDOS")) {
				for (int i = 0; i < 2; i++) {
					getCard(idJugador, uno_sv);
				}
				System.out.println("Te ha salido un MASDOS, se han añadido 2 a tu mazo");
			}
		}
	}

	// El metodo de crear Usuario
	private void addUser(Jugador jugador) throws SQLException {
		dao.connect();
		dao.addJugador(jugador);
		dao.disconnect();
	}

	// El metodo de mostrar Usuarios registrados en la BBDD
	private void mostrarJugadores() throws SQLException {
		dao.connect();
		dao.showNameJugador();
		dao.disconnect();
	}

	// El metodo de mostrar cartas del Jugador
	private void showCards(int idJugador, String user) throws SQLException {
		dao.connect();
		dao.showCardsJugador(idJugador, user);
		dao.disconnect();
	}

	// El metodo de insertar cartas del Jugador
	private void insertCard2Jugador(Carta carta) throws SQLException {
		dao.connect();
		dao.addCard2Jugador(carta);
		dao.disconnect();
	}

	// Metodo informacion del Jugador
	private void infoJugador(int idJugador) throws SQLException {
		dao.connect();
		dao.showInfoJugador(idJugador);
		dao.disconnect();
	}

	// Metodo de dar 1 punto ganado del Jugador
	private void partidaGanada(int idJugador) throws SQLException {
		dao.connect();
		dao.addGanada(idJugador);
		dao.disconnect();
	}

	// Metodo de eliminar carta
	private void deleteCard(int idJugador, int idCarta) throws SQLException {
		dao.connect();
		dao.deleteCartas(idJugador, idCarta);
		dao.disconnect();
	}
}
