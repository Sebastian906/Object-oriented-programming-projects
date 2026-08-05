package service;

import models.CuentaBancaria;
import models.Transaccion;
import models.TipoTransaccion;
import models.UsuarioSesion;

import exceptions.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio principal de lógica de negocio del ATM.
 * Coordina todas las operaciones del cajero automático:
 * autenticación, consultas, retiros, depósitos, transferencias
 * y cambios de PIN.
 * Principios POO aplicados:
 * - Single Responsibility: Coordina operaciones del ATM
 * - Composición: Contiene CuentaBancaria, UsuarioSesion, HistorialService
 * - Encapsulamiento: Estado interno protegido
 */
public class CajeroService {
    // ATRIBUTOS
    // Colección de cuentas bancarias (key: número de tarjeta)
    private final Map<String, CuentaBancaria> cuentas;

    // Servicio de sesión de usuario
    private final UsuarioSesion sesion;

    // Servicio de historial de transacciones
    private final HistorialService historialService;

    // Servicio de generación de IDs
    private final GeneradorIdService generadorId;

    // Número máximo de intentos de PIN
    private static final int MAX_INTENTOS_PIN = 3;

    // CONSTRUCTOR
    /**
     * Constructor de CajeroService.
     * Inicializa todos los servicios y carga cuentas de prueba.
     */
    public CajeroService() {
        this.cuentas = new HashMap<>();
        this.sesion = new UsuarioSesion();
        this.historialService = new HistorialService();
        this.generadorId = new GeneradorIdService();
        // Cargar cuentas de prueba
        cargarCuentasPrueba();
    }

    // MÉTODOS DE AUTENTICACIÓN
    /**
     * Inicia sesión con tarjeta y PIN.
     * @param numeroTarjeta Número de tarjeta
     * @param pin PIN ingresado
     * @return true si la autenticación fue exitosa
     * @throws CuentaBloqueadaException Si la cuenta está bloqueada
     * @throws PINIncorrectoException Si el PIN es incorrecto
     */
    public boolean iniciarSesion(String numeroTarjeta, String pin) 
            throws CuentaBloqueadaException, PINIncorrectoException {
        // Buscar cuenta por tarjeta
        CuentaBancaria cuenta = cuentas.get(numeroTarjeta);

        if (cuenta == null) {
            throw new PINIncorrectoException(MAX_INTENTOS_PIN);
        }
        // Verificar si la cuenta está bloqueada
        if (sesion.estaBloqueada()) {
            throw new CuentaBloqueadaException(cuenta.getNumeroCuenta());
        }        
        // Intentar autenticar
        if (sesion.autenticar(cuenta, pin)) {
            return true;
        } else {
            int intentosRestantes = sesion.getIntentosRestantes();

            if (sesion.estaBloqueada()) {
                throw new CuentaBloqueadaException(cuenta.getNumeroCuenta());
            }
            throw new PINIncorrectoException(intentosRestantes);
        }
    }

    // Cierra la sesión actual.
    public void cerrarSesion() {
        sesion.cerrarSesion();
    }

    /**
     * Verifica si hay una sesión activa.
     * @return true si hay sesión autenticada
     */
    public boolean estaAutenticado() {
        return sesion.estaAutenticada();
    }

