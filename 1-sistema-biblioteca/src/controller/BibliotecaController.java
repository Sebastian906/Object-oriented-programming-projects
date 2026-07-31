package controller;

import exceptions.DatoInvalidoException;
import exceptions.EmailDuplicadoException;
import exceptions.LibroNoDisponibleException;
import exceptions.LibroNoEncontradoException;
import exceptions.LimitePrestamosException;
import exceptions.PrestamoNoEncontradoException;
import exceptions.PrestamoYaDevueltoException;
import exceptions.UsuarioInactivoException;
import exceptions.UsuarioNoEncontradoException;
import java.util.List;
import models.Biblioteca;
import models.Libro;
import models.Prestamo;
import models.Usuario;
import service.ValidadorService;
import view.BibliotecaVista;

/**
 * Controlador principal de la biblioteca.
 * Coordina las operaciones entre la vista y el modelo.
 * Conceptos POO aplicados:
 * - Clase
 * - Asociación (dependencias con Biblioteca y BibliotecaVista)
 * - Encapsulamiento
 * - Manejo de excepciones
 * - Patrón MVC (capa intermedia)
 */
public class BibliotecaController {
    
    // Dependencias (asociación por composición)
    @SuppressWarnings("FieldMayBeFinal")
    private Biblioteca biblioteca;
    @SuppressWarnings("FieldMayBeFinal")
    private BibliotecaVista vista;

    /**
     * Constructor del controlador.
     * @param biblioteca Instancia de la biblioteca (modelo)
     * @param vista      Instancia de la vista
     */
    public BibliotecaController(Biblioteca biblioteca, BibliotecaVista vista) {
        this.biblioteca = biblioteca;
        this.vista = vista;
    }

    // EJECUCIÓN PRINCIPAL
    /**
     * Método principal que ejecuta el bucle del sistema.
     * Implementa el flujo principal de la aplicación.
     */
    public void ejecutar() {
        boolean ejecutando = true;

        while (ejecutando) {
            int opcion = vista.mostrarMenuPrincipal();

            switch (opcion) {
                case 1:
                    gestionarLibros();
                    break;
                case 2:
                    gestionarUsuarios();
                    break;
                case 3:
                    gestionarPrestamos();
                    break;
                case 4:
                    verEstadisticas();
                    break;
                case 0:
                    ejecutando = false;
                    vista.mostrarMensaje("¡Hasta luego!");
                    break;
                default:
                    vista.mostrarError("Opción no válida. Intente de nuevo.");
            }
        }
    }

    // GESTIÓN DE LIBROS
    /**
     * Maneja el submenú de gestión de libros.
     */
    private void gestionarLibros() {
        boolean enMenu = true;

        while (enMenu) {
            int opcion = vista.mostrarMenuLibros();

            switch (opcion) {
                case 1:
                    agregarLibro();
                    break;
                case 2:
                    buscarLibroPorIsbn();
                    break;
                case 3:
                    buscarLibrosPorTitulo();
                    break;
                case 4:
                    listarTodosLosLibros();
                    break;
                case 0:
                    enMenu = false;
                    break;
                default:
                    vista.mostrarError("Opción no válida.");
            }
        }
    }

    /**
     * Agrega un nuevo libro al catálogo.
     */
    private void agregarLibro() {
        try {
            String[] datos = vista.formularioLibro();

            // Validar datos usando el servicio de validación
            ValidadorService.validarNoVacio("ISBN", datos[0]);
            ValidadorService.validarNoVacio("Título", datos[1]);
            ValidadorService.validarIsbn(datos[0]);

            int cantidad = ValidadorService.validarPositivo("Cantidad", Integer.parseInt(datos[6]));

            // Crear libro
            Libro libro = new Libro(
                datos[0],  // ISBN
                datos[1],  // Título
                datos[2],  // Autor
                datos[3],  // Editorial
                datos[4],  // Año
                datos[5],  // Categoría
                cantidad   // Cantidad
            );

            // Intentar agregar
            if (biblioteca.agregarLibro(libro)) {
                vista.mostrarExito("Libro agregado correctamente.");
            } else {
                vista.mostrarError("Ya existe un libro con el ISBN: " + datos[0]);
            }
        } catch (NumberFormatException e) {
            vista.mostrarError("La cantidad debe ser un número entero.");
        } catch (DatoInvalidoException e) {
            vista.mostrarError(e.getMessage());
        } catch (Exception e) {
            vista.mostrarError("Error al agregar libro: " + e.getMessage());
        }
    }

