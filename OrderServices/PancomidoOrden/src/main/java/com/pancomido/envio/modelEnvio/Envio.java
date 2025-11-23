package com.pancomido.envio.modelEnvio;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "envio")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String ciudad;

    @Column(nullable = false)
    private String estado; 

    @ManyToOne
    @JoinColumn(name = "run_cliente", referencedColumnName = "run", nullable = true)
    @JsonIgnoreProperties("envios")  // O el nombre del atributo que tenga la relación inversa
    private Cliente cliente;
}
