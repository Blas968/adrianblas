package com.proyecto.pedidos.model;

public abstract class Producto {
    private String nombre;
    private double precio;

public Producto(String nombre, double precio) {
    if (precio < 0) {
        throw new IllegalArgumentException("El precio no puede ser negativo");
    }
    this.nombre = nombre;
    this.precio = precio;
}

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    // Método que cada subclase implementará
    public abstract double calcularPrecioFinal();

    @Override
    public String toString() {
        return String.format("%s (precio base: %.2f)", nombre, precio);
    }
}
