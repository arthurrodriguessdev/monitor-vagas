package arthurrodrigues.monitorvagas.business.service;

import arthurrodrigues.monitorvagas.business.utils.SecurityUtils;
import arthurrodrigues.monitorvagas.fixtures.dto.PreferenciaDTOFixture;
import arthurrodrigues.monitorvagas.fixtures.entity.PreferenciaFixture;
import arthurrodrigues.monitorvagas.fixtures.entity.UsuarioFixture;
import arthurrodrigues.monitorvagas.infrastructure.converter.PreferenciaConverter;
import arthurrodrigues.monitorvagas.infrastructure.entity.Preferencia;
import arthurrodrigues.monitorvagas.infrastructure.entity.Usuario;
import arthurrodrigues.monitorvagas.infrastructure.exception.UsuarioNotFound;
import arthurrodrigues.monitorvagas.infrastructure.repository.PreferenciaRepository;
import arthurrodrigues.monitorvagas.infrastructure.repository.UsuarioRepository;
import arthurrodrigues.monitorvagas.infrastructure.security.TokenService;
import arthurrodrigues.monitorvagas.presentation.dto.PreferenciaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PreferenciaServiceTest {
    @InjectMocks
    private PreferenciaService preferenciaService;
    @Mock
    private PreferenciaRepository preferenciaRepository;
    @Mock
    private PreferenciaConverter preferenciaConverter;
    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private TokenService tokenService;

    private Usuario usuario;
    private Preferencia preferencia;
    private String token;
    private PreferenciaDTO preferenciaDTO;

    @BeforeEach
    void setUp(){
        token = "tokenteste";
        usuario = UsuarioFixture.build();
        preferencia = PreferenciaFixture.buil();
        preferenciaDTO = PreferenciaDTOFixture.build();

        when(tokenService.getSubjectToken(SecurityUtils.tokenFormat(token)))
                .thenReturn(usuario.email);
    }

    @Test
    @DisplayName("Deve adicionar preferência com sucesso")
    void deveAdicionarPreferenciaComSucesso(){
        when(usuarioRepository.findByEmail(usuario.email))
                .thenReturn(usuario);
        when(preferenciaRepository.save(any(Preferencia.class)))
                .thenReturn(preferencia);
        when(preferenciaConverter.toPreferenciaDTO(preferencia))
                .thenReturn(preferenciaDTO);

        PreferenciaDTO resultado = preferenciaService.adicionarPreferencia(
                preferencia.nome,
                token
        );

        assertEquals(preferenciaDTO, resultado);
        verify(tokenService).getSubjectToken(SecurityUtils.tokenFormat(token));
        verify(usuarioRepository).findByEmail(usuario.email);
        verify(preferenciaRepository).save(any(Preferencia.class));
        verify(preferenciaConverter).toPreferenciaDTO(preferencia);
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não for encontrado")
    void deveLancarExcecaoUsuarioNaoForEncontrado(){
        when(usuarioRepository.findByEmail(usuario.email))
                .thenReturn(null);

        UsuarioNotFound exception = assertThrows(
                UsuarioNotFound.class, ()->{
                    preferenciaService.adicionarPreferencia(preferencia.nome, token);
                }
        );

        assertEquals("Usuário não encontrado", exception.getMessage());
        verify(tokenService).getSubjectToken(SecurityUtils.tokenFormat(token));
        verify(usuarioRepository).findByEmail(usuario.email);
        verify(preferenciaRepository, never()).save(any(Preferencia.class));
        verify(preferenciaConverter, never()).toPreferenciaDTO(preferencia);
    }
}