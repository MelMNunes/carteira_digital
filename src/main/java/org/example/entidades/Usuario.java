package org.example.entidades;

// Importações necessárias
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity // Indica que esta classe é uma entidade JPA (será mapeada para uma tabela no banco de dados)
@Data // Anotação do Lombok para gerar automaticamente getters, setters, toString, etc.
public class Usuario {

    @Id // Indica que este campo é a chave primária da entidade
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Define a estratégia de geração automática do ID
    private Long id; // Identificador único do usuário

    private String nome; // Nome do usuário
    private String sobrenome; // Sobrenome do usuário
    private String cpf; // CPF do usuário
    private String paisDeCadastro; // País de cadastro do usuário
    private LocalDate dataDeNascimento; // Data de nascimento do usuário
    private LocalDateTime dataHoraCadastro; // Data e hora do cadastro do usuário

    // Relacionamento um-para-muitos com CarteiraVirtual
    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JsonManagedReference // Usado para controlar a serialização JSON e evitar referências cíclicas
    private List<CarteiraVirtual> carteirasVirtuais = new ArrayList<>(); // Lista de carteiras virtuais associadas ao usuário

    // Método executado antes da persistência para definir a data e hora do cadastro
    @PrePersist
    public void horaEDataCadastro() {
        dataHoraCadastro = LocalDateTime.now(); // Define a data e hora do cadastro como o momento atual
    }
}
