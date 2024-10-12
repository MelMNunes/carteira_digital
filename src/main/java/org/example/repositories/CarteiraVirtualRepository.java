package org.example.repositories;

import org.example.entidades.CarteiraVirtual;
import org.example.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarteiraVirtualRepository extends JpaRepository<CarteiraVirtual, Long> {
    CarteiraVirtual findByUsuario(Usuario usuario);
    CarteiraVirtual findByUsuarioId(Long usuarioId);
}
