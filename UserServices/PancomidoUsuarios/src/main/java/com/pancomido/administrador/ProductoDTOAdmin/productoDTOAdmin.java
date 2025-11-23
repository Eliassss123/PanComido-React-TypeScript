package com.pancomido.administrador.ProductoDTOAdmin;


public class productoDTOAdmin {

    private Long id;
    private String nombre;
    private Double precio;

    // Constructor vacío necesario para deserialización JSON
    public productoDTOAdmin() {}

    // Constructor con todos los campos
    public productoDTOAdmin(Long id, String nombre, Double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "ProductoDTOAdmin{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                '}';
    }
}
