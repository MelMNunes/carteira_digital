package org.example.controllers;

// Importações necessárias
import org.example.entidades.Usuario;
import org.example.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // Define que esta classe é um controlador REST
@RequestMapping("/usuarios") // Define o mapeamento base para todas as URLs deste controlador
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService; // Injeção de dependência do serviço de usuário

    // Endpoint para cadastrar um novo usuário via POST (recebe um usuario no body (requestBody), e retorna uma resposta(responseEntity) sucesso ou erro)
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@RequestBody Usuario usuario) {
        try {
            Usuario usuarioCadastrado = usuarioService.cadastrarUsuario(usuario);
            return ResponseEntity.ok(usuarioCadastrado); // Retorna sucesso (código 200) com o usuário cadastrado
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // Retorna erro de requisição inválida (código 400)
        }
    }

    // Endpoint para buscar um usuário pelo ID via GET
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.buscarUsuarioPorId(id); // Busca o usuário pelo ID
        return usuario.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); // Retorna o usuário se encontrado, ou 404 se não encontrado
    }

    // Endpoint para deletar um usuário pelo ID via DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable Long id) {
        boolean deletado = usuarioService.deletarUsuario(id); // Tenta deletar o usuário pelo ID

        if (deletado) {
            return ResponseEntity.status(HttpStatus.OK).body("Usuário deletado com sucesso"); // Retorna sucesso (código 200) se deletado
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Não foi possível deletar esse usuário"); // Retorna 404 se o usuário não foi encontrado para deletar
        }
    }

    // Endpoint para buscar usuários com filtros via GET
    @GetMapping("/buscar")
    public ResponseEntity<List<Usuario>> buscarUsuarios(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String sobrenome,
            @RequestParam(required = false) String cpf,
            @RequestParam(required = false) String pais,
            @RequestParam(required = false) String dataDeNascimento) {
        List<Usuario> usuarios = usuarioService.buscarUsuarios(nome, sobrenome, cpf, pais, dataDeNascimento); // Busca usuários com base nos parâmetros fornecidos
        return ResponseEntity.ok(usuarios); // Retorna a lista de usuários encontrados (código 200)
    }

    // Endpoint para realizar depósito em uma carteira virtual específica via POST
    @PostMapping("/{id}/deposito")
    public ResponseEntity<String> depositar(@PathVariable Long id, @RequestParam double valor) {
        try {
            boolean sucesso = usuarioService.depositar(id, valor); // Tenta realizar o depósito
            if (sucesso) {
                return ResponseEntity.ok("Depósito realizado com sucesso."); // Retorna sucesso se o depósito foi realizado
            } else {
                return ResponseEntity.badRequest().body("Falha ao realizar depósito."); // Retorna erro de requisição inválida se houve falha no depósito
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // Retorna erro de requisição inválida se ocorreu uma exceção
        }
    }

    // Endpoint para realizar saque em uma carteira virtual específica via POST
    @PostMapping("/{id}/saque")
    public ResponseEntity<String> sacar(@PathVariable Long id, @RequestParam double valor) {
        try {
            boolean sucesso = usuarioService.sacar(id, valor); // Tenta realizar o saque
            if (sucesso) {
                return ResponseEntity.ok("Saque realizado com sucesso."); // Retorna sucesso se o saque foi realizado
            } else {
                return ResponseEntity.badRequest().body("Saldo insuficiente ou usuário não autorizado."); // Retorna erro de requisição inválida se houve falha no saque
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()); // Retorna erro de requisição inválida se ocorreu uma exceção
        }
    }

    // Endpoint para consultar o saldo de uma carteira virtual específica via GET
    @GetMapping("/{id}/saldo")
    public ResponseEntity<String> consultarSaldo(@PathVariable Long id) {
        Double saldo = usuarioService.consultarSaldo(id); // Consulta o saldo do usuário pelo ID
        String moeda = usuarioService.consultarMoeda(id); // Consulta a moeda da carteira virtual do usuário pelo ID
        String saldoFormatado = String.format("%s %.2f", moeda, saldo); // Formata o saldo com a moeda correspondente
        return ResponseEntity.ok(saldoFormatado); // Retorna o saldo formatado (código 200)
    }

    // Endpoint para buscar usuários pelo nome via GET
    @GetMapping("/buscarPorNome")
    public ResponseEntity<List<Usuario>> buscarUsuariosPorNome(@RequestParam String nome) {
        List<Usuario> usuarios = usuarioService.buscarUsuariosPorNome(nome); // Busca usuários pelo nome fornecido
        return ResponseEntity.ok(usuarios); // Retorna a lista de usuários encontrados (código 200)
    }
}
