package exceptions;

/**
 * Excepción lanzada cuando no se encuentra un usuario por su ID.
 */
public class UsuarioNoEncontradoException extends BibliotecaException {

    public UsuarioNoEncontradoException(String id) {
        super("No se encontró un usuario con el ID: " + id);
    }
}
