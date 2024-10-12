package org.example.repositories;

// Importações necessárias
import org.example.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository // Indica que esta interface é um repositório gerenciado pelo Spring
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Declaração de métodos de consulta personalizados

    List<Usuario> findByNomeContaining(String nome);
    // Método para buscar usuários cujo nome contenha a sequência fornecida.
    // É uma consulta personalizada do Spring Data JPA.
    // Gera uma consulta SQL equivalente a "SELECT * FROM Usuario WHERE nome LIKE %nome%".

    Optional<Usuario> findByCpf(String cpf);
    // Método para buscar um usuário pelo CPF.
    // Retorna um Optional<Usuario> para indicar a possibilidade de o usuário não existir.
    // É uma consulta personalizada do Spring Data JPA.
    // Gera uma consulta SQL equivalente a "SELECT * FROM Usuario WHERE cpf = cpf".
}
