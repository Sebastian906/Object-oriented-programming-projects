package service;

import models.Transaccion;
import models.TipoTransaccion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio de gestión del historial de transacciones.
 * Almacena y gestiona todas las transacciones realizadas en el ATM,
 * organizadas por cuenta de origen. Mantiene la persistencia en memoria
 * usando HashMap.
 * Principios POO aplicados:
 * - Encapsulamiento: Estado interno protegido
 * - Single Responsibility: Solo gestiona historial
 * - Composición: Contiene colecciones de objetos Transaccion
 */
public class HistorialService {

    // ATRIBUTOS
    /**
     * Mapa que almacena transacciones por cuenta de origen.
     * Key: Número de cuenta origen
     * Value: Lista de transacciones de esa cuenta
     */
    private final Map<String, List<Transaccion>> transacciones;

    // Límite máximo de transacciones a mostrar por página
    private static final int LIMITE_POR_PAGINA = 10;

    // CONSTRUCTOR
    /**
     * Constructor de HistorialService.
     * Inicializa el mapa de transacciones vacío.
     */
    public HistorialService() {
        this.transacciones = new HashMap<>();
    }

    // MÉTODOS PÚBLICOS
    /**
     * Registra una nueva transacción en el historial.
     * @param transaccion Transacción a registrar
     */
    public void registrarTransaccion(Transaccion transaccion) {
        String cuentaOrigen = transaccion.getCuentaOrigen();

        // Obtener o crear la lista para esta cuenta
        transacciones.computeIfAbsent(cuentaOrigen, k -> new ArrayList<>());

        // Agregar la transacción
        transacciones.get(cuentaOrigen).add(transaccion);
    }

    /**
     * Obtiene todas las transacciones de una cuenta.
     * @param cuentaOrigen Número de cuenta
     * @return Lista de transacciones (puede estar vacía)
     */
    public List<Transaccion> obtenerHistorial(String cuentaOrigen) {
        List<Transaccion> historial = transacciones.get(cuentaOrigen);
        if (historial == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(historial); // Retornar copia
    }

    /**
     * Obtiene las últimas N transacciones de una cuenta.
     * @param cuentaOrigen Número de cuenta
     * @param cantidad Cantidad de transacciones a obtener
     * @return Lista de las últimas transacciones
     */
    public List<Transaccion> obtenerHistorialReciente(String cuentaOrigen, int cantidad) {
        List<Transaccion> historial = obtenerHistorial(cuentaOrigen);
        int totalTransacciones = historial.size();
        if (totalTransacciones <= cantidad) {
            return new ArrayList<>(historial);
        }
        // Obtener las últimas N transacciones
        int inicio = totalTransacciones - cantidad;
        return new ArrayList<>(historial.subList(inicio, totalTransacciones));
    }

    /**
     * Obtiene las transacciones de una cuenta filtradas por tipo.
     * @param cuentaOrigen Número de cuenta
     * @param tipo Tipo de transacción a filtrar
     * @return Lista de transacciones del tipo especificado
     */
    public List<Transaccion> obtenerHistorialPorTipo(String cuentaOrigen, 
                                                       TipoTransaccion tipo) {
        List<Transaccion> historial = obtenerHistorial(cuentaOrigen);
        List<Transaccion> filtradas = new ArrayList<>();

        for (Transaccion t : historial) {
            if (t.getTipo() == tipo) {
                filtradas.add(t);
            }
        }
        return filtradas;
    }

    /**
     * Limpia todo el historial de una cuenta.
     * @param cuentaOrigen Número de cuenta a limpiar
     */
    public void limpiarHistorial(String cuentaOrigen) {
        transacciones.remove(cuentaOrigen);
    }

    // Limpia todo el historial de todas las cuentas.
    public void limpiarTodo() {
        transacciones.clear();
    }

    /**
     * Cuenta el número total de transacciones de una cuenta.
     * @param cuentaOrigen Número de cuenta
     * @return Número total de transacciones
     */
    public int contarTransacciones(String cuentaOrigen) {
        List<Transaccion> historial = transacciones.get(cuentaOrigen);
        return historial != null ? historial.size() : 0;
    }

    /**
     * Cuenta el número total de transacciones en el sistema.
     * @return Total de transacciones
     */
    public int contarTotalTransacciones() {
        int total = 0;
        for (List<Transaccion> lista : transacciones.values()) {
            total += lista.size();
        }
        return total;
    }

    /**
     * Verifica si una cuenta tiene transacciones.
     * @param cuentaOrigen Número de cuenta
     * @return true si tiene transacciones, false en caso contrario
     */
    public boolean tieneTransacciones(String cuentaOrigen) {
        List<Transaccion> historial = transacciones.get(cuentaOrigen);
        return historial != null && !historial.isEmpty();
    }

    /**
     * Obtiene el límite de transacciones por página.
     * @return Límite por página
     */
    public int getLimitePorPagina() {
        return LIMITE_POR_PAGINA;
    }

    // REPRESENTACIÓN EN TEXTO
    /**
     * Retorna la representación en texto del historial.
     * @return Cadena con resumen del historial
     */
    @Override
    public String toString() {
        return """
               HistorialService {
                 Cuentas con historial: """ + transacciones.size() +
               "\n  Total transacciones: " + contarTotalTransacciones() +
               "\n}";
    }
}
