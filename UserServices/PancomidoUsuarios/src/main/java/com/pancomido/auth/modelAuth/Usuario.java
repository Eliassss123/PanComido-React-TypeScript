package com.pancomido.auth.modelAuth;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    @Column(unique = true, nullable = false)
    private String correo;

    @Column(name = "contraseña")  // <-- NOMBRE REAL DE LA COLUMNA EN MYSQL
    private String contrasena;    // <-- NOMBRE DEL ATRIBUTO EN JAVA (sin ñ)

    
    @Column(nullable = false)
    private String rol = "CLIENTE";

}
