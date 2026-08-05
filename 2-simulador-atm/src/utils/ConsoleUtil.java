package utils;

import java.util.Scanner;

/**
 * Clase de utilidad para entrada/salida por consola.
 * Proporciona métodos estáticos para leer datos del usuario
 * y mostrar información formateada en la consola.
 * Principios POO aplicados:
 * - Abstracción: Centraliza operaciones de consola reutilizables
 * - Encapsulamiento: Método estático con Scanner compartido
 * Nota: Esta clase usa un Scanner estático para evitar
 * problemas con múltiples instancias de Scanner en System.in.
 */
public final class ConsoleUtil {
    // ATRIBUTOS
    // Scanner compartido para entrada de datos
    private static final Scanner scanner = new Scanner(System.in);

    // CONSTANTES DE COLORES (ANSI)
    // Código ANSI para color rojo
    private static final String ANSI_ROJO = "\u001B[31m";

    // Código ANSI para color verde
    private static final String ANSI_VERDE = "\u001B[32m";

    // Código ANSI para color amarillo
    private static final String ANSI_AMARILLO = "\u001B[33m";

    // Código ANSI para color azul
    private static final String ANSI_AZUL = "\u001B[34m";

    // Código ANSI para resetear color
    private static final String ANSI_RESET = "\u001B[0m";

    // CONSTRUCTOR PRIVADO    
    /**
     * Constructor privado para evitar instanciación.
     * Esta clase solo contiene métodos estáticos.
     */
    private ConsoleUtil() {
        throw new UnsupportedOperationException("Esta clase no debe ser instanciada");
    }

    // MÉTODOS DE ENTRADA
    /**
     * Lee una línea de texto del usuario.
     * @param mensaje Mensaje a mostrar antes de leer
     * @return Texto ingresado por el usuario
     */
    public static String leerTexto(String mensaje) {
        System.out.print(mensaje + ": ");
        return scanner.nextLine().trim();
    }

    /**
     * Lee un número entero del usuario.
     * @param mensaje Mensaje a mostrar antes de leer
     * @return Número entero ingresado
     */
    public static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje + ": ");
                int valor = Integer.parseInt(scanner.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                mostrarError("Por favor ingrese un número entero válido");
            }
        }
    }

    /**
     * Lee un número decimal del usuario.
     * @param mensaje Mensaje a mostrar antes de leer
     * @return Número decimal ingresado
     */
    public static double leerDecimal(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje + ": ");
                double valor = Double.parseDouble(scanner.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                mostrarError("Por favor ingrese un número decimal válido");
            }
        }
    }

    /**
     * Lee un PIN del usuario (ocultando la entrada).
     * Nota: En consola real no se puede ocultar, pero se puede simular.
     * @param mensaje Mensaje a mostrar antes de leer
     * @return PIN ingresado
     */
    public static String leerPin(String mensaje) {
        System.out.print(mensaje + ": ");
        return scanner.nextLine().trim();
    }

    /**
     * Lee una confirmación del usuario (sí/no).
     * @param mensaje Mensaje a mostrar
     * @return true si el usuario confirma, false en caso contrario
     */
    public static boolean leerConfirmacion(String mensaje) {
        System.out.print(mensaje + " (s/n): ");
        String respuesta = scanner.nextLine().trim().toLowerCase();
        return respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí");
    }

    // MÉTODOS DE SALIDA
    /**
     * Muestra un mensaje informativo.
     * @param mensaje Mensaje a mostrar
     */
    public static void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    /**
     * Muestra un mensaje de éxito en color verde.
     * @param mensaje Mensaje a mostrar
     */
    public static void mostrarExito(String mensaje) {
        System.out.println(ANSI_VERDE + mensaje + ANSI_RESET);
    }

    /**
     * Muestra un mensaje de error en color rojo.
     * @param mensaje Mensaje a mostrar
     */
    public static void mostrarError(String mensaje) {
        System.out.println(ANSI_ROJO + "ERROR: " + mensaje + ANSI_RESET);
    }

    /**
     * Muestra un mensaje de advertencia en color amarillo.
     * @param mensaje Mensaje a mostrar
     */
    public static void mostrarAdvertencia(String mensaje) {
        System.out.println(ANSI_AMARILLO + "ADVERTENCIA: " + mensaje + ANSI_RESET);
    }

    /**
     * Muestra un título formateado.
     * @param titulo Título a mostrar
     */
    public static void mostrarTitulo(String titulo) {
        System.out.println("\n" + ANSI_AZUL + "=== " + titulo + " ===" + ANSI_RESET);
    }

    // Muestra una línea separadora.
    public static void mostrarSeparador() {
        System.out.println("─────────────────────────────────────────");
    }

    // Muestra una línea en blanco.
    public static void mostrarLineaEnBlanco() {
        System.out.println();
    }

    // MÉTODOS DE UTILIDAD
    /**
     * Limpia la pantalla de la consola.
     * Nota: Este método puede no funcionar en todos los sistemas operativos.
     */
    public static void limpiarPantalla() {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) {
                Runtime.getRuntime().exec("cls");
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (Exception e) {
            // Si no se puede limpiar, imprimir líneas vacías
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    /**
     * Pausa la ejecución hasta que el usuario presione Enter.
     * @param mensaje Mensaje a mostrar
     */
    public static void pausar(String mensaje) {
        System.out.print("\n" + mensaje);
        scanner.nextLine();
    }

    /**
     * Cierra el scanner de entrada.
     * Debería llamarse al finalizar la aplicación.
     */
    public static void cerrar() {
        scanner.close();
    }
}
