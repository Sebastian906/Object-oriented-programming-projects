package exceptions;

/**
 * Excepción lanzada cuando la cuenta destino no existe en el sistema.
 * Se produce durante transferencias cuando se ingresa un número
 * de cuenta que no corresponde a ninguna cuenta registrada.
 * Hereda de: ATMException
 */
public class CuentaNoEncontradaException extends ATMException {
    // Número de cuenta no encontrada
    private final String numeroCuenta;

    /**
     * Constructor de CuentaNoEncontradaException.
     * @param numeroCuenta Número de cuenta no encontrada
     */
    public CuentaNoEncontradaException(String numeroCuenta) {
        super("Cuenta destino no encontrada: " + numeroCuenta);
        this.numeroCuenta = numeroCuenta;
    }

    /**
     * Obtiene el número de cuenta no encontrada.
     * @return Número de cuenta
     */
    public String getNumeroCuenta() {
        return numeroCuenta;
    }
}