    /**
     * Busca un libro por su ISBN.
     */
    private void buscarLibroPorIsbn() {
        System.out.print("\nIngrese el ISBN: ");
        String isbn = vista.formularioDevolucion(); // Reutilizamos el método de lectura

        Libro libro = biblioteca.buscarLibroPorIsbn(isbn);

        if (libro != null) {
            vista.mostrarLibro(libro);
        } else {
            vista.mostrarError("No se encontró un libro con el ISBN: " + isbn);
        }
    }

    /**
     * Busca libros por título.
     */
    private void buscarLibrosPorTitulo() {
        System.out.print("\nIngrese el título a buscar: ");
        String titulo = vista.formularioDevolucion();

        List<Libro> resultados = biblioteca.buscarLibros(titulo);
        vista.mostrarListaLibros(resultados);
    }

    /**
     * Lista todos los libros del catálogo.
     */
    private void listarTodosLosLibros() {
        List<Libro> libros = biblioteca.getLibros();
        vista.mostrarListaLibros(libros);
    }

    // GESTIÓN DE USUARIOS    
    /**
     * Maneja el submenú de gestión de usuarios.
     */
    private void gestionarUsuarios() {
        boolean enMenu = true;

        while (enMenu) {
            int opcion = vista.mostrarMenuUsuarios();

            switch (opcion) {
                case 1:
                    registrarUsuario();
                    break;
                case 2:
                    buscarUsuarioPorId();
                    break;
                case 3:
                    buscarUsuarioPorEmail();
                    break;
                case 4:
                    listarTodosLosUsuarios();
                    break;
                case 0:
                    enMenu = false;
                    break;
                default:
                    vista.mostrarError("Opción no válida.");
            }
        }
    }

    /**
     * Registra un nuevo usuario.
     */
    @SuppressWarnings("UseSpecificCatch")
    private void registrarUsuario() {
        try {
            String[] datos = vista.formularioUsuario();

            // Validar datos usando el servicio de validación
            ValidadorService.validarNoVacio("Nombre", datos[0]);
            ValidadorService.validarEmail(datos[1]);
            ValidadorService.validarTelefono(datos[2]);

            // Verificar que no exista un usuario con el mismo email
            if (biblioteca.buscarUsuarioPorEmail(datos[1]) != null) {
                throw new EmailDuplicadoException(datos[1]);
            }

            // Generar ID único
            String id = biblioteca.generarIdUsuario();

            // Crear usuario
            Usuario usuario = new Usuario(
                id,        // ID generado
                datos[0],  // Nombre
                datos[1],  // Email
                datos[2]   // Teléfono
            );

            // Registrar usuario
            biblioteca.registrarUsuario(usuario);
            vista.mostrarExito("Usuario registrado correctamente. ID: " + id);
            
        } catch (DatoInvalidoException | EmailDuplicadoException e) {
            vista.mostrarError(e.getMessage());
        } catch (Exception e) {
            vista.mostrarError("Error al registrar usuario: " + e.getMessage());
        }
    }

    /**
     * Busca un usuario por su ID.
     */
    private void buscarUsuarioPorId() {
        System.out.print("\nIngrese el ID del usuario: ");
        String id = vista.formularioDevolucion();

        Usuario usuario = biblioteca.buscarUsuarioPorId(id);

        if (usuario != null) {
            vista.mostrarUsuario(usuario);
        } else {
            vista.mostrarError("No se encontró un usuario con el ID: " + id);
        }
    }

