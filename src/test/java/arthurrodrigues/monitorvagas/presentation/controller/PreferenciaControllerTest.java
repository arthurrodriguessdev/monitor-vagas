package arthurrodrigues.monitorvagas.presentation.controller;

import arthurrodrigues.monitorvagas.business.service.PreferenciaService;
import arthurrodrigues.monitorvagas.fixtures.dto.PreferenciaDTOFixture;
import arthurrodrigues.monitorvagas.infrastructure.exception.UsuarioNotFound;
import arthurrodrigues.monitorvagas.infrastructure.exception.handler.GlobalExceptionHandler;
import arthurrodrigues.monitorvagas.presentation.dto.PreferenciaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class PreferenciaControllerTest {
    @Mock
    private PreferenciaService preferenciaService;
    @InjectMocks
    private PreferenciaController preferenciaController;

    private MockMvc mockMvc;
    private PreferenciaDTO preferenciaDTO;

    @BeforeEach
    void setUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(preferenciaController)
                .alwaysDo(print())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        preferenciaDTO = PreferenciaDTOFixture.build();
    }

    @Test
    @DisplayName("Deve adicionar preferência com sucesso")
    void deveAdicionarPreferenciaComSucesso() throws Exception {
        when(preferenciaService.adicionarPreferencia(any(), any()))
                .thenReturn(preferenciaDTO);

        adicionarPreferencia().andExpect(status().isOk());
        verify(preferenciaService).adicionarPreferencia(any(), any());
    }

    @Test
    @DisplayName("Deve retornar not found quando usuário não for encontrado")
    void deveRetornarNotFoundQuandoUsuarioNaoForEncontrado() throws Exception {
        when(preferenciaService.adicionarPreferencia(any(), any()))
                .thenThrow(new UsuarioNotFound("Usuário não encontrado"));

        adicionarPreferencia().andExpect(status().isNotFound());
        verify(preferenciaService).adicionarPreferencia(any(), any());
    }

    ResultActions adicionarPreferencia() throws Exception{
        return mockMvc.perform(post("/preferencia")
                .header("Authorization", "tokenteste")
                .param("nomePreferencia", "Java")
        );
    }
}