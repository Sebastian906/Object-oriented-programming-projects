# Simulador de Cajero Automatico (ATM)

Proyecto desarrollado como parte del curso de Programacion Orientada a Objetos. Este simulador permite realizar operaciones bancarias basicas mediante una interfaz grafica, aplicando los principios fundamentales de la POO y el patron de arquitectura MVC.

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

El Simulador de Cajero Automatico es una aplicacion de escritorio desarrollada en Java que replica el funcionamiento basico de un cajero automatico bancario. El sistema ofrece una interfaz grafica desarrollada con Swing que permite a los usuarios realizar operaciones bancarias comunes de forma segura e intuitiva.

El sistema esta disenado para resolver las operaciones basicas de cualquier cajero automatico: consultar saldo, retirar efectivo, depositar dinero, transferir fondos entre cuentas, cambiar el PIN de acceso y consultar el historial de transacciones. Cada operacion incluye validaciones para garantizar la integridad de los datos y un manejo adecuado de errores.

### Funcionalidades Principales

- **Consulta de Saldo**: Visualizacion del saldo actual de la cuenta.
- **Retiro de Efectivo**: Retiro de dinero con validacion de saldo disponible y limites diarios.
- **Deposito de Efectivo**: Ingreso de dinero a la cuenta con validacion de montos.
- **Transferencia**: Envio de dinero a otra cuenta bancaria.
- **Cambio de PIN**: Actualizacion del codigo de acceso con verificacion del PIN actual.
- **Historial de Transacciones**: Consulta completa de todas las operaciones realizadas.

### Restricciones del Sistema

- El saldo minimo en cuenta es de $100.
- El monto maximo de retiro por operacion es de $500,000.
- El monto minimo de deposito es de $10,000.
- El monto minimo de transferencia es de $1,000.
- El PIN debe tener exactamente 4 digitos.
- La cuenta se bloquea despues de 3 intentos de PIN incorrectos.

---

## Universo del Discurso

El universo del discurso de este proyecto corresponde al dominio de la banca minorista y los servicios financieros. En este contexto, se identifican los siguientes actores y elementos:

### Actores

- **Cliente**: Persona que posee una cuenta bancaria y realiza operaciones a traves del cajero automatico.
- **Sistema Bancario**: Entidad que gestiona las cuentas, procesa las transacciones y mantiene la seguridad de las operaciones.

### Elementos del Dominio

- **Cuenta Bancaria**: Entidad que representa la cuenta de un cliente. Contiene informacion de identificacion (numero de cuenta), saldo actual y un codigo PIN de acceso.
- **Transaccion**: Registro de cada operacion realizada en el sistema. Cada transaccion tiene un tipo, un monto, una fecha y un estado (exitosa o fallida).
- **Sesion**: Sesion activa del usuario en el cajero automatico. Controla el tiempo de acceso y las operaciones permitidas.
- **Cajero Automatico**: Maquina fisica o virtual que interactua con el cliente para realizar operaciones bancarias.

### Relaciones entre Elementos

- Un **Cliente** posee una o mas **Cuentas Bancarias**.
- Una **Cuenta Bancaria** puede tener multiples **Transacciones**.
- Una **Transaccion** pertenece a una **Cuenta Bancaria** y tiene un tipo especifico.
- El **Cajero Automatico** interactua con el **Cliente** a traves de una **Sesion**.
- La **Sesion** controla el acceso a las **Cuentas Bancarias** del cliente.

---

## Arquitectura del Proyecto

El proyecto implementa el patron de arquitectura **MVC (Modelo-Vista-Controlador)**, que separa la logica de negocio de la interfaz de usuario y de la coordinacion de operaciones.

### Estructura de Directorios

