package main;

import controller.CajeroController;
import service.CajeroService;
import view.CajeroVistaGUI;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Punto de entrada de la aplicación Simulador de ATM.
 * Esta clase inicializa todos los componentes del sistema
 * y lanza la interfaz gráfica de usuario.
 * Arquitectura del sistema:
 * 
 * ┌─────────────────────────────────────────────────────────┐
 * │                      MAIN (Main.java)                   │
 * │  • Crea instancias de servicios, controlador y vista    │
 * │  • Coordina el arranque de la aplicación                │
 * └─────────────────────────────────────────────────────────┘
 *                          │
 *                          ▼
 * ┌─────────────────────────────────────────────────────────┐
 * │                    VISTA (CajeroVistaGUI)               │
 * │  • Interfaz gráfica Swing                               │
 * │  • Captura entradas del usuario                         │
 * │  • Muestra resultados                                   │
 * └─────────────────────────────────────────────────────────┘
 *                          │
 *                          ▼
 * ┌─────────────────────────────────────────────────────────┐
 * │                  CONTROLADOR (CajeroController)         │
 * │  • Recibe peticiones de la vista                        │
 * │  • Valida datos                                         │
 * │  • Delega al servicio                                   │
 * └─────────────────────────────────────────────────────────┘
 *                          │
 *                          ▼
 * ┌─────────────────────────────────────────────────────────┐
 * │                   SERVICIO (CajeroService)              │
 * │  • Contiene lógica de negocio                           │
 * │  • Gestiona cuentas y transacciones                     │
 * │  • Persiste datos en memoria                            │
 * └─────────────────────────────────────────────────────────┘
 * Principios POO aplicados:
 * - Dependency Injection: Se inyectan dependencias en cada componente
 * - Separation of Concerns: Cada paquete tiene una responsabilidad
 * - Single Responsibility: Main solo inicializa y lanza
 * @author Proyecto POO - Simulador ATM
 * @version 1.0
 */
public class Main {
    // CONSTANTES
    // Nombre de la aplicación
    private static final String NOMBRE_APP = "Simulador de Cajero Automático";

    // Versión de la aplicación
    private static final String VERSION = "1.0";

    // MÉTODO PRINCIPAL
    /**
     * Método principal de la aplicación.
     * Inicializa:
     * 1. ElLookAndFeel del sistema para apariencia nativa
     * 2. El servicio principal del ATM
     * 3. El controlador que coordina modelo y vista
     * 4. La interfaz gráfica de usuario
     * @param args Argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        System.out.println("=== " + NOMBRE_APP + " v" + VERSION + " ===");
        System.out.println("Iniciando aplicación...");
        // Configurar Look and Feel del sistema
        configurarApariencia();        
        // Ejecutar en el EDT (Event Dispatch Thread) de Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                iniciarAplicacion();
            }
        });
    }

    // MÉTODOS DE INICIALIZACIÓN    
    /**
     * Configura la apariencia de la aplicación.
     * Usa el Look and Feel del sistema operativo para una experiencia nativa.
     */
    private static void configurarApariencia() {
        try {
            // Establecer Look and Feel del sistema
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println("Apariencia configurada: " + 
                UIManager.getLookAndFeel().getName());
        } catch (Exception e) {
            System.out.println("No se pudo configurar la apariencia del sistema");
            System.out.println("Se usará la apariencia por defecto de Swing");
        }
    }

    /**
     * Inicializa y lanza la aplicación.
     * Flujo de inicialización:
     * 1. Crear CajeroService (carga cuentas de prueba)
     * 2. Crear CajeroController (inyecta servicio)
     * 3. Crear CajeroVistaGUI (inyecta controlador)
     * 4. Mostrar ventana
     */
    private static void iniciarAplicacion() {
        try {
            System.out.println("Inicializando servicios...");
            // 1. Crear servicio principal
            CajeroService cajeroService = new CajeroService();
            System.out.println("✓ CajeroService inicializado");
            // 2. Crear controlador
            CajeroController controller = new CajeroController(cajeroService);
            System.out.println("✓ CajeroController inicializado");
            // 3. Crear vista
            CajeroVistaGUI vista = new CajeroVistaGUI(controller);
            System.out.println("✓ CajeroVistaGUI inicializada");
            // 4. Mostrar ventana
            vista.mostrar();
            System.out.println("✓ Aplicación iniciada correctamente");
            System.out.println();
            System.out.println("Cuentas de prueba disponibles:");
            System.out.println("  Tarjeta: 1234567890123456 | PIN: 1234 | Titular: Juan Pérez");
            System.out.println("  Tarjeta: 2345678901234567 | PIN: 5678 | Titular: María García");
            System.out.println("  Tarjeta: 3456789012345678 | PIN: 9012 | Titular: Carlos López");
            System.out.println("  Tarjeta: 4567890123456789 | PIN: 3456 | Titular: Ana Martínez");
            System.out.println();
        } catch (Exception e) {
            System.err.println("Error al iniciar la aplicación: " + e.getMessage());
            e.printStackTrace();
            // Mostrar diálogo de error
            javax.swing.JOptionPane.showMessageDialog(
                null,
                "Error al iniciar la aplicación:\n" + e.getMessage(),
                "Error",
                javax.swing.JOptionPane.ERROR_MESSAGE
            );            
            System.exit(1);
        }
    }
}
