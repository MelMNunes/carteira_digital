package org.example.entidades;

// Importações necessárias
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity // Indica que esta classe é uma entidade JPA (será mapeada para uma tabela no banco de dados)
@Data // Anotação do Lombok para gerar automaticamente getters, setters, toString, etc.
public class Movimentacao {

    @Id // Indica que este campo é a chave primária da entidade
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Define a estratégia de geração automática do ID
    private Long id; // Identificador único da movimentação

    private String tipo; // Tipo de movimentação (por exemplo: compra, venda, transferência)

    private Double valor; // Valor da movimentação

    private LocalDateTime dataHora; // Data e hora da movimentação

    // Relacionamento muitos-para-um com CarteiraVirtual
    @ManyToOne
    @JoinColumn(name = "carteira_virtual_id") // Nome da coluna na tabela que faz referência à carteira virtual
    private CarteiraVirtual carteiraVirtual; // Carteira virtual à qual a movimentação está associada
}