```
2-simulador-atm/
├── src/
│   ├── main/              Punto de entrada de la aplicacion
│   │   └── Main.java
│   ├── models/            Entidades del dominio
│   │   ├── CuentaBancaria.java
│   │   ├── Transaccion.java
│   │   ├── TipoTransaccion.java
│   │   └── UsuarioSesion.java
│   ├── view/              Interfaz grafica de usuario
│   │   └── CajeroVistaGUI.java
│   ├── controller/        Coordinacion entre modelo y vista
│   │   └── CajeroController.java
│   ├── service/           Servicios de negocio y utilidades
│   │   ├── CajeroService.java
│   │   ├── HistorialService.java
│   │   ├── GeneradorIdService.java
│   │   └── ValidadorService.java
│   ├── exceptions/        Jerarquia de excepciones personalizadas
│   │   ├── ATMException.java
│   │   ├── CuentaNoEncontradaException.java
│   │   ├── CuentaBloqueadaException.java
│   │   ├── PINIncorrectoException.java
│   │   ├── SaldoInsuficienteException.java
│   │   ├── MontoInvalidoException.java
│   │   └── SesionNoAutenticadaException.java
│   └── utils/             Utilidades generales
│       ├── ConsoleUtil.java
│       └── FormatUtil.java
└── docs/
    └── diagrams/         Diagramas del proyecto
```

### Descripcion de Capas

