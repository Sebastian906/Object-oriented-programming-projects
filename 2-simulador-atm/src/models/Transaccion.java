package models;

import java.time.LocalDateTime;
import utils.FormatUtil;

/**
 * Clase que representa una transacción realizada en el ATM.
 * Cada transacción almacena información sobre la operación financiera,
 * incluyendo tipo, monto, fecha, saldos anterior/posterior y cuentas involucradas.
 * Principios POO aplicados:
 * - Abstracción: Representa una operación financiera del mundo real
 * - Encapsulamiento: Atributos privados con solo getters (inmutabilidad parcial)
 */
public class Transaccion {
    // ATRIBUTOS PRIVADOS (Encapsulamiento)

    // Identificador único de la transacción
    private final String id;

    // Tipo de transacción (consultar enum TipoTransaccion)
    private final TipoTransaccion tipo;

    // Monto de la operación
    private final double monto;

    // Fecha y hora de la transacción
    private final LocalDateTime fechaHora;

    // Saldo antes de la transacción
    private final double saldoAnterior;

    // Saldo después de la transacción
    private final double saldoPosterior;

    // Descripción de la operación realizada
    private final String descripcion;

    // Número de cuenta origen
    private final String cuentaOrigen;

    // Número de cuenta destino (null si no es transferencia)
    private final String cuentaDestino;

    // CONSTRUCTOR

    /**
     * Constructor principal de Transaccion.
     * @param id Identificador único
     * @param tipo Tipo de transacción
     * @param monto Monto de la operación
     * @param fechaHora Fecha y hora de la transacción
     * @param saldoAnterior Saldo antes de la operación
     * @param saldoPosterior Saldo después de la operación
     * @param descripcion Descripción de la operación
     * @param cuentaOrigen Cuenta origen
     * @param cuentaDestino Cuenta destino (null si no aplica)
     */
    public Transaccion(String id, TipoTransaccion tipo, double monto,
                       LocalDateTime fechaHora, double saldoAnterior,
                       double saldoPosterior, String descripcion,
                       String cuentaOrigen, String cuentaDestino) {
        this.id = id;
        this.tipo = tipo;
        this.monto = monto;
        this.fechaHora = fechaHora;
        this.saldoAnterior = saldoAnterior;
        this.saldoPosterior = saldoPosterior;
        this.descripcion = descripcion;
        this.cuentaOrigen = cuentaOrigen;
        this.cuentaDestino = cuentaDestino;
    }

    // GETTERS (Solo lectura - Inmutabilidad)

    /**
     * Obtiene el identificador de la transacción.
     * @return ID de la transacción
     */
    public String getId() {
        return id;
    }

    /**
     * Obtiene el tipo de transacción.
     * @return Tipo de transacción
     */
    public TipoTransaccion getTipo() {
        return tipo;
    }

    /**
     * Obtiene el monto de la transacción.
     * @return Monto operado
     */
    public double getMonto() {
        return monto;
    }

    /**
     * Obtiene la fecha y hora de la transacción.
     * @return Fecha y hora
     */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /**
     * Obtiene el saldo antes de la transacción.
     * @return Saldo anterior
     */
    public double getSaldoAnterior() {
        return saldoAnterior;
    }

    /**
     * Obtiene el saldo después de la transacción.
     * @return Saldo posterior
     */
    public double getSaldoPosterior() {
        return saldoPosterior;
    }

    /**
     * Obtiene la descripción de la transacción.
     * @return Descripción
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Obtiene la cuenta origen.
     * @return Número de cuenta origen
     */
    public String getCuentaOrigen() {
        return cuentaOrigen;
    }

    /**
     * Obtiene la cuenta destino.
     * @return Número de cuenta destino o null si no aplica
     */
    public String getCuentaDestino() {
        return cuentaDestino;
    }

    // MÉTODOS DE UTILIDAD
    /**
     * Verifica si la transacción es una transferencia.
     * @return true si es transferencia, false en caso contrario
     */
    public boolean esTransferencia() {
        return tipo == TipoTransaccion.TRANSFERENCIA;
    }

    /**
     * Obtiene la fecha formateada de la transacción.
     * @return Fecha formateada como "dd/MM/yyyy HH:mm:ss"
     */
    public String getFechaFormateada() {
        return FormatUtil.formatearFechaCompleta(fechaHora);
    }


    // REPRESENTACIÓN EN TEXTO
    /**
     * Retorna la representación en texto de la transacción.
     * @return Cadena con información de la transacción
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Transacción {")
          .append("\n  ID: ").append(id)
          .append("\n  Tipo: ").append(tipo.getDescripcion())
          .append("\n  Monto: $").append(FormatUtil.formatearMoneda(monto))
          .append("\n  Fecha: ").append(getFechaFormateada())
          .append("\n  Saldo Anterior: $").append(FormatUtil.formatearMoneda(saldoAnterior))
          .append("\n  Saldo Posterior: $").append(FormatUtil.formatearMoneda(saldoPosterior));
        
        if (cuentaDestino != null) {
            sb.append("\n  Cuenta Destino: ").append(cuentaDestino);
        }
        
        sb.append("\n}");
        return sb.toString();
    }
}