    // MÉTODOS DE OPERACIONES BANCARIAS
    /**
     * Consulta el saldo de la cuenta actual.
     * @return Saldo disponible
     * @throws SesionNoAutenticadaException Si no hay sesión activa
     */
    public double consultarSaldo() throws SesionNoAutenticadaException {
        verificarSesionActiva();

        CuentaBancaria cuenta = sesion.getCuentaActual();
        // Registrar transacción de consulta
        registrarTransaccion(TipoTransaccion.CONSULTA_SALDO, 0, 
                           cuenta.getSaldo(), cuenta.getSaldo(),
                           "Consulta de saldo", cuenta.getNumeroCuenta(), null);

        return cuenta.getSaldo();
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
        verificarSesionActiva();
        verificarMontoPositivo(monto);

        CuentaBancaria cuenta = sesion.getCuentaActual();
        double saldoAnterior = cuenta.getSaldo();
        // Verificar fondos suficientes
        if (saldoAnterior < monto) {
            throw new SaldoInsuficienteException(saldoAnterior, monto);
        }
        // Realizar el débito
        if (cuenta.debitar(monto)) {
            double saldoPosterior = cuenta.getSaldo();
            // Registrar transacción
            registrarTransaccion(TipoTransaccion.RETIRO, monto,
                               saldoAnterior, saldoPosterior,
                               "Retiro de efectivo", cuenta.getNumeroCuenta(), null);
            return saldoPosterior;
        }
        throw new SaldoInsuficienteException(saldoAnterior, monto);
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

        verificarSesionActiva();
        verificarMontoPositivo(monto);

        CuentaBancaria cuenta = sesion.getCuentaActual();
        double saldoAnterior = cuenta.getSaldo();
        // Realizar el crédito
        cuenta.acreditar(monto);
        double saldoPosterior = cuenta.getSaldo();
        // Registrar transacción
        registrarTransaccion(TipoTransaccion.DEPOSITO, monto,
                           saldoAnterior, saldoPosterior,
                           "Depósito de efectivo", cuenta.getNumeroCuenta(), null);
        return saldoPosterior;
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

        verificarSesionActiva();
        verificarMontoPositivo(monto);

        CuentaBancaria cuentaOrigen = sesion.getCuentaActual();        
        // Buscar cuenta destino
        CuentaBancaria cuentaDestino = null;
        for (CuentaBancaria c : cuentas.values()) {
            if (c.getNumeroCuenta().equals(numeroCuentaDestino)) {
                cuentaDestino = c;
                break;
            }
        }

        if (cuentaDestino == null) {
            throw new CuentaNoEncontradaException(numeroCuentaDestino);
        }
        // Verificar que no sea transferencia a la misma cuenta
        if (cuentaOrigen.getNumeroCuenta().equals(numeroCuentaDestino)) {
            throw new MontoInvalidoException(monto, 
                "No puede transferir a la misma cuenta");
        }
        // Verificar fondos suficientes
        double saldoAnteriorOrigen = cuentaOrigen.getSaldo();
        if (saldoAnteriorOrigen < monto) {
            throw new SaldoInsuficienteException(saldoAnteriorOrigen, monto);
        }
        // Realizar transferencia
        double saldoAnteriorDestino = cuentaDestino.getSaldo();

        cuentaOrigen.debitar(monto);
        cuentaDestino.acreditar(monto);

        double saldoPosteriorOrigen = cuentaOrigen.getSaldo();
        double saldoPosteriorDestino = cuentaDestino.getSaldo();
        // Registrar transacciones
        registrarTransaccion(TipoTransaccion.TRANSFERENCIA, monto,
                           saldoAnteriorOrigen, saldoPosteriorOrigen,
                           "Transferencia enviada a " + numeroCuentaDestino,
                           cuentaOrigen.getNumeroCuenta(), numeroCuentaDestino);

        registrarTransaccion(TipoTransaccion.TRANSFERENCIA, monto,
                           saldoAnteriorDestino, saldoPosteriorDestino,
                           "Transferencia recibida de " + cuentaOrigen.getNumeroCuenta(),
                           numeroCuentaDestino, cuentaOrigen.getNumeroCuenta());

        return true;
    }

    /**
     * Cambia el PIN de la cuenta actual.
     * @param pinActual PIN actual para verificación
     * @param nuevoPin Nuevo PIN a establecer
     * @return true si el cambio fue exitoso
     * @throws SesionNoAutenticadaException Si no hay sesión activa
     * @throws PINIncorrectoException Si el PIN actual es incorrecto
     */
    public boolean cambiarPin(String pinActual, String nuevoPin) 
            throws SesionNoAutenticadaException, PINIncorrectoException {

        verificarSesionActiva();

        CuentaBancaria cuenta = sesion.getCuentaActual();
        // Verificar PIN actual
        if (!cuenta.validarPin(pinActual)) {
            throw new PINIncorrectoException(sesion.getIntentosRestantes());
        }
        // Validar nuevo PIN
        if (!ValidadorService.validarPin(nuevoPin)) {
            throw new PINIncorrectoException(sesion.getIntentosRestantes());
        }
        // Cambiar PIN
        cuenta.cambiarPin(nuevoPin);
        return true;
    }

    // MÉTODOS DE HISTORIAL
    /**
     * Obtiene el historial de transacciones de la cuenta actual.
     * @return Lista de transacciones
     * @throws SesionNoAutenticadaException Si no hay sesión activa
     */
    public List<Transaccion> obtenerHistorial() throws SesionNoAutenticadaException {
        verificarSesionActiva();

        CuentaBancaria cuenta = sesion.getCuentaActual();
        return historialService.obtenerHistorial(cuenta.getNumeroCuenta());
    }

