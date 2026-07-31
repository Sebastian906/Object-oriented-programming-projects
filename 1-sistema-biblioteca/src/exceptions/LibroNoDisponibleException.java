package exceptions;

/**
 * Excepción lanzada cuando un libro no tiene ejemplares disponibles para préstamo.
 */
public class LibroNoDisponibleException extends BibliotecaException {

    public LibroNoDisponibleException(String titulo) {
        super("El libro '" + titulo + "' no tiene ejemplares disponibles.");
    }
}
