package by.rozze.weblab3.mail;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.logging.Logger;

@ApplicationScoped
public class MailService {
    private static final String HOST = "smtp.mail.ru";
    private static final String PORT = "587";
    private static final String FROM = "***@mail.ru";
    private static final String LOGIN = "***@mail.ru";
    private static final String PASSWORD = "password";

    private static final Logger LOG = Logger.getLogger(MailService.class.getName());

    public void sendReport(String email, Long pointsCount, String time) {
        String topic = "Количество проверенных точек";
        String text = """
        
        На момент планирования отчета было проверено %d точек на сайте
        
        Время отчета: %s
        
        """.formatted(pointsCount, time);

        sendEmail(email, topic, text);
    }

    private void sendEmail(String to, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(LOGIN, PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);
            Transport.send(message);
            LOG.info("Статистика отправлена на почту");
        } catch (MessagingException e) {
            LOG.severe("Ошибка отправки email: " + e.getMessage());
        }
    }
}