**Modelo (models/**): Contiene las entidades que representan los objetos del dominio: CuentaBancaria, Transaccion, TipoTransaccion y UsuarioSesion. Las entidades encapsulan sus atributos y exponen comportamientos a traves de metodos que garantizan la integridad de los datos.

**Vista (view/**): CajeroVistaGUI presenta la informacion al usuario y captura sus entradas mediante una interfaz grafica con multiples pantallas (login, menu principal, retiro, deposito, transferencia, historial y cambio de PIN). Utiliza componentes de Swing como botones, campos de texto y paneles.

**Controlador (controller/**): CajeroController actua como intermediario entre el modelo y la vista. Recibe las peticiones del usuario a traves de la vista, invoca las operaciones correspondientes en el servicio y devuelve los resultados para su presentacion.

**Servicios (service/**): CajeroService centraliza la logica de negocio y las operaciones bancarias. HistorialService gestiona el registro y consulta de transacciones. ValidadorService proporciona metodos de validacion de datos. GeneradorIdService crea identificadores unicos para cuentas y transacciones.

**Excepciones (exceptions/**): Definen una jerarquia de errores especificos del dominio que permiten un manejo preciso de las situaciones excepcionales, como cuentas no encontradas, saldos insuficientes o PINs incorrectos.

**Utilidades (utils/**): Contienen metodos estaticos para operaciones comunes como entrada/salida por consola y formateo de datos.

### Flujo de una Operacion

Cuando el usuario selecciona una accion en la interfaz grafica, el flujo es el siguiente:

1. La **Vista** captura la entrada del usuario y la envia al **Controlador**.
2. El **Controlador** valida los datos usando los **Servicios** si es necesario.
3. El **Controlador** solicita la operacion al **Servicio de Cajero**.
4. El **Servicio** ejecuta la logica de negocio y retorna el resultado.
5. El **Controlador** envia el resultado de vuelta a la **Vista** para su presentacion.

---

## Fundamentos de Programacion

Antes de entrar en los conceptos de POO, es importante reconocer los fundamentos de programacion que se aplican en este proyecto.

### Variables y Tipos de Datos

El proyecto utiliza tipos de datos primitivos (int, boolean, String, double) y tipos de datos compuestos (LocalDateTime, ArrayList, HashMap) para representar la informacion del sistema.

```java
// Tipos primitivos
String numeroCuenta = "1234567890123456";
int pin = 1234;
boolean estaActivo = true;
double saldo = 10000.00;

// Tipos compuestos
LocalDateTime fechaTransaccion = LocalDateTime.now();
ArrayList<Transaccion> historial = new ArrayList<>();
```

### Estructuras de Control

Se emplean estructuras condicionales (if-else, switch) y ciclos (for-each) para controlar el flujo de ejecucion.

```java
// Estructura condicional
if (saldo >= monto) {
    // Realizar retiro
} else {
    // Mostrar error de saldo insuficiente
}

// Ciclo for-each para recorrer colecciones
for (Transaccion transaccion : historial) {
    if (transaccion.getTipo() == TipoTransaccion.RETIRO) {
        totalRetiros++;
    }
}
```

### Arreglos y Colecciones

El proyecto utiliza ArrayList como estructura de datos dinamica para almacenar colecciones de transacciones, y HashMap para almacenar cuentas bancarias indexadas por numero de cuenta.

```java
// Crear una lista de transacciones
ArrayList<Transaccion> historial = new ArrayList<>();

// Agregar una transaccion
historial.add(nuevaTransaccion);

// Buscar cuentas en un mapa
HashMap<String, CuentaBancaria> cuentas = new HashMap<>();
cuentas.put(numeroCuenta, cuenta);

// Obtener una cuenta
CuentaBancaria cuenta = cuentas.get(numeroCuenta);
```

### Metodos y Funciones

Los metodos permiten organizar el codigo en bloques reutilizables. Cada metodo tiene una responsabilidad especifica y puede recibir parametros y devolver un valor.

```java
// Metodo que retorna un valor
public double consultarSaldo() {
    return cuenta.getSaldo();
}

// Metodo que realiza una accion
public void registrarTransaccion(TipoTransaccion tipo, double monto) {
    Transaccion transaccion = new Transaccion(tipo, monto);
    historial.add(transaccion);
}
```

### Manejo de Errores

El proyecto implementa un sistema de excepciones personalizadas que permite manejar errores especificos del dominio de forma controlada.

```java
try {
    double nuevoSaldo = controller.retirarEfectivo(monto);
    vista.mostrarExito("Retiro exitoso");
} catch (SaldoInsuficienteException e) {
    vista.mostrarError("Saldo insuficiente");
} catch (MontoInvalidoException e) {
    vista.mostrarError("Monto invalido");
}
```

---

## Pilares de la Programacion Orientada a Objetos

Los pilares de la POO son los conceptos fundamentales que diferencian la programacion orientada a objetos de otros paradigmas. A continuacion se explica cada pilar y como se aplica en este proyecto.

### 1. Abstraccion

La abstraccion consiste en representar los elementos esenciales del mundo real como objetos en el codigo, omitiendo los detalles irrelevantes. Se logra a traves de clases que definen que propiedades y comportamientos tiene cada objeto.

En este proyecto, la clase **CuentaBancaria** es un ejemplo claro de abstraccion. En el mundo real, una cuenta bancaria tiene miles de propiedades (tipo de cuenta, sucursal, fecha de apertura, titular, documentos, etc.), pero para el cajero automatico solo nos importan: numero de cuenta, saldo y PIN.

```java
// Propiedades esenciales para el sistema
clase CuentaBancaria {
    atributo privado numeroCuenta : Texto
    atributo privado saldo : NumeroDecimal
    atributo privado pin : NumeroEntero
    atributo privado activa : Logico
}
```

De manera similar, la clase **Transaccion** abstrae la operacion bancaria representando solo: ID, tipo, monto, fecha y estado.

### 2. Encapsulamiento

El encapsulamiento protege los datos internos de un objeto impidiendo el acceso directo desde fuera de la clase. El acceso se realiza unicamente a traves de metodos publicos, lo que permite controlar como se leen y modifican los atributos.

En el proyecto, todos los atributos de las clases son **private**. Esto significa que ninguna otra clase puede acceder directamente a estas variables. Para leer o modificar sus valores, se utilizan metodos publicos.

```java
clase CuentaBancaria {
    // Atributo privado - no accesible desde fuera
    atributo privado saldo : NumeroDecimal

    // Metodo para leer el valor
    metodo publico consultarSaldo() : NumeroDecimal {
        retornar saldo
    }

    // Metodo para modificar el valor con validacion
    metodo publico debitar(monto : NumeroDecimal) : NumeroDecimal {
        si monto <= 0 entonces
            lanzar MontoInvalidoException
        fin si
        
        si saldo < monto entonces
            lanzar SaldoInsuficienteException
        fin si
        
        saldo = saldo - monto
        retornar saldo
    }
}
```

Este patron se aplica en todas las entidades: CuentaBancaria, Transaccion y UsuarioSesion. El encapsulamiento tambien protege la integridad de los datos. Por ejemplo, en la clase CuentaBancaria, el metodo `debitar()` verifica que el monto sea valido y que haya saldo suficiente antes de modificar el saldo.

### 3. Herencia

La herencia permite crear nuevas clases basadas en clases existentes, reutilizando codigo y estableciendo jerarquias de relacion. La clase hija hereda los atributos y metodos de la clase padre, y puede agregar nuevos o modificar los existentes.

En este proyecto, la herencia se aplica en la jerarquia de excepciones personalizadas. Todas las excepciones heredan de ATMException, que a su vez hereda de Exception de Java.

```java
// Clase base
clase ATMException extends Exception {
    constructor(mensaje : Texto) {
        llamar super(mensaje)
    }
}

// Excepciones especificas que heredan
clase SaldoInsuficienteException extends ATMException {
    constructor(saldoActual : NumeroDecimal, montoSolicitado : NumeroDecimal) {
        llamar super("Saldo insuficiente. Disponible: $" + saldoActual)
    }
}

clase PINIncorrectoException extends ATMException {
    constructor(intentosRestantes : NumeroEntero) {
        llamar super("PIN incorrecto. Intentos restantes: " + intentosRestantes)
    }
}
```

Gracias a esta jerarquia, el controlador puede capturar errores generales o especificos segun sea necesario.

### 4. Polimorfismo

El polimorfismo permite que objetos de diferentes clases respondan al mismo mensaje de forma diferente. Un metodo puede comportarse de manera distinta dependiendo del tipo de objeto que lo invoque.

En este proyecto, el polimorfismo se manifiesta en el **enum TipoTransaccion**, donde cada constante tiene su propia representacion en texto.

```java
enumeracion TipoTransaccion {
    CONSULTA_SALDO("Consulta de Saldo"),
    RETIRO("Retiro de Efectivo"),
    DEPOSITO("Deposito de Efectivo"),
    TRANSFERENCIA("Transferencia"),
    CAMBIO_PIN("Cambio de PIN");

    atributo privado descripcion : Texto

    metodo publico toString() : Texto {
        retornar descripcion
    }
}
```

Cada tipo de transaccion tiene su propia descripcion, pero todas comparten la misma interfaz. Esto permite que el sistema trabaje con cualquier tipo de transaccion de forma uniforme.

### 5. Composicion

La composicion es un tipo de relacion entre objetos donde una clase contiene instancias de otras clases como atributos. Es un pilar fundamental para construir sistemas complejos a partir de componentes mas simples.

En este proyecto, la composicion se aplica en varias clases:

```java
clase CajeroService {
    // Composicion: CajeroService "contiene" una coleccion de cuentas
    atributo privado cuentas : Mapa de CuentaBancaria
    atributo privado historialService : HistorialService
    atributo privado sesionActual : UsuarioSesion

    constructor() {
        cuentas = nuevo Mapa()
        historialService = nuevo HistorialService()
        sesionActual = nuevo UsuarioSesion()
    }
}
```

De manera similar, la clase Transaccion esta compuesta por un TipoTransaccion y una referencia a la cuenta asociada:

```java
clase Transaccion {
    // Transaccion "contiene" un TipoTransaccion
    atributo privado tipo : TipoTransaccion
    atributo privado monto : NumeroDecimal
    atributo privado fecha : FechaHora
    atributo privado exitosa : Logico
}
```

Esta relacion de composicion permite que un CajeroService tenga acceso a todas las cuentas, al historial de transacciones y a la sesion actual, creando un modelo rico y conectado.

---

## Logica del Sistema en Pseudocodigo

A continuacion se presenta la logica principal del sistema en pseudocodigo, facilitando la comprension del flujo de ejecucion sin necesidad de conocimientos avanzados de programacion.

### Flujo Principal del Sistema

```
INICIO
    CREAR instancia de CajeroService
    CREAR instancia de CajeroVistaGUI
    
    MOSTRAR ventana de login
    esperar que el usuario ingrese tarjeta y PIN
    
    SI autenticacion exitosa ENTONCES
        MOSTRAR pantalla principal
        EJECUTAR loop de operaciones
    SINO
        CERRAR aplicacion
    FIN SI
FIN
```

### Proceso de Autenticacion

```
PROCEDIMIENTO autenticar(numeroTarjeta, pinIngresado)
    // Buscar la cuenta en el sistema
    cuenta = buscarCuentaPorNumero(numeroTarjeta)
    
    SI cuenta no existe ENTONCES
        LANZAR CuentaNoEncontradaException
    FIN SI
    
    SI cuenta esta bloqueada ENTONCES
        LANZAR CuentaBloqueadaException
    FIN SI
    
    SI pinIngresado != cuenta.obtenerPin() ENTONCES
        cuenta.incrementarIntentosFallidos()
        
        SI intentosFallidos >= 3 ENTONCES
            cuenta.bloquear()
            LANZAR CuentaBloqueadaException
        SINO
            intentosRestantes = 3 - intentosFallidos
            LANZAR PINIncorrectoException(intentosRestantes)
        FIN SI
    FIN SI
    
    // PIN correcto
    cuenta.reiniciarIntentosFallidos()
    crearSesion(cuenta)
    RETORNAR VERDADERO
FIN PROCEDIMIENTO
```

### Consulta de Saldo

```
PROCEDIMIENTO consultarSaldo()
    SI no hay sesion activa ENTONCES
        LANZAR SesionNoAutenticadaException
    FIN SI
    
    cuenta = obtenerCuentaActual()
    saldo = cuenta.consultarSaldo()
    
    registrarTransaccion(TipoTransaccion.CONSULTA_SALDO, 0)
    
    RETORNAR saldo
FIN PROCEDIMIENTO
```

### Retiro de Efectivo

```
PROCEDIMIENTO retirarEfectivo(monto)
    SI no hay sesion activa ENTONCES
        LANZAR SesionNoAutenticadaException
    FIN SI
    
    SI monto <= 0 ENTONCES
        LANZAR MontoInvalidoException("El monto debe ser mayor a 0")
    FIN SI
    
    SI monto > 500000 ENTONCES
        LANZAR MontoInvalidoException("El monto maximo es $500,000")
    FIN SI
    
    cuenta = obtenerCuentaActual()
    saldoActual = cuenta.consultarSaldo()
    
    SI saldoActual < monto ENTONCES
        LANZAR SaldoInsuficienteException(saldoActual, monto)
    FIN SI
    
    // Realizar el debito
    nuevoSaldo = cuenta.debitar(monto)
    
    // Registrar la transaccion
    registrarTransaccion(TipoTransaccion.RETIRO, monto)
    
    RETORNAR nuevoSaldo
FIN PROCEDIMIENTO
```

### Deposito de Efectivo

```
PROCEDIMIENTO depositarEfectivo(monto)
    SI no hay sesion activa ENTONCES
        LANZAR SesionNoAutenticadaException
    FIN SI
    
    SI monto <= 0 ENTONCES
        LANZAR MontoInvalidoException("El monto debe ser mayor a 0")
    FIN SI
    
    SI monto < 10000 ENTONCES
        LANZAR MontoInvalidoException("El deposito minimo es $10,000")
    FIN SI
    
    cuenta = obtenerCuentaActual()
    nuevoSaldo = cuenta.acreditar(monto)
    
    registrarTransaccion(TipoTransaccion.DEPOSITO, monto)
    
    RETORNAR nuevoSaldo
FIN PROCEDIMIENTO
```

### Transferencia

```
PROCEDIMIENTO transferir(cuentaDestino, monto)
    SI no hay sesion activa ENTONCES
        LANZAR SesionNoAutenticadaException
    FIN SI
    
    SI monto <= 0 ENTONCES
        LANZAR MontoInvalidoException("El monto debe ser mayor a 0")
    FIN SI
    
    SI monto < 1000 ENTONCES
        LANZAR MontoInvalidoException("La transferencia minima es $1,000")
    FIN SI
    
    cuentaOrigen = obtenerCuentaActual()
    
    SI cuentaDestino == cuentaOrigen.obtenerNumero() ENTONCES
        LANZAR MontoInvalidoException("No puede transferir a su propia cuenta")
    FIN SI
    
    destino = buscarCuentaPorNumero(cuentaDestino)
    
    SI destino no existe ENTONCES
        LANZAR CuentaNoEncontradaException
    FIN SI
    
    // Verificar saldo
    saldoActual = cuentaOrigen.consultarSaldo()
    SI saldoActual < monto ENTONCES
        LANZAR SaldoInsuficienteException(saldoActual, monto)
    FIN SI
    
    // Realizar la transferencia
    cuentaOrigen.debitar(monto)
    destino.acreditar(monto)
    
    registrarTransaccion(TipoTransaccion.TRANSFERENCIA, monto)
    
    RETORNAR VERDADERO
FIN PROCEDIMIENTO
```

### Cambio de PIN

```
PROCEDIMIENTO cambiarPIN(pinActual, nuevoPin)
    SI no hay sesion activa ENTONCES
        LANZAR SesionNoAutenticadaException
    FIN SI
    
    cuenta = obtenerCuentaActual()
    
    SI pinActual != cuenta.obtenerPin() ENTONCES
        LANZAR PINIncorrectoException
    FIN SI
    
    SI nuevoPin tiene longitud != 4 ENTONCES
        LANZAR MontoInvalidoException("El PIN debe tener 4 digitos")
    FIN SI
    
    cuenta.establecerPin(nuevoPin)
    
    registrarTransaccion(TipoTransaccion.CAMBIO_PIN, 0)
    
    RETORNAR VERDADERO
FIN PROCEDIMIENTO
```

---

## Compilacion y Ejecucion

### Requisitos

- Java JDK 21 o superior
- Sin dependencias externas (solo biblioteca estandar de Java)

### Compilacion Manual

Desde la carpeta del proyecto, ejecutar los siguientes comandos:

```powershell
# Crear carpeta de salida
mkdir -Force out

# Compilar todos los archivos Java
javac -d out -sourcepath src src/main/Main.java
```

El flag `-sourcepath` es fundamental para que Java resuelva las referencias entre paquetes correctamente.

### Ejecucion

```powershell
java -cp out main.Main
```

Al ejecutar la aplicacion, se mostrara una ventana grafica con la pantalla de login.

### Cuentas de Prueba

El sistema incluye 4 cuentas de prueba precargadas:

| Numero de Cuenta | PIN | Titular |
|------------------|-----|---------|
| 1234567890123456 | 1234 | Juan Perez |
| 2345678901234567 | 5678 | Maria Garcia |
| 3456789012345678 | 9012 | Carlos Lopez |
| 4567890123456789 | 3456 | Ana Martinez |

---

## Casos de Uso y Manual de Usuario

### Caso de Uso 1: Iniciar Sesion

**Descripcion**: El usuario desea acceder al cajero automatico con su tarjeta y PIN.

**Pasos**:

1. Iniciar la aplicacion.
2. En la pantalla de login, ingresa el numero de tarjeta (16 digitos).
3. Ingresa el PIN de 4 digitos.
4. Hacer clic en "Acceder".

**Resultado esperado**: El sistema valida las credenciales y muestra el menu principal con las opciones disponibles.

**Errores esperados**:

- "Cuenta no encontrada": Si el numero de tarjeta no existe en el sistema.
- "PIN incorrecto": Si el PIN no coincide con el registrado.
- "Cuenta bloqueada": Si se superaron los 3 intentos fallidos.

### Caso de Uso 2: Consultar Saldo

**Descripcion**: El usuario desea conocer el saldo actual de su cuenta.

**Pasos**:

1. En el menu principal, hacer clic en "Consultar Saldo".
2. El sistema muestra el saldo actual formateado como moneda.

**Resultado esperado**: Se muestra el saldo disponible en la cuenta.

### Caso de Uso 3: Retirar Efectivo

**Descripcion**: El usuario desea retirar dinero de su cuenta.

**Pasos**:

1. En el menu principal, hacer clic en "Retirar Efectivo".
2. Ingresar el monto a retirar (puede usar formatos: 10000, 10,000, $10,000).
3. Hacer clic en "Confirmar Retiro".

**Resultado esperado**: Se realiza el debito de la cuenta y se muestra el nuevo saldo.

**Errores esperados**:

- "Monto invalido": Si el monto es menor o igual a 0 o mayor a $500,000.
- "Saldo insuficiente": Si no hay fondos suficientes para el retiro.

### Caso de Uso 4: Depositar Efectivo

**Descripcion**: El usuario desea depositar dinero en su cuenta.

**Pasos**:

1. En el menu principal, hacer clic en "Depositar Efectivo".
2. Ingresar el monto a depositar (minimo $10,000).
3. Hacer clic en "Confirmar Deposito".

**Resultado esperado**: Se acredita el monto a la cuenta y se muestra el nuevo saldo.

**Errores esperados**:

- "Monto invalido": Si el monto es menor a $10,000.

### Caso de Uso 5: Transferir Fondos

**Descripcion**: El usuario desea transferir dinero a otra cuenta.

**Pasos**:

1. En el menu principal, hacer clic en "Transferir".
2. Ingresar el numero de cuenta destino (16 digitos).
3. Ingresar el monto a transferir (minimo $1,000).
4. Hacer clic en "Confirmar Transferencia".

**Resultado esperado**: Se realiza la transferencia y se muestra confirmacion.

**Errores esperados**:

- "Cuenta destino no encontrada": Si la cuenta destino no existe.
- "No puede transferir a su propia cuenta": Si se intenta transferir a la misma cuenta.
- "Saldo insuficiente": Si no hay fondos suficientes.

### Caso de Uso 6: Cambiar PIN

**Descripcion**: El usuario desea actualizar su codigo de acceso.

**Pasos**:

1. En el menu principal, hacer clic en "Cambiar PIN".
2. Ingresar el PIN actual.
3. Ingresar el nuevo PIN (4 digitos).
4. Confirmar el nuevo PIN.
5. Hacer clic en "Confirmar Cambio".

**Resultado esperado**: Se actualiza el PIN de la cuenta.

**Errores esperados**:

- "PIN actual incorrecto": Si el PIN actual no coincide.
- "Los PINs no coinciden": Si la confirmacion no coincide con el nuevo PIN.

### Caso de Uso 7: Ver Historial

**Descripcion**: El usuario desea consultar todas las transacciones realizadas.

**Pasos**:

1. En el menu principal, hacer clic en "Ver Historial".
2. El sistema muestra la lista de transacciones ordenadas por fecha.

**Resultado esperado**: Se muestra el historial completo con tipo, monto y fecha de cada transaccion.

### Caso de Uso 8: Cerrar Sesion

**Descripcion**: El usuario desea salir del sistema de forma segura.

**Pasos**:

1. En el menu principal, hacer clic en "Cerrar Sesion".
2. El sistema cierra la sesion y vuelve a la pantalla de login.

**Resultado esperado**: La sesion se cierra y los datos se mantienen en memoria para la siguiente sesion.

---

## Notas Finales

Este proyecto demuestra la aplicacion practica de los conceptos fundamentales de la Programacion Orientada a Objetos en un escenario del mundo real. Cada decision de diseno refleja un principio de la POO: desde la abstraccion de las entidades bancarias hasta el manejo de errores con excepciones personalizadas, pasando por el encapsulamiento de datos y la composicion de objetos.

La arquitectura MVC separa responsabilidades de forma clara, permitiendo que la logica de negocio, la interfaz de usuario y la coordinacion de operaciones evolucionen de manera independiente. Esto facilita el mantenimiento del codigo y permite agregar nuevas funcionalidades sin modificar las existentes.

El uso de pseudocodigo en la seccion de logica busca hacer accesible la comprension del sistema a personas que no estan familiarizadas con la programacion, priorizando la logica del negocio sobre los detalles de implementacion.
