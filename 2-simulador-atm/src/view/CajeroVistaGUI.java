package view;

import controller.CajeroController;
import models.CuentaBancaria;
import models.Transaccion;

import exceptions.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Interfaz gráfica de usuario para el ATM desarrollada con Swing.
 * Proporciona una interfaz visual intuitiva para interactuar con el
 * cajero automático, incluyendo pantallas para autenticación,
 * operaciones bancarias y visualización de historial.
 * Principios POO aplicados:
 * - Abstracción: Representa la interfaz de usuario del ATM
 * - Single Responsibility: Solo maneja la presentación
 * - Dependency Inversion: Depende del CajeroController (abstracción)
 * Arquitectura: Capa Vista en patrón MVC
 */
public class CajeroVistaGUI extends JFrame {
    // ATRIBUTOS
    // Controlador del ATM
    private final CajeroController controller;

    // Panel principal de contenido
    private JPanel contentPane;

    // CardLayout para alternar entre pantallas
    private CardLayout cardLayout;

    // Panel que contiene las pantallas
    private JPanel cardPanel;

    // COMPONENTES DE PANTALLA DE INICIO
    // Campo de texto para número de tarjeta
    private JTextField txtNumeroTarjeta;

    // Campo de contraseña para PIN
    private JPasswordField txtPin;

    // Botón de iniciar sesión
    private JButton btnIniciarSesion;

    // COMPONENTES DE PANTALLA PRINCIPAL
    // Etiqueta para mostrar el saldo actual
    private JLabel lblSaldo;

    // Etiqueta para mostrar el nombre del titular
    private JLabel lblTitular;

    // Tabla para mostrar historial de transacciones
    private JTable tablaHistorial;

    // Modelo de la tabla de historial
    private DefaultTableModel modeloTabla;

    // COMPONENTES DE PANTALLA DE OPERACIONES
    // Campo de texto para monto (retiro)
    private JTextField txtMontoRetiro;
    
    // Campo de texto para monto (depósito)
    private JTextField txtMontoDeposito;
    
    // Campo de texto para monto (transferencia)
    private JTextField txtMontoTransferencia;
    
    // Campo de texto para cuenta destino (transferencias)
    private JTextField txtCuentaDestino;

    // CONSTANTES DE NOMBRES DE PANTALLAS
    private static final String PANTALLA_INICIO = "INICIO";
    private static final String PANTALLA_PRINCIPAL = "PRINCIPAL";
    private static final String PANTALLA_SALDO = "SALDO";
    private static final String PANTALLA_RETIRAR = "RETIRAR";
    private static final String PANTALLA_DEPOSITAR = "DEPOSITAR";
    private static final String PANTALLA_TRANSFERIR = "TRANSFERIR";
    private static final String PANTALLA_HISTORIAL = "HISTORIAL";
    private static final String PANTALLA_CAMBIAR_PIN = "CAMBIAR_PIN";
    
    // COLORES DEL TEMA (Solicitados: Verde=Confirmar, Rojo=Cancelar, Azul=Acceder)
    private static final Color COLOR_AZUL_OSCURO = new Color(0, 51, 102);
    private static final Color COLOR_VERDE = new Color(34, 139, 34);
    private static final Color COLOR_ROJO = new Color(180, 30, 30);
    private static final Color COLOR_FONDO = new Color(230, 230, 230);
    private static final Color COLOR_TEXTO = new Color(35, 35, 35);
    private static final Color COLOR_TEXTO_OSCURO = new Color(25, 25, 25);
    
    // ========================================================================
    // MÉTODO AUXILIAR PARA LIMPIAR MONTOS
    // ========================================================================
    
