package exceptions;

/**
 * Excepción lanzada cuando el monto ingresado no es válido.
 * Se produce cuando el usuario ingresa un monto cero, negativo
 * o que excede los límites permitidos por el sistema.
 * Hereda de: ATMException
 */
public class MontoInvalidoException extends ATMException {
    // Monto inválido ingresado
    private final double montoIngresado;

    /**
     * Constructor de MontoInvalidoException.
     * @param montoIngresado Monto inválido
     */
    public MontoInvalidoException(double montoIngresado) {
        super("Monto inválido: $" + String.format("%,.2f", montoIngresado) + 
              ". El monto debe ser mayor a $0.");
        this.montoIngresado = montoIngresado;
    }

    /**
     * Constructor con mensaje personalizado.
     * @param montoIngresado Monto inválido
     * @param mensajePersonalizado Mensaje adicional
     */
    public MontoInvalidoException(double montoIngresado, String mensajePersonalizado) {
        super("Monto inválido: $" + String.format("%,.2f", montoIngresado) + 
              ". " + mensajePersonalizado);
        this.montoIngresado = montoIngresado;
    }

    /**
     * Obtiene el monto inválido.
     * @return Monto ingresado
     */
    public double getMontoIngresado() {
        return montoIngresado;
    }
}
