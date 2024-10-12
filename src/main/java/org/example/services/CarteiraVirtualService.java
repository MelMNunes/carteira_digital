package org.example.services;

// Importações necessárias
import org.example.entidades.CarteiraVirtual;
import org.example.repositories.CarteiraVirtualRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service // Indica que esta classe é um componente de serviço gerenciado pelo Spring
public class CarteiraVirtualService {

    @Autowired
    private CarteiraVirtualRepository carteiraVirtualRepository; // Repositório para operações relacionadas a carteiras virtuais

    // Método para buscar uma carteira virtual pelo ID (OPTIONAL= Evitar NPE (NullPointerException) usado para dizer que pode ter valores nulos)
    public Optional<CarteiraVirtual> buscarPorId(Long id) {
        return carteiraVirtualRepository.findById(id); // Utiliza o repositório para buscar a carteira virtual pelo ID
    }
}
