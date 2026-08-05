package controller;

import service.CajeroService;
import service.ValidadorService;
import models.CuentaBancaria;
import models.Transaccion;

import exceptions.*;

import java.util.List;

/**
 * Controlador principal del sistema ATM.
 * Actúa como intermediario entre la vista (interfaz gráfica) y el
 * modelo (servicios de negocio). Coordina las operaciones del ATM
 * y maneja las excepciones de forma centralizada.
 * Principios POO aplicados:
 * - Single Responsibility: Coordina modelo y vista
 * - Dependency Inversion: Depende de abstracciones (servicios)
 * - Open/Closed: Puede extenderse sin modificar código existente
 * Flujo de trabajo:
 * 1. La vista envía peticiones al controlador
 * 2. El controlador valida datos usando servicios
 * 3. El controlador delega al CajeroService
 * 4. El controlador retorna resultados a la vista
 */
public class CajeroController {
    // ATRIBUTOS
    // Servicio principal del ATM
    private final CajeroService cajeroService;

    // CONSTRUCTOR
    /**
     * Constructor de CajeroController.
     * @param cajeroService Servicio principal del ATM
     */
    public CajeroController(CajeroService cajeroService) {
        this.cajeroService = cajeroService;
    }

    // MÉTODOS DE AUTENTICACIÓN
    /**
     * Inicia sesión con tarjeta y PIN.
     * @param numeroTarjeta Número de tarjeta ingresado
     * @param pin PIN ingresado
     * @return true si la autenticación fue exitosa
     * @throws CuentaBloqueadaException Si la cuenta está bloqueada
     * @throws PINIncorrectoException Si el PIN es incorrecto
     */
    public boolean iniciarSesion(String numeroTarjeta, String pin) 
            throws CuentaBloqueadaException, PINIncorrectoException {
        // Validar formato de tarjeta
        if (!ValidadorService.validarNumeroTarjeta(numeroTarjeta)) {
            throw new PINIncorrectoException(3);
        }
        // Validar formato de PIN
        if (!ValidadorService.validarPin(pin)) {
            throw new PINIncorrectoException(3);
        }

        return cajeroService.iniciarSesion(numeroTarjeta, pin);
    }

    // Cierra la sesión actual.
    public void cerrarSesion() {
        cajeroService.cerrarSesion();
    }

    /**
     * Verifica si hay una sesión activa.
     * @return true si hay sesión autenticada
     */
    public boolean estaAutenticado() {
        return cajeroService.estaAutenticado();
    }

    // MÉTODOS DE OPERACIONES BANCARIAS
    /**
     * Consulta el saldo de la cuenta actual.
     * @return Saldo disponible
     * @throws SesionNoAutenticadaException Si no hay sesión activa
     */
    public double consultarSaldo() throws SesionNoAutenticadaException {
        return cajeroService.consultarSaldo();
    }

    /**
     * Retira efectivo de la cuenta actual.
     * @param monto Monto a retirar
     * @return Nuevo saldo después del retiro
     * @throws SesionNoAutenticadaException Si no hay sesión activa
     * @throws MontoInvalidoException Si el monto es inválido
     * @throws SaldoInsuficienteException Si no hay fondos suficientes
     */
    public double retirarEfectivo(double monto) 
            throws SesionNoAutenticadaException, MontoInvalidoException, 
                   SaldoInsuficienteException {
        // Validar monto
        if (!ValidadorService.validarMontoRetiro(monto)) {
            throw new MontoInvalidoException(monto, 
                "Máximo de retiro: $" + 
                String.format("%,.2f", ValidadorService.getMontoMaximoRetiro()));
        }

        return cajeroService.retirarEfectivo(monto);
    }

    /**
     * Deposita efectivo a la cuenta actual.
     * @param monto Monto a depositar
     * @return Nuevo saldo después del depósito
     * @throws SesionNoAutenticadaException Si no hay sesión activa
     * @throws MontoInvalidoException Si el monto es inválido
     */
    public double depositarEfectivo(double monto) 
            throws SesionNoAutenticadaException, MontoInvalidoException {
        // Validar monto mínimo de depósito
        if (!ValidadorService.validarMontoDeposito(monto)) {
            throw new MontoInvalidoException(monto, 
                "Mínimo de depósito: $" + 
                String.format("%,.2f", ValidadorService.getMontoMinimoDeposito()));
        }

        return cajeroService.depositarEfectivo(monto);
    }

