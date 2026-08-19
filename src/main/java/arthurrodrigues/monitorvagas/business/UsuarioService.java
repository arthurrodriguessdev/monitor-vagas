package arthurrodrigues.monitorvagas.business;

import arthurrodrigues.monitorvagas.business.dtos.UsuarioDTO;
import arthurrodrigues.monitorvagas.infrastructure.converter.UsuarioConverter;
import arthurrodrigues.monitorvagas.infrastructure.entity.Usuario;
import arthurrodrigues.monitorvagas.infrastructure.repository.UsuarioRepository;
import arthurrodrigues.monitorvagas.infrastructure.security.TokenService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioConverter usuarioConverter;
    private final TokenService tokenService;

    public UsuarioDTO registrarUsuario(String nome, String email, String senha){
        if(usuarioRepository.findByEmail(email) != null){
            throw new IllegalArgumentException("Já existe um usuário criado com este e-mail");
        }

        senha = passwordEncoder.encode(senha);
        Usuario novoUsuario = Usuario.builder()
                .nome(nome)
                .senha(senha)
                .email(email).build();

        usuarioRepository.save(novoUsuario);
        return usuarioConverter.toUsuarioDTO(novoUsuario);
    }

    public String login(Usuario usuario){
        return tokenService.generateToken(usuario);
    }
}