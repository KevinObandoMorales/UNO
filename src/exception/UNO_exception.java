package exception;

// Creamos un Exception como aviso de Error.
public class UNO_exception extends Exception {
	public UNO_exception() {

	}

	public UNO_exception(String ex_error) {
		super(ex_error);
	}

}
