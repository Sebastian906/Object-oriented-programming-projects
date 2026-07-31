package models;

import java.time.LocalDate;

/**
 * Representa un usuario en el sistema de la biblioteca.
 * Conceptos de POO aplicados:
 * - Clase (definición de un objeto)
 * - Atributos privados (encapsulamiento)
 * - Constructores (inicialización de objetos)
 * - Métodos de acceso (getters y setters)
 * - Métodos de comportamiento (acciones que puede realizar un objeto)
 * - Uso de LocalDate para manejo de fechas
 * - Sobreescritura de métodos (toString, equals, hashCode)
 */
public class Usuario {
    // Atributos (encapsulamiento)
    @SuppressWarnings("FieldMayBeFinal")
    private String id;
    private String nombre;
    private String email;
    private String telefono;
    @SuppressWarnings("FieldMayBeFinal")
    private LocalDate fechaRegistro;
    private boolean activo;

    /**
     * Constructor de la clase Usuario.
     * La fecha de registro se asigna automáticamente con la fecha actual.
     * 
     * @param id        Identificador único del usuario
     * @param nombre    Nombre completo del usuario
     * @param email     Correo electrónico del usuario
     * @param telefono  Teléfono del usuario
     */
    public Usuario(String id, String nombre, String email, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.fechaRegistro = LocalDate.now(); // Fecha automática
        this.activo = true; // Inicialmente activo
    }

    // GETTERS
    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefono() {
        return telefono;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public boolean isActivo() {
        return activo;
    }

    // SETTERS
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // MÉTODOS DE COMPORTAMIENTO
    /**
     * Verifica si el usuario está activo en el sistema.
     * @return true si el usuario está activo
     */
    public boolean estaActivo() {
        return activo;
    }

    // SOBREESCRITURA
    /**
     * Representación en texto del usuario.
     * Sobreescritura del método toString() de la clase Object.
     */
    @Override
    public String toString() {
        return """
               Usuario {
                 ID: """ + id +
               "\n  Nombre: " + nombre +
               "\n  Email: " + email +
               "\n  Teléfono: " + telefono +
               "\n  Registro: " + fechaRegistro +
               "\n  Activo: " + (activo ? "Sí" : "No") +
               "\n}";
    }
}
