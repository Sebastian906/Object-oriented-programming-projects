package utils;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Clase utilitaria para entrada/salida por consola.
 * Contiene métodos para leer datos del teclado de forma segura.
 * Conceptos POO aplicados:
 * - Clase utilitaria
 * - Separación de responsabilidades
 * - Manejo seguro de entrada
 */
public final class ConsoleUtil {

    @SuppressWarnings("FieldMayBeFinal")
    private static Scanner scanner = new Scanner(System.in);

    // Constructor privado
    private ConsoleUtil() {
        throw new UnsupportedOperationException("Clase utilitaria no instanciable");
    }

    /**
     * Lee un número entero del teclado.
     * @return Número entero o -1 si hay error
     */
    public static int leerEntero() {
        try {
            String entrada = scanner.nextLine();
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Lee un número entero con mensaje.
     * @param mensaje Mensaje a mostrar
     * @return Número entero
     */
    public static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        return leerEntero();
    }

    /**
     * Lee un número decimal del teclado.
     * @return Número decimal o 0.0 si hay error
     */
    public static double leerDouble() {
        try {
            String entrada = scanner.nextLine();
            return Double.parseDouble(entrada);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /**
     * Lee un número decimal con mensaje.
     * @param mensaje Mensaje a mostrar
     * @return Número decimal
     */
    public static double leerDouble(String mensaje) {
        System.out.print(mensaje);
        return leerDouble();
    }

    /**
     * Lee una fecha del teclado en formato dd/MM/yyyy.
     * @return LocalDate o null si el formato es inválido
     */
    public static LocalDate leerFecha() {
        String fechaStr = scanner.nextLine();
        return FormatUtil.parsearFecha(fechaStr);
    }

    /**
     * Lee una fecha con mensaje.
     * @param mensaje Mensaje a mostrar
     * @return LocalDate
     */
    public static LocalDate leerFecha(String mensaje) {
        System.out.print(mensaje);
        return leerFecha();
    }

    /**
     * Lee una línea de texto del teclado.
     * @return Texto ingresado
     */
    public static String leerTexto() {
        return scanner.nextLine();
    }

    /**
     * Lee una línea de texto con mensaje.
     * @param mensaje Mensaje a mostrar
     * @return Texto ingresado
     */
    public static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    /**
     * Lee una confirmación (s/n).
     * @param mensaje Mensaje a mostrar
     * @return true si responde 's' o 'si'
     */
    public static boolean confirmar(String mensaje) {
        System.out.print(mensaje + " (s/n): ");
        String respuesta = scanner.nextLine().toLowerCase();
        return respuesta.equals("s") || respuesta.equals("si");
    }

    /**
     * Muestra un mensaje de éxito.
     * @param mensaje Mensaje
     */
    public static void mostrarExito(String mensaje) {
        System.out.println("\n✓ " + mensaje);
    }

    /**
     * Muestra un mensaje de error.
     * @param mensaje Mensaje
     */
    public static void mostrarError(String mensaje) {
        System.out.println("\n✗ ERROR: " + mensaje);
    }

    /**
     * Muestra un mensaje informativo.
     * @param mensaje Mensaje
     */
    public static void mostrarMensaje(String mensaje) {
        System.out.println("\n" + mensaje);
    }

    /**
     * Cierra el scanner.
     */
    public static void cerrar() {
        scanner.close();
    }
}