    /**
     * Obtiene las últimas N transacciones de la cuenta actual.
     * @param cantidad Número de transacciones a obtener
     * @return Lista de transacciones recientes
     * @throws SesionNoAutenticadaException Si no hay sesión activa
     */
    public List<Transaccion> obtenerHistorialReciente(int cantidad) 
            throws SesionNoAutenticadaException {

        verificarSesionActiva();

        CuentaBancaria cuenta = sesion.getCuentaActual();
        return historialService.obtenerHistorialReciente(
            cuenta.getNumeroCuenta(), cantidad);
    }

    // MÉTODOS DE INFORMACIÓN
    /**
     * Obtiene la información de la cuenta actual.
     * @return Cuenta actual o null
     */
    public CuentaBancaria getCuentaActual() {
        return sesion.getCuentaActual();
    }

    /**
     * Obtiene el historial de transacciones.
     * @return Servicio de historial
     */
    public HistorialService getHistorialService() {
        return historialService;
    }

    /**
     * Obtiene el contador de IDs generados.
     * @return Cantidad de IDs generados
     */
    public int getCantidadTransaccionesRegistradas() {
        return generadorId.getCantidadGenerada();
    }

    // MÉTODOS PRIVADOS
    /**
     * Verifica que hay una sesión activa.
     * @throws SesionNoAutenticadaException Si no hay sesión
     */
    private void verificarSesionActiva() throws SesionNoAutenticadaException {
        if (!sesion.estaAutenticada()) {
            throw new SesionNoAutenticadaException();
        }
    }

    /**
     * Verifica que el monto sea positivo.
     * @param monto Monto a verificar
     * @throws MontoInvalidoException Si el monto no es positivo
     */
    private void verificarMontoPositivo(double monto) throws MontoInvalidoException {
        if (!ValidadorService.validarMontoPositivo(monto)) {
            throw new MontoInvalidoException(monto);
        }
    }

    /**
     * Registra una transacción en el historial.
     * @param tipo Tipo de transacción
     * @param monto Monto operado
     * @param saldoAnterior Saldo antes de la operación
     * @param saldoPosterior Saldo después de la operación
     * @param descripcion Descripción de la operación
     * @param cuentaOrigen Cuenta origen
     * @param cuentaDestino Cuenta destino (null si no aplica)
     */
    private void registrarTransaccion(TipoTransaccion tipo, double monto,
                                      double saldoAnterior, double saldoPosterior,
                                      String descripcion, String cuentaOrigen,
                                      String cuentaDestino) {
        String id = generadorId.generarIdTransaccion();

        Transaccion transaccion = new Transaccion(
            id, tipo, monto, LocalDateTime.now(),
            saldoAnterior, saldoPosterior, descripcion,
            cuentaOrigen, cuentaDestino
        );
        historialService.registrarTransaccion(transaccion);
    }

    /**
     * Carga cuentas de prueba en el sistema.
     * Estas cuentas permiten probar el sistema sin crear datos manualmente.
     */
    private void cargarCuentasPrueba() {
        // Cuenta 1: Juan Pérez
        CuentaBancaria cuenta1 = new CuentaBancaria(
            "1001", "Juan Pérez", "1234", 500000, "1234567890123456"
        );
        cuentas.put(cuenta1.getNumeroTarjeta(), cuenta1);

        // Cuenta 2: María García
        CuentaBancaria cuenta2 = new CuentaBancaria(
            "1002", "María García", "5678", 1200000, "2345678901234567"
        );
        cuentas.put(cuenta2.getNumeroTarjeta(), cuenta2);

        // Cuenta 3: Carlos López
        CuentaBancaria cuenta3 = new CuentaBancaria(
            "1003", "Carlos López", "9012", 75000, "3456789012345678"
        );
        cuentas.put(cuenta3.getNumeroTarjeta(), cuenta3);

        // Cuenta 4: Ana Martínez (saldo bajo para probar errores)
        CuentaBancaria cuenta4 = new CuentaBancaria(
            "1004", "Ana Martínez", "3456", 25000, "4567890123456789"
        );
        cuentas.put(cuenta4.getNumeroTarjeta(), cuenta4);
    }
}
