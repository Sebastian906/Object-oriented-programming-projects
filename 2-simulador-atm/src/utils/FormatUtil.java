package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Clase de utilidad para formateo de datos.
 * Proporciona métodos estáticos para formatear montos, fechas,
 * números de cuenta y otros datos del sistema ATM.
 * Principios POO aplicados:
 * - Abstracción: Centraliza operaciones de formateo reutilizables
 * - Encapsulamiento: Métodos estáticos sin estado
 * Nota: Esta clase es estática porque no mantiene estado.
 * Todos sus métodos son puros (input -> output, sin efectos secundarios).
 */
public final class FormatUtil {
    // CONSTANTES
    // Formato de moneda colombiana
    private static final String FORMATO_MONEDA = "$%,.2f";
    
    // Formato de fecha completa
    private static final String FORMATO_FECHA_COMPLETA = "dd/MM/yyyy HH:mm:ss";
    
    // Formato de fecha corta
    private static final String FORMATO_FECHA_CORTA = "dd/MM/yyyy";
    
    // Formato de hora
    private static final String FORMATO_HORA = "HH:mm:ss";
    
    // Formato de número de tarjeta (con guiones)
    @SuppressWarnings("unused")
    private static final String FORMATO_TARJETA = "****-****-****-****";
    
    // Formato de número de cuenta enmascarado
    @SuppressWarnings("unused")
    private static final String FORMATO_CUENTA_ENMASCARADA = "****%s";
    
    // CONSTRUCTOR PRIVADO
    /**
     * Constructor privado para evitar instanciación.
     * Esta clase solo contiene métodos estáticos.
     */
    private FormatUtil() {
        throw new UnsupportedOperationException("Esta clase no debe ser instanciada");
    }

    // MÉTODOS DE FORMATEO DE MONEDA
    /**
     * Formatea un monto como moneda colombiana.
     * @param monto Monto a formatear
     * @return Cadena formateada (ej: "$1,234,567.89")
     */
    public static String formatearMoneda(double monto) {
        return String.format(FORMATO_MONEDA, monto);
    }

    /**
     * Formatea un monto como moneda con símbolo personalizado.
     * @param monto Monto a formatear
     * @param simbolo Símbolo de moneda
     * @return Cadena formateada con símbolo
     */
    public static String formatearMoneda(double monto, String simbolo) {
        return simbolo + String.format("%,.2f", monto);
    }

    /**
     * Formatea un monto para mostrar sin decimales.
     * @param monto Monto a formatear
     * @return Cadena formateada sin decimales
     */
    public static String formatearMonedaEntera(double monto) {
        return String.format("$%,.0f", monto);
    }

    // MÉTODOS DE FORMATEO DE FECHA
    /**
     * Formatea una fecha y hora completa.
     * @param fecha Fecha a formatear
     * @return Cadena formateada (ej: "25/12/2024 14:30:45")
     */
    public static String formatearFechaCompleta(LocalDateTime fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATO_FECHA_COMPLETA);
        return fecha.format(formatter);
    }

    /**
     * Formatea solo la fecha (sin hora).
     * @param fecha Fecha a formatear
     * @return Cadena formateada (ej: "25/12/2024")
     */
    public static String formatearFechaCorta(LocalDateTime fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATO_FECHA_CORTA);
        return fecha.format(formatter);
    }

    /**
     * Formatea solo la hora (sin fecha).
     * @param fecha Fecha a formatear
     * @return Cadena formateada (ej: "14:30:45")
     */
    public static String formatearHora(LocalDateTime fecha) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATO_HORA);
        return fecha.format(formatter);
    }

    // MÉTODOS DE FORMATEO DE CUENTAS Y TARJETAS
    /**
     * Enmascara un número de tarjeta mostrando solo los últimos 4 dígitos.
     * @param numeroTarjeta Número de tarjeta completo
     * @return Tarjeta enmascarada (ej: "****-****-****-1234")
     */
    public static String enmascararTarjeta(String numeroTarjeta) {
        if (numeroTarjeta == null || numeroTarjeta.length() < 4) {
            return "****";
        }
        String ultimos4 = numeroTarjeta.substring(numeroTarjeta.length() - 4);
        return "****-****-****-" + ultimos4;
    }

    /**
     * Enmascara un número de cuenta mostrando solo los últimos dígitos.
     * @param numeroCuenta Número de cuenta completo
     * @param digitosVisibles Número de dígitos a mostrar
     * @return Cuenta enmascarada
     */
    public static String enmascararCuenta(String numeroCuenta, int digitosVisibles) {
        if (numeroCuenta == null || numeroCuenta.length() <= digitosVisibles) {
            return numeroCuenta;
        }
        String sufijo = numeroCuenta.substring(numeroCuenta.length() - digitosVisibles);
        String prefijo = "*".repeat(numeroCuenta.length() - digitosVisibles);
        return prefijo + sufijo;
    }

    /**
     * Enmascara un número de cuenta mostrando solo los últimos 4 dígitos.
     * @param numeroCuenta Número de cuenta completo
     * @return Cuenta enmascarada
     */
    public static String enmascararCuenta(String numeroCuenta) {
        return enmascararCuenta(numeroCuenta, 4);
    }

    // MÉTODOS DE FORMATEO DE NÚMEROS
    /**
     * Formatea un número con separadores de miles.
     * @param numero Número a formatear
     * @return Cadena formateada (ej: "1,234,567")
     */
    public static String formatearNumero(long numero) {
        return String.format("%,d", numero);
    }

    /**
     * Formatea un número con separadores de miles y decimales.
     * @param numero Número a formatear
     * @param decimales Cantidad de decimales
     * @return Cadena formateada
     */
    public static String formatearNumero(double numero, int decimales) {
        String formato = "%,." + decimales + "f";
        return String.format(formato, numero);
    }

    // MÉTODOS DE FORMATEO DE TEXTO
    /**
     * Convierte la primera letra de cada palabra a mayúscula.
     * @param texto Texto a formatear
     * @return Texto con formato de título
     */
    public static String formatearTitulo(String texto) {
        if (texto == null || texto.isEmpty()) {
            return texto;
        }

        StringBuilder resultado = new StringBuilder();
        boolean nuevaPalabra = true;

        for (char c : texto.toCharArray()) {
            if (Character.isWhitespace(c)) {
                nuevaPalabra = true;
                resultado.append(c);
            } else if (nuevaPalabra) {
                resultado.append(Character.toUpperCase(c));
                nuevaPalabra = false;
            } else {
                resultado.append(Character.toLowerCase(c));
            }
        }        
        return resultado.toString();
    }

    /**
     * Recorta espacios en blanco al inicio y final de un texto.
     * @param texto Texto a recortar
     * @return Texto recortado
     */
    public static String recortar(String texto) {
        if (texto == null) {
            return null;
        }
        return texto.trim();
    }

    /**
     * Verifica si un texto está vacío o es nulo.
     * @param texto Texto a verificar
     * @return true si está vacío o es nulo
     */
    public static boolean estaVacio(String texto) {
        return texto == null || texto.trim().isEmpty();
    }
}
