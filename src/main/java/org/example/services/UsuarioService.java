package org.example.services;

// Importações necessárias
import org.example.entidades.CarteiraVirtual;
import org.example.entidades.Movimentacao;
import org.example.entidades.Usuario;
import org.example.repositories.CarteiraVirtualRepository;
import org.example.repositories.MovimentacaoRepository;
import org.example.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service // Indica que esta classe é um componente de serviço gerenciado pelo Spring
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository; // Repositório para operações relacionadas a usuários

    @Autowired
    private CarteiraVirtualRepository carteiraVirtualRepository; // Repositório para operações relacionadas a carteiras virtuais

    @Autowired
    private MovimentacaoRepository movimentacaoRepository; // Repositório para operações relacionadas a movimentações financeiras

    @Transactional // Indica que todos os métodos públicos desta classe devem ser transacionais
    public Usuario cadastrarUsuario(Usuario usuario) {
        // Verifica se o CPF já está cadastrado
        Optional<Usuario> usuarioExistente = usuarioRepository.findByCpf(usuario.getCpf());
        if (usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("O CPF não pode ser cadastrado mais de uma vez, tente novamente!");
        }

        // Verifica se o país é permitido para cadastro da carteira virtual
        String paisDeCadastro = usuario.getPaisDeCadastro().toUpperCase();
        if (!paisDeCadastro.equals("BR") && !paisDeCadastro.equals("DE") && !paisDeCadastro.equals("CH")) {
            throw new IllegalArgumentException("Essa carteira virtual só pode ser cadastrada nos países: Brasil (BR), Alemanha (DE) e Suíça (CH)");
        }

        // Define a moeda da carteira virtual baseado no país de cadastro
        String moeda;
        switch (paisDeCadastro) {
            case "BR":
                moeda = "R$";
                break;
            case "DE":
                moeda = "€";
                break;
            case "CH":
                moeda = "Fr";
                break;
            default:
                moeda = "R$"; // Moeda padrão para país não especificado
                break;
        }

        // Salva o usuário no banco de dados
        usuario = usuarioRepository.save(usuario);

        // Cria a carteira virtual para o usuário
        CarteiraVirtual carteiraVirtual = new CarteiraVirtual();
        carteiraVirtual.setUsuario(usuario);
        carteiraVirtual.setSaldo(0.0); // Saldo inicial
        carteiraVirtual.setMoeda(moeda); // Moeda conforme país de cadastro
        carteiraVirtualRepository.save(carteiraVirtual);

        // Adiciona a carteira virtual à lista de carteiras do usuário
        usuario.getCarteirasVirtuais().add(carteiraVirtual);

        return usuario;
    }

    public String consultarMoeda(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            CarteiraVirtual carteiraVirtual = carteiraVirtualRepository.findByUsuarioId(usuario.getId());
            if (carteiraVirtual != null) {
                return carteiraVirtual.getMoeda();
            }
        }
        return null;
    }

    // Calcula a idade com base na data de nascimento e na data atual
    private int calcularIdade(LocalDate dataDeNascimento) {
        LocalDate dataReferencia = LocalDate.of(2024, 6, 25);
        return Period.between(dataDeNascimento, dataReferencia).getYears();
    }

    @Transactional
    public boolean depositar(Long id, Double valor) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            if (calcularIdade(usuario.getDataDeNascimento()) < 18) {
                throw new IllegalArgumentException("Usuários menores de idade não podem realizar a ação.");
            }
            CarteiraVirtual carteiraVirtual = criarCarteiraVirtual(usuario);

            Movimentacao movimentacao = new Movimentacao();
            movimentacao.setTipo("DEPOSITO");
            movimentacao.setValor(valor);
            movimentacao.setDataHora(LocalDateTime.now());
            movimentacao.setCarteiraVirtual(carteiraVirtual);
            movimentacaoRepository.save(movimentacao);

            carteiraVirtual.setSaldo(carteiraVirtual.getSaldo() + valor);
            carteiraVirtualRepository.save(carteiraVirtual);
            return true;
        }
        return false; // Usuário não encontrado
    }

    @Transactional
    public boolean sacar(Long id, Double valor) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            if (calcularIdade(usuario.getDataDeNascimento()) < 18) {
                throw new IllegalArgumentException("Usuários menores de idade não podem realizar a ação.");
            }
            CarteiraVirtual carteiraVirtual = criarCarteiraVirtual(usuario);

            if (carteiraVirtual.getSaldo() >= valor) {
                Movimentacao movimentacao = new Movimentacao();
                movimentacao.setTipo("SAQUE");
                movimentacao.setValor(valor);
                movimentacao.setDataHora(LocalDateTime.now());
                movimentacao.setCarteiraVirtual(carteiraVirtual);
                movimentacaoRepository.save(movimentacao);

                carteiraVirtual.setSaldo(carteiraVirtual.getSaldo() - valor);
                carteiraVirtualRepository.save(carteiraVirtual);
                return true;
            } else {
                return false; // Saldo insuficiente
            }
        }
        return false; // Usuário não encontrado
    }

    public Double consultarSaldo(Long id) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(id);
        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();
            CarteiraVirtual carteiraVirtual = carteiraVirtualRepository.findByUsuarioId(usuario.getId());
            if (carteiraVirtual != null) {
                return carteiraVirtual.getSaldo();
            }
        }
        return null;
    }

    // Obtém a carteira virtual do usuário ou cria uma nova se não existir
    private CarteiraVirtual criarCarteiraVirtual(Usuario usuario) {
        CarteiraVirtual carteiraVirtual = carteiraVirtualRepository.findByUsuario(usuario);
        if (carteiraVirtual == null) {
            carteiraVirtual = new CarteiraVirtual();
            carteiraVirtual.setUsuario(usuario);
            carteiraVirtual.setSaldo(0.0); // Saldo inicial
            carteiraVirtual.setMoeda("BRL"); // Moeda padrão
            carteiraVirtualRepository.save(carteiraVirtual);
        }
        return carteiraVirtual;
    }

    // Busca usuários por nome, podendo filtrar pelo nome completo
    public List<Usuario> buscarUsuariosPorNome(String nome) {
        if (nome != null && !nome.isEmpty()) {
            return usuarioRepository.findByNomeContaining(nome);
        } else {
            return usuarioRepository.findAll();
        }
    }

    // Busca um usuário pelo ID
    public Optional<Usuario> buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    // Deleta um usuário pelo ID, se existir
    public boolean deletarUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    // Método para buscar usuários com filtros avançados (não implementado neste exemplo)
    public List<Usuario> buscarUsuarios(String nome, String sobrenome, String cpf, String pais, String dataDeNascimento) {
        // Implemente a lógica de busca com filtros (opcional) aqui
        return usuarioRepository.findAll();
    }
}