    /**
     * Limpia y parsea un monto ingresado por el usuario.
     * Acepta formatos: 10000, 10,000, 10.000, $10,000, $10.000
     */
    private double parsearMonto(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new NumberFormatException("Campo vacío");
        }
        // Eliminar espacios, signo $ y espacios
        String limpio = texto.trim().replace("$", "").replace(" ", "");
        // Eliminar comas
        limpio = limpio.replace(",", "");
        // Eliminar puntos que sean separadores de miles (si hay más de un punto)
        if (limpio.chars().filter(c -> c == '.').count() > 1) {
            limpio = limpio.replace(".", "");
        }
        return Double.parseDouble(limpio);
    }
    
    /**
     * Configura un botón con estilo uniforme y colores correctos.
     */
    private void configurarBoton(JButton boton, Color colorFondo, Color colorTexto) {
        boton.setBackground(colorFondo);
        boton.setForeground(colorTexto);
        boton.setOpaque(true);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFont(new Font("Arial", Font.BOLD, 13));
    }
    
    // CONSTRUCTOR
    /**
     * Constructor de CajeroVistaGUI.
     * @param controller Controlador del ATM
     */
    public CajeroVistaGUI(CajeroController controller) {
        this.controller = controller;
        inicializarComponentes();
    }
    
    // INICIALIZACIÓN DE COMPONENTES
    // Inicializa todos los componentes de la interfaz.
    private void inicializarComponentes() {
        // Configurar ventana principal
        setTitle("Simulador de Cajero Automático");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 650);
        setLocationRelativeTo(null); // Centrar en pantalla
        setResizable(false);

        // Crear panel principal con BorderLayout
        contentPane = new JPanel(new BorderLayout(10, 10));
        contentPane.setBackground(COLOR_FONDO);
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(contentPane);

        // Configurar CardLayout
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(COLOR_FONDO);

        // Crear pantallas
        crearPantallaInicio();
        crearPantallaPrincipal();
        crearPantallaSaldo();
        crearPantallaRetirar();
        crearPantallaDepositar();
        crearPantallaTransferir();
        crearPantallaHistorial();
        crearPantallaCambiarPin();

        // Agregar panel de cartas al panel principal (centrado)
        contentPane.add(cardPanel, BorderLayout.CENTER);
    }

    // PANTALLA DE INICIO
    // Crea la pantalla de inicio de sesión.
    private void crearPantallaInicio() {
        // Panel contenedor con GridBagLayout para centrado perfecto
        JPanel panelContenedor = new JPanel(new GridBagLayout());
        panelContenedor.setBackground(COLOR_FONDO);

        // Panel interno con BoxLayout
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        panel.setPreferredSize(new Dimension(400, 450));

        // Título
        JLabel titulo = new JLabel("CAJERO AUTOMÁTICO");
        titulo.setFont(new Font("Arial", Font.BOLD, 22));
        titulo.setForeground(COLOR_AZUL_OSCURO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Subtítulo
        JLabel subtitulo = new JLabel("Ingrese sus datos para continuar");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitulo.setForeground(COLOR_TEXTO);
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(subtitulo);

        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Número de tarjeta
        JLabel lblTarjeta = new JLabel("Número de Tarjeta:");
        lblTarjeta.setFont(new Font("Arial", Font.BOLD, 12));
        lblTarjeta.setForeground(COLOR_TEXTO_OSCURO);
        lblTarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblTarjeta);

        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        txtNumeroTarjeta = new JTextField(16);
        txtNumeroTarjeta.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtNumeroTarjeta.setFont(new Font("Arial", Font.PLAIN, 14));
        txtNumeroTarjeta.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtNumeroTarjeta.setToolTipText("Ingrese 16 dígitos");
        panel.add(txtNumeroTarjeta);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // PIN
        JLabel lblPin = new JLabel("PIN:");
        lblPin.setFont(new Font("Arial", Font.BOLD, 12));
        lblPin.setForeground(COLOR_TEXTO_OSCURO);
        lblPin.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblPin);

        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        txtPin = new JPasswordField(6);
        txtPin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtPin.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPin.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtPin.setToolTipText("Ingrese 4-6 dígitos");
        panel.add(txtPin);

        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Botón de iniciar sesión
        btnIniciarSesion = new JButton("Iniciar Sesión");
        btnIniciarSesion.setFont(new Font("Arial", Font.BOLD, 14));
        configurarBoton(btnIniciarSesion, COLOR_AZUL_OSCURO, Color.WHITE);
        btnIniciarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIniciarSesion.setMaximumSize(new Dimension(250, 45));
        btnIniciarSesion.addActionListener((ActionEvent e) -> {
            iniciarSesion();
        });
        panel.add(btnIniciarSesion);

        // Agregar panel interno al contenedor (centrado)
        panelContenedor.add(panel);

        // Agregar panel al card panel
        cardPanel.add(panelContenedor, PANTALLA_INICIO);
    }

    // PANTALLA PRINCIPAL
    // Crea la pantalla principal con las opciones del ATM.
    private void crearPantallaPrincipal() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Header con información del usuario
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(COLOR_AZUL_OSCURO);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        lblTitular = new JLabel("Bienvenido");
        lblTitular.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitular.setForeground(Color.WHITE);
        lblTitular.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(lblTitular);

        lblSaldo = new JLabel("Saldo: $0.00");
        lblSaldo.setFont(new Font("Arial", Font.PLAIN, 13));
        lblSaldo.setForeground(new Color(230, 230, 230));
        lblSaldo.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(lblSaldo);

        panel.add(headerPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Título del menú
        JLabel lblMenu = new JLabel("Seleccione una operación:");
        lblMenu.setFont(new Font("Arial", Font.BOLD, 13));
        lblMenu.setForeground(COLOR_TEXTO_OSCURO);
        lblMenu.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblMenu);

        panel.add(Box.createRigidArea(new Dimension(0, 12)));

        // Botones de operaciones
        JButton btnConsultarSaldo = crearBotonOperacion(" Consultar Saldo", PANTALLA_SALDO);
        JButton btnRetirar = crearBotonOperacion(" Retirar Efectivo", PANTALLA_RETIRAR);
        JButton btnDepositar = crearBotonOperacion(" Depositar Efectivo", PANTALLA_DEPOSITAR);
        JButton btnTransferir = crearBotonOperacion(" Transferir", PANTALLA_TRANSFERIR);
        JButton btnHistorial = crearBotonOperacion(" Ver Historial", PANTALLA_HISTORIAL);
        JButton btnCambiarPin = crearBotonOperacion(" Cambiar PIN", PANTALLA_CAMBIAR_PIN);

        panel.add(btnConsultarSaldo);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(btnRetirar);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(btnDepositar);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(btnTransferir);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(btnHistorial);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(btnCambiarPin);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Botón de cerrar sesión
        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        configurarBoton(btnCerrarSesion, COLOR_ROJO, Color.WHITE);
        btnCerrarSesion.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCerrarSesion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnCerrarSesion.addActionListener((ActionEvent e) -> {
            cerrarSesion();
        });
        panel.add(btnCerrarSesion);

        cardPanel.add(panel, PANTALLA_PRINCIPAL);
    }

    /**
     * Crea un botón de operación con estilo uniforme.
     * @param texto Texto del botón
     * @param pantallaDestino Nombre de la pantalla destino
     * @return Botón configurado
     */
    private JButton crearBotonOperacion(String texto, String pantallaDestino) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.PLAIN, 13));
        boton.setBackground(new Color(255, 255, 255));
        boton.setForeground(COLOR_TEXTO_OSCURO);
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        // Efecto hover simplificado
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(230, 240, 250));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(255, 255, 255));
            }
        });

        boton.addActionListener((ActionEvent e) -> {
            actualizarPantallaPrincipal();
            cardLayout.show(cardPanel, pantallaDestino);
        });

        return boton;
    }

    // PANTALLA DE CONSULTA DE SALDO
    // Crea la pantalla de consulta de saldo.
    private void crearPantallaSaldo() {
        JPanel panelContenedor = new JPanel(new GridBagLayout());
        panelContenedor.setBackground(COLOR_FONDO);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(40, 40, 40, 40)
        ));
        panel.setPreferredSize(new Dimension(400, 300));

        // Título
        JLabel titulo = new JLabel("CONSULTA DE SALDO");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(COLOR_AZUL_OSCURO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);

        panel.add(Box.createRigidArea(new Dimension(0, 35)));

        // Saldo
        JLabel lblSaldoActual = new JLabel("Saldo Disponible:");
        lblSaldoActual.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSaldoActual.setForeground(COLOR_TEXTO_OSCURO);
        lblSaldoActual.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblSaldoActual);

        panel.add(Box.createRigidArea(new Dimension(0, 12)));

        lblSaldo = new JLabel("$0.00");
        lblSaldo.setFont(new Font("Arial", Font.BOLD, 32));
        lblSaldo.setForeground(COLOR_VERDE);
        lblSaldo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblSaldo);

        panel.add(Box.createRigidArea(new Dimension(0, 35)));

        // Botón volver
        JButton btnVolver = new JButton("Volver al Menú");
        configurarBoton(btnVolver, COLOR_AZUL_OSCURO, Color.WHITE);
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver.setMaximumSize(new Dimension(200, 40));
        btnVolver.addActionListener((ActionEvent e) -> {
            cardLayout.show(cardPanel, PANTALLA_PRINCIPAL);
        });
        panel.add(btnVolver);

        panelContenedor.add(panel);
        cardPanel.add(panelContenedor, PANTALLA_SALDO);
    }

    // PANTALLA DE RETIRO
    // Crea la pantalla de retiro de efectivo.
    private void crearPantallaRetirar() {
        JPanel panelContenedor = new JPanel(new GridBagLayout());
        panelContenedor.setBackground(COLOR_FONDO);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(30, 35, 30, 35)
        ));
        panel.setPreferredSize(new Dimension(400, 380));
        
        // Título
        JLabel titulo = new JLabel("RETIRO DE EFECTIVO");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(COLOR_AZUL_OSCURO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);

        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Instrucción
        JLabel instruccion = new JLabel("Ingrese el monto a retirar:");
        instruccion.setFont(new Font("Arial", Font.PLAIN, 13));
        instruccion.setForeground(COLOR_TEXTO_OSCURO);
        instruccion.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(instruccion);

        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Campo de monto
        txtMontoRetiro = new JTextField();
        txtMontoRetiro.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtMontoRetiro.setFont(new Font("Arial", Font.PLAIN, 16));
        txtMontoRetiro.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtMontoRetiro.setToolTipText("Máximo: $500,000");
        panel.add(txtMontoRetiro);

        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Límite
        JLabel lblLimite = new JLabel("Máximo por transacción: $500,000");
        lblLimite.setFont(new Font("Arial", Font.ITALIC, 11));
        lblLimite.setForeground(Color.GRAY);
        lblLimite.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblLimite);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Botones
        JButton btnConfirmar = new JButton("Confirmar Retiro");
        configurarBoton(btnConfirmar, COLOR_VERDE, Color.WHITE);
        btnConfirmar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConfirmar.setMaximumSize(new Dimension(250, 40));
        btnConfirmar.addActionListener((ActionEvent e) -> {
            realizarRetiro();
        });
        panel.add(btnConfirmar);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btnVolver = new JButton("Cancelar");
        configurarBoton(btnVolver, COLOR_ROJO, Color.WHITE);
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver.setMaximumSize(new Dimension(250, 40));
        btnVolver.addActionListener((ActionEvent e) -> {
            limpiarCampos();
            cardLayout.show(cardPanel, PANTALLA_PRINCIPAL);
        });
        panel.add(btnVolver);

        panelContenedor.add(panel);
        cardPanel.add(panelContenedor, PANTALLA_RETIRAR);
    }
    
    // PANTALLA DE DEPÓSITO
    // Crea la pantalla de depósito de efectivo.
    private void crearPantallaDepositar() {
        JPanel panelContenedor = new JPanel(new GridBagLayout());
        panelContenedor.setBackground(COLOR_FONDO);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(30, 35, 30, 35)
        ));
        panel.setPreferredSize(new Dimension(400, 380));

        // Título
        JLabel titulo = new JLabel("DEPÓSITO DE EFECTIVO");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(COLOR_AZUL_OSCURO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);

        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Instrucción
        JLabel instruccion = new JLabel("Ingrese el monto a depositar:");
        instruccion.setFont(new Font("Arial", Font.PLAIN, 13));
        instruccion.setForeground(COLOR_TEXTO_OSCURO);
        instruccion.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(instruccion);

        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Campo de monto
        txtMontoDeposito = new JTextField();
        txtMontoDeposito.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtMontoDeposito.setFont(new Font("Arial", Font.PLAIN, 16));
        txtMontoDeposito.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtMontoDeposito.setToolTipText("Mínimo: $10,000");
        panel.add(txtMontoDeposito);

        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Límite
        JLabel lblLimite = new JLabel("Mínimo por depósito: $10,000");
        lblLimite.setFont(new Font("Arial", Font.ITALIC, 11));
        lblLimite.setForeground(Color.GRAY);
        lblLimite.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblLimite);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Botones
        JButton btnConfirmar = new JButton("Confirmar Depósito");
        configurarBoton(btnConfirmar, COLOR_VERDE, Color.WHITE);
        btnConfirmar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConfirmar.setMaximumSize(new Dimension(250, 40));
        btnConfirmar.addActionListener((ActionEvent e) -> {
            realizarDeposito();
        });
        panel.add(btnConfirmar);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btnVolver = new JButton("Cancelar");
        configurarBoton(btnVolver, COLOR_ROJO, Color.WHITE);
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver.setMaximumSize(new Dimension(250, 40));
        btnVolver.addActionListener((ActionEvent e) -> {
            limpiarCampos();
            cardLayout.show(cardPanel, PANTALLA_PRINCIPAL);
        });
        panel.add(btnVolver);

        panelContenedor.add(panel);
        cardPanel.add(panelContenedor, PANTALLA_DEPOSITAR);
    }

    // PANTALLA DE TRANSFERENCIA
    // Crea la pantalla de transferencia.
    private void crearPantallaTransferir() {
        JPanel panelContenedor = new JPanel(new GridBagLayout());
        panelContenedor.setBackground(COLOR_FONDO);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(30, 35, 30, 35)
        ));
        panel.setPreferredSize(new Dimension(400, 450));

        // Título
        JLabel titulo = new JLabel("TRANSFERENCIA");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(COLOR_AZUL_OSCURO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);

        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Cuenta destino
        JLabel lblCuentaDestino = new JLabel("Número de Cuenta Destino:");
        lblCuentaDestino.setFont(new Font("Arial", Font.PLAIN, 13));
        lblCuentaDestino.setForeground(COLOR_TEXTO_OSCURO);
        lblCuentaDestino.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblCuentaDestino);

        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        txtCuentaDestino = new JTextField();
        txtCuentaDestino.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtCuentaDestino.setFont(new Font("Arial", Font.PLAIN, 16));
        txtCuentaDestino.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(txtCuentaDestino);

        panel.add(Box.createRigidArea(new Dimension(0, 18)));

        // Monto
        JLabel lblMonto = new JLabel("Monto a Transferir:");
        lblMonto.setFont(new Font("Arial", Font.PLAIN, 13));
        lblMonto.setForeground(COLOR_TEXTO_OSCURO);
        lblMonto.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblMonto);

        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        txtMontoTransferencia = new JTextField();
        txtMontoTransferencia.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtMontoTransferencia.setFont(new Font("Arial", Font.PLAIN, 16));
        txtMontoTransferencia.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(txtMontoTransferencia);

        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Límite
        JLabel lblLimite = new JLabel("Mínimo por transferencia: $1,000");
        lblLimite.setFont(new Font("Arial", Font.ITALIC, 11));
        lblLimite.setForeground(Color.GRAY);
        lblLimite.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblLimite);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Botones
        JButton btnConfirmar = new JButton("Confirmar Transferencia");
        configurarBoton(btnConfirmar, COLOR_VERDE, Color.WHITE);
        btnConfirmar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConfirmar.setMaximumSize(new Dimension(250, 40));
        btnConfirmar.addActionListener((ActionEvent e) -> {
            realizarTransferencia();
        });
        panel.add(btnConfirmar);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btnVolver = new JButton("Cancelar");
        configurarBoton(btnVolver, COLOR_ROJO, Color.WHITE);
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver.setMaximumSize(new Dimension(250, 40));
        btnVolver.addActionListener((ActionEvent e) -> {
            limpiarCampos();
            cardLayout.show(cardPanel, PANTALLA_PRINCIPAL);
        });
        panel.add(btnVolver);

        panelContenedor.add(panel);
        cardPanel.add(panelContenedor, PANTALLA_TRANSFERIR);
    }

    // PANTALLA DE HISTORIAL
    // Crea la pantalla de historial de transacciones.
    private void crearPantallaHistorial() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(COLOR_FONDO);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Título
        JLabel titulo = new JLabel("HISTORIAL DE TRANSACCIONES");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setForeground(COLOR_AZUL_OSCURO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Configurar tabla de historial
        String[] columnas = {"ID", "Tipo", "Monto", "Fecha", "Saldo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaHistorial = new JTable(modeloTabla);
        tablaHistorial.setFont(new Font("Arial", Font.PLAIN, 11));
        tablaHistorial.setRowHeight(24);
        tablaHistorial.getTableHeader().setFont(new Font("Arial", Font.BOLD, 11));
        tablaHistorial.getTableHeader().setBackground(COLOR_AZUL_OSCURO);
        tablaHistorial.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(tablaHistorial);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(460, 350));
        panel.add(scrollPane);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotones.setBackground(COLOR_FONDO);

        JButton btnActualizar = new JButton("Actualizar");
        configurarBoton(btnActualizar, COLOR_AZUL_OSCURO, Color.WHITE);
        btnActualizar.addActionListener((var e) -> {
            cargarHistorial();
        });
        panelBotones.add(btnActualizar);

        JButton btnVolver = new JButton("Volver al Menú");
        configurarBoton(btnVolver, COLOR_ROJO, Color.WHITE);
        btnVolver.addActionListener((ActionEvent e) -> {
            cardLayout.show(cardPanel, PANTALLA_PRINCIPAL);
        });
        panelBotones.add(btnVolver);

        panel.add(panelBotones);

        cardPanel.add(panel, PANTALLA_HISTORIAL);
    }

    // PANTALLA DE CAMBIO DE PIN
    // Crea la pantalla de cambio de PIN.
    private void crearPantallaCambiarPin() {
        JPanel panelContenedor = new JPanel(new GridBagLayout());
        panelContenedor.setBackground(COLOR_FONDO);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(30, 35, 30, 35)
        ));
        panel.setPreferredSize(new Dimension(400, 480));

        // Título
        JLabel titulo = new JLabel("CAMBIO DE PIN");
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        titulo.setForeground(COLOR_AZUL_OSCURO);
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titulo);

        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        // PIN actual
        JLabel lblPinActual = new JLabel("PIN Actual:");
        lblPinActual.setFont(new Font("Arial", Font.PLAIN, 13));
        lblPinActual.setForeground(COLOR_TEXTO_OSCURO);
        lblPinActual.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblPinActual);

        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        JPasswordField txtPinActual = new JPasswordField(6);
        txtPinActual.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtPinActual.setFont(new Font("Arial", Font.PLAIN, 16));
        txtPinActual.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(txtPinActual);

        panel.add(Box.createRigidArea(new Dimension(0, 18)));

        // Nuevo PIN
        JLabel lblNuevoPin = new JLabel("Nuevo PIN (4-6 dígitos):");
        lblNuevoPin.setFont(new Font("Arial", Font.PLAIN, 13));
        lblNuevoPin.setForeground(COLOR_TEXTO_OSCURO);
        lblNuevoPin.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblNuevoPin);

        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        JPasswordField txtNuevoPin = new JPasswordField(6);
        txtNuevoPin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtNuevoPin.setFont(new Font("Arial", Font.PLAIN, 16));
        txtNuevoPin.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(txtNuevoPin);

        panel.add(Box.createRigidArea(new Dimension(0, 18)));

        // Confirmar nuevo PIN
        JLabel lblConfirmarPin = new JLabel("Confirmar Nuevo PIN:");
        lblConfirmarPin.setFont(new Font("Arial", Font.PLAIN, 13));
        lblConfirmarPin.setForeground(COLOR_TEXTO_OSCURO);
        lblConfirmarPin.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblConfirmarPin);

        panel.add(Box.createRigidArea(new Dimension(0, 8)));

        JPasswordField txtConfirmarPin = new JPasswordField(6);
        txtConfirmarPin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        txtConfirmarPin.setFont(new Font("Arial", Font.PLAIN, 16));
        txtConfirmarPin.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(txtConfirmarPin);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Botones
        JButton btnConfirmar = new JButton("Confirmar Cambio");
        configurarBoton(btnConfirmar, COLOR_VERDE, Color.WHITE);
        btnConfirmar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConfirmar.setMaximumSize(new Dimension(250, 40));
        final JPasswordField finalPinActual = txtPinActual;
        final JPasswordField finalNuevoPin = txtNuevoPin;
        final JPasswordField finalConfirmarPin = txtConfirmarPin;
        btnConfirmar.addActionListener((ActionEvent e) -> {
            realizarCambioPin(finalPinActual, finalNuevoPin, finalConfirmarPin);
        });
        panel.add(btnConfirmar);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        JButton btnVolver = new JButton("Cancelar");
        configurarBoton(btnVolver, COLOR_ROJO, Color.WHITE);
        btnVolver.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnVolver.setMaximumSize(new Dimension(250, 40));
        btnVolver.addActionListener((ActionEvent e) -> {
            limpiarCampos();
            cardLayout.show(cardPanel, PANTALLA_PRINCIPAL);
        });
        panel.add(btnVolver);

        panelContenedor.add(panel);
        cardPanel.add(panelContenedor, PANTALLA_CAMBIAR_PIN);
    }

    // MÉTODOS DE ACCIÓN
    // Realiza el proceso de inicio de sesión.
    private void iniciarSesion() {
        String tarjeta = txtNumeroTarjeta.getText().trim();
        String pin = new String(txtPin.getPassword()).trim();
        
        if (tarjeta.isEmpty() || pin.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese todos los campos",
                "Campos vacíos",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            if (controller.iniciarSesion(tarjeta, pin)) {
                JOptionPane.showMessageDialog(this,
                    "Sesión iniciada correctamente",
                    "Bienvenido",
                    JOptionPane.INFORMATION_MESSAGE);
                actualizarPantallaPrincipal();
                cardLayout.show(cardPanel, PANTALLA_PRINCIPAL);
                limpiarCampos();
            }
        } catch (CuentaBloqueadaException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Cuenta bloqueada",
                JOptionPane.ERROR_MESSAGE);
        } catch (PINIncorrectoException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "PIN incorrecto",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error inesperado: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Cierra la sesión actual y vuelve a la pantalla de inicio.
    private void cerrarSesion() {
        controller.cerrarSesion();
        limpiarCampos();
        cardLayout.show(cardPanel, PANTALLA_INICIO);
    }

    // Actualiza la información de la pantalla principal.
    private void actualizarPantallaPrincipal() {
        CuentaBancaria cuenta = controller.getCuentaActual();
        if (cuenta != null) {
            lblTitular.setText("Bienvenido, " + cuenta.getTitular());
            lblSaldo.setText("Saldo: $" + String.format("%,.2f", cuenta.getSaldo()));
        }
    }
    // Realiza un retiro de efectivo.
    private void realizarRetiro() {
        try {
            String textoMonto = txtMontoRetiro.getText().trim();
            if (textoMonto.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor ingrese un monto", 
                    "Campo vacío", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            double monto = parsearMonto(textoMonto);
            double nuevoSaldo = controller.retirarEfectivo(monto);
            
            JOptionPane.showMessageDialog(this,
                "Retiro exitoso!\n\nNuevo saldo: $" + 
                String.format("%,.2f", nuevoSaldo),
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            
            actualizarPantallaPrincipal();
            txtMontoRetiro.setText("");
            cardLayout.show(cardPanel, PANTALLA_PRINCIPAL);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese un monto válido numérico\n Ejemplo: 10000",
                "Error de formato",
                JOptionPane.ERROR_MESSAGE);
        } catch (MontoInvalidoException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Monto inválido",
                JOptionPane.ERROR_MESSAGE);
        } catch (SaldoInsuficienteException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Saldo insuficiente",
                JOptionPane.ERROR_MESSAGE);
        } catch (SesionNoAutenticadaException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Sesión no activa",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error inesperado: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Realiza un depósito de efectivo.
    private void realizarDeposito() {
        try {
            String textoMonto = txtMontoDeposito.getText().trim();
            if (textoMonto.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor ingrese un monto", 
                    "Campo vacío", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            double monto = parsearMonto(textoMonto);
            double nuevoSaldo = controller.depositarEfectivo(monto);

            JOptionPane.showMessageDialog(this,
                "Depósito exitoso!\n\nNuevo saldo: $" + 
                String.format("%,.2f", nuevoSaldo),
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);

            actualizarPantallaPrincipal();
            txtMontoDeposito.setText("");
            cardLayout.show(cardPanel, PANTALLA_PRINCIPAL);
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese un monto válido numérico\n Ejemplo: 10000",
                "Error de formato",
                JOptionPane.ERROR_MESSAGE);
        } catch (MontoInvalidoException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Monto inválido",
                JOptionPane.ERROR_MESSAGE);
        } catch (SesionNoAutenticadaException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Sesión no activa",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error inesperado: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    // Realiza una transferencia.
    private void realizarTransferencia() {
        try {
            String cuentaDestino = txtCuentaDestino.getText().trim();
            String textoMonto = txtMontoTransferencia.getText().trim();

            if (cuentaDestino.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Por favor ingrese el número de cuenta destino",
                    "Campo vacío",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (textoMonto.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Por favor ingrese un monto",
                    "Campo vacío",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            double monto = parsearMonto(textoMonto);

            if (controller.transferir(cuentaDestino, monto)) {
                JOptionPane.showMessageDialog(this,
                    "Transferencia exitosa a cuenta: " + cuentaDestino,
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                actualizarPantallaPrincipal();
                limpiarCampos();
                cardLayout.show(cardPanel, PANTALLA_PRINCIPAL);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Por favor ingrese un monto válido numérico\n Ejemplo: 10000",
                "Error de formato",
                JOptionPane.ERROR_MESSAGE);
        } catch (CuentaNoEncontradaException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Cuenta no encontrada",
                JOptionPane.ERROR_MESSAGE);
        } catch (MontoInvalidoException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Monto inválido",
                JOptionPane.ERROR_MESSAGE);
        } catch (SaldoInsuficienteException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Saldo insuficiente",
                JOptionPane.ERROR_MESSAGE);
        } catch (SesionNoAutenticadaException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Sesión no activa",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error inesperado: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Realiza el cambio de PIN.
    private void realizarCambioPin(JPasswordField pinActual, 
                                    JPasswordField nuevoPin, 
                                    JPasswordField confirmarPin) {
        try {
            String pinActualStr = new String(pinActual.getPassword()).trim();
            String nuevoPinStr = new String(nuevoPin.getPassword()).trim();
            String confirmarPinStr = new String(confirmarPin.getPassword()).trim();
            
            if (pinActualStr.isEmpty() || nuevoPinStr.isEmpty() || confirmarPinStr.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Por favor complete todos los campos",
                    "Campos vacíos",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Validar que los nuevos PINs coincidan
            if (!nuevoPinStr.equals(confirmarPinStr)) {
                JOptionPane.showMessageDialog(this,
                    "Los nuevos PINs no coinciden",
                    "Error de confirmación",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (controller.cambiarPin(pinActualStr, nuevoPinStr)) {
                JOptionPane.showMessageDialog(this,
                    "PIN cambiado exitosamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);
                limpiarCampos();
                cardLayout.show(cardPanel, PANTALLA_PRINCIPAL);
            }            
        } catch (PINIncorrectoException e) {
            JOptionPane.showMessageDialog(this,
                "PIN incorrecto: " + e.getMessage(),
                "Error de PIN",
                JOptionPane.ERROR_MESSAGE);
        } catch (SesionNoAutenticadaException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Sesión no activa",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error inesperado: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Carga el historial de transacciones en la tabla.
    private void cargarHistorial() {
        try {
            List<Transaccion> historial = controller.obtenerHistorial();
            // Limpiar tabla
            modeloTabla.setRowCount(0);            
            // Agregar transacciones
            for (Transaccion t : historial) {
                Object[] fila = {
                    t.getId(),
                    t.getTipo().getDescripcion(),
                    "$" + String.format("%,.2f", t.getMonto()),
                    t.getFechaFormateada(),
                    "$" + String.format("%,.2f", t.getSaldoPosterior())
                };
                modeloTabla.addRow(fila);
            }

            if (historial.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "No hay transacciones registradas",
                    "Historial vacío",
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (SesionNoAutenticadaException e) {
            JOptionPane.showMessageDialog(this,
                e.getMessage(),
                "Sesión no activa",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // MÉTODOS DE UTILIDAD

    // Limpia todos los campos de texto.
    private void limpiarCampos() {
        if (txtNumeroTarjeta != null) {
            txtNumeroTarjeta.setText("");
        }
        if (txtPin != null) {
            txtPin.setText("");
        }
        if (txtMontoRetiro != null) {
            txtMontoRetiro.setText("");
        }
        if (txtMontoDeposito != null) {
            txtMontoDeposito.setText("");
        }
        if (txtMontoTransferencia != null) {
            txtMontoTransferencia.setText("");
        }
        if (txtCuentaDestino != null) {
            txtCuentaDestino.setText("");
        }
    }

    // Muestra la ventana de la aplicación.
    public void mostrar() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                setVisible(true);
            }
        });
    }
}
