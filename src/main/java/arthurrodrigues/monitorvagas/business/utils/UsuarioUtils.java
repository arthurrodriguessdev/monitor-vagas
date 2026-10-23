package arthurrodrigues.monitorvagas.business.utils;

import arthurrodrigues.monitorvagas.infrastructure.entity.Usuario;
import arthurrodrigues.monitorvagas.infrastructure.exception.UsuarioNotFound;
import arthurrodrigues.monitorvagas.infrastructure.repository.UsuarioRepository;
import arthurrodrigues.monitorvagas.infrastructure.security.TokenService;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class UsuarioUtils {
    private static TokenService tokenService;
    private static SecurityUtils securityUtils;
    private static UsuarioRepository usuarioRepository;

    public static Usuario getUsuario(String token){
        String emailUsuario = tokenService.getSubjectToken(
                SecurityUtils.tokenFormat(token), "access");

        Usuario usuario = (Usuario) usuarioRepository.findByEmail(emailUsuario);
        if(usuario == null){
            throw new UsuarioNotFound("Usuário não encontrado");
        }

        return usuario;
    }
}