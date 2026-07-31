package exceptions;

/**
 * Excepción lanzada cuando no se encuentra un libro por su ISBN.
 */
public class LibroNoEncontradoException extends BibliotecaException {

    public LibroNoEncontradoException(String isbn) {
        super("No se encontró un libro con el ISBN: " + isbn);
    }
}
