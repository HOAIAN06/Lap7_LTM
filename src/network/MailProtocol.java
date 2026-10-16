package network;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Store;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import models.Mail;

public class MailProtocol {
    // Gửi email qua máy chủ SMTP của Gmail.
    // Tham số: mail (thư cần gửi), gmailAppPassword (App Password của tài khoản mail.sender).
    // Trả về: void (ném Exception nếu gửi thất bại).
    public static void sendEmailViaGmail(Mail mail, String gmailAppPassword) throws Exception {
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
            transport.connect("smtp.gmail.com", mail.sender, gmailAppPassword);
            transport.sendMessage(message, message.getAllRecipients());
        }
    }

    // Đọc các email trong hộp thư đến Gmail bằng POP3S.
    // Tham số: gmailUsername (địa chỉ Gmail), gmailAppPassword (App Password của tài khoản).
    // Trả về: danh sách Mail (ném Exception nếu đọc thất bại).
    public static List<Mail> readEmails(String gmailUsername, String gmailAppPassword) throws Exception {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "pop3s");
        properties.put("mail.pop3s.host", "pop.gmail.com");
        properties.put("mail.pop3s.port", "995");
        properties.put("mail.pop3s.ssl.enable", "true");

        Store store = null;
        Folder inbox = null;
        List<Mail> mails = new ArrayList<>();

        try {
            Session session = Session.getInstance(properties);
            store = session.getStore("pop3s");
            store.connect("pop.gmail.com", gmailUsername, gmailAppPassword);

            inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.getMessages();
            for (Message message : messages) {
                String sender = InternetAddress.toString(message.getFrom());
                String subject = message.getSubject() == null ? "" : message.getSubject();
                String content = message.getContent() == null ? "" : message.getContent().toString();
                mails.add(new Mail(sender, gmailUsername, subject, content));
            }
            return mails;
        } finally {
            if (inbox != null && inbox.isOpen()) {
                inbox.close(false);
            }
            if (store != null && store.isConnected()) {
                store.close();
            }
        }
    }
}
