package exceptions;

/**
 * Excepción base para todas las excepciones del sistema de biblioteca.
 * Permite manejar todas las excepciones específicas de forma genérica.
 * Conceptos POO aplicados:
 * - Herencia (todas las excepciones heredan de esta)
 * - Encapsulamiento de mensaje de error
 */
public class BibliotecaException extends Exception {

    /**
     * Constructor con mensaje de error.
     * @param mensaje Descripción del error
     */
    public BibliotecaException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa original.
     * @param mensaje Descripción del error
     * @param causa   Excepción original que causó este error
     */
    public BibliotecaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
