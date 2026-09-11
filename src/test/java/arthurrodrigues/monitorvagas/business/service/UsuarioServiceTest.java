package arthurrodrigues.monitorvagas.business.service;

import arthurrodrigues.monitorvagas.fixtures.dto.UsuarioDTOFixture;
import arthurrodrigues.monitorvagas.infrastructure.converter.UsuarioConverter;
import arthurrodrigues.monitorvagas.infrastructure.entity.Usuario;
import arthurrodrigues.monitorvagas.infrastructure.repository.UsuarioRepository;
import arthurrodrigues.monitorvagas.infrastructure.security.TokenService;
import arthurrodrigues.monitorvagas.presentation.dto.UsuarioDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {
    @InjectMocks
    private UsuarioService usuarioService;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UsuarioConverter usuarioConverter;
    @Mock
    private TokenService tokenService;

    private UsuarioDTO usuarioDTO;
    private Usuario usuario;

    @BeforeEach
    void setup(){
        usuarioDTO = UsuarioDTOFixture.build();

        usuario = new Usuario();
        usuario.setEmail("emailteste@gmail.com");
        usuario.setSenha("senhateste");
    }

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso")
    void deveSalvarUsuarioComSucesso(){
        when(usuarioRepository.findByEmail("emailteste@gmail.com")).thenReturn(null);
        when(passwordEncoder.encode("senhateste")).thenReturn("senhacriptografada");
        when(usuarioConverter.toUsuarioDTO(any(Usuario.class))).thenReturn(usuarioDTO);

        UsuarioDTO resultado = usuarioService.registrarUsuario(
                "Arthur",
                "emailteste@gmail.com",
                "senhateste"
        );

        assertEquals(usuarioDTO, resultado);

        verify(usuarioRepository).findByEmail("emailteste@gmail.com");
        verify(passwordEncoder).encode("senhateste");
        verify(usuarioRepository).save(any(Usuario.class));
        verify(usuarioConverter).toUsuarioDTO(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário já existir")
    void deveRetornarExcecaoUsuarioExistente(){
        // Neste caso, um usuário já existe com o e-mail
        when(usuarioRepository.findByEmail("emailteste@gmail.com")).thenReturn(usuario);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, ()->{
            usuarioService.registrarUsuario(
                    "Arthur",
                    "emailteste@gmail.com",
                    "senhateste"
            );
        });

        assertEquals("Já existe um usuário criado com este e-mail", exception.getMessage());
        verify(passwordEncoder, never()).encode("senhateste");
        verify(usuarioRepository, never()).save(usuario);
    }

    @Test
    @DisplayName("Deve realizar login e retornar o token")
    void deveLogarUsuario(){
        when(tokenService.generateToken(usuario)).thenReturn("tokengerado");

        String tokenGerado = usuarioService.login(usuario);
        assertEquals("tokengerado", tokenGerado);
        verify(tokenService).generateToken(usuario);
    }
}