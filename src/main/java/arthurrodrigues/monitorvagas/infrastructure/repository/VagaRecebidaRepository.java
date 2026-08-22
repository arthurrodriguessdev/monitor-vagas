package arthurrodrigues.monitorvagas.infrastructure.repository;

import arthurrodrigues.monitorvagas.infrastructure.entity.Usuario;
import arthurrodrigues.monitorvagas.infrastructure.entity.VagaRecebida;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VagaRecebidaRepository extends JpaRepository<VagaRecebida, Long> {
    boolean existsByUrlVagaAndUsuario(String urlVaga, Usuario usuario);
}