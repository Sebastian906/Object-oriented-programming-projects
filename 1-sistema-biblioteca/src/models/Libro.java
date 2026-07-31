package models;

/**
 * Representa un libro en el catálogo de la biblioteca.
 * Conceptos POO aplicados:
 * - Clase (definición de un objeto)
 * - Atributos privados (encapsulamiento)
 * - Constructores (inicialización de objetos)
 * - Métodos de acceso (getters y setters)
 * - Métodos de comportamiento (acciones que puede realizar un objeto)
 * - Sobreescritura de métodos (toString, equals, hashCode)
 */
public class Libro {
    // Atributos (encapsulamiento con modificadores private)
    @SuppressWarnings("FieldMayBeFinal")
    private String isbn;
    private String titulo;
    private String autor;
    private String editorial;
    private String anioPublicacion;
    private String categoria;
    private int cantidadTotal;
    private int cantidadDisponible;

    /**
     * Constructor de la clase Libro.
     * @param isbn               ISBN único del libro
     * @param titulo             Título del Libro
     * @param autor              Autor del libro
     * @param editorial          Editorial del libro
     * @param anioPublicacion    Año de publicación del libro
     * @param categoria          Categoría/género del libro
     * @param cantidadTotal      Cantidad total de ejemplares del libro
     */
    public Libro(String isbn, String titulo, String autor, String editorial, 
                 String anioPublicacion, String categoria, int cantidadTotal) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.anioPublicacion = anioPublicacion;
        this.categoria = categoria;
        this.cantidadTotal = cantidadTotal;
        this.cantidadDisponible = cantidadTotal; // Inicialmente todos disponibles
    }

    // GETTERS
    public String getIsbn() {
        return isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getEditorial() {
        return editorial;
    }

    public String getAnioPublicacion() {
        return anioPublicacion;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getCantidadTotal() {
        return cantidadTotal;
    }

    public int getCantidadDisponible() {
        return cantidadDisponible;
    }

    // SETTERS
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public void setAnioPublicacion(String anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setCantidadTotal(int cantidadTotal) {
        this.cantidadTotal = cantidadTotal;
        // Ajustar cantidad disponible si se reduce el total
        if (cantidadDisponible > cantidadTotal) {
            cantidadDisponible = cantidadTotal;
        }
    }

    // MÉTODOS DE COMPORTAMIENTO
    /** 
     * Registra el préstamo de un ejemplo del libro.
     * Disminuye la cantidad disponible en 1.
     * @return true si se pudo prestar, false si no hay ejemplares disponibles.
     */
    public boolean prestar() {
        if (cantidadDisponible > 0) {
            cantidadDisponible--;
            return true;
        }
        return false;
    }

    /**
     * Registra la devolución de un ejemplar del libro.
     * Aumenta la cantidad disponible en 1.
     * @return true si se pudo devolver, false si ya estaba todo disponible.
     */
    public boolean devolver() {
        if (cantidadDisponible < cantidadTotal) {
            cantidadDisponible++;
            return true;
        }
        return false;
    }

    /**
     * Verifica si hay ejemplares disponibles para préstamo.
     * @return true si hay al menos un ejemplar disponible
     */
    public boolean estaDisponible() {
        return cantidadDisponible > 0;
    }

    // SOBREESCRITURA
    @Override
    public String toString() {
        return """
               Libro {
                 ISBN: """ + isbn +
               "\n  Título: " + titulo +
               "\n  Autor: " + autor +
               "\n  Editorial: " + editorial +
               "\n  Año: " + anioPublicacion +
               "\n  Categoría: " + categoria +
               "\n  Ejemplares: " + cantidadDisponible + " de " + cantidadTotal +
               "\n}";
    }
}
