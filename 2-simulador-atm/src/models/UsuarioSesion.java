package models;

import java.time.LocalDateTime;

/**
 * Clase que maneja el estado de la sesión de un usuario en el ATM.
 * Controla la autenticación, intentos fallidos y bloqueo de la cuenta
 * por motivos de seguridad.
 * Principios POO aplicados:
 * - Abstracción: Representa el estado de una sesión de usuario
 * - Encapsulamiento: Estado interno protegido con acceso controlado
 */
public class UsuarioSesion {

    // CONSTANTES

    // Número máximo de intentos permitidos antes de bloquear la cuenta
    private static final int MAX_INTENTOS = 3;

    // ATRIBUTOS PRIVADOS (Encapsulamiento)

    // Cuenta actualmente autenticada
    private CuentaBancaria cuentaActual;

    // Estado de autenticación de la sesión
    private boolean estaAutenticada;

    // Contador de intentos fallidos de PIN
    private int intentosFallidos;

    // Fecha y hora de inicio de la sesión
    private LocalDateTime fechaInicio;

    // CONSTRUCTOR

    /**
     * Constructor por defecto de UsuarioSesion.
     * Inicializa una sesión no autenticada.
     */
    public UsuarioSesion() {
        this.cuentaActual = null;
        this.estaAutenticada = false;
        this.intentosFallidos = 0;
        this.fechaInicio = null;
    }

    // GETTERS

    /**
     * Obtiene la cuenta actualmente autenticada.
     * @return Cuenta actual o null si no hay sesión activa
     */
    public CuentaBancaria getCuentaActual() {
        return cuentaActual;
    }

    /**
     * Verifica si hay una sesión autenticada.
     * @return true si hay sesión activa, false en caso contrario
     */
    public boolean estaAutenticada() {
        return estaAutenticada;
    }

    /**
     * Obtiene el número de intentos fallidos.
     * @return Número de intentos fallidos
     */
    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    /**
     * Obtiene la fecha de inicio de la sesión.
     * @return Fecha y hora de inicio
     */
    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    // MÉTODOS DE AUTENTICACIÓN

    /**
     * Intenta autenticar al usuario con la cuenta y PIN proporcionados.
     * @param cuenta Cuenta a autenticar
     * @param pinIngresado PIN ingresado por el usuario
     * @return true si la autenticación fue exitosa, false en caso contrario
     */
    public boolean autenticar(CuentaBancaria cuenta, String pinIngresado) {
        // Si la cuenta está bloqueada, no permitir autenticación
        if (estaBloqueada()) {
            return false;
        }

        // Validar que la cuenta esté activa
        if (!cuenta.estaActiva()) {
            return false;
        }

        // Validar el PIN
        if (cuenta.validarPin(pinIngresado)) {
            this.cuentaActual = cuenta;
            this.estaAutenticada = true;
            this.intentosFallidos = 0;
            this.fechaInicio = LocalDateTime.now();
            return true;
        } else {
            this.intentosFallidos++;
            return false;
        }
    }

    /**
     * Cierra la sesión actual.
     * Resetea todos los valores a su estado inicial.
     */
    public void cerrarSesion() {
        this.cuentaActual = null;
        this.estaAutenticada = false;
        this.intentosFallidos = 0;
        this.fechaInicio = null;
    }

    // Incrementa el contador de intentos fallidos.
    public void incrementarIntentos() {
        this.intentosFallidos++;
    }

    // Resetea el contador de intentos fallidos.
    public void reiniciarIntentos() {
        this.intentosFallidos = 0;
    }

    // MÉTODOS DE VALIDACIÓN

    /**
     * Verifica si la sesión está bloqueada por intentos fallidos.
     * @return true si se alcanzó el máximo de intentos, false en caso contrario
     */
    public boolean estaBloqueada() {
        return intentosFallidos >= MAX_INTENTOS;
    }

    /**
     * Obtiene el número máximo de intentos permitidos.
     * @return Máximo de intentos
     */
    public int getMaxIntentos() {
        return MAX_INTENTOS;
    }

    /**
     * Obtiene los intentos restantes antes del bloqueo.
     * @return Número de intentos restantes
     */
    public int getIntentosRestantes() {
        return MAX_INTENTOS - intentosFallidos;
    }

    // REPRESENTACIÓN EN TEXTO

    /**
     * Retorna la representación en texto de la sesión.

     * @return Cadena con información de la sesión
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("UsuarioSesion {")
          .append("\n  Autenticada: ").append(estaAutenticada ? "Sí" : "No")
          .append("\n  Intentos Fallidos: ").append(intentosFallidos)
          .append("\n  Bloqueada: ").append(estaBloqueada() ? "Sí" : "No");

        if (cuentaActual != null) {
            sb.append("\n  Cuenta: ").append(cuentaActual.getNumeroCuenta())
              .append("\n  Titular: ").append(cuentaActual.getTitular());
        }

        if (fechaInicio != null) {
            sb.append("\n  Inicio Sesión: ").append(fechaInicio);
        }

        sb.append("\n}");
        return sb.toString();
    }
}
