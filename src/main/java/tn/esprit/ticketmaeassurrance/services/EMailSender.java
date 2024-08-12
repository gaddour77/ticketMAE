package tn.esprit.ticketmaeassurrance.services;

import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@AllArgsConstructor
public class EMailSender implements IMailSender {

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;
    public void sendMail(String to, String subject, String body) {
      MimeMessage message = mailSender.createMimeMessage();
      MimeMessageHelper helper;
      try {
          Context context = new Context();
          context.setVariable("name", to);
          context.setVariable("message",body);

          // Generate email content from template
          String htmlContent = templateEngine.process("mail", context);
          helper = new MimeMessageHelper(message, true);
          helper.setTo(to);
          helper.setSubject(subject);
          helper.setText(htmlContent, true);
          mailSender.send(message);
      } catch (jakarta.mail.MessagingException e) {
          throw new RuntimeException(e);
      }
  }

    @Override
    public void sendEmail(String to, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
