# Sistema de Gestion de Biblioteca

Proyecto desarrollado como parte del curso de Programacion Orientada a Objetos. Este sistema permite gestionar el catalogo de libros, el registro de usuarios y el control de prestamos de una biblioteca, aplicando los principios fundamentales de la POO y el patron de arquitectura MVC.

---

## Contenido

1. [Descripcion del Sistema](#descripcion-del-sistema)
2. [Universo del Discurso](#universo-del-discurso)
3. [Arquitectura del Proyecto](#arquitectura-del-proyecto)
4. [Fundamentos de Programacion](#fundamentos-de-programacion)
5. [Pilares de la Programacion Orientada a Objetos](#pilares-de-la-programacion-orientada-a-objetos)
6. [Logica del Sistema en Pseudocodigo](#logica-del-sistema-en-pseudocodigo)
7. [Compilacion y Ejecucion](#compilacion-y-ejecucion)
8. [Casos de Uso y Manual de Usuario](#casos-de-uso-y-manual-de-usuario)

---

## Descripcion del Sistema

El Sistema de Gestion de Biblioteca es una aplicacion de escritorio desarrollada en Java que permite administrar los recursos de una biblioteca de forma digital. El sistema ofrece dos modos de interaccion: una interfaz basada en consola y una interfaz grafica desarrollada con Swing.

El sistema esta disenado para resolver las operaciones基本icas de cualquier biblioteca: registrar libros en un catalogo, administrar usuarios autorizados y controlar los prestamos de ejemplares. Cada operacion incluye validaciones para garantizar la integridad de los datos y un manejo adecuado de errores.

### Funcionalidades Principales

- **Gestion de Libros**: Registro, busqueda por ISBN, busqueda por titulo y listado completo del catalogo.
- **Gestion de Usuarios**: Registro con generacion automatica de identificadores, busqueda por ID o email, y listado de usuarios activos.
- **Gestion de Prestamos**: Registro de prestamos con validacion de disponibilidad y limites, devolucion de libros con verificacion de fechas, y listado de prestamos activos o por usuario.
- **Estadisticas**: Visualizacion de resumen general con conteo de libros, usuarios y prestamos activos.

### Restricciones del Sistema

- Un usuario puede tener un maximo de 3 prestamos activos simultaneamente.
- No se puede prestar un libro del cual no hayan ejemplares disponibles.
- Los usuarios inactivos no pueden realizar prestamos.
- Cada libro se identifica unicamente por su ISBN.
- Cada usuario se identifica unicamente por su email.

---

## Universo del Discurso

El universo del discurso de este proyecto corresponde al dominio de una biblioteca publica o institucional. En este contexto, se identifican los siguientes actores y elementos:

### Actores

- **Bibliotecario**: Persona encargada de administrar el sistema. Registra libros, usuarios y procesa prestamos y devoluciones.
- **Usuario**: Persona que solicita el prestamo de libros. Debe estar registrado y activo en el sistema.

### Elementos del Dominio

- **Libro**: Recurso bibliografico disponible para prestamo. Cada libro tiene informacion de identificacion (ISBN, titulo, autor), datos editorialies y un control de ejemplares disponibles.
- **Usuario**: Persona registrada en el sistema con datos de contacto y un estado que determina si puede realizar prestamos.
- **Prestamo**: Transaccion que vincula un libro con un usuario por un periodo determinado. Tiene un ciclo de vida que incluye la creacion, el periodo activo y la devolucion.
- **Biblioteca**: Entidad central que contiene y administra todos los recursos y operaciones.

### Relaciones entre Elementos

- Un **Libro** puede tener multiples **Prestamos** en el tiempo (pero solo uno activo por ejemplar).
- Un **Usuario** puede tener multiples **Prestamos** activos (hasta 3).
- Un **Prestamo** asocia un **Libro** con un **Usuario** en un momento determinado.
- La **Biblioteca** contiene colecciones de libros, usuarios y prestamos.

---

## Arquitectura del Proyecto

El proyecto implementa el patron de arquitectura **MVC (Modelo-Vista-Controlador)**, que separa la logica de negocio de la interfaz de usuario y de la coordinacion de operaciones.

### Estructura de Directorios

```
1-sistema-biblioteca/
├── src/
│   ├── main/              Punto de entrada de la aplicacion
│   │   └── Main.java
│   ├── models/            Entidades del dominio y logica de negocio
│   │   ├── Biblioteca.java
│   │   ├── Libro.java
│   │   ├── Usuario.java
│   │   ├── Prestamo.java
│   │   └── EstadoPrestamo.java
│   ├── view/              Interfaz de usuario
│   │   ├── BibliotecaVista.java        (Consola)
│   │   └── BibliotecaVistaGUI.java     (Swing)
│   ├── controller/        Coordinacion entre modelo y vista
│   │   └── BibliotecaController.java
│   ├── service/           Servicios utilitarios
│   │   ├── GeneradorIdService.java
│   │   └── ValidadorService.java
│   ├── exceptions/        Jerarquia de excepciones personalizadas
│   │   ├── BibliotecaException.java
│   │   ├── LibroNoEncontradoException.java
│   │   ├── LibroNoDisponibleException.java
│   │   ├── UsuarioNoEncontradoException.java
│   │   ├── UsuarioInactivoException.java
│   │   ├── LimitePrestamosException.java
│   │   ├── PrestamoNoEncontradoException.java
│   │   ├── PrestamoYaDevueltoException.java
│   │   ├── EmailDuplicadoException.java
│   │   └── DatoInvalidoException.java
│   └── utils/             Utilidades generales
│       ├── ConsoleUtil.java
│       └── FormatUtil.java
└── docs/
    └── diagrams/         Diagramas del proyecto
```

### Descripcion de Capas

**Modelo (models/**)**: Contiene las entidades que representan los objetos del dominio (Libro, Usuario, Prestamo) y la clase Biblioteca que centraliza la logica de negocio y las colecciones de datos. Las entidades encapsulan sus atributos y exponen comportamientos a traves de metodos.

**Vista (view/**)**: Presenta la informacion al usuario y captura sus entradas. BibliotecaVista funciona mediante menus de consola, mientras que BibliotecaVistaGUI ofrece una interfaz grafica con pestanas, tablas y formularios. Ambas vistas implementan la misma funcionalidad pero con diferentes tecnologias de presentacion.

**Controlador (controller/**)**: BibliotecaController actua como intermediario entre el modelo y la vista. Recibe las peticiones del usuario a traves de la vista, invoca las operaciones correspondientes en el modelo y devuelve los resultados para su presentacion.

**Servicios (service/**)**: Proporcionan funcionalidades reutilizables como la generacion secuencial de identificadores y la validacion de datos de entrada.

**Excepciones (exceptions/**)**: Definen una jerarquia de errores especificos del dominio que permiten un manejo preciso de las situaciones excepcionales.

**Utilidades (utils/**)**: Contienen metodos estaticos para operaciones comunes como entrada/salida por consola y formateo de datos.

### Flujo de una Operacion

Cuando el usuario selecciona una opcion en la interfaz, el flujo es el siguiente:

1. La **Vista** captura la entrada del usuario y la envia al **Controlador**.
2. El **Controlador** valida los datos usando los **Servicios** si es necesario.
3. El **Controlador** solicita la operacion al **Modelo**.
4. El **Modelo** ejecuta la logica de negocio y retorna el resultado.
5. El **Controlador** envia el resultado de vuelta a la **Vista** para su presentacion.

---

## Fundamentos de Programacion

Antes de entrar en los conceptos de POO, es importante reconocer los fundamentos de programacion que se aplican en este proyecto.

### Variables y Tipos de Datos

El proyecto utiliza tipos de datos primitivos (int, boolean, String) y tipos de datos compuestos (LocalDate, ArrayList) para representar la informacion del sistema.

```java
// Tipos primitivos
String isbn = "978-84-376-0494-7";
int cantidadTotal = 5;
boolean estaActivo = true;

// Tipos compuestos
LocalDate fechaRegistro = LocalDate.now();
ArrayList<Libro> libros = new ArrayList<>();
```

### Estructuras de Control

Se emplean estructuras condicionales (if-else, switch) y ciclos (while, for-each) para controlar el flujo de ejecucion.

```java
// Estructura condicional
if (libro.estaDisponible()) {
    // Realizar prestamo
} else {
    // Mostrar error
}

// Ciclo while para menus
while (ejecutando) {
    int opcion = vista.mostrarMenuPrincipal();
    // Procesar opcion
}

// Ciclo for-each para recorrer colecciones
for (Libro libro : libros) {
    if (libro.getTitulo().contains(tituloBuscado)) {
        resultados.add(libro);
    }
}
```

### Arreglos y Colecciones

El proyecto utiliza ArrayList como estructura de datos dinamica para almacenar colecciones de objetos. A diferencia de los arreglos estaticos, ArrayList permite agregar y eliminar elementos en tiempo de ejecucion.

```java
// Crear una lista vacia
ArrayList<Libro> libros = new ArrayList<>();

// Agregar un elemento
libros.add(nuevoLibro);

// Buscar un elemento
for (Libro libro : libros) {
    if (libro.getIsbn().equals(isbnBuscado)) {
        return libro;
    }
}

// Obtener el tamano de la lista
int total = libros.size();
```

### Metodos y Funciones

Los metodos permiten organizar el codigo en bloques reutilizables. Cada metodo tiene una responsabilidad especifica y puede recibir parametros y devolver un valor.

```java
// Metodo que retorna un valor
public Libro buscarLibroPorIsbn(String isbn) {
    for (Libro libro : libros) {
        if (libro.getIsbn().equals(isbn)) {
            return libro;
        }
    }
    return null;
}

// Metodo que realiza una accion
public void mostrarMensaje(String mensaje) {
    System.out.println(mensaje);
}
```

### Manejo de Errores

El proyecto implementa un sistema de excepciones personalizadas que permite manejar errores especificos del dominio de forma controlada.

```java
try {
    Prestamo prestamo = biblioteca.prestarLibro(isbn, idUsuario, dias);
    vista.mostrarExito("Prestamo registrado");
} catch (LibroNoEncontradoException e) {
    vista.mostrarError("Libro no encontrado: " + e.getMessage());
} catch (LibroNoDisponibleException e) {
    vista.mostrarError("Sin ejemplares: " + e.getMessage());
}
```

---

## Pilares de la Programacion Orientada a Objetos

Los pilares de la POO son los conceptos fundamentales que diferencian la programacion orientada a objetos de otros paradigmas. A continuacion se explica cada pilar y como se aplica en este proyecto.

### 1. Abstraccion

La abstraccion consiste en representar los elementos esenciales del mundo real como objetos en el codigo, omitiendo los detalles irrelevantes. Se logra a traves de clases que definen que propiedades y comportamientos tiene cada objeto.

En este proyecto, la clase **Libro** es un ejemplo claro de abstraccion. En el mundo real, un libro tiene miles de propiedades (peso, color, numero de paginas, estado fisico, etc.), pero para el sistema de biblioteca solo nos importan: ISBN, titulo, autor, editorial, anio de publicacion, categoria y cantidad de ejemplares.

```java
public class Libro {
    // Propiedades esenciales para el sistema
    private String isbn;
    private String titulo;
    private String autor;
    private String editorial;
    private String anioPublicacion;
    private String categoria;
    private int cantidadTotal;
    private int cantidadDisponible;
}
```

De manera similar, la clase **Usuario** abstrae a la persona real representando solo: ID, nombre, email, telefono, fecha de registro y estado de actividad.

### 2. Encapsulamiento

El encapsulamiento protege los datos internos de un objeto impidiendo el acceso directo desde fuera de la clase. El acceso se realiza unicamente a traves de metodos publicos (getters y setters), lo que permite controlar como se leen y modifican los atributos.

En el proyecto, todos los atributos de las clases son **private**. Esto significa que ninguna otra clase puede acceder directamente a estas variables. Para leer o modificar sus valores, se utilizan metodos publicos.

```java
public class Libro {
    // Atributo privado - no accesible desde fuera
    private String titulo;

    // Getter para leer el valor
    public String getTitulo() {
        return titulo;
    }

    // Setter para modificar el valor
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
}
```

Este patron se aplica en todas las entidades: Libro, Usuario, Prestamo y en la propia Biblioteca, donde las colecciones (libros, usuarios, prestamos) son privadas y solo accesibles a traves de metodos que controlan las operaciones permitidas.

El encapsulamiento tambien protege la integridad de los datos. Por ejemplo, en la clase Libro, el metodo `prestar()` verifica que haya ejemplares disponibles antes de decrementar la cantidad:

```java
public boolean prestar() {
    if (cantidadDisponible > 0) {
        cantidadDisponible--;
        return true;
    }
    return false;
}
```

### 3. Herencia

La herencia permite crear nuevas clases basadas en clases existentes, reutilizando codigo y estableciendo jerarquias de relacion. La clase hija hereda los atributos y metodos de la clase padre, y puede agregar nuevos o modificar los existentes.

En este proyecto, la herencia se aplica de forma sutil pero importante:

**Jerarquia de Excepciones**: Todas las excepciones personalizadas heredan de BibliotecaException, que a su vez hereda de Exception de Java.

```java
// Clase base
public class BibliotecaException extends Exception {
    public BibliotecaException(String mensaje) {
        super(mensaje);
    }
}

// Excepciones especificas que heredan
public class LibroNoEncontradoException extends BibliotecaException {
    public LibroNoEncontradoException(String isbn) {
        super("No se encontro libro con ISBN: " + isbn);
    }
}

public class LibroNoDisponibleException extends BibliotecaException {
    public LibroNoDisponibleException(String titulo) {
        super("No hay ejemplares disponibles de: " + titulo);
    }
}
```

Gracias a esta jerarquia, el controlador puede capturar errores generales o especificos segun sea necesario:

```java
// Capturar toda excepcion del dominio
catch (BibliotecaException e) {
    vista.mostrarError(e.getMessage());
}

// Capturar excepciones especificas
catch (LibroNoEncontradoException e) {
    vista.mostrarError("Libro no encontrado");
} catch (LibroNoDisponibleException e) {
    vista.mostrarError("Sin stock");
}
```

**Clases de Utilidad**: Las clases ConsoleUtil y FormatUtil extienden implicitamente la funcionalidad de la biblioteca estandar de Java, proporcionando metodos estaticos reutilizables.

### 4. Polimorfismo

El polimorfismo permite que objetos de diferentes clases respondan al mismo mensaje de forma diferente. Un metodo puede comportarse de manera distinta dependiendo del tipo de objeto que lo invoque.

En este proyecto, el polimorfismo se manifiesta de varias formas:

**Sobreescritura de toString()**: Cada clase redefine el metodo toString() para proporcionar una representacion en texto adecuada.

```java
// En la clase Libro
@Override
public String toString() {
    return "Libro { ISBN: " + isbn + ", Titulo: " + titulo + " }";
}

// En la clase Usuario
@Override
public String toString() {
    return "Usuario { ID: " + id + ", Nombre: " + nombre + " }";
}

// En la clase Prestamo
@Override
public String toString() {
    return "Prestamo { ID: " + id + ", Libro: " + libro.getTitulo() + " }";
}
```

**Polimorfismo de interfaz**: La clase BibliotecaController puede trabajar con cualquier tipo de vista que implemente los metodos necesarios, ya sea BibliotecaVista (consola) o BibliotecaVistaGUI (Swing).

```java
// Ambas vistas implementan los mismos metodos
public class BibliotecaVista {
    public int mostrarMenuPrincipal() { ... }
    public void mostrarExito(String mensaje) { ... }
}

public class BibliotecaVistaGUI {
    public int mostrarMenuPrincipal() { ... }
    public void mostrarExito(String mensaje) { ... }
}
```

**Uso de Enumeraciones**: El enum EstadoPrestamo representa estados con comportamiento encapsulado.

```java
public enum EstadoPrestamo {
    ACTIVO("Activo"),
    DEVUELTO("Devuelto"),
    VENCIDO("Vencido");

    private final String descripcion;

    // Cada constante tiene su propio estado
    @Override
    public String toString() {
        return descripcion;
    }
}
```

### 5. Composicion

La composicion es un tipo de relacion entre objetos donde una clase contiene instancias de otras clases como atributos. Es un pilar fundamental para construir sistemas complejos a partir de componentes mas simples.

En este proyecto, la composicion se aplica en la clase Biblioteca, que contiene colecciones de objetos Libro, Usuario y Prestamo:

```java
public class Biblioteca {
    // Composicion: Biblioteca "contiene" Libros, Usuarios y Prestamos
    private final ArrayList<Libro> libros;
    private final ArrayList<Usuario> usuarios;
    private final ArrayList<Prestamo> prestamos;

    public Biblioteca() {
        this.libros = new ArrayList<>();
        this.usuarios = new ArrayList<>();
        this.prestamos = new ArrayList<>();
    }
}
```

De manera similar, la clase Prestamo esta compuesta por un objeto Libro y un objeto Usuario:

```java
public class Prestamo {
    // Un Prestamo "contiene" un Libro y un Usuario
    private final Libro libro;
    private final Usuario usuario;
}
```

Esta relacion de composicion permite que un Prestamo tenga acceso a toda la informacion del libro prestado y del usuario que lo solicito, creando un modelo rico y conectado.

---

## Logica del Sistema en Pseudocodigo

A continuacion se presenta la logica principal del sistema en pseudocodigo, facilitando la comprension del flujo de ejecucion sin necesidad de conocimientos avanzados de programacion.

### Flujo Principal del Sistema

```
INICIO
    CREAR instancia de Biblioteca
    
    MOSTRAR dialogo de seleccion de modo
    SI usuario selecciona "Modo Consola" ENTONCES
        EJECUTAR modo consola
    SINO SI usuario selecciona "Modo Grafico" ENTONCES
        EJECUTAR modo grafico
    SINO
        TERMINAR
    FIN SI
FIN

PROCEDIMIENTO modo consola
    CREAR BibliotecaVista
    CREAR BibliotecaController(biblioteca, vista)
    EXECUTAR controller.ejecutar()
FIN PROCEDIMIENTO

PROCEDIMIENTO modo grafico
    CREAR BibliotecaVistaGUI(biblioteca)
    MOSTRAR ventana grafica
FIN PROCEDIMIENTO
```

### Logica del Controlador (Bucle Principal)

```
PROCEDIMIENTO ejecutar()
    ejecutando = VERDADERO
    
    MIENTRAS ejecutando HACER
        opcion = vista.mostrarMenuPrincipal()
        
        SEGUN opcion HACER
            CASO 1: gestionarLibros()
            CASO 2: gestionarUsuarios()
            CASO 3: gestionarPrestamos()
            CASO 4: verEstadisticas()
            CASO 0: 
                ejecutando = FALSO
                vista.mostrarMensaje("Hasta luego")
            DEFECTO: vista.mostrarError("Opcion no valida")
        FIN SEGUN
    FIN MIENTRAS
FIN PROCEDIMIENTO
```

### Registro de un Prestamo

```
PROCEDIMIENTO registrarPrestamo()
    datos = vista.formularioPrestamo()
    
    // Validaciones iniciales
    SI datos[0] esta vacio O datos[1] esta vacio O datos[2] esta vacio ENTONCES
        vista.mostrarError("Todos los campos son obligatorios")
        RETORNAR
    FIN SI
    
    diasPrestamo = Convertir datos[2] a entero
    
    SI diasPrestamo <= 0 ENTONCES
        vista.mostrarError("Los dias deben ser mayores a 0")
        RETORNAR
    FIN SI
    
    // Proceso de prestamo
    PRESTAR LIBRO
        SI el libro con ISBN datos[0] no existe ENTONCES
            LANZAR LibroNoEncontradoException
        FIN SI
        
        SI el libro no tiene ejemplares disponibles ENTONCES
            LANZAR LibroNoDisponibleException
        FIN SI
        
        SI el usuario con ID datos[1] no existe ENTONCES
            LANZAR UsuarioNoEncontradoException
        FIN SI
        
        SI el usuario no esta activo ENTONCES
            LANZAR UsuarioInactivoException
        FIN SI
        
        SI el usuario ya tiene 3 prestamos activos ENTONCES
            LANZAR LimitePrestamosException
        FIN SI
        
        idPrestamo = Generar ID unico
        fechaDevolucion = FechaActual + diasPrestamo
        
        CREAR Prestamo(idPrestamo, libro, usuario, fechaDevolucion)
        libro.prestar()  // Reducir cantidad disponible
        AGREGAR prestamos a coleccion
        
        RETORNAR prestamo
    FIN PRESTAR
    
    vista.mostrarExito("Prestamo registrado")
    vista.mostrarPrestamo(prestamo)
    
CATCH LibroNoEncontradoException
    vista.mostrarError("Libro no encontrado")
CATCH LibroNoDisponibleException
    vista.mostrarError("Sin ejemplares disponibles")
CATCH UsuarioNoEncontradoException
    vista.mostrarError("Usuario no encontrado")
CATCH UsuarioInactivoException
    vista.mostrarError("Usuario inactivo")
CATCH LimitePrestamosException
    vista.mostrarError("Limite de prestamos alcanzado")
FIN PROCEDIMIENTO
```

### Devolucion de un Libro

```
PROCEDIMIENTO devolverLibro()
    idPrestamo = vista.formularioDevolucion()
    
    // Buscar el prestamo
    prestamo = buscarPrestamoPorId(idPrestamo)
    
    SI prestamo no existe ENTONCES
        LANZAR PrestamoNoEncontradoException
    FIN SI
    
    SI prestamo ya fue devuelto ENTONCES
        LANZAR PrestamoYaDevueltoException
    FIN SI
    
    // Proceso de devolucion
    prestamo.getLibro().devolver()  // Aumentar cantidad disponible
    resultado = prestamo.devolver()  // Registrar devolucion
    
    SI fecha de devolucion > fecha esperada ENTONCES
        calcular dias de retraso
        RETORNAR "Devuelto con X dias de retraso"
    SINO
        RETORNAR "Devuelto a tiempo"
    FIN SI
    
FIN PROCEDIMIENTO
```

### Busqueda de Libros por Titulo

```
PROCEDIMIENTO buscarLibros(tituloBuscado)
    resultados = LISTA VACIA
    tituloMinusculas = Convertir tituloBuscado a minusculas
    
    PARA CADA libro EN libros HACER
        tituloLibro = Convertir libro.getTitulo() a minusculas
        
        SI tituloLibro CONTIENE tituloMinusculas ENTONCES
            AGREGAR libro a resultados
        FIN SI
    FIN PARA
    
    RETORNAR resultados
FIN PROCEDIMIENTO
```

---

## Compilacion y Ejecucion

### Requisitos

- Java JDK 21 o superior
- Sin dependencias externas (solo biblioteca estandar de Java)

### Compilacion Manual

Desde la raiz del repositorio, ejecutar los siguientes comandos:

```powershell
# Crear carpeta de salida
mkdir -Force out

# Compilar todos los archivos Java
javac -d out -sourcepath 1-sistema-biblioteca/src 1-sistema-biblioteca/src/main/Main.java
```

El flag `-sourcepath` es fundamental para que Java resuelva las referencias entre paquetes correctamente.

### Ejecucion

```powershell
java -cp out main.Main
```

Al ejecutar la aplicacion, se mostrara un dialogo preguntando si se desea usar el modo consola o el modo grafico.

---

## Casos de Uso y Manual de Usuario

### Caso de Uso 1: Registrar un Libro

**Descripcion**: El bibliotecario desea agregar un nuevo libro al catalogo de la biblioteca.

**Pasos**:

1. Iniciar la aplicacion y seleccionar "Modo Consola" o "Modo Grafico".
2. En el menu principal, seleccionar "Gestion de Libros".
3. Seleccionar "Agregar libro".
4. Ingresar los datos solicitados:
   - ISBN: 978-84-376-0494-7
   - Titulo: Cien Anos de Soledad
   - Autor: Gabriel Garcia Marquez
   - Editorial: Editorial Sudamericana
   - Anio de publicacion: 1967
   - Categoria: Novela
   - Cantidad de ejemplares: 3
5. Confirmar la operacion.

**Resultado esperado**: Mensaje de exito indicando que el libro fue agregado correctamente. El libro aparecera en el listado de libros.

**Error esperado**: Si se intenta registrar un libro con un ISBN ya existente, el sistema mostrara un mensaje de error indicando que el libro ya esta registrado.

### Caso de Uso 2: Registrar un Usuario

**Descripcion**: Un nuevo usuario desea darse de alta en la biblioteca.

**Pasos**:

1. En el menu principal, seleccionar "Gestion de Usuarios".
2. Seleccionar "Registrar usuario".
3. Ingresar los datos:
   - Nombre completo: Maria Lopez Garcia
   - Email: maria.lopez@correo.com
   - Telefono: 555-123-4567
4. Confirmar la operacion.

**Resultado esperado**: Mensaje de exito con el ID asignado automaticamente al usuario (por ejemplo: "Usuario registrado correctamente. ID: U1").

**Error esperado**: Si se intenta registrar un email que ya existe, el sistema mostrara un error de email duplicado.

### Caso de Uso 3: Realizar un Prestamo

**Descripcion**: Un usuario registrado solicita el prestamo de un libro disponible.

**Pasos**:

1. Verificar que el usuario este registrado y activo.
2. Verificar que el libro este en el catalogo y tenga ejemplares disponibles.
3. En el menu principal, seleccionar "Gestion de Prestamos".
4. Seleccionar "Registrar prestamo".
5. Ingresar los datos:
   - ISBN del libro: 978-84-376-0494-7
   - ID del usuario: U1
   - Dias de prestamo: 14
6. Confirmar la operacion.

**Resultado esperado**: Mensaje de exito con los detalles del prestamo, incluyendo el ID del prestamo y la fecha esperada de devolucion.

**Errores esperados**:

- "Libro no encontrado": Si el ISBN no existe en el catalogo.
- "Sin ejemplares disponibles": Si todos los ejemplares estan prestados.
- "Usuario no encontrado": Si el ID no corresponde a ningun usuario registrado.
- "Usuario inactivo": Si el usuario fue desactivado del sistema.
- "Limite de prestamos alcanzado": Si el usuario ya tiene 3 prestamos activos.

### Caso de Uso 4: Devolver un Libro

**Descripcion**: Un usuario devuelve un libro que tenia en prestamo.

**Pasos**:

1. En el menu principal, seleccionar "Gestion de Prestamos".
2. Seleccionar "Devolver libro".
3. Ingresar el ID del prestamo (por ejemplo: P1).
4. Confirmar la operacion.

**Resultado esperado**:

- Si se devuelve a tiempo: "Libro devuelto a tiempo. Gracias!"
- Si se devuelve con retraso: "Libro devuelto con X dia(s) de retraso."

**Errores esperados**:

- "Prestamo no encontrado": Si el ID no corresponde a ningun prestamo.
- "Prestamo ya devuelto": Si el prestamo ya fue processado anteriormente.

### Caso de Uso 5: Buscar Libros

**Descripcion**: El usuario desea encontrar un libro por su titulo.

**Pasos**:

1. En el menu principal, seleccionar "Gestion de Libros".
2. Seleccionar "Buscar libros por titulo".
3. Ingresar el texto a buscar (por ejemplo: "Cien").

**Resultado esperado**: Lista de todos los libros cuyo titulo contiene el texto ingresado, mostrando ISBN, titulo, autor y ejemplares disponibles. La busqueda no distingue entre mayusculas y minusculas.

### Caso de Uso 6: Ver Estadisticas

**Descripcion**: El bibliotecario desea consultar un resumen general de la actividad de la biblioteca.

**Pasos**:

1. En el menu principal, seleccionar "Ver Estadisticas".

**Resultado esperado**: Un resumen que incluye:

- Total de libros registrados en el catalogo.
- Total de usuarios activos en el sistema.
- Total de prestamos realizados (historico).
- Prestamos actualmente activos.

### Caso de Uso 7: Listar Prestamos por Usuario

**Descripcion**: Se desea consultar todos los prestamos asociados a un usuario especifico.

**Pasos**:

1. En el menu principal, seleccionar "Gestion de Prestamos".
2. Seleccionar "Listar prestamos por usuario".
3. Ingresar el ID del usuario (por ejemplo: U1).

**Resultado esperado**: Lista de todos los prestamos del usuario, mostrando ID del prestamo, titulo del libro, fecha de prestamo, fecha de devolucion esperada y estado actual.

---

## Notas Finales

Este proyecto demuestra la aplicacion practica de los conceptos fundamentales de la Programacion Orientada a Objetos en un escenario del mundo real. Cada decision de diseno refleja un principio de la POO: desde la abstraccion de las entidades del dominio hasta el manejo de errores con excepciones personalizadas, pasando por el encapsulamiento de datos y la composicion de objetos.

La arquitectura MVC separa responsabilidades de forma clara, permitiendo que la logica de negocio, la interfaz de usuario y la coordinacion de operaciones evolucionen de manera independiente. Esto facilita el mantenimiento del codigo y permite agregar nuevas funcionalidades sin modificar las existentes.

El uso de pseudocodigo en la seccion de logica busca hacer accesible la comprension del sistema a personas que no estan familiarizadas con la programacion, priorizando la logica del negocio sobre los detalles de implementacion.
