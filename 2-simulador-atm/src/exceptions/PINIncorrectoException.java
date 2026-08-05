package exceptions;

/**
 * Excepción lanzada cuando el PIN ingresado es incorrecto.
 * Se produce cuando el usuario ingresa un PIN que no coincide
 * con el PIN registrado en la cuenta bancaria.
 * Hereda de: ATMException
 */
public class PINIncorrectoException extends ATMException {

    // Intentos restantes antes de bloquear la cuenta
    private final int intentosRestantes;

    /**
     * Constructor de PINIncorrectoException.
     * @param intentosRestantes Número de intentos restantes
     */
    public PINIncorrectoException(int intentosRestantes) {
        super("PIN incorrecto. Intentos restantes: " + intentosRestantes);
        this.intentosRestantes = intentosRestantes;
    }

    /**
     * Obtiene los intentos restantes.
     * @return Número de intentos restantes
     */
    public int getIntentosRestantes() {
        return intentosRestantes;
    }
}
