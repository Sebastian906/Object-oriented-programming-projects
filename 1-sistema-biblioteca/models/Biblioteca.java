import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase principal que gestiona la biblioteca.
 * Centraliza las colecciones y operaciones del sistema.
 * Conceptos POO aplicados:
 * - Clase
 * - Composición (contiene listas de entidades)
 * - Asociación con Libro, Usuario y Prestamo
 * - Encapsulamiento de colecciones
 * - Uso de ArrayList (colecciones)
 * - Manejo de excepciones
 */
public class Biblioteca {
    // Atributos (colecciones encapsuladas)
    @SuppressWarnings("FieldMayBeFinal")
    private ArrayList<Libro> libros;
    @SuppressWarnings("FieldMayBeFinal")
    private ArrayList<Usuario> usuarios;
    @SuppressWarnings("FieldMayBeFinal")
    private ArrayList<Prestamo> prestamos;
    // Contadores para generación de IDs
    private int contadorUsuarios;
    private int contadorPrestamos;

    /**
     * Constructor de la clase Biblioteca.
     * Inicializa las colecciones vacías.
     */
    public Biblioteca() {
        this.libros = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.prestamos = new ArrayList<>();
        this.contadorUsuarios = 0;
        this.contadorPrestamos = 0;
    }

    // GESTIÓN DE LIBROS
    /**
     * Agrega un libro al catálogo de la biblioteca.
     * @param libro Libro a agregar
     * @return true si se agregó correctamente, false si ya existe un libro con ese ISBN
     */
    public boolean agregarLibro(Libro libro) {
        // Verificar que no exista un libro con el mismo ISBN
        if (buscarLibroPorIsbn(libro.getIsbn()) != null) {
            return false;
        }
        libros.add(libro);
        return true;
    }

    /**
     * Busca un libro por su ISBN.
     * @param isbn ISBN a buscar
     * @return El libro encontrado o null si no existe
     */
    public Libro buscarLibroPorIsbn(String isbn) {
        for (Libro libro : libros) {
            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }
        return null;
    }

    /**
     * Busca libros por título (búsqueda parcial, case-insensitive).
     * @param titulo Título a buscar
     * @return Lista de libros que coinciden con la búsqueda
     */
    public List<Libro> buscarLibros(String titulo) {
        List<Libro> resultados = new ArrayList<>();
        String tituloLower = titulo.toLowerCase();
        
        for (Libro libro : libros) {
            if (libro.getTitulo().toLowerCase().contains(tituloLower)) {
                resultados.add(libro);
            }
        }
        return resultados;
    }

    /**
     * Obtiene todos los libros del catálogo.
     * @return Lista con todos los libros
     */
    public ArrayList<Libro> getLibros() {
        return libros;
    }

    // GESTIÓN DE USUARIOS
    /**
     * Registra un nuevo usuario en la biblioteca.
     * @param usuario Usuario a registrar
     * @return true si se registró correctamente, false si ya existe un usuario con ese email
     */
    public boolean registrarUsuario(Usuario usuario) {
        // Verificar que no exista un usuario con el mismo email
        if (buscarUsuarioPorEmail(usuario.getEmail()) != null) {
            return false;
        }
        usuarios.add(usuario);
        return true;
    }