    /**
     * Busca un usuario por su email.
     */
    private void buscarUsuarioPorEmail() {
        System.out.print("\nIngrese el email: ");
        String email = vista.formularioDevolucion();

        Usuario usuario = biblioteca.buscarUsuarioPorEmail(email);

        if (usuario != null) {
            vista.mostrarUsuario(usuario);
        } else {
            vista.mostrarError("No se encontró un usuario con el email: " + email);
        }
    }

    /**
     * Lista todos los usuarios registrados.
     */
    private void listarTodosLosUsuarios() {
        List<Usuario> usuarios = biblioteca.getUsuarios();
        vista.mostrarListaUsuarios(usuarios);
    }

    // GESTIÓN DE PRÉSTAMOS    
    /**
     * Maneja el submenú de gestión de préstamos.
     */
    private void gestionarPrestamos() {
        boolean enMenu = true;

        while (enMenu) {
            int opcion = vista.mostrarMenuPrestamos();

            switch (opcion) {
                case 1:
                    registrarPrestamo();
                    break;
                case 2:
                    devolverLibro();
                    break;
                case 3:
                    listarPrestamosActivos();
                    break;
                case 4:
                    listarPrestamosPorUsuario();
                    break;
                case 0:
                    enMenu = false;
                    break;
                default:
                    vista.mostrarError("Opción no válida.");
            }
        }
    }

    /**
     * Registra un nuevo préstamo.
     */
    @SuppressWarnings("UseSpecificCatch")
    private void registrarPrestamo() {
        try {
            String[] datos = vista.formularioPrestamo();

            // Validar datos usando el servicio de validación
            ValidadorService.validarNoVacio("ISBN", datos[0]);
            ValidadorService.validarNoVacio("ID Usuario", datos[1]);
            ValidadorService.validarNoVacio("Días préstamo", datos[2]);

            int diasPrestamo = ValidadorService.validarPositivo("Días préstamo", 
                    Integer.parseInt(datos[2]));

            // Registrar préstamo
            Prestamo prestamo = biblioteca.prestarLibro(datos[0], datos[1], diasPrestamo);

            vista.mostrarExito("Préstamo registrado exitosamente.");
            vista.mostrarPrestamo(prestamo);
        } catch (NumberFormatException e) {
            vista.mostrarError("Los días de préstamo deben ser un número entero.");
        } catch (DatoInvalidoException | LibroNoEncontradoException | LibroNoDisponibleException | UsuarioNoEncontradoException | UsuarioInactivoException | LimitePrestamosException e) {
            vista.mostrarError(e.getMessage());
        } catch (Exception e) {
            vista.mostrarError("Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Devuelve un libro prestado.
     */
    @SuppressWarnings("UseSpecificCatch")
    private void devolverLibro() {
        try {
            String idPrestamo = vista.formularioDevolucion();

            String resultado = biblioteca.devolverLibro(idPrestamo);
            vista.mostrarExito(resultado);
        } catch (PrestamoNoEncontradoException | PrestamoYaDevueltoException e) {
            vista.mostrarError(e.getMessage());
        } catch (Exception e) {
            vista.mostrarError("Error inesperado: " + e.getMessage());
        }
    }

    /**
     * Lista todos los préstamos activos.
     */
    private void listarPrestamosActivos() {
        List<Prestamo> prestamos = biblioteca.listarPrestamosActivos();
        vista.mostrarListaPrestamos(prestamos);
    }

    /**
     * Lista los préstamos de un usuario específico.
     */
    private void listarPrestamosPorUsuario() {
        System.out.print("\nIngrese el ID del usuario: ");
        String idUsuario = vista.formularioDevolucion();

        List<Prestamo> prestamos = biblioteca.listarPrestamosPorUsuario(idUsuario);
        vista.mostrarListaPrestamos(prestamos);
    }

    // ESTADÍSTICAS   
    /**
     * Muestra las estadísticas de la biblioteca.
     */
    private void verEstadisticas() {
        String estadisticas = biblioteca.obtenerEstadisticas();
        vista.mostrarEstadisticas(estadisticas);
    }
}
