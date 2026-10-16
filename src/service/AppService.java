package service;

import java.util.List;

import db.SupabaseDB;
import models.Mail;
import network.MailProtocol;

public class AppService {
    private final SupabaseDB database = new SupabaseDB();
    private String currentUser;

    public String getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    // Đăng ký tài khoản mới.
    // Tham số: username, password.
    // Trả về: true nếu thành công.
    public boolean register(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
        }
        return database.registerUser(username.trim(), password);
    }

    // Đăng nhập và lưu phiên hiện tại.
    // Tham số: username, password.
    // Trả về: true nếu thành công.
    public boolean login(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.");
        }
        boolean ok = database.loginUser(username.trim(), password);
        currentUser = ok ? username.trim() : null;
        return ok;
    }

    public void logout() {
        currentUser = null;
    }

    // Gửi mail qua Gmail và lưu lịch sử vào DB.
    // Tham số: sender, appPassword, receiver, subject, content.
    // Trả về: true nếu gửi và lưu DB đều thành công; false nếu gửi được nhưng lưu DB thất bại.
    public boolean sendMail(String sender, String appPassword, String receiver, String subject, String content)
            throws Exception {
        if (!isLoggedIn()) {
            throw new IllegalStateException("Vui lòng đăng nhập trước khi gửi mail.");
        }
        if (isBlank(sender) || isBlank(appPassword) || isBlank(receiver)) {
            throw new IllegalArgumentException("Vui lòng nhập Gmail gửi, App Password và email nhận.");
        }

        Mail mail = new Mail(sender.trim(), receiver.trim(), nullToEmpty(subject), nullToEmpty(content));
        MailProtocol.sendEmailViaGmail(mail, appPassword);
        return database.saveMailToDb(mail);
    }

    // Đọc hộp thư đến qua POP3.
    // Tham số: gmail, appPassword.
    // Trả về: danh sách thư.
    public List<Mail> readMails(String gmail, String appPassword) throws Exception {
        if (isBlank(gmail) || isBlank(appPassword)) {
            throw new IllegalArgumentException("Vui lòng nhập Gmail và App Password.");
        }
        return MailProtocol.readEmails(gmail.trim(), appPassword);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
