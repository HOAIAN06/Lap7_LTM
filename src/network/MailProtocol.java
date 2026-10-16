package network;

import java.util.Properties;

import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import models.Mail;

public class MailProtocol {
    private static final String GMAIL = "lequocdinhkt@gmail.com";
    private static final String APP_PASSWORD = "fiad zfjg lnmz vxtp";

    // Gửi email qua SMTP Gmail (tài khoản cố định trong code).
    // Tham số: mail (receiver/subject/content; sender trong mail là username app, không dùng làm From SMTP).
    // Trả về: void (ném Exception nếu gửi thất bại).
    public static void sendEmailViaGmail(Mail mail) throws Exception {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(GMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.receiver));
        message.setSubject(mail.subject, "UTF-8");
        message.setText(
                "Người gửi (username): " + mail.sender + "\n\n" + mail.content,
                "UTF-8"
        );

        try (Transport transport = session.getTransport("smtp")) {
            transport.connect("smtp.gmail.com", GMAIL, APP_PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
        }
    }
}
