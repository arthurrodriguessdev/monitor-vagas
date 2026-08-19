package arthurrodrigues.monitorvagas.business;

import arthurrodrigues.monitorvagas.business.dtos.PreferenciaDTO;
import arthurrodrigues.monitorvagas.business.utils.SecurityUtils;
import arthurrodrigues.monitorvagas.infrastructure.converter.PreferenciaConverter;
import arthurrodrigues.monitorvagas.infrastructure.entity.Preferencia;
import arthurrodrigues.monitorvagas.infrastructure.entity.Usuario;
import arthurrodrigues.monitorvagas.infrastructure.repository.PreferenciaRepository;
import arthurrodrigues.monitorvagas.infrastructure.repository.UsuarioRepository;
import arthurrodrigues.monitorvagas.infrastructure.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreferenciaService {
    private final PreferenciaRepository preferenciaRepository;
    private final PreferenciaConverter preferenciaConverter;
    private final UsuarioRepository usuarioRepository;
    private final TokenService tokenService;

    public PreferenciaDTO adicionarPreferencia(String nome, String token){
        String emailUsuario = tokenService.getSubjectToken(
                SecurityUtils.tokenFormat(token));

        Usuario usuario = (Usuario) usuarioRepository.findByEmail(emailUsuario);
        Preferencia preferencia = preferenciaRepository.save(new Preferencia(usuario, nome));
        return preferenciaConverter.toPreferenciaDTO(preferencia);
    }
}
