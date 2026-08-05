package service;

/**
 * Servicio de validaciones para el sistema ATM.
 * Contiene métodos estáticos para validar diferentes tipos
 * de datos de entrada: PINs, montos, números de cuenta, etc.
 * Principios POO aplicados:
 * - Abstracción: Representa reglas de validación del dominio
 * - Encapsulamiento: Lógica de validación centralizada
 * Nota: Esta clase es estática porque no mantiene estado.
 * Todos sus métodos son puros (input -> output, sin efectos secundarios).
 */
public class ValidadorService {
    // CONSTANTES DE VALIDACIÓN
    // Longitud mínima del PIN
    private static final int PIN_MIN_LENGTH = 4;

    // Longitud máxima del PIN
    private static final int PIN_MAX_LENGTH = 6;

    // Monto mínimo para depósitos
    private static final double MONTO_MINIMO_DEPOSITO = 10000;

    // Monto mínimo para transferencias
    private static final double MONTO_MINIMO_TRANSFERENCIA = 1000;

    // Monto máximo por retiro
    private static final double MONTO_MAXIMO_RETIRO = 500000;

    // Longitud del número de tarjeta
    private static final int LONGITUD_TARJETA = 16;

    // VALIDACIÓN DE PIN
    /**
     * Valida que el PIN tenga el formato correcto.
     * Reglas:
     * - Debe tener entre 4 y 6 dígitos
     * - Solo debe contener números
     * - No puede ser nulo o vacío
     * @param pin PIN a validar
     * @return true si el PIN es válido, false en caso contrario
     */
    public static boolean validarPin(String pin) {
        if (pin == null || pin.trim().isEmpty()) {
            return false;
        }
        // Verificar longitud
        if (pin.length() < PIN_MIN_LENGTH || pin.length() > PIN_MAX_LENGTH) {
            return false;
        }
        // Verificar que solo contenga dígitos
        return pin.matches("\\d+");
    }

    /**
     * Valida que dos PINs coincidan.
     * @param pin1 Primer PIN
     * @param pin2 Segundo PIN
     * @return true si los PINs coinciden, false en caso contrario
     */
    public static boolean validarCoincidenciaPin(String pin1, String pin2) {
        if (pin1 == null || pin2 == null) {
            return false;
        }
        return pin1.equals(pin2);
    }

    // VALIDACIÓN DE MONTOS
    /**
     * Valida que el monto sea válido para retiro.
     * Reglas:
     * - Debe ser mayor a 0
     * - No puede exceder el máximo permitido
     * @param monto Monto a validar
     * @return true si el monto es válido, false en caso contrario
     */
    public static boolean validarMontoRetiro(double monto) {
        return monto > 0 && monto <= MONTO_MAXIMO_RETIRO;
    }

    /**
     * Valida que el monto sea válido para depósito.
     * Reglas:
     * - Debe ser mayor o igual al mínimo permitido
     * @param monto Monto a validar
     * @return true si el monto es válido, false en caso contrario
     */
    public static boolean validarMontoDeposito(double monto) {
        return monto >= MONTO_MINIMO_DEPOSITO;
    }

    /**
     * Valida que el monto sea válido para transferencia.
     * Reglas:
     * - Debe ser mayor o igual al mínimo permitido
     * @param monto Monto a validar
     * @return true si el monto es válido, false en caso contrario
     */
    public static boolean validarMontoTransferencia(double monto) {
        return monto >= MONTO_MINIMO_TRANSFERENCIA;
    }

    /**
     * Valida que el monto sea mayor a cero.
     * @param monto Monto a validar
     * @return true si el monto es válido, false en caso contrario
     */
    public static boolean validarMontoPositivo(double monto) {
        return monto > 0;
    }

    // VALIDACIÓN DE NÚMEROS DE CUENTA Y TARJETA
    /**
     * Valida el formato del número de cuenta.
     * Reglas:
     * - No puede ser nulo o vacío
     * - Debe tener entre 6 y 12 caracteres
     * @param numeroCuenta Número de cuenta a validar
     * @return true si el formato es válido, false en caso contrario
     */
    public static boolean validarNumeroCuenta(String numeroCuenta) {
        if (numeroCuenta == null || numeroCuenta.trim().isEmpty()) {
            return false;
        }
        return numeroCuenta.length() >= 6 && numeroCuenta.length() <= 12;
    }

    /**
     * Valida el formato del número de tarjeta.
     * Reglas:
     * - Debe tener exactamente 16 dígitos
     * - Solo debe contener números
     * @param numeroTarjeta Número de tarjeta a validar
     * @return true si el formato es válido, false en caso contrario
     */
    public static boolean validarNumeroTarjeta(String numeroTarjeta) {
        if (numeroTarjeta == null || numeroTarjeta.trim().isEmpty()) {
            return false;
        }
        return numeroTarjeta.length() == LONGITUD_TARJETA && 
               numeroTarjeta.matches("\\d+");
    }

    // VALIDACIÓN DE NOMBRES
    /**
     * Valida que el nombre del titular no esté vacío.
     * @param nombre Nombre a validar
     * @return true si el nombre es válido, false en caso contrario
     */
    public static boolean validarNombreTitular(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return false;
        }
        return nombre.trim().length() >= 3;
    }

    // MÉTODOS DE INFORMACIÓN
    /**
     * Obtiene el límite máximo de retiro.
     * @return Monto máximo de retiro
     */
    public static double getMontoMaximoRetiro() {
        return MONTO_MAXIMO_RETIRO;
    }

    /**
     * Obtiene el límite mínimo de depósito.
     * @return Monto mínimo de depósito
     */
    public static double getMontoMinimoDeposito() {
        return MONTO_MINIMO_DEPOSITO;
    }

    /**
     * Obtiene el límite mínimo de transferencia.
     * @return Monto mínimo de transferencia
     */
    public static double getMontoMinimoTransferencia() {
        return MONTO_MINIMO_TRANSFERENCIA;
    }

    /**
     * Obtiene la longitud del PIN válido.
     * @return Cadena con el rango de longitud válido
     */
    public static String getRangoLongitudPin() {
        return PIN_MIN_LENGTH + "-" + PIN_MAX_LENGTH;
    }
}
