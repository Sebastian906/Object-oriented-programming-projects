package exceptions;

/**
 * Excepción lanzada cuando no se encuentra un préstamo por su ID.
 */
public class PrestamoNoEncontradoException extends BibliotecaException {

    public PrestamoNoEncontradoException(String id) {
        super("No se encontró un préstamo con el ID: " + id);
    }
}
