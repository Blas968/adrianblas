# adrianblas — Sistema de gestión de pedidos

Proyecto en **Java 11** con **Maven** que modela el núcleo de una tienda: clientes, pedidos, productos (físicos y digitales), control de inventario y facturación. Incluye pruebas unitarias y de integración con **JUnit 5**, cobertura con **JaCoCo** y análisis de calidad con **SonarQube**.

## Qué hace

- **Clientes y pedidos**: un `Pedido` pertenece a un `Cliente` y agrupa varios productos. Calcula el total y genera un resumen.
- **Productos**: la clase abstracta `Producto` tiene dos tipos concretos, cada uno con su forma de calcular el precio final:
  - `ProductoFisico`: precio + 21 % de IVA + coste de envío.
  - `ProductoDigital`: precio con descuento y licencia, sin IVA.
- **Inventario** (`GestorInventario`): alta de productos, reserva de stock, confirmación de venta y detección de productos críticos (menos de 5 unidades).
- **Cálculos financieros** (`CalculadoraFinanciera`): IVA (general, reducido y súper reducido), descuento por fidelidad, gastos de envío (gratis a partir de 100 €) y comisión de pasarela de pago.
- **Facturación** (`ServicioFacturacion`): procesa una factura completa, valida su integridad y emite notas de crédito.

## Estructura del proyecto

```
adrianblas/
├── pom.xml                       # Configuración de Maven (Java 11, JUnit 5, JaCoCo, Sonar)
├── README.md
├── CONTRIBUTING.md
└── src/
    ├── main/java/com/proyecto/pedidos/
    │   ├── app.java/Main.java    # Punto de entrada (de momento, un "Hello World")
    │   └── model/                # Cliente, Pedido, Producto, ProductoFisico,
    │                             # ProductoDigital, GestorInventario,
    │                             # CalculadoraFinanciera, ServicioFacturacion
    └── test/java/com/proyecto/pedidos/test/
                                  # Tests unitarios y de integración (JUnit 5)
```

## Qué necesitas para ejecutarlo

| Herramienta | Versión | Para qué |
|---|---|---|
| JDK (Java Development Kit) | 11 o superior | Compilar y ejecutar |
| Apache Maven | 3.6 o superior | Compilar, probar y empaquetar |
| Git | cualquiera reciente | Clonar el repositorio |
| SonarQube *(opcional)* | Community, en `localhost:9000` | Solo si quieres análisis de calidad |

Conexión a internet la primera vez, para que Maven descargue las dependencias.

## Instalación

1. **Comprueba que tienes Java y Maven instalados:**

   ```bash
   java -version
   mvn -version
   ```

   Si alguno de los dos no aparece, instálalo antes de seguir (por ejemplo, un JDK de [Adoptium](https://adoptium.net) y Maven desde [maven.apache.org](https://maven.apache.org/download.cgi)).

2. **Clona el repositorio:**

   ```bash
   git clone https://github.com/Blas968/adrianblas.git
   ```

3. **Entra en la carpeta del proyecto:**

   ```bash
   cd adrianblas
   ```

## Puesta en marcha paso a paso

1. **Compila el proyecto.** Maven descarga las dependencias y genera las clases en `target/classes`:

   ```bash
   mvn clean compile
   ```

2. **Ejecuta los tests.** Lanza todas las pruebas de `src/test` y genera el informe de cobertura:

   ```bash
   mvn clean test
   ```

   Si todo va bien, verás `BUILD SUCCESS` al final.

3. **Mira el informe de cobertura (JaCoCo).** Abre en el navegador:

   ```
   target/site/jacoco/index.html
   ```

4. **Empaqueta el proyecto** en un `.jar`:

   ```bash
   mvn clean package
   ```

   El resultado queda en `target/adrianblas-1.0-SNAPSHOT.jar`.

5. **(Opcional) Análisis con SonarQube.** Con SonarQube arrancado en `http://localhost:9000` y un token generado desde tu usuario de Sonar, ejecuta:

   ```bash
   mvn clean verify sonar:sonar -Dsonar.token=TU_TOKEN
   ```

   Sustituye `TU_TOKEN` por tu token personal. **No lo subas nunca al repositorio.** El análisis se centra en el paquete `model` y usa el informe de JaCoCo para la cobertura.

## Notas

- `Main` es por ahora un programa de ejemplo que imprime `Hello World!!`. La lógica del proyecto vive en el paquete `model` y se comprueba a través de los tests.
- La carpeta `target/` se genera sola al compilar y está en el `.gitignore`: no se sube a Git.

## Contribuir

Si quieres colaborar, lee antes [CONTRIBUTING.md](CONTRIBUTING.md): explica cómo crear ramas, escribir commits y abrir Pull Requests.