    /**
     * Transfiere fondos a otra cuenta.
     * @param numeroCuentaDestino Número de cuenta destino
     * @param monto Monto a transferir
     * @return true si la transferencia fue exitosa
     * @throws SesionNoAutenticadaException Si no hay sesión activa
     * @throws MontoInvalidoException Si el monto es inválido
     * @throws SaldoInsuficienteException Si no hay fondos suficientes
     * @throws CuentaNoEncontradaException Si la cuenta destino no existe
     */
    public boolean transferir(String numeroCuentaDestino, double monto) 
            throws SesionNoAutenticadaException, MontoInvalidoException,
                   SaldoInsuficienteException, CuentaNoEncontradaException {
        // Validar cuenta destino
        if (!ValidadorService.validarNumeroCuenta(numeroCuentaDestino)) {
            throw new CuentaNoEncontradaException(numeroCuentaDestino);
        }
        // Validar monto mínimo de transferencia
        if (!ValidadorService.validarMontoTransferencia(monto)) {
            throw new MontoInvalidoException(monto, 
                "Mínimo de transferencia: $" + 
                String.format("%,.2f", ValidadorService.getMontoMinimoTransferencia()));
        }
        return cajeroService.transferir(numeroCuentaDestino, monto);
    }

    /**
     * Cambia el PIN de la cuenta actual.
     * @param pinActual PIN actual para verificación
     * @param nuevoPin Nuevo PIN a establecer
     * @return true si el cambio fue exitoso
     * @throws SesionNoAutenticadaException Si no hay sesión activa
     * @throws PINIncorrectoException Si el PIN actual es incorrecto o el nuevo es inválido
     */
    public boolean cambiarPin(String pinActual, String nuevoPin) 
            throws SesionNoAutenticadaException, PINIncorrectoException {
        // Validar formato de PINs
        if (!ValidadorService.validarPin(pinActual)) {
            throw new PINIncorrectoException(cajeroService.getCuentaActual() != null ? 
                cajeroService.getCuentaActual().getSaldo() > 0 ? 3 : 3 : 3);
        }

        if (!ValidadorService.validarPin(nuevoPin)) {
            throw new PINIncorrectoException(3);
        }
        // Verificar que los PINs sean diferentes
        if (pinActual.equals(nuevoPin)) {
            throw new PINIncorrectoException(3);
        }
        return cajeroService.cambiarPin(pinActual, nuevoPin);
    }

    // MÉTODOS DE HISTORIAL
    /**
     * Obtiene el historial completo de transacciones.
     * @return Lista de transacciones
     * @throws SesionNoAutenticadaException Si no hay sesión activa
     */
    public List<Transaccion> obtenerHistorial() throws SesionNoAutenticadaException {
        return cajeroService.obtenerHistorial();
    }

    /**
     * Obtiene las últimas N transacciones.
     * @param cantidad Número de transacciones a obtener
     * @return Lista de transacciones recientes
     * @throws SesionNoAutenticadaException Si no hay sesión activa
     */
    public List<Transaccion> obtenerHistorialReciente(int cantidad) 
            throws SesionNoAutenticadaException {
        return cajeroService.obtenerHistorialReciente(cantidad);
    }

    // MÉTODOS DE INFORMACIÓN
    /**
     * Obtiene la información de la cuenta actual.
     * @return Cuenta actual o null
     */
    public CuentaBancaria getCuentaActual() {
        return cajeroService.getCuentaActual();
    }

    /**
     * Obtiene el límite máximo de retiro.
     * @return Monto máximo de retiro
     */
    public double getLimiteRetiro() {
        return ValidadorService.getMontoMaximoRetiro();
    }

    /**
     * Obtiene el límite mínimo de depósito.
     * @return Monto mínimo de depósito
     */
    public double getLimiteDeposito() {
        return ValidadorService.getMontoMinimoDeposito();
    }

    /**
     * Obtiene el límite mínimo de transferencia.
     * @return Monto mínimo de transferencia
     */
    public double getLimiteTransferencia() {
        return ValidadorService.getMontoMinimoTransferencia();
    }
}
