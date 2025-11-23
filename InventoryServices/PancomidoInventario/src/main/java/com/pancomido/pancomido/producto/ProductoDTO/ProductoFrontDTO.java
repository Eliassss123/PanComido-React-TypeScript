package com.pancomido.pancomido.producto.ProductoDTO;

public class ProductoFrontDTO {
    private Long id;
    private String titulo;
    private String url;
    private Integer precio;
    private String categoria;

    public ProductoFrontDTO() {}

    public ProductoFrontDTO(Long id, String titulo, String url, Integer precio, String categoria) {
        this.id = id;
        this.titulo = titulo;
        this.url = url;
        this.precio = precio;
        this.categoria = categoria;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Integer getPrecio() { return precio; }
    public void setPrecio(Integer precio) { this.precio = precio; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
}
