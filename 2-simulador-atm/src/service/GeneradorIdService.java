package service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Servicio para la generación de identificadores únicos.
 * Genera IDs secuenciales para las transacciones del sistema.
 * Utiliza AtomicInteger para garantizar thread-safety en caso
 * de implementación multi-hilo futura.
 * Principios POO aplicados:
 * - Encapsulamiento: Lógica de generación oculta al exterior
 * - Single Responsibility: Solo genera IDs
 */
public class GeneradorIdService {
    // ATRIBUTOS
    // Contador secuencial para generar IDs
    private final AtomicInteger contador;

    // Prefijo para los IDs de transacción
    private static final String PREFIJO_TRANSACCION = "T";

    // Longitud mínima del ID (sin prefijo)
    private static final int LONGITUD_ID = 4;

    // CONSTRUCTOR
    /**
     * Constructor de GeneradorIdService.
     * Inicializa el contador en 0.
     */
    public GeneradorIdService() {
        this.contador = new AtomicInteger(0);
    }

    // MÉTODOS PÚBLICOS
    /**
     * Genera un nuevo ID único para transacción.
     * Formato: T0001, T0002, T0003, etc.
     * @return ID único generado
     */
    public String generarIdTransaccion() {
        int numero = contador.incrementAndGet();
        return PREFIJO_TRANSACCION + String.format("%0" + LONGITUD_ID + "d", numero);
    }

    /**
     * Obtiene el siguiente ID sin incrementar el contador.
     * Útil para preview.
     * @return Siguiente ID disponible
     */
    public String peekSiguienteId() {
        int siguiente = contador.get() + 1;
        return PREFIJO_TRANSACCION + String.format("%0" + LONGITUD_ID + "d", siguiente);
    }

    /**
     * Obtiene el último ID generado.
     * @return Último ID o null si no se ha generado ninguno
     */
    public String getUltimoIdGenerado() {
        int actual = contador.get();
        if (actual == 0) {
            return null;
        }
        return PREFIJO_TRANSACCION + String.format("%0" + LONGITUD_ID + "d", actual);
    }

    /**
     * Reinicia el contador a cero.
     * PRECAUCIÓN: Usar con precaución - solo para pruebas.
     */
    public void reiniciar() {
        contador.set(0);
    }

    /**
     * Obtiene el contador actual.
     * @return Número de IDs generados
     */
    public int getCantidadGenerada() {
        return contador.get();
    }
}
