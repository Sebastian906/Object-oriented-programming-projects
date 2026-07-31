package utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Clase utilitaria para formateo y conversión de datos.
 * Contiene métodos estáticos reutilizables.
 * Conceptos POO aplicados:
 * - Clase utilitaria (métodos estáticos)
 * - Single Responsibility Principle
 * - Reutilización de código
 */
public final class FormatUtil {

    // Formateador de fechas compartido
    private static final DateTimeFormatter FORMATEADOR_FECHA = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Constructor privado para evitar instanciación
    private FormatUtil() {
        throw new UnsupportedOperationException("Clase utilitaria no instanciable");
    }

    /**
     * Calcula los días restantes entre dos fechas.
     * @param fechaInicio   Fecha de inicio
     * @param fechaFin      Fecha límite
     * @return Días restantes (negativo si ya pasó)
     */
    public static int calcularDiasRestantes(LocalDate fechaInicio, LocalDate fechaFin) {
        return (int) ChronoUnit.DAYS.between(fechaInicio, fechaFin);
    }

    /**
     * Calcula los días restantes desde hoy hasta una fecha límite.
     * @param fechaLimite Fecha límite
     * @return Días restantes
     */
    public static int calcularDiasRestantes(LocalDate fechaLimite) {
        return calcularDiasRestantes(LocalDate.now(), fechaLimite);
    }

    /**
     * Verifica si una fecha ya pasó.
     * @param fecha Fecha a verificar
     * @return true si la fecha es anterior a hoy
     */
    public static boolean estaVencida(LocalDate fecha) {
        return LocalDate.now().isAfter(fecha);
    }

    /**
     * Formatea una fecha en formato dd/MM/yyyy.
     * @param fecha Fecha a formatear
     * @return String con la fecha formateada
     */
    public static String formatearFecha(LocalDate fecha) {
        if (fecha == null) {
            return "N/A";
        }
        return fecha.format(FORMATEADOR_FECHA);
    }

    /**
     * Parsea una fecha desde un string en formato dd/MM/yyyy.
     * @param fechaStr String con la fecha
     * @return LocalDate o null si el formato es inválido
     */
    public static LocalDate parsearFecha(String fechaStr) {
        try {
            return LocalDate.parse(fechaStr, FORMATEADOR_FECHA);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Formatea un número con separadores de miles.
     * @param numero Número a formatear
     * @return String con formato
     */
    public static String formatearNumero(int numero) {
        return String.format("%,d", numero);
    }

    /**
     * Trunca un string a una longitud máxima y agrega "..." si es necesario.
     * @param texto      Texto a truncar
     * @param maxLength  Longitud máxima
     * @return Texto truncado
     */
    public static String truncarTexto(String texto, int maxLength) {
        if (texto == null) {
            return "";
        }
        if (texto.length() <= maxLength) {
            return texto;
        }
        return texto.substring(0, maxLength - 3) + "...";
    }

    /**
     * Centra un texto dentro de un ancho dado.
     * @param texto  Texto a centrar
     * @param ancho  Ancho total
     * @return Texto centrado
     */
    public static String centrarTexto(String texto, int ancho) {
        if (texto == null) {
            texto = "";
        }
        if (texto.length() >= ancho) {
            return texto;
        }
        int espaciosIzq = (ancho - texto.length()) / 2;
        int espaciosDer = ancho - texto.length() - espaciosIzq;
        return " ".repeat(espaciosIzq) + texto + " ".repeat(espaciosDer);
    }

    /**
     * Genera una línea separadora.
     * @param ancho    Ancho de la línea
     * @param caracter Carácter para la línea
     * @return Línea separadora
     */
    public static String generarLinea(int ancho, char caracter) {
        return String.valueOf(caracter).repeat(ancho);
    }
}
