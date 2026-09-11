package arthurrodrigues.monitorvagas.business.service;

import arthurrodrigues.monitorvagas.fixtures.dto.UsuarioDTOFixture;
import arthurrodrigues.monitorvagas.presentation.dto.UsuarioDTO;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Properties;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {
    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private TemplateEngine templateEngine;
    private MimeMessage mimeMessage;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setup(){
        Properties properties = new Properties();
        Session session = Session.getInstance(properties);
        mimeMessage = new MimeMessage(session);

        usuarioDTO = UsuarioDTOFixture.build();
    }

    @Test
    @DisplayName("Deve enviar o e-mail com sucesso")
    void deveEnviarEmailComSucesso(){
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(
                eq("notificacao"),
                any(Context.class))).thenReturn("<h1>Teste</h1>");

        emailService.enviarEmail(null, usuarioDTO, 0);
        verify(javaMailSender).createMimeMessage();
        verify(templateEngine).process(eq("notificacao"), any(Context.class));
        verify(javaMailSender).send(mimeMessage);
    }
}