package exceptions;

/**
 * Excepción lanzada cuando un usuario alcanza el límite máximo de préstamos activos.
 */
public class LimitePrestamosException extends BibliotecaException {

    public LimitePrestamosException(String nombreUsuario) {
        super("El usuario '" + nombreUsuario + "' ya tiene el máximo de 3 préstamos activos.");
    }
}
