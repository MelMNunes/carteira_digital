package org.example.services;

// Importações necessárias
import org.example.entidades.CarteiraVirtual;
import org.example.entidades.Movimentacao;
import org.example.repositories.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service // Indica que esta classe é um componente de serviço gerenciado pelo Spring
public class MovimentacaoService {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository; // Repositório para operações relacionadas a movimentações

    // Método para criar uma nova movimentação
    public Movimentacao criarMovimentacao(CarteiraVirtual carteiraVirtual, String tipo, Double valor) {
        Movimentacao movimentacao = new Movimentacao(); // Cria uma nova instância de Movimentacao

        // Configura os atributos da movimentação
        movimentacao.setCarteiraVirtual(carteiraVirtual); // Define a carteira virtual associada à movimentação
        movimentacao.setTipo(tipo); // Define o tipo de movimentação (ex: DEPOSITO, SAQUE)
        movimentacao.setValor(valor); // Define o valor da movimentação
        movimentacao.setDataHora(LocalDateTime.now()); // Define a data e hora atuais como dataHora da movimentação

        // Salva a movimentação no banco de dados através do repositório
        return movimentacaoRepository.save(movimentacao); // Retorna a movimentação salva
    }

}
