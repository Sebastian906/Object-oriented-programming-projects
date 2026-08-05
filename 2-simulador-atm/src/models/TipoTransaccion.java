package models;

/**
 * Enumeración que define los tipos de transacciones disponibles en el ATM.
 * Cada constante representa un tipo de operación financiera que el usuario
 * puede realizar a través del cajero automático.
 * 
 * Principios POO aplicados:
 * - Abstracción: Representa tipos de operaciones del mundo real
 * - Encapsulamiento: Cada constante tiene un nombre y descripción
 */
public enum TipoTransaccion {
    // Consulta del saldo actual de la cuenta
    CONSULTA_SALDO("Consulta de Saldo"),

    // Retiro de efectivo de la cuenta
    RETIRO("Retiro de Efectivo"),

    // Depósito de efectivo en la cuenta
    DEPOSITO("Depósito de Efectivo"),

    // Transferencia de fondos a otra cuenta
    TRANSFERENCIA("Transferencia de Fondos"),

    // Cambio de PIN de la cuenta
    CAMBIO_PIN("Cambio de PIN");

    // Descripción legible del tipo de transacción
    private final String descripcion;

    /**
     * Constructor del enum.
     * @param descripcion Descripción legible del tipo de transacción
     */
    TipoTransaccion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la descripción del tipo de transacción.
     * @return Descripción legible del tipo
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Retorna la representación en texto del tipo de transacción.
     * @return Descripción del tipo de transacción
     */
    @Override
    public String toString() {
        return descripcion;
    }
}
