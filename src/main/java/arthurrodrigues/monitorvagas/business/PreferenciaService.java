package arthurrodrigues.monitorvagas.business;

import arthurrodrigues.monitorvagas.business.dtos.PreferenciaDTO;
import arthurrodrigues.monitorvagas.infrastructure.converter.PreferenciaConverter;
import arthurrodrigues.monitorvagas.infrastructure.entity.Preferencia;
import arthurrodrigues.monitorvagas.infrastructure.entity.PreferenciaUsuario;
import arthurrodrigues.monitorvagas.infrastructure.repository.PreferenciaRepository;
import arthurrodrigues.monitorvagas.infrastructure.repository.PreferenciaUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreferenciaService {
    private final PreferenciaRepository preferenciaRepository;
    private final PreferenciaUsuarioRepository preferenciaUsuarioRepository;
    private final PreferenciaConverter preferenciaConverter;

    public PreferenciaDTO adicionarPreferencia(String nome, Long idPreferenciaUsuario){
        PreferenciaUsuario preferenciaUsuario = preferenciaUsuarioRepository
                .findById(idPreferenciaUsuario).orElseThrow(null);

        Preferencia preferencia = preferenciaRepository.save(new Preferencia(preferenciaUsuario, nome));
        return preferenciaConverter.toPreferenciaDTO(preferencia);
    }
}
