package view;

import java.time.LocalDate;
import java.util.List;
import utils.ConsoleUtil;

/**
 * Vista principal de la biblioteca.
 * Maneja la entrada y salida de datos por consola.
 * Conceptos POO aplicados:
 * - Clase
 * - Encapsulamiento
 * - Uso de Scanner (entrada de datos)
 * - Uso de System.out (salida de datos)
 */
public class BibliotecaVista {

    // MENÚS
    /**
     * Muestra el menú principal y retorna la opción seleccionada.
     * @return Opción seleccionada por el usuario
     */
    public int mostrarMenuPrincipal() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║     SISTEMA DE GESTIÓN DE BIBLIOTECA    ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  1. Gestión de Libros                   ║");
        System.out.println("║  2. Gestión de Usuarios                 ║");
        System.out.println("║  3. Gestión de Préstamos                ║");
        System.out.println("║  4. Ver Estadísticas                    ║");
        System.out.println("║  0. Salir                               ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.print("Seleccione una opción: ");
        return ConsoleUtil.leerEntero();
    }

    /**
     * Muestra el menú de libros y retorna la opción seleccionada.
     * @return Opción seleccionada
     */
    public int mostrarMenuLibros() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║          GESTIÓN DE LIBROS               ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  1. Agregar libro                        ║");
        System.out.println("║  2. Buscar libro por ISBN                ║");
        System.out.println("║  3. Buscar libros por título             ║");
        System.out.println("║  4. Listar todos los libros              ║");
        System.out.println("║  0. Volver al menú principal             ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.print("Seleccione una opción: ");
        return ConsoleUtil.leerEntero();
    }

    /**
     * Muestra el menú de usuarios y retorna la opción seleccionada.
     * @return Opción seleccionada
     */
    public int mostrarMenuUsuarios() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║         GESTIÓN DE USUARIOS              ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  1. Registrar usuario                    ║");
        System.out.println("║  2. Buscar usuario por ID               ║");
        System.out.println("║  3. Buscar usuario por email            ║");
        System.out.println("║  4. Listar todos los usuarios            ║");
        System.out.println("║  0. Volver al menú principal             ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.print("Seleccione una opción: ");
        return ConsoleUtil.leerEntero();
    }

    /**
     * Muestra el menú de préstamos y retorna la opción seleccionada.
     * @return Opción seleccionada
     */
    public int mostrarMenuPrestamos() {
        System.out.println("\n╔══════════════════════════════════════════╗");
        System.out.println("║        GESTIÓN DE PRÉSTAMOS              ║");
        System.out.println("╠══════════════════════════════════════════╣");
        System.out.println("║  1. Registrar préstamo                   ║");
        System.out.println("║  2. Devolver libro                       ║");
        System.out.println("║  3. Listar préstamos activos             ║");
        System.out.println("║  4. Listar préstamos por usuario         ║");
        System.out.println("║  0. Volver al menú principal             ║");
        System.out.println("╚══════════════════════════════════════════╝");
        System.out.print("Seleccione una opción: ");
        return ConsoleUtil.leerEntero();
    }

    // FORMULARIOS
    /**
     * Muestra el formulario para agregar un libro.
     * Retorna los datos como un arreglo de Strings.
     * @return Arreglo con [isbn, titulo, autor, editorial, anio, categoria, cantidad]
     */
    public String[] formularioLibro() {
        System.out.println("\n--- AGREGAR NUEVO LIBRO ---");
        
        String[] datos = new String[7];
        
        System.out.print("ISBN: ");
        datos[0] = ConsoleUtil.leerTexto();

        System.out.print("Título: ");
        datos[1] = ConsoleUtil.leerTexto();

        System.out.print("Autor: ");
        datos[2] = ConsoleUtil.leerTexto();

        System.out.print("Editorial: ");
        datos[3] = ConsoleUtil.leerTexto();

        System.out.print("Año de publicación: ");
        datos[4] = ConsoleUtil.leerTexto();

        System.out.print("Categoría: ");
        datos[5] = ConsoleUtil.leerTexto();

        System.out.print("Cantidad de ejemplares: ");
        datos[6] = ConsoleUtil.leerTexto();

        return datos;
    }

    /**
     * Muestra el formulario para registrar un usuario.
     * Retorna los datos como un arreglo de Strings.
     * @return Arreglo con [nombre, email, telefono]
     */
    public String[] formularioUsuario() {
        System.out.println("\n--- REGISTRAR NUEVO USUARIO ---");
        
        String[] datos = new String[3];
        
        System.out.print("Nombre completo: ");
        datos[0] = ConsoleUtil.leerTexto();
        
        System.out.print("Email: ");
        datos[1] = ConsoleUtil.leerTexto();
        
        System.out.print("Teléfono: ");
        datos[2] = ConsoleUtil.leerTexto();
        
        return datos;
    }

