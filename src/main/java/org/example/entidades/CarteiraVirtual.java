package org.example.entidades;

// Importações necessárias
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import java.util.List;

@Entity // Indica que esta classe é uma entidade JPA (será mapeada para uma tabela no banco de dados)
@Data // Anotação do Lombok para gerar automaticamente getters, setters, toString, etc.
public class CarteiraVirtual {

    @Id // Indica que este campo é a chave primária da entidade
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Define a estratégia de geração automática do ID
    private Long id; // Identificador único da carteira virtual

    private String moeda; // Tipo de moeda da carteira virtual
    private Double saldo; // Saldo da carteira virtual

    // Relacionamento muitos-para-um com Usuario
    @ManyToOne
    @JoinColumn(name = "usuario_id") // Nome da coluna na tabela que faz referência ao usuário
    @JsonBackReference // Usado para controlar a serialização JSON e evitar referências cíclicas
    private Usuario usuario; // Usuário proprietário da carteira virtual

    // Relacionamento um-para-muitos com Movimentacao
    @OneToMany(mappedBy = "carteiraVirtual", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Movimentacao> movimentacoes; // Lista de movimentações associadas à carteira virtual
}
