/**
 * Enumeración que representa los estados posibles de un préstamo.
 * Conceptos de POO aplicados:
 * - Enumeración (tipo de dato cerrado)
 * - Encapsulamiento de constantes
 */
public enum EstadoPrestamo {
    ACTIVO("Activo"),
    DEVUELTO("Devuelto"),
    VENCIDO("Vencido");

    private final String descripcion;

    EstadoPrestamo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
