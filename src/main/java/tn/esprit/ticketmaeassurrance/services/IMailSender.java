package tn.esprit.ticketmaeassurrance.services;

public interface IMailSender {
    void sendEmail(String to, String subject, String body);
}