    /**
     * Muestra el formulario para registrar un préstamo.
     * @return Arreglo con [isbn, idUsuario, diasPrestamo]
     */
    public String[] formularioPrestamo() {
        System.out.println("\n--- REGISTRAR PRÉSTAMO ---");
        
        String[] datos = new String[3];
        
        System.out.print("ISBN del libro: ");
        datos[0] = ConsoleUtil.leerTexto();
        
        System.out.print("ID del usuario: ");
        datos[1] = ConsoleUtil.leerTexto();
        
        System.out.print("Días de préstamo: ");
        datos[2] = ConsoleUtil.leerTexto();
        
        return datos;
    }

    /**
     * Muestra el formulario para devolver un libro.
     * @return ID del préstamo a devolver
     */
    public String formularioDevolucion() {
        System.out.println("\n--- DEVOLVER LIBRO ---");
        System.out.print("ID del préstamo: ");
        return ConsoleUtil.leerTexto();
    }

    // MOSTRAR DATOS
    /**
     * Muestra un libro en formato legible.
     * @param libro Libro a mostrar
     */
    public void mostrarLibro(Object libro) {
        System.out.println(libro);
    }

    /**
     * Muestra una lista de libros.
     * @param libros Lista de libros a mostrar
     */
    public void mostrarListaLibros(List<?> libros) {
        if (libros.isEmpty()) {
            ConsoleUtil.mostrarMensaje("No se encontraron libros.");
            return;
        }

        System.out.println("\n--- LISTA DE LIBROS ---");
        for (Object libro : libros) {
            System.out.println(libro);
            System.out.println("---");
        }
    }

    /**
     * Muestra un usuario en formato legible.
     * @param usuario Usuario a mostrar
     */
    public void mostrarUsuario(Object usuario) {
        System.out.println(usuario);
    }

    /**
     * Muestra una lista de usuarios.
     * @param usuarios Lista de usuarios a mostrar
     */
    public void mostrarListaUsuarios(List<?> usuarios) {
        if (usuarios.isEmpty()) {
            ConsoleUtil.mostrarMensaje("No se encontraron usuarios.");
            return;
        }

        System.out.println("\n--- LISTA DE USUARIOS ---");
        for (Object usuario : usuarios) {
            System.out.println(usuario);
            System.out.println("---");
        }
    }

    /**
     * Muestra un préstamo en formato legible.
     * @param prestamo Préstamo a mostrar
     */
    public void mostrarPrestamo(Object prestamo) {
        System.out.println(prestamo);
    }

    /**
     * Muestra una lista de préstamos.
     * @param prestamos Lista de préstamos a mostrar
     */
    public void mostrarListaPrestamos(List<?> prestamos) {
        if (prestamos.isEmpty()) {
            ConsoleUtil.mostrarMensaje("No se encontraron préstamos.");
            return;
        }

        System.out.println("\n--- LISTA DE PRÉSTAMOS ---");
        for (Object prestamo : prestamos) {
            System.out.println(prestamo);
            System.out.println("---");
        }
    }

    /**
     * Muestra las estadísticas de la biblioteca.
     * @param estadísticas String con las estadísticas
     */
    public void mostrarEstadisticas(String estadisticas) {
        System.out.println(estadisticas);
    }

    // MENSAJES    
    /**
     * Muestra un mensaje de éxito.
     * @param mensaje Mensaje a mostrar
     */
    public void mostrarExito(String mensaje) {
        ConsoleUtil.mostrarExito(mensaje);
    }

    /**
     * Muestra un mensaje de error.
     * @param mensaje Mensaje a mostrar
     */
    public void mostrarError(String mensaje) {
        ConsoleUtil.mostrarError(mensaje);
    }

    /**
     * Muestra un mensaje informativo.
     * @param mensaje Mensaje a mostrar
     */
    public void mostrarMensaje(String mensaje) {
        ConsoleUtil.mostrarMensaje(mensaje);
    }

    /**
     * Pide confirmación al usuario.
     * @param mensaje Mensaje de confirmación
     * @return true si el usuario confirma
     */
    public boolean confirmar(String mensaje) {
        return ConsoleUtil.confirmar(mensaje);
    }

    // UTILIDADES DE ENTRADA
    /**
     * Lee una fecha del teclado en formato dd/MM/yyyy.
     * @return LocalDate con la fecha ingresada
     */
    public LocalDate leerFecha() {
        return ConsoleUtil.leerFecha("Fecha (dd/MM/yyyy): ");
    }

    /**
     * Cierra el scanner.
     */
    public void cerrar() {
        ConsoleUtil.cerrar();
    }
}