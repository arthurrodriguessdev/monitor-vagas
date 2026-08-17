package arthurrodrigues.monitorvagas.infrastructure.repository;

import arthurrodrigues.monitorvagas.infrastructure.entity.PreferenciaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenciaUsuarioRepository extends JpaRepository<PreferenciaUsuario, Long> {
    PreferenciaUsuario findByEmail(String email);
}
