package network;

import java.util.Properties;

import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import models.Mail;

public class MailProtocol {
    // Tài khoản SMTP thật sự dùng để gửi (auth).
    private static final String SMTP_GMAIL = "lequocdinhkt@gmail.com";
    private static final String APP_PASSWORD = "fiad zfjg lnmz vxtp";

    // Gửi email qua SMTP.
    // From trên thư = mail.sender (Gmail người gửi nhập trên form).
    // Auth SMTP vẫn dùng tài khoản cố định trong code.
    // Tham số: mail (sender/receiver/subject/content).
    // Trả về: void (ném Exception nếu gửi thất bại).
    public static void sendEmailViaGmail(Mail mail) throws Exception {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mail.sender));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(mail.receiver));
        message.setSubject(mail.subject, "UTF-8");
        message.setText(mail.content, "UTF-8");

        try (Transport transport = session.getTransport("smtp")) {
            transport.connect("smtp.gmail.com", SMTP_GMAIL, APP_PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
        }
    }
}
