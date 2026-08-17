package arthurrodrigues.monitorvagas.business;

import arthurrodrigues.monitorvagas.business.dtos.PreferenciaUsuarioDTO;
import arthurrodrigues.monitorvagas.infrastructure.converter.PreferenciaUsuarioConverter;
import arthurrodrigues.monitorvagas.infrastructure.entity.PreferenciaUsuario;
import arthurrodrigues.monitorvagas.infrastructure.exceptions.EmailException;
import arthurrodrigues.monitorvagas.infrastructure.repository.PreferenciaUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PreferenciaUsuarioService {
    private final PreferenciaUsuarioRepository preferenciaUsuarioRepository;
    private final PreferenciaUsuarioConverter preferenciaUsuarioConverter;

    public PreferenciaUsuarioDTO adicionarPreferenciaUsuario(String email){
        if(preferenciaUsuarioRepository.findByEmail(email) != null){
            throw new EmailException("Já existe um cadastro com o e-mail informado");
        }

        PreferenciaUsuario preferenciaUsuario = preferenciaUsuarioRepository.save(new PreferenciaUsuario(email));
        return preferenciaUsuarioConverter.toPreferenciaUsuarioDTO(preferenciaUsuario);
    }
}
