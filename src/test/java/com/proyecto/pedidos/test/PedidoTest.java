package com.proyecto.pedidos.test;

import com.proyecto.pedidos.model.Cliente;
import com.proyecto.pedidos.model.Pedido;
import com.proyecto.pedidos.model.ProductoDigital;
import com.proyecto.pedidos.model.ProductoFisico;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Batería de pruebas unitarias (JUnit 5) para las clases Pedido y Cliente.
 *
 * Reglas de cálculo que se deducen de los valores esperados en los tests:
 *  - ProductoFisico:  precio * 1.21 (21 % de IVA) + gastos de envío.
 *  - ProductoDigital: precio * (1 - descuento), sin IVA ni envío.
 *  - El total del pedido es la suma de los totales de sus productos.
 *
 * Nomenclatura de los casos de prueba:
 *  - TC-PE-XX -> casos de la clase Pedido.
 *  - TC-CL-XX -> casos de la clase Cliente.
 */
@DisplayName("Tests de Pedido y Cliente")
public class PedidoTest {

    // Cliente compartido por todos los tests; se recrea antes de cada uno
    private Cliente cliente;

    /**
     * Se ejecuta antes de CADA test. Crea un cliente nuevo para que
     * los tests sean independientes entre sí (un test que modifique el
     * cliente no afecta a los demás).
     */
    @BeforeEach
    void setUp() {
        cliente = new Cliente("Adrian Blas", "adrian@email.com", "Calle Mayor 1");
    }

    // ===================== PEDIDO =====================

    /**
     * Comprueba el total con un único producto físico.
     * Cálculo: 100 * 1.21 + 5 de envío = 126.
     */
    @Test
    @DisplayName("TC-PE-01: calcularTotal con un ProductoFisico")
    void testTotalConProductoFisico() {
        Pedido pedido = new Pedido(cliente);
        pedido.agregarProducto(new ProductoFisico("Teclado", 100.0, 5.0));
        // El tercer parámetro (0.001) es la tolerancia permitida al comparar doubles
        assertEquals(126.0, pedido.calcularTotal(), 0.001);
    }

    /**
     * Comprueba el total con un único producto digital con descuento.
     * Cálculo: 200 - 10 % de descuento = 180.
     */
    @Test
    @DisplayName("TC-PE-02: calcularTotal con un ProductoDigital")
    void testTotalConProductoDigital() {
        Pedido pedido = new Pedido(cliente);
        pedido.agregarProducto(new ProductoDigital("Software", 200.0, "LIC-1", 0.10));
        assertEquals(180.0, pedido.calcularTotal(), 0.001);
    }

    /**
     * Comprueba que el total suma correctamente productos de distinto tipo.
     * Cálculo: físico 50 * 1.21 = 60.5 + digital 100 (sin descuento) = 160.5.
     */
    @Test
    @DisplayName("TC-PE-03: calcularTotal con productos mixtos")
    void testTotalConProductosMixtos() {
        Pedido pedido = new Pedido(cliente);
        pedido.agregarProducto(new ProductoFisico("Ratón", 50.0, 0.0));
        pedido.agregarProducto(new ProductoDigital("App", 100.0, "A1", 0.0));
        assertEquals(160.5, pedido.calcularTotal(), 0.001);
    }

    /**
     * Caso límite: un pedido sin productos debe tener total 0.
     */
    @Test
    @DisplayName("TC-PE-04: Pedido vacío tiene total cero")
    void testTotalPedidoVacio() {
        Pedido pedido = new Pedido(cliente);
        assertEquals(0.0, pedido.calcularTotal(), 0.001);
    }

    /**
     * Caso límite: añadir un producto null no debe lanzar excepción
     * ni alterar el total del pedido.
     */
    @Test
    @DisplayName("TC-PE-05: agregarProducto null no rompe el pedido")
    void testAgregarProductoNullNoLanzaExcepcion() {
        Pedido pedido = new Pedido(cliente);
        // assertDoesNotThrow verifica que la llamada se ejecuta sin excepciones
        assertDoesNotThrow(() -> pedido.agregarProducto(null));
        // Además, el total debe seguir siendo 0 (el null no se ha contado)
        assertEquals(0.0, pedido.calcularTotal(), 0.001);
    }

    /**
     * Comprueba con assertNotEquals que un pedido con producto
     * no devuelve un total de 0.
     */
    @Test
    @DisplayName("TC-PE-06: Total con producto NO es igual a cero")
    void testTotalConProductoNoEsCero() {
        Pedido pedido = new Pedido(cliente);
        pedido.agregarProducto(new ProductoFisico("Monitor", 200.0, 15.0));
        assertNotEquals(0.0, pedido.calcularTotal());
    }

    /**
     * Invariante: el total nunca debe ser negativo, ni siquiera con
     * un descuento alto (50 %) en un producto digital.
     */
    @Test
    @DisplayName("TC-PE-07: Total no es negativo")
    void testTotalNuncaEsNegativo() {
        Pedido pedido = new Pedido(cliente);
        pedido.agregarProducto(new ProductoDigital("Tool", 30.0, "T1", 0.5));
        // assertFalse comprueba que la condición (total < 0) es falsa
        assertFalse(pedido.calcularTotal() < 0);
    }

