package tools;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class EmailService {
    private final String username;
    private final String password;
    private final Properties props;

    public EmailService(String username, String password) {
        this.username = username;
        this.password = password;

        // Setup properties for SMTP server
        props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
    }

    public void sendEmail(String recipient, String subject, String body, File attachment) {
        // Create session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create MimeMessage
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);

            // Create Multipart
            Multipart multipart = new MimeMultipart();

            // Text part
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);

            // Image attachment part
            MimeBodyPart imagePart = new MimeBodyPart();
            imagePart.attachFile(attachment);

            // Add parts to Multipart
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(imagePart);

            // Set content of message to Multipart
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            System.out.println("Email sent successfully.");
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
        }
    }
}

