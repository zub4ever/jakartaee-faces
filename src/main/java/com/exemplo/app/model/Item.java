package com.exemplo.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entidade de exemplo — serve só para provar que Faces + CDI + JPA + PostgreSQL
 * estão funcionando. Apague ou renomeie quando começar o seu domínio.
 */
@Entity
@Table(name = "item")
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Informe um título.")
    @Size(max = 120, message = "Máximo de 120 caracteres.")
    @Column(nullable = false, length = 120)
    private String titulo;

    @Column(name = "criado_em", nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @PrePersist
    void prePersist() {
        criadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }
}
