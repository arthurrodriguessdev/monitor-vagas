package arthurrodrigues.monitorvagas.infrastructure.repository;

import arthurrodrigues.monitorvagas.infrastructure.entity.PreferenciaUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PreferenciaUsuarioRepository extends JpaRepository<PreferenciaUsuario, Long> {
    PreferenciaUsuario findByEmail(String email);

    @Query(value = """
        SELECT DISTINCT pu
        FROM PreferenciaUsuario pu
        LEFT JOIN FETCH pu.preferencias
        """)
    List<PreferenciaUsuario> findAllWithPreferences();
}