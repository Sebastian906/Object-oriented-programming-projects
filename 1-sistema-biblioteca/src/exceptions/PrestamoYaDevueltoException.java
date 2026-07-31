package exceptions;

/**
 * Excepción lanzada cuando se intenta devolver un préstamo que ya fue devuelto.
 */
public class PrestamoYaDevueltoException extends BibliotecaException {

    public PrestamoYaDevueltoException(String idPrestamo) {
        super("El préstamo " + idPrestamo + " ya fue devuelto anteriormente.");
    }
}
