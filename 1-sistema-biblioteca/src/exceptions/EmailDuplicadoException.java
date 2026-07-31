package exceptions;

/**
 * Excepción lanzada cuando se intenta registrar un usuario con un email ya existente.
 */
public class EmailDuplicadoException extends BibliotecaException {

    public EmailDuplicadoException(String email) {
        super("Ya existe un usuario registrado con el email: " + email);
    }
}