    /**
     * Busca un usuario por su ID.
     * @param id ID a buscar
     * @return El usuario encontrado o null si no existe
     */
    public Usuario buscarUsuarioPorId(String id) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equals(id)) {
                return usuario;
            }
        }
        return null;
    }

    /**
     * Busca un usuario por su email.
     * @param email Email a buscar
     * @return El usuario encontrado o null si no existe
     */
    public Usuario buscarUsuarioPorEmail(String email) {
        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equals(email)) {
                return usuario;
            }
        }
        return null;
    }

    /**
     * Obtiene todos los usuarios registrados.
     * @return Lista con todos los usuarios
     */
    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    // GESTIÓN DE PRÉSTAMOS
    /**
     * Registra un nuevo préstamo de libro.
     * @param isbn              ISBN del libro a prestar
     * @param idUsuario         ID del usuario que recibe el préstamo
     * @param diasPrestamo      Número de días para devolución
     * @return El préstamo creado, o null si hubo un error
     * @throws Exception si el libro no existe, no está disponible o el usuario no existe
     */
    public Prestamo prestarLibro(String isbn, String idUsuario, int diasPrestamo) throws Exception {
        // 1. Buscar el libro
        Libro libro = buscarLibroPorIsbn(isbn);
        if (libro == null) {
            throw new Exception("No se encontró un libro con el ISBN: " + isbn);
        }

        // 2. Verificar disponibilidad
        if (!libro.estaDisponible()) {
            throw new Exception("El libro '" + libro.getTitulo() + "' no tiene ejemplares disponibles.");
        }

        // 3. Buscar el usuario
        Usuario usuario = buscarUsuarioPorId(idUsuario);
        if (usuario == null) {
            throw new Exception("No se encontró un usuario con el ID: " + idUsuario);
        }

        // 4. Verificar que el usuario esté activo
        if (!usuario.estaActivo()) {
            throw new Exception("El usuario " + usuario.getNombre() + " no está activo.");
        }

        // 5. Verificar límite de préstamos activos (máximo 3)
        int prestamosActivos = contarPrestamosActivos(idUsuario);
        if (prestamosActivos >= 3) {
            throw new Exception("El usuario " + usuario.getNombre() + " ya tiene el máximo de 3 préstamos activos.");
        }

        // 6. Crear el préstamo
        contadorPrestamos++;
        String idPrestamo = "P" + contadorPrestamos;
        LocalDate fechaDevolucion = LocalDate.now().plusDays(diasPrestamo);
        
        Prestamo prestamo = new Prestamo(idPrestamo, libro, usuario, fechaDevolucion);
        
        // 7. Actualizar disponibilidad del libro
        libro.prestar();
        
        // 8. Guardar el préstamo
        prestamos.add(prestamo);
        
        return prestamo;
    }

    /**
     * Registra la devolución de un libro.
     * @param idPrestamo ID del préstamo a devolver
     * @return Mensaje con el resultado de la devolución
     * @throws Exception si el préstamo no existe o ya fue devuelto
     */
    public String devolverLibro(String idPrestamo) throws Exception {
        // 1. Buscar el préstamo
        Prestamo prestamo = buscarPrestamoPorId(idPrestamo);
        if (prestamo == null) {
            throw new Exception("No se encontró un préstamo con el ID: " + idPrestamo);
        }

        // 2. Verificar que esté activo
        if (!prestamo.estaActivo()) {
            throw new Exception("El préstamo " + idPrestamo + " ya fue devuelto.");
        }

        // 3. Devolver el libro (aumentar disponibilidad)
        prestamo.getLibro().devolver();

        // 4. Registrar la devolución en el préstamo
        return prestamo.devolver();
    }

    /**
     * Cuenta los préstamos activos de un usuario.
     * @param idUsuario ID del usuario
     * @return Número de préstamos activos
     */
    private int contarPrestamosActivos(String idUsuario) {
        int contador = 0;
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getUsuario().getId().equals(idUsuario) && prestamo.estaActivo()) {
                contador++;
            }
        }
        return contador;
    }

    /**
     * Busca un préstamo por su ID.
     * @param id ID a buscar
     * @return El préstamo encontrado o null si no existe
     */
    public Prestamo buscarPrestamoPorId(String id) {
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getId().equals(id)) {
                return prestamo;
            }
        }
        return null;
    }

    /**
     * Lista todos los préstamos activos.
     * @return Lista de préstamos con estado ACTIVO
     */
    public List<Prestamo> listarPrestamosActivos() {
        List<Prestamo> activos = new ArrayList<>();
        for (Prestamo prestamo : prestamos) {
            if (prestamo.estaActivo()) {
                activos.add(prestamo);
            }
        }
        return activos;
    }

    /**
     * Lista todos los préstamos de un usuario.
     * @param idUsuario ID del usuario
     * @return Lista de préstamos del usuario
     */
    public List<Prestamo> listarPrestamosPorUsuario(String idUsuario) {
        List<Prestamo> usuarioPrestamos = new ArrayList<>();
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getUsuario().getId().equals(idUsuario)) {
                usuarioPrestamos.add(prestamo);
            }
        }
        return usuarioPrestamos;
    }

    /**
     * Obtiene todos los préstamos.
     * @return Lista con todos los préstamos
     */
    public ArrayList<Prestamo> getPrestamos() {
        return prestamos;
    }

    // UTILIDADES
    /**
     * Genera el siguiente ID de usuario.
     * @return ID de usuario único
     */
    public String generarIdUsuario() {
        contadorUsuarios++;
        return "U" + contadorUsuarios;
    }

    /**
     * Marca los préstamos vencidos.
     * Método que debería ejecutarse periódicamente.
     */
    public void actualizarPrestamosVencidos() {
        for (Prestamo prestamo : prestamos) {
            if (prestamo.estaActivo() && prestamo.estaVencido()) {
                // Aquí se podría marcar como VENCIDO si tuviéramos setter para estado
                // Por ahora solo se detecta con estaVencido()
            }
        }
    }

    /**
     * Obtiene estadísticas de la biblioteca.
     * @return String con estadísticas
     */
    public String obtenerEstadisticas() {
        int totalLibros = libros.size();
        int totalUsuarios = usuarios.size();
        int totalPrestamos = prestamos.size();
        int prestamosActivos = listarPrestamosActivos().size();
        
        return """
               ESTADISTICAS DE LA BIBLIOTECA
               Total de libros: """ + totalLibros +
               "\nTotal de usuarios: " + totalUsuarios +
               "\nTotal de préstamos: " + totalPrestamos +
               "\nPréstamos activos: " + prestamosActivos +
               "\n";
    }
}
