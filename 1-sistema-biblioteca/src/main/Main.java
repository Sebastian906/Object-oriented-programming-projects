package main;

import controller.BibliotecaController;
import javax.swing.JOptionPane;
import models.Biblioteca;
import view.BibliotecaVista;
import view.BibliotecaVistaGUI;

/**
 * Clase principal del Sistema de Gestión de Biblioteca.
 * Punto de entrada de la aplicación.
 * Conceptos POO aplicados:
 * - Instanciación de objetos
 * - Inyección de dependencias (básica)
 * - Patrón MVC (separación de responsabilidades)
 * - Polimorfismo (diferentes vistas)
 * Arquitectura:
 * - MODELO: Biblioteca (datos y lógica de negocio)
 * - VISTA: BibliotecaVista (consola) o BibliotecaVistaGUI (Swing)
 * - CONTROLADOR: BibliotecaController (coordina operaciones)
 */
public class Main {

    /**
     * Método principal - Punto de entrada de la aplicación.
     * Ofrece al usuario elegir entre modo consola o interfaz gráfica.
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        // 1. Crear el modelo (Biblioteca)
        Biblioteca biblioteca = new Biblioteca();

        // 2. Preguntar al usuario qué modo desea usar
        String[] opciones = {"Modo Consola", "Modo Gráfico (Swing)", "Salir"};
        
        int seleccion = JOptionPane.showOptionDialog(
            null,
            "Seleccione el modo de ejecución:",
            "Sistema de Gestión de Biblioteca",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]
        );

        // 3. Ejecutar según la selección
        switch (seleccion) {
            case 0: // Modo Consola
                ejecutarModoConsola(biblioteca);
                break;
            case 1: // Modo Gráfico
                ejecutarModoGrafico(biblioteca);
                break;
            default: // Salir o cerrar diálogo
                System.out.println("Sistema finalizado.");
                break;
        }
    }

    /**
     * Ejecuta la aplicación en modo consola.
     * @param biblioteca Instancia de la biblioteca
     */
    private static void ejecutarModoConsola(Biblioteca biblioteca) {
        System.out.println("=== MODO CONSOLA ===");
        System.out.println("Iniciando Sistema de Gestión de Biblioteca...");

        // Crear vista de consola
        BibliotecaVista vista = new BibliotecaVista();

        // Crear controlador e inyectar dependencias
        BibliotecaController controller = new BibliotecaController(biblioteca, vista);

        // Ejecutar la aplicación
        controller.ejecutar();

        // Cerrar recursos
        vista.cerrar();

        System.out.println("Sistema finalizado.");
    }

    /**
     * Ejecuta la aplicación en modo gráfico (Swing).
     * @param biblioteca Instancia de la biblioteca
     */
    private static void ejecutarModoGrafico(Biblioteca biblioteca) {
        System.out.println("=== MODO GRÁFICO ===");
        System.out.println("Iniciando interfaz gráfica...");

        // Crear vista gráfica
        BibliotecaVistaGUI vistaGUI = new BibliotecaVistaGUI(biblioteca);

        // Mostrar ventana
        vistaGUI.mostrar();

        System.out.println("Interfaz gráfica iniciada.");
    }
}
