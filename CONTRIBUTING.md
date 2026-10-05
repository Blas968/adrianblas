# Guía para contribuir

¡Gracias por querer colaborar en **adrianblas**! Esta guía explica cómo trabajar en el proyecto para que todos lo hagamos de la misma forma: cómo crear ramas, cómo escribir commits y cómo abrir Pull Requests.

## Antes de empezar

1. Haz un **fork** del repositorio (o pide acceso si eres colaborador) y clónalo:

   ```bash
   git clone https://github.com/Blas968/adrianblas.git
   cd adrianblas
   ```

2. Comprueba que el proyecto funciona en tu equipo (necesitas JDK 11+ y Maven, mira el [README](README.md)):

   ```bash
   mvn clean test
   ```

## Cómo crear ramas

La rama **`main`** es la rama estable. **Nunca se trabaja directamente sobre ella**: todo cambio se hace en una rama propia y entra por Pull Request.

### Nombre de la rama

Usa el formato `tipo/descripcion-corta`, en minúsculas y con guiones:

| Prefijo | Cuándo usarlo | Ejemplo |
|---|---|---|
| `feature/` | Nueva funcionalidad | `feature/descuento-por-volumen` |
| `fix/` | Corregir un error | `fix/calculo-iva-reducido` |
| `docs/` | Solo documentación o JavaDoc | `docs/javadoc-pedido` |
| `test/` | Añadir o mejorar tests | `test/gestor-inventario` |
| `refactor/` | Mejorar el código sin cambiar su comportamiento | `refactor/calculadora-financiera` |

### Pasos

1. Ponte en `main` y actualízala:

   ```bash
   git checkout main
   git pull origin main
   ```

2. Crea tu rama y cámbiate a ella:

   ```bash
   git checkout -b feature/descuento-por-volumen
   ```

3. Trabaja en tu rama. Una rama = un cambio concreto. Si vas a hacer algo distinto, crea otra rama.

## Cómo hacer los commits

Haz commits **pequeños y frecuentes**, cada uno con un único propósito. Usamos el formato de [Conventional Commits](https://www.conventionalcommits.org/es/), en español:

```
tipo: descripción corta en imperativo
```

| Tipo | Uso |
|---|---|
| `feat` | Nueva funcionalidad |
| `fix` | Corrección de un error |
| `docs` | Documentación o JavaDoc |
| `test` | Tests nuevos o modificados |
| `refactor` | Cambio interno sin alterar el comportamiento |
| `chore` | Tareas de mantenimiento (configuración, `pom.xml`, etc.) |

### Reglas

- La primera línea tiene **máximo 72 caracteres**, sin punto final.
- Escríbela en **imperativo**: "añade", "corrige", "documenta" (no "añadido" ni "añadiendo").
- Explica el **qué** y el **porqué**, no el cómo. Si hace falta más detalle, déjalo en una línea en blanco y un párrafo debajo.
- Antes de cada commit, comprueba que los tests pasan: `mvn clean test`.

### Ejemplos

```
feat: añade descuento por volumen en CalculadoraFinanciera
fix: corrige el IVA reducido en aplicarIVA
docs: documenta con JavaDoc la clase Pedido
test: añade tests de reserva de stock en GestorInventario
chore: actualiza la versión de JaCoCo en el pom.xml
```

### Cómo se hace

```bash
git add src/main/java/com/proyecto/pedidos/model/CalculadoraFinanciera.java
git commit -m "feat: añade descuento por volumen en CalculadoraFinanciera"
```

Evita `git add .` sin mirar antes qué vas a subir (`git status`).

## Cómo hacer Pull Requests

1. **Sube tu rama** al repositorio:

   ```bash
   git push origin feature/descuento-por-volumen
   ```

2. **Abre el Pull Request** en GitHub desde tu rama hacia `main` (botón *Compare & pull request*).

3. **Ponle un título claro**, con el mismo estilo que los commits:

   ```
   feat: añade descuento por volumen en CalculadoraFinanciera
   ```

4. **Rellena la descripción** con esta plantilla:

   ```markdown
   ## Qué cambia
   Explicación breve de los cambios.

   ## Por qué
   Motivo del cambio o issue que resuelve.

   ## Cómo probarlo
   Pasos para comprobar que funciona (por ejemplo: `mvn clean test`).

   ## Checklist
   - [ ] El proyecto compila (`mvn clean compile`)
   - [ ] Los tests pasan (`mvn clean test`)
   - [ ] He añadido o actualizado tests si hacía falta
   - [ ] He documentado con JavaDoc las clases o métodos nuevos
   - [ ] No he subido archivos de `target/`, tokens ni contraseñas
   ```

5. **Espera la revisión.** Si te piden cambios, hazlos en la misma rama con nuevos commits y vuelve a hacer `git push`: el Pull Request se actualiza solo.

6. **Cuando se apruebe**, se fusiona en `main` y puedes borrar tu rama:

   ```bash
   git checkout main
   git pull origin main
   git branch -d feature/descuento-por-volumen
   ```

### Qué se revisa en un Pull Request

- Que el código compile y los tests pasen.
- Que el cambio haga una sola cosa y esté bien explicado.
- Que siga el estilo del proyecto (ver abajo).

## Estilo del código

- Java 11, codificación UTF-8.
- Las clases del modelo van en el paquete `com.proyecto.pedidos.model` y los tests en `com.proyecto.pedidos.test`.
- Nombres de clases en `PascalCase`, métodos y variables en `camelCase`.
- Documenta con JavaDoc las clases y métodos públicos.
- Todo código nuevo debería llevar su test con JUnit 5.

## Seguridad

**Nunca subas al repositorio** tokens, contraseñas ni claves (por ejemplo, el token de SonarQube). Pásalos por parámetro o variable de entorno. Si subes uno por error, revócalo y genera uno nuevo: borrarlo en un commit posterior no basta, porque queda en el historial de Git.

## ¿Dudas?

Abre un *issue* en el repositorio explicando tu duda o propuesta antes de empezar a trabajar en algo grande.


Este es mi granito de arena en este proyecto