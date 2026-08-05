package models;

import utils.FormatUtil;

/**
 * Clase que representa una cuenta bancaria en el sistema ATM.
 * Esta clase encapsula todos los datos y comportamientos de una cuenta bancaria,
 * incluyendo número de cuenta, titular, saldo, PIN y número de tarjeta.
 * Principios POO aplicados:
 * - Abstracción: Representa los datos esenciales de una cuenta bancaria real
 * - Encapsulamiento: Atributos privados con acceso controlado via getters/setters
 * - Composición: Puede contener objetos Transaccion (a través del historial)
 */
public class CuentaBancaria {
    // ATRIBUTOS PRIVADOS (Encapsulamiento)

    // Número único de la cuenta bancaria
    @SuppressWarnings("FieldMayBeFinal")
    private String numeroCuenta;

    // Nombre del titular de la cuenta 
    @SuppressWarnings("FieldMayBeFinal")
    private String titular;

    // PIN de acceso (almacenado para validación)
    private String pin;

    // Saldo disponible en la cuenta
    private double saldo;

    // Número de tarjeta asociada a la cuenta
    @SuppressWarnings("FieldMayBeFinal")
    private String numeroTarjeta;

    // Estado de la cuenta (activa/inactiva)
    private boolean estaActiva;

    // CONSTRUCTOR

    /**
     * Constructor principal de CuentaBancaria.
     * @param numeroCuenta Número único de la cuenta
     * @param titular Nombre del titular de la cuenta
     * @param pin PIN de acceso (4-6 dígitos)
     * @param saldo Saldo inicial de la cuenta
     * @param numeroTarjeta Número de tarjeta asociada
     */
    public CuentaBancaria(String numeroCuenta, String titular, String pin, 
                          double saldo, String numeroTarjeta) {
        this.numeroCuenta = numeroCuenta;
        this.titular = titular;
        this.pin = pin;
        this.saldo = saldo;
        this.numeroTarjeta = numeroTarjeta;
        this.estaActiva = true;
    }

    // GETTERS (Lectura de atributos)
    /**
     * Obtiene el número de la cuenta bancaria.
     * @return Número de cuenta
     */
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    /**
     * Obtiene el nombre del titular de la cuenta.
     * @return Nombre del titular
     */
    public String getTitular() {
        return titular;
    }

    /**
     * Obtiene el saldo actual de la cuenta.
     * @return Saldo disponible
     */
    public double getSaldo() {
        return saldo;
    }

    /**
     * Obtiene el número de tarjeta asociada a la cuenta.
     * @return Número de tarjeta
     */
    public String getNumeroTarjeta() {
        return numeroTarjeta;
    }

    /**
     * Verifica si la cuenta está activa.
     * @return true si la cuenta está activa, false en caso contrario
     */
    public boolean estaActiva() {
        return estaActiva;
    }

    // MÉTODOS DE NEGOCIO
    /**
     * Valida si el PIN ingresado coincide con el PIN de la cuenta.
     * @param pinIngresado PIN a validar
     * @return true si el PIN es correcto, false en caso contrario
     */
    public boolean validarPin(String pinIngresado) {
        if (pinIngresado == null) {
            return false;
        }
        return this.pin.equals(pinIngresado);
    }
    
    /**
     * Cambia el PIN de la cuenta.
     * @param nuevoPin Nuevo PIN a establecer (4-6 dígitos)
     */
    public void cambiarPin(String nuevoPin) {
        this.pin = nuevoPin;
    }
    
    /**
     * Debita un monto de la cuenta si hay fondos suficientes.
     * @param monto Monto a debitar (debe ser mayor a 0)
     * @return true si la operación fue exitosa, false si fondos insuficientes
     */
    public boolean debitar(double monto) {
        if (monto > 0 && saldo >= monto) {
            saldo -= monto;
            return true;
        }
        return false;
    }
    
    /**
     * Acredita un monto a la cuenta.
     * @param monto Monto a acreditar (debe ser mayor a 0)
     */
    public void acreditar(double monto) {
        if (monto > 0) {
            saldo += monto;
        }
    }

    // Desactiva la cuenta bancaria.
    public void desactivar() {
        this.estaActiva = false;
    }

    // Activa la cuenta bancaria.
    public void activar() {
        this.estaActiva = true;
    }

    // REPRESENTACIÓN EN TEXTO

    /**
     * Retorna la representación en texto de la cuenta bancaria.
     * Útil para debugging y visualización.
     * @return Cadena con información de la cuenta
     */
    @Override
    public String toString() {
        return """
                CuentaBancaria {
                 N\u00famero: """ + numeroCuenta +
                "\n  Titular: " + titular +
                "\n  Saldo: " + FormatUtil.formatearMoneda(saldo) +
                "\n  Tarjeta: " + FormatUtil.enmascararTarjeta(numeroTarjeta) +
                "\n  Activa: " + (estaActiva ? "Sí" : "No");
    }
}
