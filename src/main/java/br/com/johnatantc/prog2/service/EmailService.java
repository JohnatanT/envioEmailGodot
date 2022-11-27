package br.com.johnatantc.prog2.service;

import br.com.johnatantc.prog2.entity.Email;
import br.com.johnatantc.prog2.repository.EmailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.List;
import java.util.Properties;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Service
public class EmailService {

    @Autowired
    private EmailRepository repository;

    @Transactional
    public void enviarEmails() {
        List<Email> emails = repository.findByEnviado(FALSE);

        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.mailtrap.io");
        prop.put("mail.smtp.port", "2525");
        prop.put("mail.smtp.ssl.trust", "smtp.mailtrap.io");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("549370846d8c2b", "32f916d9002765");
            }
        });

        emails.forEach(email -> {
            try {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("from@gmail.com"));
                message.setRecipients(
                        Message.RecipientType.TO, InternetAddress.parse("to@gmail.com"));
                message.setSubject(email.getAssunto());

                String msg = email.getMensagem();

                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

                Multipart multipart = new MimeMultipart();
                multipart.addBodyPart(mimeBodyPart);

                message.setContent(multipart);

                Transport.send(message);
                email.setEnviado(TRUE);

                repository.save(email);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });


    }
}
