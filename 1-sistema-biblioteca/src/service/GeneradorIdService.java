package service;

/**
 * Servicio de generación de IDs únicos.
 * Genera IDs secuenciales para usuarios y préstamos.
 * Conceptos POO aplicados:
 * - Clase utilitaria
 * - Patrón Singleton (acceso global a contadores)
 * - Separación de responsabilidades
 */
public final class GeneradorIdService {

    // Contadores estáticos (compartidos entre instancias)
    private static int contadorUsuarios = 0;
    private static int contadorPrestamos = 0;

    // Constructor privado para evitar instanciación
    private GeneradorIdService() {
        throw new UnsupportedOperationException("Esta es una clase utilitaria y no puede ser instanciada.");
    }

    /**
     * Genera el siguiente ID de usuario.
     * Formato: U1, U2, U3, ...
     * @return ID de usuario único
     */
    public static String generarIdUsuario() {
        contadorUsuarios++;
        return "U" + contadorUsuarios;
    }

    /**
     * Genera el siguiente ID de préstamo.
     * Formato: P1, P2, P3, ...
     * @return ID de préstamo único
     */
    public static String generarIdPrestamo() {
        contadorPrestamos++;
        return "P" + contadorPrestamos;
    }

    /**
     * Obtiene el último ID de usuario generado.
     * @return Número del último ID generado
     */
    public static int getUltimoIdUsuario() {
        return contadorUsuarios;
    }

    /**
     * Obtiene el último ID de préstamo generado.
     * @return Número del último ID generado
     */
    public static int getUltimoIdPrestamo() {
        return contadorPrestamos;
    }

    /**
     * Reinicia los contadores (útil para testing).
     */
    public static void reiniciar() {
        contadorUsuarios = 0;
        contadorPrestamos = 0;
    }
}
