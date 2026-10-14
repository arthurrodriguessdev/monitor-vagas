package arthurrodrigues.monitorvagas.infrastructure.repository;

import arthurrodrigues.monitorvagas.infrastructure.entity.Preferencia;
import arthurrodrigues.monitorvagas.infrastructure.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreferenciaRepository extends JpaRepository<Preferencia, Long> {
    List<Preferencia> findByUsuario(Usuario usuario);
}
