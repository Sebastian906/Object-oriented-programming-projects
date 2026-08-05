# AGENTS.md - Proyectos OOP

## Repository Structure

Multi-project Java OOP learning repository. 10 independent projects, numbered 1-10. **Only `1-sistema-biblioteca` has code** — others are empty placeholders.

```
proyectos-oop/
├── 1-sistema-biblioteca/    ← Active project with code
├── 2-simulador-atm/         ← Empty
├── 3-gestion-tienda/        ← Empty
└── ...10-motor-busqueda/
```

## Project 1: Sistema Biblioteca

**Stack:** Java 21, Swing GUI, no build system (manual javac compilation)

**Package structure** (inside `src/`):
- `main/` — Entry point (`Main.java`)
- `models/` — Domain entities (Biblioteca, Libro, Usuario, Prestamo, EstadoPrestamo)
- `view/` — UI layer (BibliotecaVista console, BibliotecaVistaGUI Swing)
- `controller/` — Business logic coordination (BibliotecaController)
- `service/` — Utilities (GeneradorIdService, ValidadorService)
- `exceptions/` — Custom exception hierarchy
- `utils/` — Helper classes (ConsoleUtil, FormatUtil)

**Architecture:** MVC pattern. Main → JOptionPane mode selector → Console or Swing mode.

## Build & Run

**No Maven/Gradle.** Manual compilation required.

```powershell
# Compile (from repo root)
javac -d out -sourcepath 1-sistema-biblioteca/src 1-sistema-biblioteca/src/main/Main.java

# Run
java -cp out main.Main
```

**Important:** `-sourcepath` must point to the `src/` directory for package resolution to work.

## Conventions

- **Language:** All code, comments, Javadoc, and variable names in Spanish
- **Architecture:** Strict MVC separation — models don't import view/controller
- **No external dependencies:** Standard library only (java.util, java.time, javax.swing)
- **No test framework:** No JUnit or test files present

## Common Pitfalls

1. **Package mismatch:** Java files use `package models;` etc. but live in `src/models/`. The `-sourcepath` flag is critical.
2. **Swing threading:** GUI must run on EDT. `BibliotecaVistaGUI` uses `SwingUtilities.invokeLater()`.
3. **Empty projects:** Don't assume code exists in projects 2-10.
