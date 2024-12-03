package ucb.CaresyncApp.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarEmail(String destinatario, String codigo, String nomePaciente) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("caresync.sup@gmail.com");
            helper.setSubject("Recuperação de senha - CARESYNC");
            helper.setTo(destinatario);
            String template = carregarTemplateEmail();
            template = template.replace("#codigo", codigo);
            template = template.replace("#nomepaciente", nomePaciente);
            helper.setText(template, true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String carregarTemplateEmail() throws IOException {
        ClassPathResource resource = new ClassPathResource("email-template.html");
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

}
