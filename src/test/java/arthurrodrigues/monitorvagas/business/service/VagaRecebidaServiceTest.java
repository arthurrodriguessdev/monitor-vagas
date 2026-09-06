package arthurrodrigues.monitorvagas.business.service;

import arthurrodrigues.monitorvagas.fixtures.VagaRecebidaDTOFixture;
import arthurrodrigues.monitorvagas.infrastructure.converter.VagaRecebidaConverter;
import arthurrodrigues.monitorvagas.infrastructure.entity.Usuario;
import arthurrodrigues.monitorvagas.infrastructure.entity.VagaRecebida;
import arthurrodrigues.monitorvagas.infrastructure.exception.UsuarioNotFound;
import arthurrodrigues.monitorvagas.infrastructure.repository.UsuarioRepository;
import arthurrodrigues.monitorvagas.infrastructure.repository.VagaRecebidaRepository;
import arthurrodrigues.monitorvagas.presentation.dto.VagaRecebidaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class VagaRecebidaServiceTest {
    @InjectMocks
    private VagaRecebidaService vagaRecebidaService;
    @Mock
    private VagaRecebidaRepository vagaRecebidaRepository;
    @Mock
    private VagaRecebidaConverter vagaRecebidaConverter;
    @Mock
    private UsuarioRepository usuarioRepository;
    private Usuario usuario;
    private VagaRecebida vagaRecebida;
    private VagaRecebidaDTO vagaRecebidaDTO;

    @BeforeEach
    void setup(){
        usuario = new Usuario();
        vagaRecebida = new VagaRecebida();
        vagaRecebidaDTO = VagaRecebidaDTOFixture.build();
    }

    @Test
    @DisplayName("Deve registrar vaga recebida com sucesso")
    void deveRegistrarVagaRecebidaComSucesso(){
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(vagaRecebidaConverter.toVagaRecebida(
                vagaRecebidaDTO)).thenReturn(vagaRecebida);
        when(vagaRecebidaRepository.existsByUrlVagaAndUsuario(
                "URL Teste",
                usuario)).thenReturn(false);

        vagaRecebidaService.registrarVagaRecebida(vagaRecebidaDTO);

        verify(usuarioRepository).findById(1L);
        verify(vagaRecebidaConverter).toVagaRecebida(vagaRecebidaDTO);
        verify(vagaRecebidaRepository).existsByUrlVagaAndUsuario("URL Teste", usuario);
        verify(vagaRecebidaRepository).save(vagaRecebida);
    }

    @Test
    @DisplayName("Deve retornar a exceção de usuário não encontrado")
    void deveRetornarExcecaoUsuarioNaoEncontrado(){
        when(usuarioRepository.findById(1L)).thenReturn(Optional.empty());

        UsuarioNotFound exception = assertThrows(UsuarioNotFound.class, ()->{
            vagaRecebidaService.registrarVagaRecebida(vagaRecebidaDTO);
        });

        verify(usuarioRepository).findById(1L);
        assertEquals(
                "Usuário não encontrado",
                exception.getMessage()
        );

        // Exceção ocorre antes, não é para chamar estes métodos
        verify(vagaRecebidaConverter, never())
                .toVagaRecebida(any(VagaRecebidaDTO.class));
        verify(vagaRecebidaRepository, never())
                .existsByUrlVagaAndUsuario(any(), any());
        verify(vagaRecebidaRepository, never())
                .save(any(VagaRecebida.class));
    }
}