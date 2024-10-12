package org.example.repositories;

import org.example.entidades.CarteiraVirtual;
import org.example.entidades.Movimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {
    void deleteAllByCarteiraVirtual(CarteiraVirtual carteiraVirtual);
}

