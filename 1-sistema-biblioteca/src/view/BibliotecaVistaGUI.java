package view;

import exceptions.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import models.Biblioteca;
import models.Libro;
import models.Prestamo;
import models.Usuario;

/**
 * Interfaz gráfica de la biblioteca usando Swing.
 * Implementa la vista en el patrón MVC.
 * Conceptos POO aplicados:
 * - Herencia (extends JFrame)
 * - Composición (contiene paneles, tablas, etc.)
 * - Implementación de interfaces (ActionListener implícito)
 * - Eventos y listeners
 */
public class BibliotecaVistaGUI extends JFrame {

    private final Biblioteca biblioteca;
    private final DateTimeFormatter formateadorFecha;

    // Componentes principales
    private JTabbedPane tabbedPane;
    
    // Tabs de Libros
    private JTable tablaLibros;
    private DefaultTableModel modeloTablaLibros;
    
    // Tabs de Usuarios
    private JTable tablaUsuarios;
    private DefaultTableModel modeloTablaUsuarios;
    
    // Tabs de Préstamos
    private JTable tablaPrestamos;
    private DefaultTableModel modeloTablaPrestamos;
    
    // Campos de formulario
    private JTextField txtIsbn, txtTitulo, txtAutor, txtEditorial, txtAnio, txtCategoria, txtCantidad;
    private JTextField txtNombre, txtEmail, txtTelefono;
    private JTextField txtIsbnPrestamo, txtIdUsuario, txtDiasPrestamo;
    private JTextField txtIdDevolucion;

    /**
     * Constructor de la vista gráfica.
     * @param biblioteca Instancia de la biblioteca (modelo)
     */
    public BibliotecaVistaGUI(Biblioteca biblioteca) {
        this.biblioteca = biblioteca;
        this.formateadorFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        configurarVentana();
        inicializarComponentes();
    }

    /**
     * Configura las propiedades básicas de la ventana.
     */
    private void configurarVentana() {
        setTitle("Sistema de Gestión de Biblioteca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null); // Centrar en pantalla
        setLayout(new BorderLayout());
    }

    /**
     * Inicializa todos los componentes de la interfaz.
     */
    private void inicializarComponentes() {
        // Crear panel de pestañas
        tabbedPane = new JTabbedPane();
        
        // Agregar pestañas
        tabbedPane.addTab("Libros", crearPanelLibros());
        tabbedPane.addTab("Usuarios", crearPanelUsuarios());
        tabbedPane.addTab("Préstamos", crearPanelPrestamos());
        tabbedPane.addTab("Estadísticas", crearPanelEstadisticas());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        // Barra de estado
        add(crearBarraEstado(), BorderLayout.SOUTH);
    }

    // PESTAÑA DE LIBROS
    /**
     * Crea el panel de gestión de libros.
     * @return Panel con formulario y tabla de libros
     */
    private JPanel crearPanelLibros() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridLayout(4, 4, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Agregar Libro"));
        
        txtIsbn = new JTextField();
        txtTitulo = new JTextField();
        txtAutor = new JTextField();
        txtEditorial = new JTextField();
        txtAnio = new JTextField();
        txtCategoria = new JTextField();
        txtCantidad = new JTextField();
        
        panelFormulario.add(new JLabel("ISBN:"));
        panelFormulario.add(txtIsbn);
        panelFormulario.add(new JLabel("Título:"));
        panelFormulario.add(txtTitulo);
        panelFormulario.add(new JLabel("Autor:"));
        panelFormulario.add(txtAutor);
        panelFormulario.add(new JLabel("Editorial:"));
        panelFormulario.add(txtEditorial);
        panelFormulario.add(new JLabel("Año:"));
        panelFormulario.add(txtAnio);
        panelFormulario.add(new JLabel("Categoría:"));
        panelFormulario.add(txtCategoria);
        panelFormulario.add(new JLabel("Cantidad:"));
        panelFormulario.add(txtCantidad);
        
        JButton btnAgregar = new JButton("Agregar Libro");
        btnAgregar.addActionListener(e -> agregarLibro());
        panelFormulario.add(btnAgregar);
        
        panel.add(panelFormulario, BorderLayout.NORTH);
        
        // Tabla de libros
        String[] columnas = {"ISBN", "Título", "Autor", "Editorial", "Año", "Categoría", "Disponible"};
        modeloTablaLibros = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No editable
            }
        };
        tablaLibros = new JTable(modeloTablaLibros);
        JScrollPane scrollPane = new JScrollPane(tablaLibros);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Catálogo de Libros"));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Botones de acción
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(e -> actualizarListaLibros());
        JButton btnBuscar = new JButton("Buscar por Título");
        btnBuscar.addActionListener(e -> buscarLibrosPorTitulo());
        
