package arthurrodrigues.monitorvagas.presentation.controller;

import arthurrodrigues.monitorvagas.business.service.JobService;
import arthurrodrigues.monitorvagas.fixtures.dto.JobDTOFixture;
import arthurrodrigues.monitorvagas.infrastructure.exception.VagaNotFoundException;
import arthurrodrigues.monitorvagas.infrastructure.exception.handler.GlobalExceptionHandler;
import arthurrodrigues.monitorvagas.presentation.dto.JobDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class VagasControllerTest {
    @InjectMocks
    private VagasController vagasController;
    @Mock
    private JobService jobService;

    private List<String> listaPreferencias;
    private String jsonListaPreferencias;
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    private List<JobDTO> vagas;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        objectMapper = new ObjectMapper();
        listaPreferencias = new ArrayList<>();
        vagas = List.of(JobDTOFixture.build());
        mockMvc = MockMvcBuilders.standaloneSetup(vagasController)
                .alwaysDo(print())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();

        listaPreferencias.add("Java");
        listaPreferencias.add("Python");

        jsonListaPreferencias = objectMapper.writeValueAsString(listaPreferencias);
    }

    @Test
    @DisplayName("Deve pesquisar vagas com sucesso")
    void devePesquisarVagasComSucesso() throws Exception {
        when(jobService.pesquisarVagas(listaPreferencias))
                .thenReturn(vagas);

        pesquisarVagas().andExpect(status().isOk());
        verify(jobService).pesquisarVagas(listaPreferencias);
    }

    @Test
    @DisplayName("Deve retornar bad request quando não houver preferências")
    void devePesquisarVagasSemPreferencias() throws Exception {
        when(jobService.pesquisarVagas(any()))
                .thenThrow(new IllegalArgumentException("Informe pelo menos uma palavra para buscar vagas"));

        pesquisarVagas().andExpect(status().isBadRequest());
        verify(jobService, never()).getAllVagas();
    }

    @Test
    @DisplayName("Deve retornar not found quando nenhuma vaga for encontrada")
    void deveRetornarErroQuandoNenhumaVagaForEncontrada() throws Exception {
        when(jobService.pesquisarVagas(any()))
                .thenThrow(new VagaNotFoundException("Nenhuma vaga foi encontrada"));

        pesquisarVagas().andExpect(status().isNotFound());
        verify(jobService).pesquisarVagas(listaPreferencias);
    }

    ResultActions pesquisarVagas() throws Exception{
        return mockMvc.perform(post("/vagas/pesquisar")
                .header("Authorization", "tokenteste")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonListaPreferencias)
        );
    }
}
