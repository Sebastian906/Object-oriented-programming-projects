package exceptions;

/**
 * Excepción lanzada cuando no hay fondos suficientes para la operación.
 * Se produce cuando el usuario intenta retirar o transferir un monto
 * mayor al saldo disponible en la cuenta.
 * Hereda de: ATMException
 */
public class SaldoInsuficienteException extends ATMException {    
    // Saldo disponible en la cuenta
    private final double saldoDisponible;

    // Monto solicitado
    private final double montoSolicitado;

    /**
     * Constructor de SaldoInsuficienteException.
     * @param saldoDisponible Saldo actual disponible
     * @param montoSolicitado Monto que se intentó operar
     */
    public SaldoInsuficienteException(double saldoDisponible, double montoSolicitado) {
        super("Saldo insuficiente. Disponible: $" + 
              String.format("%,.2f", saldoDisponible) + 
              " - Solicitado: $" + String.format("%,.2f", montoSolicitado));
        this.saldoDisponible = saldoDisponible;
        this.montoSolicitado = montoSolicitado;
    }

    /**
     * Obtiene el saldo disponible.
     * @return Saldo disponible
     */
    public double getSaldoDisponible() {
        return saldoDisponible;
    }

    /**
     * Obtiene el monto solicitado.
     * @return Monto solicitado
     */
    public double getMontoSolicitado() {
        return montoSolicitado;
    }
}
