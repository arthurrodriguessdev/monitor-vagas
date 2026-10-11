package arthurrodrigues.monitorvagas.presentation.controller;

import arthurrodrigues.monitorvagas.business.service.UsuarioService;
import arthurrodrigues.monitorvagas.fixtures.dto.LoginDTOFixture;
import arthurrodrigues.monitorvagas.fixtures.entity.UsuarioFixture;
import arthurrodrigues.monitorvagas.infrastructure.entity.Usuario;
import arthurrodrigues.monitorvagas.presentation.dto.login.LoginDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthenticationControllerTest {
    @InjectMocks
    private AuthenticationController authenticationController;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private AuthenticationManager authenticationManager;

    private ObjectMapper objectMapper;
    private MockMvc mockMvc;
    private LoginDTO loginDTO;
    private String jsonLogin;
    private String url;
    private Usuario usuario;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                .alwaysDo(print()).build();

        objectMapper = new ObjectMapper();
        loginDTO = LoginDTOFixture.build();
        jsonLogin = objectMapper.writeValueAsString(loginDTO); // Passando os campos para json
        usuario = UsuarioFixture.build();
    }

    @Test
    void deveLogarUsuarioComSucesso() throws Exception {
        var auth = new UsernamePasswordAuthenticationToken(usuario, null);
        when(authenticationManager.authenticate(any()))
                .thenReturn(auth);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(jsonLogin)
        ).andExpect(status().isOk());

        verify(usuarioService).login(usuario);
    }

    @Test
    void deveRegistrarUsuarioComSucesso() throws Exception {
        mockMvc.perform(post("/auth/register")
                .param("nome", "Nome Teste")
                .param("email", "emailteste@gmail.com")
                .param("senha", "senhateste")
        ).andExpect(status().isOk());

        verify(usuarioService).registrarUsuario(
                "Nome Teste",
                "emailteste@gmail.com",
                "senhateste"
        );
    }
}