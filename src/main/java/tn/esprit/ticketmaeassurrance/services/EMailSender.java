package tn.esprit.ticketmaeassurrance.services;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service

public class EMailSender implements IMailSender {

    private final JavaMailSender mailSender;

    public EMailSender(JavaMailSender javaMailSender) {
        this.mailSender = javaMailSender;
    }
  public void sendMail(String to, String subject, String body) {
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
