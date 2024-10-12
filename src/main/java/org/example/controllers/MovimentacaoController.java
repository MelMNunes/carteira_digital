package org.example.controllers;

// Importações necessárias
import org.example.entidades.CarteiraVirtual;
import org.example.entidades.Movimentacao;
import org.example.services.CarteiraVirtualService;
import org.example.services.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Define que esta classe é um controlador REST
@RequestMapping("/movimentacoes") // Define o mapeamento base para todas as URLs deste controlador
public class MovimentacaoController {

    @Autowired
    private MovimentacaoService movimentacaoService; // Injeção de dependência do serviço de movimentação

    @Autowired
    private CarteiraVirtualService carteiraVirtualService; // Injeção de dependência do serviço de carteira virtual

    // Endpoint para criar uma nova movimentação em uma carteira virtual específica via POST
    @PostMapping("/carteira/{carteiraId}")
    public ResponseEntity<Movimentacao> criarMovimentacao(
            @PathVariable Long carteiraId,
            @RequestParam String tipo,
            @RequestParam Double valor) {
        // Busca a carteira virtual pelo ID fornecido na URL
        CarteiraVirtual carteiraVirtual = carteiraVirtualService.buscarPorId(carteiraId)
                .orElseThrow(() -> new IllegalArgumentException("Carteira virtual não encontrada"));

        // Cria a movimentação na carteira virtual encontrada
        Movimentacao movimentacao = movimentacaoService.criarMovimentacao(carteiraVirtual, tipo, valor);

        // Retorna a movimentação criada como resposta de sucesso (código 200)
        return ResponseEntity.ok(movimentacao);
    }

}
