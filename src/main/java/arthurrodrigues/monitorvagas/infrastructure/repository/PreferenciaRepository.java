package arthurrodrigues.monitorvagas.infrastructure.repository;

import arthurrodrigues.monitorvagas.infrastructure.entity.Preferencia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenciaRepository extends JpaRepository<Preferencia, Long> {
}