        panelBotones.add(btnActualizar);
        panelBotones.add(btnBuscar);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        return panel;
    }

    /**
     * Agrega un libro desde el formulario.
     */
    private void agregarLibro() {
        try {
            // Validar campos
            if (txtIsbn.getText().isEmpty() || txtTitulo.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "El ISBN y el Título son obligatorios.", 
                    "Error de Validación", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int cantidad = Integer.parseInt(txtCantidad.getText());
            
            // Crear libro
            Libro libro = new Libro(
                txtIsbn.getText(),
                txtTitulo.getText(),
                txtAutor.getText(),
                txtEditorial.getText(),
                txtAnio.getText(),
                txtCategoria.getText(),
                cantidad
            );
            
            // Agregar al modelo
            if (biblioteca.agregarLibro(libro)) {
                JOptionPane.showMessageDialog(this, 
                    "Libro agregado correctamente.", 
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                actualizarListaLibros();
                limpiarFormularioLibro();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Ya existe un libro con el ISBN: " + txtIsbn.getText(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "La cantidad debe ser un número entero.", 
                "Error de Formato", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza la tabla de libros con los datos actuales.
     */
    private void actualizarListaLibros() {
        modeloTablaLibros.setRowCount(0); // Limpiar tabla
        
        for (Libro libro : biblioteca.getLibros()) {
            Object[] fila = {
                libro.getIsbn(),
                libro.getTitulo(),
                libro.getAutor(),
                libro.getEditorial(),
                libro.getAnioPublicacion(),
                libro.getCategoria(),
                libro.getCantidadDisponible() + " de " + libro.getCantidadTotal()
            };
            modeloTablaLibros.addRow(fila);
        }
    }

    /**
     * Busca libros por título.
     */
    private void buscarLibrosPorTitulo() {
        String titulo = JOptionPane.showInputDialog(this, 
            "Ingrese el título a buscar:", 
            "Buscar Libros", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (titulo != null && !titulo.trim().isEmpty()) {
            List<Libro> resultados = biblioteca.buscarLibros(titulo);
            
            modeloTablaLibros.setRowCount(0);
            for (Libro libro : resultados) {
                Object[] fila = {
                    libro.getIsbn(),
                    libro.getTitulo(),
                    libro.getAutor(),
                    libro.getEditorial(),
                    libro.getAnioPublicacion(),
                    libro.getCategoria(),
                    libro.getCantidadDisponible() + " de " + libro.getCantidadTotal()
                };
                modeloTablaLibros.addRow(fila);
            }
            
            if (resultados.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "No se encontraron libros con ese título.", 
                    "Resultado de Búsqueda", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    /**
     * Limpia el formulario de libros.
     */
    private void limpiarFormularioLibro() {
        txtIsbn.setText("");
        txtTitulo.setText("");
        txtAutor.setText("");
        txtEditorial.setText("");
        txtAnio.setText("");
        txtCategoria.setText("");
        txtCantidad.setText("");
    }

    // PESTAÑA DE USUARIOS
    /**
     * Crea el panel de gestión de usuarios.
     * @return Panel con formulario y tabla de usuarios
     */
    private JPanel crearPanelUsuarios() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridLayout(2, 3, 5, 5));
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Registrar Usuario"));
        
        txtNombre = new JTextField();
        txtEmail = new JTextField();
        txtTelefono = new JTextField();
        
        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);
        panelFormulario.add(new JLabel("Email:"));
        panelFormulario.add(txtEmail);
        panelFormulario.add(new JLabel("Teléfono:"));
        panelFormulario.add(txtTelefono);
        
        JButton btnRegistrar = new JButton("Registrar");
        btnRegistrar.addActionListener(e -> registrarUsuario());
        panelFormulario.add(btnRegistrar);
        
        panel.add(panelFormulario, BorderLayout.NORTH);
        
        // Tabla de usuarios
        String[] columnas = {"ID", "Nombre", "Email", "Teléfono", "Registro", "Activo"};
        modeloTablaUsuarios = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaUsuarios = new JTable(modeloTablaUsuarios);
        JScrollPane scrollPane = new JScrollPane(tablaUsuarios);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Usuarios Registrados"));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Botón actualizar
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(e -> actualizarListaUsuarios());
        panelBotones.add(btnActualizar);
        panel.add(panelBotones, BorderLayout.SOUTH);
        
        return panel;
    }

    /**
     * Registra un usuario desde el formulario.
     */
    private void registrarUsuario() {
        try {
            // Validar campos
            if (txtNombre.getText().isEmpty() || txtEmail.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "El Nombre y el Email son obligatorios.", 
                    "Error de Validación", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Verificar email duplicado
            if (biblioteca.buscarUsuarioPorEmail(txtEmail.getText()) != null) {
                throw new EmailDuplicadoException(txtEmail.getText());
            }
            
            // Generar ID
            String id = biblioteca.generarIdUsuario();
            
            // Crear usuario
            Usuario usuario = new Usuario(
                id,
                txtNombre.getText(),
                txtEmail.getText(),
                txtTelefono.getText()
            );
            
            // Registrar
            biblioteca.registrarUsuario(usuario);
            
            JOptionPane.showMessageDialog(this, 
                "Usuario registrado correctamente.\nID: " + id, 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            actualizarListaUsuarios();
            limpiarFormularioUsuario();
            
        } catch (EmailDuplicadoException e) {
            JOptionPane.showMessageDialog(this, 
                e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza la tabla de usuarios.
     */
    private void actualizarListaUsuarios() {
        modeloTablaUsuarios.setRowCount(0);
        
        for (Usuario usuario : biblioteca.getUsuarios()) {
            Object[] fila = {
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getTelefono(),
                usuario.getFechaRegistro().format(formateadorFecha),
                usuario.estaActivo() ? "Sí" : "No"
            };
            modeloTablaUsuarios.addRow(fila);
        }
    }

    /**
     * Limpia el formulario de usuarios.
     */
    private void limpiarFormularioUsuario() {
        txtNombre.setText("");
        txtEmail.setText("");
        txtTelefono.setText("");
    }

    // PESTAÑA DE PRÉSTAMOS
    /**
     * Crea el panel de gestión de préstamos.
     * @return Panel con formularios y tabla de préstamos
     */
    private JPanel crearPanelPrestamos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de formulario de préstamo
        JPanel panelPrestamo = new JPanel(new GridLayout(2, 3, 5, 5));
        panelPrestamo.setBorder(BorderFactory.createTitledBorder("Registrar Préstamo"));
        
        txtIsbnPrestamo = new JTextField();
        txtIdUsuario = new JTextField();
        txtDiasPrestamo = new JTextField();
        
        panelPrestamo.add(new JLabel("ISBN Libro:"));
        panelPrestamo.add(txtIsbnPrestamo);
        panelPrestamo.add(new JLabel("ID Usuario:"));
        panelPrestamo.add(txtIdUsuario);
        panelPrestamo.add(new JLabel("Días Préstamo:"));
        panelPrestamo.add(txtDiasPrestamo);
        
        JButton btnPrestar = new JButton("Registrar Préstamo");
        btnPrestar.addActionListener(e -> registrarPrestamo());
        panelPrestamo.add(btnPrestar);
        
        panel.add(panelPrestamo, BorderLayout.NORTH);
        
        // Tabla de préstamos
        String[] columnas = {"ID", "Libro", "Usuario", "Fecha Préstamo", "Devolución Esperada", "Estado"};
        modeloTablaPrestamos = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaPrestamos = new JTable(modeloTablaPrestamos);
        JScrollPane scrollPane = new JScrollPane(tablaPrestamos);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Préstamos Activos"));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel de devolución
        JPanel panelDevolucion = new JPanel(new FlowLayout());
        panelDevolucion.setBorder(BorderFactory.createTitledBorder("Devolver Libro"));
        
        txtIdDevolucion = new JTextField(10);
        JButton btnDevolver = new JButton("Devolver");
        btnDevolver.addActionListener(e -> devolverLibro());
        
        panelDevolucion.add(new JLabel("ID Préstamo:"));
        panelDevolucion.add(txtIdDevolucion);
        panelDevolucion.add(btnDevolver);
        
        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.addActionListener(e -> actualizarListaPrestamos());
        panelDevolucion.add(btnActualizar);
        
        panel.add(panelDevolucion, BorderLayout.SOUTH);
        
        return panel;
    }

    /**
     * Registra un préstamo desde el formulario.
     */
    private void registrarPrestamo() {
        try {
            // Validar campos
            if (txtIsbnPrestamo.getText().isEmpty() || 
                txtIdUsuario.getText().isEmpty() || 
                txtDiasPrestamo.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Todos los campos son obligatorios.", 
                    "Error de Validación", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            int diasPrestamo = Integer.parseInt(txtDiasPrestamo.getText());
            if (diasPrestamo <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Los días de préstamo deben ser mayores a 0.", 
                    "Error de Validación", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Registrar préstamo
            Prestamo prestamo = biblioteca.prestarLibro(
                txtIsbnPrestamo.getText(),
                txtIdUsuario.getText(),
                diasPrestamo
            );
            
            JOptionPane.showMessageDialog(this, 
                "Préstamo registrado exitosamente.\nID: " + prestamo.getId(), 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            actualizarListaPrestamos();
            txtIsbnPrestamo.setText("");
            txtIdUsuario.setText("");
            txtDiasPrestamo.setText("");
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Los días de préstamo deben ser un número entero.", 
                "Error de Formato", 
                JOptionPane.ERROR_MESSAGE);
        } catch (LibroNoEncontradoException | LibroNoDisponibleException | 
                 UsuarioNoEncontradoException | UsuarioInactivoException | 
                 LimitePrestamosException e) {
            JOptionPane.showMessageDialog(this, 
                e.getMessage(), 
                "Error de Negocio", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Devuelve un libro.
     */
    private void devolverLibro() {
        try {
            String idPrestamo = txtIdDevolucion.getText();
            
            if (idPrestamo.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Ingrese el ID del préstamo.", 
                    "Error de Validación", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            String resultado = biblioteca.devolverLibro(idPrestamo);
            
            JOptionPane.showMessageDialog(this, 
                resultado, 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            actualizarListaPrestamos();
            txtIdDevolucion.setText("");
            
        } catch (PrestamoNoEncontradoException | PrestamoYaDevueltoException e) {
            JOptionPane.showMessageDialog(this, 
                e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Actualiza la tabla de préstamos.
     */
    private void actualizarListaPrestamos() {
        modeloTablaPrestamos.setRowCount(0);
        
        for (Prestamo prestamo : biblioteca.listarPrestamosActivos()) {
            Object[] fila = {
                prestamo.getId(),
                prestamo.getLibro().getTitulo(),
                prestamo.getUsuario().getNombre(),
                prestamo.getFechaPrestamo().format(formateadorFecha),
                prestamo.getFechaDevolucionEsperada().format(formateadorFecha),
                prestamo.estaVencido() ? "VENCIDO" : "Activo"
            };
            modeloTablaPrestamos.addRow(fila);
        }
    }

    // PESTAÑA DE ESTADÍSTICAS
    /**
     * Crea el panel de estadísticas.
     * @return Panel con estadísticas de la biblioteca
     */
    private JPanel crearPanelEstadisticas() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextArea areaEstadisticas = new JTextArea();
        areaEstadisticas.setEditable(false);
        areaEstadisticas.setFont(new Font("Monospaced", Font.PLAIN, 14));
        
        JButton btnActualizar = new JButton("Actualizar Estadísticas");
        btnActualizar.addActionListener(e -> {
            areaEstadisticas.setText(biblioteca.obtenerEstadisticas());
        });
        
        panel.add(new JScrollPane(areaEstadisticas), BorderLayout.CENTER);
        panel.add(btnActualizar, BorderLayout.SOUTH);
        
        return panel;
    }

    // BARRA DE ESTADO
    /**
     * Crea la barra de estado inferior.
     * @return Panel con información de estado
     */
    private JPanel crearBarraEstado() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT));
        barra.setBorder(BorderFactory.createEtchedBorder());
        
        JLabel lblEstado = new JLabel("Sistema de Gestión de Biblioteca v1.0 | Libros: " + 
            biblioteca.getLibros().size() + " | Usuarios: " + 
            biblioteca.getUsuarios().size() + " | Préstamos activos: " + 
            biblioteca.listarPrestamosActivos().size());
        
        barra.add(lblEstado);
        return barra;
    }

    // ==================== MÉTODOS PÚBLICOS ====================
    
    /**
     * Muestra la ventana.
     */
    public void mostrar() {
        SwingUtilities.invokeLater(() -> {
            setVisible(true);
            actualizarListaLibros();
            actualizarListaUsuarios();
            actualizarListaPrestamos();
        });
    }

    /**
     * Cierra la ventana.
     */
    public void cerrar() {
        dispose();
    }
}
