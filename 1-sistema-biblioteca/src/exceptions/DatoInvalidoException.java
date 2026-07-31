package exceptions;

/**
 * Excepción lanzada cuando se ingresa un dato inválido o con formato incorrecto.
 */
public class DatoInvalidoException extends BibliotecaException {

    public DatoInvalidoException(String campo, String razon) {
        super("Dato inválido en '" + campo + "': " + razon);
    }

    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
