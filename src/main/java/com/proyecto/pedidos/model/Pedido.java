package com.proyecto.pedidos.model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private Cliente cliente;
    private List<Producto> productos;

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.productos = new ArrayList<>();
    }

    public void agregarProducto(Producto p) {
        if (p != null) productos.add(p);
    }

    public double calcularTotal() {
        double total = 0.0;
        for (Producto p : productos) {
            total += p.calcularPrecioFinal();
        }
        return total;
    }

    public String mostrarResumen() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Resumen del pedido ===\n");
        sb.append("Cliente: ").append(cliente.toString()).append("\n");
        sb.append("Productos:\n");
        for (Producto p : productos) {
            sb.append(" - ").append(p.toString()).append("\n");
        }
        sb.append(String.format("Total: %.2f\n", calcularTotal()));
        return sb.toString();
    }
}