    /**
     * Comprueba que el resumen del pedido incluye el nombre del cliente,
     * incluso si el pedido todavía no tiene productos.
     */
    @Test
    @DisplayName("TC-PE-08: mostrarResumen contiene el nombre del cliente")
    void testResumenContieneNombreCliente() {
        Pedido pedido = new Pedido(cliente);
        assertTrue(pedido.mostrarResumen().contains("Adrian Blas"));
    }

    /**
     * Comprueba que el resumen lista el nombre de los productos añadidos.
     */
    @Test
    @DisplayName("TC-PE-10: mostrarResumen contiene el nombre del producto")
    void testResumenContieneProducto() {
        Pedido pedido = new Pedido(cliente);
        pedido.agregarProducto(new ProductoFisico("Teclado", 100.0, 5.0));
        assertTrue(pedido.mostrarResumen().contains("Teclado"));
    }

    /**
     * Test parametrizado: ejecuta el mismo test con varios conjuntos de datos.
     * Cada fila de @CsvSource es una ejecución, con el formato:
     *   precioFísico, envíoFísico, precioDigital, descuentoDigital, totalEsperado
     *
     * Casos cubiertos:
     *  1) solo físico            2) solo digital
     *  3) físico + digital       4) digital con 50 % de descuento
     *  5) todo a cero (pedido sin productos)
     */
    @ParameterizedTest(name = "PrecioF={0}, EnvioF={1}, PrecioD={2}, DescD={3} => Total={4}")
    @CsvSource({
        "100.0, 5.0,  0.0,  0.0,  126.0",
        "0.0,   0.0, 200.0, 0.10, 180.0",
        "100.0, 5.0, 100.0, 0.0,  226.0",
        "50.0,  3.0,  50.0, 0.5,   88.5",
        "0.0,   0.0,   0.0, 0.0,    0.0"
    })
    @DisplayName("TC-PE-09: calcularTotal parametrizado")
    void testCalcularTotalParametrizado(double precioF, double envioF,
                                        double precioD, double descD,
                                        double esperado) {
        Pedido pedido = new Pedido(cliente);
        // Solo se añade el producto físico si tiene precio o envío
        if (precioF > 0 || envioF > 0)
            pedido.agregarProducto(new ProductoFisico("Físico", precioF, envioF));
        // Solo se añade el producto digital si tiene precio
        if (precioD > 0)
            pedido.agregarProducto(new ProductoDigital("Digital", precioD, "LIC", descD));
        assertEquals(esperado, pedido.calcularTotal(), 0.001);
    }

    // ===================== CLIENTE =====================

    /** Comprueba el getter del nombre (valor asignado en setUp). */
    @Test
    @DisplayName("TC-CL-01: Cliente devuelve nombre correcto")
    void testClienteNombre() {
        assertEquals("Adrian Blas", cliente.getNombre());
    }

    /** Comprueba el getter del correo electrónico. */
    @Test
    @DisplayName("TC-CL-02: Cliente devuelve correo correcto")
    void testClienteCorreo() {
        assertEquals("adrian@email.com", cliente.getCorreo());
    }

    /**
     * Comprueba que toString() incluye los tres datos del cliente:
     * nombre, correo y dirección.
     */
    @Test
    @DisplayName("TC-CL-03: toString de cliente contiene los tres campos")
    void testClienteToString() {
        String resultado = cliente.toString();
        assertTrue(resultado.contains("Adrian Blas"));
        assertTrue(resultado.contains("adrian@email.com"));
        assertTrue(resultado.contains("Calle Mayor 1"));
    }

    /**
     * Comprueba que setNombre modifica el valor: el nombre antiguo
     * ya no debe coincidir y el nuevo sí.
     */
    @Test
    @DisplayName("TC-CL-04: setNombre actualiza el nombre")
    void testSetNombre() {
        cliente.setNombre("Nuevo Nombre");
        assertNotEquals("Adrian Blas", cliente.getNombre());
        assertEquals("Nuevo Nombre", cliente.getNombre());
    }

    /** Comprueba que setCorreo actualiza el correo electrónico. */
    @Test
    @DisplayName("TC-CL-05: setCorreo actualiza el correo")
    void testSetCorreo() {
        cliente.setCorreo("nuevo@email.com");
        assertEquals("nuevo@email.com", cliente.getCorreo());
    }

    /** Comprueba que setDireccion actualiza la dirección. */
    @Test
    @DisplayName("TC-CL-06: setDireccion actualiza la dirección")
    void testSetDireccion() {
        cliente.setDireccion("Calle Nueva 99");
        assertEquals("Calle Nueva 99", cliente.getDireccion());
    }

    /** Comprueba el getter de la dirección (valor asignado en setUp). */
    @Test
    @DisplayName("TC-CL-07: getDireccion devuelve la dirección correcta")
    void testGetDireccion() {
        assertEquals("Calle Mayor 1", cliente.getDireccion());
    }
}