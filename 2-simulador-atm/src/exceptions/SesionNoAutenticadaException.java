package exceptions;

/**
 * Excepción lanzada cuando se intenta acceder a funcionalidades
 * sin haber iniciado sesión previamente.
 * Se produce cuando el usuario intenta realizar operaciones
 * antes de autenticarse con tarjeta y PIN.
 * Hereda de: ATMException
 */
public class SesionNoAutenticadaException extends ATMException {

    // Constructor de SesionNoAutenticadaException.
    public SesionNoAutenticadaException() {
        super("Debe iniciar sesión para realizar esta operación. " +
              "Inserte su tarjeta e ingrese su PIN.");
    }
}
