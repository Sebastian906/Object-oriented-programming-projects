package exceptions;

/**
 * Excepción lanzada cuando la cuenta ha sido bloqueada por seguridad.
 * Se produce cuando se alcanza el número máximo de intentos fallidos
 * de PIN, activando el mecanismo de seguridad del sistema.
 * Hereda de: ATMException
 */
public class CuentaBloqueadaException extends ATMException {
    /**
     * Constructor de CuentaBloqueadaException.
     * @param numeroCuenta Número de la cuenta bloqueada
     */
    public CuentaBloqueadaException(String numeroCuenta) {
        super("Cuenta " + numeroCuenta + " bloqueada por seguridad. " +
              "Contacte a su banco para desbloquearla.");
    }
}
