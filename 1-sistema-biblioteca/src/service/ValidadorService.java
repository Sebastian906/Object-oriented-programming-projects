package service;

import exceptions.DatoInvalidoException;

/**
 * Servicio de validación de datos.
 * Contiene métodos estáticos para validar entradas del sistema.
 * Conceptos POO aplicados:
 * - Clase utilitaria con métodos estáticos
 * - Separación de responsabilidades (Single Responsibility Principle)
 * - Reutilización de lógica de validación
 */
public final class ValidadorService {

    // Constructor privado para evitar instanciación
    private ValidadorService() {
        throw new UnsupportedOperationException("Esta es una clase utilitaria y no puede ser instanciada.");
    }

    /**
     * Valida que un texto no esté vacío o sea nulo.
     * @param campo   Nombre del campo (para el mensaje de error)
     * @param valor   Valor a validar
     * @throws DatoInvalidoException si el valor es nulo o está vacío
     */
    public static void validarNoVacio(String campo, String valor) throws DatoInvalidoException {
        if (valor == null || valor.trim().isEmpty()) {
            throw new DatoInvalidoException(campo, "no puede estar vacío.");
        }
    }

    /**
     * Valida que un email tenga un formato básico correcto.
     * @param email Email a validar
     * @throws DatoInvalidoException si el email no tiene un formato válido
     */
    public static void validarEmail(String email) throws DatoInvalidoException {
        validarNoVacio("email", email);
        
        // Validación básica de formato
        if (!email.contains("@") || !email.contains(".")) {
            throw new DatoInvalidoException("email", "debe tener un formato válido (ejemplo: usuario@dominio.com).");
        }
        
        // Verificar que no termine en punto
        if (email.endsWith(".")) {
            throw new DatoInvalidoException("email", "no puede terminar en punto.");
        }
    }

    /**
     * Valida que un ISBN no esté vacío.
     * @param isbn ISBN a validar
     * @throws DatoInvalidoException si el ISBN es nulo o está vacío
     */
    public static void validarIsbn(String isbn) throws DatoInvalidoException {
        validarNoVacio("ISBN", isbn);
        
        // ISBN-13 debe tener 13 caracteres, ISBN-10 debe tener 10 caracteres
        String isbnLimpio = isbn.replaceAll("[-\\s]", "");
        if (isbnLimpio.length() != 10 && isbnLimpio.length() != 13) {
            throw new DatoInvalidoException("ISBN", "debe tener 10 o 13 caracteres.");
        }
    }

    /**
     * Valida que un número entero esté dentro de un rango.
     * @param campo     Nombre del campo
     * @param valor     Valor a validar
     * @param minimo    Valor mínimo permitido (inclusivo)
     * @param maximo    Valor máximo permitido (inclusivo)
     * @return El valor validado
     * @throws DatoInvalidoException si el valor está fuera del rango
     */
    public static int validarRangoEntero(String campo, int valor, int minimo, int maximo) throws DatoInvalidoException {
        if (valor < minimo || valor > maximo) {
            throw new DatoInvalidoException(campo, "debe estar entre " + minimo + " y " + maximo + ".");
        }
        return valor;
    }

    /**
     * Valida que un número entero sea positivo.
     * @param campo Nombre del campo
     * @param valor Valor a validar
     * @return El valor validado
     * @throws DatoInvalidoException si el valor es menor o igual a 0
     */
    public static int validarPositivo(String campo, int valor) throws DatoInvalidoException {
        if (valor <= 0) {
            throw new DatoInvalidoException(campo, "debe ser un número positivo.");
        }
        return valor;
    }

    /**
     * Valida que un teléfono tenga un formato básico correcto.
     * @param telefono Teléfono a validar
     * @throws DatoInvalidoException si el teléfono tiene un formato inválido
     */
    public static void validarTelefono(String telefono) throws DatoInvalidoException {
        if (telefono == null || telefono.trim().isEmpty()) {
            return; // El teléfono es opcional
        }
        
        // Permitir solo números, espacios, guiones y paréntesis
        if (!telefono.matches("[\\d\\s\\-\\(\\)\\+]+")) {
            throw new DatoInvalidoException("teléfono", "solo puede contener números, espacios, guiones, paréntesis y el signo +.");
        }
    }

    /**
     * Valida que un texto no exceda una longitud máxima.
     * @param campo      Nombre del campo
     * @param valor      Valor a validar
     * @param maxLength  Longitud máxima permitida
     * @throws DatoInvalidoException si el valor excede la longitud máxima
     */
    public static void validarLongitudMaxima(String campo, String valor, int maxLength) throws DatoInvalidoException {
        if (valor != null && valor.length() > maxLength) {
            throw new DatoInvalidoException(campo, "no puede exceder los " + maxLength + " caracteres.");
        }
    }
}
