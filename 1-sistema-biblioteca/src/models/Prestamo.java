package models;

import java.time.LocalDate;
import utils.FormatUtil;

/**
 * Representa un préstamo de libro a un usuario.
 * Conceptos POO aplicados:
 * - Clase
 * - Asociación con otras clases (Libro, Usuario)
 * - Atributos privados (encapsulamiento)
 * - Constructor
 * - Métodos de comportamiento
 * - Uso de LocalDate y ChronoUnit
 * - Sobreescritura de toString()
 */
public class Prestamo {
    // Atributos (encapsulamiento)
    private final String id;
    private final Libro libro;
    private final Usuario usuario;
    private final LocalDate fechaPrestamo;
    private final LocalDate fechaDevolucionEsperada;
    private LocalDate fechaDevolucionReal;
    private EstadoPrestamo estado;

    /**
     * Constructor de la clase Prestamo.
     * La fecha de préstamo se asigna automáticamente con la fecha actual.
     * El estado inicial es ACTIVO.
     * @param id                      Identificador único del préstamo
     * @param libro                   Libro prestado
     * @param usuario                 Usuario que recibe el préstamo
     * @param fechaDevolucionEsperada Fecha límite de devolución
     */
    public Prestamo(String id, Libro libro, Usuario usuario, LocalDate fechaDevolucionEsperada) {
        this.id = id;
        this.libro = libro;
        this.usuario = usuario;
        this.fechaPrestamo = LocalDate.now(); // Fecha automática
        this.fechaDevolucionEsperada = fechaDevolucionEsperada;
        this.fechaDevolucionReal = null; // Aún no devuelto
        this.estado = EstadoPrestamo.ACTIVO; // Estado inicial
    }

    // GETTERS
    public String getId() {
        return id;
    }

    public Libro getLibro() {
        return libro;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaDevolucionEsperada() {
        return fechaDevolucionEsperada;
    }

    public LocalDate getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public EstadoPrestamo getEstado() {
        return estado;
    }

    // MÉTODOS DE COMPORTAMIENTO
    /**
     * Verifica si el préstamo está activo.
     * @return true si el estado es ACTIVO
     */
    public boolean estaActivo() {
        return estado == EstadoPrestamo.ACTIVO;
    }

    /**
     * Verifica si el préstamo está vencido.
     * Un préstamo está vencido si la fecha actual supera la fecha de devolución esperada
     * y el préstamo sigue activo.
     * @return true si el préstamo está vencido
     */
    public boolean estaVencido() {
        if (estado == EstadoPrestamo.ACTIVO) {
            return FormatUtil.estaVencida(fechaDevolucionEsperada);
        }
        return false;
    }

    /**
     * Calcula los días restantes para la devolución.
     * @return Número de días restantes (negativo si está vencido)
     */
    public int calcularDiasRestantes() {
        return FormatUtil.calcularDiasRestantes(fechaDevolucionEsperada);
    }

    /**
     * Registra la devolución del libro.
     * Actualiza el estado a DEVUELTO y registra la fecha real de devolución.
     * @return Mensaje con el resultado de la devolución
     */
    public String devolver() {
        if (estado != EstadoPrestamo.ACTIVO) {
            return "El préstamo ya fue devuelto o está cancelado.";
        }

        this.fechaDevolucionReal = LocalDate.now();
        this.estado = EstadoPrestamo.DEVUELTO;

        // Verificar si fue devuelto a tiempo
        if (fechaDevolucionReal.isAfter(fechaDevolucionEsperada)) {
            int diasRetraso = FormatUtil.calcularDiasRestantes(
                fechaDevolucionEsperada, fechaDevolucionReal);
            return "Libro devuelto con " + diasRetraso + " día(s) de retraso.";
        } else {
            return "Libro devuelto a tiempo. ¡Gracias!";
        }
    }

    // SOBREESCRITURA
    /**
     * Representación en texto del préstamo.
     * Sobreescritura del método toString() de la clase Object.
     */
    @Override
    public String toString() {
        return """
               Prestamo {
                 ID: """ + id +
               "\n  Libro: " + libro.getTitulo() +
               "\n  Usuario: " + usuario.getNombre() +
               "\n  Fecha Préstamo: " + FormatUtil.formatearFecha(fechaPrestamo) +
               "\n  Devolución Esperada: " + FormatUtil.formatearFecha(fechaDevolucionEsperada) +
               "\n  Devolución Real: " + (fechaDevolucionReal != null ? 
                   FormatUtil.formatearFecha(fechaDevolucionReal) : "Pendiente") +
               "\n  Estado: " + estado +
               "\n}";
    }
}
