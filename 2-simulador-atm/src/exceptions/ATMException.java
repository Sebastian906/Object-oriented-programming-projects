package exceptions;

/**
 * Excepción base para todas las excepciones del dominio ATM.
 * Esta clase establece la jerarquía de excepciones personalizadas
 * del sistema, permitiendo un manejo específico de errores.
 * Principios POO aplicados:
 * - Herencia: Todas las excepciones del ATM heredan de esta clase
 * - Abstracción: Representa errores genéricos del sistema
 */
public class ATMException extends Exception {

    // Mensaje descriptivo del error
    private final String mensaje;

    /**
     * Constructor de ATMException.
     * @param mensaje Mensaje descriptivo del error
     */
    public ATMException(String mensaje) {
        super(mensaje);
        this.mensaje = mensaje;
    }

    /**
     * Obtiene el mensaje de error.
     * @return Mensaje descriptivo
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Retorna la representación en texto de la excepción.
     * @return Mensaje de error
     */
    @Override
    public String toString() {
        return "ATMException: " + mensaje;
    }
}
