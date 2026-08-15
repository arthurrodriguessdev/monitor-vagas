package arthurrodrigues.monitorvagas.business;

import arthurrodrigues.monitorvagas.business.dtos.JobDTO;
import arthurrodrigues.monitorvagas.infrastructure.exceptions.EmailException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}")
    private String remetente;
    @Value("${spring.mail.nomeRemetente}")
    private String nomeRemetente;

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public void enviarEmail(List<JobDTO> listaVagas, int quantidadeVagas){
        try{
            MimeMessage mensagem = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(
                    mensagem, true, StandardCharsets.UTF_8.name());

            mimeMessageHelper.setFrom(new InternetAddress(remetente, nomeRemetente));
            mimeMessageHelper.setTo(InternetAddress.parse("canaldotu050@gmail.com"));
            mimeMessageHelper.setSubject("Vagas de Emprego");

            // Setando variáveis do template
            Context context = new Context();
            context.setVariable("vagas", listaVagas);
            context.setVariable("quantidadeVagas", quantidadeVagas);

            String template = templateEngine.process("notificacao", context);
            mimeMessageHelper.setText(template, true);
            javaMailSender.send(mensagem);
            log.info("E-mail enviado com sucesso");

        } catch (MessagingException e) {
            throw new EmailException("Erro ao montar a mensagem de e-mail.", e);
        } catch (UnsupportedEncodingException e) {
            throw new EmailException("Erro na codificação.", e);
        } catch (Exception e) {
            throw new EmailException("Erro ao enviar o e-mail.", e);
        }
    }
}