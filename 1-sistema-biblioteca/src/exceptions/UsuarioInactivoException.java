package exceptions;

/**
 * Excepción lanzada cuando un usuario inactivo intenta realizar una operación.
 */
public class UsuarioInactivoException extends BibliotecaException {

    public UsuarioInactivoException(String nombre) {
        super("El usuario '" + nombre + "' no está activo en el sistema.");
    }
}
